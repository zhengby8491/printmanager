/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.Comment;
import com.huayin.printmanager.persist.entity.sys.Comment_Reply;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CommentType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.CommentService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 留言板
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class CommentServiceImpl extends BaseServiceImpl implements CommentService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CommentService#getDetail(java.lang.Long)
	 */
	@Override
	public Comment getDetail(Long id)
	{
		DynamicQuery queryd = new DynamicQuery(Comment.class);
		queryd.eq("id", id);
		Comment comment = daoFactory.getCommonDao().getByDynamicQuery(queryd, Comment.class);
		DynamicQuery query = new DynamicQuery(Comment_Reply.class);
		query.eq("masterId", id);
		query.asc("updateTime");
		List<Comment_Reply> commentReplyList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Comment_Reply.class);
		comment.setDetailList(commentReplyList);
		return comment;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.CommentService#findByCondition(com.huayin.printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<Comment> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(Comment.class);

		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (queryParam.getUdateMin() != null)
		{
			query.ge("updateTime", queryParam.getUdateMin());
		}
		if (queryParam.getUdateMax() != null)
		{
			query.le("updateTime", queryParam.getUdateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getTitle()))
		{
			query.like("title", "%" + queryParam.getTitle() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getCompanyLinkName()))
		{
			query.like("userName", "%" + queryParam.getCompanyLinkName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getCompanyTel()))
		{
			query.like("contact", "%" + queryParam.getCompanyTel() + "%");
		}
		if (queryParam.getType() != null)
		{
			query.eq("type", queryParam.getType());
		}

		if (queryParam.getAuditFlag() != null)
		{
			query.eq("replyState", queryParam.getAuditFlag());
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("createTime");
		query.setIsSearchTotalCount(true);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Comment.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.CommentService#findByCompany(com.huayin.printmanager.service.vo.QueryParam)
	 */
	public SearchResult<Comment> findByCompany(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(Comment.class);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("createTime");
		query.eq("companyId", UserUtils.getCompanyId());
		query.setIsSearchTotalCount(true);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Comment.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CommentService#save(com.huayin.printmanager.persist.entity.sys.Comment)
	 */
	@Override
	@Transactional
	public Comment save(Comment comment)
	{
		comment.setType(CommentType.OTHER);
		comment.setCompanyId(UserUtils.getCompanyId());
		comment.setUserName(UserUtils.getUserName());
		comment.setCreateTime(new Date());
		comment.setContact(UserUtils.getUser().getMobile());
		daoFactory.getCommonDao().saveEntity(comment);
		return comment;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CommentService#reply(java.lang.Long, java.lang.String,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public Comment reply(Long id, String reply, BoolValue isManagerReplay)
	{
		DynamicQuery queryd = new DynamicQuery(Comment.class);
		queryd.eq("id", id);
		Comment comment_ = daoFactory.getCommonDao().getByDynamicQuery(queryd, Comment.class);
		comment_.setReplyState(isManagerReplay);
		comment_.setUpdateTime(new Date());
		// 回复子表对象
		Comment_Reply commentReply = new Comment_Reply();
		commentReply.setMasterId(id);
		commentReply.setReply(reply);
		commentReply.setReplyUserName(UserUtils.getUserName());
		commentReply.setUpdateTime(new Date());
		commentReply.setCompanyId(UserUtils.getCompanyId());
		if (isManagerReplay != null)
		{
			commentReply.setIsManagerReplay(isManagerReplay);
		}
		daoFactory.getCommonDao().updateEntity(comment_);
		daoFactory.getCommonDao().saveEntity(commentReply);
		return comment_;
	}
}
