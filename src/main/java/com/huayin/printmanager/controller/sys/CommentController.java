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

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.sys.Comment;
import com.huayin.printmanager.persist.entity.sys.Comment_Reply;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统模块 - 留言板
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/comment")
public class CommentController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 提问详情
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:13:07, think
	 */
	@RequestMapping(value = "question_detail/{id}")
	public String questionDetail(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		Comment comment = serviceFactory.getCommentService().getDetail(id);
		map.put("comment", comment);
		return "sys/service/question_detail";
	}

	/**
	 * <pre>
	 * 页面 - 回复详情
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:13:28, think
	 */
	@RequestMapping(value = "ask_detail/{id}")
	public String askDetail(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		Comment comment = serviceFactory.getCommentService().getDetail(id);
		map.put("comment", comment);
		return "sys/comment/ask_detail";
	}

	/**
	 * <pre>
	 * 页面 - 留言板列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:14:02, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:comment:list")
	public String list()
	{
		return "sys/comment/list";
	}

	/**
	 * <pre>
	 * Ajax列表 - 留言板列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:14:21, think
	 */
	@RequestMapping(value = "listDetail")
	@ResponseBody
	public SearchResult<Comment> listDetail(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getCommentService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 功能 - 提问新增
	 * </pre>
	 * @param comment
	 * @param map
	 * @param request
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:14:41, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public AjaxResponseBody save(@RequestBody Comment comment, ModelMap map, HttpServletRequest request)
	{
		comment.setIp(request.getRemoteAddr());
		serviceFactory.getCommentService().save(comment);
		return returnSuccessBody(comment);
	}

	/**
	 * <pre>
	 * 功能 - 留言回复
	 * </pre>
	 * @param map
	 * @param commentReply
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:15:06, think
	 */
	@RequestMapping(value = "reply")
	@ResponseBody
	public AjaxResponseBody reply(ModelMap map, @RequestBody Comment_Reply commentReply)
	{
		Comment comment = serviceFactory.getCommentService().reply(commentReply.getId(), commentReply.getReply(), commentReply.getIsManagerReplay());
		return returnSuccessBody(comment);
	}

	/**
	 * <pre>
	 * Ajax列表 - 我的提问 数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:12:18, think
	 */
	@RequestMapping(value = "myListDetail")
	@ResponseBody
	public SearchResult<Comment> myListDetail(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getCommentService().findByCompany(queryParam);
	}

}
