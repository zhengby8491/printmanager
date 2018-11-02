/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.huayin.common.util.DateTimeUtil;
import com.huayin.printmanager.persist.entity.workbench.WorkbenchOften;
import com.huayin.printmanager.persist.enumerate.AccountTransType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.AdjustType;
import com.huayin.printmanager.persist.enumerate.CommentType;
import com.huayin.printmanager.persist.enumerate.CompanyState;
import com.huayin.printmanager.persist.enumerate.CompanyType;
import com.huayin.printmanager.persist.enumerate.FinanceTradeMode;
import com.huayin.printmanager.persist.enumerate.InitStep;
import com.huayin.printmanager.persist.enumerate.MaterialType;
import com.huayin.printmanager.persist.enumerate.OfferMaterialType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.OutSourceType;
import com.huayin.printmanager.persist.enumerate.PrintModleName;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.ProductType;
import com.huayin.printmanager.persist.enumerate.SplitType;
import com.huayin.printmanager.persist.enumerate.SupplierType;
import com.huayin.printmanager.persist.enumerate.ZeroOriginType;

/**
 * <pre>
 * 列表查询参数
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年7月19日, zhaojt
 * @version 	   2.0, 2018年2月26日上午9:32:30, zhengby, 代码规范
 * @version 	   2.1, 2018年3月16日上午10:32:30, think, 重新整理排版和规范
 */
public class QueryParam implements Serializable
{
	private static final long serialVersionUID = 1L;

	// ==================== 公共 ====================

	/**
	 * 代工平台 - 源数据来源类型（ TODO 是不是去掉zero前缀，会更容易理解？）
	 */
	private ZeroOriginType zeroOriginType;

	/**
	 * 区域 - 省
	 */
	private String province;

	/**
	 * 区域 - 市
	 */
	private String city;

	/**
	 * 区域 - 区
	 */
	private String county;

	/**
	 * 代工平台 - 源数据公司id
	 */
	private String originCompanyId;

	/**
	 * 汇总年份
	 */
	private String year;

	/**
	 * 日期区间
	 */
	@DateTimeFormat(iso = ISO.DATE)
	private Date dateMin;

	@DateTimeFormat(iso = ISO.DATE)
	private Date dateMax;

	/**
	 * 创建日期区间
	 */
	@DateTimeFormat(iso = ISO.DATE)
	private Date createTimeMin;

	@DateTimeFormat(iso = ISO.DATE)
	private Date createTimeMax;

	/**
	 * 修改日期区间
	 */
	@DateTimeFormat(iso = ISO.DATE)
	private Date udateMin;

	@DateTimeFormat(iso = ISO.DATE)
	private Date udateMax;

	/**
	 * 交货日期区间
	 */
	@DateTimeFormat(iso = ISO.DATE)
	private Date deliverDateMin;

	@DateTimeFormat(iso = ISO.DATE)
	private Date deliverDateMax;

	/**
	 * 单据类型
	 */
	private BillType billType;

	/**
	 * 是否期初
	 */
	private BoolValue isBegin;

	/**
	 * 代工平台
	 */
	private BoolValue isOem;
	
	/**
	 * 创建人
	 */
	private String createName;

	// ==================== 员工信息 ====================

	/**
	 * 员工ID
	 */
	private Long employeeId;

	/**
	 * 员工名称
	 */
	private String employeeName;

	// ==================== 客户分类 ====================

	/**
	 * 客户分类ID
	 */
	private Long customerClassId;

	/**
	 * 客户分类名
	 */
	private String customerClassName;

	// ==================== 客户信息 ====================

	/**
	 * 客户ID
	 */
	private Long customerId;

	/**
	 * 客户IDs
	 */
	private List<Long> customerIdList;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 客户单据编号
	 */
	private String customerBillNo;

	/**
	 * 客户料号
	 */
	private String customerMaterialCode;

	// ==================== 供应商分类 ====================

	// 供应商分类ID
	private Long supplierClassId;

	// 供应商分类名称
	private String supplierClassName;

	// ==================== 供应商 ====================

