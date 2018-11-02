/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年02月22日 下午17:19:23
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.sale.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.ObjectHelper;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.helper.AbsHelper.CountMap;
import com.huayin.printmanager.helper.service.SaleDeliverHelper;
import com.huayin.printmanager.helper.service.SaleReconcilHelper;
import com.huayin.printmanager.helper.service.SaleReturnHelper;
import com.huayin.printmanager.helper.service.WorkHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.CustomerClass;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.ProcedureClass;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.ProductClass;
import com.huayin.printmanager.persist.entity.basic.Product_Customer;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.persist.entity.offer.OfferOrderQuoteOut;
import com.huayin.printmanager.persist.entity.offer.OfferPart;
import com.huayin.printmanager.persist.entity.offer.OfferPartProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.sale.SaleDeliver;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderMaterial;
import com.huayin.printmanager.persist.entity.sale.SaleOrderPack;
import com.huayin.printmanager.persist.entity.sale.SaleOrderPart;
import com.huayin.printmanager.persist.entity.sale.SaleOrderPart2Product;
import com.huayin.printmanager.persist.entity.sale.SaleOrderProcedure;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturn;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.OrderType;
import com.huayin.printmanager.persist.enumerate.ProductType;
import com.huayin.printmanager.persist.enumerate.ProgressStatusSale;
import com.huayin.printmanager.persist.enumerate.ResultType;
import com.huayin.printmanager.persist.enumerate.SaleMaterialType;
import com.huayin.printmanager.persist.enumerate.SaleProcedureType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sale.SaleOrderService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售订单管理
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月22日下午5:31:34
 */
@Service
public class SaleOrderServiceImpl extends BaseServiceImpl implements SaleOrderService
{
	@Override
	public SaleOrder get(Long id)
	{
		// SaleOrder order = daoFactory.getCommonDao().getEntity(SaleOrder.class, id);
		DynamicQuery query = new CompanyDynamicQuery(SaleOrder.class);
		query.eq("id", id);
		SaleOrder order = daoFactory.getCommonDao().getByDynamicQuery(query, SaleOrder.class);

		// order.setDetailList(this.getDetailList(id));
		return order;
	}

