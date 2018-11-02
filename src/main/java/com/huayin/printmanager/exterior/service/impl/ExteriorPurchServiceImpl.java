/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月12日 下午5:56:44
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JavaType;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.exterior.dto.ReqBody;
import com.huayin.printmanager.exterior.dto.RequestDto;
import com.huayin.printmanager.exterior.dto.ResponseDto;
import com.huayin.printmanager.exterior.dto.RspBody;
import com.huayin.printmanager.exterior.dto.RspHeader;
import com.huayin.printmanager.exterior.enums.ActionType;
import com.huayin.printmanager.exterior.enums.DeliveryMethod;
import com.huayin.printmanager.exterior.enums.InvoiceType;
import com.huayin.printmanager.exterior.enums.OrderStatusType;
import com.huayin.printmanager.exterior.enums.PaymentType;
import com.huayin.printmanager.exterior.enums.PurchOrderStatusType;
import com.huayin.printmanager.exterior.enums.ShipmentType;
import com.huayin.printmanager.exterior.service.ExteriorPurchService;
import com.huayin.printmanager.exterior.utils.ExteriorHelper;
import com.huayin.printmanager.exterior.vo.ExteriorPurchOrder;
import com.huayin.printmanager.exterior.vo.ExteriorPurchOrderDetail;
import com.huayin.printmanager.pay.util.JsonMapper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.persist.entity.basic.SettlementClass;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.SupplierAddress;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 外部接口  - 平台采购单数据交互
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月12日下午5:56:44, zhengby
 */
@Service
public class ExteriorPurchServiceImpl extends BaseServiceImpl implements ExteriorPurchService
{
	protected static final ExecutorService threadPool = Executors.newCachedThreadPool();

