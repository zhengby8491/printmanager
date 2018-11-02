/**
 * <pre>
 * Author:		   think
 * Create:	 	   2013年10月17日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Maps;
import com.huayin.common.exception.ServiceResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.expresslogin.dto.LoginResultDto;
import com.huayin.printmanager.expresslogin.factory.ExpressLoginFactory;
import com.huayin.printmanager.expresslogin.tencent.config.TencentConfig;
import com.huayin.printmanager.expresslogin.tencent.config.WechatConfig;
import com.huayin.printmanager.persist.entity.sys.AgentQuotient;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.entity.sys.UserShare;
import com.huayin.printmanager.persist.enumerate.InitStep;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.UserShareType;
import com.huayin.printmanager.utils.CookieUtils;
import com.huayin.printmanager.utils.HttpRequest;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架  - 登录
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2013年10月17日, zhaojt
 * @version 	   1.0, 2018年2月27日下午2:43:50, zhengby, 代码规范
 */
@Controller
public class LoginController extends BaseController
{
	// private final String Access_Token_URL =
	// "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&state=YZ&redirect_uri=%s";

	// private final String Open_Id_URL = "https://graph.qq.com/oauth2.0/me?access_token=%s";

	// private final String Get_User_Info_URL =
	// "https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s";

	// private final long client_id = 101384020;

	// private final String client_secret = "68954998a03bcb2005f8316bf2e72176";

	// private final String redirect_uri = "http://www.51ygj.com/print";

	// ==================== 第三方登陆 ====================

