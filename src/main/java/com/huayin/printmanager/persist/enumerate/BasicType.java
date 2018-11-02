/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

import com.huayin.common.persist.entity.AbstractTableIdEntity;
import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.basic.Account;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.CustomerClass;
import com.huayin.printmanager.persist.entity.basic.DeliveryClass;
import com.huayin.printmanager.persist.entity.basic.Department;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.ExchangeRate;
import com.huayin.printmanager.persist.entity.basic.Machine;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.persist.entity.basic.PaymentClass;
import com.huayin.printmanager.persist.entity.basic.Position;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.ProcedureClass;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.ProductClass;
import com.huayin.printmanager.persist.entity.basic.Product_Customer;
import com.huayin.printmanager.persist.entity.basic.SettlementClass;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.SupplierClass;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.basic.UnitConvert;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.begin.AccountBeginDetail;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.begin.MaterialBegin;
import com.huayin.printmanager.persist.entity.begin.MaterialBeginDetail;
import com.huayin.printmanager.persist.entity.begin.ProductBegin;
import com.huayin.printmanager.persist.entity.begin.ProductBeginDetail;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherReceive;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.oem.OemOrder;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.entity.oem.OemReconcil;
import com.huayin.printmanager.persist.entity.offer.OfferFormula;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArrive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturn;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.produce.WorkPart;
import com.huayin.printmanager.persist.entity.produce.WorkPart2Product;
import com.huayin.printmanager.persist.entity.produce.WorkProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.produce.WorkReport;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefund;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStock;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.entity.sale.SaleDeliver;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderMaterial;
import com.huayin.printmanager.persist.entity.sale.SaleOrderPart2Product;
import com.huayin.printmanager.persist.entity.sale.SaleOrderProcedure;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReturn;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.entity.stock.*;
import com.huayin.printmanager.persist.entity.sys.User;

