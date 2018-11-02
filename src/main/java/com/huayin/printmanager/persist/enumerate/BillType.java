package com.huayin.printmanager.persist.enumerate;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.entity.begin.AccountBegin;
import com.huayin.printmanager.persist.entity.begin.AccountBeginDetail;
import com.huayin.printmanager.persist.entity.begin.CustomerBegin;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.begin.MaterialBegin;
import com.huayin.printmanager.persist.entity.begin.MaterialBeginDetail;
import com.huayin.printmanager.persist.entity.begin.ProductBegin;
import com.huayin.printmanager.persist.entity.begin.ProductBeginDetail;
import com.huayin.printmanager.persist.entity.begin.SupplierBegin;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjust;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjustDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherPaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherReceiveDetail;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceiveDetail;
import com.huayin.printmanager.persist.entity.oem.OemDeliver;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.entity.oem.OemOrder;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.entity.oem.OemReconcil;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;
import com.huayin.printmanager.persist.entity.oem.OemReturn;
import com.huayin.printmanager.persist.entity.oem.OemReturnDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArrive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturn;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.produce.WorkReport;
import com.huayin.printmanager.persist.entity.produce.WorkReportDetail;
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
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturn;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialAdjust;
import com.huayin.printmanager.persist.entity.stock.StockMaterialAdjustDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialInventory;
import com.huayin.printmanager.persist.entity.stock.StockMaterialInventoryDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherIn;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherInDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherOut;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherOutDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturn;
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSplit;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSplitDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplement;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplementDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTake;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTakeDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTransfer;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTransferDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductAdjust;
import com.huayin.printmanager.persist.entity.stock.StockProductAdjustDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductIn;
import com.huayin.printmanager.persist.entity.stock.StockProductInDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductInventory;
import com.huayin.printmanager.persist.entity.stock.StockProductInventoryDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherIn;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherInDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherOut;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherOutDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductTransfer;
import com.huayin.printmanager.persist.entity.stock.StockProductTransferDetail;

@SuppressWarnings("unchecked")
public enum BillType
{
	// 采购流程单据
	PURCH_PO("PO", "采购订单",PurchOrder.class,PurchOrderDetail.class,PurchStockDetail.class),
	PURCH_PN("PN", "采购入库",PurchStock.class,PurchStockDetail.class,PurchRefundDetail.class,PurchReconcilDetail.class),
	PURCH_PR("PR", "采购退货",PurchRefund.class,PurchRefundDetail.class,PurchReconcilDetail.class),
	PURCH_PK("PK", "采购对账",PurchReconcil.class,PurchReconcilDetail.class,FinancePaymentDetail.class,FinanceWriteoffPaymentDetail.class),

	// 销售流程单据
	SALE_SO("SO", "销售订单",SaleOrder.class,SaleOrderDetail.class,SaleDeliverDetail.class,WorkProduct.class),
	SALE_IV("IV", "送货单",SaleDeliver.class,SaleDeliverDetail.class,SaleReturnDetail.class,SaleReconcilDetail.class),
	SALE_IR("IR", "退货单",SaleReturn.class,SaleReturnDetail.class,SaleReconcilDetail.class),
	SALE_SK("SK", "销售对账",SaleReconcil.class,SaleReconcilDetail.class,FinanceReceiveDetail.class,FinanceWriteoffReceiveDetail.class),

	// 库存流程单据

	STOCK_SPIN("IS", "成品入库",StockProductIn.class,StockProductInDetail.class),
	STOCK_SPOI("SPI", "成品其他入库",StockProductOtherIn.class,StockProductOtherInDetail.class),
	STOCK_SPOO("SPO", "成品其他出库",StockProductOtherOut.class,StockProductOtherOutDetail.class),
	STOCK_SPI("PI", "成品盘点",StockProductInventory.class,StockProductInventoryDetail.class),
	STOCK_SPA("PA", "成品调整",StockProductAdjust.class,StockProductAdjustDetail.class),
	STOCK_SPT("PT", "成品调拨",StockProductTransfer.class,StockProductTransferDetail.class),
	
	STOCK_SMOI("SMI", "材料其他入库",StockMaterialOtherIn.class,StockMaterialOtherInDetail.class),
	STOCK_SMOO("SMO", "材料其他出库",StockMaterialOtherOut.class,StockMaterialOtherOutDetail.class),
	STOCK_SMI("MI", "材料盘点",StockMaterialInventory.class,StockMaterialInventoryDetail.class),
	STOCK_SMA("MA", "材料调整",StockMaterialAdjust.class,StockMaterialAdjustDetail.class),
	STOCK_SMT("MT", "材料调拨",StockMaterialTransfer.class,StockMaterialTransferDetail.class),
	STOCK_SPL("CT", "材料分切",StockMaterialSplit.class,StockMaterialSplitDetail.class),

	STOCK_MR("MR", "生产领料",StockMaterialTake.class,StockMaterialTakeDetail.class),

	STOCK_SM("SM", "生产补料",StockMaterialSupplement.class,StockMaterialSupplementDetail.class),

	STOCK_RM("RM", "生产退料",StockMaterialReturn.class,StockMaterialReturnDetail.class),

	
	// 生产流程
	PRODUCE_MO("MO", "生产工单",Work.class,WorkProduct.class,OutSourceProcessDetail.class,StockMaterialTakeDetail.class,StockProductInDetail.class,
			PurchOrderDetail.class,SaleDeliverDetail.class,StockMaterialReturnDetail.class,StockMaterialSupplementDetail.class),
	
