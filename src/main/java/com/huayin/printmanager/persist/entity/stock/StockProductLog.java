/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.stock;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 产品出入库记录
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_product_log")
public class StockProductLog extends BaseTableIdEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 单据类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType billType;

	/**
	 * 单据ID
	 */
	private Long billId;

	/**
	 * 单据编号
	 */
	@Column(length = 50)
	private String billNo;
	
	/**
	 * 表id（暂用于变更）
	 */
	private Long sourceId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 仓库Id
	 */
	private Long warehouseId;

	/**
	 * 产品分类
	 */
	@Column(length = 50)
	private Long productClassId;

	/**
	 * 产品编号
	 */
	@Column(length = 50)
	private String code;

	/**
	 * 客户料号
	 */
	@Column(length = 50)
	private String customerMaterialCode;

	/**
	 * 成品名称
	 */
	@Column(length = 50)
	private String productName;
	
	/**
	 * 成品id
	 */
	@Column(length = 20)
	private Long productId;

	/**
	 * 产品图片
	 */
	@Transient
	private String imgUrl;

	/**
	 * 产品规格
	 */
	@Column(length = 50)
	private String specifications;

	/**
	 * 单位
	 */
	private Long unitId;

	/**
	 * 入库数量
	 */
	private Integer inQty;

	/**
	 * 出库数量
	 */
	private Integer outQty;

	/**
	 * 单价(含税)
	 */
	private BigDecimal price;

	/**
	 * 入库金额
	 */
	private BigDecimal inMoney;

	/**
	 * 出库金额
	 */
	private BigDecimal outMoney;
	
	/**
	 * 库存数量
	 */
	private Integer storgeQty;
	
	/**
	 * 客户id
	 */
	private Long customerId;
	
	/**
	 * 客户名称
	 */
	private String customerName;
	
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
	
	public Long getSourceId()
	{
		return sourceId;
	}

	public void setSourceId(Long sourceId)
	{
		this.sourceId = sourceId;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	
	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public Long getUnitId()
	{
		return unitId;
	}

	public void setUnitId(Long unitId)
	{
		this.unitId = unitId;
	}

	public Integer getInQty()
	{
		return inQty;
	}

	public void setInQty(Integer inQty)
	{
		this.inQty = inQty;
	}

	public Integer getOutQty()
	{
		return outQty;
	}

	public void setOutQty(Integer outQty)
	{
		this.outQty = outQty;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getInMoney()
	{
		return inMoney;
	}

	public void setInMoney(BigDecimal inMoney)
	{
		this.inMoney = inMoney;
	}

	public BigDecimal getOutMoney()
	{
		return outMoney;
	}

	public void setOutMoney(BigDecimal outMoney)
	{
		this.outMoney = outMoney;
	}

	public Long getBillId()
	{
		return billId;
	}

	public void setBillId(Long billId)
	{
		this.billId = billId;
	}

	public Long getProductClassId()
	{
		return productClassId;
	}

	public void setProductClassId(Long productClassId)
	{
		this.productClassId = productClassId;
	}

	public String getCustomerMaterialCode()
	{
		return customerMaterialCode;
	}

	public void setCustomerMaterialCode(String customerMaterialCode)
	{
		this.customerMaterialCode = customerMaterialCode;
	}

	public String getSpecifications()
	{
		return specifications;
	}

	public void setSpecifications(String specifications)
	{
		this.specifications = specifications;
	}

	public Integer getStorgeQty()
	{
		return storgeQty;
	}

	public void setStorgeQty(Integer storgeQty)
	{
		this.storgeQty = storgeQty;
	}

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public String getBillTypeText()
	{
		if (billType != null)
		{
			return billType.getText();
		}
		else
		{
			return "-";
		}
	}

	public String getWarehouseName()
	{
		if (warehouseId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.WAREHOUSE.name(), warehouseId, "name");
		}
		return "-";
	}

	public String getProductClassName()
	{
		if (productClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.PRODUCTCLASS.name(), productClassId, "name");
		}
		return "-";
	}

	public String getUnitName()
	{
		if (unitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), unitId, "name");
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
