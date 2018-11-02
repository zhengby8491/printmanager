/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.basic;

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
import com.huayin.printmanager.persist.entity.basic.Position;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 职位设置
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月21日 上午9:41:48
 * @since        2.0, 2017年12月22日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/position")
public class PositionController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 包装方式列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:44:25, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:position:list")
	public String list()
	{
		return "basic/position/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:44:52, zhengby
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<Position> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getPositionService().findByCondition(queryParam);
	}
	
	/**
	 * <pre>
	 * 页面 - 跳转到新增包装方式
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:42:59, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:position:create")
	public String create()
	{
		return "basic/position/create";
	}

	/**
	 * <pre>
	 * 功能- 新增
	 * </pre>
	 * @param position
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:43:21, zhengby
	 * @since 2.0, 2017年12月22日 下午17:07:00, think, 规范和国际化
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:position:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "职位信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Position position, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(position.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Position obj = serviceFactory.getPositionService().getByName(position.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		Position _position = new Position();
		_position.setCompanyId(UserUtils.getCompanyId());
		_position.setName(position.getName());
		_position.setSort(position.getSort());
		_position.setMemo(position.getMemo());
		serviceFactory.getPositionService().save(_position);

		UserUtils.clearCacheBasic(BasicType.POSITION);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 跳转到修改包装方式
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:43:34, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:position:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Position position = serviceFactory.getPositionService().get(id);
		map.put("position", position);
		return "basic/position/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改包装方式
	 * </pre>
	 * @param position
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:43:51, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:position:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "职位信息", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody Position position, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(position.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Position _position = serviceFactory.getPositionService().get(position.getId());
		Position obj = serviceFactory.getPositionService().getByName(position.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_position.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_position.setName(position.getName());
		_position.setSort(position.getSort());
		_position.setMemo(position.getMemo());
		serviceFactory.getPositionService().update(_position);
		
		UserUtils.clearCacheBasic(BasicType.POSITION);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除包装方式
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:44:08, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:position:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "职位信息", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.POSITION, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