	PRODUCE_SUPPLEMENT("MO","生产补单",Work.class,WorkProduct.class,OutSourceProcessDetail.class,StockMaterialTakeDetail.class,StockProductInDetail.class,
			PurchOrderDetail.class,SaleDeliverDetail.class,StockMaterialReturnDetail.class,StockMaterialSupplementDetail.class),

	PRODUCE_TURNING("MO","生产翻单",Work.class,WorkProduct.class,OutSourceProcessDetail.class,StockMaterialTakeDetail.class,StockProductInDetail.class,
			PurchOrderDetail.class,SaleDeliverDetail.class,StockMaterialReturnDetail.class,StockMaterialSupplementDetail.class),

	PRODUCE_PROOFING("MO","打样工单",Work.class,WorkProduct.class,OutSourceProcessDetail.class,StockMaterialTakeDetail.class,StockProductInDetail.class,
			PurchOrderDetail.class,SaleDeliverDetail.class,StockMaterialReturnDetail.class,StockMaterialSupplementDetail.class),
	
	PRODUCE_REPORT("DY","工单上报",WorkReport.class,WorkReportDetail.class),

	// 财务流程单据
	FINANCE_PAY("RV", "付款单",FinancePayment.class,FinancePaymentDetail.class),
	FINANCE_WRITEOFF_PAY("WRV", "付款核销单",FinanceWriteoffPayment.class,FinanceWriteoffPaymentDetail.class),

	FINANCE_REC("RC", "收款单",FinanceReceive.class,FinanceReceiveDetail.class),
	FINANCE_WRITEOFF_RC("WRC", "收款核销单",FinanceWriteoffReceive.class,FinanceWriteoffReceiveDetail.class),
  FINANCE_OTHER_PAY("OV","其他付款单",FinanceOtherPayment.class,FinanceOtherPaymentDetail.class),
  FINANCE_OTHER_REC("ORC","其他收款单",FinanceOtherReceive.class,FinanceOtherReceiveDetail.class),	
	FINANCE_ADJUST("FA", "财务调整单", FinanceAdjust.class,FinanceAdjustDetail.class),
  // 发外加工
	OUTSOURCE_OP("OP", "发外加工单",OutSourceProcess.class,OutSourceProcessDetail.class,OutSourceArriveDetail.class),

	OUTSOURCE_OA("OA", "发外到货单",OutSourceArrive.class,OutSourceArriveDetail.class,OutSourceReturnDetail.class,OutSourceReconcilDetail.class),

	OUTSOURCE_OR("OR", "发外退货单",OutSourceReturn.class,OutSourceReturnDetail.class,OutSourceReconcilDetail.class),
	
	OUTSOURCE_OC("OC", "发外对账单",OutSourceReconcil.class,OutSourceReconcilDetail.class,FinancePaymentDetail.class,FinanceWriteoffPaymentDetail.class),

	// 代工
	OEM_EO("EO", "代工订单", OemOrder.class, OemOrderDetail.class, OemDeliverDetail.class),
	OEM_ED("ED", "代工送货", OemDeliver.class, OemDeliverDetail.class,OemReturnDetail.class,OemReconcilDetail.class ),
	OEM_ER("ER", "代工退货", OemReturn.class, OemReturnDetail.class, OemReconcilDetail.class),
	OEM_EC("EC", "代工对账", OemReconcil.class, OemReconcilDetail.class, FinanceReceiveDetail.class, FinanceWriteoffReceiveDetail.class),
	
	//期初
	BEGIN_CUSTOMER("BC","客户期初",CustomerBegin.class,CustomerBeginDetail.class,FinanceReceiveDetail.class,FinanceWriteoffReceiveDetail.class),
	BEGIN_ACCOUNT("BA","账户期初",AccountBegin.class,AccountBeginDetail.class),
	BEGIN_MATERIAL("BM","材料期初",MaterialBegin.class,MaterialBeginDetail.class),
	BEGIN_PRODUCT("BP","产品期初",ProductBegin.class,ProductBeginDetail.class),
	BEGIN_SUPPLIER("BS","供应商期初",SupplierBegin.class,SupplierBeginDetail.class,FinancePaymentDetail.class,FinanceWriteoffPaymentDetail.class);
	
	/*
	 * 单据流水号生成前缀
	 */
	private String code;

	private String text;
	/**
	 * 单据表名
	 */
	private Class<? extends BaseBillMasterTableEntity> cla;
	/**
	 * 单据明细表名
	 */
	private Class<? extends BaseBillDetailTableEntity> detailCla;
	/**
	 * 下游单据表名
	 */
	private Class<? extends BaseBillDetailTableEntity>[] refrenceClaArray;
	
	BillType(String code, String text,Class<? extends BaseBillMasterTableEntity> cla,Class<? extends BaseBillDetailTableEntity> detailCla,Class<? extends BaseBillDetailTableEntity>... refrenceClaArray)
	{
		this.code = code;
		this.text = text;
		this.cla=cla;
		this.detailCla=detailCla;
		this.refrenceClaArray=refrenceClaArray;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Class<? extends BaseBillMasterTableEntity> getCla()
	{
		return cla;
	}

	public void setCla(Class<? extends BaseBillMasterTableEntity> cla)
	{
		this.cla = cla;
	}

	public Class<? extends BaseBillDetailTableEntity>[] getRefrenceClaArray()
	{
		return refrenceClaArray;
	}

	public void setRefrenceClaArray(Class<? extends BaseBillDetailTableEntity>[] refrenceClaArray)
	{
		this.refrenceClaArray = refrenceClaArray;
	}

	public Class<? extends BaseBillDetailTableEntity> getDetailCla()
	{
		return detailCla;
	}

	public void setDetailCla(Class<? extends BaseBillDetailTableEntity> detailCla)
	{
		this.detailCla = detailCla;
	}
 
	
}
