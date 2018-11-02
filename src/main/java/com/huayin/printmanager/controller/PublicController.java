/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.huayin.common.exception.ServiceException;
import com.huayin.common.exception.ServiceResult;
import com.huayin.common.exception.ServiceResultFactory;
import com.huayin.common.exception.TransException;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.ErrorCodeConstants;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.entity.basic.ExchangeRate;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.persist.enumerate.SmsType;
import com.huayin.printmanager.utils.CaptchaServlet;
import com.huayin.printmanager.utils.CookieUtils;
import com.huayin.printmanager.utils.ProcessingUtil;
import com.huayin.printmanager.utils.QRCodeUtils;
import com.huayin.printmanager.utils.ServletUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架 - 全局属性控制
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年5月18日, zhaojt
 * @version 	   2.0, 2018年2月27日下午2:25:35, zhengby, 代码规范
 */
@Controller
public class PublicController extends BaseController
{

	@Autowired
	private CookieLocaleResolver resolver;

	/**
	 * <pre>
	 * 生成条码
	 * </pre>
	 * @param content
	 * @param request
	 * @param response
	 * @since 1.0, 2018年2月27日 下午2:26:22, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/qrcode/{content}")
	public void viewQRcode(@PathVariable String content, HttpServletRequest request, HttpServletResponse response)
	{
		String createUrl = "";
		FileInputStream in = null;
		OutputStream out = null;
		try
		{
			createUrl = QRCodeUtils.getDir().concat(File.separator).concat(content + ".gif");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			out = response.getOutputStream();
			in = new FileInputStream(new File(createUrl));
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = in.read(b)) > 0)
			{
				out.write(b, 0, i);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				out.flush();
				out.close();
				in.close();
				out = null;
				in = null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * <pre>
	 * 选择语言
	 * </pre>
	 * @param language
	 * @param request
	 * @param response
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:26:56, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/language/{language}")
	public String selectLanguage(@PathVariable String language, HttpServletRequest request, HttpServletResponse response)
	{
		if (language.equals("zh_cn"))
		{
			resolver.setLocale(request, response, Locale.CHINA);
		}
		else if (language.equals("en"))
		{
			resolver.setLocale(request, response, Locale.ENGLISH);
		}
		else if (language.equals("ko"))
		{
			resolver.setLocale(request, response, Locale.KOREAN);
		}
		else
		{
			resolver.setLocale(request, response, Locale.CHINA);
		}
		return "redirect:" + basePath;
	}

	/**
	 * <pre>
	 * 获取主题方案
	 * </pre>
	 * @param theme
	 * @param request
	 * @param response
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:27:06, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response)
	{
		if (StringUtils.isNotBlank(theme))
		{
			CookieUtils.setCookie(response, "theme", theme);
		}
		else
		{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:" + basePath;
	}

	/**
	 * <pre>
	 * 发送手机验证码
	 * </pre>
	 * @param request
	 * @param captcha
	 * @param mobile
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:27:17, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/sendValidCode")
	@ResponseBody
	public ServiceResult<Object> sendMobileValidCode(HttpServletRequest request, @RequestParam(value = "captcha") String captcha, String mobile, SmsType type)
	{

		if (CaptchaServlet.validate(request, captcha))
		{
			Object lastTime = UserUtils.getSessionCache(SysConstants.SESSION_KEY_SMS_VALIDATECODE + type);
			if (lastTime != null && (System.currentTimeMillis() - Long.parseLong(lastTime.toString()) < (60 * 1000)))
			{// 距离上次发送时间小于1分钟，不能重复发送
				return ServiceResultFactory.getServiceResult(ErrorCodeConstants.CommonCode.COMMON_SMS_SEND_FREQUENTLY, "1分钟内只能发送1次");
			}
			StringBuffer validCode = new StringBuffer();
			Random random = new Random();
			for (int i = 0; i < 4; i++)
			{
				validCode.append(random.nextInt(10));
			}
			UserUtils.removeSessionCache(SysConstants.SESSION_KEY_SMS_MOBILE);
			UserUtils.removeSessionCache(SysConstants.SESSION_KEY_SMS_VALIDATECODE);
			UserUtils.removeSessionCache(SysConstants.SESSION_KEY_SMS_VALIDATECODE + type);

			UserUtils.putSessionCache(SysConstants.SESSION_KEY_SMS_MOBILE, mobile);
			UserUtils.putSessionCache(SysConstants.SESSION_KEY_SMS_VALIDATECODE, validCode.toString());
			UserUtils.putSessionCache(SysConstants.SESSION_KEY_SMS_VALIDATECODE + type, System.currentTimeMillis());

			serviceFactory.getSmsSendService().sendNow(mobile, type.createCodeMsg(validCode.toString()), type);

			return ServiceResultFactory.getServiceResult();
		}
		return ServiceResultFactory.getServiceResult(new TransException("图片验证码错误"));

	}

	/**
	 * <pre>
	 * 校验手机验证码
	 * </pre>
	 * @param mobile
	 * @param code
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:27:27, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/checkValidCode")
	@ResponseBody
	public boolean checkValidCode(String mobile, String code)
	{
		if (UserUtils.validateSmsValidCode(mobile, code))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * <pre>
	 * 判断是否有权限
	 * </pre>
	 * @param permission
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:27:37, zhengby
	 */
	@RequestMapping(value = "${basePath}/hasPermission")
	@ResponseBody
	public Boolean hasPermission(String permission)
	{
		return UserUtils.hasPermission(permission);
		// Subject subject = SecurityUtils.getSubject();
		// return subject.isPermitted(permission);
	}

