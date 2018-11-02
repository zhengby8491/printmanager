/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月17日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service;

import org.springframework.context.ApplicationContext;

import com.huayin.printmanager.exterior.service.ExteriorPurchService;
import com.huayin.printmanager.exterior.service.ExteriorRegisterService;
import com.huayin.printmanager.exterior.service.ExteriorService;
import com.huayin.printmanager.job.SystemJobService;
import com.huayin.printmanager.persist.dao.DaoFactory;
import com.huayin.printmanager.service.basic.AccountService;
import com.huayin.printmanager.service.basic.CustomerClassService;
import com.huayin.printmanager.service.basic.CustomerService;
import com.huayin.printmanager.service.basic.DeliveryClassService;
import com.huayin.printmanager.service.basic.DepartmentService;
import com.huayin.printmanager.service.basic.EmployeeService;
import com.huayin.printmanager.service.basic.MachineService;
import com.huayin.printmanager.service.basic.MaterialClassService;
import com.huayin.printmanager.service.basic.MaterialService;
import com.huayin.printmanager.service.basic.PaymentClassService;
import com.huayin.printmanager.service.basic.PositionService;
import com.huayin.printmanager.service.basic.ProcedureClassService;
import com.huayin.printmanager.service.basic.ProcedureService;
import com.huayin.printmanager.service.basic.ProductClassService;
import com.huayin.printmanager.service.basic.ProductService;
import com.huayin.printmanager.service.basic.SettlementClassService;
import com.huayin.printmanager.service.basic.SupplierClassService;
import com.huayin.printmanager.service.basic.SupplierService;
import com.huayin.printmanager.service.basic.TaxRateService;
import com.huayin.printmanager.service.basic.UnitConvertService;
import com.huayin.printmanager.service.basic.UnitService;
import com.huayin.printmanager.service.basic.WarehouseService;
import com.huayin.printmanager.service.begin.AccountBeginService;
import com.huayin.printmanager.service.begin.CustomerBeginService;
import com.huayin.printmanager.service.begin.MaterialBeginService;
import com.huayin.printmanager.service.begin.ProductBeginService;
import com.huayin.printmanager.service.begin.SupplierBeginService;
import com.huayin.printmanager.service.finance.FinanceAccountLogService;
import com.huayin.printmanager.service.finance.FinanceAdjustService;
import com.huayin.printmanager.service.finance.FinanceOtherPaymentService;
import com.huayin.printmanager.service.finance.FinanceOtherReceiveService;
import com.huayin.printmanager.service.finance.FinancePaymentAdvanceLogService;
import com.huayin.printmanager.service.finance.FinancePaymentService;
import com.huayin.printmanager.service.finance.FinanceReceiveAdvanceLogService;
import com.huayin.printmanager.service.finance.FinanceReceiveService;
import com.huayin.printmanager.service.finance.FinanceSumService;
import com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService;
import com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService;
import com.huayin.printmanager.service.lang.MergeSum;
import com.huayin.printmanager.service.oem.OemDeliverService;
import com.huayin.printmanager.service.oem.OemOrderService;
import com.huayin.printmanager.service.oem.OemReconcilService;
import com.huayin.printmanager.service.oem.OemReturnService;
import com.huayin.printmanager.service.oem.OemTransmitService;
import com.huayin.printmanager.service.oem.OemZeroService;
import com.huayin.printmanager.service.offer.OfferAutoService;
import com.huayin.printmanager.service.offer.OfferOrderService;
import com.huayin.printmanager.service.offer.OfferSettingService;
import com.huayin.printmanager.service.outsource.OutSourceArriveService;
import com.huayin.printmanager.service.outsource.OutSourceProcessService;
import com.huayin.printmanager.service.outsource.OutSourceReconcilService;
import com.huayin.printmanager.service.outsource.OutSourceReturnService;
import com.huayin.printmanager.service.pay.PayService;
import com.huayin.printmanager.service.produce.WorkService;
import com.huayin.printmanager.service.purch.PurchOrderService;
import com.huayin.printmanager.service.purch.PurchReconcilService;
import com.huayin.printmanager.service.purch.PurchReturnService;
import com.huayin.printmanager.service.purch.PurchStockService;
import com.huayin.printmanager.service.purch.PurchSumService;
import com.huayin.printmanager.service.sale.SaleDeliverService;
import com.huayin.printmanager.service.sale.SaleOrderService;
import com.huayin.printmanager.service.sale.SaleReconcilService;
import com.huayin.printmanager.service.sale.SaleReturnService;
import com.huayin.printmanager.service.sale.SaleTransmitService;
import com.huayin.printmanager.service.stock.StockMaterialAdjustService;
import com.huayin.printmanager.service.stock.StockMaterialInventoryService;
import com.huayin.printmanager.service.stock.StockMaterialOtherInService;
import com.huayin.printmanager.service.stock.StockMaterialOtherOutService;
import com.huayin.printmanager.service.stock.StockMaterialReturnService;
import com.huayin.printmanager.service.stock.StockMaterialService;
import com.huayin.printmanager.service.stock.StockMaterialSplitService;
import com.huayin.printmanager.service.stock.StockMaterialSupplementService;
import com.huayin.printmanager.service.stock.StockMaterialTakeService;
import com.huayin.printmanager.service.stock.StockMaterialTransferService;
import com.huayin.printmanager.service.stock.StockProductAdjustService;
import com.huayin.printmanager.service.stock.StockProductInService;
import com.huayin.printmanager.service.stock.StockProductInventoryService;
import com.huayin.printmanager.service.stock.StockProductOtherInService;
import com.huayin.printmanager.service.stock.StockProductOtherOutService;
import com.huayin.printmanager.service.stock.StockProductService;
import com.huayin.printmanager.service.stock.StockProductTransferService;
import com.huayin.printmanager.service.stock.StockService;
import com.huayin.printmanager.service.sys.AdvertisementService;
import com.huayin.printmanager.service.sys.AgentQuotientService;
import com.huayin.printmanager.service.sys.BuyService;
import com.huayin.printmanager.service.sys.CommentService;
import com.huayin.printmanager.service.sys.CompanyService;
import com.huayin.printmanager.service.sys.MenuService;
import com.huayin.printmanager.service.sys.RoleService;
import com.huayin.printmanager.service.sys.SequenceService;
import com.huayin.printmanager.service.sys.SmsLogService;
import com.huayin.printmanager.service.sys.SmsPartnerService;
import com.huayin.printmanager.service.sys.SmsPortalService;
import com.huayin.printmanager.service.sys.SystemConfigService;
import com.huayin.printmanager.service.sys.SystemLogService;
import com.huayin.printmanager.service.sys.SystemNoticeService;
import com.huayin.printmanager.service.sys.SystemVersionNoticeService;
import com.huayin.printmanager.service.sys.UserService;
import com.huayin.printmanager.service.sys.UserShareService;
import com.huayin.printmanager.service.workbench.WorkbenchMemorandumService;
import com.huayin.printmanager.service.workbench.WorkbenchNoticeService;
import com.huayin.printmanager.service.workbench.WorkbenchOftenService;
import com.huayin.printmanager.sms.SmsSendService;
import com.huayin.printmanager.wx.service.WXBasicService;
import com.huayin.printmanager.wx.service.WXCheckService;
import com.huayin.printmanager.wx.service.WXEmployeeSerice;
import com.huayin.printmanager.wx.service.WXPayService;
import com.huayin.printmanager.wx.service.WXScheduleService;
import com.huayin.printmanager.wx.service.WXStatisticsService;
import com.huayin.printmanager.wx.service.WXSumService;
import com.huayin.printmanager.wx.service.WXWarnService;

