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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.sys.Advertisement;
import com.huayin.printmanager.persist.entity.sys.AdvertisementAccesslog;
import com.huayin.printmanager.persist.enumerate.AdvertisementType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.JedisUtils;

/**
 * <pre>
 * 系统模块 - 广告管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/advertisement")
public class AdvertisementController extends BaseController
{
	/**
	 * 广告访问IP控制时间间隔2分钟
	 */
	private int ipSeconds = 2 * 60;

	/**
	 * <pre>
	 * 页面 - 广告新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:10:07, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sys:advertisement:create")
	@AdminAuth
	public String create(ModelMap map)
	{
		return "sys/advertisement/create";
	}

	/**
	 * <pre>
	 * 功能 - 广告新增
	 * </pre>
	 * @param advertisement
	 * @param request
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:09:25, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sys:advertisement:create")
	@AdminAuth
	public AjaxResponseBody save(@ModelAttribute("advertisement") Advertisement advertisement, MultipartHttpServletRequest request)
	{
		// advertisement.setPhotoUrl("http://" + request.getServerName() + ":" + request.getServerPort());
		MultipartFile file = request.getFile("pic");
		serviceFactory.getAdvertisementService().save(advertisement, file);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 广告修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:08:13, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sys:advertisement:edit")
	@AdminAuth
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		Advertisement advertisement = serviceFactory.getAdvertisementService().get(id);
		map.put("advertisement", advertisement);
		return "sys/advertisement/edit";
	}

	/**
	 * <pre>
	 * 功能 - 广告修改
	 * </pre>
	 * @param advertisement
	 * @param request
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:08:04, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sys:advertisement:edit")
	@AdminAuth
	public AjaxResponseBody update(@ModelAttribute("advertisement") Advertisement advertisement, HttpServletRequest request)
	{
		MultipartFile file = null;
		if (request instanceof MultipartHttpServletRequest)
		{
			file = ((MultipartHttpServletRequest) request).getFile("pic");
			/*
			 * if(file!=null){ advertisement.setPhotoUrl("http://" + request.getServerName() + ":" +
			 * request.getServerPort()); }
			 */
		}
		serviceFactory.getAdvertisementService().update(advertisement, file);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 广告删除
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:10:33, think
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("sys:advertisement:del")
	@AdminAuth
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		serviceFactory.getAdvertisementService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 广告列表
	 * </pre>
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年10月25日 下午2:07:16, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:advertisement:list")
	public String list() throws Exception
	{
		return "sys/advertisement/list";
	}

	/**
	 * <pre>
	 * Ajax列表 - 广告列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:06:46, think
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("sys:advertisement:list")
	@ResponseBody
	public SearchResult<Advertisement> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<Advertisement> result = serviceFactory.getAdvertisementService().findByCondition(queryParam);
		return result;
	}

	/**
	 * Ajax列表 - 广告访问日志
	 */
	@RequestMapping(value = "ajaxAccesslogList")
	@RequiresPermissions("sys:advertisementAccesslog:list")
	@ResponseBody
	public SearchResult<AdvertisementAccesslog> ajaxAccesslogList(@RequestBody QueryParam queryParam)
	{
		SearchResult<AdvertisementAccesslog> result = serviceFactory.getAdvertisementService().findAccesslogByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * Ajax列表 - 所有已发布广告
	 * </pre>
	 * @param advertisementType
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:05:42, think
	 */
	@RequestMapping(value = "ajaxPublisList")
	@ResponseBody
	public List<Advertisement> ajaxPublisList(@RequestParam(required = true) AdvertisementType advertisementType)
	{
		List<Advertisement> result = serviceFactory.getAdvertisementService().findAllPublish(advertisementType);
		return result;
	}

	/**
	 * <pre>
	 * Ajax列表 - 点击统计
	 * </pre>
	 * @param id
	 * @param ip
	 * @param address
	 * @param request
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:05:32, think
	 */
	@RequestMapping(value = "ajaxStatistics")
	@ResponseBody
	public AjaxResponseBody ajaxStatistics(@RequestParam(required = true) Long id, String ip, String address, HttpServletRequest request)
	{
		String iip = request.getHeader("x-forwarded-for");
		if (iip == null || iip.length() == 0 || "unknown".equalsIgnoreCase(iip))
		{
			iip = request.getHeader("Proxy-Client-IP");
		}
		if (iip == null || iip.length() == 0 || "unknown".equalsIgnoreCase(iip))
		{
			iip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (iip == null || iip.length() == 0 || "unknown".equalsIgnoreCase(iip))
		{
			iip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (iip == null || iip.length() == 0 || "unknown".equalsIgnoreCase(iip))
		{
			iip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (iip == null || iip.length() == 0 || "unknown".equalsIgnoreCase(iip))
		{
			iip = request.getRemoteAddr();
		}
		if (JedisUtils.exists(iip + id.toString()))
		{
			return returnSuccessBody();
		}
		serviceFactory.getAdvertisementService().statistics(id, ip, address, iip);
		JedisUtils.set(iip + id.toString(), id.toString(), ipSeconds);
		return returnSuccessBody();
	}

}
