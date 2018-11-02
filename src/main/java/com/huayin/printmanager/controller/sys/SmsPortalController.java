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
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.util.PropertyClone;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.persist.entity.sys.SmsPortal;
import com.huayin.printmanager.persist.enumerate.SmsPartnerState;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 短信供应商
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/smsportal")
public class SmsPortalController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 短信供应商新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:00:43, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sys:smsportal:create")
	@AdminAuth
	public String create(ModelMap map)
	{
		List<SmsPartner> partnerList = serviceFactory.getSmsPartnerService().list(SmsPartnerState.OPEN);
		map.put("partnerList", partnerList);
		return "sys/smsportal/create";
	}

	/**
	 * <pre>
	 * 功能 - 短信供应商新增
	 * </pre>
	 * @param smsportal
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:00:59, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sys:smsportal:edit")
	@AdminAuth
	public AjaxResponseBody save(@RequestBody SmsPortal smsportal, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(smsportal.getName(), smsportal.getAccountId(), smsportal.getSecretkey(), smsportal.getPartnerId(), smsportal.getSign(), smsportal.getState()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		SmsPortal _smsportal = serviceFactory.getSmsPortalService().getByAccountId(smsportal.getAccountId());

		if (_smsportal != null)
		{
			return returnErrorBody("接入编号已经存在！");
		}
		smsportal.setCreateTime(new Date());
		smsportal.setCreateName(UserUtils.getUserName());
		serviceFactory.getSmsPortalService().save(smsportal);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 短信供应商修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:01:10, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sys:smsportal:edit")
	@AdminAuth
	public String edit(@PathVariable Long id, ModelMap map)
	{

		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		SmsPortal smsportal = serviceFactory.getSmsPortalService().get(id);

		List<SmsPartner> partnerList = serviceFactory.getSmsPartnerService().list(SmsPartnerState.OPEN);
		map.put("partnerList", partnerList);
		map.put("smsportal", smsportal);
		return "sys/smsportal/edit";
	}

	/**
	 * <pre>
	 * 功能 - 短信供应商修改
	 * </pre>
	 * @param smsportal
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:01:21, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sys:smsportal:edit")
	@AdminAuth
	public AjaxResponseBody update(@RequestBody SmsPortal smsportal, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(smsportal.getName(), smsportal.getAccountId(), smsportal.getSecretkey(), smsportal.getPartnerId(), smsportal.getSign(), smsportal.getState()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		SmsPortal _smsportal = serviceFactory.getSmsPortalService().getByAccountId(smsportal.getAccountId());

		if (_smsportal != null && _smsportal.getId() != smsportal.getId())
		{
			return returnErrorBody("接入编号已经存在！");
		}
		SmsPortal oldSmsPortal = serviceFactory.getSmsPortalService().get(smsportal.getId());
		PropertyClone.copyProperties(oldSmsPortal, smsportal, false);// 替换新内容
		oldSmsPortal.setUpdateName(UserUtils.getUserName());
		oldSmsPortal.setUpdateTime(new Date());

		serviceFactory.getSmsPortalService().update(oldSmsPortal);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 短信供应商删除
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:01:30, think
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("sys:smsportal:del")
	@AdminAuth
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}

		try
		{
			serviceFactory.getSmsPortalService().delete(id);
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
	 * 页面 - 短信供应商列表
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:01:41, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:smsportal:list")
	@AdminAuth
	public String list(ModelMap map)
	{
		List<SmsPortal> result = serviceFactory.getSmsPortalService().list();
		map.put("result", result);
		return "sys/smsportal/list";
	}

	/**
	 * <pre>
	 * 页面 - 测试发送页面
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:01:58, think
	 */
	@RequestMapping(value = "test")
	@AdminAuth
	public String test(ModelMap map)
	{
		return "sys/smsportal/test";
	}

}
