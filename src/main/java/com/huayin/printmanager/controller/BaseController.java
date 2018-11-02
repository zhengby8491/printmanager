/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月26日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.huayin.common.constant.Constant;
import com.huayin.common.util.JsonUtils;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.service.ServiceFactory;
import com.huayin.printmanager.utils.BeanValidators;
import com.huayin.printmanager.utils.DateUtils;

/**
 * <pre>
 * 框架 - 基础controller
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月26日
 */
public abstract class BaseController
{
	@Autowired
	public ServiceFactory serviceFactory;

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 管理基础路径
	 */
	protected String basePath = SystemConfigUtil.getBasePath();

	public static final String TRANSACTION_RESPONSE_MESSAGE_REF = "反审核失败：已被下游单据引用";

	public static final String TRANSACTION_RESPONSE_CODE_ref = "8888";

	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	
	/**
	 * <pre>
	 * 返回异常信息
	 * </pre>
	 * @param map
	 * @param ex
	 * @return
	 */
	public static final String returnErrorPage(ModelMap map, Throwable ex)
	{
		return returnErrorPage(map, ex.getMessage());
	}

	/**
	 * <pre>
	 * 返回错误信息
	 * </pre>
	 * @param map
	 * @param errorMessage
	 * @return
	 */
	public static final String returnErrorPage(ModelMap map, String errorMessage)
	{
		return returnErrorPage(map, Constant.TRANSACTION_RESPONSE_CODE_UNKOWN, errorMessage);
	}

	/**
	 * <pre>
	 * 返回错误信息
	 * </pre>
	 * @param map
	 * @param errorCode
	 * @param errorMessage
	 * @return
	 */
	public static final String returnErrorPage(ModelMap map, String errorCode, String errorMessage)
	{
		map.put(Constant.MESSAGE_KEY, errorMessage);
		map.put(Constant.CODE_KEY, errorCode);
		return SysConstants.MODEL_VIEW_ERROR;
	}

	/**
	 * <pre>
	 * 返回成功对象
	 * </pre>
	 * @param map{'codekey':'0000','messagekey','成功，系统处理正常。'}
	 * @return
	 */
	public static final AjaxResponseBody returnSuccessBody()
	{
		return new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_SUCCESS, Constant.TRANSACTION_RESPONSE_MESSAGE_SUCCESS);
	}

	/**
	 * <pre>
	 * 返回成功对象
	 * </pre>
	 * @param map{'codekey':'0000','messagekey',message}
	 * @return
	 */
	public static final AjaxResponseBody returnSuccessBody(String message)
	{
		return new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_SUCCESS, message);
	}

	/**
	 * <pre>
	 * 返回成功对象
	 * </pre>
	 * @param map{'codekey':'0000','messagekey',message}
	 * @return
	 */
	public static final AjaxResponseBody returnSuccessBody(Object object)
	{
		return new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_SUCCESS, Constant.TRANSACTION_RESPONSE_MESSAGE_SUCCESS, object);
	}

	/**
	 * <pre>
	 * 返回错误对象
	 * </pre>
	 * @param map{'codekey':'9999','messagekey',errorMessage}
	 * @return
	 */
	public static final AjaxResponseBody returnErrorBody(Object object)
	{
		return new AjaxResponseBody(TRANSACTION_RESPONSE_CODE_ref, TRANSACTION_RESPONSE_MESSAGE_REF, object);
	}

	/**
	 * <pre>
	 * 返回错误对象
	 * </pre>
	 * @param map{'codekey':'9999','messagekey',ex.getMessage()}
	 * @return
	 */
	public static final AjaxResponseBody returnErrorBody(Throwable ex)
	{
		return new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN, ex.getMessage());

	}

	/**
	 * <pre>
	 * 返回错误对象
	 * </pre>
	 * @param map{'codekey':'9999','messagekey',errorMessage}
	 * @return
	 */
	public static final AjaxResponseBody returnErrorBody(String errorMessage)
	{
		return new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN, errorMessage);
	}

	/**
	 * <pre>
	 * 返回错误对象
	 * </pre>
	 * @param {'codekey':errorCode,'messagekey',errorMessage}
	 * @param errorCode
	 * @param errorMessage
	 * @return
	 */
	public static final AjaxResponseBody returnErrorBody(String errorCode, String errorMessage)
	{
		return new AjaxResponseBody(errorCode, errorMessage);
	}


	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups)
	{
		try
		{
			BeanValidators.validateWithException(validator, object, groups);
		}
		catch (ConstraintViolationException ex)
		{
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[] {}));
			return false;
		}
		return true;
	}

	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups)
	{
		try
		{
			BeanValidators.validateWithException(validator, object, groups);
		}
		catch (ConstraintViolationException ex)
		{
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[] {}));
			return false;
		}
		return true;
	}

	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组，不传入此参数时，同@Valid注解验证
	 * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
	 */
	protected void beanValidator(Object object, Class<?>... groups)
	{
		BeanValidators.validateWithException(validator, object, groups);
	}

	/**
	 * 添加Model消息
	 * @param message
	 */
	protected void addMessage(Model model, String... messages)
	{
		StringBuilder sb = new StringBuilder();
		for (String message : messages)
		{
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		model.addAttribute("message", sb.toString());
	}

	/**
	 * 添加Flash消息
	 * @param message
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages)
	{
		StringBuilder sb = new StringBuilder();
		for (String message : messages)
		{
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}

	/**
	 * 客户端返回JSON字符串
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object)
	{
		return renderString(response, JsonUtils.toJson(object), "application/json");
	}

	/**
	 * 客户端返回字符串
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type)
	{
		try
		{
			response.reset();
			response.setContentType(type);
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		}
		catch (IOException e)
		{
			return null;
		}
	}

	

	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder)
	{
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport()
		{
			@Override
			public void setAsText(String text)
			{
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText()
			{
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
		{
			@Override
			public void setAsText(String text)
			{
				setValue(DateUtils.parseDate(text));
			}
			// @Override
			// public String getAsText() {
			// Object value = getValue();
			// return value != null ? DateUtils.formatDateTime((Date)value) : "";
			// }
		});
	}

	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request)
	{
		String remoteAddr = request.getHeader("X-Real-IP");
		if (StringUtils.isNotBlank(remoteAddr))
		{
			remoteAddr = request.getHeader("X-Forwarded-For");
		}
		else if (StringUtils.isNotBlank(remoteAddr))
		{
			remoteAddr = request.getHeader("Proxy-Client-IP");
		}
		else if (StringUtils.isNotBlank(remoteAddr))
		{
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}

	public static String redirect(String url)
	{
		return "redirect:" + url;
	}
}
