package com.child.common.aspect;

import com.child.common.annotation.RequirePrimaryCaregiver;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.redis.RedisComponent;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
public class PrimaryCaregiverAspect {

    @Resource
    private RedisComponent redisComponent;


    @Around("@annotation(com.child.common.annotation.RequirePrimaryCaregiver)")
    public Object checkPrimaryCaregiver(ProceedingJoinPoint joinPoint)throws Throwable{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        if (token == null || token.isBlank()) {
            throw new BusinessException(ResponseCodeEnum.CODE_401);
        }
        String role = redisComponent.get("user:role:" + token);
        if (role == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_401);
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequirePrimaryCaregiver anno = signature.getMethod().getAnnotation(RequirePrimaryCaregiver.class);
        if (!role.equals("1")){
            throw new BusinessException(anno.message());
        }
        return joinPoint.proceed();
    }
}