	// 供应商类型
	private SupplierType supplierType;

	// 供应商ID
	private Long supplierId;

	// 供应商idList
	private List<Long> supplierIdList;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	// ==================== 产品分类 ====================

	// 产品分类
	private Long productClassId;

	// 产品分类名
	private String productClassName;

	// ==================== 产品信息 ====================

	// 产品类型
	private ProductType productType;

	// 产品名称
	private String productName;

  // 产品名称
	private Long productId;
	
	// 产品规格
	private String productStyle;

	// 产品编号
	private String productCode;

	// ==================== 材料分类 ====================

	// 材料分类
	private Long materialClassId;

	// 材料分类名称
	private String materialClassName;

	// ==================== 材料信息 ====================

	// 材料类型
	private MaterialType materialType;

	// 材料名称
	private String materialName;

	/**
	 * 材料规格 
	 */
	private String specifications;

	// ==================== 工序分类 ====================

	// 工序分类
	private Long procedureClassId;

	// 工序分类名称
	private String procedureClassName;

	// ==================== 工序信息 ====================

	// 工序类型
	private ProcedureType procedureType;

	// 工序类型数组
	private ProcedureType[] procedureTypeArray;

	// 工序名称
	private String procedureName;

	// 部件名称
	private String partName;

	// 发料人
	private Long sendEmployeeId;

	// 领料人
	private Long receiveEmployeeId;

	// 调出仓库
	private Long outWarehouseId;

	// 调入仓库
	private Long inWarehouseId;

	// 销售单号
	private String saleBillNo;

	// 原单明细ID
	private Long sourceDetailId;

	// 工单号
	private String workBillNo;

	// ==================== 公司信息 ====================

	// 公司名称
	private String companyName;

	// 公司状态
	private CompanyState companyState;

	// 公司类型
	private CompanyType companyType;

	// 联系人姓名
	private String companyLinkName;

	// 公司电话
	private String companyTel;

	// 是否正式
	private BoolValue isFormal;

	// 初始化进度
	private InitStep initStep;

	// 试用时间最小
	private Date expireTimeMin;

	// 使用时间最大
	private Date expireTimeMax;

	// ==================== 部门信息 ====================

	// 部门名称
	private String departmentName;

	// 职位名称
	private String positionName;

	// 结算方式名称
	private String settlementClassName;

	// 税率名称
	private String taxRateName;

	// 单位名称
	private String unitName;

	// 系统公告标题
	private String noticeTitle;

	// 是否发布
	private BoolValue publish;

	// 手机号码
	private String tel;

	// 打印模板名称
	private PrintModleName printModleName;

	// 机台名称
	private String machineName;

	// 单据编号
	private String billNo;

	// 是否完工标志
	private BoolValue completeFlag;

	// 是否审核标志
	private BoolValue auditFlag;

	// 是否取消
	private BoolValue isCancel;

	/**
	 * 是否显示0库存
	 */
	private BoolValue isEmptyWare;

	// 非固定字段查询条件
	private String searchContent;

	private Integer pageSize;

	private Integer pageNumber;

	private String title;

	// 用户名
	private String userName;

	/**
	 * 留言类型
	 */
	private CommentType type;

	/**
	 * 报价类型
	 */
	private OfferType offerType;

	/**
	 * 分切类型
	 */
	private SplitType splitType;

	private String name;

	private String code;

	private List<WorkbenchOften> oftenList;

	/**
	 * 上报单号
	 */
	private String masterBillNo;

	/**
	 * 原单号
	 */
	private String sourceBillNo;

	/**
	 * 采购单号
	 */
	private String orderBillNo;

	/**
	 * 加工单号
	 */
	private String outSourceBillNo;

	/**
	 * 克重
	 */
	private Integer weight;

	/**
	 * 销售订单单据编号
	 */
	private String saleOrderBillNo;

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
	private Integer isPay;

	/**
	 * 订单状态  0已取消   1待支付   2 已完成
	 */
	private Integer orderState = 1;