	/**
	 * <pre>
	 * 页面  - 跳转到第三方登录页面
	 * </pre>
	 * @param type
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:44:59, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/expresslogin/{type}")
	public String expressLogin(@PathVariable String type, HttpServletRequest request, ModelMap map)
	{
		UserShareType userShareType = null;

		try
		{
			userShareType = UserShareType.valueOf(type);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		String htmlText = ExpressLoginFactory.getExpressLoginCreator(userShareType).createRequestService().buildRequest(request, userShareType);
		map.put("text", htmlText);
		return "sys/user/expressCommit";
	}

	/**
	 * <pre>
	 * 页面  - 跳转到第三方返回页面
	 * </pre>
	 * @param type
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:45:43, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/expressReturn/{type}")
	public String expressLoginVerity(@PathVariable String type, HttpServletRequest request, ModelMap map)
	{
		UserShareType userShareType = UserShareType.valueOf(type);
		LoginResultDto dto = ExpressLoginFactory.getExpressLoginCreator(userShareType).createNotifyService().validataNotify(request, userShareType);

		if (dto.getIsSuccess())
		{
			UserShare userShare = serviceFactory.getUserShareService().isExist(dto.getUserId(), userShareType);
			if (userShare == null)
			{
				// 跳转绑定页面
				String userInfo = null;
				if (UserShareType.TENCENT == userShareType)
				{
					userInfo = HttpRequest.sendGet(String.format(TencentConfig.getUserInfoUrl, dto.getAccessToken(), SystemConfigUtil.getExpressLoginTencentAppId(), dto.getUserId()));
				}
				else if (UserShareType.WEIXIN == userShareType)
				{
					userInfo = HttpRequest.sendGet(String.format(WechatConfig.getUserInfoUrl, dto.getAccessToken(), dto.getUserId()));
				}

				// 找不到用户，登录失败
				if (userInfo == null)
				{
					return "login";
				}

				JSONObject userInfoObject = new JSONObject(userInfo);
				UserUtils.putSessionCache("openid", dto.getUserId());
				UserUtils.putSessionCache("nickname", userInfoObject.get("nickname"));
				if (UserShareType.TENCENT == userShareType)
				{
					UserUtils.putSessionCache("figureurl_qq_2", userInfoObject.get("figureurl_qq_2"));
				}
				else if (UserShareType.WEIXIN == userShareType)
				{
					UserUtils.putSessionCache("figureurl_qq_2", userInfoObject.get("headimgurl"));
				}

				UserUtils.putSessionCache("userShareType", userShareType);
				UserUtils.putSessionCache("userShareTypeText", userShareType.getText());
				UserUtils.putSessionCache("userInfo", userInfo);
				return "redirect:" + basePath + "/public/thirdBindUser";
			}
			else
			{
				User user = serviceFactory.getUserService().get(userShare.getUserId());
				UserUtils.login(user);
				return "redirect:" + basePath + "/loginSuc";
			}
		}
		else
		{
			return "login";
		}
	}

	/**
	 * <pre>
	 * 页面  - 跳转到第三方登录绑定账号页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:46:09, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/thirdBindUser")
	public String thirdBindUser()
	{
		return "sys/user/thirdBindUser";
	}

	/**
	 * <pre>
	 * 页面  - 跳转到第三方登录成功/失败页面
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:46:41, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/user_center_bind", method = RequestMethod.POST)
	public String userCenterBind(HttpServletRequest request, HttpServletResponse response, ModelMap map)
	{
		User user = serviceFactory.getUserService().getByUserName(request.getParameter("userName"));
		if (user == null)
		{
			map.put(SysConstants.REQUEST_KEY_LOGIN_PARAM_ERRORMESSAGE, "用户名不存在");
			return "redirect:" + basePath + "/public/thirdBindUser";
		}
		String password = request.getParameter("passWord");
		if (UserUtils.validatePassword(password, user.getPassword()))
		{
			UserShare userShare = new UserShare();
			userShare.setIdentifier((String) UserUtils.getSessionCache("openid"));
			userShare.setUserId(user.getId());
			userShare.setUserType((UserShareType) UserUtils.getSessionCache("userShareType"));
			serviceFactory.getUserShareService().save(userShare);
			UserUtils.login(user);
			return "redirect:" + basePath + "/loginSuc";
		}
		map.put(SysConstants.REQUEST_KEY_LOGIN_PARAM_ERRORMESSAGE, "验证失败");
		return "redirect:" + basePath + "/public/thirdBindUser";
	}

	// ==================== 登陆 ====================

	/**
	 * <pre>
	 * 页面  - 跳转到登录页面
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:48:10, zhengby
	 */
	@RequestMapping(value = "${basePath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap map)
	{
		if (UserUtils.isLogined())
		{// 已登录
			return "redirect:" + basePath;
		}
		else
		{
			if (request.getAttribute(SysConstants.LOGIN_ERR_MSG) != null)
			{
				map.put(SysConstants.REQUEST_KEY_LOGIN_PARAM_ERRORMESSAGE, request.getAttribute(SysConstants.LOGIN_ERR_MSG));
			}
			// 判断是否记住用户名密码

			Boolean rememberMe = true;
			map.put(SysConstants.REQUEST_KEY_LOGIN_PARAM_REMEMBERME, rememberMe);
			// map.put(SysConstants.LOGIN_ISSHOWCAPTCHA_PARAM, isCaptchaLogin(username, true, false));
		}
		return "login";
	}

	/**
	 * <pre>
	 * 页面  - 跳转到登录成功/失败页面
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @param username
	 * @param password
	 * @param captcha
	 * @param rememberMe
	 * @param wxbind
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:49:06, zhengby
	 */
	@RequestMapping(value = "${basePath}/login", method = RequestMethod.POST)
	public String loginSubmit(HttpServletRequest request, HttpServletResponse response, ModelMap map, String username, String password, String captcha, boolean rememberMe, boolean wxbind)
	{
		String error_message = null;
		if (Validate.validateObjectsNullOrEmpty(username, password))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		// 校验登录验证码
		if (_isCaptchaLogin(username, false, false))
		{
			String vcode = (String) request.getSession().getAttribute(SysConstants.SESSION_KEY_LOGIN_PARAM_CAPTCHA);
			String wanneng = SystemConfigUtil.getWanNengCode();
			boolean isValidate = false;
			if (captcha != null)
			{
				if (wanneng == null)
				{
					isValidate = captcha.toUpperCase().equals(vcode.toUpperCase());
				}
				else
				{
					isValidate = captcha.toUpperCase().equals(vcode.toUpperCase()) || captcha.toUpperCase().equals(wanneng.toUpperCase());
				}
				if (!isValidate)
				{
					error_message = "验证码不正确";
					map.put(SysConstants.REQUEST_KEY_LOGIN_PARAM_ISSHOWCAPTCHA, true);
				}
			}
		}

		if (StringUtils.isBlank(error_message))
		{
			User user = serviceFactory.getUserService().getByUserName(username.trim());
			if (user != null)
			{
				if (!UserUtils.validatePassword(password, user.getPassword()))
				{// 密码校验不通过,为了防止用户恶意登录，故此处返回消息不精确指定用户或密码的错误点
					error_message = "用户或密码错误, 请重试";
				}
				else
				{
					UserUtils.login(user);
				}
			}
			else
			{
				error_message = "用户或密码错误, 请重试";
			}
		}

		// 记住我
		if(rememberMe)
		{
			CookieUtils.setCookie(response, "username", username);
			CookieUtils.setCookie(response, "rememberMe", "true");
		}
		else
		{
			CookieUtils.setCookie(response, "rememberMe", "false");
		}	
		if (StringUtils.isNotBlank(error_message))
		{
			map.put(SysConstants.REQUEST_KEY_LOGIN_PARAM_ERRORMESSAGE, error_message);
			map.put(SysConstants.REQUEST_KEY_LOGIN_PARAM_ISSHOWCAPTCHA, _isCaptchaLogin(username, true, false));
			return "login";
		}
		else
		{
			return "redirect:loginSuc";
		}
	}

	/**
	 * <pre>
	 * 页面  - 登录成功后页面
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:51:24, zhengby
	 */
	@RequestMapping(value = "${basePath}/loginSuc")
	@SystemControllerLog(SystemLogType = SystemLogType.LOGIN, Description = "用户登录")
	public String loginSuc(HttpServletRequest request, ModelMap map)
	{
		ServiceResult<User> result = serviceFactory.getUserService().onLoginSuccess(request);
		if (result.getIsSuccess())
		{
			// 登录成功后，验证码计算器清零
			_isCaptchaLogin(result.getReturnValue().getUserName(), false, true);
			Company company = UserUtils.getCompany();
			// 登陆初始化基础数据
			if (company.getInitStep() == InitStep.OVER && !company.getId().equals("1"))
			{
				// 给老账户添加原始单位换算信息
				serviceFactory.getCompanyService().initUnitConvert();
				// 初始化报价的损耗设置
				serviceFactory.getCompanyService().initWasteSetting();
			}
			return "redirect:" + basePath;
		}
		else
		{
			logger.error(result.getMessage());
			map.put(SysConstants.REQUEST_KEY_LOGIN_PARAM_ERRORMESSAGE, result.getMessage());
			UserUtils.loginout();
			return "login";
		}
	}

	/**
	 * <pre>
	 * 页面  - 登录成功，进入管理首页
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:52:33, zhengby
	 */
	@RequestMapping(value = "${basePath}")
	public String main(ModelMap map)
	{
		Company company = UserUtils.getCompany();
		if (company.getInitStep() == InitStep.INIT_COMPANY)
		{// 完善公司信息
			return "init/company_init";
		}
		else if (company.getInitStep() == InitStep.INIT_BASIC)
		{// 初始化基础资料资料
			return "init/basic_init";
		}
		else
		{
			if (company.getAgentQuotientId() != null)
			{
				AgentQuotient agent = serviceFactory.getAgentQuotientService().get(company.getAgentQuotientId());

				if (agent != null && StringUtils.isNotBlank(agent.getPhotoUrl()))
				{
					map.put("photoUrl", agent.getPhotoUrl());
					map.put("name", agent.getName());
				}
			}
			return "main";
		}
	}

	/**
	 * <pre>
	 * 页面  - 跳转到退出登录页面
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:52:54, zhengby
	 */
	@RequestMapping(value = "${basePath}/loginout", method = RequestMethod.GET)
	@SystemControllerLog(SystemLogType = SystemLogType.LOGINOUT, Description = "用户退出")
	public String loginout(HttpServletRequest request)
	{
		request.setAttribute(SystemLogAspect.USERNAME, UserUtils.getUserName());
		UserUtils.loginout();
		return "redirect:" + basePath + "/login";
	}

	// ==================== 演示账户 ====================

	/**
	 * <pre>
	 * 页面  - 全流程演示用户登录
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:53:17, zhengby
	 */
	@RequestMapping(value = "${basePath}/demo", method = RequestMethod.GET)
	public String demo()
	{
		UserUtils.loginout();
		String username = (String) (SystemConfigUtil.getConfig(SysConstants.SITE_DEMO_USERNAME));
		// String password=(String)(SystemConfigUtil.getConfig(SysConstants.SITE_DEMO_USERNAME_PWD));
		User user = serviceFactory.getUserService().getByUserName(username.trim());
		UserUtils.login(user);
		return "redirect:loginSuc";
	}

	/**
	 * <pre>
	 * 页面  - 多版本功能的演示试用
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:53:31, zhengby
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "${basePath}/demos/{type}")
	public String demos(@PathVariable("type") Integer type)
	{
		try
		{
			String usernames = SystemConfigUtil.getConfig(SysConstants.SITE_DEMO_USERNAMES);
			usernames = usernames.replaceAll("&quot;", "\"");
			Map<String, String> map = (Map<String, String>) JsonUtils.jsonToMap(usernames);
			String username = map.get(type + "");
			if (StringUtils.isEmpty(username))
				return "redirect:" + basePath;

			UserUtils.loginout();
			User user = serviceFactory.getUserService().getByUserName(username.trim());
			UserUtils.login(user);
		}
		catch (Exception e)
		{
			return "redirect:" + basePath;
		}

		return "redirect:" + basePath + "/loginSuc";
	}

	// ==================== 基本功能 ====================

	/**
	 * 
	 * <pre>
	 * 功能  - 是否是验证码登录
	 * </pre>
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 * @since 1.0, 2017年10月17日 下午3:31:12, think
	 */
	@SuppressWarnings("unchecked")
	private boolean _isCaptchaLogin(String useruame, boolean isFail, boolean clean)
	{
		Map<String, Integer> loginFailMap = (Map<String, Integer>) UserUtils.getSessionCache("loginFailMap");
		if (loginFailMap == null)
		{
			loginFailMap = Maps.newHashMap();
			UserUtils.putSessionCache("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum == null)
		{
			loginFailNum = 0;
		}
		if (isFail)
		{
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean)
		{
			loginFailMap.remove(useruame);
		}
		UserUtils.putSessionCache("loginFailMap", loginFailMap);
		return loginFailNum >= 3;
	}

	public static void main(String[] args)
	{
		// 测试第三方登录
		String loginUrl = "http://localhost:8080/print/exterior/login";

		HttpClient httpClient = new HttpClient();
		// 获得POST请求方法
		PostMethod postMethod = new PostMethod(loginUrl);
		//postMethod.setRequestHeader("Referer", "http://127.0.0.1");
		postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2453.0 Safari/537.36");
		postMethod.addRequestHeader("Content-Type","application/json; charset=utf-8");
		// 设置登录请求参数
//		NameValuePair[] data = { new NameValuePair("username", "wanghai"), new NameValuePair("password", "123456"), };
//		postMethod.setRequestBody(data);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("username", "zheng001");
		jsonObj.put("password", "123456");
		jsonObj.put("isRegister", "false");
		jsonObj.put("province", "广东");
		ByteArrayRequestEntity entity = new ByteArrayRequestEntity(jsonObj.toString().getBytes(), "UTF-8");
		postMethod.setRequestEntity(entity);
		try
		{
			// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
			httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			int loginState = httpClient.executeMethod(postMethod);
			// 获得登陆后的 Cookie
			Cookie[] cookies = httpClient.getState().getCookies();
			String tmpcookies = "";
			for (Cookie c : cookies)
			{
				tmpcookies += c.toString() + ";";
			}
			System.out.println(tmpcookies);
			System.out.println(loginState);
			System.out.println(postMethod.getResponseBodyAsString());
			// 进行登陆后的操作
//			String loginUrl2 = "http://127.0.0.1:8080/print/purch/transmit/createOrder";
//			PostMethod postMethod2 = new PostMethod(loginUrl2);
//			// 每次访问需授权的网址时需带上前面的 cookie 作为通行证
//			postMethod2.setRequestHeader("cookie", tmpcookies);
//			httpClient.executeMethod(postMethod2);
//			System.out.println(postMethod2.getResponseBodyAsString());
		}
		catch (Exception e)
		{
			System.out.println("登陆出错------------->" + e.getMessage());
		}
	}
}