	@Override
	public SaleOrder get(String billNo)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrder.class);
		query.eq("billNo", billNo);
		SaleOrder order = daoFactory.getCommonDao().getByDynamicQuery(query, SaleOrder.class);
		return order;
	}

	public SaleOrder getHasChildren(Long id)
	{
		SaleOrder order = this.get(id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	@Override
	public SaleOrderDetail getDetail(Long id)
	{
		// SaleOrderDetail detail = daoFactory.getCommonDao().getEntity(SaleOrderDetail.class, id);
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class);
		query.eq("id", id);
		SaleOrderDetail bean = daoFactory.getCommonDao().getByDynamicQuery(query, SaleOrderDetail.class);

		if (null != bean)
		{
			// 找出产品图片路径imgUrl
			DynamicQuery imgQuery = new CompanyDynamicQuery(Product.class);
			imgQuery.eq("id", bean.getProductId());
			String imgUrl = daoFactory.getCommonDao().getByDynamicQuery(imgQuery, Product.class).getImgUrl();
			bean.setImgUrl(imgUrl);
			bean.setPartList(this.getPartListByDetailId(id));
			bean.setPack(this.getPackByDetailId(id));
		}
		return bean;
	}

	@Override
	public List<SaleOrderPart> getPartListByDetailId(Long detailId)
	{
		DynamicQuery query_product = new CompanyDynamicQuery(SaleOrderPart.class);
		query_product.eq("masterId", detailId);
		query_product.asc("id");
		List<SaleOrderPart> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query_product, SaleOrderPart.class);

		for (SaleOrderPart part : list)
		{
			part.setMaterialList(this.getSaleMaterialListByPartId(part.getId()));
			part.setProcedureList(this.getProcedureListByPartId(part.getId()));
			part.setProductList(this.getPart2ProductByPartId(part.getId()));
		}
		return list;
	}

	@Override
	public List<SaleOrderMaterial> getSaleMaterialListByPartId(Long partId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderMaterial.class);
		query.eq("saleMaterialType", SaleMaterialType.PART);
		query.eq("parentId", partId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SaleOrderMaterial.class);
	}

	@Override
	public List<SaleOrderProcedure> getProcedureListByDetailId(Long saleDetailId)
	{
		DynamicQuery query_product = new CompanyDynamicQuery(SaleOrderProcedure.class);
		query_product.eq("saleDetailId", saleDetailId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query_product, SaleOrderProcedure.class);
	}

	@Override
	public List<SaleOrderProcedure> getProcedureListByPartId(Long partId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderProcedure.class);
		query.eq("saleProcedureType", SaleProcedureType.PART);
		query.eq("parentId", partId);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SaleOrderProcedure.class);
	}

	@Override
	public List<SaleOrderPart2Product> getPart2ProductByPartId(Long partId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderPart2Product.class);
		query.eq("salePartId", partId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SaleOrderPart2Product.class);
	}

	@Override
	public SaleOrderPack getPackByDetailId(Long detailId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderPack.class);
		query.eq("masterId", detailId);
		SaleOrderPack pack = daoFactory.getCommonDao().getByDynamicQuery(query, SaleOrderPack.class);
		if (null != pack)
		{
			pack.setMaterialList(this.getSaleMaterialListByPackId(pack.getId()));
			pack.setProcedureList(this.getProcedureListByPackId(pack.getId()));
		}
		return pack;
	}

	@Override
	public List<SaleOrderMaterial> getSaleMaterialListByDetaiId(Long detailId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderMaterial.class);
		query.eq("saleDetailId", detailId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SaleOrderMaterial.class);
	}

	@Override
	public List<SaleOrderMaterial> getSaleMaterialListByPackId(Long packId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderMaterial.class);
		query.eq("saleMaterialType", SaleMaterialType.PRODUCT);
		query.eq("parentId", packId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SaleOrderMaterial.class);
	}

	@Override
	public List<SaleOrderProcedure> getProcedureListByPackId(Long packId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderProcedure.class);
		query.eq("saleProcedureType", SaleProcedureType.PRODUCT);
		query.eq("parentId", packId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SaleOrderProcedure.class);
	}

	public SaleOrderDetail getDetailHasMaster(Long id)
	{
		SaleOrderDetail detail = this.getDetail(id);
		detail.setMaster(this.get(detail.getMasterId()));
		return detail;
	}

	@Override
	public SaleOrder lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrder.class);
		query.eq("id", id);
		SaleOrder order = daoFactory.getCommonDao().lockByDynamicQuery(query, SaleOrder.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(SaleOrderDetail.class);
		query_detail.eq("masterId", id);
		List<SaleOrderDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, SaleOrderDetail.class, LockType.LOCK_WAIT);
		// 没有考虑性能查询
		for (SaleOrderDetail bean : detailList)
		{
			bean.setPartList(this.getPartListByDetailId(bean.getId()));
			bean.setPack(this.getPackByDetailId(bean.getId()));
		}
		order.setDetailList(detailList);
		return order;
	}

	@Override
	public List<SaleOrderDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class,"sd");
		query.eq("sd.masterId", id);
		query.createAlias(Product.class, JoinType.LEFTJOIN,"p","p.id=sd.productId");
		query.createAlias(ProductClass.class,JoinType.LEFTJOIN, "pc","pc.id=p.productClassId");
		query.addProjection(Projections.property("sd, pc.productType as productType, p.fileName as fileName"));
		SearchResult<Object[]> result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		List<SaleOrderDetail> detailList = new ArrayList<>(); 
		for (Object[] obj : result.getResult())
		{
			SaleOrderDetail detail = (SaleOrderDetail) obj[0]; 
			//设置产品类型
			ProductType productType = (ProductType) obj[1];
			detail.setProductType(productType);
			// 设置产品图片路径
			String fileName = (String) obj[2];
			Product product = new Product();
			product.setFileName(fileName);
			detail.setImgUrl(product.getImgUrl());
			detailList.add(detail);
		}
		return detailList;
	}
	
	@Override
	public SaleOrder getLatestOrderHasDetail ()
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrder.class);
		query.setPageSize(5);
		query.setPageIndex(1);
		query.eq("isCheck", BoolValue.YES);
		query.eq("isCancel", BoolValue.NO);
		query.desc("createTime");
		SearchResult<SaleOrder> result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, SaleOrder.class);
		SaleOrder order = result.getResult().get(0);
		if(null != order)
		{
			order.setDetailList(getDetailList(order.getId()));
		}
		return order;
	}
	
	@Override
	@Transactional
	public SaleOrder save(SaleOrder order)
	{
		// 报价订单id
		List<Long> offerOrderIdList = order.getOfferOrderIdList();
		BoolValue flag = order.getIsCheck();
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.SALE_SO);
		order.setBillNo(UserUtils.createBillNo(BillType.SALE_SO));
		order.setProgressStatus(ProgressStatusSale.NO_PRODUCE);
		order.setUserNo(UserUtils.getUser().getUserNo());
		// 创建人优先取员工姓名
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			order.setCreateName(e.getName());
		}
		else
		{
			order.setCreateName(UserUtils.getUserName());
		}
		order.setCreateTime(new Date());
		order.setCreateEmployeeId(UserUtils.getEmployeeId());
		order.setIsCheck(BoolValue.NO);
		order.setIsForceComplete(BoolValue.NO);
		order = daoFactory.getCommonDao().saveEntity(order);
		for (SaleOrderDetail detail : order.getDetailList())
		{
			_newSaleOrderDetail(detail, order.getId(), true);// 构造新增SaleOrderDetail对象
		}
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());

		// 先保存DetailList
		for (SaleOrderDetail detail : order.getDetailList())
		{
			_saveDetailProcedureMaterial(detail);// 保存销售部件和成品的工序和材料
		}

		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			if (serviceFactory.getCommonService().audit(BillType.SALE_SO, order.getId(), BoolValue.YES))
			{
			  // 查找出最新创建的订单
			  SaleOrder latestOrder = this.getLatestOrderHasDetail();
			  // 判断先当前保存的订单是否是最新创建的订单
			  if (null != latestOrder && latestOrder.getId() == order.getId())
			  {
			  	// 销售订单审核后，对应的产品的销售单价反写到基础资料里产品信息里对应的产品的单价
					Map<Long, BigDecimal> map = new HashMap<>();
				  for (SaleOrderDetail detail : order.getDetailList())
				  {
				  	map.put(detail.getProductId(), detail.getPrice());
				  }
				  serviceFactory.getProductService().updatePrice(map);
			  }
			}
		}

		// 更新报价系统的引用
		if (offerOrderIdList != null)
		{
			for (Long id : offerOrderIdList)
			{
				OfferOrder _order = serviceFactory.getOfferOrderService().getOrder(id);
				_order.setBillNo(order.getBillNo());
				_order.setBillType(order.getBillType());
				_order.setSaleId(order.getId());
				serviceFactory.getOfferOrderService().updateOrder(_order);
			}
		}

		return order;
	}
	
	/**
	 * <pre>
	 * 构造新增SaleOrderDetail对象
	 * </pre>
	 * @param detail
	 * @param masterId
	 */
	private void _newSaleOrderDetail(SaleOrderDetail detail, Long masterId, boolean isAdd)
	{
		if (isAdd)
		{
			detail.setId(null);
		}
		detail.setProduceedQty(0);
		detail.setProduceSpareedQty(0);
		detail.setDeliverQty(0);
		detail.setDeliverMoney(new BigDecimal(0));
		detail.setDeliverSpareedQty(0);
		detail.setIsForceComplete(BoolValue.NO);
		detail.setMasterId(masterId);
		detail.setCompanyId(UserUtils.getCompanyId());
		TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), detail.getTaxRateId());
		detail.setPercent(taxRate.getPercent());
		detail.setUserNo(UserUtils.getUser().getUserNo());
	}

	/**
	 * <pre>
	 * 构造新增SaleOrderPart对象
	 * </pre>
	 * @param part
	 * @param masterId
	 */
	private void _newSaleOrderPart(SaleOrderPart part, Long masterId, boolean isAdd)
	{
		if (isAdd)
		{
			part.setId(null);
		}
		part.setCompanyId(UserUtils.getCompanyId());
		part.setUserNo(UserUtils.getUser().getUserNo());
		part.setMasterId(masterId);
		part.setIsForceComplete(BoolValue.NO);
	}

	/**
	 * <pre>
	 * 构造新增SaleOrderPart2Product对象
	 * </pre>
	 * @param p2p
	 * @param partId
	 * @param saleDetailId
	 */
	private void _newSaleOrderPart2Product(SaleOrderPart2Product p2p, Long partId, Long saleDetailId, boolean isAdd)
	{
		if (isAdd)
		{
			p2p.setId(null);
		}
		p2p.setCompanyId(UserUtils.getCompanyId());
		p2p.setSaleDetailId(saleDetailId);
		p2p.setSalePartId(partId);
	}

	/**
	 * <pre>
	 * 构造新增SaleOrderMaterial对象
	 * </pre>
	 * @param material
	 * @param parentId
	 * @param saleDetailId
	 */
	private void _newSaleOrderMaterial(SaleOrderMaterial material, Long parentId, Long saleDetailId, SaleMaterialType type, boolean isAdd)
	{
		if (isAdd)
		{
			material.setId(null);
		}
		material.setCompanyId(UserUtils.getCompanyId());
		material.setSaleDetailId(saleDetailId);
		material.setSaleMaterialType(type);
		material.setParentId(parentId);
		material.setIsForceComplete(BoolValue.NO);
		material.setPurchQty(new BigDecimal(0));
		material.setTakeQty(new BigDecimal(0));
		material.setIsNotTake(BoolValue.NO);
		material.setIsNotPurch(BoolValue.NO);
	}

	/**
	 * <pre>
	 * 构造新增SaleOrderProcedure对象
	 * </pre>
	 * @param procedure
	 * @param parentId
	 * @param saleDetailId
	 */
	private void _newSaleOrderProcedure(SaleOrderProcedure procedure, Long parentId, Long saleDetailId, SaleProcedureType type, boolean isAdd)
	{
		procedure.setId(null);
		procedure.setCompanyId(UserUtils.getCompanyId());
		procedure.setSaleDetailId(saleDetailId);
		procedure.setSaleProcedureType(type);
		procedure.setParentId(parentId);
		procedure.setIsForceComplete(BoolValue.NO);
		procedure.setOutOfQty(new BigDecimal(0));
		procedure.setArriveOfQty(new BigDecimal(0));
	}

	/**
	 * <pre>
	 * 保存销售部件和成品的工序和材料
	 * </pre>
	 * @param detail
	 */
	private void _saveDetailProcedureMaterial(SaleOrderDetail detail)
	{
		_savePart(detail, true);// 保存部件工序和材料
		_savePack(detail, true);// 保存成品工序和材料
	}

	/**
	 * <pre>
	 * 保存销售部件的工序和材料信息
	 * </pre>
	 * @param detail
	 */
	private void _savePart(SaleOrderDetail detail, boolean isAdd)
	{
		// 保存部件列表信息
		if (detail.getPartList() != null)
		{
			for (SaleOrderPart part : detail.getPartList())
			{
				_newSaleOrderPart(part, detail.getId(), isAdd);
				part = daoFactory.getCommonDao().saveEntity(part);

				for (SaleOrderPart2Product p2p : part.getProductList())
				{
					_newSaleOrderPart2Product(p2p, part.getId(), detail.getId(), isAdd);
				}
				daoFactory.getCommonDao().saveAllEntity(part.getProductList());
				for (SaleOrderMaterial material : part.getMaterialList())
				{
					_newSaleOrderMaterial(material, part.getId(), detail.getId(), SaleMaterialType.PART, isAdd);
				}
				daoFactory.getCommonDao().saveAllEntity(part.getMaterialList());

				for (SaleOrderProcedure procedure : part.getProcedureList())
				{
					_newSaleOrderProcedure(procedure, part.getId(), detail.getId(), SaleProcedureType.PART, isAdd);
				}
				daoFactory.getCommonDao().saveAllEntity(part.getProcedureList());
			}
		}
	}

	/**
	 * <pre>
	 * 保存销售成品的工序和材料信息
	 * </pre>
	 * @param detail
	 */
	private void _savePack(SaleOrderDetail detail, boolean isAdd)
	{
		// 保存成品信息
		if (detail.getPack() != null)
		{
			if (isAdd)
			{
				detail.getPack().setId(null);
			}
			detail.getPack().setCompanyId(UserUtils.getCompanyId());
			detail.getPack().setMasterId(detail.getId());
			detail.getPack().setUserNo(UserUtils.getUser().getUserNo());
			detail.getPack().setIsForceComplete(BoolValue.NO);
			SaleOrderPack pack = daoFactory.getCommonDao().saveEntity(detail.getPack());
			for (SaleOrderMaterial material : detail.getPack().getMaterialList())
			{
				_newSaleOrderMaterial(material, pack.getId(), detail.getId(), SaleMaterialType.PRODUCT, isAdd);
			}
			daoFactory.getCommonDao().saveAllEntity(detail.getPack().getMaterialList());

			for (SaleOrderProcedure procedure : detail.getPack().getProcedureList())
			{
				_newSaleOrderProcedure(procedure, pack.getId(), detail.getId(), SaleProcedureType.PRODUCT, isAdd);
			}
			daoFactory.getCommonDao().saveAllEntity(detail.getPack().getProcedureList());
		}
	}

	@Override
	@Transactional
	public SaleOrder update(SaleOrder order)
	{
		// 需要删除的集合
		List<SaleOrderPart> del_part_list = new ArrayList<SaleOrderPart>();
		List<SaleOrderPart2Product> del_part2product_list = new ArrayList<SaleOrderPart2Product>();
		List<SaleOrderMaterial> del_material_list = new ArrayList<SaleOrderMaterial>();

		// 需要新增的集合
		List<SaleOrderPart2Product> add_part2product_list = new ArrayList<SaleOrderPart2Product>();
		List<SaleOrderMaterial> add_material_list = new ArrayList<SaleOrderMaterial>();
		List<SaleOrderProcedure> add_procedure_list = new ArrayList<SaleOrderProcedure>();

		// 需要更新的集合
		List<SaleOrderPart> update_part_list = new ArrayList<SaleOrderPart>();
		List<SaleOrderPart2Product> update_part2product_list = new ArrayList<SaleOrderPart2Product>();
		List<SaleOrderMaterial> update_material_list = new ArrayList<SaleOrderMaterial>();

		BoolValue flag = order.getIsCheck();
		order.setIsCheck(BoolValue.NO);
		SaleOrder old_order = serviceFactory.getSaleOrderService().lockHasChildren(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		Map<Long, SaleOrderDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();

		List<SaleOrderDetail> del_detail = new ArrayList<SaleOrderDetail>();

		for (SaleOrderDetail detail : old_order.getDetailList())
		{
			old_detailIds.add(detail.getId());
		}
		for (SaleOrderDetail new_detail : order.getDetailList())
		{
			if (new_detail.getId() != null)
			{
				new_detailIds.add(new_detail.getId());
			}
		}

		for (SaleOrderDetail new_detail : order.getDetailList())
		{
			if (new_detail.getId() == null)
			{// 新增
				_newSaleOrderDetail(new_detail, order.getId(), true);// 构造新增SaleOrderDetail对象
				daoFactory.getCommonDao().saveEntity(new_detail);

				// 保存销售部件和成品的工序和材料
				_saveDetailProcedureMaterial(new_detail);
			}
			else
			{
				// 清空数据
				del_part2product_list.clear();
				del_material_list.clear();
				del_part_list.clear();

				if (old_detailIds.contains(new_detail.getId()))
				{
					// 删除所有工序信息
					if (new_detail.getPartList() != null && new_detail.getPartList().size() > 0)
					{
						daoFactory.getCommonDao().deleteAllEntity(this.getProcedureListByDetailId(new_detail.getId()));
					}
					// 更新
					SaleOrderDetail old_detail = old_detail_map.get(new_detail.getId());

					// ----------------------------部件更新-----------------------------------------------------------
					// 更新删除掉的部件信息
					Map<Long, SaleOrderPart> old_part_map = new HashMap<Long, SaleOrderPart>();
					if (old_detail.getPartList() != null)
					{
						for (SaleOrderPart object : old_detail.getPartList())
						{
							old_part_map.put(object.getId(), object);
						}
					}

					List<Long> old_part_Ids = new ArrayList<Long>();
					List<Long> new_part_Ids = new ArrayList<Long>();

					for (SaleOrderPart part : old_detail.getPartList())
					{
						old_part_Ids.add(part.getId());
					}
					for (SaleOrderPart new_part : new_detail.getPartList())
					{
						if (new_part.getId() != null)
						{
							new_part_Ids.add(new_part.getId());
						}
					}
					for (Long id : old_part_Ids)
					{
						// new_part_Ids不能为空,否则默认只更新Detail
						if (!new_part_Ids.contains(id) && new_part_Ids.size() > 0)
						{
							SaleOrderPart old_part = old_part_map.get(id);
							del_part_list.add(old_part);
							del_part2product_list.addAll(old_part.getProductList());
							del_material_list.addAll(old_part.getMaterialList());
						}
						// 独立处理，清空所有部件（多条记录时，页面保存时需要单独计算清空所有部件，TODO 后面需要优化整个业务逻辑，比较难理解）
						else if (null != new_detail.getPartTruncate() && new_detail.getPartTruncate() == true)
						{
							SaleOrderPart old_part = old_part_map.get(id);
							del_part_list.add(old_part);
							del_part2product_list.addAll(old_part.getProductList());
							del_material_list.addAll(old_part.getMaterialList());
						}
					}

					// 保存部件列表信息
					for (SaleOrderPart part : new_detail.getPartList())
					{
						if (part.getId() == null)
						{// 新增
							_newSaleOrderPart(part, new_detail.getId(), true);// 构造新增SaleOrderPart对象
							SaleOrderPart new_part = daoFactory.getCommonDao().saveEntity(part);
							for (SaleOrderPart2Product p2p : part.getProductList())
							{
								_newSaleOrderPart2Product(p2p, new_part.getId(), new_detail.getId(), true);// 构造新增SaleOrderPart2Product对象
								add_part2product_list.add(p2p);
							}
							for (SaleOrderMaterial material : part.getMaterialList())
							{
								_newSaleOrderMaterial(material, new_part.getId(), new_detail.getId(), SaleMaterialType.PART, true);// 构造新增SaleOrderMaterial对象
								add_material_list.add(material);
							}
							for (SaleOrderProcedure procedure : part.getProcedureList())
							{
								_newSaleOrderProcedure(procedure, new_part.getId(), new_detail.getId(), SaleProcedureType.PART, true);// 构造新增SaleOrderProcedure对象
								add_procedure_list.add(procedure);
							}
						}
						else
						{
							if (old_part_Ids.contains(part.getId()))
							{// 更新
								SaleOrderPart old_part = old_part_map.get(part.getId());

								PropertyClone.copyProperties(old_part, part, false, new String[] { "materialList", "procedureList", "productList" });// 替换成新内容
								update_part_list.add(old_part);

								// -----------------------部件产品更新----------------------------------------------
								List<SaleOrderPart2Product> old_p2p_list = old_part_map.get(part.getId()).getProductList();
								Map<Long, SaleOrderPart2Product> old_p2p_map = ConverterUtils.list2Map(old_p2p_list, "id");
								List<Long> old_p2p_Ids = new ArrayList<Long>();
								List<Long> new_p2p_Ids = new ArrayList<Long>();
								for (SaleOrderPart2Product p2p : old_p2p_list)
								{
									old_p2p_Ids.add(p2p.getId());
								}
								for (SaleOrderPart2Product new_p2p : part.getProductList())
								{
									if (new_p2p.getId() != null)
									{
										new_p2p_Ids.add(new_p2p.getId());
									}
								}
								for (Long id : old_p2p_Ids)
								{
									if (!new_p2p_Ids.contains(id))
									{
										SaleOrderPart2Product old_p2p = old_p2p_map.get(id);
										del_part2product_list.add(old_p2p);
									}
								}
								for (SaleOrderPart2Product new_p2p : part.getProductList())
								{
									if (new_p2p.getId() == null)
									{// 新增
										new_p2p.setCompanyId(order.getCompanyId());
										new_p2p.setSaleDetailId(new_detail.getId());
										new_p2p.setSalePartId(part.getId());
										add_part2product_list.add(new_p2p);
									}
									else
									{
										if (old_p2p_Ids.contains(new_p2p.getId()))
										{// 更新
											SaleOrderPart2Product old_p2p = old_p2p_map.get(new_p2p.getId());
											PropertyClone.copyProperties(old_p2p, new_p2p, false);
											update_part2product_list.add(old_p2p);
										}
									}
								}
								// -----------------------部件材料----------------------------------------------
								List<SaleOrderMaterial> old_material_list = old_part_map.get(part.getId()).getMaterialList();
								Map<Long, SaleOrderMaterial> old_material_map = ConverterUtils.list2Map(old_material_list, "id");
								List<Long> old_material_Ids = new ArrayList<Long>();
								List<Long> new_material_Ids = new ArrayList<Long>();
								for (SaleOrderMaterial material : old_material_list)
								{
									old_material_Ids.add(material.getId());
								}
								for (SaleOrderMaterial new_material : part.getMaterialList())
								{
									if (new_material.getId() != null)
									{
										new_material_Ids.add(new_material.getId());
									}
								}
								for (Long id : old_material_Ids)
								{
									if (!new_material_Ids.contains(id))
									{
										SaleOrderMaterial old_material = old_material_map.get(id);
										del_material_list.add(old_material);
									}
								}
								for (SaleOrderMaterial new_material : part.getMaterialList())
								{
									if (new_material.getId() == null)
									{// 新增
										_newSaleOrderMaterial(new_material, part.getId(), new_detail.getId(), SaleMaterialType.PART, true);// 构造新增SaleOrderMaterial对象
										add_material_list.add(new_material);
									}
									else
									{
										if (old_material_Ids.contains(new_material.getId()))
										{// 更新
											SaleOrderMaterial old_material = old_material_map.get(new_material.getId());
											PropertyClone.copyProperties(old_material, new_material, false);
											update_material_list.add(old_material);
										}
									}
								}
								// -----------------------部件工序----------------------------------------------
								for (SaleOrderProcedure procedure : part.getProcedureList())
								{
									_newSaleOrderProcedure(procedure, part.getId(), new_detail.getId(), SaleProcedureType.PART, true);// 构造新增SaleOrderProcedure对象
									add_procedure_list.add(procedure);
								}
							}
						}
					}
					
					// ----------------------------成品更新-----------------------------------------------------------
					// 更新成品信息
					{
						if (new_detail.getPack() != null)
						{
							// 新增成品（历史数据都会进入该方法）
							if (old_detail.getPack() == null)
							{
								_savePack(new_detail, true);
							}
							// 更新成品
							else
							{
								List<SaleOrderMaterial> old_material_list = Lists.newArrayList();
								if (old_detail.getPack() != null)
								{
									old_material_list = old_detail.getPack().getMaterialList();
								}
								Map<Long, SaleOrderMaterial> old_material_map = ConverterUtils.list2Map(old_material_list, "id");
								List<Long> old_material_Ids = new ArrayList<Long>();
								List<Long> new_material_Ids = new ArrayList<Long>();
								for (SaleOrderMaterial material : old_material_list)
								{
									old_material_Ids.add(material.getId());
								}
								for (SaleOrderMaterial new_material : new_detail.getPack().getMaterialList())
								{
									if (new_material.getId() != null)
									{
										new_material_Ids.add(new_material.getId());
									}
								}
								for (Long id : old_material_Ids)
								{
									if (!new_material_Ids.contains(id))
									{
										SaleOrderMaterial old_material = old_material_map.get(id);
										del_material_list.add(old_material);
									}
								}
								for (SaleOrderMaterial new_material : new_detail.getPack().getMaterialList())
								{
									if (new_material.getId() == null)
									{// 新增
										_newSaleOrderMaterial(new_material, old_detail.getPack().getId(), old_detail.getId(), SaleMaterialType.PRODUCT, true);// 构造新增SaleOrderMaterial对象
										add_material_list.add(new_material);
									}
									else
									{
										if (old_material_Ids.contains(new_material.getId()))
										{// 更新
											SaleOrderMaterial old_material = old_material_map.get(new_material.getId());
											PropertyClone.copyProperties(old_material, new_material, false);
											update_material_list.add(old_material);
										}
									}
								}
								for (SaleOrderProcedure procedure : new_detail.getPack().getProcedureList())
								{
									_newSaleOrderProcedure(procedure, old_detail.getPack().getId(), old_detail.getId(), SaleProcedureType.PRODUCT, true);// 构造新增SaleOrderProcedure对象
									add_procedure_list.add(procedure);
								}
							}
						}
					}

					daoFactory.getCommonDao().deleteAllEntity(del_part2product_list);
					daoFactory.getCommonDao().deleteAllEntity(del_material_list);
					daoFactory.getCommonDao().deleteAllEntity(del_part_list);

					daoFactory.getCommonDao().saveAllEntity(add_part2product_list);
					daoFactory.getCommonDao().saveAllEntity(add_material_list);
					daoFactory.getCommonDao().saveAllEntity(add_procedure_list);

					daoFactory.getCommonDao().updateAllEntity(update_part_list);
					daoFactory.getCommonDao().updateAllEntity(update_part2product_list);
					daoFactory.getCommonDao().updateAllEntity(update_material_list);

					SaleOrderPack old_pack = old_detail.getPack();
					if (null != old_pack && null != new_detail.getPack())
					{
						PropertyClone.copyProperties(old_pack, new_detail.getPack(), false);
						daoFactory.getCommonDao().updateEntity(old_pack);
					}

					// 更新主表信息
					PropertyClone.copyProperties(old_detail, new_detail, false, null, new String[] { "memo" });// 替换成新内容
					daoFactory.getCommonDao().updateEntity(old_detail);// 更新子表
				}
			}
		}

		// 删除操作
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))
			{
				SaleOrderDetail old_detail = old_detail_map.get(id);
				del_detail.add(old_detail);
			}
		}

		// 删除对应得部件、工序和材料
		_delete(del_detail);

		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "deliveryAddress" });// 替换新内容
		order.setUpdateName(UserUtils.getUserName());
		order.setUpdateTime(new Date());
		// 必须先保存子表，然后再保存主表
		daoFactory.getCommonDao().updateEntity(old_order);// 更新主表
		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			if (serviceFactory.getCommonService().audit(BillType.SALE_SO, order.getId(), BoolValue.YES))
			{
				 // 查找出最新创建的订单
			  SaleOrder latestOrder = this.getLatestOrderHasDetail();
			  // 判断先当前保存的订单是否是最新创建的订单
			  if (null != latestOrder && latestOrder.getId() == order.getId())
			  {
			  	// 销售订单审核后，对应的产品的销售单价反写到基础资料里产品信息里对应的产品的单价
					Map<Long, BigDecimal> map = new HashMap<>();
				  for (SaleOrderDetail detail : order.getDetailList())
				  {
				  	map.put(detail.getProductId(), detail.getPrice());
				  }
				  serviceFactory.getProductService().updatePrice(map);
			  }
			};
		}

		return order;
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		List<SaleOrderDetail> detailList = this.getDetailList(id);

		// 删除对应得部件、工序和材料
		_delete(detailList);
		// 删除销售订单
		daoFactory.getCommonDao().deleteEntity(this.get(id));
		// 删除报价系统引用
		List<OfferOrder> offerOrderList = serviceFactory.getOfferOrderService().findBySaleId(id);
		for (OfferOrder offerOrder : offerOrderList)
		{
			offerOrder.setBillNo(null);
			offerOrder.setBillType(null);
			offerOrder.setSaleId(null);
		}
		daoFactory.getCommonDao().updateAllEntity(offerOrderList);
	}

	/**
	 * <pre>
	 * 删除销售详情（需要删除对应的工序信息）
	 * </pre>
	 * @param detailList
	 */
	private void _delete(List<SaleOrderDetail> detailList)
	{
		for (SaleOrderDetail detail : detailList)
		{
			List<SaleOrderPart> list = this.getPartListByDetailId(detail.getId());
			for (SaleOrderPart part : list)
			{
				daoFactory.getCommonDao().deleteAllEntity(this.getPart2ProductByPartId(part.getId())); // 删除产品信息
				daoFactory.getCommonDao().deleteAllEntity(this.getSaleMaterialListByPartId(part.getId()));// 删除材料（部件）
			}
			SaleOrderDetail oldDetail = this.getDetail(detail.getId());
			daoFactory.getCommonDao().deleteAllEntity(oldDetail.getPartList()); // 删除部件
			daoFactory.getCommonDao().deleteAllEntity(this.getProcedureListByDetailId(detail.getId())); // 删除工序
			if (null != oldDetail.getPack())
			{
				daoFactory.getCommonDao().deleteAllEntity(this.getSaleMaterialListByPackId(oldDetail.getPack().getId()));// 删除材料（成品）
				daoFactory.getCommonDao().deleteEntity(oldDetail.getPack());// 删除关系表
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(detailList);// 删除所有详情
	}

	@Override
	@Transactional
	public boolean checkAll()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(SaleOrder.class);
			query.eq("isCheck", BoolValue.NO);
			query.eq("isForceComplete", BoolValue.NO);
			List<SaleOrder> saleOrderlList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, SaleOrder.class);
			for (SaleOrder saleOrder : saleOrderlList)
			{
				saleOrder.setIsCheck(BoolValue.YES);
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public SearchResult<SaleOrderDetail> quickFindForWorkByCondition(QueryParam queryParam)
	{
		// 获取客户分类下的客户id
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class, "a");
		query.addProjection(Projections.property("a, b,d,c"));
		query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "cc", "b.customerId=cc.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("cc.employeeId", employes);
		}
		query.createAlias(Product.class, JoinType.LEFTJOIN, "c", "a.productId=c.id");
		query.createAlias(ProductClass.class, JoinType.LEFTJOIN, "d", "c.productClassId=d.id");
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.isCancel", BoolValue.NO);

		query.add(Restrictions.gtProperty("a.qty", "a.produceedQty"));// 销售数>生产数
		query.add(Restrictions.gtProperty("a.qty", "a.deliverQty"));// 销售数>送货
		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("a.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (CollectionUtils.isNotEmpty(queryParam.getIds()))
		{
			query.in("a.id", queryParam.getIds());
		}
		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("a.style", "%" + queryParam.getProductStyle() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("b.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (queryParam.getDeliverDateMin() != null)
		{
			query.ge("a.deliveryTime", queryParam.getDeliverDateMin());
		}
		if (queryParam.getDeliverDateMax() != null)
		{
			query.le("a.deliveryTime", queryParam.getDeliverDateMax());
		}

		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getProductType() != null && !queryParam.getProductType().equals(ProductType.ROTARY))
		{
			query.eq("d.productType", queryParam.getProductType());
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("cc.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getCompleteFlag() == BoolValue.YES)
		{
			query.eq("a.isForceComplete", BoolValue.YES);// 工单是否强制完工

		}
		else
		{
			query.eq("a.isForceComplete", BoolValue.NO);// 工单是否强制完工
			query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		}
		query.eq("b.orderType", OrderType.NORMAL);
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.eq("cc.companyId", UserUtils.getCompanyId());
		query.eq("c.companyId", UserUtils.getCompanyId());
		query.eq("d.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<SaleOrderDetail> result = new SearchResult<SaleOrderDetail>();
		result.setResult(new ArrayList<SaleOrderDetail>());
		for (Object[] c : temp_result.getResult())
		{
			SaleOrderDetail detail = (SaleOrderDetail) c[0];
			ProductClass productClass = (ProductClass) c[2];
			Product product = (Product) c[3];
			detail.setImgUrl(product.getImgUrl());
			detail.setProductType(productClass.getProductType());
			detail.setMaster((SaleOrder) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;

	}

	@Override
	public List<SaleOrder> getYearsFromSaleOrder()
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrder.class);
		query.addProjection(Projections.property("distinct DATE_FORMAT(createTime,'%Y') as name,DATE_FORMAT(createTime,'%Y') as value"));
		query.eq("isCheck", BoolValue.YES);
		query.eq("companyId", UserUtils.getCompanyId());
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SaleOrder.class);
	}

	@Override
	public SearchResult<SumVo> sumSaleOrderByCustomer(QueryParam queryParam, String type)
	{
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class, "d");
		query.addProjection(Projections.property("new com.huayin.printmanager.service.vo.SumVo(" + "d.id," + (type.equals("name") ? "c.name as name," : type.equals("class") ? "cc.name as name," : "") + "SUM(d.money) as sumMoney ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-01' THEN d.money ELSE 0 END ) as january," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-02' THEN d.money ELSE 0 END ) as february," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-03' THEN d.money ELSE 0 END ) as march," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear()
				+ "-04' THEN d.money ELSE 0 END ) as april," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-05' THEN d.money ELSE 0 END ) as may," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-06' THEN d.money ELSE 0 END ) as june," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-07' THEN d.money ELSE 0 END ) as july," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-08' THEN d.money ELSE 0 END ) as august," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-09' THEN d.money ELSE 0 END ) as september,"
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-10' THEN d.money ELSE 0 END ) as october," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-11' THEN d.money ELSE 0 END ) as november," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-12' THEN d.money ELSE 0 END ) as december" + ")"));
		query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "a", "a.id=d.masterId");
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=a.customerId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (type.equals("class"))
		{
			query.createAlias(CustomerClass.class, JoinType.LEFTJOIN, "cc", "cc.id=c.customerClassId");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (queryParam.getCustomerClassId() != null)
		{
			query.eq("cc.id", queryParam.getCustomerClassId());
		}
		if (type.equals("name"))
		{
			query.isNotNull("c.name");
		}
		else if (type.equals("class"))
		{
			query.isNotNull("a.customerId");
		}
		query.eq("a.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("DATE_FORMAT(a.createTime,'%Y')", queryParam.getYear());
		if (type.equals("name"))
		{
			// 按客户名称
			query.addGourp("a.customerId");
		}
		else if (type.equals("class"))
		{
			// 按客户分类
			query.addGourp("cc.id");
		}

		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		// query.setQueryType(QueryType.JDBC);
		result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, SumVo.class);

		/*
		 * List<SumVo> list = new ArrayList<SumVo>(); for (Map<String, Object> map : mapResult.getResult()) { SumVo vo = new
		 * SumVo(); try { vo = ObjectHelper.mapToObject(map, SumVo.class); } catch (Exception e) { e.printStackTrace(); }
		 * list.add(vo); } result.setResult(list); result.setCount(list.size());
		 */
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SumVo> sumSaleOrderByProduct(QueryParam queryParam, String type)
	{
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class, "d");
		query.addProjection(Projections.property(("d.id," + (type.equals("name") ? "d.productName as name," : type.equals("class") ? "cc.name as name," : "")) + "SUM(d.money) as 'sumMoney' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-01' THEN d.money ELSE 0 END ) as 'january' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-02' THEN d.money ELSE 0 END ) as 'february' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-03' THEN d.money ELSE 0 END ) as 'march' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-04' THEN d.money ELSE 0 END ) as 'april' ,"
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-05' THEN d.money ELSE 0 END ) as 'may' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-06' THEN d.money ELSE 0 END ) as 'june' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-07' THEN d.money ELSE 0 END ) as 'july' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-08' THEN d.money ELSE 0 END ) as 'august' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-09' THEN d.money ELSE 0 END ) as 'september' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear()
				+ "-10' THEN d.money ELSE 0 END ) as 'october' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-11' THEN d.money ELSE 0 END ) as 'november' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-12' THEN d.money ELSE 0 END ) as 'december'"));
		query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "a", "a.id=d.masterId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "customer", "customer.id=a.customerId");
			query.inArray("customer.employeeId", employes);
		}
		if (type.equals("class"))
		{
			query.createAlias(Product.class, JoinType.LEFTJOIN, "c", "c.id=d.productId");
			query.createAlias(ProductClass.class, JoinType.LEFTJOIN, "cc", "cc.id=c.productClassId");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("d.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getProductClassId() != null)
		{
			query.eq("cc.id", queryParam.getProductClassId());
		}
		if (type.equals("name"))
		{
			query.isNotNull("d.productName");
		}
		else if (type.equals("class"))
		{
			query.isNotNull("d.productId");
		}
		query.eq("a.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("DATE_FORMAT(a.createTime,'%Y')", queryParam.getYear());
		if (type.equals("name"))
		{
			// 按客户名称
			query.addGourp("d.productId");
		}
		else if (type.equals("class"))
		{
			// 按客户分类
			query.addGourp("cc.id");
		}

		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SumVo> sumSaleOrderBySeller(QueryParam queryParam)
	{
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class, "d");
		query.addProjection(Projections.property("d.id,e.name as name," + "SUM(d.money) as 'sumMoney' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-01' THEN d.money ELSE 0 END ) as 'january' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-02' THEN d.money ELSE 0 END ) as 'february' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-03' THEN d.money ELSE 0 END ) as 'march' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-04' THEN d.money ELSE 0 END ) as 'april' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear()
				+ "-05' THEN d.money ELSE 0 END ) as 'may' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-06' THEN d.money ELSE 0 END ) as 'june' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-07' THEN d.money ELSE 0 END ) as 'july' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-08' THEN d.money ELSE 0 END ) as 'august' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-09' THEN d.money ELSE 0 END ) as 'september' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-10' THEN d.money ELSE 0 END ) as 'october' ,"
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-11' THEN d.money ELSE 0 END ) as 'november' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-12' THEN d.money ELSE 0 END ) as 'december'"));
		query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "a", "a.id=d.masterId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=a.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.createAlias(Employee.class, JoinType.LEFTJOIN, "e", "e.id=a.employeeId");
		if (queryParam.getEmployeeId() != null)
		{
			query.eq("a.employeeId", queryParam.getEmployeeId());
		}
		query.isNotNull("e.name");
		query.eq("a.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("DATE_FORMAT(a.createTime,'%Y')", queryParam.getYear());
		query.addGourp("a.employeeId");
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}

	@Override
	public SearchResult<SaleOrderDetail> queryPagDetailList(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class, "detail");
		query.eq("detail.masterId", queryParam.getId());
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setQueryType(QueryType.JDBC);
		SearchResult<SaleOrderDetail> result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, SaleOrderDetail.class);
		return result;
	}

	@Override
	@Transactional
	public void changePrice(SaleOrderDetail saleOrderDetail) throws Exception
	{
		SaleOrderDetail detail = daoFactory.getCommonDao().getEntity(SaleOrderDetail.class, saleOrderDetail.getId());
		if (detail != null)
		{
			SaleOrderDetail detail_tmp = (SaleOrderDetail) BeanUtils.cloneBean(detail);

			detail.setPrice(saleOrderDetail.getPrice());
			detail.setMoney(saleOrderDetail.getMoney());

			// 不含税金额
			BigDecimal noTaxMoney = saleOrderDetail.getMoney().divide(new BigDecimal(1d + detail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
			// 税额
			BigDecimal tax = detail.getMoney().subtract(noTaxMoney);
			// 不含税单价
			BigDecimal noTaxPrice = noTaxMoney.divide(new BigDecimal(detail.getQty()), 4, BigDecimal.ROUND_UP);
			// 已送货金额
			BigDecimal deliverMoney = saleOrderDetail.getPrice().multiply(new BigDecimal(detail.getDeliverQty()));

			detail.setNoTaxMoney(noTaxMoney);
			detail.setNoTaxPrice(noTaxPrice);
			detail.setTax(tax);
			detail.setDeliveryTime(saleOrderDetail.getDeliveryTime());
			detail.setDeliverMoney(deliverMoney);
			daoFactory.getCommonDao().updateEntity(detail);
			// 修改销售订单主表数据
			SaleOrder saleOrder = daoFactory.getCommonDao().getEntity(SaleOrder.class, detail.getMasterId());
			BigDecimal totalMoney = detail.getMoney().subtract(detail_tmp.getMoney()).add(saleOrder.getTotalMoney());
			BigDecimal totalTax = detail.getTax().subtract(detail_tmp.getTax()).add(saleOrder.getTotalTax());
			BigDecimal noTaxTotalMoney = detail.getNoTaxMoney().subtract(detail_tmp.getNoTaxMoney()).add(saleOrder.getNoTaxTotalMoney());
			saleOrder.setTotalMoney(totalMoney);
			saleOrder.setNoTaxTotalMoney(noTaxTotalMoney);
			saleOrder.setTotalTax(totalTax);
			daoFactory.getCommonDao().updateEntity(saleOrder);

			// 此笔销售订单明细里的产品的下游单据的，单价，金额，税额，不含税金额，不含税单价也需要重新计算并保存，单价，
			// 金额更新的下游单据有：生产工单，销售送货，销售退货，销售对账
			// 1.更新生产工单
			DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class);
			query.eq("sourceDetailId", saleOrderDetail.getId());
			List<WorkProduct> workProductList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkProduct.class);
			if (CollectionUtils.isNotEmpty(workProductList))
			{
				for (WorkProduct workProduct : workProductList)
				{
					workProduct.setPrice(saleOrderDetail.getPrice());
					workProduct.setMoney(saleOrderDetail.getPrice().multiply(new BigDecimal(workProduct.getSaleProduceQty())));

					// 不含税金额
					BigDecimal noTaxMoney_work = workProduct.getMoney().divide(new BigDecimal(1d + detail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
					// 不含税单价
					BigDecimal noTaxPrice_work = noTaxMoney_work.divide(new BigDecimal(workProduct.getSaleProduceQty()), 4, BigDecimal.ROUND_UP);
					// 税额
					BigDecimal tax_work = workProduct.getMoney().subtract(noTaxMoney_work);

					workProduct.setNoTaxMoney(noTaxMoney_work);
					workProduct.setNoTaxPrice(noTaxPrice_work);
					workProduct.setTax(tax_work);
					workProduct.setDeliveryTime(saleOrderDetail.getDeliveryTime());
					daoFactory.getCommonDao().updateEntity(workProduct);
				}
			}

			// 2.更新销售送货
			DynamicQuery query_deliver = new CompanyDynamicQuery(SaleDeliverDetail.class);
			query_deliver.eq("sourceDetailId", saleOrderDetail.getId());
			List<SaleDeliverDetail> saleDeliverDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query_deliver, SaleDeliverDetail.class);
			if (CollectionUtils.isNotEmpty(saleDeliverDetailList))
			{
				for (SaleDeliverDetail saleDeliverDetail : saleDeliverDetailList)
				{
					SaleDeliverDetail saleDeliverDetail_tmp = (SaleDeliverDetail) BeanUtils.cloneBean(saleDeliverDetail);

					saleDeliverDetail.setPrice(saleOrderDetail.getPrice());
					saleDeliverDetail.setMoney(saleOrderDetail.getPrice().multiply(new BigDecimal(saleDeliverDetail.getQty())));

					// 不含税金额
					BigDecimal noTaxMoney_deliver = saleDeliverDetail.getMoney().divide(new BigDecimal(1d + detail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
					// 不含税单价
					BigDecimal noTaxPrice_deliver = noTaxMoney_deliver.divide(new BigDecimal(saleDeliverDetail.getQty()), 4, BigDecimal.ROUND_UP);
					// 税额
					BigDecimal tax_deliver = saleDeliverDetail.getMoney().subtract(noTaxMoney_deliver);
					// 已送货金额
					BigDecimal returnMoney = saleOrderDetail.getPrice().multiply(new BigDecimal(saleDeliverDetail.getReturnQty()));
					// 已对账金额
					BigDecimal reconcilMoney = saleOrderDetail.getPrice().multiply(new BigDecimal(saleDeliverDetail.getReconcilQty()));

					saleDeliverDetail.setNoTaxMoney(noTaxMoney_deliver);
					saleDeliverDetail.setNoTaxPrice(noTaxPrice_deliver);
					saleDeliverDetail.setTax(tax_deliver);
					saleDeliverDetail.setReturnMoney(returnMoney);
					saleDeliverDetail.setReconcilMoney(reconcilMoney);
					daoFactory.getCommonDao().updateEntity(saleDeliverDetail);

					// 修改销售送货主表数据
					SaleDeliver saleDeliver = daoFactory.getCommonDao().getEntity(SaleDeliver.class, saleDeliverDetail.getMasterId());
					BigDecimal totalMoney_deliver = saleDeliverDetail.getMoney().subtract(saleDeliverDetail_tmp.getMoney()).add(saleDeliver.getTotalMoney());
					BigDecimal totalTax_deliver = saleDeliverDetail.getTax().subtract(saleDeliverDetail_tmp.getTax()).add(saleDeliver.getTotalTax());
					BigDecimal noTaxTotalMoney_deliver = saleDeliverDetail.getNoTaxMoney().subtract(saleDeliverDetail_tmp.getNoTaxMoney()).add(saleDeliver.getNoTaxTotalMoney());
					saleDeliver.setTotalMoney(totalMoney_deliver);
					saleDeliver.setNoTaxTotalMoney(noTaxTotalMoney_deliver);
					saleDeliver.setTotalTax(totalTax_deliver);
					daoFactory.getCommonDao().updateEntity(saleDeliver);

					// 3.更新销售退货
					DynamicQuery query_return = new CompanyDynamicQuery(SaleReturnDetail.class);
					query_return.eq("sourceDetailId", saleDeliverDetail.getId());
					List<SaleReturnDetail> saleReturnDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query_return, SaleReturnDetail.class);
					if (CollectionUtils.isNotEmpty(saleReturnDetailList))
					{
						for (SaleReturnDetail saleReturnDetail : saleReturnDetailList)
						{
							SaleReturnDetail saleReturnDetail_tmp = (SaleReturnDetail) BeanUtils.cloneBean(saleReturnDetail);

							saleReturnDetail.setPrice(saleOrderDetail.getPrice());
							saleReturnDetail.setMoney(saleOrderDetail.getPrice().multiply(new BigDecimal(saleReturnDetail.getQty())));

							// 不含税金额
							BigDecimal noTaxMoney_return = saleReturnDetail.getMoney().divide(new BigDecimal(1d + detail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
							// 不含税单价
							BigDecimal noTaxPrice_return = noTaxMoney_return.divide(new BigDecimal(saleReturnDetail.getQty()), 4, BigDecimal.ROUND_UP);
							// 税额
							BigDecimal tax_return = saleReturnDetail.getMoney().subtract(noTaxMoney_return);

							BigDecimal reconcilMoney2 = saleOrderDetail.getPrice().multiply(new BigDecimal(saleReturnDetail.getReconcilQty()));
							saleReturnDetail.setNoTaxMoney(noTaxMoney_return);
							saleReturnDetail.setNoTaxPrice(noTaxPrice_return);
							saleReturnDetail.setTax(tax_return);
							saleReturnDetail.setReconcilMoney(reconcilMoney2);
							daoFactory.getCommonDao().updateEntity(saleReturnDetail);

							// 修改销售退货主表数据
							SaleReturn saleReturn = daoFactory.getCommonDao().getEntity(SaleReturn.class, saleReturnDetail.getMasterId());
							BigDecimal totalMoney_return = saleReturnDetail.getMoney().subtract(saleReturnDetail_tmp.getMoney()).add(saleReturn.getTotalMoney());
							BigDecimal totalTax_return = saleReturnDetail.getTax().subtract(saleReturnDetail_tmp.getTax()).add(saleReturn.getTotalTax());
							BigDecimal noTaxTotalMoney_return = saleReturnDetail.getNoTaxMoney().subtract(saleReturnDetail_tmp.getNoTaxMoney()).add(saleReturn.getNoTaxTotalMoney());
							saleReturn.setTotalMoney(totalMoney_return);
							saleReturn.setNoTaxTotalMoney(noTaxTotalMoney_return);
							saleReturn.setTotalTax(totalTax_return);
							daoFactory.getCommonDao().updateEntity(saleReturn);

							// 销售对账-退货
							DynamicQuery query_reconcil2 = new CompanyDynamicQuery(SaleReconcilDetail.class);
							query_reconcil2.eq("sourceDetailId", saleReturnDetail.getId());
							List<SaleReconcilDetail> saleReconcilDetail2List = daoFactory.getCommonDao().findEntityByDynamicQuery(query_reconcil2, SaleReconcilDetail.class);
							if (CollectionUtils.isNotEmpty(saleReconcilDetail2List))
							{
								for (SaleReconcilDetail saleReconcilDetail2 : saleReconcilDetail2List)
								{
									SaleReconcilDetail saleReconcilDetail_tmp = (SaleReconcilDetail) BeanUtils.cloneBean(saleReconcilDetail2);

									saleReconcilDetail2.setPrice(saleOrderDetail.getPrice());
									saleReconcilDetail2.setMoney(saleOrderDetail.getPrice().multiply(new BigDecimal(saleReconcilDetail2.getQty())));

									// 不含税金额
									BigDecimal noTaxMoney_reconcil = saleReconcilDetail2.getMoney().divide(new BigDecimal(1d + detail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
									// 不含税单价
									BigDecimal noTaxPrice_reconcil = noTaxMoney_reconcil.divide(new BigDecimal(saleReconcilDetail2.getQty()), 4, BigDecimal.ROUND_UP);
									// 税额
									BigDecimal tax_reconcil = saleReconcilDetail2.getMoney().subtract(noTaxMoney_reconcil);

									saleReconcilDetail2.setNoTaxMoney(noTaxMoney_reconcil);
									saleReconcilDetail2.setNoTaxPrice(noTaxPrice_reconcil);
									saleReconcilDetail2.setTax(tax_reconcil);
									daoFactory.getCommonDao().updateEntity(saleReconcilDetail2);

									// 修改销售对账主表数据
									SaleReconcil saleReconcil = daoFactory.getCommonDao().getEntity(SaleReconcil.class, saleReconcilDetail2.getMasterId());
									BigDecimal totalMoney_reconcil = saleReconcilDetail2.getMoney().subtract(saleReconcilDetail_tmp.getMoney()).add(saleReconcil.getTotalMoney());
									BigDecimal totalTax_reconcil = saleReconcilDetail2.getTax().subtract(saleReconcilDetail_tmp.getTax()).add(saleReconcil.getTotalTax());
									BigDecimal noTaxTotalMoney_reconcil = saleReconcilDetail2.getNoTaxMoney().subtract(saleReconcilDetail_tmp.getNoTaxMoney()).add(saleReconcil.getNoTaxTotalMoney());
									saleReconcil.setTotalMoney(totalMoney_reconcil);
									saleReconcil.setNoTaxTotalMoney(noTaxTotalMoney_reconcil);
									saleReconcil.setTotalTax(totalTax_reconcil);
									daoFactory.getCommonDao().updateEntity(saleReconcil);
								}
							}
						}
					}

					// 4.更新销售对账
					DynamicQuery query_reconcil = new CompanyDynamicQuery(SaleReconcilDetail.class);
					query_reconcil.eq("sourceDetailId", saleDeliverDetail.getId());
					List<SaleReconcilDetail> saleReconcilDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query_reconcil, SaleReconcilDetail.class);
					if (CollectionUtils.isNotEmpty(saleReconcilDetailList))
					{
						for (SaleReconcilDetail saleReconcilDetail : saleReconcilDetailList)
						{
							SaleReconcilDetail saleReconcilDetail_tmp = (SaleReconcilDetail) BeanUtils.cloneBean(saleReconcilDetail);

							saleReconcilDetail.setPrice(saleOrderDetail.getPrice());
							saleReconcilDetail.setMoney(saleOrderDetail.getPrice().multiply(new BigDecimal(saleReconcilDetail.getQty())));

							// 不含税金额
							BigDecimal noTaxMoney_reconcil = saleReconcilDetail.getMoney().divide(new BigDecimal(1d + detail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
							// 不含税单价
							BigDecimal noTaxPrice_reconcil = noTaxMoney_reconcil.divide(new BigDecimal(saleReconcilDetail.getQty()), 4, BigDecimal.ROUND_UP);
							// 税额
							BigDecimal tax_reconcil = saleReconcilDetail.getMoney().subtract(noTaxMoney_reconcil);

							saleReconcilDetail.setNoTaxMoney(noTaxMoney_reconcil);
							saleReconcilDetail.setNoTaxPrice(noTaxPrice_reconcil);
							saleReconcilDetail.setTax(tax_reconcil);
							daoFactory.getCommonDao().updateEntity(saleReconcilDetail);

							// 修改销售对账主表数据
							SaleReconcil saleReconcil = daoFactory.getCommonDao().getEntity(SaleReconcil.class, saleReconcilDetail.getMasterId());
							BigDecimal totalMoney_reconcil = saleReconcilDetail.getMoney().subtract(saleReconcilDetail_tmp.getMoney()).add(saleReconcil.getTotalMoney());
							BigDecimal totalTax_reconcil = saleReconcilDetail.getTax().subtract(saleReconcilDetail_tmp.getTax()).add(saleReconcil.getTotalTax());
							BigDecimal noTaxTotalMoney_reconcil = saleReconcilDetail.getNoTaxMoney().subtract(saleReconcilDetail_tmp.getNoTaxMoney()).add(saleReconcil.getNoTaxTotalMoney());
							saleReconcil.setTotalMoney(totalMoney_reconcil);
							saleReconcil.setNoTaxTotalMoney(noTaxTotalMoney_reconcil);
							saleReconcil.setTotalTax(totalTax_reconcil);
							daoFactory.getCommonDao().updateEntity(saleReconcil);
						}
					}
				}
			}

			// 5.记录日志

		}
	}

	// ==================== 新规范 - 代码重构 ====================

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sale.SaleOrderService#findByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<SaleOrder> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrder.class, "o");
		query.addProjection(Projections.property("o"));
		query.createAlias(Customer.class, "c");
		query.eqProperty("c.id", "o.customerId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("o.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("o.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerBillNo()))
		{
			query.like("o.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("o.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("o.isCheck", queryParam.getAuditFlag());
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("o.createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleOrder.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sale.SaleOrderService#findDetailByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<SaleOrderDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class, "sod");
		query.addProjection(Projections.property("sod, so,p.fileName"));
		query.createAlias(SaleOrder.class, "so");
		query.createAlias(Customer.class, "c");
		query.createAlias(Product.class, "p");
		query.eqProperty("sod.masterId", "so.id");
		query.eqProperty("c.id", "so.customerId");
		query.eqProperty("p.id", "sod.productId");
		query.eq("so.isCancel", BoolValue.NO);
		if (StringUtils.isNotBlank(queryParam.getProductCode()))
		{
			query.like("sod.productCode", "%" + queryParam.getProductCode() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("so.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("sod.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("sod.style", "%" + queryParam.getProductStyle() + "%");
		}

		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("so.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("so.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("so.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getProductName()))
		{
			query.like("sod.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getDeliverDateMin() != null)
		{
			query.ge("sod.deliveryTime", queryParam.getDeliverDateMin());
		}
		if (queryParam.getDeliverDateMax() != null)
		{
			query.le("sod.deliveryTime", queryParam.getDeliverDateMax());
		}
		if (queryParam.getEmployeeId() != null && queryParam.getEmployeeId() != -1)
		{
			query.eq("so.employeeId", queryParam.getEmployeeId());
		}
		if (null != queryParam.getCustomerClassId())
		{
			query.eq("c.customerClassId", queryParam.getCustomerClassId());
		}
		if (null != queryParam.getProductClassId())
		{
			query.in("p.productClassId", queryParam.getProductClassId());
		}
		if (null != queryParam.getProductId())
		{
			query.eq("sod.productId", queryParam.getProductId());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("so.isCheck", queryParam.getAuditFlag());
		}
		query.eq("so.companyId", UserUtils.getCompanyId());
		query.eq("c.companyId", UserUtils.getCompanyId());
		query.eq("p.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("so.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<SaleOrderDetail> result = new SearchResult<SaleOrderDetail>();
		result.setResult(new ArrayList<SaleOrderDetail>());

		Product product = new Product();
		for (Object[] c : temp_result.getResult())
		{
			SaleOrder saleOrder = (SaleOrder) c[1];
			SaleOrderDetail detail = (SaleOrderDetail) c[0];
			product.setFileName((String)c[2]);
			// detail.setDeliverCheck(serviceFactory.getSaleDeliverService().hasCheckAll(saleOrder.getBillNo(),detail.getProductId()));
			// detail.setReconcilCheck(serviceFactory.getSaleReconcilService().hasCheckAll(saleOrder.getBillNo(),detail.getProductId()));
			// detail.setProduceedCheck(serviceFactory.getWorkService().hasCheckAll(saleOrder.getBillNo(),detail.getProductId()));
			// detail.setStockCheck(serviceFactory.getStockProductInService().hasCheckAll(saleOrder.getBillNo(),detail.getProductId()));
			// detail.setReceiveCheck(serviceFactory.getReceiveService().hasCheckAll(saleOrder.getBillNo(),detail.getProductId()));
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster(saleOrder);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sale.SaleOrderService#findFlowByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<SaleOrderDetail> findFlowByCondition(QueryParam queryParam)
	{
		SearchResult<SaleOrderDetail> result = findDetailByCondition(queryParam);
		// 查询所有工单产品已入库数量
		Map<Long, BigDecimal> inStockQtyMap = WorkHelper.countAllInStockQty();
		// 查询所有销售送货数量
		Map<Long, CountMap> deliverReconcilQtyMap = SaleDeliverHelper.countAllReconcilQty();
		// 查询所有销售退货数量
		Map<Long, BigDecimal> returnReconcilQtyMap = SaleReturnHelper.countAllReconcilQty();
		// 已收款金额
		Map<Long, BigDecimal> reconcilReceiveMoneyMap = SaleReconcilHelper.countAllReceiveMoney();

		for (SaleOrderDetail detail : result.getResult())
		{
			// 查询工单已返写的入库数量 说明:工单源单明细ID即销售订单明细ID
			// serviceFactory.getWorkService().countReturnStockQty(detail);
			BigDecimal stockQty = WorkHelper.countInStockQty(inStockQtyMap, detail.getId());
			detail.setStockQty(stockQty.intValue());

			// 对账数量 = 送货对账数量 - 退货对账数量
			detail.setReconcilQty(0);
			detail.setReceiveMoney(new BigDecimal(0));
			CountMap countMap = SaleDeliverHelper.countReconcilQty(deliverReconcilQtyMap, detail.getId());
			if (null != countMap)
			{
				BigDecimal returnReconcilQty = SaleReturnHelper.countReconcilQty(returnReconcilQtyMap, countMap.getId());
				if (null == returnReconcilQty)
				{
					returnReconcilQty = new BigDecimal(0);
				}
				detail.setReconcilQty(countMap.getCount().subtract(returnReconcilQty).intValue());
				// System.out.println("对账数量：" + (countMap.getCount() - returnReconcilQty));

				// 已收款金额
				BigDecimal receiveMoney = SaleReconcilHelper.countReconcilQty(reconcilReceiveMoneyMap, countMap.getId());
				if (null == receiveMoney)
				{
					receiveMoney = new BigDecimal(0);
				}
				detail.setReceiveMoney(receiveMoney);
				// System.out.println("已收款金额： " + (receiveMoney));
			}

			// 查询送货单已返写的对账数量 说明:送货源单明细ID即销售订单明细ID
			// serviceFactory.getSaleDeliverService().coutReturnReconcilQty(detail);

			// System.out.println("2 对账数量：" + detail.getReconcilQty());
			// System.out.println("2 已收款金额： " + detail.getReceiveMoney());
		}

		return result;
	}

	@Override
	public Map<ResultType, List<String>> genFromOfferCheck(List<String> ids)
	{
		Map<ResultType, List<String>> result = Maps.newHashMap();
		List<String> tips = Lists.newArrayList();
		boolean flag = false;
		if (null != ids)
		{
			for (String id : ids)
			{
				/**
				 * 在创建销售订单之前，需要先检查基础数据是否需要同步
				 * 1. 查询报价订单信息
				 * 2. 检查同步基础数据
				 *    2-1. 检查客户信息
				 *    2-2. 检查产品信息
				 *    2-3. 检查部件工序信息
				 *    2-4. 检查部件材料信息
				 *    2-5. 检查部件成品工序信息
				 */

				// 1. 查询报价订单信息
				OfferOrder offerBean = serviceFactory.getOfferOrderService().get(Long.valueOf(id));

				if (null == offerBean)
				{
					return null;
				}

				// 报价订单已经被引用
				if (offerBean.getBillNo() != null)
				{
					flag = true;
					tips.add(offerBean.getBillNo() + "," + offerBean.getSaleId());
					continue;
				}

				// tips - 工序名称
				List<String> tipProcedureList = Lists.newArrayList();
				// tips - 工序分类名称
				List<String> tipProcedureClassList = Lists.newArrayList();
				// tips - 材料名称
				List<String> tipMaterialList = Lists.newArrayList();
				// tips - 材料分类名称
				List<String> tipMaterialClassList = Lists.newArrayList();

				// 2. 检查同步基础数据
				// 2-1. 检查客户信息
				Long customerId = serviceFactory.getCustomerService().findIdByName(offerBean.getCustomerName());
				if (null == customerId)
				{
					tips.add("客户名称：<span style='color: red;'>" + offerBean.getCustomerName() + "</span>在客户信息里不存在");
				}

				// 2-2. 检查产品信息
				Product oldProduct = serviceFactory.getProductService().getByName(offerBean.getProductName());
				if (null == oldProduct)
				{
					// 创建产品分类
					ProductClass oldProductClass = serviceFactory.getProductClassService().getByName(offerBean.getOfferTypeText());
					if (null == oldProductClass)
					{
						tips.add("产品分类：<span style='color: red;'>" + offerBean.getOfferTypeText() + "</span>在产品分类里不存在");
					}

					tips.add("产品名称：<span style='color: red;'>" + offerBean.getProductName() + "</span>在产品信息里不存在");
				}

				List<OfferPart> offerPartList = offerBean.getOfferPartList();
				if (null != offerPartList && offerPartList.size() > 0)
				{
					for (OfferPart offerPart : offerPartList)
					{
						// 2-3. 检查部件工序信息
						// 需要特殊处理的格式 ： 工序名称(价格)&nbsp;&nbsp;
						List<OfferPartProcedure> offerPartProcedureList = offerPart.getOfferPartProcedureList();
						for (OfferPartProcedure offerPartProcedure : offerPartProcedureList)
						{
							// 新增的字段，老数据可能为null
							if (null != offerPartProcedure.getProcedureClass())
							{
								// 工序名称
								String name = offerPartProcedure.getProcedureName();
								Procedure procedure = serviceFactory.getProcedureService().getByName(name);
								// 工序名称不需要重复
								if (null == procedure && !tipProcedureList.contains(name))
								{
									// 工序分类
									ProcedureClass procedureClass = serviceFactory.getProcedureClassService().getByName(offerPartProcedure.getProcedureClass());
									if (null == procedureClass && !tipProcedureClassList.contains(offerPartProcedure.getProcedureClass()))
									{
										tipProcedureClassList.add(offerPartProcedure.getProcedureClass());
									}
									tipProcedureList.add(name);
								}
							}
						}

						// 2-4. 检查部件材料信息
						String name = offerPart.getPaperWeight() + "克" + offerPart.getPaperName();
						Material material = serviceFactory.getMaterialService().getByName(name);
						if (null == material && !tipMaterialList.contains(name))
						{
							String className = offerPart.getPaperName();
							// 添加材料分类
							MaterialClass materialClass = serviceFactory.getMaterialClassService().getByName(className);
							if (null == materialClass && !tipMaterialClassList.contains(className))
							{
								tipMaterialClassList.add(className);
							}

							tipMaterialList.add(name);
						}

						// 坑纸
						if (BoolValue.YES == offerPart.getContainBflute())
						{
							// 坑行
							String className = offerPart.getBflutePit();
							// 纸质
							String bflutePaperQuality = offerPart.getBflutePaperQuality();
							// 材料名称
							name = className + bflutePaperQuality;
							Material material2 = serviceFactory.getMaterialService().getByName(name);
							if (null == material2 && !tipMaterialList.contains(name))
							{
								// 添加材料分类
								MaterialClass materialClass2 = serviceFactory.getMaterialClassService().getByName(className);
								if (null == materialClass2 && !tipMaterialClassList.contains(className))
								{
									tipMaterialClassList.add(className);
								}
								tipMaterialList.add(name);
							}
						}
					}
				}

				// 2-5. 检查部件成品工序信息
				List<OfferPartProcedure> offerPartProcedureList = offerBean.getProductProcedure();
				if (null != offerPartProcedureList)
				{
					for (OfferPartProcedure offerPartProcedure : offerPartProcedureList)
					{
						// 新增的字段，老数据可能为null
						if (null != offerPartProcedure.getProcedureClass())
						{
							// 工序名称
							String name = offerPartProcedure.getProcedureName();
							Procedure procedure = serviceFactory.getProcedureService().getByName(name);
							// 工序名称不需要重复
							if (null == procedure && !tipProcedureList.contains(name))
							{
								// 工序分类
								String className = offerPartProcedure.getProcedureClass();
								ProcedureClass procedureClass = serviceFactory.getProcedureClassService().getByName(className);
								if (null == procedureClass && !tipProcedureClassList.contains(className))
								{
									tipProcedureClassList.add(className);
								}
								tipProcedureList.add(name);
							}
						}
					}
				}

				// tips - 工序分类
				if (tipProcedureClassList.size() > 0)
				{
					String s = "工序分类：<span style='color: red;'>";
					for (String t : tipProcedureClassList)
					{
						s += (t + ",");
					}
					s = s.substring(0, s.lastIndexOf(","));
					s += "</span>在工序分类里不存在";
					tips.add(s);
				}

				// tips - 工序名称
				if (tipProcedureList.size() > 0)
				{
					String s = "工序名称：<span style='color: red;'>";
					for (String t : tipProcedureList)
					{
						s += (t + ",");
					}
					s = s.substring(0, s.lastIndexOf(","));
					s += "</span>在工序信息里不存在";
					tips.add(s);
				}

				// tips - 材料分类
				if (tipMaterialClassList.size() > 0)
				{
					String s = "材料分类：<span style='color: red;'>";
					for (String t : tipMaterialClassList)
					{
						s += (t + ",");
					}
					s = s.substring(0, s.lastIndexOf(","));
					s += "</span>在材料分类里不存在";
					tips.add(s);
				}

				// tips - 材料名称
				if (tipMaterialList.size() > 0)
				{
					String s = "材料名称：<span style='color: red;'>";
					for (String t : tipMaterialList)
					{
						s += (t + ",");
					}
					s = s.substring(0, s.lastIndexOf(","));
					s += "</span>在材料信息里不存在";
					tips.add(s);
				}
			}
		}

		// 失败
		if (flag)
		{
			result.put(ResultType.ERROR, tips);
		}
		// 成功
		else
		{
			result.put(ResultType.SUCCESS, tips);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public SaleOrder genFromOffer(List<String> ids)
	{
		SaleOrder order = new SaleOrder();
		List<SaleOrderDetail> detailList = Lists.newArrayList();
		order.setDetailList(detailList);
		if (null != ids)
		{
			for (String id : ids)
			{
				/**
				 * 在创建销售订单之前，需要先准备好基础数据
				 * 1. 查询报价订单信息
				 * 2. 同步基础数据
				 *    2-1. 同步客户信息
				 *    2-2. 同步产品信息
				 *    2-3. 同步部件信息
				 *    2-4. 同步部件工序信息
				 *    2-5. 同步部件材料信息
				 *    2-6. 同步部件成品工序信息
				 */

				// 1. 查询报价订单信息
				OfferOrder offerBean = serviceFactory.getOfferOrderService().get(Long.valueOf(id));

				if (null == offerBean)
				{
					return null;
				}

				// 已经被引用
				if (offerBean.getBillNo() != null)
				{
					return null;
				}

				// 2. 同步基础数据
				// 2-1. 同步客户信息
				Customer customer = serviceFactory.getCustomerService().getByName(offerBean.getCustomerName());
				if (null == customer)
				{
					String name = offerBean.getCustomerName();
					String address = offerBean.getLinkAddress();
					String userName = offerBean.getLinkName();
					String mobile = offerBean.getPhone();
					customer = serviceFactory.getCustomerService().saveQuick(name, address, userName, mobile);
				}
				// 2-1. 设置客户信息
				order.setCustomer(customer);
				order.setCustomerId(customer.getId());
				// 客户名称
				order.setCustomerName(offerBean.getCustomerName());
				// 联系人
				order.setLinkName(offerBean.getLinkName());
				// 联系电话
				order.setMobile(offerBean.getPhone());
				// 送货地址
				order.setDeliveryAddress(offerBean.getLinkAddress());
				// 未审核
				order.setIsCheck(BoolValue.NO);
				// 销售员
				order.setEmployeeId(customer.getEmployeeId());

				// 2-2. 同步产品信息
				SaleOrderDetail saleOrderDetail = new SaleOrderDetail();
				detailList.add(saleOrderDetail);
				// 产品信息不存在，直接保存并绑定客户信息
				Product oldProduct = serviceFactory.getProductService().getByName(offerBean.getProductName());
				if (null == oldProduct)
				{
					String name = offerBean.getProductName();
					String specifications = offerBean.getSpecification();
					// 画册书刊创建产品分类时产品类别默认为书刊印刷，其余全部默认为包装印刷
					ProductType productType = ProductType.PACKE;
					if (offerBean.getOfferType() == OfferType.ALBUMBOOK)
					{
						productType = ProductType.BOOK;
					}
					// 创建产品分类
					ProductClass oldProductClass = serviceFactory.getProductClassService().getByName(offerBean.getOfferTypeText());
					if (null == oldProductClass)
					{
						oldProductClass = serviceFactory.getProductClassService().saveQuick(productType, offerBean.getOfferTypeText());
					}
					// 创建产品信息
					oldProduct = serviceFactory.getProductService().saveQuick(oldProductClass.getId(), customer.getId(), name, null, specifications);
				}
				// 产品信息存在时自动新增的客户绑定在产品信息里面
				else
				{
					Product_Customer productCustomer = serviceFactory.getProductService().findCustomerById(oldProduct.getId(), customer.getId());
					if(null == productCustomer)
					{
						serviceFactory.getProductService().appendProductCustomer(oldProduct.getId(), customer.getId());
					}
				}
				
				// 报价单号
				saleOrderDetail.setOfferId(offerBean.getId());
				saleOrderDetail.setOfferNo(offerBean.getOfferNo());
				// 产品id
				saleOrderDetail.setProductId(oldProduct.getId());
				// 产品编码
				saleOrderDetail.setProductCode(oldProduct.getCode());
				// 成品名称
				saleOrderDetail.setProductName(offerBean.getProductName());
				// 产品规格
				saleOrderDetail.setStyle(offerBean.getSpecification());
				saleOrderDetail.setUnitId(oldProduct.getUnitId());
				// 数量
				saleOrderDetail.setQty(offerBean.getAmount());
				// 备品数量
				saleOrderDetail.setSpareQty(0);
				// 单价
				OfferOrderQuoteOut quote = offerBean.getOfferOrderQuoteOutList().get(0);
				for (OfferOrderQuoteOut q : offerBean.getOfferOrderQuoteOutList())
				{
					if (q.getAmount().intValue() == offerBean.getAmount().intValue())
					{
						quote = q;
						break;
					}
				}
				// 单价
				saleOrderDetail.setPrice(new BigDecimal(quote.getTaxPrice()).setScale(4, BigDecimal.ROUND_HALF_UP));
				// 金额 = 数量 * 单价
				BigDecimal money = saleOrderDetail.getPrice().multiply(new BigDecimal(offerBean.getAmount()));
				saleOrderDetail.setMoney(money.setScale(2, BigDecimal.ROUND_HALF_UP));
				// 税率
				Integer taxPercent = offerBean.getTaxPercent();
				saleOrderDetail.setPercent(taxPercent);
				List<TaxRate> taxRateList = (List<TaxRate>) UserUtils.getBasicList("TAXRATE");
				for (TaxRate taxRate : taxRateList)
				{
					if (taxRate.getPercent().intValue() == taxPercent.intValue())
					{
						saleOrderDetail.setTaxRateId(taxRate.getId());
						break;
					}
				}
				// 税额
				double tax = saleOrderDetail.getMoney().doubleValue() - (saleOrderDetail.getMoney().doubleValue() / (1 + (double) taxPercent / 100));
				saleOrderDetail.setTax(new BigDecimal(tax).setScale(2, BigDecimal.ROUND_HALF_UP));
				// 送货时间
				saleOrderDetail.setDeliveryTime(offerBean.getDeliveryDate());
				// 图片
				saleOrderDetail.setImgUrl("");
				// 总金 额
				order.setTotalMoney(saleOrderDetail.getMoney().add(saleOrderDetail.getTax()));
				// 总金额(不含税)
				order.setNoTaxTotalMoney(saleOrderDetail.getMoney());
				// 总税额
				order.setTotalTax(saleOrderDetail.getTax());

				// 2-3. 同步部件信息
				List<SaleOrderPart> partList = Lists.newArrayList();
				saleOrderDetail.setPartList(partList);
				for (OfferPart offerPart : offerBean.getOfferPartList())
				{
					SaleOrderPart saleOrderPart = new SaleOrderPart();
					// 部件名称（默认盒型）
					saleOrderPart.setPartName(offerBean.getBoxType());
					if (null != offerPart.getPartName())
					{
						saleOrderPart.setPartName(offerPart.getPartName());
					}
					// 部件中的产品名称
					SaleOrderPart2Product part2Product = new SaleOrderPart2Product();
					part2Product.setProductName(oldProduct.getName());
					part2Product.setProductId(oldProduct.getId());
					saleOrderPart.getProductList().add(part2Product);

					// 2-4. 同步部件工序信息
					List<SaleOrderProcedure> procedureList = Lists.newArrayList();
					saleOrderPart.setProcedureList(procedureList);
					List<OfferPartProcedure> offerPartProcedureList = offerPart.getOfferPartProcedureList();
					for (OfferPartProcedure offerPartProcedure : offerPartProcedureList)
					{
						// 新增的字段，老数据可能为null
						if (null != offerPartProcedure.getProcedureClass())
						{
							// 工序名称
							String name = offerPartProcedure.getProcedureName();
							// 工序信息
							Procedure procedure = serviceFactory.getProcedureService().getByName(name);
							if (null == procedure)
							{
								// 工序分类
								ProcedureClass procedureClass = serviceFactory.getProcedureClassService().getByName(offerPartProcedure.getProcedureClass());
								if (null == procedureClass)
								{
									procedureClass = serviceFactory.getProcedureClassService().saveQuick(offerPartProcedure.getProcedureClass(), offerPartProcedure.getProcedureType());
								}

								// 工序信息
								procedure = serviceFactory.getProcedureService().saveQuick(name, procedureClass.getProcedureType(), procedureClass.getId());
							}

							SaleOrderProcedure saleProcedure = new SaleOrderProcedure();
							// 工序id
							saleProcedure.setProcedureId(procedure.getId());
							saleProcedure.setProcedureCode(procedure.getCode());
							saleProcedure.setProcedureName(procedure.getName());
							saleProcedure.setProcedureType(procedure.getProcedureType());
							saleProcedure.setProcedureClassId(procedure.getProcedureClassId());
							saleProcedure.setIsOutSource(BoolValue.NO);
							procedureList.add(saleProcedure);
						}
					}

					// 2-5. 同步部件材料信息
					/**
					 * TODO 单位换算可能需要支持国际化
					 * 1. 材料信息时，自动新增的材料计价单位默认为【吨】，库存单位默认为【张】
					 * 2. 坑纸材料信息时， 自动新增的材料计价单位默认为【千平方英寸】，库存单位默认为【张】
					 */
					List<Unit> unitList = (List<Unit>) UserUtils.getBasicList("UNIT");
					// 单位 - 吨（正常情况不可能为null）
					Unit d = null;
					// 单位 - 张（正常情况不可能为null）
					Unit z = null;
					// 单位 - 千平方英寸（正常情况不可能为null）
					Unit thousand = null;
					for (Unit unit : unitList)
					{
						if ("吨".equals(unit.getName()))
						{
							d = unit;
						}
						else if ("张".equals(unit.getName()))
						{
							z = unit;
						}
						else if ("千平方英寸".equals(unit.getName()))
						{
							thousand = unit;
						}
					}

					// 基础材料
					List<SaleOrderMaterial> materialList = Lists.newArrayList();
					saleOrderPart.setMaterialList(materialList);
					String name = offerPart.getPaperWeight() + "克" + offerPart.getPaperName();
					Material material = serviceFactory.getMaterialService().getByName(name);
					if (null == material)
					{
						String className = offerPart.getPaperName();
						// 添加材料分类
						MaterialClass materialClass = serviceFactory.getMaterialClassService().getByName(className);
						if (null == materialClass)
						{
							materialClass = serviceFactory.getMaterialClassService().saveQuick(className);
						}

						// 添加材料信息
						material = serviceFactory.getMaterialService().saveQuick(materialClass.getId(), name, offerPart.getPaperWeight(), d.getId(), z.getId(), offerPart.getPaperTonPrice());
					}
					SaleOrderMaterial saleMaterial = new SaleOrderMaterial();
					saleMaterial.setMaterialId(material.getId());
					saleMaterial.setMaterialCode(material.getCode());
					saleMaterial.setMaterialName(material.getName());
					saleMaterial.setStyle(offerPart.getPaperType().getStyle());
					saleMaterial.setStockUnitId(material.getStockUnitId());
					saleMaterial.setValuationUnitId(material.getValuationUnitId());
					saleMaterial.setWeight(material.getWeight());
					// saleMaterial.setIsCustPaper(isCustPaper);
					materialList.add(saleMaterial);

					// 坑纸
					if (BoolValue.YES == offerPart.getContainBflute())
					{
						// 坑行
						String className = offerPart.getBflutePit();
						// 纸质
						String bflutePaperQuality = offerPart.getBflutePaperQuality();
						// 材料名称
						name = className + bflutePaperQuality;
						Material material2 = serviceFactory.getMaterialService().getByName(name);
						if (null == material2)
						{
							// 添加材料分类
							MaterialClass materialClass2 = serviceFactory.getMaterialClassService().getByName(className);
							if (null == materialClass2)
							{
								materialClass2 = serviceFactory.getMaterialClassService().saveQuick(className);
							}

							// 添加材料信息
							material2 = serviceFactory.getMaterialService().saveQuick(materialClass2.getId(), name, 0, thousand.getId(), z.getId(), offerPart.getBflutePrice());
						}

						SaleOrderMaterial saleMaterial2 = new SaleOrderMaterial();
						saleMaterial2.setMaterialId(material2.getId());
						saleMaterial2.setMaterialCode(material2.getCode());
						saleMaterial2.setMaterialName(material2.getName());
						saleMaterial2.setStyle(offerPart.getMachineSpec());
						saleMaterial2.setStockUnitId(material2.getStockUnitId());
						saleMaterial2.setValuationUnitId(material2.getValuationUnitId());
						saleMaterial2.setWeight(material2.getWeight());
						// saleMaterial.setIsCustPaper(isCustPaper);
						materialList.add(saleMaterial2);
					}

					partList.add(saleOrderPart);
				}

				// 2-6. 同步部件成品工序信息（没有材料）
				SaleOrderPack pack = new SaleOrderPack();
				saleOrderDetail.setPack(pack);
				List<SaleOrderProcedure> procedureList = Lists.newArrayList();
				pack.setProcedureList(procedureList);
				List<OfferPartProcedure> offerPartProcedureList = offerBean.getProductProcedure();
				if (null != offerPartProcedureList)
				{
					for (OfferPartProcedure offerPartProcedure : offerPartProcedureList)
					{
						// 新增的字段，老数据可能为null
						if (null != offerPartProcedure.getProcedureClass())
						{
							// 工序名称
							String name = offerPartProcedure.getProcedureName();
							// 工序信息
							Procedure procedure = serviceFactory.getProcedureService().getByName(name);
							if (null == procedure)
							{
								// 工序分类
								ProcedureClass procedureClass = serviceFactory.getProcedureClassService().getByName(offerPartProcedure.getProcedureClass());
								if (null == procedureClass)
								{
									procedureClass = serviceFactory.getProcedureClassService().saveQuick(offerPartProcedure.getProcedureClass(), offerPartProcedure.getProcedureType());
								}

								// 工序信息
								procedure = serviceFactory.getProcedureService().saveQuick(name, procedureClass.getProcedureType(), procedureClass.getId());
							}

							SaleOrderProcedure saleProcedure = new SaleOrderProcedure();
							// 工序id
							saleProcedure.setProcedureId(procedure.getId());
							saleProcedure.setIsOutSource(BoolValue.NO);
							procedureList.add(saleProcedure);
						}
					}
				}
			}
		}

		return order;
	}
}
