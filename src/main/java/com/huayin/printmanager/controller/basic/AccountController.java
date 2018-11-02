/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月26日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.basic;

import java.math.BigDecimal;
import java.util.Date;

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
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.Account;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 账号信息
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/account")
public class AccountController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 账户信息列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:30:51, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:account:list")
	public String list()
	{
		return "basic/account/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:31:11, zhengby
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<Account> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getAccountService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 账户新增
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:23:54, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:account:create")
	public String create()
	{
		return "basic/account/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增账户
	 * </pre>
	 * @param account
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:24:39, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:account:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "账户信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Account account, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(account.getBankNo(), account.getBranchName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Account obj = serviceFactory.getAccountService().getByBankNo(account.getBankNo());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}

		Account _account = new Account();
		_account.setCompanyId(UserUtils.getCompanyId());
		_account.setBankType(account.getBankType());
		_account.setBranchName(account.getBranchName());
		_account.setBankNo(account.getBankNo());
		_account.setCurrencyType(account.getCurrencyType());
		_account.setAccountType(account.getAccountType());
		_account.setSort(account.getSort());
		_account.setCreateName(UserUtils.getUser().getUserName());
		_account.setCreateTime(new Date());
		_account.setMemo(account.getMemo());
		_account.setMoney(new BigDecimal(0));
		Account _accountNew = serviceFactory.getAccountService().save(_account);
		return returnSuccessBody(_accountNew);
	}

	/**
	 * <pre>
	 * 页面 - 账户信息修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:25:34, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:account:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Account account = serviceFactory.getAccountService().get(id);
		map.put("account", account);
		return "basic/account/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改账户信息
	 * </pre>
	 * @param account
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:29:57, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:account:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "账户信息", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody Account account, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(account.getBankNo(), account.getBranchName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}

		Account _account = serviceFactory.getAccountService().lock(account.getId());
		Account obj = serviceFactory.getAccountService().getByBankNo(account.getBankNo());

		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_account.getId().longValue() != obj.getId())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}

		_account.setBankType(account.getBankType());
		_account.setBranchName(account.getBranchName());
		_account.setBankNo(account.getBankNo());
		_account.setCurrencyType(account.getCurrencyType());
		_account.setAccountType(account.getAccountType());
		_account.setSort(account.getSort());
		_account.setUpdateName(UserUtils.getUser().getUserName());
		_account.setUpdateTime(new Date());
		_account.setMemo(account.getMemo());
		serviceFactory.getAccountService().update(_account);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除账户信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:29:33, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:account:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "账户信息", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnSuccessBody(false);
		}
		boolean delete = serviceFactory.getCommonService().delete(BasicType.ACCOUNT, id);
		return returnSuccessBody(delete);
	}

}
