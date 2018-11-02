/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月17日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.impl;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.huayin.printmanager.exterior.service.ExteriorPurchService;
import com.huayin.printmanager.exterior.service.ExteriorRegisterService;
import com.huayin.printmanager.exterior.service.ExteriorService;
import com.huayin.printmanager.job.SystemJobService;
import com.huayin.printmanager.persist.dao.DaoFactory;
import com.huayin.printmanager.service.CommonService;
import com.huayin.printmanager.service.PersistService;
import com.huayin.printmanager.service.QuickService;
import com.huayin.printmanager.service.ServiceFactory;
import com.huayin.printmanager.service.TemplateService;
import com.huayin.printmanager.service.WorkDeskService;
import com.huayin.printmanager.service.ZeroService;
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
@Service("serviceFactory")
public class ServiceFactoryImpl implements ServiceFactory, ApplicationContextAware
{
	private ApplicationContext applicationContext;

	@Autowired
	private DaoFactory daoFactory;

	// ==================== 框架 ====================
	
	@Autowired
	private MergeSum mergeSum;

	/**
	 * 框架 - 缓存常用功能
	 */
	@Autowired
	private CommonService commonService;

	/**
	 * 框架 - 持久层服务接口
	 */
	@Autowired
	private PersistService persistService;

	/**
	 * 框架 - 公共业务常用功能
	 */
	@Autowired
	private ZeroService zeroService;

	/**
	 * 框架 - 快捷选择
	 */
	@Autowired
	private QuickService quickService;

	/**
	 * 框架 - 自定义模板
	 */
	@Autowired
	private TemplateService templateService;

	/**
	 * 框架 - 常用功能
	 */
	@Autowired
	private WorkDeskService workDeskService;

	/**
	 * 框架 - 系统任务
	 */
	@Autowired
	private SystemJobService systemJobService;

	/**
	 * 框架 - 用户快捷登录功能
	 */
	@Autowired
	private UserShareService userShareService;

	// ==================== 平台 ====================

	/**
	 * 短信平台 - 短信发送
	 */
	@Autowired
	private SmsSendService smsSendService;
	
	

	
	/**
	 * TODO 应该改为印管家系统、印管家后台系统、短信系统、支付系统（支付宝、微信、银行卡）、H5系统(微信)
	 */

	// ==================== 系统管理 ====================

	/**
	 * 系统模块 - 广告管理
	 */
	@Autowired
	private AdvertisementService advertisementService;

	/**
	 * 系统模块 - 代理商管理
	 */
	@Autowired
	private AgentQuotientService agentQuotientService;

	/**
	 * 系统模块 - 购买信息
	 */
	@Autowired
	private BuyService buyService;

	/**
	 * 系统模块 - 留言板
	 */
	@Autowired
	private CommentService commentService;

	/**
	 * 系统模块 - 公司管理
	 */
	@Autowired
	private CompanyService companyService;

	/**
	 * 系统模块 - 菜单管理
	 */
	@Autowired
	private MenuService menuService;

	/**
	 * 系统模块 - 角色管理
	 */
	@Autowired
	private RoleService roleService;

	/**
	 * 交易流水号
	 */
	@Autowired
	private SequenceService sequenceService;

	/**
	 * 系统模块 - 短信日志
	 */
	@Autowired
	private SmsLogService smsLogService;

	/**
	 * 系统模块 - 短信渠道
	 */
	@Autowired
	private SmsPartnerService smsPartnerService;

	/**
	 * 系统模块 - 短信供应商
	 */
	@Autowired
	private SmsPortalService smsPortalService;

	/**
	 * 系统模块 - 系统参数
	 */
	@Autowired
	private SystemConfigService systemConfigService;

	/**
	 * 系统模块 - 系统日志
	 */
	@Autowired
	private SystemLogService systemLogService;

	/**
	 * 系统模块 - 系统公告
	 */
	@Autowired
	private SystemNoticeService systemNoticeService;

	/**
	 * 系统模块 - 版本公告
	 */
	@Autowired
	private SystemVersionNoticeService systemVersionNoticeService;

	/**
	 * 系统模块 - 用户管理
	 */
	@Autowired
	private UserService userService;

	// ==================== 基础设置 ====================

