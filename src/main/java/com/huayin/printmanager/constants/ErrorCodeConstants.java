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

/**
 * <pre>
 * 公共 - 返回错误代码
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public final class ErrorCodeConstants
{
	/**
	 * 公共错误代码
	 */
	public final static class CommonCode
	{
		public final static String COMMON_DATA_NOT_VALIDATA = "9001";// 数据没有通过验证

		public final static String COMMON_CHECKCODE_ERROR = "9002";// 验证码错误

		public final static String COMMON_SMS_SEND_FREQUENTLY = "9003";// 短信发送频繁

		public final static String COMMON_SMS_SEND_FAILED = "9004";// 短信发送失败

		public final static String COMMON_PERMISSION_ERROR = "9995";// 权限非法

		public final static String COMMON_DIGEST_ERROR = "9996";// 摘要出错

		public final static String COMMON_DATA_NOT_FOUND = "9997";// 数据未找到

		public final static String COMMON_STATE_ERROR = "9998";// 状态错误

		public final static String COMMON_UKNOW_ERROR = "9999";// 未知错误
	}

	/**
	 * 注册模块错误代码
	 */
	public final static class RegisterCode
	{
		public final static String REGISTER_FAILED = "1001";// 注册失败
	}

	/**
	 * 登录模块错误代码
	 */
	public final static class LoginCode
	{
		public final static String LOGIN_FAILED = "2001";// 登录失败

		public final static String LOGIN_USER_CLOSED = "2002";// 用户被停用

		public final static String LOGIN_USER__DELETE = "2003";// 用户被删除
	}

	/**
	 * 用户模块错误代码
	 */
	public final static class UserCode
	{
		public final static String USER_NOT_FOUND = "3001";// 帐户不存在

		public final static String USER_COMPANY_NOT_FOUND = "3002";// 会员店不存在

		public final static String USER__QUESTION_NOT_FOUND = "3003";// 密保问题不存在

		public final static String USER_NAME_ISEXISTED = "3004";// 用户名已经存在

		public final static String USER_EMAIL_ISEXISTED = "3005";// 邮箱已经存在

		public final static String USER_OLDPWD_ERROR = "3006";// 原始密码输入不正确

	}

	/**
	 * <pre>
	 * 单据操作
	 * </pre>
	 * @author zhaojt
	 * @version 1.0, 2016年9月9日
	 */
	public final static class BillCode
	{
		public final static String BILL_SAVE_FAILED = "4001";// 保存失败
	}

	/**
	 * <pre>
	 * 账户操作
	 * </pre>
	 * @author zhaojt
	 * @version 1.0, 2016年9月9日
	 */
	public final static class AccountCode
	{
		public final static String ACCOUNT_ISNULL = "5001";// 帐户不能为空不足

		public final static String ACCOUNT_MONEY_NOT_ENOUGH = "5002";// 帐户余额不足
	}
}
