package com.sducat.common.constant;

/**
 * 通用常量信息
 *
 * @author ruoyi
 */
public class Constants {

    /**
     * 评论以及猫咪滑动图片的最多张数
     */
    public static final Integer PIC_MAX_NUM = 4;

    /**
     * 若一个字段只有 是与否 两个状态，则可用0 1表示
     */
    public static final int YES = 1;
    public static final int NO = 0;

    /**
     * 待审核，审核通过，审核不通过
     */
    public static final int WAIT_CHECK = 0;
    public static final int CHECK_SUCCESS = 1;
    public static final int CHECK_FAIL = -1;

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 登录用户 redis key
     */
    public static final String TOKEN_REDIS_KEY = "sducat:token:";


    /**
     * refresh_token redis key
     */
    public static final String REFRESH_TOKEN_REDIS_KEY = "sducat:refresh_token:";

    /**
     * 通过userId存redis的key
     */
    public static final String USER_INFO_REDIS_KEY = "sducat:user:";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_TOKEN_KEY = "login_token_key";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_REFRESH_TOKEN_KEY = "login_refresh_token_key";

}
