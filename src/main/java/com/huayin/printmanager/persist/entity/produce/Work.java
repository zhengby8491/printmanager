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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ProductType;

/**
 * <pre>
 * 生产管理 - 生产工单 ：施工单表
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年5月24日 下午4:26:39
 * @version 	   2.0, 2018年2月23日上午10:14:39, zhengby, 代码规范
 */
@Entity
@Table(name = "produce_work")
public class Work extends BaseBillMasterTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 工单产品类型
	 */
	@Enumerated(EnumType.STRING)
	private ProductType productType;

	/**
	 * 是否整单发外
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isOutSource;

	/**
	 * 是否急单
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isEmergency;

	/**
	 * 补单次数
	 */
	private Integer supplementCount;

	/**
	 * 翻单次数
	 */
	private Integer turningCount;

	/**
	 * （补单或翻单）的引用工单ID
	 */
	private Long sourceWorkId;

	/**
	 * 产品列表
	 */
	@Transient
	private List<WorkProduct> productList = new ArrayList<WorkProduct>();

	/**
	 * 部件列表
	 */
	@Transient
	private List<WorkPart> partList = new ArrayList<WorkPart>();

	/**
	 * 装订打包
	 */
	@Transient
	private WorkPack pack;

	public BoolValue getIsOutSource()
	{
		return isOutSource;
	}

	public void setIsOutSource(BoolValue isOutSource)
	{
		this.isOutSource = isOutSource;
	}

	public BoolValue getIsEmergency()
	{
		return isEmergency;
	}

	public void setIsEmergency(BoolValue isEmergency)
	{
		this.isEmergency = isEmergency;
	}

	public List<WorkProduct> getProductList()
	{
		return productList;
	}

	public void setProductList(List<WorkProduct> productList)
	{
		this.productList = productList;
	}

	public List<WorkPart> getPartList()
	{
		return partList;
	}

	public void setPartList(List<WorkPart> partList)
	{
		this.partList = partList;
	}

	public WorkPack getPack()
	{
		return pack;
	}

	public void setPack(WorkPack pack)
	{
		this.pack = pack;
	}

	public Integer getSupplementCount()
	{
		return supplementCount;
	}

	public void setSupplementCount(Integer supplementCount)
	{
		this.supplementCount = supplementCount;
	}

	public Integer getTurningCount()
	{
		return turningCount;
	}

	public void setTurningCount(Integer turningCount)
	{
		this.turningCount = turningCount;
	}

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
	}

	public Long getSourceWorkId()
	{
		return sourceWorkId;
	}

	public void setSourceWorkId(Long sourceWorkId)
	{
		this.sourceWorkId = sourceWorkId;
	}

	public String getProductTypeText()
	{
		if (productType != null)
		{

			return productType.getText();
		}
		return "-";
	}

}
