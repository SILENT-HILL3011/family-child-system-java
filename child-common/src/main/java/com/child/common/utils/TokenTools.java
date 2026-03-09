package com.child.common.utils;

import java.util.UUID;

public class TokenTools {

    public static String getToken(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