/**
 * <pre>
 * 框架 - 业务服务接口工厂实现
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月11日
 */
public interface ServiceFactory
{
	public ApplicationContext getApplicationContext();

	public DaoFactory getDaoFactory();

	// ==================== 框架 ====================
	
	/**
	 * <pre>
	 * 框架 - 合并汇总数据
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月24日 上午9:37:21, think
	 */
	public MergeSum getMergeSum();
	
	public CommonService getCommonService();

	public PersistService getPersistService();
	
	/**
	 * <pre>
	 * 框架 - 功能业务常用功能
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月16日 下午2:31:31, think
	 */
	public ZeroService getZeroService();
	
	/**
	 * <pre>
	 * 框架 - 快捷选择
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月16日 下午2:06:22, think
	 */
	public QuickService getQuickService();

	/**
	 * <pre>
	 * 框架 - 自定义模板
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:53:40, think
	 */
	public TemplateService getTemplateService();

	public WorkDeskService getWorkDeskService();

	public SystemJobService getSystemJobService();

	public UserShareService getUserShareService();

	// ==================== 平台 ====================

	public SmsSendService getSmsSendService();

	// ==================== 系统管理 ====================

	public AdvertisementService getAdvertisementService();

	public AgentQuotientService getAgentQuotientService();

	public BuyService getBuyService();

	public CommentService getCommentService();

	public CompanyService getCompanyService();

	public MenuService getMenuService();

	public RoleService getRoleService();

	public SequenceService getSequenceService();

	public SmsLogService getSmsLogService();

	public SmsPartnerService getSmsPartnerService();

	public SmsPortalService getSmsPortalService();

	public SystemConfigService getSystemConfigService();

	public SystemLogService getSystemLogService();

	public SystemNoticeService getSystemNoticeService();

	public SystemVersionNoticeService getSystemVersionNoticeService();

	public UserService getUserService();

	// ==================== 基础设置 ====================

	public AccountService getAccountService();

	public CustomerClassService getCustomerClassService();

	public CustomerService getCustomerService();

	public DeliveryClassService getDeliveryClassService();

	public DepartmentService getDepartmentService();

	public EmployeeService getEmployeeService();

	public MachineService getMachineService();

	public MaterialClassService getMaterialClassService();

	public MaterialService getMaterialService();

	public PaymentClassService getPaymentClassService();

	public PositionService getPositionService();

