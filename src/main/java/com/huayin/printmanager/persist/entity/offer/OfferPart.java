/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月8日 下午3:07:09
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OfferPrintColorType;
import com.huayin.printmanager.persist.enumerate.OfferPrintStyleType;
import com.huayin.printmanager.persist.enumerate.OfferSpotColorType;
import com.huayin.printmanager.persist.enumerate.PaperType;

/**
 * <pre>
 * 自动报价 - 保存部件
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月8日 下午3:07:09
 */
@Entity
@Table(name = "offer_order_part")
public class OfferPart extends BaseTableIdEntity implements Cloneable
{
	private static final long serialVersionUID = -3326766421125080102L;

	/**
	 * 主表id
	 */
	private Long masterId;

	/**
	 * 部件名称
	 */
	private String partName;

	/**
	 * 印刷类型
	 */
	private OfferPrintStyleType offerPrintStyleType;

	/**
	 * 普色类型
	 */
	private OfferPrintColorType offerPrintColorType;

	/**
	 * 专色类型
	 */
	private OfferSpotColorType offerSpotColorType;

	/**
	 * 普色类型（双面印刷才会有）
	 */
	private OfferPrintColorType offerPrintColorType2;

	/**
	 * 专色类型（双面印刷才会有）
	 */
	private OfferSpotColorType offerSpotColorType2;

	/**
	 * 纸张名称
	 */
	private String paperName;

	/**
	 * 克重
	 */
	private Integer paperWeight;

	/**
	 * 吨价(材料单价)
	 */
	private BigDecimal paperTonPrice;

	/**
	 * 是否客来纸
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue customPaper;

	/**
	 * 加坑纸
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue containBflute;

	/**
	 * 坑纸 - 坑形
	 */
	@Column(length = 50)
	private String bflutePit;

	/**
	 * 坑纸 - 纸质
	 */
	@Column(length = 50)
	private String bflutePaperQuality;

	/**
	 * 坑纸 - 单价
	 */
	private BigDecimal bflutePrice;

	/**
	 * 后道工序
	 */
	@Transient
	private List<OfferPartProcedure> offerPartProcedureList;

	/**
	 * 后道工序字符串（导出EXCEL使用）
	 */
	@Column(length = 3000)
	private String partProcedureStr;

	/**
	 * 印版张数
	 */
	private Integer sheetZQ;

	/**
	 * 总印张
	 */
	private Integer paperTotal;

	// ==================== 自动报价 -书刊类专用字段 ======================
	/**
	 * 页数（书刊类专用）
	 */
	private Integer pages = 0;

	/**
	 * 贴数
	 */
	private Integer thread = 0;

	/**
	 * 总贴数
	 */
	private Integer threads = 0;

	// ==================== 自动报价 - 对外报价单列表 ======================

	/**
	 * 对外报价单列表 - 印刷纸张
	 */
	@Transient
	private String printName;

	/**
	 * 对外报价单列表 - 颜色
	 */
	@Transient
	private String printColor;

	/**
	 * 对外报价单列表 - 加工工序
	 */
	@Transient
	private String printProcedure;

	/**
	 * 对外报价单列表 - 数量
	 */
	@Transient
	private Integer amount;

	/**
	 * 对外报价单列表 - 单价
	 */
	@Transient
	private double price;

	/**
	 * 对外报价单列表 - 金额
	 */
	@Transient
	private double fee;

	/**
	 * 对外报价单列表 - 含税单价
	 */
	@Transient
	private double taxPrice;

	/**
	 * 对外报价单列表 - 含税金额
	 */
	@Transient
	private double taxFee;

	/**
	 * 计价数量
	 */
	@Column(columnDefinition = "decimal(18,6) default '0.00'")
	private BigDecimal calNum;

	// ==================== 自动报价 - 内部核价列表(最优印刷机数据) ======================

	/**
	 * 印刷机名
	 */
	private String machineName;

	/**
	 * 上机规格
	 */
	private String machineSpec;

	/**
	 * 拼版数
	 */
	private Integer sheetNum;