	/**
	 * 基础设置 - 账号信息
	 */
	@Autowired
	private AccountService accountService;

	/**
	 * 基础设置 - 客户分类
	 */
	@Autowired
	private CustomerClassService customerClassService;

	/**
	 * 基础设置 - 客户信息
	 */
	@Autowired
	private CustomerService customerService;

	/**
	 * 基础设置 - 送货方式
	 */
	@Autowired
	private DeliveryClassService deliveryClassService;

	/**
	 * 基础设置  - 部门信息
	 */
	@Autowired
	private DepartmentService departmentService;

	/**
	 * 基础设置 - 员工信息
	 */
	@Autowired
	private EmployeeService employeeService;

	/**
	 * 基础设置 - 机台信息
	 */
	@Autowired
	private MachineService machineService;

	/**
	 * 基础设置 - 材料分类
	 */
	@Autowired
	private MaterialClassService materialClassService;

	/**
	 * 基础设置 - 材料信息
	 */
	@Autowired
	private MaterialService materialService;

	/**
	 * 基础模块 - 付款方式
	 */
	@Autowired
	private PaymentClassService paymentClassService;

	/**
	 * 基础设置 - 职位设置
	 */
	@Autowired
	private PositionService positionService;

	/**
	 * 基础设置 - 工序分类
	 */
	@Autowired
	private ProcedureClassService procedureClassService;

	/**
	 * 基础设置 - 工序信息
	 */
	@Autowired
	private ProcedureService procedureService;

	/**
	 * 基础设置 - 产品分类
	 */
	@Autowired
	private ProductClassService productClassService;

	/**
	 * 基础设置 - 产品信息
	 */
	@Autowired
	private ProductService productService;

	/**
	 * 基础模块 - 结算方式
	 */
	@Autowired
	private SettlementClassService settlementService;

	/**
	 * 基础设置 - 供应商类型
	 */
	@Autowired
	private SupplierClassService supplierClassService;

	/**
	 * 基础设置 - 供应商信息
	 */
	@Autowired
	private SupplierService supplierService;

	/**
	 * 基础设置 - 税率信息
	 */
	@Autowired
	private TaxRateService taxRateService;

	/**
	 * 基础设置 - 单位信息
	 */
	@Autowired
	private UnitService unitService;

	/**
	 * 基础设置 - 单位换算
	 */
	@Autowired
	private UnitConvertService unitConvertService;

	/**
	 * 基础设置 - 仓库信息
	 */
	@Autowired
	private WarehouseService warehouseService;

	// ==================== 基础设置 - 期初 ====================

	/**
	 * 基础设置 - 账户期初
	 */
	@Autowired
	private AccountBeginService accountBeginService;

	/**
	 * 基础设置 - 客户期初
	 */
	@Autowired
	private CustomerBeginService customerBeginService;

	/**
	 * 基础设置 - 材料期初
	 */
	@Autowired
	private MaterialBeginService materialBeginService;

	/**
	 * 基础设置 - 产品期初
	 */
	@Autowired
	private ProductBeginService productBeginService;

	/**
	 * 基础设置 - 供应商期初
	 */
	@Autowired
	private SupplierBeginService supplierBeginService;

	// ==================== 财务管理 ====================

	@Autowired
	private FinanceAccountLogService accountLogService;

	@Autowired
	private FinanceOtherPaymentService otherPaymentService;

	@Autowired
	private FinanceOtherReceiveService otherReceiveService;

	@Autowired
	private FinancePaymentService paymentService;

	@Autowired
	private FinanceReceiveService receiveService;

	@Autowired
	private FinanceSumService sumService;

	@Autowired
	private FinanceWriteoffPaymentService writeoffPaymentService;

	@Autowired
	private FinanceWriteoffReceiveService writeoffReceiveService;

	@Autowired
	private FinanceReceiveAdvanceLogService receiveAdvanceLogService;

	@Autowired
	private FinancePaymentAdvanceLogService paymentAdvanceLogService;
	
	@Autowired
	private FinanceAdjustService financeAdjustService;
	// ==================== 经理查询 ====================

	// ==================== 报价系统 ======================

	@Autowired
	private OfferOrderService offerOrderService;

	@Autowired
	private OfferSettingService offerSettingService;

