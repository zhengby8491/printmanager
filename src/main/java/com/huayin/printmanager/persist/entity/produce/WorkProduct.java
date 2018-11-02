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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 生产管理 - 生产工单：工单产品表
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年5月24日, zhong
 * @version 	   2.0, 2018年2月23日上午10:24:56, zhengby, 代码规范
 */
@Entity
@Table(name = "produce_work_product")
public class WorkProduct extends BaseBillDetailTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 源单类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType sourceBillType;

	/**
	 * 是否发外
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isOutSource;

	/**
	 * 源单ID
	 */
	@Column(length = 50)
	private Long sourceId;

	/**
	 * 源单单据编号
	 */
	@Column(length = 50)
	private String sourceBillNo;

	/**
	 * 源单明细ID
	 */
	@Column(length = 50)
	private Long sourceDetailId;

	/**
	 * 源单数量（销售数量）
	 */
	private Integer sourceQty;

	/**
	 * 客户ID
	 */
	@Column(length = 50)
	private Long customerId;

	/**
	 * 客户代码
	 */
	@Column(length = 50)
	private String customerCode;

	/**
	 * 客户名称
	 */
	@Column(length = 50)
	private String customerName;

	/**
	 * 客户单号
	 */
	@Column(length = 50)
	private String customerBillNo;

	/**
	 * 客户料号
	 */
	@Column(length = 50)
	private String customerMaterialCode;

	/**
	 * 产品ID
	 */
	@Column(length = 50)
	private Long productId;

	/**
	 * 成品名称
	 */
	@Column(length = 50)
	private String productName;

	/**
	 * 产品规格
	 */
	private String style;

	/**
	 * 单位ID
	 */
	private Long unitId;

	/**
	 * 销售生产数量
	 */
	private Integer saleProduceQty;

	/**
	 * 销售备品生产数量
	 */
	private Integer spareProduceQty;

	/**
	 * 生产数量
	 */
	private Integer produceQty;

	/**
	 * 已外发数量
	 */
	private Integer outOfQty;

	/**
	 *已入库数量
	 */
	private Integer inStockQty;

	/**
	 * 完工数量
	 */
	@Transient
	private Integer completeQty;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 客户要求
	 */
	@Column(length = 150)
	private String customerRequire;

	/**
	 * 工单对象
	 */
	@Transient
	private Work master;

	/**
	 * 单价(含税)
	 */
	private BigDecimal price;

	/**
	 * 金额（含税）
	 */
	private BigDecimal money;

	/**
	 * 已送货数量
	 */
	private Integer deliverQty = 0;

	/**
	 * 已送备品数量
	 */
	private Integer deliverSpareedQty = 0;

	/**
	 * 已送货金额
	 */
	private BigDecimal deliverMoney = new BigDecimal(0);

	/**
	 * 产品代码
	 */
	@Column(length = 50)
	private String productCode;

	/**
	 * 金额(不含税）
	 */
	private BigDecimal noTaxMoney;

	/**
	 * 税额
	 */
	private BigDecimal tax;

	/**
	 * 税率ID
	 */
	private Long taxRateId;

	/**
	 * 税率值
	 */
	@Column(length = 20)
	private Integer percent;

	/**
	 * 产品图片
	 */
	@Transient
	private String imgUrl;

	/**
	 * 单价(不含税）
	 */
	private BigDecimal noTaxPrice;

	public BillType getSourceBillType()
	{
		return sourceBillType;
	}

	public void setSourceBillType(BillType sourceBillType)
	{
		this.sourceBillType = sourceBillType;
	}

	public Long getSourceId()
	{
		return sourceId;
	}

	public void setSourceId(Long sourceId)
	{
		this.sourceId = sourceId;
	}

	public String getSourceBillNo()
	{
		return sourceBillNo;
	}

	public void setSourceBillNo(String sourceBillNo)
	{
		this.sourceBillNo = sourceBillNo;
	}

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public String getCustomerCode()
	{
		return customerCode;
	}

	public void setCustomerCode(String customerCode)
	{
		this.customerCode = customerCode;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public String getCustomerBillNo()
	{
		return customerBillNo;
	}

	public void setCustomerBillNo(String customerBillNo)
	{
		this.customerBillNo = customerBillNo;
	}

	public String getCustomerMaterialCode()
	{
		return customerMaterialCode;
	}

	public void setCustomerMaterialCode(String customerMaterialCode)
	{
		this.customerMaterialCode = customerMaterialCode;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public Integer getSpareProduceQty()
	{
		return spareProduceQty;
	}

	public void setSpareProduceQty(Integer spareProduceQty)
	{
		this.spareProduceQty = spareProduceQty;
	}

	public Integer getOutOfQty()
	{
		return outOfQty;
	}

	public void setOutOfQty(Integer outOfQty)
	{
		this.outOfQty = outOfQty;
	}

	public Integer getInStockQty()
	{
		return inStockQty;
	}

	public void setInStockQty(Integer inStockQty)
	{
		this.inStockQty = inStockQty;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public String getCustomerRequire()
	{
		return customerRequire;
	}

	public void setCustomerRequire(String customerRequire)
	{
		this.customerRequire = customerRequire;
	}

	public Work getMaster()
	{
		return master;
	}

	public void setMaster(Work master)
	{
		this.master = master;
	}

	public Long getUnitId()
	{
		return unitId;
	}

	public void setUnitId(Long unitId)
	{
		this.unitId = unitId;
	}

	public Integer getSourceQty()
	{
		return sourceQty;
	}

	public void setSourceQty(Integer sourceQty)
	{
		this.sourceQty = sourceQty;
	}

	public Integer getProduceQty()
	{
		return produceQty;
	}

	public void setProduceQty(Integer produceQty)
	{
		this.produceQty = produceQty;
	}

	public Integer getSaleProduceQty()
	{
		return saleProduceQty;
	}

	public void setSaleProduceQty(Integer saleProduceQty)
	{
		this.saleProduceQty = saleProduceQty;
	}

	public Long getSourceDetailId()
	{
		return sourceDetailId;
	}

	public void setSourceDetailId(Long sourceDetailId)
	{
		this.sourceDetailId = sourceDetailId;
	}

	public BoolValue getIsOutSource()
	{
		return isOutSource;
	}

	public void setIsOutSource(BoolValue isOutSource)
	{
		this.isOutSource = isOutSource;
	}

	// ----------------------------------------------------------------------
	public String getUnitName()
	{
		if (unitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), unitId, "name");
		}
		return "-";
	}

	public Integer getCompleteQty()
	{
		return completeQty;
	}

	public void setCompleteQty(Integer completeQty)
	{
		this.completeQty = completeQty;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public Integer getDeliverQty()
	{
		return deliverQty;
	}

	public void setDeliverQty(Integer deliverQty)
	{
		this.deliverQty = deliverQty;
	}

	public Integer getDeliverSpareedQty()
	{
		return deliverSpareedQty;
	}

	public void setDeliverSpareedQty(Integer deliverSpareedQty)
	{
		this.deliverSpareedQty = deliverSpareedQty;
	}

	public BigDecimal getDeliverMoney()
	{
		return deliverMoney;
	}

	public void setDeliverMoney(BigDecimal deliverMoney)
	{
		this.deliverMoney = deliverMoney;
	}

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}

	public BigDecimal getNoTaxMoney()
	{
		return noTaxMoney;
	}

	public void setNoTaxMoney(BigDecimal noTaxMoney)
	{
		this.noTaxMoney = noTaxMoney;
	}

	public BigDecimal getTax()
	{
		return tax;
	}

	public void setTax(BigDecimal tax)
	{
		this.tax = tax;
	}

	public Long getTaxRateId()
	{
		return taxRateId;
	}

	public void setTaxRateId(Long taxRateId)
	{
		this.taxRateId = taxRateId;
	}

	public Integer getPercent()
	{
		return percent;
	}

	public void setPercent(Integer percent)
	{
		this.percent = percent;
	}

	public BigDecimal getNoTaxPrice()
	{
		return noTaxPrice;
	}

	public void setNoTaxPrice(BigDecimal noTaxPrice)
	{
		this.noTaxPrice = noTaxPrice;
	}

	public String getImgUrl()
	{
		return imgUrl;
	}

	public void setImgUrl(String imgUrl)
	{
		this.imgUrl = imgUrl;
	}

}
