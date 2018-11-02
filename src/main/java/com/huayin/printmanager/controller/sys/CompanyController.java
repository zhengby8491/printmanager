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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.exception.OperatorException;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.sys.AgentQuotient;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.enumerate.InitStep;
import com.huayin.printmanager.service.sys.vo.CompanyVo;
import com.huayin.printmanager.service.sys.vo.RegisterInitVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.MenuUtils;
import com.huayin.printmanager.utils.ProcessingUtil;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 公司管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/company")
public class CompanyController extends BaseController
{
	/**
	 * <pre>
	 * 功能 - 初始化公司信息
	 * </pre>
	 * @param request
	 * @param map
	 * @param company
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:16:22, think
	 */
	@RequestMapping(value = "initCompany", method = RequestMethod.POST)
	public String initCompany(HttpServletRequest request, ModelMap map, Company company)
	{
		Company old_company = serviceFactory.getCompanyService().get(UserUtils.getCompany().getId());

		if (Validate.validateObjectsNullOrEmpty(company, company.getName()))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		if (serviceFactory.getCompanyService().existCompanyName(company.getName(), UserUtils.getCompany().getId()))
		{
			return returnErrorPage(map, "公司名已经存在！");
		}

		if (company.getProvince().equals("请选择"))
		{
			old_company.setProvince(null);
			old_company.setCity(null);
			old_company.setCounty(null);
		}
		else
		{
			old_company.setProvince(company.getProvince());
			old_company.setCity(company.getCity());
			old_company.setCounty(company.getCounty());
		}

		old_company.setInitStep(InitStep.INIT_BASIC);
		old_company.setName(company.getName());
		old_company.setEmail(company.getEmail());
		old_company.setLinkName(company.getLinkName());
		old_company.setAddress(company.getAddress());
		old_company.setTel(company.getTel());
		old_company.setFax(company.getFax());
		serviceFactory.getPersistService().update(old_company);
		UserUtils.updateSessionUser();
		return "init/basic_init";
	}

