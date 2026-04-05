package com.child.common.constants;

public class Constant {

    public static final String REDIS_TOKEN_KEY = "token:";
    public static final String REDIS_ROLE_KEY = "user:role:";
    public static final String REDIS_FAMILY_KEY = "user:familyId:";
    public static final Integer TOKEN_EXPIRE_HOURS = 2 * 60 * 60;

    public static final Integer LENGTH_12 = 12;
    public static final Integer LENGTH_20 = 20;


    private static final String REDIS_KEY_PREFIX = "child-system:";
    public static final String REDIS_KEY_CHECK_CODE = REDIS_KEY_PREFIX + "checkcode:";
    public static final String TOKEN_HEADER_KEY = "token";

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    public static final Integer IS = 1;
    public static final Integer NO = 0;

    public static final Integer NUM_ZERO = 0;
    public static final Integer NUM_ONE = 1;



    public static final String SYSTEM_ID = "000000000000";
    public static final String SCHEDULE_REMIND = "日程提醒";
    public static final String TOMORROW_SCHEDULE = "明天日程：";
    public static final String SCHEDULE_TIME = "时间：";
    public static final String EXPIRE_REMIND = "【即将开始！1小时后日程】";

    public static final String REDIS_CHILD_INFO_KEY = "child:info:";       // 儿童详情
    public static final String REDIS_CHILD_LIST_KEY = "child:list:";       // 儿童列表
    public static final long CHILD_CACHE_EXPIRE_SECONDS = 86400;           // 1天过期
}
