package com.hilda.common.constant;

/**
 * Redis常量配置类
 *
 */
public class RedisConst {

    public static final String SKUKEY_PREFIX = "sku:";
    public static final String SKUKEY_SUFFIX = "info:";

    public static final String USERKEY_PREFIX = "login:";
    public static final String USERKEY_SUFFIX = "userInfo:";
    public static final Long USER_LOGIN_TTL = 60 * 20L;
    //单位：秒
    public static final long SKUKEY_TIMEOUT = 24 * 60 * 60;
    public static final long CARTKEY_TIMEOUT = 24 * 60 * 60;

    public static final String TEMP_UID = "userTempId";
    public static final String UID = "uid";

    public static final String CARTKEY_PREFIX = "cart:";
    public static final String CARTKEY_SUFFIX = "info:";
}