	/**
	 * <pre>
	 * 判断是否有报价系统权限
	 * </pre>
	 * @param offerType
	 * @param permission
	 * @return
	 * @since 1.0, 2017年12月11日 上午11:47:37, think
	 */
	@RequestMapping(value = "${basePath}/hasOfferPermission")
	@ResponseBody
	public Boolean hasOfferPermission(String offerType, String permission)
	{
		return UserUtils.hasOfferPermission(offerType, permission);
	}

	/**
	 * <pre>
	 * 获取基础信息对象列表
	 * </pre>
	 * @param type
	 * @param field
	 * @param values
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:27:52, zhengby
	 */
	@RequestMapping(value = "${basePath}/basicInfoList")
	@ResponseBody
	public List<?> getBasicInfoList(String type, String field, String values)
	{
		return UserUtils.getBasicListParam(type, field, values);
	}

	/**
	 * <pre>
	 * 获取基础信息对象
	 * </pre>
	 * @param type
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:28:02, zhengby
	 */
	@RequestMapping(value = "${basePath}/basicInfo")
	@ResponseBody
	public Object getBasicInfo(String type, Long id)
	{
		return UserUtils.getBasicInfo(type, id);
	}

	/**
	 * <pre>
	 * 获取币别对象
	 * </pre>
	 * @param currencyType
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:28:14, zhengby
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "${basePath}/getExchangeRate")
	@ResponseBody
	public ExchangeRate getExchangeRate(CurrencyType currencyType)
	{

		ExchangeRate exchangeRate = new ExchangeRate();
		exchangeRate.setRate(new BigDecimal(1));

		List<ExchangeRate> beanList = (List<ExchangeRate>) UserUtils.getBasicList(BasicType.EXCHANGERATE.name());
		for (ExchangeRate bean : beanList)
		{
			if (bean.getCurrencyType() == currencyType && bean.getStandardCurrencyType() == UserUtils.getCompany().getStandardCurrency())
			{
				exchangeRate = bean;
			}
		}
		return exchangeRate;
		// return
		// serviceFactory.getCompanyService().getExchangeRate(currencyType);
	}

	/**
	 * <pre>
	 * 获取枚举对象
	 * </pre>
	 * @param className
	 * @param name
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:28:25, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/getEnum")
	@ResponseBody
	public Enum<?> getEnum(String className, String name)
	{
		Object itemsObject = null;
		try
		{
			Class<?> enumType = Class.forName(className);

			itemsObject = enumType.getEnumConstants();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		Object[] itemsArray = (Object[]) itemsObject;
		for (Object item : itemsArray)
		{
			if (((Enum<?>) item).name().equals(name))
			{
				return (Enum<?>) item;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * 获取枚举对象名称
	 * </pre>
	 * @param className
	 * @param name
	 * @param property
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:28:43, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/getEnumText")
	@ResponseBody
	public String getEnumText(String className, String name, String property)
	{
		Object itemsObject = null;
		try
		{
			Class<?> enumType = Class.forName(className);

			itemsObject = enumType.getEnumConstants();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		Object[] itemsArray = (Object[]) itemsObject;
		for (Object item : itemsArray)
		{
			if (((Enum<?>) item).name().equals(name))
			{
				BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
				return wrapper.getPropertyValue(property).toString();
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * 主菜单跳转到流程图
	 * </pre>
	 * @param workflow
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:29:13, zhengby
	 */
	@RequestMapping(value = "${basePath}/menu/{workflow}")
	public String menuWorkflow(@PathVariable String workflow)
	{
		return "navigation/" + workflow + "_nav";
	}