/**
 * <pre>
 * 框架 - 基础设置缓存枚举
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
public enum BasicType
{
	CUSTOMER("company_customer","客户信息",Customer.class, false, "KH","customerId",SaleOrder.class,WorkProduct.class,CustomerBeginDetail.class,Product_Customer.class,FinanceReceive.class,OemOrder.class),
	
	SUPPLIER("company_supplier","供应商信息",Supplier.class,false, "SC","supplierId",PurchOrder.class,OutSourceProcess.class,SupplierBeginDetail.class,FinancePayment.class),
	
	PRODUCT("company_product","产品信息",Product.class,false, "CP","productId",SaleOrderDetail.class,SaleOrderPart2Product.class, WorkProduct.class,WorkPart2Product.class,ProductBeginDetail.class,StockProductOtherInDetail.class),
	
	MATERIAL("company_material","材料信息",Material.class,false, "CL","materialId",SaleOrderMaterial.class,PurchOrderDetail.class,WorkMaterial.class,MaterialBeginDetail.class,StockMaterialOtherInDetail.class,StockMaterialOtherOutDetail.class,
					StockMaterialReturnDetail.class,StockMaterialAdjustDetail.class,StockMaterialInventoryDetail.class,StockMaterialTransferDetail.class,StockMaterialSplitDetail.class,StockMaterialInventoryDetail.class,
          StockMaterialTakeDetail.class, StockMaterialSupplementDetail.class),
	
	PROCEDURE("company_procedure","工序信息",Procedure.class,false, "GX","procedureId",WorkProcedure.class,SaleOrderProcedure.class),
	
	MACHINE("company_machine","机台信息",Machine.class,false, "JT","machineId",WorkPart.class),
	
	OFFERFORMULA("company_formula","报价公式",OfferFormula.class,false, "OF","formulaId",Procedure.class),

	DEPARTMENT("company_department","部门",Department.class,false, "BM","departmentId",Employee.class),
	
	POSITION("company_position","岗位",Position.class,false, "ZW","positionId",Employee.class),
	
	EMPLOYEE("company_employee","员工",Employee.class,true, "YG","employeeId",User.class,Customer.class,Supplier.class,WorkReport.class,StockMaterialTake.class,StockMaterialReturn.class,StockMaterialOtherIn.class,
						StockMaterialOtherOut.class,StockMaterialAdjust.class,StockMaterialInventory.class,StockMaterialSupplement.class,StockMaterialTransfer.class,StockProductIn.class,StockProductOtherIn.class,
						StockProductOtherOut.class,StockProductAdjust.class,StockProductInventory.class,StockProductTransfer.class,FinanceOtherPayment.class,FinanceOtherReceive.class,FinancePayment.class,FinanceReceive.class,
						OemOrder.class),
	
	TAXRATE("company_taxrate","税率",TaxRate.class,true, "SL","taxRateId",Customer.class,Supplier.class,SaleOrderDetail.class,PurchOrderDetail.class,SaleDeliverDetail.class,PurchStockDetail.class,PurchReconcilDetail.class,
						OutSourceProcessDetail.class,OutSourceArriveDetail.class,OutSourceReconcilDetail.class,OemOrderDetail.class),
	
	ACCOUNT("company_account","账户",Account.class,true, "","accountId",FinancePayment.class,FinanceReceive.class,AccountBeginDetail.class,FinanceOtherPayment.class,FinanceOtherReceive.class),
	
	PAYMENTCLASS("company_paymentclass","付款方式",PaymentClass.class,true, "FK","paymentClassId",Customer.class,Supplier.class,SaleOrder.class,PurchOrder.class,SaleDeliver.class,SaleReconcil.class,SaleReturn.class,
						PurchStock.class,PurchRefund.class,PurchReconcil.class,OemOrder.class),
	
	SETTLEMENTCLASS("company_settlementclass","结算方式",SettlementClass.class,true, "","settlementClassId",Supplier.class,Customer.class,SaleReconcil.class,PurchStock.class,PurchOrder.class,PurchRefund.class,PurchReconcil.class,
						OutSourceArrive.class,OutSourceReturn.class,OutSourceReconcil.class,OemReconcil.class),
	
	CUSTOMERCLASS("company_customerclass","客户分类",CustomerClass.class,false, "","customerClassId",Customer.class),
	
	SUPPLIERCLASS("company_supplierclass","供应商分类",SupplierClass.class,false, "","supplierClassId",Supplier.class),
	
	PRODUCTCLASS("company_productclass","产品分类",ProductClass.class,false, "","productClassId",Product.class),
	
	MATERIALCLASS("company_materialclass","材料分类",MaterialClass.class,false, "","materialClassId",Material.class),
	
	PROCEDURECLASS("company_procedureclass","工序分类",ProcedureClass.class,false, "","procedureClassId",Procedure.class),
	
	UNIT("company_unit","单位",Unit.class,false, "","unitId",Product.class,Material.class,UnitConvert.class),
	
	UNITCONVERT("company_unitconvert","单位换算",UnitConvert.class,false, "","unitconvertId"),
	
	DELIVERYCLASS("company_deliveryclass","送货方式",DeliveryClass.class, true, "","deliveryClassId",Customer.class,Supplier.class,SaleOrder.class,SaleDeliver.class,SaleReturn.class,PurchOrder.class,PurchStock.class,
						OutSourceArrive.class,OutSourceProcess.class,OutSourceReturn.class),
	
	WAREHOUSE("company_warehouse","仓库信息",Warehouse.class,true, "","warehouseId",StockProductIn.class,StockProductOtherIn.class,ProductBegin.class,PurchStockDetail.class,StockMaterialOtherIn.class,MaterialBegin.class, 
						SaleDeliverDetail.class,PurchRefundDetail.class,StockMaterialReturn.class,StockMaterialSplit.class,StockMaterialSplitDetail.class,StockMaterialTransfer.class,SaleReturnDetail.class,OutSourceArriveDetail.class,
						OutSourceReturnDetail.class,StockProductTransfer.class),
	
	EXCHANGERATE("company_exchangerate","汇率",ExchangeRate.class,false, "","exchangeRateId",SaleOrderDetail.class,PurchOrderDetail.class,OutSourceProcessDetail.class);

	/**
	 * 缓存名称
	 */
	private String cacheName;

	/**
	 * 描述
	 */
	private String description;
	/**
	 * 类名
	 */
	private Class<? extends BaseBasicTableEntity> cla;
	
	/**
	 * 是否显示自定义
	 */
	private boolean isQuickCreate = false;
	
	/**
	 * 基础代码前缀
	 */
	private String codePrefix;
	
	/**
	 *被引用的字段名称 
	 */
	private String refrenceColumnName;
	
	/**
	 * 引用表名,删除数据时判断是否被引用
	 */
	private Class<? extends AbstractTableIdEntity>[] refrenceClaArray;
	
	@SafeVarargs
	BasicType(String cacheName, String description,Class<? extends BaseBasicTableEntity> cla, boolean isQuickCreate, String codePrefix,String refrenceColumnName,Class<? extends AbstractTableIdEntity>... refrenceClaArray)
	{
		this.cacheName = cacheName;
		this.description = description;
		this.cla=cla;
		this.isQuickCreate = isQuickCreate;
		this.codePrefix=codePrefix;
		this.refrenceColumnName=refrenceColumnName;
		this.refrenceClaArray=refrenceClaArray;
	}

	public String getCacheName()
	{
		return cacheName;
	}

	public void setCacheName(String cacheName)
	{
		this.cacheName = cacheName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Class<? extends BaseBasicTableEntity> getCla()
	{
		return cla;
	}

	public void setCla(Class<? extends BaseBasicTableEntity> cla)
	{
		this.cla = cla;
	}

	public boolean isQuickCreate()
	{
		return isQuickCreate;
	}

	public void setQuickCreate(boolean isQuickCreate)
	{
		this.isQuickCreate = isQuickCreate;
	}

	public String getCodePrefix()
	{
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix)
	{
		this.codePrefix = codePrefix;
	}

	public Class<? extends AbstractTableIdEntity>[] getRefrenceClaArray()
	{
		return refrenceClaArray;
	}

	public void setRefrenceClaArray(Class<? extends AbstractTableIdEntity>[] refrenceClaArray)
	{
		this.refrenceClaArray = refrenceClaArray;
	}

	public String getRefrenceColumnName()
	{
		return refrenceColumnName;
	}

	public void setRefrenceColumnName(String refrenceColumnName)
	{
		this.refrenceColumnName = refrenceColumnName;
	}
}