	/**
	 * <pre>
	 * 功能 - 初始化基础资料
	 * </pre>
	 * @param initVo
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:16:42, think
	 */
	@RequestMapping(value = "initBasic", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResponseBody initBasic(@RequestBody RegisterInitVo initVo, ModelMap map)
	{
		serviceFactory.getCompanyService().initBasic(initVo);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 清理所有数据（基础资料数据和单据数据）
	 * </pre>
	 * @param companyId
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:17:02, think
	 */
	@RequestMapping(value = "clearAllData", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("sys:company:clearAllData")
	@AdminAuth
	public AjaxResponseBody clearAllData(String companyId, ModelMap map)
	{
		serviceFactory.getCommonService().clearAllData(companyId);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 清理所有单据数据
	 * </pre>
	 * @param companyId
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:17:13, think
	 */
	@RequestMapping(value = "clearAllBillData", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("sys:company:clearAllBillData")
	@AdminAuth
	public AjaxResponseBody clearAllBillData(String companyId, ModelMap map)
	{
		serviceFactory.getCommonService().clearAllBillData(companyId);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 重设公司初始化状态
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2018年9月22日 下午4:20:00, zhengby
	 */
	@RequestMapping(value = "resetCompanyState", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("sys:company:resetCompanyState")
	@AdminAuth            
	public AjaxResponseBody resetCompanyState(String companyId)
	{
		if (serviceFactory.getCommonService().resetCompanyState(InitStep.INIT_BASIC, companyId))
		{
			return returnSuccessBody();
		} else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}
	
	/**
	 * <pre>
	 * 页面 - 分配权限
	 * </pre>
	 * @param companyId
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:17:22, think
	 */
	@RequestMapping(value = "permissions/{companyId}")
	@RequiresPermissions("sys:company:permissions")
	@AdminAuth
	public String permissions(@PathVariable String companyId, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(companyId))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		List<Menu> allMenuList = serviceFactory.getMenuService().findAll();
		allMenuList = MenuUtils.buildTree(allMenuList, MenuUtils.TREE_ROOT_ID);

		Map<Long, Long> hasMenuIdMap = new HashMap<Long, Long>();
		List<Menu> hasMenuList = serviceFactory.getMenuService().findAll(companyId);
		for (Menu m : hasMenuList)
		{
			hasMenuIdMap.put(m.getId(), m.getId());
		}
		map.put("allMenuList", allMenuList);
		map.put("hasMenuIdMap", hasMenuIdMap);
		map.put("companyId", companyId);
		return "sys/company/permissions";
	}

	/**
	 * <pre>
	 * 页面 - 批量分配权限
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:17:39, think
	 */
	@RequestMapping(value = "permissionsBatch")
	@RequiresPermissions("sys:company:permissions")
	@AdminAuth
	public String permissionsBatch(ModelMap map)
	{
		List<Menu> allMenuList = serviceFactory.getMenuService().findAll();
		allMenuList = MenuUtils.buildTree(allMenuList, MenuUtils.TREE_ROOT_ID);

		// Map<Long, Long> hasMenuIdMap = new HashMap<Long, Long>();
		// List<Menu> hasMenuList = serviceFactory.getMenuService().findAll();
		// for (Menu m : hasMenuList)
		// {
		// hasMenuIdMap.put(m.getId(), m.getId());
		// }
		map.put("allMenuList", allMenuList);
		// map.put("hasMenuIdMap", hasMenuIdMap);
		return "sys/company/permissions_batch";
	}

	/**
	 * <pre>
	 * 页面 - 批量追加权限
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:17:51, think
	 */
	@RequestMapping(value = "permissionsAppendBatch")
	@RequiresPermissions("sys:company:permissions")
	@AdminAuth
	public String permissionsAppendBatch(ModelMap map)
	{
		List<Menu> allMenuList = serviceFactory.getMenuService().findAll();
		allMenuList = MenuUtils.buildTree(allMenuList, MenuUtils.TREE_ROOT_ID);

		// Map<Long, Long> hasMenuIdMap = new HashMap<Long, Long>();
		// List<Menu> hasMenuList = serviceFactory.getMenuService().findAll();
		// for (Menu m : hasMenuList)
		// {
		// hasMenuIdMap.put(m.getId(), m.getId());
		// }
		map.put("allMenuList", allMenuList);
		// map.put("hasMenuIdMap", hasMenuIdMap);
		return "sys/company/permissions_append_batch";
	}

	/**
	 * <pre>
	 * 功能 - 更新公司权限
	 * </pre>
	 * @param company
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:18:03, think
	 */
	@RequestMapping(value = "permissions_save")
	@ResponseBody
	@RequiresPermissions("sys:company:permissions")
	@AdminAuth
	public AjaxResponseBody permissionsSave(@RequestBody Company company, ModelMap map)
	{
		try
		{
			if (Validate.validateObjectsNullOrEmpty(company, company.getId()))
			{
				return returnErrorBody("公司ID不能为空");
			}
			// 更新公司菜单权限
			serviceFactory.getMenuService().updateCompanyMenu(company.getId(), company.getMenuIdList());

			UserUtils.clearCachePermission(company.getId());
			return returnSuccessBody();
		}
		catch (OperatorException ex)
		{
			ex.printStackTrace();
			return returnErrorBody(ex);
		}
	}

	/**
	 * <pre>
	 * 功能 - 批量更新公司权限
	 * </pre>
	 * @param key
	 * @param ids
	 * @param menuIds
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:18:28, think
	 */
	@RequestMapping(value = "permissions_batch_save")
	@ResponseBody
	@RequiresPermissions("sys:company:permissions")
	@AdminAuth
	public AjaxResponseBody permissionsBatchSave(@RequestParam("key") String key, @RequestParam("ids[]") List<String> ids, @RequestParam("menuIds[]") List<Long> menuIds, ModelMap map)
	{
		try
		{
			if (Validate.validateObjectsNullOrEmpty(ids, menuIds))
			{
				return returnErrorBody("公司IDS不能为空");
			}
			// 更新公司菜单权限(TODO 批量更新)
			int index = 1;
			for (String id : ids)
			{

				serviceFactory.getMenuService().updateCompanyMenu(id, menuIds);
				UserUtils.clearCachePermission(id);

				// 每处理一条，则提醒更新
				ProcessingUtil.put(key, index++);
			}
			// 清空
			ProcessingUtil.remove(key);

			return returnSuccessBody();
		}
		catch (OperatorException ex)
		{
			ex.printStackTrace();
			return returnErrorBody(ex);
		}
	}

	/**
	 * <pre>
	 * 功能 - 批量追加公司权限
	 * </pre>
	 * @param key
	 * @param ids
	 * @param menuIds
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:18:43, think
	 */
	@RequestMapping(value = "permissions_append_batch_save")
	@ResponseBody
	@RequiresPermissions("sys:company:permissions")
	@AdminAuth
	public AjaxResponseBody permissionsAppendBatchSave(@RequestParam("key") String key, @RequestParam("ids[]") List<String> ids, @RequestParam("menuIds[]") List<Long> menuIds, ModelMap map)
	{
		try
		{
			if (Validate.validateObjectsNullOrEmpty(ids, menuIds))
			{
				return returnErrorBody("公司IDS不能为空");
			}
			// 更新公司菜单权限(TODO 批量更新)
			int index = 1;
			for (String id : ids)
			{

				serviceFactory.getMenuService().appendCompanyMenu(id, menuIds);
				UserUtils.clearCachePermission(id);

				// 每处理一条，则提醒更新
				ProcessingUtil.put(key, index++);
			}
			// 清空
			ProcessingUtil.remove(key);

			return returnSuccessBody();
		}
		catch (OperatorException ex)
		{
			ex.printStackTrace();
			return returnErrorBody(ex);
		}
	}

	/**
	 * <pre>
	 * 页面 - 公司查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:19:02, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("sys:company:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable String id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		if (UserUtils.isSystemCompany() || UserUtils.getCompanyId().equals(id))
		{// 管理员或自己才能查看
			Company company = serviceFactory.getCompanyService().get(id);
			map.put("company", company);
			return "sys/company/view";
		}
		else
		{
			return returnErrorPage(map, "权限非法");
		}

	}

	/**
	 * <pre>
	 * 页面 - 公司编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:19:19, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sys:company:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable String id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		if (UserUtils.getCompanyId().equals(id))
		{// 只有登录用户所在的公司才能编辑本公司，系统管理员也不能编辑他人公司资料
			Company company = serviceFactory.getCompanyService().get(id);
			map.put("company", company);
			return "sys/company/edit";
		}
		else
		{
			return returnErrorPage(map, "权限非法！");
		}
	}

	/**
	 * <pre>
	 * 功能 - 公司更新
	 * </pre>
	 * @param company
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:19:37, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sys:company:edit")
	public AjaxResponseBody update(@RequestBody Company company, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(company, company.getId(), company.getName()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		if ((!UserUtils.isSystemCompany()) && !UserUtils.getCompanyId().equals(company.getId()))
		{// 非系统管理公司不能修改其他公司资料
			return returnErrorBody("权限非法！");
		}
		Company old_company = serviceFactory.getCompanyService().get(company.getId());
		if (serviceFactory.getCompanyService().existCompanyName(company.getName(), company.getId()))
		{
			return returnErrorBody("公司名已经存在！");
		}

		old_company.setName(company.getName());
		old_company.setTel(company.getTel());
		old_company.setFax(company.getFax());
		old_company.setLinkName(company.getLinkName());
		old_company.setAddress(company.getAddress());
		old_company.setEmail(company.getEmail());
		old_company.setIntroduction(company.getIntroduction());
		old_company.setWeixin(company.getWeixin());
		old_company.setWebsite(company.getWebsite());
		old_company.setUpdateName(UserUtils.getUserName());
		old_company.setAgentQuotientId(company.getAgentQuotientId());
		old_company.setUpdateTime(new Date());
		old_company.setIsOem(company.getIsOem());
		serviceFactory.getPersistService().update(old_company);

		if (old_company.getId().equals(UserUtils.getCompanyId()))
		{// 修改本身，需要更新SESSION信息
			// UserUtils.getUser().setCompany(old_company);
			UserUtils.updateSessionUser();
		}

		return returnSuccessBody();

	}

	/**
	 * <pre>
	 * 页面 - 公司维护
	 * </pre>
	 * @param request
	 * @param map
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:19:48, think
	 */
	@RequestMapping(value = "maintain/{companyId}")
	@RequiresPermissions("sys:company:maintain")
	@AdminAuth
	public String maintain(HttpServletRequest request, ModelMap map, @PathVariable String companyId)
	{

		if (Validate.validateObjectsNullOrEmpty(companyId))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		List<AgentQuotient> agentList = serviceFactory.getAgentQuotientService().findAgentQuotient();
		Company company = serviceFactory.getCompanyService().get(companyId);
		map.put("company", company);
		map.put("agentList", agentList);
		return "sys/company/maintain";

	}

	/**
	 * <pre>
	 * 功能 - 公司维护修改
	 * </pre>
	 * @param company
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:20:04, think
	 */
	@RequestMapping(value = "maintenUpdate")
	@ResponseBody
	@RequiresPermissions("sys:company:maintain")
	@AdminAuth
	public AjaxResponseBody maintenUpdate(Company company, ModelMap map)
	{
		Company _company = serviceFactory.getCompanyService().get(company.getId());
		if (Validate.validateObjectsNullOrEmpty(company.getContractCompanyName()))
		{

			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		if (Validate.validateObjectsNullOrEmpty(_company))
		{

			return returnErrorBody("公司不存在！");
		}
		_company.setContractCompanyName(company.getContractCompanyName());
		_company.setContractTime(company.getContractTime());
		_company.setIsFormal(company.getIsFormal());
		_company.setExpireTime(company.getExpireTime());
		_company.setAgentQuotientId(company.getAgentQuotientId());
		_company.setRoleCountMax(company.getRoleCountMax());
		_company.setSystemVersion(company.getSystemVersion());
		_company.setIsOem(company.getIsOem());
		serviceFactory.getPersistService().update(_company);

		if (_company.getId().equals(UserUtils.getCompanyId()))
		{// 修改本身，需要更新SESSION信息
			// UserUtils.getUser().setCompany(_company);
			UserUtils.updateSessionUser();
		}

		return returnSuccessBody();

	}

	/**
	 * <pre>
	 * 功能 - 校验公司名是否存在
	 * </pre>
	 * @param name
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:20:32, think
	 */
	@RequestMapping(value = "existForRegister")
	@ResponseBody
	public boolean existForRegister(String name, ModelMap map)
	{
		return !serviceFactory.getCompanyService().existCompanyName(name.trim(), null);
	}

	/**
	 * <pre>
	 * 功能 - 校验公司名是否存在
	 * </pre>
	 * @param name
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:20:46, think
	 */
	@RequestMapping(value = "existForEdit")
	@ResponseBody
	public boolean existForEdit(String name, ModelMap map)
	{
		return !serviceFactory.getCompanyService().existCompanyName(name.trim(), UserUtils.getCompanyId());
	}

	/**
	 * <pre>
	 * 页面 - 公司列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年10月25日 下午3:21:06, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:company:list")
	public String list(QueryParam queryParam) throws Exception
	{
		return "sys/company/list";

	}

	/**
	 * <pre>
	 * Ajax列表 - 公司列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:21:21, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<CompanyVo> ajaxList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getCompanyService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 公共 - 单独初始化单位换算
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:21:32, think
	 */
	@RequestMapping(value = "initUnitConvert")
	public AjaxResponseBody initUnitConvert()
	{
		serviceFactory.getCompanyService().initUnitConvert();
		return returnSuccessBody();
	}
}
