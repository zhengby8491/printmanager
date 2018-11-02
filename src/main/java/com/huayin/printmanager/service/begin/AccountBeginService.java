/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月5日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.begin;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.begin.AccountBegin;
import com.huayin.printmanager.persist.entity.begin.AccountBeginDetail;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 账户期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月5日
 * @since        2.0, 2018年1月5日 下午17:07:00, think, 规范和国际化
 */
public interface AccountBeginService
{
	/**
	 * <pre>
	 * 根据id获取账户期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:20:43, think
	 */
	public AccountBegin get(Long id);

	/**
	 * <pre>
	 * 根据id获取期货期初明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:20:57, think
	 */
	public List<AccountBeginDetail> getDetail(Long id);

	/**
	 * <pre>
	 * 添加账户期初信息
	 * </pre>
	 * @param accountBegin
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:21:13, think
	 */
	public Long save(AccountBegin accountBegin);

	/**
	 * <pre>
	 * 修改账户期初信息
	 * </pre>
	 * @param accountBegin
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:21:35, think
	 */
	public Long update(AccountBegin accountBegin);

	/**
	 * <pre>
	 * 删除账户期初信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:21:48, think
	 */
	public Boolean delete(Long id);

	/**
	 * <pre>
	 * 审核账户期初信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:22:06, think
	 */
	public Boolean audit(Long id);

	/**
	 * <pre>
	 * 反审核账户期初信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 上午11:32:52, zhengby
	 */
	public Boolean auditCancel(Long id);
	
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:20:19, think
	 */
	public SearchResult<AccountBegin> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查所有记录中账户ID的集合
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:22:23, think
	 */
	public List<Long> getAccountIdList();

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:22:29, think
	 */
	public AccountBegin lockHasChildren(Long id);

}