	/**
	 * 印张正数
	 */
	private Integer impositionNum;

	/**
	 * 损耗数
	 */
	private Integer waste;

	/**
	 * 大纸尺寸
	 */
	@Enumerated(EnumType.STRING)
	private PaperType paperType;

	/**
	 * 纸开度
	 */
	private Integer materialOpening;

	/**
	 * 大纸张数
	 */
	private Integer bigPaperNum;

	/**
	 * 坑纸数
	 */
	private Integer bfluteNum;
	
	/**
	 * 坑纸计价数量
	 */
	@Column(columnDefinition = "decimal(18,6) default '0.00'")
	private BigDecimal bfluteCalNum;

	/**
	 * 机台最低价格
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private double lowerPrice;

	/**
	 * 正反专色
	 */
	private String prosConsSpot;

	/**
	 * 正反普色
	 */
	private String prosConsColor;

	// ==================== 自动报价 - 报价单列表部件费用 ======================

	/**
	 * 上机长
	 */
	private Integer machineLength;

	/**
	 * 上机宽
	 */
	private Integer machineWidth;

	/**
	 * 最大上机尺寸
	 */
	private String maxStyle;

	/**
	 * 最大上机尺寸长
	 */
	private String maxLength;

	/**
	 * 最大上机尺寸宽
	 */
	private String maxWidth;

	/**
	 * 材料长
	 */
	private Integer materialLenth;

	/**
	 * 材料宽
	 */
	private Integer materialWidth;

	/**
	 * 材料数量
	 */
	private Integer materialAmount;

