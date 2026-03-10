package com.child.common.redis;

import com.child.common.constants.Constant;
import com.child.common.utils.TokenTools;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisComponent {

    @Resource
    private RedisUtils redisUtils;

    public String saveUserLoginToken(String token, String phoneNumber){
        redisUtils.setEx(Constant.REDIS_TOKEN_KEY+token,phoneNumber,Constant.TOKEN_EXPIRE_HOURS);
        return token;
    }

    public String getPhoneNumberByToken(String token){
        if (token == null || token.isEmpty()){
            return null;
        }
        String redisKey = Constant.REDIS_TOKEN_KEY+token;
        String phoneNumber = redisUtils.get(redisKey);
        return phoneNumber;
    }

    public String refreshToken(String phoneNumber){
        String newToken = TokenTools.getToken();
        redisUtils.setEx(Constant.REDIS_TOKEN_KEY+phoneNumber,newToken,Constant.TOKEN_EXPIRE_HOURS);
        return newToken;
    }

    public Boolean validateToken(String phoneNumber,String token){
        String redisToken = redisUtils.get(Constant.REDIS_TOKEN_KEY+phoneNumber);
        return redisToken != null && redisToken.equals(token);
    }

}
