package com.child.common.aspect;

import com.child.common.constants.Constant;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.redis.RedisComponent;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class GlobalOperationAspect {

    @Resource
    private RedisComponent redisComponent;

    @Pointcut("@annotation(com.child.common.annotation.GlobalInterceptor)")
    public void loginCheckPointcut(){}

    @Before("loginCheckPointcut()")
    private void beforeLoginCheck(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null){
            throw new BusinessException(ResponseCodeEnum.CODE_401);
        }
        HttpServletRequest request = attributes.getRequest();

        String token =request.getHeader(Constant.TOKEN_HEADER_KEY);
        if (token == null || token.isEmpty()){
            token = request.getParameter(Constant.TOKEN_HEADER_KEY);
        }
        if (token == null || token.isEmpty()){
            throw new BusinessException(ResponseCodeEnum.CODE_401);
        }
        String phoneNumber = redisComponent.getPhoneNumberByToken(token);
        if (phoneNumber == null || phoneNumber.isEmpty()){
            throw new BusinessException(ResponseCodeEnum.CODE_401);
        }
    }
}