	/**
	 * 订单类型 1用户创建  2后台创建 
	 */
	private Integer orderType = 1;

	/**
	 * 购买类型  1 购买    2续费
	 */
	private Integer buyType;

	/**
	 * 报价系统材料类型 1：基础材料  2：坑纸
	 */
	private OfferMaterialType offerMaterialType = null;

	/**
	 * 整单外发(YES|NO)
	 */
	private BoolValue isOutSource;

	/**
	 * 交易类型
	 */
	private AccountTransType transType;

	/**
	 * 交易模式
	 */
	private FinanceTradeMode tradeMode;

	// =======================================================

	private List<Long> ids;

	// ID
	private Long id;

	// 账户号
	private String bankNo;

	// 仓库ID
	private Long warehouseId;

	// 仓库名称
	private String warehouseName;

	// 送货方式
	private String deliveryClassName;

	// 付款方式名称
	private String paymentClassName;

	// 发外类型
	private OutSourceType outSourceType;

	// 代工单号
	private String oemOrderBillNo;
	
	// 商家id
	private Long businessId;
	
	// 商家名称
	private String businessName;
	
	// 商家类型
	private AdjustType adjustType;

	// ==================== Get And Set ====================

	// ==================== 公共 ====================

	public ZeroOriginType getZeroOriginType()
	{
		return zeroOriginType;
	}

	public void setZeroOriginType(ZeroOriginType zeroOriginType)
	{
		this.zeroOriginType = zeroOriginType;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getCounty()
	{
		return county;
	}

	public void setCounty(String county)
	{
		this.county = county;
	}

	public String getOriginCompanyId()
	{
		return originCompanyId;
	}

	public void setOriginCompanyId(String originCompanyId)
	{
		this.originCompanyId = originCompanyId;
	}

	public String getYear()
	{
		return year;
	}

	public void setYear(String year)
	{
		this.year = year;
	}

	public Date getDateMin()
	{
		if (dateMin != null)
		{
			return DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(dateMin) + " 00:00:01", DateTimeUtil.DATE_FORMAT_YMDHMS);
		}
		return dateMin;
	}

	public void setDateMin(Date dateMin)
	{

		this.dateMin = dateMin;
	}

