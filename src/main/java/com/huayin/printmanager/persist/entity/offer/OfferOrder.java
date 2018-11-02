/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 下午1:33:43
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.OfferSheetType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.PaperType;
import com.huayin.printmanager.utils.DateUtils;

/**
 * <pre>
 * 报价模块 - 报价单
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日
 */
@Entity
@Table(name = "offer_order")
public class OfferOrder extends BaseOfferEntity implements Cloneable
{
	private static final long serialVersionUID = -9064495946832615405L;

	// ==================== 自动报价 - 引用单据 ======================

	/**
	 * 单据类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType billType;

	/**
	 * 单据编号
	 */
	@Column(length = 50)
	private String billNo;
	
	/**
	 * 销售订单id
	 */
	@Column(length = 20)
	private Long saleId;

	// ==================== 自动报价 - 公共信息 ======================

	/**
	 * 报价类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private OfferType offerType;

	/**
	 * 报价类型文字描述
	 */
	@Transient
	private String OfferTypeText;

	/**
	 * 纸张类型 0默认大度 1正度
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private PaperType paperType;
	
	/**
	 * 设计样式（offer_order表没有该字段）
	 */
	private String designType;

	/**
	 * 设计费用（offer_order表没有该字段）
	 */
	private BigDecimal designFee;

	// ==================== 自动报价 - 基本信息 ======================

	/**
	 * 报价人
	 */
	private String createName;

	/**
	 * 成品尺寸-长
	 */
	private Integer length;

	/**
	 * 成品尺寸-宽
	 */
	private Integer width;

	/**
	 * 成品尺寸-高
	 */
	private Integer height;

	/**
	 * 成品尺寸-展长（265）
	 */
	private Integer styleLength;

	/**
	 * 成品尺寸-展宽（230）
	 */
	private Integer styleWidth;

	/**
	 * 成品尺寸（不分类型; 如，正2开(530*760)、90*50、B6(176*125)3号信封）
	 */
	@Column(length = 100)
	private String spec;

	/**
	 * 成品尺寸(用于展示)
	 */
	@Column(length = 100)
	private String specification;

	/**
	 * 纸张数（页）
	 */
	private Integer pageType;

	/**
	 * 联单联数
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private OfferSheetType sheetType;

	/**
	 * 印刷数量
	 */
	private Integer amount;

	/**
	 * 多部件
	 */
	@Transient
	private List<OfferPart> offerPartList;

	/**
	 * 增值税
	 */
	private Integer taxPercent;

	/**
	 * 阶梯数量列数
	 */
	private Integer ladderCol;

	/**
	 * 阶梯数量间隔
	 */
	private Integer ladderSpeed;

	/**
	 * 成品工序
	 */
	@Transient
	private List<OfferPartProcedure> productProcedure;

	/**
	 * 成品工序字符串
	 */
	@Column(length = 3000)
	private String productProcedureStr;

	// ==================== 自动报价 - 对外报价 ======================

	/**
	 * 报价时间
	 */
	private Date createDateTime;

	/**
	 * 交货时间
	 */
	private Date deliveryDate;

	/**
	 * 报价单号
	 */
	@Column(length = 50)
	private String offerNo;

	/**
	 * 客户名称
	 */
	@Column(length = 100)
	private String customerName;

	/**
	 * 客户地址
	 */
	@Column(length = 100)
	private String linkAddress;

	/**
	 * 联系人
	 */
	@Column(length = 50)
	private String linkName;

	/**
	 * 联系方式
	 */
	@Column(length = 20)
	private String phone;

	/**
	 * 成品名称
	 */
	@Column(length = 100)
	private String productName;

	/**
	 * 对外内部阶梯数据列表
	 */
	@Transient
	private List<OfferOrderQuoteOut> offerOrderQuoteOutList;
	// ==================== 自动报价 - 内部核价 ======================

	/**
	 * 页面选中的机台部件名称（boxtype + id）
	 */
	@Transient
	private List<String> chooseedMachineList;

	/**
	 * 类型：取选择的名称，如果选择的是平口盒就平口盒，选择的是其他盒型就是其他盒型
	 */
	private String boxType;

	/**
	 * 机台列表
	 */
	@Transient
	private List<OfferMachine> offerMachineList;

	/**
	 * 机台报价列表
	 */
	@Transient
	private List<Map<String, Object>> offerMachineOrderList;

	/**
	 * 内部核价单阶梯数据报表
	 */
	@Transient
	private List<OfferOrderQuoteInner> offerOrderQuoteInnerList;

	/**
	 * 机台报价费用明细
	 */
	@Transient
	private List<Map<String, Object>> offerMachineDetailFeeList;

