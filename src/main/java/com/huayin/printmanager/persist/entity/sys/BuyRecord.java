/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sys;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;

/**
 * <pre>
 * 系统模块 - 购买记录
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 * @since        1.0, 2018年04月19日, 重命名未sys_buy_record
 */
@Entity
@Table(name = "sys_buy_record")
public class BuyRecord extends BaseTableIdEntity
{
	private static final long serialVersionUID = -1274909943065493926L;

	/**
	 * 订单创建时间
	 */
	private Date createTime = new Date();

	/**
	 * 支付时间
	 */
	private Date payTime;

	/**
	 * 订单号
	 */
	@Column(length = 50)
	private String billNo;

	/**
	 * 购买服务名称
	 */
	@Column(length = 50)
	private String productName;

	/**
	 * 购买服务id
	 */
	private Long productId;

	/**
	 * 购买类型 1 购买  2升级
	 */
	private Integer type;

	/**
	 * 销售价格
	 */
	private BigDecimal price;

	/**
	 * 支付金额
	 */
	private BigDecimal payPrice;

	/**
	 * 税额
	 */
	private BigDecimal tax;

	/**
	 * 购买人
	 */
	@Column(length = 50)
	private String userName;

	/**
	 * 购买公司
	 */
	@Column(length = 100)
	private String companyName;

	/**
	 * 联系人
	 */
	@Column(length = 50)
	private String linkMan;

	/**
	 * 联系电话
	 */
	private String telephone;

	/**
	 * 邀请人
	 */
	private String inviter;

	/**
	 * 邀请人电话
	 */
	private String inviterPhone;

	/**
	 * 发票信息
	 */
	private String invoiceInfor;

	/**
	 * 是否支付
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BoolValue isPay = BoolValue.NO;

	/**
	 * 订单状态  0已取消   1待支付   2 已完成
	 */
	private Integer orderState = 1;

	/**
	 * 支付宝/微信  交易号
	 */
	@Column(length = 100)
	private String trade_no;

	/**
	 * 支付方式 1支付宝 2微信 3银行卡
	 */
	private Integer paymentMethod;

	/**
	 * 订单类型 1在线订单  2线下订单
	 */
	private Integer orderType = 1;

	/**
	 * 奖金
	 */
	private Integer bonus;

	/**
	 * 订单来源 1 pc  2微信
	 */
	private Integer orderFrom;

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getPayTime()
	{
		return payTime;
	}

	public void setPayTime(Date payTime)
	{
		this.payTime = payTime;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
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

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getTax()
	{
		return tax;
	}

	public void setTax(BigDecimal tax)
	{
		this.tax = tax;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public String getLinkMan()
	{
		return linkMan;
	}

	public void setLinkMan(String linkMan)
	{
		this.linkMan = linkMan;
	}

	public String getTelephone()
	{
		return telephone;
	}

	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}

	public String getInviter()
	{
		return inviter;
	}

	public void setInviter(String inviter)
	{
		this.inviter = inviter;
	}

	public String getInviterPhone()
	{
		return inviterPhone;
	}

	public void setInviterPhone(String inviterPhone)
	{
		this.inviterPhone = inviterPhone;
	}

	public String getInvoiceInfor()
	{
		return invoiceInfor;
	}

	public void setInvoiceInfor(String invoiceInfor)
	{
		this.invoiceInfor = invoiceInfor;
	}

	public BoolValue getIsPay()
	{
		return isPay;
	}

	public void setIsPay(BoolValue isPay)
	{
		this.isPay = isPay;
	}

	public String getTrade_no()
	{
		return trade_no;
	}

	public void setTrade_no(String trade_no)
	{
		this.trade_no = trade_no;
	}

	public Integer getPaymentMethod()
	{
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod)
	{
		this.paymentMethod = paymentMethod;
	}

	public Integer getOrderState()
	{
		return orderState;
	}

	public void setOrderState(Integer orderState)
	{
		this.orderState = orderState;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

	public BigDecimal getPayPrice()
	{
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice)
	{
		this.payPrice = payPrice;
	}

	public Integer getOrderType()
	{
		return orderType;
	}

	public void setOrderType(Integer orderType)
	{
		this.orderType = orderType;
	}

	public Integer getBonus()
	{
		return bonus;
	}

	public void setBonus(Integer bonus)
	{
		this.bonus = bonus;
	}

	public Integer getOrderFrom()
	{
		return orderFrom;
	}

	public void setOrderFrom(Integer orderFrom)
	{
		this.orderFrom = orderFrom;
	}

}
