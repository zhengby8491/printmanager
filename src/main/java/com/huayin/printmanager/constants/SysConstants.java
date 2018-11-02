/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.constants;

import java.text.DecimalFormat;

/**
 * <pre>
 * 公共 - 系统常量及系统配置参数定义
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public class SysConstants
{
	// ---------------------------------常量配置---------------------------------------------

	public final static String CACHE_TYPE_EHCACHE = "ehcache";

	public final static String CACHE_TYPE_REDIS = "redis";

	/**
	 * 全流程演示试用
	 */
	public final static String SITE_DEMO_USERNAME = "SITE_DEMO_USERNAME";

	/**
	 * 多版本功能的演示试用
	 */
	public static final String SITE_DEMO_USERNAMES = "SITE_DEMO_USERNAMES";

	public final static String SITE_DEMO_USERNAME_PWD = "SITE_DEMO_USERNAME_PWD";

	/**
	 * spring session redis key用户session在redis中的key
	 */
	public final static String REDIS_SESSION_KEY_PREFIX = "spring:session:sessions:";

	/**
	 *  spring session expires redis key用户session过期时间在redis中的key
	 */
	public final static String REDIS_SESSION_EXPIRES_KEY_PREFIX = "spring:session:sessions:expires:";

	/**
	 * 是
	 */
	public final static String YES = "YES";

	/**
	 * 否
	 */
	public final static String NO = "NO";

	/**
	 * boolean: TRUE
	 */
	public static final String TRUE = "true";

	/**
	 * boolean: FALSE
	 */
	public static final String FALSE = "false";

	/**
	 * 内部异常
	 */
	public final static String LOGIN_ERR_MSG = "login.error.msg";

	/**
	 * 序列格式8位
	 */
	public final static DecimalFormat deciamoFormat = new DecimalFormat("00000000");

	/**
	 * 加密配置:HASH_ALGORITHM
	 */
	public final static String HASH_ALGORITHM = "SHA-1";

	/**
	 * 加密配置:HASH_INTERATIONS
	 */
	public final static int HASH_INTERATIONS = 1024;

	/**
	 * 加密配置:SALT_SIZE
	 */
	public final static int SALT_SIZE = 8;

	/**
	 * 后台系统错误处理页面
	 */
	public final static String MODEL_VIEW_ERROR = "error/error_info";

	// ----------------------------------session key---------------------------------------------

	/**
	 * 用户session key user
	 */
	public final static String SESSION_KEY_LOGIN_USER = "session_login_user";

	/**
	 * 用户session key userId
	 */
	public final static String SESSION_KEY_LOGIN_USERID = "session_login_userId";

	/**
	 * 接收短信的手机session key
	 */
	public final static String SESSION_KEY_SMS_MOBILE = "session_sms_mobile";

	/**
	 * 接收短信中的验证码session key
	 */
	public final static String SESSION_KEY_SMS_VALIDATECODE = "session_sms_validatecode";

	// 微信绑定参数
	public static final String SESSION_KEY_LOGIN_WXBIND = "session_login_wxBind";

	// 验证码
	public static final String SESSION_KEY_LOGIN_PARAM_CAPTCHA = "captcha";

	// 消息参数
	public static final String SESSION_KEY_LOGIN_PARAM_MESSAGE = "message";

	// 是否显示验证码
	public static final String REQUEST_KEY_LOGIN_PARAM_ISSHOWCAPTCHA = "isShowCaptcha";

	// 记住我
	public static final String REQUEST_KEY_LOGIN_PARAM_REMEMBERME = "rememberMe";

	// 错误消息参数
	public static final String REQUEST_KEY_LOGIN_PARAM_ERRORMESSAGE = "error_message";
	// -----------------------------------------------系统参数------------------------------------------------------------

	/**
	 * 印管家访问地址
	 */
	public final static String SYSTEM_URL = "SYSTEM_URL";
	/**
	 * 项目发布版本号:201609291031
	 */
	public final static String SITE_RESOURCE_VERSION = "SITE_RESOURCE_VERSION";

	/**
	 * 缓存类型：ehcache、redis
	 */
	public final static String CACHE_TYPE = "CACHE_TYPE";

	/**
	 * 缓存时间
	 */
	public final static String CACHE_MINUTE = "CACHE_MINUTE";

	/**
	 * 根目录:/print
	 */
	public final static String SITE_BASE_PATH = "SITE_BASE_PATH";
	
	/**
	 * 默认国际化语言
	 */
	public final static String SITE_DEFAULT_I18N = "SITE_DEFAULT_I18N";

	/**
	 * 项目名称：印管家
	 */
	public final static String SITE_PROJECT_NAME = "SITE_PROJECT_NAME";

	/**
	 * 项目年份：2016
	 */
	public final static String SITE_PROJECT_YEAR = "SITE_PROJECT_YEAR";

	/**
	 * 项目版本：V2.0.0
	 */
	public final static String SITE_PROJECT_VERSION = "SITE_PROJECT_VERSION";

	/**
	 * 版权：深圳华印信息技术股份有限公司
	 */
	public final static String SITE_PROJECT_COPYRIGHT = "SITE_PROJECT_COPYRIGHT";

	/**
	 * 客服电话
	 */
	public final static String SITE_SERVICE_PHONE = "SITE_SERVICE_PHONE";

	/**
	 * 公司到期提前提醒天数
	 */
	public final static String COMPANY_EXPIRE_NOTIFY_DAY = "COMPANY_EXPIRE_NOTIFY_DAY";

	/**
	 * 公司到期提前提醒内容
	 */
	public final static String COMPANY_EXPIRE_NOTIFY_CONTENT = "COMPANY_EXPIRE_NOTIFY_CONTENT";

	// 超级管理员用户名
	public static final String ADMIN_USERNAME = "ADMIN_USERNAME";

	// 系统管理公司ID
	public static final String ADMIN_COMPANYID = "ADMIN_COMPANYID";

	/**
	 * #静态文件后缀：.css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.htm,.html,.crx,.xpi,.exe,.ipa,.apk
	 */
	public final static String SITE_STATIC_FILE_SUFFIX = "SITE_STATIC_FILE_SUFFIX";

	/**
	 * 万能校验码 万能
	 */
	public final static String DEFAULT_WANNENG_CODE = "DEFAULT_WANNENG_CODE";

	/**
	 * 项目缓存前缀
	 */
	public final static String CACHE_REDIS_KEY_PREFIX = "CACHE_REDIS_KEY_PREFIX";

	/**
	 * 系统作业状态Key值
	 */
	public final static String SYSTEM_JOB_STATUS = "SYSTEM_JOB_STATUS";

	/**
	 * 默认是否允许同一用户同时重复登录
	 */
	public final static String DEFAULT_SAME_USER_LOGIN_MULTIPLE = "DEFAULT_SAME_USER_LOGIN_MULTIPLE";

	/**
	 * 默认每页大小
	 */
	public final static String DEFAULT_PAGE_SIZE = "DEFAULT_PAGE_SIZE";

	/**
	 * 到货转对账不分页情况下最大抓取数量
	 */
	public final static String TRANSMIT_ARRIVE_PAGE_SIZE_MAX = "TRANSMIT_ARRIVE_PAGE_SIZE_MAX";

	/**
	 * 退货转对账不分页情况下最大抓取数量
	 */
	public final static String TRANSMIT_RETURN_PAGE_SIZE_MAX = "TRANSMIT_ARRIVE_PAGE_SIZE_MAX";

	/**
	 * 在线人数基数
	 */
	public static final String SITE_ONLINE_COUNT_BASE = "SITE_ONLINE_COUNT_BASE";

	/**
	 * 最多错误登陆次数
	 */
	public static final String LOGIN_MAX_ERROR_COUNT = "LOGIN_MAX_ERROR_COUNT";

	/**
	 * 快捷登陆用户前缀
	 */
	public static final String LOGIN_EXPRESS_PREFIX = "LOGIN_EXPRESS_PREFIX";

	/**
	 * 默认短信通道ID
	 */
	public static final String SMS_PARTNER_DEFAULT = "SMS_PARTNER_DEFAULT";

	/**
	 * 服务器监控手机号码
	 */
	public static final String MONITOR_MOBILE = "MONITOR_MOBILE";

	/**
	 * 公司试用天数
	 */
	public static final String COMPANY_TRYDAY_NUM = "COMPANY_TRYDAY_NUM";

	/**
	 * 公司角色数据限制
	 */
	public static final String COMPANY_ROLE_COUNT_MAX = "COMPANY_ROLE_COUNT_MAX";

	/**
	 * 获取初始化公司模板ID(1)
	 */
	public static final String REGISTER_INIT_TEMPLATE_COMPANY_ID = "REGISTER_INIT_TEMPLATE_COMPANY_ID";

	/**
	 * 获取初始化公司权限(来源角色权限:1)【普及版】
	 */
	public static final String REGISTER_INIT_TEMPLATE_ROLE_ID = "REGISTER_INIT_TEMPLATE_ROLE_ID";

	/**
	 * 获取初始化公司权限2(来源角色权限:11)【简易版】
	 */
	public static final String REGISTER_INIT_TEMPLATE_ROLE_ID2 = "REGISTER_INIT_TEMPLATE_ROLE_ID2";

	/**
	 * 获取初始化公司角色(1,3,4,5,6,7,8)【普及版】
	 */
	public static final String REGISTER_INIT_TEMPLATE_ROLE_IDS = "REGISTER_INIT_TEMPLATE_ROLE_IDS";

	/**
	 * 获取初始化公司角色(3,4,5,6,7,8,11)【简易版】
	 */
	public static final String REGISTER_INIT_TEMPLATE_ROLE_IDS2 = "REGISTER_INIT_TEMPLATE_ROLE_IDS2";

	/**
	 * 附件上传真实根目录(图片，文件，视频等)
	 */
	public static final String ATTACH_FILE_BASE_REALPATH = "ATTACH_FILE_BASE_REALPATH";

	/**
	 * 附件的访问路径
	 */
	public static final String ATTACH_FILE_URL = "ATTACH_FILE_URL";

	/**
	 * 微信appid
	 */
	public static final String WX_APPID = "WX_APPID";

	/**
	 * 微信appsecret
	 */
	public static final String WX_APPSECRET = "WX_APPSECRET";

	/**
	 * 微信服务器路径
	 */
	public static final String WX_URL = "WX_URL";

	/**
	 * 微信体验用户名
	 */
	public static final String WX_WXPERIENCE_USER = "WX_WXPERIENCE_USER";

	/**
	 * 微信体验用户密码
	 */
	public static final String WX_WXPERIENCE_PWD = "WX_WXPERIENCE_PWD";

	/**
	 * QQ快捷登录 APP ID
	 */
	public static final String EXPRESSLOGIN_TENCENT_APPID = "EXPRESSLOGIN_TENCENT_APPID";

	/**
	 * QQ快捷登录 APP KEY
	 */
	public static final String EXPRESSLOGIN_TENCENT_APPKEY = "EXPRESSLOGIN_TENCENT_APPKEY";

	/**
	 * QQ快捷登录回调路径
	 */
	public static final String EXPRESSLOGIN_TENCENT_RETURNURL = "EXPRESSLOGIN_TENCENT_RETURNURL";

	/**
	 * 微信快捷登录 APP ID
	 */
	public static final String EXPRESSLOGIN_WECHAT_APPID = "EXPRESSLOGIN_WECHAT_APPID";

	/**
	 * 微信快捷登录 APP KEY
	 */
	public static final String EXPRESSLOGIN_WECHAT_APPKEY = "EXPRESSLOGIN_WECHAT_APPKEY";

	/**
	 * 微信快捷登录回调路径
	 */
	public static final String EXPRESSLOGIN_WECHAT_RETURNURL = "EXPRESSLOGIN_WECHAT_RETURNURL";

	public static final String QRCODE_DIR = "qrcode";

	/**
	 * 支付宝配置参数
	 */
	public static final String ZFB_CONF = "ZFB_CONF";

	/**
	 * 微信配置参数
	 */
	public static final String WX_CONF = "WX_CONF";

	/**
	 * 系统菜单
	 */
	public static final String SYS_MENU = "SYS_MENU";
	
	/**
	 * 印刷家提供的商户号
	 */
	public static final String MERCHANT_FROM_YSJ = "MERCHANT_FROM_YSJ";
	
	/**
	 * 印刷家提供的商户秘钥
	 */
	public static final String TOKENKEY_FROM_YSJ = "TOKENKEY_FROM_YSJ";
	
	/**
	 * 印刷家签名加密方式
	 */
	public static final String SIGNSTYLE_FROM_YSJ = "SIGNSTYLE_FROM_YSJ";
	
	/**
	 * 印刷家接口url
	 */
	public static final String URL_TO_YSJ = "URL_TO_YSJ";
	
	/**
	 * 是否允许印刷家访问
	 */
	public static final String SWITCH_FOR_YSJ = "SWITCH_FOR_YSJ";
	
	/**
	 * 外部通知周期
	 */
	public static final String EXT_JOB_PERIOD = "EXT_JOB_PERIOD";
	/**
	 * 是否启用通知印刷家的定时任务
	 */
	public static final String EXT_JOB_STATUS = "EXT_JOB_STATUS";
}
