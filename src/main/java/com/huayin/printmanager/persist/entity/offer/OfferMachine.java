/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月17日 下午1:28:48
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.MachinePrintColor;
import com.huayin.printmanager.persist.enumerate.MachinePro;
import com.huayin.printmanager.persist.enumerate.OfferMachineType;
import com.huayin.printmanager.persist.enumerate.OfferPrintStyleType;
import com.huayin.printmanager.persist.enumerate.OfferSettingType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.PaperType;

/**
 * 
 * <pre>
 * 报价模块 - 机台设置
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月17日
 */
@Entity
@Table(name = "offer_machine")
public class OfferMachine extends BaseBasicTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 机台类型
	 */
	@Enumerated(EnumType.STRING)
	private OfferType offerType;

	/**
	 * 机台公式类型
	 */
	@Enumerated(EnumType.STRING)
	private OfferMachineType offerMachineType;

	/**
	 * 机台名称
	 */
	private String name;

	/**
	 * 机台属性
	 */
	@Enumerated(EnumType.STRING)
	private MachinePro machinePro;

	/**
	 * 机台印色
	 */
	@Enumerated(EnumType.STRING)
	private MachinePrintColor machinePrintColor;

	/**
	 * 最大上机规格
	 */
	private String maxStyle;

	/**
	 * 最小上机规格
	 */
	private String minStyle;

	/**
	 * 最少印刷厚度
	 */
	private Integer minPrintPly;

	/**
	 * 最大印刷厚度
	 */
	private Integer maxPrintPly;

	/**
	 * 创建人
	 */
	@Column(length = 50)
	private String createName;

	/**
	 * 修改人
	 */
	@Column(length = 50)
	private String updateName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 开机费+印工计价
	 */
	@Transient
	private OfferStartPrint offerStartPrint;

	/**
	 * 自定义公式报价
	 */
	@Transient
	private OfferFormula offerFormula;

	/**
	 * 报价设置类型
	 */
	@Transient
	private OfferSettingType offerSettingType;

	// ==================== 内部核价 ====================

	/**
	 * 机台最低价格
	 */
	@Transient
	private double lowerPrice;

	/**
	 * 是不是最低价格
	 */
	@Transient
	private Boolean isLowerPrice;

	/**
	 * 内部核价 - 类型：取选择的名称，如果选择的是平口盒就平口盒，选择的是其他盒型就是其他盒型
	 */
	@Transient
	private String boxType;

	/**
	 * 拼版数 - 长
	 */
	@Transient
	private Integer sheetLen;

	/**
	 * 拼版数 - 宽
	 */
	@Transient
	private Integer sheetWidth;

	/**
	 * 内部核价 - 上机尺寸：根据上面最优的上机拼版方式，算出上机规格
	 */
	@Transient
	private String style;

	/**
	 * 内部核价 - 拼版数：根据上面最优的上机方式算出拼版数
	 */
	@Transient
	private Integer sheetNum;

	/**
	 * 内部核价 - 印刷方式：根据报价时选择的是单面印刷还是双面印刷
	 */
	@Transient
	private OfferPrintStyleType offerPrintStyleType;

	/**
	 * 内部核价 - 印张正数：印刷数量/拼版数
	 */
	@Transient
	private Integer impositionNum;

	/**
	 * 内部核价 - 损耗数：根据报价设置里的对应的损耗公式计算出损耗数
	 */
	@Transient
	private Integer waste;

	/**
	 * 内部核价 - 大纸尺寸：根据上面最优的拼版方式算出是大度纸（889*1194）还是正度纸（787*1092）
	 */
	@Transient
	@Enumerated(EnumType.STRING)
	private PaperType paperType;

	/**
	 * 内部核价 - 纸开度：根据上面最优的拼版方式算出材料开数
	 */
	@Transient
	private Integer materialOpening;

	/**
	 * 内部核价 - 大纸张数：（印张正数+损耗数）/纸开度
	 */
	@Transient
	private Integer bigPaperNum;

	/**
	 * 内部核价 - 坑纸数：印张正数+损耗数
	 */
	@Transient
	private Integer bfluteNum;

	/**
	 * 内部核价 - 手/贴数：P数/单面拼数，进位取整，不足1的按一贴算（也等于每个部件的贴数，只是方便页面显示）
	 */
	@Transient
	private Integer thread = 0;

	/**
	 * 总帖数
	 */
	@Transient
	private Integer threads = 0;

	/**
	 * 总P数
	 */
	@Transient
	private Integer tpnumber = 0;

	/**
	 * 机台对应的部件
	 */
	@Transient
	private OfferPart offerPart;

	public OfferType getOfferType()
	{
		return offerType;
	}

	public void setOfferType(OfferType offerType)
	{
		this.offerType = offerType;
	}

	public OfferMachineType getOfferMachineType()
	{
		return offerMachineType;
	}

	public void setOfferMachineType(OfferMachineType offerMachineType)
	{
		this.offerMachineType = offerMachineType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMaxStyle()
	{
		return maxStyle;
	}

	public void setMaxStyle(String maxStyle)
	{
		this.maxStyle = maxStyle;
	}

	public String getMinStyle()
	{
		return minStyle;
	}

	public void setMinStyle(String minStyle)
	{
		this.minStyle = minStyle;
	}

	public MachinePro getMachinePro()
	{
		return machinePro;
	}

	public void setMachinePro(MachinePro machinePro)
	{
		this.machinePro = machinePro;
	}

	public MachinePrintColor getMachinePrintColor()
	{
		return machinePrintColor;
	}

	public void setMachinePrintColor(MachinePrintColor machinePrintColor)
	{
		this.machinePrintColor = machinePrintColor;
	}

	public Integer getMinPrintPly()
	{
		return minPrintPly;
	}

	public void setMinPrintPly(Integer minPrintPly)
	{
		this.minPrintPly = minPrintPly;
	}

	public Integer getMaxPrintPly()
	{
		return maxPrintPly;
	}

	public void setMaxPrintPly(Integer maxPrintPly)
	{
		this.maxPrintPly = maxPrintPly;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public String getUpdateName()
	{
		return updateName;
	}

	public void setUpdateName(String updateName)
	{
		this.updateName = updateName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public OfferStartPrint getOfferStartPrint()
	{
		return offerStartPrint;
	}

	public void setOfferStartPrint(OfferStartPrint offerStartPrint)
	{
		this.offerStartPrint = offerStartPrint;
	}

	public OfferFormula getOfferFormula()
	{
		return offerFormula;
	}

	public void setOfferFormula(OfferFormula offerFormula)
	{
		this.offerFormula = offerFormula;
	}

	public OfferSettingType getOfferSettingType()
	{
		return offerSettingType;
	}

	public void setOfferSettingType(OfferSettingType offerSettingType)
	{
		this.offerSettingType = offerSettingType;
	}

	/**
	 * 
	 * <pre>
	 * 机台属性文本
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月19日 下午6:33:28, think
	 */
	public String getMachineProText()
	{
		if (machinePro != null)
		{
			return machinePro.getText();
		}
		return "-";
	}

	/**
	 * 
	 * <pre>
	 * 机台印色文本
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月19日 下午6:34:24, think
	 */
	public String getMachinePrintColorText()
	{
		if (machinePrintColor != null)
		{
			return machinePrintColor.getText();
		}
		return "-";
	}

	// ==================== 内部核价 ====================

	public double getLowerPrice()
	{
		return lowerPrice;
	}

	public void setLowerPrice(double lowerPrice)
	{
		this.lowerPrice = lowerPrice;
	}

	public Boolean getIsLowerPrice()
	{
		return isLowerPrice;
	}

	public void setIsLowerPrice(Boolean isLowerPrice)
	{
		this.isLowerPrice = isLowerPrice;
	}

	public Integer getSheetLen()
	{
		return sheetLen;
	}

	public String getBoxType()
	{
		return boxType;
	}

	public void setBoxType(String boxType)
	{
		this.boxType = boxType;
	}

	public void setSheetLen(Integer sheetLen)
	{
		this.sheetLen = sheetLen;
	}

	public Integer getSheetWidth()
	{
		return sheetWidth;
	}

	public void setSheetWidth(Integer sheetWidth)
	{
		this.sheetWidth = sheetWidth;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public Integer getSheetNum()
	{
		return sheetNum;
	}

	public void setSheetNum(Integer sheetNum)
	{
		this.sheetNum = sheetNum;
	}

	public OfferPrintStyleType getOfferPrintStyleType()
	{
		return offerPrintStyleType;
	}

	public String getOfferPrintStyleTypeText()
	{
		if (offerPrintStyleType != null)
		{
			return offerPrintStyleType.getText();
		}
		return null;
	}

	public void setOfferPrintStyleType(OfferPrintStyleType offerPrintStyleType)
	{
		this.offerPrintStyleType = offerPrintStyleType;
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

	public String getPaperTypeText()
	{
		if (paperType != null)
		{
			return paperType.getText();
		}
		return null;
	}

	public void setPaperType(PaperType paperType)
	{
		this.paperType = paperType;
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

	public Integer getBfluteNum()
	{
		return bfluteNum;
	}

	public void setBfluteNum(Integer bfluteNum)
	{
		this.bfluteNum = bfluteNum;
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

	public Integer getTpnumber()
	{
		return tpnumber;
	}

	public void setTpnumber(Integer tpnumber)
	{
		this.tpnumber = tpnumber;
	}

	public OfferPart getOfferPart()
	{
		return offerPart;
	}

	public void setOfferPart(OfferPart offerPart)
	{
		this.offerPart = offerPart;
	}
}
