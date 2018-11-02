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

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.WorkMaterialType;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 生产管理 - 生产工单 ：工单材料表(对应部件材料和成品材料)
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年5月24日 下午4:26:39
 * @version 	   2.0, 2018年2月23日上午10:13:05, zhengby，代码规范
 */
@Entity
@Table(name = "produce_work_material")
public class WorkMaterial extends BaseBillTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 工单ID
	 */
	private Long workId;

	/**
	 * 工单编号
	 */
	@Column(length = 50)
	private String workBillNo;

	/**
	 * 工单材料类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private WorkMaterialType workMaterialType;

	/**
	 * 父ID（部件ID或者成品ID)
	 */
	private Long parentId;

	/**
	 * 材料ID
	 */
	private Long materialId;

	/**
	 * 材料编号
	 */
	private String materialCode;

	/**
	 * 材料名称
	 */
	private String materialName;

	/**
	 * 材料规格
	 */
	private String style;

	/**
	 * 克 重
	 */
	private Integer weight;

	/**
	 * 库存单位
	 */
	@Column(length = 20)
	private Long stockUnitId;

	/**
	 * 计价单位
	 */
	@Column(length = 20)
	private Long valuationUnitId;

	/**
	 *材料开数
	 */
	private Integer splitQty;

	/**
	 *材料用量
	 */
	private BigDecimal qty;

	/**
	 * 已采购数量
	 */
	private BigDecimal purchQty;

	/**
	 * 已领料数量
	 */
	private BigDecimal takeQty;

	/**
	 * 是否无需领料
	 */
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private BoolValue isNotTake;

	/**
	 * 是否无需采购
	 */
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private BoolValue isNotPurch;

	/**
	 * 是否客户来纸
	 */
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private BoolValue isCustPaper;

	/**
	 * （打印显示中文用）是否客来纸
	 */
	@Transient
	private String isCustPaperText;

	/**
	 * 库存数量
	 */
	@Transient
	private BigDecimal stockQty;

	/**
	 * 工单产品对象
	 */
	@Transient
	private WorkPack workPack;

	/**
	 * 工单部件对象
	 */
	@Transient
	private WorkPart workPart;

	/**
	 * 工单对象
	 */
	@Transient
	private Work work;

	/**
	 * 产品名称列表
	 */
	@Transient
	private List<WorkProduct> productList = Lists.newArrayList();

	public Long getWorkId()
	{
		return workId;
	}

	public void setWorkId(Long workId)
	{
		this.workId = workId;
	}

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
	}

	public WorkMaterialType getWorkMaterialType()
	{
		return workMaterialType;
	}

	public void setWorkMaterialType(WorkMaterialType workMaterialType)
	{
		this.workMaterialType = workMaterialType;
	}

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}

	public WorkPack getWorkPack()
	{
		return workPack;
	}

	public void setWorkPack(WorkPack workPack)
	{
		this.workPack = workPack;
	}

	public WorkPart getWorkPart()
	{
		return workPart;
	}

	public void setWorkPart(WorkPart workPart)
	{
		this.workPart = workPart;
	}

	public Work getWork()
	{
		return work;
	}

	public void setWork(Work work)
	{
		this.work = work;
	}

	public String getMaterialName()
	{
		return materialName;
	}

	public void setMaterialName(String materialName)
	{
		this.materialName = materialName;
	}

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public Integer getSplitQty()
	{
		return splitQty;
	}

	public void setSplitQty(Integer splitQty)
	{
		this.splitQty = splitQty;
	}

	public Long getStockUnitId()
	{
		return stockUnitId;
	}

	public void setStockUnitId(Long stockUnitId)
	{
		this.stockUnitId = stockUnitId;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public BigDecimal getPurchQty()
	{
		return purchQty;
	}

	public void setPurchQty(BigDecimal purchQty)
	{
		this.purchQty = purchQty;
	}

	public Long getMaterialId()
	{
		return materialId;
	}

	public void setMaterialId(Long materialId)
	{
		this.materialId = materialId;
	}

	public BigDecimal getTakeQty()
	{
		return takeQty;
	}

	public void setTakeQty(BigDecimal takeQty)
	{
		this.takeQty = takeQty;
	}

	public BigDecimal getStockQty()
	{
		return stockQty;
	}

	public void setStockQty(BigDecimal stockQty)
	{
		this.stockQty = stockQty;
	}

	public String getMaterialCode()
	{
		return materialCode;
	}

	public void setMaterialCode(String materialCode)
	{
		this.materialCode = materialCode;
	}

	public BoolValue getIsNotTake()
	{
		return isNotTake;
	}

	public void setIsNotTake(BoolValue isNotTake)
	{
		this.isNotTake = isNotTake;
	}

	public BoolValue getIsNotPurch()
	{
		return isNotPurch;
	}

	public void setIsNotPurch(BoolValue isNotPurch)
	{
		this.isNotPurch = isNotPurch;
	}

	public Long getValuationUnitId()
	{
		return valuationUnitId;
	}

	public void setValuationUnitId(Long valuationUnitId)
	{
		this.valuationUnitId = valuationUnitId;
	}

	public BoolValue getIsCustPaper()
	{
		return isCustPaper;
	}

	public void setIsCustPaper(BoolValue isCustPaper)
	{
		this.isCustPaper = isCustPaper;
	}

	public String getIsCustPaperText()
	{
		return isCustPaperText;
	}

	public void setIsCustPaperText(String isCustPaperText)
	{
		this.isCustPaperText = isCustPaperText;
	}

	// -------------------------------------------------------------------
	public String getStockUnitName()
	{

		if (stockUnitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), stockUnitId, "name");
		}
		return "-";
	}

	public String getIsNotTakeText()
	{
		if (isNotTake != null)
		{

			return isNotTake.getText();
		}
		return "-";
	}

	public String getIsNotPurchText()
	{
		if (isNotPurch != null)
		{

			return isNotPurch.getText();
		}
		return "-";
	}

	public List<WorkProduct> getProductList()
	{
		return productList;
	}

	public void setProductList(List<WorkProduct> productList)
	{
		this.productList = productList;
	}

	public String getProductNames()
	{
		StringBuilder sb = new StringBuilder();
		if (this.productList != null && this.productList.size() > 0)
		{
			for (WorkProduct p : this.productList)
			{
				sb.append(p.getProductName()).append(",");
			}
		}
		return StringUtils.removeEnd(sb.toString(), ",");
	}
	
	public String getProductIds()
	{
		StringBuilder sb = new StringBuilder();
		if (this.productList != null && this.productList.size() > 0)
		{
			for (WorkProduct p : this.productList)
			{
				sb.append(p.getProductId()).append(",");
			}
		}
		return StringUtils.removeEnd(sb.toString(), ",");
	}
}
