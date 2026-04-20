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

    public void saveLoginFailCount(String ip, int count) {
        String key = "login:fail:" + ip;
        redisUtils.setEx(key, String.valueOf(count), 10 * 60); // 10分钟
    }

    public int getLoginFailCount(String ip) {
        String key = "login:fail:" + ip;
        String countStr = redisUtils.get(key);
        return countStr == null ? 0 : Integer.parseInt(countStr);
    }

    // 锁定IP 10分钟
    public void lockIp(String ip) {
        String key = "login:lock:" + ip;
        redisUtils.setEx(key, "locked", 10 * 60);
    }

    // 判断IP是否被锁定
    public boolean isIpLocked(String ip) {
        String key = "login:lock:" + ip;
        return redisUtils.get(key) != null;
    }

    // 清空登录失败记录
    public void clearLoginFail(String ip) {
        redisUtils.delete("login:fail:" + ip);
        redisUtils.delete("login:lock:" + ip);
    }
}