	/**
	 * 材料费用
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private double partPaperFee;

	/**
	 * 印刷费用
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private double partPrintFee;

	/**
	 * 印后费用
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private double partProcedureFee;

	public Long getMasterId()
	{
		return masterId;
	}

	public void setMasterId(Long masterId)
	{
		this.masterId = masterId;
	}

	public String getPartName()
	{
		return partName;
	}

	public void setPartName(String partName)
	{
		this.partName = partName;
	}

	public OfferPrintStyleType getOfferPrintStyleType()
	{
		return offerPrintStyleType;
	}

	public String getOfferPrintStyleTypeText()
	{
		if (null != offerPrintStyleType)
		{
			return offerPrintStyleType.getText();
		}
		return null;
	}

	public void setOfferPrintStyleType(OfferPrintStyleType offerPrintStyleType)
	{
		this.offerPrintStyleType = offerPrintStyleType;
	}

	public OfferPrintColorType getOfferPrintColorType()
	{
		return offerPrintColorType;
	}

	public String getOfferPrintColorTypeText()
	{
		if (offerPrintColorType != null)
		{
			return offerPrintColorType.getText();
		}
		return null;
	}

	public void setOfferPrintColorType(OfferPrintColorType offerPrintColorType)
	{
		this.offerPrintColorType = offerPrintColorType;
	}

	public OfferSpotColorType getOfferSpotColorType()
	{
		return offerSpotColorType;
	}

	public void setOfferSpotColorType(OfferSpotColorType offerSpotColorType)
	{
		this.offerSpotColorType = offerSpotColorType;
	}

	public OfferPrintColorType getOfferPrintColorType2()
	{
		return offerPrintColorType2;
	}

	public void setOfferPrintColorType2(OfferPrintColorType offerPrintColorType2)
	{
		this.offerPrintColorType2 = offerPrintColorType2;
	}

	public OfferSpotColorType getOfferSpotColorType2()
	{
		return offerSpotColorType2;
	}

	public void setOfferSpotColorType2(OfferSpotColorType offerSpotColorType2)
	{
		this.offerSpotColorType2 = offerSpotColorType2;
	}

	public String getPaperName()
	{
		return paperName;
	}

	public void setPaperName(String paperName)
	{
		this.paperName = paperName;
	}

	public Integer getPaperWeight()
	{
		return paperWeight;
	}

	public void setPaperWeight(Integer paperWeight)
	{
		this.paperWeight = paperWeight;
	}

	public BigDecimal getPaperTonPrice()
	{
		return paperTonPrice;
	}

	public void setPaperTonPrice(BigDecimal paperTonPrice)
	{
		this.paperTonPrice = paperTonPrice;
	}

	public BoolValue getCustomPaper()
	{
		return customPaper;
	}

	public void setCustomPaper(BoolValue customPaper)
	{
		this.customPaper = customPaper;
	}

	public BoolValue getContainBflute()
	{
		return containBflute;
	}

	public void setContainBflute(BoolValue containBflute)
	{
		this.containBflute = containBflute;
	}

	public String getBflutePit()
	{
		return bflutePit;
	}

	public void setBflutePit(String bflutePit)
	{
		this.bflutePit = bflutePit;
	}

	public String getBflutePaperQuality()
	{
		return bflutePaperQuality;
	}

	public void setBflutePaperQuality(String bflutePaperQuality)
	{
		this.bflutePaperQuality = bflutePaperQuality;
	}

	public BigDecimal getBflutePrice()
	{
		return bflutePrice;
	}

	public void setBflutePrice(BigDecimal bflutePrice)
	{
		this.bflutePrice = bflutePrice;
	}

	public List<OfferPartProcedure> getOfferPartProcedureList()
	{
		return offerPartProcedureList;
	}

	public void setOfferPartProcedureList(List<OfferPartProcedure> offerPartProcedureList)
	{
		this.offerPartProcedureList = offerPartProcedureList;
	}

	public String getPartProcedureStr()
	{
		return partProcedureStr;
	}

	public void setPartProcedureStr(String partProcedureStr)
	{
		this.partProcedureStr = partProcedureStr;
	}

	public Integer getPaperTotal()
	{
		return paperTotal;
	}

	public void setPaperTotal(Integer paperTotal)
	{
		this.paperTotal = paperTotal;
	}

	// ==================== 自动报价 -书刊类专用字段 ======================

	public Integer getSheetZQ()
	{
		return sheetZQ;
	}

	public void setSheetZQ(Integer sheetZQ)
	{
		this.sheetZQ = sheetZQ;
	}

	public Integer getPages()
	{
		return pages;
	}

	public void setPages(Integer pages)
	{
		this.pages = pages;
	}

	public Integer getThread()
	{
		return thread;
	}

	public void setThread(Integer thread)
	{
		this.thread = thread;
	}

	public Integer getThreads()
	{
		return threads;
	}

	public void setThreads(Integer threads)
	{
		this.threads = threads;
	}

	// ==================== 自动报价 - 对外报价单列表 ======================

	public String getPrintName()
	{
		return printName;
	}

	public void setPrintName(String printName)
	{
		this.printName = printName;
	}

	public String getPrintColor()
	{
		return printColor;
	}

	public void setPrintColor(String printColor)
	{
		this.printColor = printColor;
	}

	public String getPrintProcedure()
	{
		return printProcedure;
	}

	public void setPrintProcedure(String printProcedure)
	{
		this.printProcedure = printProcedure;
	}

	public Integer getAmount()
	{
		return amount;
	}

	public void setAmount(Integer amount)
	{
		this.amount = amount;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public double getFee()
	{
		return fee;
	}

	public void setFee(double fee)
	{
		this.fee = fee;
	}

	public double getTaxPrice()
	{
		return taxPrice;
	}

	public void setTaxPrice(double taxPrice)
	{
		this.taxPrice = taxPrice;
	}

	public double getTaxFee()
	{
		return taxFee;
	}

	public void setTaxFee(double taxFee)
	{
		this.taxFee = taxFee;
	}

	public BigDecimal getCalNum()
	{
		return calNum;
	}

	public void setCalNum(BigDecimal calNum)
	{
		this.calNum = calNum;
	}
	// ==================== 自动报价 - 对外报价单列表 ======================

	public String getMachineName()
	{
		return machineName;
	}

	public void setMachineName(String machineName)
	{
		this.machineName = machineName;
	}

	public String getMachineSpec()
	{
		return machineSpec;
	}

	public void setMachineSpec(String machineSpec)
	{
		this.machineSpec = machineSpec;
	}

	public Integer getMachineLength()
	{
		return machineLength;
	}

	public void setMachineLength(Integer machineLength)
	{
		this.machineLength = machineLength;
	}

	public Integer getMachineWidth()
	{
		return machineWidth;
	}

	public void setMachineWidth(Integer machineWidth)
	{
		this.machineWidth = machineWidth;
	}

	public Integer getSheetNum()
	{
		return sheetNum;
	}

	public void setSheetNum(Integer sheetNum)
	{
		this.sheetNum = sheetNum;
	}

	public Integer getImpositionNum()
	{
		return impositionNum;
	}

	public void setImpositionNum(Integer impositionNum)
	{
		this.impositionNum = impositionNum;
	}

	public Integer getWaste()
	{
		return waste;
	}

	public void setWaste(Integer waste)
	{
		this.waste = waste;
	}

	public PaperType getPaperType()
	{
		return paperType;
	}

	public void setPaperType(PaperType paperType)
	{
		this.paperType = paperType;
	}

	public Integer getMaterialLenth()
	{
		return materialLenth;
	}

	public void setMaterialLenth(Integer materialLenth)
	{
		this.materialLenth = materialLenth;
	}

	public Integer getMaterialWidth()
	{
		return materialWidth;
	}

	public void setMaterialWidth(Integer materialWidth)
	{
		this.materialWidth = materialWidth;
	}

	public Integer getMaterialAmount()
	{
		return materialAmount;
	}

	public void setMaterialAmount(Integer materialAmount)
	{
		this.materialAmount = materialAmount;
	}

	public Integer getMaterialOpening()
	{
		return materialOpening;
	}

	public void setMaterialOpening(Integer materialOpening)
	{
		this.materialOpening = materialOpening;
	}

	public Integer getBigPaperNum()
	{
		return bigPaperNum;
	}

	public void setBigPaperNum(Integer bigPaperNum)
	{
		this.bigPaperNum = bigPaperNum;
	}

	public BigDecimal getBfluteCalNum()
	{
		return bfluteCalNum;
	}

	public void setBfluteCalNum(BigDecimal bfluteCalNum)
	{
		this.bfluteCalNum = bfluteCalNum;
	}

	public Integer getBfluteNum()
	{
		return bfluteNum;
	}

	public void setBfluteNum(Integer bfluteNum)
	{
		this.bfluteNum = bfluteNum;
	}

	public double getLowerPrice()
	{
		return lowerPrice;
	}

	public void setLowerPrice(double lowerPrice)
	{
		this.lowerPrice = lowerPrice;
	}

	public String getProsConsSpot()
	{
		return prosConsSpot;
	}

	public void setProsConsSpot(String prosConsSpot)
	{
		this.prosConsSpot = prosConsSpot;
	}

	public String getProsConsColor()
	{
		return prosConsColor;
	}

	public void setProsConsColor(String prosConsColor)
	{
		this.prosConsColor = prosConsColor;
	}

	public String getMaxStyle()
	{
		return maxStyle;
	}

	public void setMaxStyle(String maxStyle)
	{
		this.maxStyle = maxStyle;
	}

	public String getMaxLength()
	{
		return maxLength;
	}

	public void setMaxLength(String maxLength)
	{
		this.maxLength = maxLength;
	}

	public String getMaxWidth()
	{
		return maxWidth;
	}

	public void setMaxWidth(String maxWidth)
	{
		this.maxWidth = maxWidth;
	}

	public double getPartPaperFee()
	{
		return partPaperFee;
	}

	public void setPartPaperFee(double partPaperFee)
	{
		this.partPaperFee = partPaperFee;
	}

	public double getPartPrintFee()
	{
		return partPrintFee;
	}

	public void setPartPrintFee(double partPrintFee)
	{
		this.partPrintFee = partPrintFee;
	}

	public double getPartProcedureFee()
	{
		return partProcedureFee;
	}

	public void setPartProcedureFee(double partProcedureFee)
	{
		this.partProcedureFee = partProcedureFee;
	}
}