	public ProcedureClassService getProcedureClassService();

	public ProcedureService getProcedureService();

	public ProductClassService getProductClassService();

	public ProductService getProductService();

	public SettlementClassService getSettlementClassService();

	public SupplierClassService getSupplierClassService();

	public SupplierService getSupplierService();

	public TaxRateService getTaxRateService();

	public UnitService getUnitService();

	public UnitConvertService getUnitConvertService();

	public WarehouseService getWarehouseService();

	// ==================== 基础设置 - 期初 ====================

	public AccountBeginService getAccountBeginService();

	public CustomerBeginService getCustomerBeginService();

	public MaterialBeginService getMaterialBeginService();

	public ProductBeginService getProductBeginService();

	public SupplierBeginService getSupplierBeginService();

	// ==================== 财务管理 ====================

	public FinanceAccountLogService getAccountLogService();

	public FinanceOtherPaymentService getOtherPaymentService();

	public FinanceOtherReceiveService getOtherReceiveService();

	public FinancePaymentService getPaymentService();

	public FinanceReceiveService getReceiveService();

	public FinanceSumService getSumService();

	public FinanceWriteoffPaymentService getWriteoffPaymentService();

	public FinanceWriteoffReceiveService getWriteoffReceiveService();

	public FinanceReceiveAdvanceLogService getReceiveAdvanceLogService();

	public FinancePaymentAdvanceLogService getPaymentAdvanceLogService();

	public FinanceAdjustService getFinanceAdjustService();
	// ==================== 经理查询 ====================

	// ==================== 报价系统 ======================

	public OfferOrderService getOfferOrderService();

	public OfferSettingService getOfferSettingService();

	public OfferAutoService getOfferAutoService();

	// ==================== 发外管理 ======================

	public OutSourceArriveService getOutSourceArriveService();

	public OutSourceProcessService getOutSourceProcessService();

	public OutSourceReconcilService getOutSourceReconcilService();

	public OutSourceReturnService getOutSourceReturnService();
	
  // ==================== 代工管理 ======================
	 
	public OemOrderService getOemOrderService();
	
  public OemDeliverService getOemDeliverService();
  
  public OemReturnService getOemReturnService();
  
  public OemReconcilService getOemReconcilService();

  public OemTransmitService getOemTransmitService();
  
  /**
   * 代工平台 - 对外公共业务接口
   */
  public OemZeroService getOemZeroService();
	

	// ==================== 支付管理 ======================
  public PayService getPayService();

	// ==================== 生产管理 ======================

	public WorkService getWorkService();

	// ==================== 采购管理 ======================

	public PurchOrderService getPurOrderService();

	public PurchReconcilService getPurReconcilService();

	public PurchReturnService getPurReturnService();

	public PurchStockService getPurStockService();

	public PurchSumService getPurchSumService();

	// ==================== 销售管理 ======================

	public SaleDeliverService getSaleDeliverService();

	public SaleOrderService getSaleOrderService();

	public SaleReconcilService getSaleReconcilService();

	public SaleReturnService getSaleReturnService();
	
	public SaleTransmitService getSaleTransmitService();
	// ==================== 库存管理 ======================

	public StockMaterialAdjustService getStockMaterialAdjustService();

	public StockMaterialService getMaterialStockService();

	public StockMaterialInventoryService getStockMaterialInventoryService();

	public StockMaterialOtherInService getStockMaterialOtherInService();

	public StockMaterialOtherOutService getStockMaterialOtherOutService();

	public StockMaterialReturnService getStockMaterialReturnService();

	public StockMaterialSplitService getStockMaterialSplitService();

	public StockMaterialSupplementService getStockMaterialSupplementService();

	public StockMaterialTakeService getStockMaterialTakeService();

	public StockMaterialTransferService getStockMaterialTransferService();

	public StockProductAdjustService getStockProductAdjustService();

	public StockProductService getStockProductService();

	public StockProductInService getStockProductInService();

	public StockProductInventoryService getStockProductInventoryService();

	public StockProductOtherInService getStockProductOtherInService();

	public StockProductOtherOutService getStockProductOtherOutService();

	public StockProductTransferService getStockProductTransferService();

	public StockService getStockService();

	// ==================== 工作台 ======================

	public WorkbenchMemorandumService getWorkbenchMemorandumService();

	public WorkbenchNoticeService getWorkbenchNoticeService();

	public WorkbenchOftenService getWorkbenchOftenService();

	// ==================== 微信 ======================

	public WXPayService getWXPayService();

	public WXCheckService getWXCheckService();

	public WXScheduleService getWXScheduleService();

	public WXWarnService getWXWarnService();

	public WXSumService getWXSumService();

	public WXStatisticsService getWXStatisticsService();

	public WXBasicService getWXBasicService();

	public WXEmployeeSerice getWXEmployeeSerice();

	// ====================== 印刷家交互接口  ===============
	public ExteriorService getExteriorService();
	
	public ExteriorRegisterService getExteriorRegisterService();

	public ExteriorPurchService getExteriorPurchService();
}