	/**
	 * <pre>
	 * 业务模块页面跳转控制器 如sale/order/create
	 * </pre>
	 * @param module
	 * @param business
	 * @param page
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:29:24, zhengby
	 */
	@RequestMapping(value = "${basePath}/{module}/{business}/{page}")
	public String module(@PathVariable String module, @PathVariable String business, @PathVariable String page, HttpServletRequest request)
	{
		// 组装参数
		Map<String, Object> params = new HashMap<String, Object>();
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements())
		{
			String name = names.nextElement();
			String value = request.getParameter(name);
			params.put(name, value);
		}
		request.setAttribute("params", params);
		return module.concat("/").concat(business).concat("/").concat(page);
	}

	/**
	 * <pre>
	 * 业务模块页面跳转控制器 如sale/order/create
	 * </pre>
	 * @param module
	 * @param business
	 * @param operate
	 * @param id
	 * @param map
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:29:38, zhengby
	 */
	@RequestMapping(value = "${basePath}/{module}/{business}/{operate}/{id}")
	public String module(@PathVariable String module, @PathVariable String business, @PathVariable String operate, @PathVariable String id, ModelMap map, HttpServletRequest request)
	{
		// 组装参数
		Map<String, Object> params = new HashMap<String, Object>();
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements())
		{
			String name = names.nextElement();
			String value = request.getParameter(name);
			params.put(name, value);
		}
		map.put("id", id);
		map.put("params", params);
		return module.concat("/").concat(business).concat("/").concat(operate);
	}

	/**
	 * <pre>
	 * 导入基础资料跳转
	 * </pre>
	 * @param filetype
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:30:07, zhengby
	 */
	@RequestMapping(value = "${basePath}/import/{filetype}")
	public String importFromExcel(@PathVariable String filetype, ModelMap map)
	{
		map.put("filetype", filetype);
		return "import/excel";
	}

	/**
	 * <pre>
	 * 导入基础资料处理
	 * </pre>
	 * @param filetype
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:30:17, zhengby
	 */
	@RequestMapping(value = "${basePath}/import/do")
	@ResponseBody
	public AjaxResponseBody importDo(String filetype, HttpServletRequest request)
	{
		try
		{
			if (filetype == null)
			{
				return returnErrorBody("上传失败：非法请求");
			}
			MultipartFile file = null;
			if (request instanceof MultipartHttpServletRequest)
			{
				MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
				MultiValueMap<String, MultipartFile> mvm = mr.getMultiFileMap();
				List<MultipartFile> list = mvm.get("excel");
				if (list != null && list.size() > 0)
				{
					file = list.get(0);
				}
			}
			if (file == null)
			{
				return returnErrorBody("上传失败：文件不能为空");
			}
			if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls"))
			{
				return returnErrorBody("上传失败：请重新上传后缀名为.xlsx或.xls！");
			}

			if (file.getSize() > 30000000)
			{
				return returnErrorBody("上传失败：文件大小不能超过3M");
			}
			if (BasicType.EMPLOYEE.getCacheName().equals(filetype))
			{
				Integer count = serviceFactory.getEmployeeService().importFromExcel(file.getInputStream());
				return returnSuccessBody("成功导入" + count + "记录");
			}
			if (BasicType.CUSTOMER.getCacheName().equals(filetype))
			{
				Integer count = serviceFactory.getCustomerService().importFromExcel(file.getInputStream());
				return returnSuccessBody("成功导入" + count + "记录");
			}
			if (BasicType.SUPPLIER.getCacheName().equals(filetype))
			{
				Integer count = serviceFactory.getSupplierService().importFromExcel(file.getInputStream());
				return returnSuccessBody("成功导入" + count + "记录");
			}
			if (BasicType.PROCEDURE.getCacheName().equals(filetype))
			{
				Integer count = serviceFactory.getProcedureService().importFromExcel(file.getInputStream());
				return returnSuccessBody("成功导入" + count + "记录");
			}
			if (BasicType.PRODUCT.getCacheName().equals(filetype))
			{
				Integer count = serviceFactory.getProductService().importFromExcel(file.getInputStream());
				return returnSuccessBody("成功导入" + count + "记录");
			}
			if (BasicType.MATERIAL.getCacheName().equals(filetype))
			{
				Integer count = serviceFactory.getMaterialService().importFromExcel(file.getInputStream());
				return returnSuccessBody("成功导入" + count + "记录");
			}
			returnErrorBody("处理失败：没有任何可处理的数据!");
		}
		catch (ServiceException e)
		{
			return returnErrorBody("处理失败:" + e.getMessage());
		}
		catch (Exception e)
		{
			return returnErrorBody("处理失败:后台业务逻辑发生异常，检查数据后重试.");
		}
		return null;

	}

	/**
	 * <pre>
	 * 导入基础资料处理
	 * </pre>
	 * @param filetype
	 * @param request
	 * @param response
	 * @since 1.0, 2018年2月27日 下午2:30:32, zhengby
	 */
	@RequestMapping(value = "${basePath}/downLoad/do")
	public void downLoad(String filetype, HttpServletRequest request, HttpServletResponse response)
	{
		FileInputStream in = null;
		OutputStream out = null;
		try
		{

			String fileName = null;

			if (BasicType.EMPLOYEE.getCacheName().equals(filetype))
			{
				fileName = "员工资料导入模板.xlsx";
			}
			if (BasicType.CUSTOMER.getCacheName().equals(filetype))
			{
				fileName = "客户导入模板.xlsx";
			}
			if (BasicType.SUPPLIER.getCacheName().equals(filetype))
			{
				fileName = "供应商导入模板.xlsx";
			}
			if (BasicType.PROCEDURE.getCacheName().equals(filetype))
			{
				fileName = "工序导入模板.xlsx";
			}
			if (BasicType.PRODUCT.getCacheName().equals(filetype))
			{
				fileName = "产品导入模板.xlsx";
			}
			if (BasicType.MATERIAL.getCacheName().equals(filetype))
			{
				fileName = "材料导入模板.xlsx";
			}
			String path = request.getSession().getServletContext().getRealPath(File.separator + "excelTemplate");
			File file = new File(path + File.separator + fileName);
			response.setContentType("application/ms-excel");
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			out = response.getOutputStream();
			in = new FileInputStream(file);
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = in.read(b)) > 0)
			{
				out.write(b, 0, i);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				out.flush();
				out.close();
				in.close();
				out = null;
				in = null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	/**
	 * <pre>
	 * 上传基础数据处理
	 * </pre>
	 * @param upfile
	 * @param request
	 * @param response
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:30:46, zhengby
	 */
	@RequestMapping(value = "${basePath}/attach/uploadFile")
	@ResponseBody
	public Map<String, String> uploadFile(@RequestParam(value = "upfile", required = false) MultipartFile upfile, HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, String> map = null;
		try
		{
			map = new HashMap<String, String>();
			String companyId = UserUtils.getCompanyId();
			String fileDirPath = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_BASE_REALPATH).concat(File.separator).concat(companyId);
			String fileName = upfile.getOriginalFilename();
			new File(fileDirPath).mkdirs();
			File destDir = new File(fileDirPath.concat(File.separator).concat(fileName));
			upfile.transferTo(destDir);
			String prefixUrl = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_URL) + "/" + companyId + "/";
			map.put("state", "SUCCESS");
			map.put("url", prefixUrl.concat(fileName));
			map.put("title", fileName);
			map.put("original", fileName);
		}
		catch (IllegalStateException e)
		{
			map.put("state", "上传文件发生系统异常！");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			map.put("state", "上传文件失败!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * <pre>
	 * 获取基础信息对象
	 * </pre>
	 * @param request
	 * @param response
	 * @param fileName
	 * @since 1.0, 2018年2月27日 下午2:31:03, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/downLoad")
	public void downLoadFile(HttpServletRequest request, HttpServletResponse response, String fileName)
	{
		ServletUtils.download(request, response, fileName);
	}

	/**
	 * <pre>
	 * 获取批量分配权限保存中的进度（公共调用）
	 * </pre>
	 * @param key
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:31:17, zhengby
	 */
	@RequestMapping(value = "${basePath}/public/permissions_batch_process")
	@ResponseBody
	public String getPermissionsBatchSaveProcessing(@RequestParam("key") String key)
	{
		return ProcessingUtil.get(key) + "";
	}
}
