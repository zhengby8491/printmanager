/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月11日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.TemplateDataModel;
import com.huayin.printmanager.persist.entity.sys.TemplateModel;
import com.huayin.printmanager.persist.enumerate.PrintModleName;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 框架 - 自定义模板
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月11日
 */
public interface TemplateService
{
	/**
	 * <pre>
	 * 查询模板自定义数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:50:55, think
	 */
	public SearchResult<TemplateDataModel> findDataByCondition(QueryParam queryParam);

	/**
	 * 
	 * <pre>
	 * 新增模板
	 * </pre>
	 * @param templateModel
	 */
	public void addTemplate(TemplateModel templateModel) throws Exception;

	/**
	 * 
	 * <pre>
	 * 登录账户查找的模板
	 * </pre>
	 * @return
	 */
	public List<TemplateModel> listTemplate(PrintModleName billType);

	/**
	 * 登录用户获取单个打印模板
	 * @param id
	 * @return
	 */
	public TemplateModel getTemplate(Long id);

	/**
	 * 管理员选择模板
	 * @param billType
	 * @return
	 */
	public List<TemplateModel> listTemplateByAdmin(PrintModleName billType);

	/**
	 * 管理员获取单个模板
	 * @param id
	 * @return
	 */
	public TemplateModel getTemplateByAdmin(Long id);

	/**
	 * 条件查询模板列表
	 * @param query
	 * @return
	 */
	public SearchResult<TemplateModel> queryTemplate(QueryParam query);

	/**
	 * 系统管理员添加模板
	 * @param templateModel
	 */
	public void addTemplateByAdmin(TemplateModel templateModel) throws Exception;

	/**
	 * 删除模板
	 * @param id
	 */
	public void delTemplate(Long id);

	/**
	 * 
	 * <pre>
	 * 编辑模板
	 * </pre>
	 * @param templateModel
	 */
	public void editTemplateByAdmin(TemplateModel templateModel);
}