	/**
	 * 拼版开了图
	 */
	@Transient
	private List<Map<String, Object>> offerOpeningList;

	/**
	 * 其他费用
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double ohterFee;

	/**
	 * 印刷费用
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double printFee;

	/**
	 * 工序费
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double procedureFee;

	/**
	 * 纸张费
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double paperFee = 0.0;

	/**
	 * 物流费(运费)
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double freightFee;

	/**
	 * 合计费用
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double costMoney;

	/**
	 *单价
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double unitPrice;

	/**
	 * 成品费用
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double productFee;

	/**
	 * 总P数
	 */
	private Integer tpnumber;

	// ==================== 自动报价 - 引用单据 ======================

	public BillType getBillType()
	{
		return billType;
	}

	public void setBillType(BillType billType)
	{
		this.billType = billType;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	// ==================== 自动报价 - 公共信息 ======================

	public Long getSaleId()
	{
		return saleId;
	}

	public void setSaleId(Long saleId)
	{
		this.saleId = saleId;
	}

	public OfferType getOfferType()
	{
		return offerType;
	}

	public void setOfferType(OfferType offerType)
	{
		this.offerType = offerType;
	}

	public String getOfferTypeText()
	{
		if (offerType != null)
		{
			return offerType.getText();
		}
		return null;
	}

	public void setOfferTypeText(String offerTypeText)
	{
		OfferTypeText = offerTypeText;
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
	
	public String getDesignType()
	{
		return designType;
	}

	public void setDesignType(String designType)
	{
		this.designType = designType;
	}
	
	public BigDecimal getDesignFee()
	{
		return designFee;
	}

	public void setDesignFee(BigDecimal designFee)
	{
		this.designFee = designFee;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public Integer getLength()
	{
		return length;
	}

	public void setLength(Integer length)
	{
		this.length = length;
	}

	public Integer getWidth()
	{
		return width;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}

	public Integer getHeight()
	{
		return height;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}

	public Integer getStyleLength()
	{
		return styleLength;
	}

	public void setStyleLength(Integer styleLength)
	{
		this.styleLength = styleLength;
	}

	public Integer getStyleWidth()
	{
		return styleWidth;
	}

	public void setStyleWidth(Integer styleWidth)
	{
		this.styleWidth = styleWidth;
	}

	public String getSpec()
	{
		return spec;
	}

	public void setSpec(String spec)
	{
		this.spec = spec;
	}

	public String getSpecification()
	{
		return specification;
	}

	public void setSpecification(String specification)
	{
		this.specification = specification;
	}

	public Integer getPageType()
	{
		return pageType;
	}

	public void setPageType(Integer pageType)
	{
		this.pageType = pageType;
	}

	public OfferSheetType getSheetType()
	{
		return sheetType;
	}

	public String getSheetTypeText()
	{
		if (null != sheetType)
		{
			return sheetType.getText();
		}
		return null;
	}

	public void setSheetType(OfferSheetType sheetType)
	{
		this.sheetType = sheetType;
	}

	public Integer getAmount()
	{
		return amount;
	}

	public void setAmount(Integer amount)
	{
		this.amount = amount;
	}

	public Integer getTaxPercent()
	{
		return taxPercent;
	}

	public void setTaxPercent(Integer taxPercent)
	{
		this.taxPercent = taxPercent;
	}

	public List<OfferPart> getOfferPartList()
	{
		return offerPartList;
	}

	public void setOfferPartList(List<OfferPart> offerPartList)
	{
		this.offerPartList = offerPartList;
	}

	public Integer getLadderCol()
	{
		return ladderCol;
	}

	public void setLadderCol(Integer ladderCol)
	{
		this.ladderCol = ladderCol;
	}

	public Integer getLadderSpeed()
	{
		return ladderSpeed;
	}

	public void setLadderSpeed(Integer ladderSpeed)
	{
		this.ladderSpeed = ladderSpeed;
	}

	public List<OfferPartProcedure> getProductProcedure()
	{
		return productProcedure;
	}

	public void setProductProcedure(List<OfferPartProcedure> productProcedure)
	{
		this.productProcedure = productProcedure;
	}

	public String getProductProcedureStr()
	{
		return productProcedureStr;
	}

	public void setProductProcedureStr(String productProcedureStr)
	{
		this.productProcedureStr = productProcedureStr;
	}

	// ==================== 自动报价 - 对外报价 ======================

	public Date getCreateDateTime()
	{
		return createDateTime;
	}

	public String getCreateDateTimeStr()
	{
		if (createDateTime != null)
		{
			return DateUtils.formatDate(createDateTime, "yyyy-MM-dd");
		}
		return null;
	}

	public void setCreateDateTime(Date createDateTime)
	{
		this.createDateTime = createDateTime;
	}

	public Date getDeliveryDate()
	{
		return deliveryDate;
	}

	public String getDeliveryDateStr()
	{
		if (deliveryDate != null)
		{
			return DateUtils.formatDate(deliveryDate, "yyyy-MM-dd");
		}
		return null;
	}

	public void setDeliveryDate(Date deliveryDate)
	{
		this.deliveryDate = deliveryDate;
	}

	public String getOfferNo()
	{
		return offerNo;
	}

	public void setOfferNo(String offerNo)
	{
		this.offerNo = offerNo;
	}

	public String getLinkAddress()
	{
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress)
	{
		this.linkAddress = linkAddress;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public String getLinkName()
	{
		return linkName;
	}

	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	// ==================== 自动报价 - 内部核价 ======================

	public List<String> getChooseedMachineList()
	{
		return chooseedMachineList;
	}

	public void setChooseedMachineList(List<String> chooseedMachineList)
	{
		this.chooseedMachineList = chooseedMachineList;
	}

	public String getBoxType()
	{
		return boxType;
	}

	public void setBoxType(String boxType)
	{
		this.boxType = boxType;
	}

	public List<OfferMachine> getOfferMachineList()
	{
		return offerMachineList;
	}

	public void setOfferMachineList(List<OfferMachine> offerMachineList)
	{
		this.offerMachineList = offerMachineList;
	}

	public List<Map<String, Object>> getOfferMachineOrderList()
	{
		return offerMachineOrderList;
	}

	public void setOfferMachineOrderList(List<Map<String, Object>> offerMachineOrderList)
	{
		this.offerMachineOrderList = offerMachineOrderList;
	}

	public List<OfferOrderQuoteOut> getOfferOrderQuoteOutList()
	{
		return offerOrderQuoteOutList;
	}

	public void setOfferOrderQuoteOutList(List<OfferOrderQuoteOut> offerOrderQuoteOutList)
	{
		this.offerOrderQuoteOutList = offerOrderQuoteOutList;
	}

	public List<OfferOrderQuoteInner> getOfferOrderQuoteInnerList()
	{
		return offerOrderQuoteInnerList;
	}

	public void setOfferOrderQuoteInnerList(List<OfferOrderQuoteInner> offerOrderQuoteInnerList)
	{
		this.offerOrderQuoteInnerList = offerOrderQuoteInnerList;
	}

	public List<Map<String, Object>> getOfferMachineDetailFeeList()
	{
		return offerMachineDetailFeeList;
	}

	public void setOfferMachineDetailFeeList(List<Map<String, Object>> offerMachineDetailFeeList)
	{
		this.offerMachineDetailFeeList = offerMachineDetailFeeList;
	}

	public List<Map<String, Object>> getOfferOpeningList()
	{
		return offerOpeningList;
	}

	public void setOfferOpeningList(List<Map<String, Object>> offerOpeningList)
	{
		this.offerOpeningList = offerOpeningList;
	}

	public Double getOhterFee()
	{
		return ohterFee;
	}

	public void setOhterFee(Double ohterFee)
	{
		this.ohterFee = ohterFee;
	}

	public Double getPrintFee()
	{
		return printFee;
	}

	public void setPrintFee(Double printFee)
	{
		this.printFee = printFee;
	}

	public Double getProcedureFee()
	{
		return procedureFee;
	}

	public void setProcedureFee(Double procedureFee)
	{
		this.procedureFee = procedureFee;
	}

	public Double getPaperFee()
	{
		return paperFee;
	}

	public void setPaperFee(Double paperFee)
	{
		this.paperFee = paperFee;
	}

	public Double getFreightFee()
	{
		return freightFee;
	}

	public void setFreightFee(Double freightFee)
	{
		this.freightFee = freightFee;
	}

	public Double getCostMoney()
	{
		return costMoney;
	}

	public void setCostMoney(Double costMoney)
	{
		this.costMoney = costMoney;
	}

	public Double getUnitPrice()
	{
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice)
	{
		this.unitPrice = unitPrice;
	}

	public Double getProductFee()
	{
		return productFee;
	}

	public void setProductFee(Double productFee)
	{
		this.productFee = productFee;
	}

	public Integer getTpnumber()
	{
		return tpnumber;
	}

	public void setTpnumber(Integer tpnumber)
	{
		this.tpnumber = tpnumber;
	}
	// ==================== 待整理 TODO ======================

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}