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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.Comment;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统模块 - 留言板
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface CommentService
{
	/**
	 * <pre>
	 * 根据id查提问详情
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:55:44, think
	 */
	public Comment getDetail(Long id);

	/**
	 * <pre>
	 * 管理员多条件查询所有留言
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:55:52, think
	 */
	public SearchResult<Comment> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据公司ID查询留言（我的提问）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:56:00, think
	 */
	public SearchResult<Comment> findByCompany(QueryParam queryParam);

	/**
	 * 
	 * <pre>
	 * 新增留言板
	 * </pre>
	 * @param comment
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:55:25, think
	 */
	public Comment save(Comment comment);

	/**
	 * <pre>
	 * 留言回复
	 * </pre>
	 * @param id
	 * @param reply
	 * @param isManagerReplay
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:56:08, think
	 */
	public Comment reply(Long id, String reply, BoolValue isManagerReplay);
}
