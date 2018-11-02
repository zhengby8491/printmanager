/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.produce;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;

/**
 * <pre>
 * 生产管理 - 生产工单：(成品)工单装订、打包、粘合
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年8月16日, zhaojt
 * @version 	   2.0, 2018年2月23日上午10:16:28, zhengby, 代码规范
 */
@Entity
@Table(name = "produce_work_pack")
public class WorkPack extends BaseBillDetailTableEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 工单对象
	 */
	@Transient
	private Work master;
	/**
	 * 材料列表
	 */
	@Transient
	List<WorkMaterial> materialList=new ArrayList<WorkMaterial>();
	/**
	 * 成品工序列表
	 */
	@Transient
	List<WorkProcedure> procedureList=new ArrayList<WorkProcedure>();
	
	@Transient
	private String style;
	public Work getMaster()
	{
		return master;
	}
	public void setMaster(Work master)
	{
		this.master = master;
	}
	public List<WorkMaterial> getMaterialList()
	{
		return materialList;
	}
	public void setMaterialList(List<WorkMaterial> materialList)
	{
		this.materialList = materialList;
	}
	public List<WorkProcedure> getProcedureList()
	{
		return procedureList;
	}
	public void setProcedureList(List<WorkProcedure> procedureList)
	{
		this.procedureList = procedureList;
	}
	public String getStyle()
	{
		return style;
	}
	public void setStyle(String style)
	{
		this.style = style;
	}
	
	
}