	public Date getDateMax()
	{
		if (dateMax != null)
		{
			return DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(dateMax) + " 23:59:59", DateTimeUtil.DATE_FORMAT_YMDHMS);
		}
		return dateMax;
	}

	public void setDateMax(Date dateMax)
	{
		this.dateMax = dateMax;
	}

	public Date getCreateTimeMin()
	{
		if (createTimeMin != null)
		{
			return DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(createTimeMin) + " 00:00:01", DateTimeUtil.DATE_FORMAT_YMDHMS);
		}
		return createTimeMin;
	}

	public void setCreateTimeMin(Date createTimeMin)
	{
		this.createTimeMin = createTimeMin;
	}

	public Date getCreateTimeMax()
	{
		if (createTimeMax != null)
		{
			return DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(createTimeMax) + " 23:59:59", DateTimeUtil.DATE_FORMAT_YMDHMS);
		}
		return createTimeMax;
	}

	public void setCreateTimeMax(Date createTimeMax)
	{
		this.createTimeMax = createTimeMax;
	}

	public Date getUdateMin()
	{
		if (udateMin != null)
		{
			return DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(udateMin) + " 00:00:00", DateTimeUtil.DATE_FORMAT_YMDHMS);
		}
		return udateMin;
	}

	public void setUdateMin(Date udateMin)
	{

		this.udateMin = udateMin;
	}

	public Date getUdateMax()
	{
		if (udateMax != null)
		{
			return DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(udateMax) + " 23:59:59", DateTimeUtil.DATE_FORMAT_YMDHMS);
		}
		return udateMax;
	}

	public void setUdateMax(Date udateMax)
	{
		this.udateMax = udateMax;
	}

	public BillType getBillType()
	{
		return billType;
	}

	public void setBillType(BillType billType)
	{
		this.billType = billType;
	}

	public BoolValue getIsBegin()
	{
		return isBegin;
	}

	public void setIsBegin(BoolValue isBegin)
	{
		this.isBegin = isBegin;
	}

	public BoolValue getIsOem()
	{
		return isOem;
	}

	public void setIsOem(BoolValue isOem)
	{
		this.isOem = isOem;
	}

	public String getCreateName()
	{
		return createName;
	}
	
	public void setCreateName(String createName)
	{
		this.createName = createName;
	}
	
	// ==================== 员工信息 ====================

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	// ==================== 客户分类 ====================

	public Long getCustomerClassId()
	{
		return customerClassId;
	}

	public void setCustomerClassId(Long customerClassId)
	{
		this.customerClassId = customerClassId;
	}

	public String getCustomerClassName()
	{
		return customerClassName;
	}

	public void setCustomerClassName(String customerClassName)
	{
		this.customerClassName = customerClassName;
	}

	// ==================== 客户信息 ====================

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public List<Long> getCustomerIdList()
	{
		return customerIdList;
	}

	public void setCustomerIdList(List<Long> customerIdList)
	{
		this.customerIdList = customerIdList;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public String getCustomerBillNo()
	{
		return customerBillNo;
	}

	public void setCustomerBillNo(String customerBillNo)
	{
		this.customerBillNo = customerBillNo;
	}

	public String getCustomerMaterialCode()
	{
		return customerMaterialCode;
	}

	public void setCustomerMaterialCode(String customerMaterialCode)
	{
		this.customerMaterialCode = customerMaterialCode;
	}

	// ==================== 供应商分类 ====================

	public Long getSupplierClassId()
	{
		return supplierClassId;
	}

	public void setSupplierClassId(Long supplierClassId)
	{
		this.supplierClassId = supplierClassId;
	}

	public String getSupplierClassName()
	{
		return supplierClassName;
	}

	public void setSupplierClassName(String supplierClassName)
	{
		this.supplierClassName = supplierClassName;
	}

	// ==================== 供应商 ====================

	public SupplierType getSupplierType()
	{
		return supplierType;
	}

	public void setSupplierType(SupplierType supplierType)
	{
		this.supplierType = supplierType;
	}

	public Long getSupplierId()
	{
		return supplierId;
	}

	public void setSupplierId(Long supplierId)
	{
		this.supplierId = supplierId;
	}

	public List<Long> getSupplierIdList()
	{
		return supplierIdList;
	}

	public void setSupplierIdList(List<Long> supplierIdList)
	{
		this.supplierIdList = supplierIdList;
	}

	public String getSupplierName()
	{
		return supplierName;
	}

	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
	}

	// ==================== 产品分类 ====================

	public Long getProductClassId()
	{
		return productClassId;
	}

	public void setProductClassId(Long productClassId)
	{
		this.productClassId = productClassId;
	}

	public String getProductClassName()
	{
		return productClassName;
	}

	public void setProductClassName(String productClassName)
	{
		this.productClassName = productClassName;
	}

	// ==================== 产品信息 ====================

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
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

	public String getProductStyle()
	{
		return productStyle;
	}

	public void setProductStyle(String productStyle)
	{
		this.productStyle = productStyle;
	}

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}

	// ==================== 材料分类 ====================

	public Long getMaterialClassId()
	{
		return materialClassId;
	}

	public void setMaterialClassId(Long materialClassId)
	{
		this.materialClassId = materialClassId;
	}

	public String getMaterialClassName()
	{
		return materialClassName;
	}

	public void setMaterialClassName(String materialClassName)
	{
		this.materialClassName = materialClassName;
	}

	// ==================== 材料信息 ====================

	public MaterialType getMaterialType()
	{
		return materialType;
	}

	public void setMaterialType(MaterialType materialType)
	{
		this.materialType = materialType;
	}

	public String getMaterialName()
	{
		return materialName;
	}

	public void setMaterialName(String materialName)
	{
		this.materialName = materialName;
	}

	// ==================== 工序分类 ====================

	public Long getProcedureClassId()
	{
		return procedureClassId;
	}

	public void setProcedureClassId(Long procedureClassId)
	{
		this.procedureClassId = procedureClassId;
	}

	public String getProcedureClassName()
	{
		return procedureClassName;
	}

	public void setProcedureClassName(String procedureClassName)
	{
		this.procedureClassName = procedureClassName;
	}

	// ==================== 工序信息 ====================

	public ProcedureType getProcedureType()
	{
		return procedureType;
	}

	public void setProcedureType(ProcedureType procedureType)
	{
		this.procedureType = procedureType;
	}

	public ProcedureType[] getProcedureTypeArray()
	{
		return procedureTypeArray;
	}

	public void setProcedureTypeArray(ProcedureType[] procedureTypeArray)
	{
		this.procedureTypeArray = procedureTypeArray;
	}

	public String getProcedureName()
	{
		return procedureName;
	}

	public void setProcedureName(String procedureName)
	{
		this.procedureName = procedureName;
	}

	// ==================== 公司信息 ====================

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public BoolValue getIsFormal()
	{
		return isFormal;
	}

	public void setIsFormal(BoolValue isFormal)
	{
		this.isFormal = isFormal;
	}

	public CompanyState getCompanyState()
	{
		return companyState;
	}

	public void setCompanyState(CompanyState companyState)
	{
		this.companyState = companyState;
	}

	public CompanyType getCompanyType()
	{
		return companyType;
	}

	public void setCompanyType(CompanyType companyType)
	{
		this.companyType = companyType;
	}

	public InitStep getInitStep()
	{
		return initStep;
	}

	public void setInitStep(InitStep initStep)
	{
		this.initStep = initStep;
	}

	public Date getExpireTimeMin()
	{
		if (expireTimeMin != null)
		{
			return DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(expireTimeMin) + " 00:00:00", DateTimeUtil.DATE_FORMAT_YMDHMS);
		}
		return expireTimeMin;
	}

	public void setExpireTimeMin(Date expireTimeMin)
	{
		this.expireTimeMin = expireTimeMin;
	}

	public Date getExpireTimeMax()
	{
		if (expireTimeMax != null)
		{
			return DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(expireTimeMax) + " 23:59:59", DateTimeUtil.DATE_FORMAT_YMDHMS);
		}
		return expireTimeMax;
	}

	public void setExpireTimeMax(Date expireTimeMax)
	{
		this.expireTimeMax = expireTimeMax;
	}

	public String getCompanyLinkName()
	{
		return companyLinkName;
	}

	public void setCompanyLinkName(String companyLinkName)
	{
		this.companyLinkName = companyLinkName;
	}

	public String getCompanyTel()
	{
		return companyTel;
	}

	public void setCompanyTel(String companyTel)
	{
		this.companyTel = companyTel;
	}

	// ==================== 部门信息 ====================

	public String getDepartmentName()
	{
		return departmentName;
	}

	public void setDepartmentName(String departmentName)
	{
		this.departmentName = departmentName;
	}

	public String getPositionName()
	{
		return positionName;
	}

	public void setPositionName(String positionName)
	{
		this.positionName = positionName;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public BoolValue getCompleteFlag()
	{
		return completeFlag;
	}

	public void setCompleteFlag(BoolValue completeFlag)
	{
		this.completeFlag = completeFlag;
	}

	public BoolValue getAuditFlag()
	{
		return auditFlag;
	}

	public void setAuditFlag(BoolValue auditFlag)
	{
		this.auditFlag = auditFlag;
	}

	public Integer getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	public Integer getPageNumber()
	{
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber)
	{
		this.pageNumber = pageNumber;
	}

	public String getSearchContent()
	{
		return searchContent;
	}

	public void setSearchContent(String searchContent)
	{
		this.searchContent = searchContent;
	}

	public String getPartName()
	{
		return partName;
	}

	public void setPartName(String partName)
	{
		this.partName = partName;
	}

	public OutSourceType getOutSourceType()
	{
		return outSourceType;
	}

	public void setOutSourceType(OutSourceType outSourceType)
	{
		this.outSourceType = outSourceType;
	}

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public Date getDeliverDateMin()
	{
		return deliverDateMin;
	}

	public void setDeliverDateMin(Date deliverDateMin)
	{
		this.deliverDateMin = deliverDateMin;
	}

	public Date getDeliverDateMax()
	{
		if (deliverDateMax != null)
		{
			return new Date(DateTimeUtil.addDate(deliverDateMax, 1).getTime() - 1);
		}
		return deliverDateMax;
	}

	public void setDeliverDateMax(Date deliverDateMax)
	{
		this.deliverDateMax = deliverDateMax;
	}

	public Long getSendEmployeeId()
	{
		return sendEmployeeId;
	}

	public void setSendEmployeeId(Long sendEmployeeId)
	{
		this.sendEmployeeId = sendEmployeeId;
	}

	public Long getReceiveEmployeeId()
	{
		return receiveEmployeeId;
	}

	public void setReceiveEmployeeId(Long receiveEmployeeId)
	{
		this.receiveEmployeeId = receiveEmployeeId;
	}

	public Long getOutWarehouseId()
	{
		return outWarehouseId;
	}

	public void setOutWarehouseId(Long outWarehouseId)
	{
		this.outWarehouseId = outWarehouseId;
	}

	public Long getInWarehouseId()
	{
		return inWarehouseId;
	}

	public void setInWarehouseId(Long inWarehouseId)
	{
		this.inWarehouseId = inWarehouseId;
	}

	public String getSaleBillNo()
	{
		return saleBillNo;
	}

	public void setSaleBillNo(String saleBillNo)
	{
		this.saleBillNo = saleBillNo;
	}

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
	}

	public String getBankNo()
	{
		return bankNo;
	}

	public void setBankNo(String bankNo)
	{
		this.bankNo = bankNo;
	}

	public String getDeliveryClassName()
	{
		return deliveryClassName;
	}

	public void setDeliveryClassName(String deliveryClassName)
	{
		this.deliveryClassName = deliveryClassName;
	}

	public String getEmployeeName()
	{
		return employeeName;
	}

	public void setEmployeeName(String employeeName)
	{
		this.employeeName = employeeName;
	}

	public String getPaymentClassName()
	{
		return paymentClassName;
	}

	public void setPaymentClassName(String paymentClassName)
	{
		this.paymentClassName = paymentClassName;
	}

	public String getSettlementClassName()
	{
		return settlementClassName;
	}

	public void setSettlementClassName(String settlementClassName)
	{
		this.settlementClassName = settlementClassName;
	}

	public String getTaxRateName()
	{
		return taxRateName;
	}

	public void setTaxRateName(String taxRateName)
	{
		this.taxRateName = taxRateName;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

	public String getWarehouseName()
	{
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName)
	{
		this.warehouseName = warehouseName;
	}

	public Long getSourceDetailId()
	{
		return sourceDetailId;
	}

	public void setSourceDetailId(Long sourceDetailId)
	{
		this.sourceDetailId = sourceDetailId;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public CommentType getType()
	{
		return type;
	}

	public void setType(CommentType type)
	{
		this.type = type;
	}

	public String getNoticeTitle()
	{
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle)
	{
		this.noticeTitle = noticeTitle;
	}

	public BoolValue getPublish()
	{
		return publish;
	}

	public void setPublish(BoolValue publish)
	{
		this.publish = publish;
	}

	public BoolValue getIsEmptyWare()
	{
		return isEmptyWare;
	}

	public void setIsEmptyWare(BoolValue isEmptyWare)
	{
		this.isEmptyWare = isEmptyWare;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getTel()
	{
		return tel;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}

	public PrintModleName getPrintModleName()
	{
		return printModleName;
	}

	public void setPrintModleName(PrintModleName printModleName)
	{
		this.printModleName = printModleName;
	}

	public String getMachineName()
	{
		return machineName;
	}

	public void setMachineName(String machineName)
	{
		this.machineName = machineName;
	}

	public OfferType getOfferType()
	{
		return offerType;
	}

	public void setOfferType(OfferType offerType)
	{
		this.offerType = offerType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public BoolValue getIsCancel()
	{
		return isCancel;
	}

	public void setIsCancel(BoolValue isCancel)
	{
		this.isCancel = isCancel;
	}

	public SplitType getSplitType()
	{
		return splitType;
	}

	public void setSplitType(SplitType splitType)
	{
		this.splitType = splitType;
	}

	public String getSpecifications()
	{
		return specifications;
	}

	public void setSpecifications(String specifications)
	{
		this.specifications = specifications;
	}

	public List<WorkbenchOften> getOftenList()
	{
		return oftenList;
	}

	public void setOftenList(List<WorkbenchOften> oftenList)
	{
		this.oftenList = oftenList;
	}

	public String getMasterBillNo()
	{
		return masterBillNo;
	}

	public void setMasterBillNo(String masterBillNo)
	{
		this.masterBillNo = masterBillNo;
	}

	public String getSourceBillNo()
	{
		return sourceBillNo;
	}

	public void setSourceBillNo(String sourceBillNo)
	{
		this.sourceBillNo = sourceBillNo;
	}

	public String getOrderBillNo()
	{
		return orderBillNo;
	}

	public void setOrderBillNo(String orderBillNo)
	{
		this.orderBillNo = orderBillNo;
	}

	public String getOutSourceBillNo()
	{
		return outSourceBillNo;
	}

	public void setOutSourceBillNo(String outSourceBillNo)
	{
		this.outSourceBillNo = outSourceBillNo;
	}

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public String getSaleOrderBillNo()
	{
		return saleOrderBillNo;
	}

	public void setSaleOrderBillNo(String saleOrderBillNo)
	{
		this.saleOrderBillNo = saleOrderBillNo;
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

	public Integer getIsPay()
	{
		return isPay;
	}

	public void setIsPay(Integer isPay)
	{
		this.isPay = isPay;
	}

	public Integer getOrderState()
	{
		return orderState;
	}

	public void setOrderState(Integer orderState)
	{
		this.orderState = orderState;
	}

	public Integer getOrderType()
	{
		return orderType;
	}

	public void setOrderType(Integer orderType)
	{
		this.orderType = orderType;
	}

	public Integer getBuyType()
	{
		return buyType;
	}

	public void setBuyType(Integer buyType)
	{
		this.buyType = buyType;
	}

	public OfferMaterialType getOfferMaterialType()
	{
		return offerMaterialType;
	}

	public void setOfferMaterialType(OfferMaterialType offerMaterialType)
	{
		this.offerMaterialType = offerMaterialType;
	}

	public BoolValue getIsOutSource()
	{
		return isOutSource;
	}

	public void setIsOutSource(BoolValue isOutSource)
	{
		this.isOutSource = isOutSource;
	}

	public AccountTransType getTransType()
	{
		return transType;
	}

	public void setTransType(AccountTransType transType)
	{
		this.transType = transType;
	}

	public FinanceTradeMode getTradeMode()
	{
		return tradeMode;
	}

	public void setTradeMode(FinanceTradeMode tradeMode)
	{
		this.tradeMode = tradeMode;
	}

	public List<Long> getIds()
	{
		return ids;
	}

	public void setIds(List<Long> ids)
	{
		this.ids = ids;
	}

	public String getOemOrderBillNo()
	{
		return oemOrderBillNo;
	}

	public void setOemOrderBillNo(String oemOrderBillNo)
	{
		this.oemOrderBillNo = oemOrderBillNo;
	}

	public Long getBusinessId()
	{
		return businessId;
	}

	public void setBusinessId(Long businessId)
	{
		this.businessId = businessId;
	}

	public String getBusinessName()
	{
		return businessName;
	}

	public void setBusinessName(String businessName)
	{
		this.businessName = businessName;
	}

	public AdjustType getAdjustType()
	{
		return adjustType;
	}

	public void setAdjustType(AdjustType adjustType)
	{
		this.adjustType = adjustType;
	}
	
}
