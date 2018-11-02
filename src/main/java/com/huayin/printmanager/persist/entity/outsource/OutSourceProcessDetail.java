/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.outsource;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 发外管理 - 发外加工:发外加工明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月7日 下午17:15:38, liudong
 * @version 	   2.0, 2018年2月23日下午4:04:43, zhengby, 代码规范
 */
@Entity
@Table(name = "outsource_process_detail")
public class OutSourceProcessDetail extends OutSourceDetailBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 已到货数量
	 */
	private BigDecimal arriveQty = new BigDecimal(0);

	/**
	 * 已到货数量
	 */
	private BigDecimal arriveQty2 = new BigDecimal(0);

	/**
	 * 已到货金额
	 */
	private BigDecimal arriveMoney = new BigDecimal(0);
	
	/**
	 * 工单工序信息
	 */
	private String workProcedures;

	/**
	 * 工单材料信息
	 */
	private String workMaterials;

	/**
	 * 已对账数量
	 */
	@Transient
	private BigDecimal reconcilQty;

	/**
	 * 未到货数量
	 */
	@Transient
	private BigDecimal unarriveQty = new BigDecimal(0);

	/**
	 * 未对账数量
	 */
	@Transient
	private BigDecimal unreconcilQty;

	/**
	 * 未付款金额
	 */
	@Transient
	private BigDecimal unpayment;

	/**
	 * 已到付款
	 */
	@Transient
	private BigDecimal payment;

	/**
	 * 主表对象
	 */
	@Transient
	private OutSourceProcess master;

	/**
	 * 代工平台公司信息
	 */
	@Transient
	private Company oemCompany;

	/**
	 * 代工平台客户信息
	 */
	@Transient
	private Customer oemCustomer;

	/**
	 * 产品名称列表
	 */
	@Transient
	private List<WorkProduct> productList = Lists.newArrayList();
	
	/**
	 * 产品名称字符串
	 */
	@Transient
	private String productNames;

	public BigDecimal getArriveQty()
	{
		return arriveQty;
	}

	public void setArriveQty(BigDecimal arriveQty)
	{
		this.arriveQty = arriveQty;
	}

	public BigDecimal getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(BigDecimal reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public BigDecimal getPayment()
	{
		return payment;
	}

	public void setPayment(BigDecimal payment)
	{
		this.payment = payment;
	}

	public BigDecimal getArriveMoney()
	{
		return arriveMoney;
	}

	public void setArriveMoney(BigDecimal arriveMoney)
	{
		this.arriveMoney = arriveMoney;
	}

	public BigDecimal getArriveQty2()
	{
		return arriveQty2;
	}

	public void setArriveQty2(BigDecimal arriveQty2)
	{
		this.arriveQty2 = arriveQty2;
	}

	public BigDecimal getUnarriveQty()
	{
		return unarriveQty;
	}

	public void setUnarriveQty(BigDecimal unarriveQty)
	{
		this.unarriveQty = unarriveQty;
	}

	public BigDecimal getUnreconcilQty()
	{
		return unreconcilQty;
	}

	public void setUnreconcilQty(BigDecimal unreconcilQty)
	{
		this.unreconcilQty = unreconcilQty;
	}

	public BigDecimal getUnpayment()
	{
		return unpayment;
	}

	public void setUnpayment(BigDecimal unpayment)
	{
		this.unpayment = unpayment;
	}

	public String getWorkProcedures()
	{
		return workProcedures;
	}

	public void setWorkProcedures(String workProcedures)
	{
		this.workProcedures = workProcedures;
	}

	public String getWorkMaterials()
	{
		return workMaterials;
	}

	public void setWorkMaterials(String workMaterials)
	{
		this.workMaterials = workMaterials;
	}

	public OutSourceProcess getMaster()
	{
		return master;
	}

	public void setMaster(OutSourceProcess master)
	{
		this.master = master;
	}

	public Company getOemCompany()
	{
		return oemCompany;
	}

	public void setOemCompany(Company oemCompany)
	{
		this.oemCompany = oemCompany;
	}

	public Customer getOemCustomer()
	{
		return oemCustomer;
	}

	public void setOemCustomer(Customer oemCustomer)
	{
		this.oemCustomer = oemCustomer;
	}

	public List<WorkProduct> getProductList()
	{
		return productList;
	}

	public void setProductList(List<WorkProduct> productList)
	{
		this.productList = productList;
	}

	public void setProductNames(String productNames)
	{
		this.productNames = productNames;
	}

	public String getProductNames()
	{
		StringBuilder sb = new StringBuilder();
		if (this.productList != null && this.productList.size() > 0)
		{
			for (WorkProduct p : this.productList)
			{
				sb.append(p.getProductName()).append(",");
			}
		}
		else
		{
			sb.append("-");
		}
		return StringUtils.removeEnd(sb.toString(), ",");
	}
}
