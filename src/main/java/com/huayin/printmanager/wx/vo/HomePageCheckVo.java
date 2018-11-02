/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.vo;

/**
 * <pre>
 * 微信 - 主页审核记录数VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class HomePageCheckVo
{
	/**
	 * 销售待审核记录数
	 */
	private Integer saleCheckQty;

	/**
	 * 采购待审核记录数
	 */
	private Integer purchCheckQty;

	/**
	 * 发外待审核记录数
	 */
	private Integer outsourceCheckQty;

	/**
	 * 收款待审核记录数
	 */
	private Integer receiveCheckQty;

	/**
	 * 付款待审核记录数
	 */
	private Integer paymentCheckQty;

	/**
	 * 付款核销待审核记录数
	 */
	private Integer paymentWriteCheckQty;

	/**
	 * 收款核销待审核记录数
	 */
	private Integer receiveWriteCheckQty;

	public Integer getSaleCheckQty()
	{
		return saleCheckQty;
	}

	public void setSaleCheckQty(Integer saleCheckQty)
	{
		this.saleCheckQty = saleCheckQty;
	}

	public Integer getPurchCheckQty()
	{
		return purchCheckQty;
	}

	public void setPurchCheckQty(Integer purchCheckQty)
	{
		this.purchCheckQty = purchCheckQty;
	}

	public Integer getOutsourceCheckQty()
	{
		return outsourceCheckQty;
	}

	public void setOutsourceCheckQty(Integer outsourceCheckQty)
	{
		this.outsourceCheckQty = outsourceCheckQty;
	}

	public Integer getReceiveCheckQty()
	{
		return receiveCheckQty;
	}

	public void setReceiveCheckQty(Integer receiveCheckQty)
	{
		this.receiveCheckQty = receiveCheckQty;
	}

	public Integer getPaymentCheckQty()
	{
		return paymentCheckQty;
	}

	public void setPaymentCheckQty(Integer paymentCheckQty)
	{
		this.paymentCheckQty = paymentCheckQty;
	}

	public Integer getPaymentWriteCheckQty()
	{
		return paymentWriteCheckQty;
	}

	public void setPaymentWriteCheckQty(Integer paymentWriteCheckQty)
	{
		this.paymentWriteCheckQty = paymentWriteCheckQty;
	}

	public Integer getReceiveWriteCheckQty()
	{
		return receiveWriteCheckQty;
	}

	public void setReceiveWriteCheckQty(Integer receiveWriteCheckQty)
	{
		this.receiveWriteCheckQty = receiveWriteCheckQty;
	}

	public HomePageCheckVo(Integer saleCheckQty, Integer purchCheckQty, Integer outsourceCheckQty, Integer receiveCheckQty, Integer paymentCheckQty, Integer paymentWriteCheckQty, Integer receiveWriteCheckQty)
	{
		super();
		this.saleCheckQty = saleCheckQty;
		this.purchCheckQty = purchCheckQty;
		this.outsourceCheckQty = outsourceCheckQty;
		this.receiveCheckQty = receiveCheckQty;
		this.paymentCheckQty = paymentCheckQty;
		this.paymentWriteCheckQty = paymentWriteCheckQty;
		this.receiveWriteCheckQty = receiveWriteCheckQty;
	}

	public HomePageCheckVo()
	{
		super();
	}

}
