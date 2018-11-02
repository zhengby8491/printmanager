/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.vo;

import java.io.Serializable;

/**
 * <pre>
 * 删除vo
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年11月20日, zhaojt
 * @version 	   2.0, 2018年2月26日上午11:00:26, zhengby, 代码规范
 */
public class DeleteVo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5151626188855771091L;
	private Object id;
	public DeleteVo()
	{
	}
	public Object getId()
	{
		return id;
	}
	public void setId(Object id)
	{
		this.id = id;
	}

}
