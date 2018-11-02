/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年02月22日 下午17:53:23
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;

/**
 * <pre>
 * 销售管理 - 销售订单：销售部件表
 * </pre>
 * @author think
 * @version 1.0, 2017年9月18日
 */
@Entity
@Table(name = "sale_order_part")
public class SaleOrderPart extends BaseBillDetailTableEntity
{

	private static final long serialVersionUID = 1L;

	// 拼版数 P数 单面P数 上机规格 印刷方式 印刷普色 印刷专色 印张正数 印版张数 放损% 放损数 总印张 备注

	/**
	 * 部件名称
	 */
	@Column(length = 50)
	private String partName;

	/**
	 * 部件数量
	 */
	private Integer qty;

	/**
	 * 倍数
	 */
	private Integer multiple;

	/**
	 * 拼版数
	 */
	private Integer pieceNum;

	/**
	 * P数
	 */
	private Integer pageNum;

	/**
	 * 上机规格
	 */
	private String style;

	/**
	 * 印刷方式
	 */
//	private PrintType printType;

	/**
	 * 印刷普色
	 */

	private String generalColor;

	/**
	 * 印刷专色
	 */

	private String spotColor;

	/**
	 * 印张正数
	 */
	private Integer impressionNum;

	/**
	 * 贴数
	 */
	private Integer stickersNum;

	/**
	 * 每贴正数
	 */
	private Integer stickersPostedNum;

	/**
	 * 印版付数
	 */
	private Integer plateSuitNum;

	/**
	 * 印版张数
	 */
	private Integer plateSheetNum;

	/**
	 * （包装类型：放损%）/ （书刊类型：单贴放损%）
	 */
	private BigDecimal lossRate;

	/**
	 * （书刊类型：单贴放损）
	 */
	private Integer stickerlossQty;

	/**
	 * 放损数
	 */
	private Integer lossQty;

	/**
	 * 总印张(impressionNum + lossNum)
	 */
	private Integer totalImpressionNum;

	/**
	 * 机台名称
	 */
	@Column(length = 50)
	private String machineName;

	/**
	 * 销售明细对象
	 */
	@Transient
	private SaleOrderDetail master;

	/**
	 * 部件材料列表
	 */
	@Transient
	List<SaleOrderMaterial> materialList = new ArrayList<SaleOrderMaterial>();

	/**
	 * 部件工序列表
	 */
	@Transient
	List<SaleOrderProcedure> procedureList = new ArrayList<SaleOrderProcedure>();

	/**
	 * 部件产品信息列表
	 */
	@Transient
	List<SaleOrderPart2Product> productList = new ArrayList<SaleOrderPart2Product>();

	public String getPartName()
	{
		return partName;
	}

	public void setPartName(String partName)
	{
		this.partName = partName;
	}

	public Integer getQty()
	{
		return qty;
	}

	public void setQty(Integer qty)
	{
		this.qty = qty;
	}

	public Integer getMultiple()
	{
		return multiple;
	}

	public void setMultiple(Integer multiple)
	{
		this.multiple = multiple;
	}

	public Integer getPieceNum()
	{
		return pieceNum;
	}

	public void setPieceNum(Integer pieceNum)
	{
		this.pieceNum = pieceNum;
	}

	public Integer getPageNum()
	{
		return pageNum;
	}

	public void setPageNum(Integer pageNum)
	{
		this.pageNum = pageNum;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

//	public PrintType getPrintType()
//	{
//		return printType;
//	}
//
//	public String getPrintTypeText()
//	{
//		return printType.getText();
//	}
//
//	public void setPrintType(PrintType printType)
//	{
//		this.printType = printType;
//	}

	public SaleOrderDetail getMaster()
	{
		return master;
	}

	public void setMaster(SaleOrderDetail master)
	{
		this.master = master;
	}

	public List<SaleOrderPart2Product> getProductList()
	{
		return productList;
	}

	public void setProductList(List<SaleOrderPart2Product> productList)
	{
		this.productList = productList;
	}

	public String getGeneralColor()
	{
		return generalColor;
	}

	public void setGeneralColor(String generalColor)
	{
		this.generalColor = generalColor;
	}

	public String getSpotColor()
	{
		return spotColor;
	}

	public void setSpotColor(String spotColor)
	{
		this.spotColor = spotColor;
	}

	public Integer getImpressionNum()
	{
		return impressionNum;
	}

	public void setImpressionNum(Integer impressionNum)
	{
		this.impressionNum = impressionNum;
	}

	public Integer getStickersNum()
	{
		return stickersNum;
	}

	public void setStickersNum(Integer stickersNum)
	{
		this.stickersNum = stickersNum;
	}

	public Integer getStickersPostedNum()
	{
		return stickersPostedNum;
	}

	public void setStickersPostedNum(Integer stickersPostedNum)
	{
		this.stickersPostedNum = stickersPostedNum;
	}

	public Integer getPlateSuitNum()
	{
		return plateSuitNum;
	}

	public void setPlateSuitNum(Integer plateSuitNum)
	{
		this.plateSuitNum = plateSuitNum;
	}

	public Integer getPlateSheetNum()
	{
		return plateSheetNum;
	}

	public void setPlateSheetNum(Integer plateSheetNum)
	{
		this.plateSheetNum = plateSheetNum;
	}

	public BigDecimal getLossRate()
	{
		return lossRate;
	}

	public void setLossRate(BigDecimal lossRate)
	{
		this.lossRate = lossRate;
	}

	public Integer getLossQty()
	{
		return lossQty;
	}

	public void setLossQty(Integer lossQty)
	{
		this.lossQty = lossQty;
	}

	public Integer getTotalImpressionNum()
	{
		return totalImpressionNum;
	}

	public void setTotalImpressionNum(Integer totalImpressionNum)
	{
		this.totalImpressionNum = totalImpressionNum;
	}

	public List<SaleOrderMaterial> getMaterialList()
	{
		return materialList;
	}

	public void setMaterialList(List<SaleOrderMaterial> materialList)
	{
		this.materialList = materialList;
	}

	public List<SaleOrderProcedure> getProcedureList()
	{
		return procedureList;
	}

	public void setProcedureList(List<SaleOrderProcedure> procedureList)
	{
		this.procedureList = procedureList;
	}

	public Integer getStickerlossQty()
	{
		return stickerlossQty;
	}

	public void setStickerlossQty(Integer stickerlossQty)
	{
		this.stickerlossQty = stickerlossQty;
	}

	public String getMachineName()
	{
		return machineName;
	}

	public void setMachineName(String machineName)
	{
		this.machineName = machineName;
	}
}
