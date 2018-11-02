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

import com.huayin.printmanager.persist.entity.basic.Material;

/**
 * <pre>
 * 注册-初始化材料信息VO
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年9月4日, zhaojt
 * @version 	   2.0, 2018年2月26日上午9:43:30, zhengby, 代码规范
 */
public class RegisterMaterialVo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long materialClassId;

	private List<Material> materialList;

	public Long getMaterialClassId()
	{
		return materialClassId;
	}

	public void setMaterialClassId(Long materialClassId)
	{
		this.materialClassId = materialClassId;
	}

	public List<Material> getMaterialList()
	{
		return materialList;
	}

	public void setMaterialList(List<Material> materialList)
	{
		this.materialList = materialList;
	}

}