	@Autowired
	private OfferAutoService offerAutoService;

	// ==================== 发外管理 ======================

	@Autowired
	private OutSourceArriveService outSourceArriveService;

	@Autowired
	private OutSourceProcessService outSourceProcessService;

	@Autowired
	private OutSourceReconcilService outSourceReconcilService;

	@Autowired
	private OutSourceReturnService outSourceReturnService;

	// ==================== 代工管理 ======================

	@Autowired
	private OemOrderService oemOrderService;

	@Autowired
	private OemDeliverService oemDeliverService;

	@Autowired
	private OemReturnService oemReturnService;

	@Autowired
	private OemReconcilService oemReconcilService;

	@Autowired
	private OemTransmitService oemTransmitService;

	@Autowired
	private OemZeroService oemZeroService;

	// ==================== 支付接口 ======================
	@Autowired
	private PayService payService;
	// ==================== 生产管理 ======================

	@Autowired
	private WorkService workService;

	// ==================== 采购管理 ======================
	@Autowired
	private PurchOrderService purOrderService;

	@Autowired
	private PurchReconcilService purReconcilService;

	@Autowired
	private PurchReturnService purReturnService;

	@Autowired
	private PurchStockService purStockService;

	@Autowired
	private PurchSumService purchSumService;

	// ==================== 销售管理 ======================

	@Autowired
	private SaleDeliverService saleDeliverService;

	@Autowired
	private SaleOrderService saleOrderService;

	@Autowired
	private SaleReconcilService saleReconcilService;

	@Autowired
	private SaleReturnService saleReturnService;
	
	@Autowired
	private SaleTransmitService saleTransmitService;

	// ==================== 库存管理 ======================

	@Autowired
	private StockMaterialAdjustService stockMaterialAdjustService;

	@Autowired
	private StockMaterialService materialStockService;

	@Autowired
	private StockMaterialInventoryService stockMaterialInventoryService;

	@Autowired
	private StockMaterialOtherInService stockMaterialOtherInService;

	@Autowired
	private StockMaterialOtherOutService stockMaterialOtherOutService;

	@Autowired
	private StockMaterialReturnService stockMaterialReturnService;

	@Autowired
	private StockMaterialSplitService stockMaterialSplitService;

	@Autowired
	private StockMaterialSupplementService stockMaterialSupplementService;

	@Autowired
	private StockMaterialTakeService stockMaterialTakeService;

	@Autowired
	private StockMaterialTransferService stockMaterialTransferService;

	@Autowired
	private StockProductAdjustService stockProductAdjustService;

	@Autowired
	private StockProductService stockProductService;

	@Autowired
	private StockProductInService stockProductInService;

	@Autowired
	private StockProductInventoryService stockProductInventoryService;

	@Autowired
	private StockProductOtherInService stockProductOtherInService;

	@Autowired
	private StockProductOtherOutService stockProductOtherOutService;

	@Autowired
	private StockProductTransferService stockProductTransferService;

	@Autowired
	private StockService stockService;

	// ==================== 工作台 ======================

	@Autowired
	private WorkbenchMemorandumService workbenchMemorandumService;

	@Autowired
	private WorkbenchNoticeService workbenchNoticeService;

	@Autowired
	private WorkbenchOftenService workbenchOftenService;

	// ==================== 微信 ======================

	@Autowired
	private WXPayService wXPayService;

	@Autowired
	private WXCheckService wXCheckService;

	@Autowired
	private WXScheduleService wXScheduleService;

	@Autowired
	private WXWarnService wXWarnService;

	@Autowired
	private WXSumService wXSumService;

	@Autowired
	private WXStatisticsService wXStatisticsService;

	@Autowired
	private WXBasicService wXBasicService;

	@Autowired
	private WXEmployeeSerice wxEmployeeService;
	
	//====================== 印刷家交互接口  ===============
	@Autowired
	private ExteriorService exteriorService;
	
	@Autowired
	private ExteriorRegisterService exteriorRegisterService;
	
	@Autowired
	private ExteriorPurchService exteriorPurchService;
	
