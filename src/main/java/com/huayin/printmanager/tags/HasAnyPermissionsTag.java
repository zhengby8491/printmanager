/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架 - 自定义权限标签（存在其中的一个权限即可）
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class HasAnyPermissionsTag extends BodyTagSupport
{
	private static final long serialVersionUID = -6916871149290361831L;

	private String name;

	/**
	 * 构造函数
	 */
	public HasAnyPermissionsTag()
	{
		super();
	}

	public int doEndTag() throws JspException
	{
		return super.doEndTag();
	}

	public int doStartTag() throws JspException
	{
		String[] pArray=getName().split(",");
		for(String p:pArray)
		{
			boolean show = isPermitted(p.trim());
			if (show)
			{
				return TagSupport.EVAL_BODY_INCLUDE;
			}	
		}
		return TagSupport.SKIP_BODY;
	}

	protected boolean isPermitted(String p)
	{
		return UserUtils.hasPermission(p);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