	@Override
	public ExteriorPurchOrder get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(ExteriorPurchOrder.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, ExteriorPurchOrder.class);
	}

	@Override
	public ExteriorPurchOrderDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(ExteriorPurchOrderDetail.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, ExteriorPurchOrderDetail.class);
	}

	@Override
	public ExteriorPurchOrderDetail getDetailHasMater(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(ExteriorPurchOrderDetail.class);
		query.eq("id", id);
		ExteriorPurchOrderDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, ExteriorPurchOrderDetail.class);
		ExteriorPurchOrder master = this.get(detail.getMasterId());
		detail.setMaster(master);
		return detail;
	}

	@Override
	public List<ExteriorPurchOrderDetail> getDetailList(Long masterId)
	{
		DynamicQuery query = new CompanyDynamicQuery(ExteriorPurchOrderDetail.class);
		query.eq("masterId", masterId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, ExteriorPurchOrderDetail.class);
	}

	@Override
	public List<ExteriorPurchOrderDetail> getAllDetailList(Long masterId)
	{
		DynamicQuery query = new DynamicQuery(ExteriorPurchOrderDetail.class);
		query.eq("masterId", masterId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, ExteriorPurchOrderDetail.class);
	}

	@Override
	public ExteriorPurchOrder getOrderHasDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(ExteriorPurchOrder.class);
		query.eq("id", id);
		ExteriorPurchOrder order = daoFactory.getCommonDao().getByDynamicQuery(query, ExteriorPurchOrder.class);
		order.setItems(getDetailList(id));
		return order;
	}

	@Override
	public ExteriorPurchOrder findOrderByConditions(String purchOrderNm)
	{
		DynamicQuery query = new DynamicQuery(ExteriorPurchOrder.class);
		query.eq("purchaseOrderNm", purchOrderNm);
		return daoFactory.getCommonDao().getByDynamicQuery(query, ExteriorPurchOrder.class);
	}

	@Override
	public ResponseDto acceptPONotice(RequestDto requestDto)
	{
		// 印刷家通知采购订单下单,获取印刷家采购编号
		final String purchaseOrderNm = requestDto.getReqBody().getPurchaseOrderNm();
		ResponseDto responseDto = new ResponseDto();
		RspHeader header = new RspHeader();
		RspBody body = new RspBody();
		if (purchaseOrderNm != null)
		{
			header.setRspCode("EC0000");
			header.setRspMsg("系统处理成功");
			body.setState("success");
			if (null == findOrderByConditions(purchaseOrderNm)) // 不存在此订单才执行
			{
				// 异步请求印刷家获取采购订单信息
				threadPool.execute(new Runnable()
				{
					public void run()
					{
						serviceFactory.getExteriorPurchService().purchaseOrder(purchaseOrderNm);
					}
				});
			}
		}
		else
		{
			header.setRspCode("EC9999");
			header.setRspMsg("系统处理失败！采购订单号[purchaseOrderNm]为空");
			body.setState("fail");
		}
		responseDto.setResponseHeader(header);
		responseDto.setRspBody(body);
		return responseDto;
	}

	@Override
	@Transactional
	public void purchaseOrder(String puchaseOrderNm)
	{
		// 请求body
		ReqBody body = new ReqBody();
		body.setPurchaseOrderNm(puchaseOrderNm);
		ResponseDto response = ExteriorHelper.buildRequest(body, ActionType.PURCHASE_ORDER);
		/**
		 *  获取到采购订单后开始创建采购未清
		 */
		if (response == null)
		{
			return;
		}
		RspBody rspBody = response.getRspBody();
		// 先查找出下订单的用户
		// 匹配uid，确定来源
		User user = null;
		String uid = rspBody.getUid();
		Pattern pattern = Pattern.compile("^USER");
		if (pattern.matcher(uid).find()) // 匹配到印刷家
		{
			ReqBody reqBody = new ReqBody();
			reqBody.setUid(uid);
			user = serviceFactory.getExteriorService().getUserByCondition(reqBody);
		}
		else
		{
			user = serviceFactory.getUserService().get(Long.valueOf(uid));
		}
		if (user == null)
		{
			logger.error("不存在此用户:uid=" + uid);
			return;
		}
		// 判断是否已存在该订单
		// 采购单
		ExteriorPurchOrder purchOrder = new ExteriorPurchOrder();
		purchOrder.setLinkMan(rspBody.getLinkMan());// 联系人
		purchOrder.setLinkPhone(rspBody.getLinkPhone());// 联系电话
		purchOrder.setPurchaseOrderNm(rspBody.getPurchaseOrderNm());// 采购单号
		purchOrder.setUid(rspBody.getUid());// 用户唯一标识
		purchOrder.setLocalUserId(user.getId()); // 本系统用户id
		purchOrder.setSellerName(rspBody.getSellerName()); // 供应商
		purchOrder.setSellerAddress(rspBody.getSellerAddress()); // 供应商地址
		purchOrder.setOrderPrice(BigDecimal.valueOf(Double.valueOf(rspBody.getOrderPrice())));// 订单金额
		purchOrder.setOrderFreight(BigDecimal.valueOf(Double.valueOf(rspBody.getOrderFreight()))); // 运费
		purchOrder.setPaymentType(PaymentType.setType(Integer.valueOf(rspBody.getPaymentType()))); // 支付方式
		purchOrder.setShipmentType(ShipmentType.setType(Integer.valueOf(rspBody.getShipmentType())));// 配送类型
		purchOrder.setInvoiceType(InvoiceType.setType(Integer.valueOf(rspBody.getInvoiceType()))); // 发票类型
		purchOrder.setDeliveryMethod(DeliveryMethod.setType(Integer.valueOf(rspBody.getDeliveryMethod()))); // 物流运输
		purchOrder.setShippingAddress(rspBody.getShippingAddress()); // 配送地址
		purchOrder.setOrderNm(rspBody.getOrderNm()); // 印刷家平台订单号
		purchOrder.setOrderStatus(OrderStatusType.setType(Integer.valueOf(rspBody.getOrderStatus()))); // 订单状态
		purchOrder.setOrderRemark(rspBody.getOrderRemark()); // 订单备注

		String items = JsonMapper.getInstance().toJson(rspBody.getItems());
		// 将json转化成list
		JavaType javaType = JsonMapper.getInstance().createCollectionType(List.class, ExteriorPurchOrderDetail.class);
		List<ExteriorPurchOrderDetail> rspitems = JsonMapper.getInstance().fromJson(items, javaType);
		purchOrder.setItems(rspitems);
		// 保存订单
		_savePurchOrder(purchOrder, user);
		logger.info("保存成功！");
	}

	@Override
	@Transactional
	public PurchOrder createPurchOrder(List<Long> ids)
	{
		/**
		 * 校验供应商、采购单位、材料名称、材料分类、
		 */
		PurchOrder resultOrder = new PurchOrder();
		List<PurchOrderDetail> resultDetailList = new ArrayList<>();
		resultOrder.setDetailList(resultDetailList);

		for (Long id : ids)
		{
			// -----------------  START一个外部采购单转采购订单的过程 ------------
			ExteriorPurchOrder order = this.getOrderHasDetail(id);
			List<ExteriorPurchOrderDetail> detailList = order.getItems();
			// 以主表带明细表的创建方法
			// 构建一个运费的明细
			ExteriorPurchOrderDetail freight = new ExteriorPurchOrderDetail();
			freight.setBuyNum(BigDecimal.valueOf(1));
			freight.setBuyUnit("KG");
			freight.setCategory("运费");
			freight.setItemName("运费");
			freight.setCompanyId(UserUtils.getCompanyId());
			freight.setItemPrice(order.getOrderFreight());
			freight.setItemTotalPrice(order.getOrderFreight());
			freight.setDiscountPrice(BigDecimal.valueOf(0));
			detailList.add(freight);
			// 检验供应商是否存在
			Supplier supplier = serviceFactory.getSupplierService().getByName(order.getSellerName());
			if (supplier == null)
			{
				Supplier supplier_ = new Supplier();
				supplier_.setName(order.getSellerName());
				supplier_.setCurrencyType(CurrencyType.RMB);
				List<SupplierAddress> supplierAddress = new ArrayList<>();
				SupplierAddress address = new SupplierAddress();
				address.setMobile(order.getLinkPhone());
				address.setUserName(order.getLinkMan());
				address.setAddress(order.getShippingAddress());
				supplierAddress.add(address);
				supplier_.setAddressList(supplierAddress);
				serviceFactory.getSupplierService().save(supplier_);
				UserUtils.clearCacheBasic(BasicType.SUPPLIER); // 清下缓存
				resultOrder.setSupplierId(supplier_.getId());
				resultOrder.setSupplierName(order.getSellerName());
				resultOrder.setLinkName(address.getUserName());
				resultOrder.setMobile(address.getMobile());
				resultOrder.setSupplierAddress(address.getAddress());
				resultOrder.setCurrencyType(supplier_.getCurrencyType());
			}
			else
			{
				Supplier oldSupplier = serviceFactory.getSupplierService().get(supplier.getId()); // 获取更全的供应商信息
				resultOrder.setSupplierId(oldSupplier.getId());
				resultOrder.setSupplierName(oldSupplier.getName());
				resultOrder.setLinkName(oldSupplier.getDefaultAddress().getUserName());
				resultOrder.setMobile(oldSupplier.getDefaultAddress().getMobile());
				resultOrder.setSupplierAddress(oldSupplier.getDefaultAddress().getAddress());
				resultOrder.setCurrencyType(oldSupplier.getCurrencyType());
			}
			// 检验采购单位是否存在
			for (ExteriorPurchOrderDetail _detail : detailList)
			{
				PurchOrderDetail resultDetail = new PurchOrderDetail();

				Unit unit = serviceFactory.getUnitService().getByName(_detail.getBuyUnit());
				if (unit == null)
				{
					Unit _unit = new Unit();
					_unit.setCompanyId(UserUtils.getCompanyId());
					_unit.setAccuracy(2);
					_unit.setName(_detail.getBuyUnit());
					_unit.setSort(serviceFactory.getCommonService().getNextSort(BasicType.UNIT));
					serviceFactory.getUnitService().save(_unit);
					UserUtils.clearCacheBasic(BasicType.UNIT); // 清下缓存
					resultDetail.setUnitId(_unit.getId());
					resultDetail.setPurchUnitName(_unit.getName());
					resultDetail.setValuationUnitId(_unit.getId());
					resultDetail.setValuationUnitName(_unit.getName());
					resultDetail.setPercent(17);
				}
				else
				{
					resultDetail.setUnitId(unit.getId());
					resultDetail.setPurchUnitName(unit.getName());
					resultDetail.setValuationUnitId(unit.getId());
					resultDetail.setValuationUnitName(unit.getName());
				}
				// 材料分类名称换成取二级目录
				String materilclassName = null;
				Pattern pattern = Pattern.compile("@\\^\\^@");
				if (pattern.matcher(_detail.getCategory()).find())
				{
					String[] part = _detail.getCategory().split("@\\^\\^@");
					if (part.length >= 2) // 当类目级别有二级类目或以上
					{
						materilclassName = part[1];
					}
					else // 只含有一级类目
					{
						materilclassName = part[0];
					}
				}
				else
				{
					materilclassName = _detail.getCategory();
				}
				// 检验材料分类是否存在
				MaterialClass materialClass = serviceFactory.getMaterialClassService().getByName(materilclassName);
				if (materialClass == null)
				{
					materialClass = serviceFactory.getMaterialClassService().saveQuick(materilclassName);
					UserUtils.clearCacheBasic(BasicType.MATERIALCLASS); // 清下缓存
				}
				resultDetail.setMaterialClassId(materialClass.getId());
				// 检验材料名称是否存在
				// 材料名称 = 商品名称 + sku信息 + 运费
				String materialName = null;
				if (_detail.getSkuInfo() == null)
				{
					materialName = _detail.getItemName();
				} else
				{
					materialName = _detail.getItemName() + "(" + _detail.getSkuInfo() + ")";
				}
				Material material = serviceFactory.getMaterialService().getByName(materialName);
				if (material == null)
				{
					// saveQuick([材料分类id，材料名称，材料克重，计价单位id，库存单位id，最后采购价])
					Material _material = serviceFactory.getMaterialService().saveQuick(materialClass.getId(), materialName, 0, resultDetail.getValuationUnitId(), resultDetail.getUnitId(), BigDecimal.valueOf(0));

					resultDetail.setMaterialId(_material.getId());
					resultDetail.setMaterialName(_material.getName());
					resultDetail.setCode(_material.getCode());
					resultDetail.setWeight(_material.getWeight());
					UserUtils.clearCacheBasic(BasicType.MATERIAL); // 清下缓存
				}
				else
				{
					resultDetail.setMaterialId(material.getId());
					resultDetail.setMaterialName(material.getName());
					resultDetail.setCode(material.getCode());
					resultDetail.setWeight(material.getWeight());
				}
				// 检验结算方式是否存在：默认取带来的payment_type
				String settlement = order.getPaymentType().getText();
				SettlementClass setment = serviceFactory.getSettlementClassService().getByName(settlement);
				if (setment == null)
				{
					SettlementClass setment_ = new SettlementClass();
					setment_.setCompanyId(UserUtils.getCompanyId());
					setment_.setName(settlement);
					setment_.setSort(serviceFactory.getCommonService().getNextSort(BasicType.SETTLEMENTCLASS));
					setment_ = serviceFactory.getSettlementClassService().save(setment_);
					resultOrder.setSettlementClassId(setment_.getId());
					UserUtils.clearCacheBasic(BasicType.SETTLEMENTCLASS); // 清下缓存
				} else
				{
					resultOrder.setSettlementClassId(setment.getId());
				}
				// =================== 其余字段值 ===================
				resultDetail.setQty(_detail.getBuyNum());// 采购数量
				resultDetail.setValuationQty(_detail.getBuyNum()); // 计价数量
				resultDetail.setMoney(_detail.getItemTotalPrice()); //商品总金额
				BigDecimal price = _detail.getItemTotalPrice().divide(_detail.getBuyNum(),2,RoundingMode.HALF_UP);
				resultDetail.setValuationPrice(price); // 计价单价
				String memo = null;// 备注优惠价格
				if (_detail.getDiscountPrice().compareTo(BigDecimal.valueOf(0)) != 0 )
				{
					memo = "总优惠："+ _detail.getDiscountPrice();
					resultDetail.setMemo(memo);
				}
			
				// 默认取第一个税率
				List<BaseBasicTableEntity> taxRateList = serviceFactory.getCommonService().getBasicInfoList(BasicType.TAXRATE);
				if (_detail.getCategory().equals("运费"))
				{
					TaxRate taxRate = null;
					for (BaseBasicTableEntity entity : taxRateList)
					{
						TaxRate _rate =  (TaxRate) entity;
						if (taxRate == null && _rate.getPercent() == 0)
						{
							taxRate = _rate;
						}
					}
					resultDetail.setTaxRateId(taxRate.getId()); // 税率id
					resultDetail.setPercent(taxRate.getPercent()); // 税率比
				} else
				{
					TaxRate taxRate = (TaxRate) taxRateList.get(0);
					resultDetail.setTaxRateId(taxRate.getId()); // 税率id
					resultDetail.setPercent(taxRate.getPercent()); // 税率比
				}
			
				resultDetail.setExtOrderId(order.getId());
				resultDetail.setExtOrderDetailId(_detail.getId()); // 外部单据明细id
				resultDetail.setSourceQty(_detail.getBuyNum()); // 源单数量
				resultDetailList.add(resultDetail);
			}
			// ------------------ END一个外部采购单转采购订单的过程 -----------------
		}
		return resultOrder;
	}

	/**
	 * <pre>
	 * 查询外部采购单入库状态
	 * </pre>
	 * @since 1.0, 2018年7月18日 下午2:00:39, zhengby
	 */
	@Override
	public void noticePOStatus()
	{
		/**
		 * 1.每60分钟查询一次
		 * 2.查询条件【时间段】间隔为60分钟
		 * 3.查询出所有符合条件的purchOrderDetail，再根据其extOrderDetailId查出外部采购单
		 * 4.对比外部采购单的全部明细入库数量storageQty : buyNm;
		 */
		String status = SystemConfigUtil.getConfig(SysConstants.EXT_JOB_STATUS);
		String swtch = SystemConfigUtil.getConfig(SysConstants.SWITCH_FOR_YSJ);
		if (status != null && !status.equals(SysConstants.YES) && !swtch.equals(SysConstants.YES))
		{
			logger.warn("请检查参数配置：" + SysConstants.EXT_JOB_STATUS + "-->" + status);
			logger.warn("请检查参数配置：" + SysConstants.SWITCH_FOR_YSJ + "-->" + swtch);
			return;
		}
		logger.info("正在查询入库状态");
		List<ExteriorPurchOrder> orderList = this._operatedList();
		for (ExteriorPurchOrder order : orderList)
		{
			int i = order.getItems().size(); // 订单里明细表的条目数
			int j = 0; // 用于记录完成入库的明细条目数
			for (ExteriorPurchOrderDetail detail : order.getItems())
			{
				// 如果入库数量大于等于购买数量，则完成入库
				if (detail.getStorageQty().compareTo(detail.getBuyNum()) != -1)
				{
					j++;
				}
			}
			// 订单入库状态
			PurchOrderStatusType type = PurchOrderStatusType.ALL_STOCK;
			if (j == 0) // 全部都未入库
			{
				type = PurchOrderStatusType.TO_STOCK;
			}
			else if (j > 0 && j < i) // 部分入库
			{
				type = PurchOrderStatusType.PART_STOCK;
			}
			else if (j == i) // 全部入库
			{
				type = PurchOrderStatusType.ALL_STOCK;
			}
			else
			{
				logger.error("错误：明细表条目：" + i + ",比较条目:" + j);
			}
			// 异步执行
			final String purchaseOrderNm = order.getPurchaseOrderNm();
			final PurchOrderStatusType _type = type;
			threadPool.execute(new Runnable()
			{
				@Override
				public void run()
				{
					noticePOStatus(_type, purchaseOrderNm);
				}
			});
		}
	}

	@Override
	public SearchResult<ExteriorPurchOrder> exteriorPurchList(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(ExteriorPurchOrder.class, "m");
		if (StringUtils.isNoneBlank(queryParam.getBillNo()))
		{
			query.eq("m.orderNm", queryParam.getBillNo());
		}
		if (StringUtils.isNoneBlank(queryParam.getSupplierName()))
		{
			query.like("m.sellerName", "%" + queryParam.getSupplierName() + "%");
		}
		query.isNull("m.purchOrderId");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("m.createTime");
		query.setIsSearchTotalCount(true);

		SearchResult<ExteriorPurchOrder> result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, ExteriorPurchOrder.class);
		return result;
	}

	/**
	 * <pre>
	 * 保存来源于印刷家的采购订单
	 * </pre>
	 * @param purchOrder
	 * @return
	 * @since 1.0, 2018年7月13日 下午5:43:55, zhengby
	 */
	private ExteriorPurchOrder _savePurchOrder(ExteriorPurchOrder purchOrder, User user)
	{
		purchOrder.setCompanyId(user.getCompanyId());
		purchOrder.setUserNo(user.getUserNo());
		purchOrder.setCreateName(user.getUserName());
		purchOrder.setCreateTime(new Date());
		purchOrder = daoFactory.getCommonDao().saveEntity(purchOrder);
		for (ExteriorPurchOrderDetail detail : purchOrder.getItems())
		{
			detail.setCompanyId(user.getCompanyId());
			detail.setMasterId(purchOrder.getId());
			detail.setUserNo(user.getUserNo());
		}
		daoFactory.getCommonDao().saveAllEntity(purchOrder.getItems());

		return purchOrder;
	}

	/**
	 * <pre>
	 * 查询时间范围内有过操作的外部采购单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年7月18日 上午11:40:13, zhengby
	 */
	private List<ExteriorPurchOrder> _operatedList()
	{
		String period = SystemConfigUtil.getConfig(SysConstants.EXT_JOB_PERIOD); // 系统配置参数
		Date now = new Date(); // 当前时间
		Date nowEarly = new Date(); // 查询操作时间范围
		nowEarly.setTime(now.getTime() - Long.valueOf(period));
		DynamicQuery query = new DynamicQuery(ExteriorPurchOrder.class);
		query.le("lastOperatTime", now);
		query.ge("lastOperatTime", nowEarly);
		List<ExteriorPurchOrder> orderList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, ExteriorPurchOrder.class);

		for (ExteriorPurchOrder order : orderList)
		{
			order.setItems(getAllDetailList(order.getId()));
		}
		return orderList;
	}

	/**
	 * <pre>
	 * 通知印刷家采购单的入库状态
	 * </pre>
	 * @param type
	 * @param purchaseOrderNm
	 * @since 1.0, 2018年7月19日 下午5:19:25, zhengby
	 */
	private void noticePOStatus(PurchOrderStatusType type, String purchaseOrderNm)
	{
		// 请求body
		ReqBody body = new ReqBody();
		body.setStatus(type.getValue().toString());
		body.setPurchaseOrderNm(purchaseOrderNm);
		ResponseDto response = ExteriorHelper.buildRequest(body, ActionType.NOTICE_PO_STATUS);
		logger.info("采购单编号：" + purchaseOrderNm + ",处理结果:" + response.getResponseHeader().getRspMsg());
	}
}
