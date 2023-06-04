package com.momo.theta.constants;

/**
 * 跟上下文常量工具类
 *
 * @author zhubo
 * @date 2018/12/21
 */
public final class AuthConstants {
    private AuthConstants() {
    }

    public static final String DEF_TENANT = "master";
    /**
     * JWT中封装的 用户id
     */
    public static final String JWT_KEY_USER_ID = "userid";
    /**
     * JWT中封装的 用户名称
     */
    public static final String JWT_KEY_NAME = "name";
    /**
     * JWT中封装的 token 类型
     */
    public static final String JWT_KEY_TOKEN_TYPE = "token_type";
    /**
     * JWT中封装的 用户账号
     */
    public static final String JWT_KEY_ACCOUNT = "account";

    /**
     * JWT中封装的 客户端id
     */
    public static final String JWT_KEY_CLIENT_ID = "client_id";

    /**
     * JWT token 签名
     * <p>
     * 签名密钥长度至少32位!!!
     */
    public static final String JWT_SIGN_KEY = "lamp-cloud_is_a_fantastic_project";

    /**
     * JWT中封装的 租户编码
     */
    public static final String JWT_KEY_TENANT = "tenant";

    /**
     * JWT中封装的 子租户编码
     */
    public static final String JWT_KEY_SUB_TENANT = "sub_tenant";

    /**
     * 刷新 Token
     */
    public static final String REFRESH_TOKEN_KEY = "refresh_token";

    /**
     * User信息 认证请求头
     */
    public static final String BEARER_HEADER_KEY = "token";

    /**
     * User信息 认证请求头前缀
     */
    public static final String BEARER_HEADER_PREFIX = "Bearer ";

    /**
     * 请求头和线程变量中的 前端页面地址栏#号后的路径
     */
    public static final String PATH_HEADER = "Path";
    /**
     * Client信息认证请求头
     */
    public static final String BASIC_HEADER_KEY = "Authorization";

    /**
     * Client信息认证请求头前缀
     */
    public static final String BASIC_HEADER_PREFIX = "Basic ";

    /**
     * Client信息认证请求头前缀
     */
    public static final String BASIC_HEADER_PREFIX_EXT = "Basic%20";

}
