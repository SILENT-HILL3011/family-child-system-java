package com.child.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;


public class StringTools {

    public static String getMd5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String getRandomNumber(Integer length){
        return RandomStringUtils.random(length, false, true);
    }

    public static void main(String[] args) {
        System.out.println(getRandomNumber(10));
    }
}
