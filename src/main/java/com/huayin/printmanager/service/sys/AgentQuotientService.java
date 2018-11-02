/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.AgentQuotient;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统模块 - 代理商管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface AgentQuotientService
{
	/**
	 * 根据id获取代理商
	 * @param id
	 * @return
	 */
	public AgentQuotient get(Long id);

	/**
	 * <pre>
	 * 多条件查询代理商
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:34:31, think
	 */
	public SearchResult<AgentQuotient> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 获取代理商列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:35:57, think
	 */
	public List<AgentQuotient> findAgentQuotient();

	/**
	 * <pre>
	 * 新增代理商
	 * </pre>
	 * @param advertisement
	 * @param upfile
	 * @since 1.0, 2017年10月25日 下午4:34:39, think
	 */
	public void save(AgentQuotient advertisement, MultipartFile upfile);

	/**
	 * <pre>
	 * 更新代理商
	 * </pre>
	 * @param advertisement
	 * @param upfile
	 * @since 1.0, 2017年10月25日 下午4:35:51, think
	 */
	public void update(AgentQuotient advertisement, MultipartFile upfile);

	/**
	 * <pre>
	 * 删除代理商
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月25日 下午4:34:45, think
	 */
	public void delete(Long id);

}
