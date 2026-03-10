package com.child.common.constants;

public class Constant {

    public static final String REDIS_TOKEN_KEY = "token:";
    public static final Integer TOKEN_EXPIRE_HOURS = 2 * 60 * 60;

    public static final Integer LENGTH_12 = 12;
    public static final Integer LENGTH_20 = 20;


    private static final String REDIS_KEY_PREFIX = "child-system:";
    public static final String REDIS_KEY_CHECK_CODE = REDIS_KEY_PREFIX + "checkcode:";
    public static final String TOKEN_HEADER_KEY = "token";
}
