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

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 : 销售模块明细表基础类
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年5月23日 下午7:07:15, zhengby
 * @version 	   2.0, 2018年2月23日上午11:30:24, zhengby, 代码规范
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class SaleDetailBaseEntity extends BaseBillDetailTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 源单类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType sourceBillType;

	/**
	 * 源单ID
	 */
	@Column(length = 20)
	private Long sourceId;
	

	/**
	 * 源单明细ID
	 */
	@Column(length = 50)
	private Long sourceDetailId;

	/**
	 * 源单单据编号
	 */
	@Column(length = 50)
	private String sourceBillNo;

	/**
	 * 源单数量(例如：如果施工单据类型-->取施工单材料表WorkMaterial表qty)
	 */
	private Integer sourceQty;

	/**
	 * 销售订单单据编号
	 */
	private String saleOrderBillNo;
	
	/**
	 * 销售订单id
	 */
	@Column(length = 20)
	private Long saleOrderId;
							 
	/**
	 * 产品ID
	 */
	@Column(length = 50)
	private Long productId;
	
	/**
	 * 产品图片
	 */
	@Transient
	private String imgUrl;
	/**
	 * 产品代码
	 */
	@Column(length = 50)
	private String productCode;

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
	 * 客户料号
	 */
	@Column(length = 50)
	private String customerMaterialCode;
	/**
	 * 销售单位
	 */
	@Column(length = 20)
	private Long unitId;

	/**
	 * 数量
	 */
	private Integer qty;

	/**
	 * 单价(含税)
	 */
	private BigDecimal price;

	/**
	 * 单价(不含税）
	 */
	private BigDecimal noTaxPrice;

	/**
	 * 金额（含税）
	 */
	private BigDecimal money;

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
	 * 客户要求
	 */
	private String custRequire;

	/**
	 * 备品数量
	 */
	private Integer spareQty;
	
	@Transient
	private String taxRateName;

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

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
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

	public Integer getQty()
	{
		return qty;
	}

	public void setQty(Integer qty)
	{
		this.qty = qty;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getNoTaxPrice()
	{
		return noTaxPrice;
	}

	public void setNoTaxPrice(BigDecimal noTaxPrice)
	{
		this.noTaxPrice = noTaxPrice;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
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

	public String getCustRequire()
	{
		return custRequire;
	}

	public void setCustRequire(String custRequire)
	{
		this.custRequire = custRequire;
	}

	public Integer getSpareQty()
	{
		return spareQty;
	}

	public void setSpareQty(Integer spareQty)
	{
		this.spareQty = spareQty;
	}

	public Integer getSourceQty()
	{
		return sourceQty;
	}

	public void setSourceQty(Integer sourceQty)
	{
		this.sourceQty = sourceQty;
	}

	public Long getUnitId()
	{
		return unitId;
	}
	public void setUnitId(Long unitId)
	{
		this.unitId = unitId;
	}

	public String getCustomerMaterialCode()
	{
		return customerMaterialCode;
	}

	public void setCustomerMaterialCode(String customerMaterialCode)
	{
		this.customerMaterialCode = customerMaterialCode;
	}

	public Long getSourceDetailId()
	{
		return sourceDetailId;
	}

	public void setSourceDetailId(Long sourceDetailId)
	{
		this.sourceDetailId = sourceDetailId;
	}

	public String getSaleOrderBillNo()
	{
		return saleOrderBillNo;
	}

	public void setSaleOrderBillNo(String saleOrderBillNo)
	{
		this.saleOrderBillNo = saleOrderBillNo;
	}

	public Long getSaleOrderId()
	{
		return saleOrderId;
	}

	public void setSaleOrderId(Long saleOrderId)
	{
		this.saleOrderId = saleOrderId;
	}

	public String getUnitName()
	{
		if(unitId!=null){
			return (String)UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), unitId,"name");
		}
		return "-";
	}
	public String getTaxRateName()
	{
		if(taxRateId!=null){
			return (String)UserUtils.getBasicInfoFiledValue(BasicType.TAXRATE.name(), taxRateId,"name");
		}
		return "-";
	}
	public String getSourceBillTypeText()
	{
		if(sourceBillType!=null)
		{
			return sourceBillType.getText();
		}
		return "-";
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