	// ==================== Get And Set ====================

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}

	@Override
	public ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	@Override
	public DaoFactory getDaoFactory()
	{
		return daoFactory;
	}

	// ==================== 框架 ====================
	
	@Override
	public MergeSum getMergeSum()
	{
		return mergeSum;
	}

	@Override
	public CommonService getCommonService()
	{
		return commonService;
	}

	@Override
	public PersistService getPersistService()
	{
		return persistService;
	}

	@Override
	public ZeroService getZeroService()
	{
		return zeroService;
	}

	@Override
	public QuickService getQuickService()
	{
		return quickService;
	}

	@Override
	public TemplateService getTemplateService()
	{
		return templateService;
	}

	@Override
	public WorkDeskService getWorkDeskService()
	{
		return workDeskService;
	}

	@Override
	public SystemJobService getSystemJobService()
	{
		return systemJobService;
	}

	@Override
	public UserShareService getUserShareService()
	{
		return userShareService;
	}

	// ==================== 平台 ====================

	@Override
	public SmsSendService getSmsSendService()
	{
		return smsSendService;
	}

	// ==================== 系统管理 ====================

	@Override
	public AdvertisementService getAdvertisementService()
	{
		return advertisementService;
	}

	@Override
	public AgentQuotientService getAgentQuotientService()
	{
		return agentQuotientService;
	}

	@Override
	public BuyService getBuyService()
	{
		return buyService;
	}

	@Override
	public CommentService getCommentService()
	{
		return commentService;
	}

	public CompanyService getCompanyService()
	{
		return companyService;
	}

	@Override
	public MenuService getMenuService()
	{
		return menuService;
	}

	@Override
	public RoleService getRoleService()
	{
		return roleService;
	}

	public SequenceService getSequenceService()
	{
		return sequenceService;
	}

	public SmsLogService getSmsLogService()
	{
		return smsLogService;
	}

	public SmsPartnerService getSmsPartnerService()
	{
		return smsPartnerService;
	}

	public SmsPortalService getSmsPortalService()
	{
		return smsPortalService;
	}

	public SystemConfigService getSystemConfigService()
	{
		return systemConfigService;
	}

	@Override
	public SystemLogService getSystemLogService()
	{
		return systemLogService;
	}

	@Override
	public SystemNoticeService getSystemNoticeService()
	{
		return systemNoticeService;
	}

	@Override
	public SystemVersionNoticeService getSystemVersionNoticeService()
	{
		return systemVersionNoticeService;
	}

	@Override
	public UserService getUserService()
	{
		return userService;
	}

	// ==================== 基础设置 ====================

	@Override
	public AccountService getAccountService()
	{
		return accountService;
	}

	public CustomerClassService getCustomerClassService()
	{
		return customerClassService;
	}

	public CustomerService getCustomerService()
	{
		return customerService;
	}

	@Override
	public DeliveryClassService getDeliveryClassService()
	{
		return deliveryClassService;
	}

	public DepartmentService getDepartmentService()
	{
		return departmentService;
	}

	public EmployeeService getEmployeeService()
	{
		return employeeService;
	}

	@Override
	public MachineService getMachineService()
	{
		return machineService;
	}

	public MaterialClassService getMaterialClassService()
	{
		return materialClassService;
	}

	public MaterialService getMaterialService()
	{
		return materialService;
	}

	@Override
	public PaymentClassService getPaymentClassService()
	{
		return paymentClassService;
	}

	public PositionService getPositionService()
	{
		return positionService;
	}

	public ProcedureClassService getProcedureClassService()
	{
		return procedureClassService;
	}

	public ProcedureService getProcedureService()
	{
		return procedureService;
	}

	public ProductClassService getProductClassService()
	{
		return productClassService;
	}

	public ProductService getProductService()
	{
		return productService;
	}

	@Override
	public SettlementClassService getSettlementClassService()
	{
		return settlementService;
	}

	public SupplierClassService getSupplierClassService()
	{
		return supplierClassService;
	}

	public SupplierService getSupplierService()
	{
		return supplierService;
	}

	@Override
	public TaxRateService getTaxRateService()
	{
		return taxRateService;
	}

	public UnitService getUnitService()
	{
		return unitService;
	}

	public UnitConvertService getUnitConvertService()
	{
		return unitConvertService;
	}

	public WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	// ==================== 基础设置 - 期初 ====================
	@Override
	public AccountBeginService getAccountBeginService()
	{
		return accountBeginService;
	}

	@Override
	public CustomerBeginService getCustomerBeginService()
	{
		return customerBeginService;
	}

	@Override
	public MaterialBeginService getMaterialBeginService()
	{
		return materialBeginService;
	}

	@Override
	public ProductBeginService getProductBeginService()
	{
		return productBeginService;
	}

	@Override
	public SupplierBeginService getSupplierBeginService()
	{
		return supplierBeginService;
	}

	// ==================== 财务管理 ====================

	@Override
	public FinanceAccountLogService getAccountLogService()
	{
		return accountLogService;
	}

	@Override
	public FinanceOtherPaymentService getOtherPaymentService()
	{
		return otherPaymentService;
	}

	@Override
	public FinanceOtherReceiveService getOtherReceiveService()
	{
		return otherReceiveService;
	}

	@Override
	public FinancePaymentService getPaymentService()
	{
		return paymentService;
	}

	@Override
	public FinanceReceiveService getReceiveService()
	{
		return receiveService;
	}

	@Override
	public FinanceSumService getSumService()
	{
		return sumService;
	}

	@Override
	public FinanceWriteoffPaymentService getWriteoffPaymentService()
	{
		return writeoffPaymentService;
	}

	@Override
	public FinanceWriteoffReceiveService getWriteoffReceiveService()
	{
		return writeoffReceiveService;
	}

	@Override
	public FinanceReceiveAdvanceLogService getReceiveAdvanceLogService()
	{
		return receiveAdvanceLogService;
	}

	@Override
	public FinancePaymentAdvanceLogService getPaymentAdvanceLogService()
	{
		return paymentAdvanceLogService;
	}

	@Override
	public FinanceAdjustService getFinanceAdjustService()
	{
		return financeAdjustService;
	}
	// ==================== 经理查询 ====================

	// ==================== 报价系统 ======================

	@Override
	public OfferOrderService getOfferOrderService()
	{
		return offerOrderService;
	}

	@Override
	public OfferSettingService getOfferSettingService()
	{
		return offerSettingService;
	}

	@Override
	public OfferAutoService getOfferAutoService()
	{
		return offerAutoService;
	}

	// ==================== 发外管理 ======================

	public OutSourceArriveService getOutSourceArriveService()
	{
		return outSourceArriveService;
	}

	@Override
	public OutSourceProcessService getOutSourceProcessService()
	{
		return outSourceProcessService;
	}

	public OutSourceReconcilService getOutSourceReconcilService()
	{
		return outSourceReconcilService;
	}

	public OutSourceReturnService getOutSourceReturnService()
	{
		return outSourceReturnService;
	}

	// ==================== 代工管理 ======================

	@Override
	public OemOrderService getOemOrderService()
	{
		return oemOrderService;
	}

	@Override
	public OemTransmitService getOemTransmitService()
	{
		return oemTransmitService;
	}

	@Override
	public OemDeliverService getOemDeliverService()
	{
		return oemDeliverService;
	}

	@Override
	public OemReturnService getOemReturnService()
	{
		return oemReturnService;
	}

	@Override
	public OemReconcilService getOemReconcilService()
	{
		return oemReconcilService;
	}

	@Override
	public OemZeroService getOemZeroService()
	{
		return oemZeroService;
	}

	// ==================== 支付管理 ======================

	// ==================== 生产管理 ======================

	@Override
	public WorkService getWorkService()
	{
		return workService;
	}

	// ==================== 采购管理 ======================

	@Override
	public PurchOrderService getPurOrderService()
	{
		return purOrderService;
	}

	@Override
	public PurchReconcilService getPurReconcilService()
	{
		return purReconcilService;
	}

	@Override
	public PurchReturnService getPurReturnService()
	{
		return purReturnService;
	}

	@Override
	public PurchStockService getPurStockService()
	{
		return purStockService;
	}

	@Override
	public PurchSumService getPurchSumService()
	{
		return purchSumService;
	}

	// ==================== 销售管理 ======================

	@Override
	public SaleDeliverService getSaleDeliverService()
	{
		return saleDeliverService;
	}

	@Override
	public SaleOrderService getSaleOrderService()
	{
		return saleOrderService;
	}

	@Override
	public SaleReconcilService getSaleReconcilService()
	{
		return saleReconcilService;
	}

	@Override
	public SaleReturnService getSaleReturnService()
	{
		return saleReturnService;
	}
	@Override
	public SaleTransmitService getSaleTransmitService()
	{
		return saleTransmitService;
	}
	// ==================== 库存管理 ======================

	@Override
	public StockMaterialAdjustService getStockMaterialAdjustService()
	{
		return stockMaterialAdjustService;
	}

	@Override
	public StockMaterialService getMaterialStockService()
	{
		return materialStockService;
	}

	@Override
	public StockMaterialInventoryService getStockMaterialInventoryService()
	{
		return stockMaterialInventoryService;
	}

	@Override
	public StockMaterialOtherInService getStockMaterialOtherInService()
	{
		return stockMaterialOtherInService;
	}

	@Override
	public StockMaterialOtherOutService getStockMaterialOtherOutService()
	{
		return stockMaterialOtherOutService;
	}

	@Override
	public StockMaterialReturnService getStockMaterialReturnService()
	{
		return stockMaterialReturnService;
	}

	@Override
	public StockMaterialSplitService getStockMaterialSplitService()
	{
		return stockMaterialSplitService;
	}

	@Override
	public StockMaterialSupplementService getStockMaterialSupplementService()
	{
		return stockMaterialSupplementService;
	}

	@Override
	public StockMaterialTakeService getStockMaterialTakeService()
	{
		return stockMaterialTakeService;
	}

	@Override
	public StockMaterialTransferService getStockMaterialTransferService()
	{
		return stockMaterialTransferService;
	}

	@Override
	public StockProductAdjustService getStockProductAdjustService()
	{
		return stockProductAdjustService;
	}

	@Override
	public StockProductService getStockProductService()
	{
		return stockProductService;
	}

	@Override
	public StockProductInService getStockProductInService()
	{
		return stockProductInService;
	}

	@Override
	public StockProductInventoryService getStockProductInventoryService()
	{
		return stockProductInventoryService;
	}

	@Override
	public StockProductOtherInService getStockProductOtherInService()
	{
		return stockProductOtherInService;
	}

	@Override
	public StockProductOtherOutService getStockProductOtherOutService()
	{
		return stockProductOtherOutService;
	}

	@Override
	public StockProductTransferService getStockProductTransferService()
	{
		return stockProductTransferService;
	}

	@Override
	public StockService getStockService()
	{
		return stockService;
	}

	// ==================== 工作台 ======================

	@Override
	public WorkbenchMemorandumService getWorkbenchMemorandumService()
	{
		return workbenchMemorandumService;
	}

	@Override
	public WorkbenchNoticeService getWorkbenchNoticeService()
	{
		return workbenchNoticeService;
	}

	@Override
	public WorkbenchOftenService getWorkbenchOftenService()
	{
		return workbenchOftenService;
	}

	// ==================== 微信 ======================

	@Override
	public WXPayService getWXPayService()
	{
		return wXPayService;
	}

	@Override
	public WXCheckService getWXCheckService()
	{
		return wXCheckService;
	}

	@Override
	public WXScheduleService getWXScheduleService()
	{
		return wXScheduleService;
	}

	@Override
	public WXWarnService getWXWarnService()
	{
		return wXWarnService;
	}

	@Override
	public WXSumService getWXSumService()
	{
		return wXSumService;
	}

	@Override
	public WXStatisticsService getWXStatisticsService()
	{
		return wXStatisticsService;
	}

	@Override
	public WXBasicService getWXBasicService()
	{
		return wXBasicService;
	}

	@Override
	public WXEmployeeSerice getWXEmployeeSerice()
	{
		return wxEmployeeService;
	}
	
	@Override
	public ExteriorService getExteriorService()
	{
		return exteriorService;
	}
	
	@Override
	public ExteriorRegisterService getExteriorRegisterService()
	{
		return exteriorRegisterService;
	}
	@Override
	public ExteriorPurchService getExteriorPurchService()
	{
		return exteriorPurchService;
	}

	@Override
	public PayService getPayService()
	{
		return payService;
	}




	
}
