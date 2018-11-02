/**
 * <pre>
 * Author: 		    zhengby
 * Create:	 	    2018年4月27日 下午19:30:23
 * Copyright:     Copyright (c) 2018
 * Company:		    ShenZhen HuaYin
 * @since:        1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

import com.huayin.common.persist.entity.AbstractTableIdEntity;
import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.entity.begin.AccountBeginDetail;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.begin.MaterialBeginDetail;
import com.huayin.printmanager.persist.entity.begin.ProductBeginDetail;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherReceive;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.oem.OemOrder;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialAdjustDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialInventoryDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherInDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherOutDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSplit;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplementDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTakeDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTransferDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductAdjustDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductInventoryDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherInDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherOutDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductTransferDetail;

/**
 * <pre>
 * 基础信息 - 反审核期初单据类型
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年4月28日上午10:35:34, zhengby
 */
public enum BeginBillType
{
	CUSTOMERBEGIN(CustomerBeginDetail.class, "customerId", SaleOrder.class,WorkProduct.class,OemOrder.class,FinanceReceive.class), 
	SUPPLIERBEGIN(SupplierBeginDetail.class, "supplierId", PurchOrder.class, OutSourceProcess.class,FinancePayment.class), 
	MATERIALBEGIN(MaterialBeginDetail.class, "materialId", PurchOrderDetail.class, SaleOrderMaterial.class, WorkMaterial.class, StockMaterialTakeDetail.class,
								StockMaterialSupplementDetail.class, StockMaterialReturnDetail.class, StockMaterialOtherInDetail.class, StockMaterialOtherOutDetail.class,
								StockMaterialAdjustDetail.class, StockMaterialSplit.class,StockMaterialInventoryDetail.class,StockMaterialTransferDetail.class), 
	PRODUCTBEGIN(ProductBeginDetail.class, "productId", SaleOrderDetail.class, WorkProduct.class, StockProductOtherInDetail.class, StockProductOtherOutDetail.class,
							  StockProductAdjustDetail.class, StockProductInventoryDetail.class,StockProductTransferDetail.class), 
	ACCOUNTBEGIN(AccountBeginDetail.class, "accountId", FinanceReceive.class, FinancePayment.class, FinanceOtherReceive.class, FinanceOtherPayment.class),;
	
	/**
	 * 类名
	 */
	private Class<? extends BaseBillDetailTableEntity> cla;
	
	/**
	 *被引用的字段名称 
	 */
	private String refrenceColumnName;
	
	/**
	 * 引用表名,删除数据时判断是否被引用
	 */
	private Class<? extends AbstractTableIdEntity>[] refrenceClaArray;
	
	@SafeVarargs
	BeginBillType(Class<? extends BaseBillDetailTableEntity> cla,  String refrenceColumnName, Class<? extends AbstractTableIdEntity>... refrenceClaArray)
	{
		this.cla = cla;
		this.refrenceColumnName = refrenceColumnName;
		this.refrenceClaArray = refrenceClaArray;
	}
	
	public Class<? extends BaseBillDetailTableEntity> getCla()
	{
		return cla;
	}
	public void setCla(Class<? extends BaseBillDetailTableEntity> cla)
	{
		this.cla = cla;
	}
	public String getRefrenceColumnName()
	{
		return refrenceColumnName;
	}
	public void setRefrenceColumnName(String refrenceColumnName)
	{
		this.refrenceColumnName = refrenceColumnName;
	}
	public Class<? extends AbstractTableIdEntity>[] getRefrenceClaArray()
	{
		return refrenceClaArray;
	}
	public void setRefrenceClaArray(Class<? extends AbstractTableIdEntity>[] refrenceClaArray)
	{
		this.refrenceClaArray = refrenceClaArray;
	}
	
}
