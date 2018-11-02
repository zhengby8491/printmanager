/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
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

/**
 * <pre>
 * 注册-初始化2VO
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年9月4日, zhaojt
 * @version 	   2.0, 2018年2月26日上午9:42:12, zhengby, 代码规范
 */
public class RegisterInitVo implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * 材料信息VO集合
	 */
	private List<RegisterMaterialVo> materialVoList;

	/**
	 * 产品分类ID集合
	 */
	private List<Long> productClassIdList;

	/**
	 * 工序信息VO集合
	 */
	private List<RegisterProcedureVo> procedureVoList;

	/**
	 * 选择注册版本
	 */
	private Integer version = null;

	public List<RegisterProcedureVo> getProcedureVoList()
	{
		return procedureVoList;
	}

	public void setProcedureVoList(List<RegisterProcedureVo> procedureVoList)
	{
		this.procedureVoList = procedureVoList;
	}

	public List<Long> getProductClassIdList()
	{
		return productClassIdList;
	}

	public void setProductClassIdList(List<Long> productClassIdList)
	{
		this.productClassIdList = productClassIdList;
	}

	public List<RegisterMaterialVo> getMaterialVoList()
	{
		return materialVoList;
	}

	public void setMaterialVoList(List<RegisterMaterialVo> materialVoList)
	{
		this.materialVoList = materialVoList;
	}

	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}
}
