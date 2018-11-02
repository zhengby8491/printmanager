/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys.vo;

import java.io.Serializable;
import java.util.List;

import com.huayin.printmanager.persist.entity.basic.Procedure;

/**
 * <pre>
 * 注册-初始化工序信息VO
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年9月4日, zhaojt
 * @version 	   2.0, 2018年2月26日上午9:44:39, zhengby, 代码规范
 */
public class RegisterProcedureVo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long procedureClassId;

	private List<Procedure> procedureList;

	public Long getProcedureClassId()
	{
		return procedureClassId;
	}

	public void setProcedureClassId(Long procedureClassId)
	{
		this.procedureClassId = procedureClassId;
	}

	public List<Procedure> getProcedureList()
	{
		return procedureList;
	}

	public void setProcedureList(List<Procedure> procedureList)
	{
		this.procedureList = procedureList;
	}

}
