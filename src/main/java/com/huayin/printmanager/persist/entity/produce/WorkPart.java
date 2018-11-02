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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.PrintType;

/**
 * <pre>
 *  生产管理 - 生产工单：工单部件表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年8月16日, zhaojt
 * @version 	   2.0, 2018年2月23日上午10:18:26, zhengby, 代码规范
 */
@Entity
@Table(name = "produce_work_part")
public class WorkPart extends BaseBillDetailTableEntity
{

	private static final long serialVersionUID = 1L;
	
	//拼版数	P数	单面P数	上机规格	印刷方式	印刷普色	印刷专色	印张正数	印版张数	放损%	放损数	总印张	备注
	

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
	private PrintType printType;
	
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
	 *  （书刊类型：单贴放损）
	 */
	private Integer stickerlossQty;
	/**
	 * 放损数	
	 */
	@Column(columnDefinition = "decimal(18,2)")
	private BigDecimal lossQty;
	
	/**
	 * 总印张(impressionNum + lossNum)
	 */
	private Integer totalImpressionNum;
	
	/**
	 * 齿轮数
	 */
	private Integer gear;
	
	/**
	 * 齿间距
	 */
	@Column(columnDefinition = "decimal(18,3)")
	private BigDecimal distance;
	
	/**
	 * 走距
	 */
	@Column(columnDefinition = "decimal(18,2)")
	private BigDecimal walkDistance;
	
	/**
	 * 标准用料(轮转工单)
	 */
	@Column(columnDefinition = "decimal(18,2)")
	private BigDecimal materialNum;
	
	/**
	 * 总用料
	 */
	@Column(columnDefinition = "decimal(18,2)")
	private BigDecimal totalMaterialNum;
	
	/**
	 * 机台名称
	 */
	@Column(length = 50)
	private String machineName;
	
	/**
	 * 机台Id
	 */
	private Long machineId;
	
	/**
	 * 工单对象
	 */
	@Transient
	private Work master;
	/**
	 * 部件材料列表
	 */
	@Transient
	List<WorkMaterial> materialList=new ArrayList<WorkMaterial>();
	/**
	 * 部件工序列表
	 */
	@Transient
	List<WorkProcedure> procedureList=new ArrayList<WorkProcedure>();
	/**
	 * 部件产品信息列表
	 */
	@Transient
	List<WorkPart2Product> productList=new ArrayList<WorkPart2Product>();
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

	public PrintType getPrintType()
	{
		return printType;
	}
	public String getPrintTypeText()
	{
		return printType.getText();
	}

	public void setPrintType(PrintType printType)
	{
		this.printType = printType;
	}

	public Work getMaster()
	{
		return master;
	}

	public void setMaster(Work master)
	{
		this.master = master;
	}

	public List<WorkPart2Product> getProductList()
	{
		return productList;
	}

	public void setProductList(List<WorkPart2Product> productList)
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

	public BigDecimal getLossQty()
	{
		return lossQty;
	}

	public void setLossQty(BigDecimal lossQty)
	{
		this.lossQty = lossQty;
	}

	public Integer getGear()
	{
		return gear;
	}

	public void setGear(Integer gear)
	{
		this.gear = gear;
	}

	public BigDecimal getDistance()
	{
		return distance;
	}

	public void setDistance(BigDecimal distance)
	{
		this.distance = distance;
	}

	public BigDecimal getWalkDistance()
	{
		return walkDistance;
	}

	public void setWalkDistance(BigDecimal walkDistance)
	{
		this.walkDistance = walkDistance;
	}

	public BigDecimal getMaterialNum()
	{
		return materialNum;
	}

	public void setMaterialNum(BigDecimal materialNum)
	{
		this.materialNum = materialNum;
	}

	public BigDecimal getTotalMaterialNum()
	{
		return totalMaterialNum;
	}

	public void setTotalMaterialNum(BigDecimal totalMaterialNum)
	{
		this.totalMaterialNum = totalMaterialNum;
	}

	public Integer getTotalImpressionNum()
	{
		return totalImpressionNum;
	}

	public void setTotalImpressionNum(Integer totalImpressionNum)
	{
		this.totalImpressionNum = totalImpressionNum;
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

	public Integer getStickerlossQty()
	{
		return stickerlossQty;
	}

	public void setStickerlossQty(Integer stickerlossQty)
	{
		this.stickerlossQty = stickerlossQty;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public Long getMachineId()
	{
		return machineId;
	}

	public void setMachineId(Long machineId)
	{
		this.machineId = machineId;
	}
	
}
