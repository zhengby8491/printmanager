/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年2月5日 下午1:48:33
 * Copyright: 	Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.begin.MaterialBeginDetail;
import com.huayin.printmanager.persist.entity.begin.ProductBeginDetail;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentAdvanceLog;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveAdvanceLog;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceiveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArrive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturn;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.produce.WorkPart2Product;
import com.huayin.printmanager.persist.entity.produce.WorkProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.produce.WorkReportTask;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefund;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStock;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderMaterial;
import com.huayin.printmanager.persist.entity.sale.SaleOrderPart2Product;
import com.huayin.printmanager.persist.entity.sale.SaleOrderProcedure;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialAdjustDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialInventoryDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherInDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherOutDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSplit;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSplitDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplementDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTakeDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTransferDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductAdjustDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductInDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductInventoryDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherInDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherOutDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductTransferDetail;

/**
 * <pre>
 * 更新客户名表单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月5日下午1:48:33
 */
@SuppressWarnings("unchecked")
public enum UpdateNameType
{
	
	/**
	 * 变更客户名称：销售订单表，销售送货表，销售退货表，销售对账表只保存客户id，故不用更新
	 */
	CUSTOMER("company_customer", "customerId", "customerName", Customer.class, WorkReportTask.class, WorkProduct.class,FinanceReceive.class, FinanceWriteoffReceive.class, CustomerBeginDetail.class, StockProductInDetail.class,FinanceReceiveAdvanceLog.class),
	
	/**
	 * 变更供应商名称
	 */
	SUPPLIER("company_supplier", "supplierId", "supplierName", Supplier.class, PurchOrder.class, PurchStock.class, PurchRefund.class, PurchReconcil.class, FinancePayment.class, FinanceWriteoffPayment.class, SupplierBeginDetail.class,
		  OutSourceProcess.class, OutSourceArrive.class, OutSourceReturn.class, OutSourceReconcil.class, FinancePaymentAdvanceLog.class),
	
	/**
	 * 变更材料名称
	 */
	MATERIAL("company_material", "materialId", "materialName", Material.class,PurchOrderDetail.class, PurchStockDetail.class, PurchRefundDetail.class, PurchReconcilDetail.class, FinancePaymentDetail.class, FinanceWriteoffPaymentDetail.class, 
			MaterialBeginDetail.class, WorkMaterial.class, StockMaterialTakeDetail.class, StockMaterialSupplementDetail.class, StockMaterialReturnDetail.class,StockMaterialLog.class,SaleOrderMaterial.class,
			StockMaterialSplit.class, StockMaterialSplitDetail.class,StockMaterialOtherInDetail.class, StockMaterialOtherOutDetail.class,StockMaterialInventoryDetail.class,StockMaterialTransferDetail.class, 
			StockMaterialAdjustDetail.class ),
	
	/**
	 * 变更工序名称
	 */
	PROCEDURE("company_procedure", "procedureId", "procedureName", Procedure.class, WorkProcedure.class, WorkReportTask.class, OutSourceProcessDetail.class, OutSourceArriveDetail.class, OutSourceReturnDetail.class, OutSourceReconcilDetail.class,
			FinancePaymentDetail.class, FinanceWriteoffPaymentDetail.class,SaleOrderProcedure.class),
	
	/**
	 * 变更产品规格名称
	 */
	PRODUCT("company_product", "productId", "productName", Product.class, SaleOrderDetail.class, SaleDeliverDetail.class, SaleReturnDetail.class, SaleReconcilDetail.class, FinanceReceiveDetail.class, FinanceWriteoffReceiveDetail.class, WorkProduct.class, 
			WorkPart2Product.class, WorkReportTask.class, OutSourceProcessDetail.class, OutSourceArriveDetail.class, OutSourceReturnDetail.class, OutSourceReconcilDetail.class, FinancePaymentDetail.class, StockProductLog.class,SaleOrderPart2Product.class,
			FinanceWriteoffPaymentDetail.class, ProductBeginDetail.class, StockProductInDetail.class, StockProductOtherInDetail.class, StockProductOtherOutDetail.class, StockProductTransferDetail.class, StockProductInventoryDetail.class,
			StockProductAdjustDetail.class, StockMaterialTakeDetail.class),
	;
	/**
	 * 缓存名字
	 */
	private String cacheName;
	
	/**
	 * 查询字段
	 */
	private String refrenceColumnName;
	
	/**
	 * 需变更内容的字段
	 */
	private String replaceColunmName;
	
	/**
	 * 基础类
	 */
	private Class<? extends BaseBasicTableEntity> basicCla;

	/**
	 * 引用类
	 */
	private Class<? extends BaseTableIdEntity>[] refrenceClaArray;

	UpdateNameType(String cacheName, String refrenceColumnName, String replaceColunmName, Class<? extends BaseBasicTableEntity> basicCla, Class<? extends BaseTableIdEntity>... refrenceClaArray)
	{
		this.cacheName = cacheName;
		this.refrenceColumnName = refrenceColumnName;
		this.replaceColunmName = replaceColunmName;
		this.basicCla = basicCla;
		this.refrenceClaArray = refrenceClaArray;
	}

	public String getCacheName()
	{
		return cacheName;
	}

	public void setCacheName(String cacheName)
	{
		this.cacheName = cacheName;
	}

	public String getRefrenceColumnName()
	{
		return refrenceColumnName;
	}

	public void setRefrenceColumnName(String refrenceColumnName)
	{
		this.refrenceColumnName = refrenceColumnName;
	}

	public String getReplaceColunmName()
	{
		return replaceColunmName;
	}

	public void setReplaceColunmName(String replaceColunmName)
	{
		this.replaceColunmName = replaceColunmName;
	}

	public Class<? extends BaseBasicTableEntity> getBasicCla()
	{
		return basicCla;
	}

	public void setBasicCla(Class<? extends BaseBasicTableEntity> basicCla)
	{
		this.basicCla = basicCla;
	}

	public Class<? extends BaseTableIdEntity>[] getRefrenceClaArray()
	{
		return refrenceClaArray;
	}

	public void setRefrenceClaArray(Class<? extends BaseTableIdEntity>[] refrenceClaArray)
	{
		this.refrenceClaArray = refrenceClaArray;
	}

}
