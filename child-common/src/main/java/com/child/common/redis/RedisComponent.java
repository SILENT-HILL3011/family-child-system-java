package com.child.common.redis;

import com.child.common.constants.Constant;
import com.child.common.entity.po.User;
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


    public String getExpertIdByToken(String token){
        if (token == null || token.isEmpty()){
            return null;
        }
        String redisKey = Constant.REDIS_TOKEN_KEY+token;
        String expertId = redisUtils.get(redisKey);
        return expertId;
    }

    public String getUserIdByToken(String token){
        if (token == null || token.isEmpty()){
            return null;
        }
        String redisKey = Constant.REDIS_TOKEN_KEY+token;
        String userId = redisUtils.get(redisKey);
        return userId;
    }


    public void save(String s, String role) {
        redisUtils.setEx(s,role,Constant.TOKEN_EXPIRE_HOURS);
    }

    public String get(String key) {
        return redisUtils.get(key);
    }

    public void saveChildInfo(String childId, String childJson) {
        String key = Constant.REDIS_CHILD_INFO_KEY + childId;
        redisUtils.setEx(key, childJson, Constant.CHILD_CACHE_EXPIRE_SECONDS);
    }

    public String getChildInfo(String childId) {
        String key = Constant.REDIS_CHILD_INFO_KEY + childId;
        return redisUtils.get(key);
    }

    public void deleteChildInfo(String childId) {
        String key = Constant.REDIS_CHILD_INFO_KEY + childId;
        redisUtils.delete(key);
    }

    public void saveChildList(String familyId, Integer pageNum, String listJson) {
        String key = Constant.REDIS_CHILD_LIST_KEY + familyId + ":" + pageNum;
        redisUtils.setEx(key, listJson, Constant.CHILD_CACHE_EXPIRE_SECONDS);
    }

    public String getChildList(String familyId, Integer pageNum) {
        String key = Constant.REDIS_CHILD_LIST_KEY + familyId + ":" + pageNum;
        return redisUtils.get(key);
    }

    public void clearChildListCache() {
        redisUtils.deleteByPattern(Constant.REDIS_CHILD_LIST_KEY + "*");
    }
}
