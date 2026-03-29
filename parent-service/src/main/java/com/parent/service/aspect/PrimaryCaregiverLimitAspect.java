package com.parent.service.aspect;

import com.child.common.entity.po.Member;
import com.child.common.exception.BusinessException;
import com.parent.service.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PrimaryCaregiverLimitAspect {
    @Resource
    private UserMapper userMapper;
    @Around("@annotation(com.parent.service.annotation.CheckPrimaryCaregiverLimit)")
    public Object checkLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        Integer role = null;
        String familyId = null;
        if ("inviteMember".equals(methodName)) {
            familyId = (String) args[1];
            role = (Integer) args[3];
        }
        else if ("changeRole".equals(methodName)) {
            String phone = (String) args[0];
            role = (Integer) args[1];
            Member member = userMapper.selectMemberByPhone(phone);
            if (member == null) {
                throw new BusinessException("成员不存在");
            }
            familyId = member.getFamilyId();
        }
        if (Integer.valueOf(1).equals(role)) {
            int count = userMapper.countPrimaryByFamilyId(familyId);
            if (count >= 2) {
                throw new BusinessException("一个家庭最多只能有 2 名主力照料者");
            }
        }

        return joinPoint.proceed();
    }
}
