/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.sys;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.plugin.PageSupport;
import com.huayin.printmanager.sms.SmsSendFactory;

/**
 * <pre>
 * 系统模块 - 短信渠道
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/smspartner")
public class SmsPartnerController extends BaseController
{

	/**
	 * <pre>
	 * 页面 - 短信渠道新增
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:57:04, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sys:smspartner:create")
	@AdminAuth
	public String create()
	{

		return "sys/smspartner/create";
	}

	/**
	 * <pre>
	 * 功能 - 短信渠道新增
	 * </pre>
	 * @param smsPartner
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:57:22, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sys:smspartner:edit")
	@AdminAuth
	public AjaxResponseBody save(@RequestBody SmsPartner smsPartner, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(smsPartner.getName(), smsPartner.getId()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		SmsPartner _smsPartner = serviceFactory.getSmsPartnerService().getByName(smsPartner.getName());

		if (!Validate.validateObjectsNullOrEmpty(_smsPartner))
		{
			return returnErrorBody("名称已经存在！");
		}

		_smsPartner = serviceFactory.getSmsPartnerService().get(smsPartner.getId());
		if (!Validate.validateObjectsNullOrEmpty(_smsPartner))
		{
			return returnErrorBody("渠道编号已经存在！");
		}
		SmsPartner new_smsPartner = new SmsPartner();
		new_smsPartner.setId(smsPartner.getId());
		new_smsPartner.setName(smsPartner.getName());
		new_smsPartner.setExtConfigs(smsPartner.getExtConfigs());
		new_smsPartner.setRemark(smsPartner.getRemark());
		new_smsPartner.setCreateTime(new Date());
		new_smsPartner.setPartnerId(smsPartner.getPartnerId());
		new_smsPartner.setSecretKey(smsPartner.getSecretKey());
		new_smsPartner.setSmsSendType(smsPartner.getSmsSendType());
		new_smsPartner.setState(smsPartner.getState());
		new_smsPartner.setPriority(smsPartner.getPriority());
		serviceFactory.getSmsPartnerService().save(new_smsPartner);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 短信渠道修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:57:40, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sys:smspartner:edit")
	@AdminAuth
	public String edit(@PathVariable Long id, ModelMap map)
	{

		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		SmsPartner smsPartner = serviceFactory.getSmsPartnerService().get(id);
		map.put("smsPartner", smsPartner);
		return "sys/smspartner/edit";
	}

	/**
	 * <pre>
	 * 功能 - 短信渠道修改
	 * </pre>
	 * @param smsPartner
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:57:55, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sys:smspartner:edit")
	@AdminAuth
	public AjaxResponseBody update(@RequestBody SmsPartner smsPartner, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(smsPartner.getName()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		SmsPartner _smsPartner = serviceFactory.getSmsPartnerService().get(smsPartner.getId());

		SmsPartner obj = serviceFactory.getSmsPartnerService().getByName(smsPartner.getName());

		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (obj.getId().longValue() != smsPartner.getId().longValue())
			{
				return returnErrorBody("渠道名称已经存在");
			}
		}

		_smsPartner.setName(smsPartner.getName());
		_smsPartner.setName(smsPartner.getName());
		_smsPartner.setExtConfigs(smsPartner.getExtConfigs());
		_smsPartner.setRemark(smsPartner.getRemark());
		_smsPartner.setPartnerId(smsPartner.getPartnerId());
		_smsPartner.setSecretKey(smsPartner.getSecretKey());
		_smsPartner.setSmsSendType(smsPartner.getSmsSendType());
		_smsPartner.setState(smsPartner.getState());
		_smsPartner.setPriority(smsPartner.getPriority());
		serviceFactory.getSmsPartnerService().update(_smsPartner);

		SmsSendFactory.clearGateway(smsPartner.getId());
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 短信渠道删除
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:58:05, think
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("sys:smspartner:del")
	@AdminAuth
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			serviceFactory.getSmsPartnerService().delete(id);

			SmsSendFactory.clearGateway(id);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * <pre>
	 * 页面 - 短信渠道列表
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param smsPartnerName
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:58:21, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:smspartner:list")
	@AdminAuth
	public String list(@DateTimeFormat(iso = ISO.DATE) Date dateMin, @DateTimeFormat(iso = ISO.DATE) Date dateMax, String smsPartnerName, HttpServletRequest request, ModelMap map)
	{
		map.put("dateMin", request.getParameter("dateMin"));
		map.put("dateMax", request.getParameter("dateMax"));
		map.put("smsPartnerName", smsPartnerName);
		PageSupport<SmsPartner> page = new PageSupport<SmsPartner>(request);
		SearchResult<SmsPartner> result = serviceFactory.getSmsPartnerService().findByCondition(dateMin, dateMax, smsPartnerName, page.getPageNo(), page.getPageSize());
		page.setSearchResult(result);
		map.put("page", page);
		return "sys/smspartner/list";
	}
}
