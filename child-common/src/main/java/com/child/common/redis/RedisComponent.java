package com.child.common.redis;

import com.child.common.constants.Constant;
import com.child.common.utils.TokenTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisComponent {

    @Autowired
    private RedisUtils redisUtils;

    public String saveUserLoginToken(String token, String phoneNumber){
        redisUtils.setEx(Constant.REDIS_TOKEN_KEY+phoneNumber,token,Constant.TOKEN_EXPIRE_HOURS);
        return token;
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
