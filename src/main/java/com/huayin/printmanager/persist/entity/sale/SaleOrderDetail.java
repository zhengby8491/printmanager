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
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.ProductType;

/**
 * <pre>
 * 销售管理 - 销售订单：销售订单明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年5月23日 下午7:07:15
 * @version 	   2.0, 2018年2月22日下午5:27:53, zhengby, 代码规范
 */
@Entity
@Table(name = "sale_order_detail")
public class SaleOrderDetail extends SaleDetailBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 已生产数量
	 */
	private Integer produceedQty = 0;

	/**
	 * 未生产数量，临时返回属性，不要生成字段
	 */
	@Transient
	private Integer produceedQty2 = 0;

	/**
	 * 已入库数量,临时返回属性，不需要生成字段
	 */
	@Transient
	private Integer stockQty;

	/**
	 * 已对账数量,临时返回属性，不需要生成字段
	 */
	@Transient
	private Integer reconcilQty;

	/**
	 * 已收款金额,临时返回属性，不需要生成字段
	 */
	@Transient
	private BigDecimal receiveMoney;

	/**
	 * 已生产备品数量
	 */
	private Integer produceSpareedQty = 0;

	/**
	 * 已送货数量
	 */
	private Integer deliverQty = 0;

	/**
	 * 未送货数量，临时返回属性，不要生成字段
	 */
	@Transient
	private Integer deliverQty2 = 0;

	/**
	 * 已生产审核状态
	 */
	@Transient
	private Boolean produceedCheck;

	/**
	 * 已入库审核状态
	 */
	@Transient
	private Boolean stockCheck;

	/**
	 * 已送货审核状态
	 */
	@Transient
	private Boolean deliverCheck;

	/**
	 * 已对账审核状态
	 */
	@Transient
	private Boolean reconcilCheck;

	/**
	 * 已收款审核状态
	 */
	@Transient
	private Boolean receiveCheck;

	/**
	 * 已送货金额
	 */
	private BigDecimal deliverMoney = new BigDecimal(0);

	/**
	 * 已送备品数量
	 */
	private Integer deliverSpareedQty = 0;
	
	/**
	 * 库存数量
	 */
	@Transient
	private Integer storageQty = 0;
	
	/**
	 * 报价单号ID
	 */
	@Column(length = 20)
	private Long offerId;

	/**
	 * 报价单号
	 */
	@Column(length = 50)
	private String offerNo;

	/**
	 * 销售订单主表
	 */
	@Transient
	private SaleOrder master;

	/**
	 * 产品类别
	 */
	@Transient
	@Enumerated(EnumType.STRING)
	private ProductType productType;

	/**
	 * 部件列表
	 */
	@Transient
	private List<SaleOrderPart> partList = new ArrayList<SaleOrderPart>();

	/**
	 * 清空所有部件（页面提交时用到）
	 */
	@Transient
	private Boolean partTruncate = null;

	/**
	 * 装订打包
	 */
	@Transient
	private SaleOrderPack pack;

	public Integer getDeliverQty()
	{
		return deliverQty;
	}

	public void setDeliverQty(Integer deliverQty)
	{
		this.deliverQty = deliverQty;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public Integer getProduceedQty()
	{
		return produceedQty;
	}

	public void setProduceedQty(Integer produceedQty)
	{
		this.produceedQty = produceedQty;
	}

	public Integer getProduceSpareedQty()
	{
		return produceSpareedQty;
	}

	public void setProduceSpareedQty(Integer produceSpareedQty)
	{
		this.produceSpareedQty = produceSpareedQty;
	}

	public Integer getDeliverSpareedQty()
	{
		return deliverSpareedQty;
	}

	public void setDeliverSpareedQty(Integer deliverSpareedQty)
	{
		this.deliverSpareedQty = deliverSpareedQty;
	}
	
	public Integer getStorageQty()
	{
		return storageQty;
	}

	public void setStorageQty(Integer storageQty)
	{
		this.storageQty = storageQty;
	}

	public Long getOfferId()
	{
		return offerId;
	}

	public void setOfferId(Long offerId)
	{
		this.offerId = offerId;
	}

	public String getOfferNo()
	{
		return offerNo;
	}

	public void setOfferNo(String offerNo)
	{
		this.offerNo = offerNo;
	}

	public SaleOrder getMaster()
	{
		return master;
	}

	public void setMaster(SaleOrder master)
	{
		this.master = master;
	}

	public Integer getStockQty()
	{
		return stockQty;
	}

	public void setStockQty(Integer stockQty)
	{
		this.stockQty = stockQty;
	}

	public Integer getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(Integer reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public BigDecimal getReceiveMoney()
	{
		return receiveMoney;
	}

	public void setReceiveMoney(BigDecimal receiveMoney)
	{
		this.receiveMoney = receiveMoney;
	}

	public BigDecimal getDeliverMoney()
	{
		return deliverMoney;
	}

	public void setDeliverMoney(BigDecimal deliverMoney)
	{
		this.deliverMoney = deliverMoney;
	}

	public Boolean getDeliverCheck()
	{
		return deliverCheck;
	}

	public void setDeliverCheck(Boolean deliverCheck)
	{
		this.deliverCheck = deliverCheck;
	}

	public Boolean getReconcilCheck()
	{
		return reconcilCheck;
	}

	public void setReconcilCheck(Boolean reconcilCheck)
	{
		this.reconcilCheck = reconcilCheck;
	}

	public Boolean getProduceedCheck()
	{
		return produceedCheck;
	}

	public void setProduceedCheck(Boolean produceedCheck)
	{
		this.produceedCheck = produceedCheck;
	}

	public Boolean getStockCheck()
	{
		return stockCheck;
	}

	public void setStockCheck(Boolean stockCheck)
	{
		this.stockCheck = stockCheck;
	}

	public Boolean getReceiveCheck()
	{
		return receiveCheck;
	}

	public void setReceiveCheck(Boolean receiveCheck)
	{
		this.receiveCheck = receiveCheck;
	}

	public Integer getProduceedQty2()
	{
		return produceedQty2;
	}

	public void setProduceedQty2(Integer produceedQty2)
	{
		this.produceedQty2 = produceedQty2;
	}

	public Integer getDeliverQty2()
	{
		return deliverQty2;
	}

	public void setDeliverQty2(Integer deliverQty2)
	{
		this.deliverQty2 = deliverQty2;
	}

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
	}

	public List<SaleOrderPart> getPartList()
	{
		return partList;
	}

	public void setPartList(List<SaleOrderPart> partList)
	{
		this.partList = partList;
	}

	public SaleOrderPack getPack()
	{
		return pack;
	}

	public void setPack(SaleOrderPack pack)
	{
		this.pack = pack;
	}

	public Boolean getPartTruncate()
	{
		return partTruncate;
	}

	public void setPartTruncate(Boolean partTruncate)
	{
		this.partTruncate = partTruncate;
	}
}
