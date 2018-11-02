/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月17日 上午11:22:13
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.offer.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.helper.ServiceHelper;
import com.huayin.printmanager.helper.service.OfferHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.offer.OfferBflute;
import com.huayin.printmanager.persist.entity.offer.OfferFormula;
import com.huayin.printmanager.persist.entity.offer.OfferMachine;
import com.huayin.printmanager.persist.entity.offer.OfferPaper;
import com.huayin.printmanager.persist.entity.offer.OfferPrePrint;
import com.huayin.printmanager.persist.entity.offer.OfferProcedure;
import com.huayin.printmanager.persist.entity.offer.OfferProfit;
import com.huayin.printmanager.persist.entity.offer.OfferStartPrint;
import com.huayin.printmanager.persist.entity.offer.OfferWaste;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.OfferMachineType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.offer.OfferSettingService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 报价模块 - 报价设置
 * </pre>
 * @author think
 * @since 1.0, 2017年10月17日
 */
@Service
public class OfferSettingServiceImpl extends BaseServiceImpl implements OfferSettingService
{
	// ==================== 报价设置 - 损耗设置 ====================

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#findAdminWaste()
	 */
	@Override
	public List<OfferWaste> findAdminWaste()
	{
		return getWaste(UserUtils.getSystemCompanyId());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getlockWaste(java.lang.String)
	 */
	@Override
	public List<OfferWaste> getlockWaste(String companyId)
	{
		DynamicQuery query = new DynamicQuery(OfferWaste.class);
		query.eq("companyid", companyId);
		query.asc("sort");
		return daoFactory.getCommonDao().lockByDynamicQuery(query, OfferWaste.class, QueryConstants.LockType.LOCK_PASS);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getWaste(java.lang.String)
	 */
	@Override
	public List<OfferWaste> getWaste(String companyId)
	{
		DynamicQuery query = new DynamicQuery(OfferWaste.class);
		query.eq("companyId", companyId);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferWaste.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getWaste()
	 */
	@Override
	public List<OfferWaste> getWaste()
	{
		return getWaste(UserUtils.getCompanyId());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#saveWaste(java.util.List)
	 */
	@Override
	@Transactional
	public List<OfferWaste> saveWaste(List<OfferWaste> list)
	{
		/**
		 * 1. 更新数据 
		 * 2. 更新缓存
		 */

		// 1. 更新数据（用户登陆时，会初始化数据。所以，这里不会为空）
		daoFactory.getCommonDao().updateAllEntity(list);

		// 2. 更新缓存（区分系统管理员和普通公司）
		if (UserUtils.isSystemAdmin())
		{
			OfferHelper.clearAdminWasteSettingCache();
		}
		else
		{
			OfferHelper.clearWasteSettingCache();
		}

		return list;
	}

	// ==================== 报价设置 - 机台 ====================

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getMachine(java.lang.Long)
	 */
	@Override
	public OfferMachine getMachine(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferMachine.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, OfferMachine.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getMachineStartPrint(java.lang.Long)
	 */
	@Override
	public OfferStartPrint getMachineStartPrint(Long master)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferStartPrint.class);
		query.eq("masterId", master);
		return daoFactory.getCommonDao().getByDynamicQuery(query, OfferStartPrint.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getMachineFormula(java.lang.Long)
	 */
	@Override
	public OfferFormula getMachineFormula(Long master)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferFormula.class);
		query.eq("masterId", master);
		return daoFactory.getCommonDao().getByDynamicQuery(query, OfferFormula.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getLockMachine(java.lang.Long)
	 */
	@Override
	public OfferMachine getLockMachine(Long id)
	{
		return daoFactory.getCommonDao().lockObject(OfferMachine.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getLockMachineStartPrint(java.lang.Long)
	 */
	@Override
	public OfferStartPrint getLockMachineStartPrint(Long id)
	{
		return daoFactory.getCommonDao().lockObject(OfferStartPrint.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getLockMachineFormula(java.lang.Long)
	 */
	@Override
	public OfferFormula getLockMachineFormula(Long id)
	{
		return daoFactory.getCommonDao().lockObject(OfferFormula.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#findAllMachineStartPrint()
	 */
	@Override
	public List<OfferStartPrint> findAllMachineStartPrint()
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferStartPrint.class);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferStartPrint.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#findAllMachineFormula()
	 */
	@Override
	public List<OfferFormula> findAllMachineFormula()
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferFormula.class);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferFormula.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#findMachineByCondition(com.huayin.printmanager.service.
	 * vo. QueryParam)
	 */
	@Override
	public SearchResult<OfferMachine> findMachineByCondition(QueryParam queryParam)
	{
		// 只能按照OfferType查询
		if (queryParam.getOfferType() == null)
		{
			return null;
		}

		DynamicQuery query = new CompanyDynamicQuery(OfferMachine.class);
		query.setIsSearchTotalCount(true);
		// 报价类型
		if (queryParam.getOfferType() != null)
		{
			query.eq("offerType", queryParam.getOfferType());
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getMachineName()))
		{
			query.like("name", "%" + queryParam.getMachineName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OfferMachine.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getMachine(java.lang.Long)
	 */
	@Override
	public OfferMachine findMachine(Long id)
	{
		OfferMachine machine = getMachine(id);
		// 查询开机费+印工计价
		if (null != machine && machine.getOfferMachineType() == OfferMachineType.START_PRINT)
		{
			machine.setOfferStartPrint(getMachineStartPrint(machine.getId()));
		}
		// 自定义报价公式
		if (null != machine && machine.getOfferMachineType() == OfferMachineType.CUSTOM)
		{
			machine.setOfferFormula(getMachineFormula(machine.getId()));
		}
		return machine;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#findAllMachine(com.huayin.printmanager.persist.enumerate.
	 * OfferType)
	 */
	@Override
	public List<OfferMachine> findAllMachine(OfferType offerType)
	{
		Map<Long, OfferStartPrint> mapStartPrint = Maps.newHashMap();
		List<OfferStartPrint> listStartPrint = findAllMachineStartPrint();
		if (CollectionUtils.isNotEmpty(listStartPrint))
		{
			for (OfferStartPrint startPrint : listStartPrint)
			{
				mapStartPrint.put(startPrint.getMasterId(), startPrint);
			}
		}
		Map<Long, OfferFormula> mapFormula = Maps.newHashMap();
		List<OfferFormula> listFormula = findAllMachineFormula();
		if (CollectionUtils.isNotEmpty(listFormula))
		{
			for (OfferFormula formula : listFormula)
			{
				mapFormula.put(formula.getMasterId(), formula);
			}
		}
		QueryParam queryParam = new QueryParam();
		queryParam.setOfferType(offerType);
		queryParam.setPageNumber(1);
		queryParam.setPageSize(100);// 基本上不会超过100
		SearchResult<OfferMachine> listMacine = findMachineByCondition(queryParam);
		if (CollectionUtils.isNotEmpty(listMacine.getResult()))
		{
			for (OfferMachine machine : listMacine.getResult())
			{
				if (OfferMachineType.START_PRINT == machine.getOfferMachineType())
				{
					machine.setOfferStartPrint(mapStartPrint.get(machine.getId()));
				}
				else if (OfferMachineType.CUSTOM == machine.getOfferMachineType())
				{
					machine.setOfferFormula(mapFormula.get(machine.getId()));
				}
			}
		}

		return listMacine.getResult();
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#findMachineByName(com.huayin.printmanager.persist.
	 * enumerate.OfferType, java.lang.String)
	 */
	@Override
	public OfferMachine findMachineByName(OfferType offerType, String machineName)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferMachine.class);
		query.eq("name", machineName);
		query.eq("offerType", offerType);
		return daoFactory.getCommonDao().getByDynamicQuery(query, OfferMachine.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#saveMachine(com.huayin.printmanager.persist.entity.offer.
	 * OfferMachine)
	 */
	@Override
	@Transactional
	public OfferMachine saveMachine(OfferMachine offerMachine)
	{
		/**
		 * 1. 构造机台必填数据 
		 * 2. 创建机台 
		 * 3. 创建【开机费+印工计价】或【自定义公式计价】
		 */

		// 1. 构造必填数据
		offerMachine.setCompanyId(UserUtils.getCompanyId());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			offerMachine.setCreateName(e.getName());
		}
		else
		{
			offerMachine.setCreateName(UserUtils.getUserName());
		}
		offerMachine.setCreateTime(new Date());

		// 2. 创建机台
		offerMachine = daoFactory.getCommonDao().saveEntity(offerMachine);

		// 3. 创建开机费+印工计价
		if (offerMachine.getOfferMachineType() == OfferMachineType.START_PRINT)
		{
			OfferStartPrint offerStartPrint = offerMachine.getOfferStartPrint();
			offerStartPrint.setCompanyId(UserUtils.getCompanyId());
			offerStartPrint.setMasterId(offerMachine.getId());

			daoFactory.getCommonDao().saveEntity(offerStartPrint);
		}
		// 3. 自定义公式计价
		else if (offerMachine.getOfferMachineType() == OfferMachineType.CUSTOM)
		{
			OfferFormula offerFormula = offerMachine.getOfferFormula();
			offerFormula.setCode(UserUtils.createNo("GS"));
			offerFormula.setCompanyId(UserUtils.getCompanyId());
			offerFormula.setMasterId(offerMachine.getId());

			daoFactory.getCommonDao().saveEntity(offerFormula);
		}

		return offerMachine;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#updateMachine(com.huayin.printmanager.persist.entity.
	 * offer.OfferMachine)
	 */
	@Override
	@Transactional
	public OfferMachine updateMachine(OfferMachine offerMachine)
	{
		/**
		 * 1. 构造机台必填数据
		 * 2. 更新机台 
		 * 3. 更新【开机费+印工计价】或【自定义公式计价】
		 */

		// 1. 构造必填数据
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			offerMachine.setUpdateName(e.getName());
		}
		else
		{
			offerMachine.setUpdateName(UserUtils.getUserName());
		}
		offerMachine.setUpdateTime(new Date());
		// 从服务器同步旧数据
		OfferMachine lockMachine = getLockMachine(offerMachine.getId());
		offerMachine.setCompanyId(lockMachine.getCompanyId());
		offerMachine.setCreateName(lockMachine.getCreateName());
		offerMachine.setCreateTime(lockMachine.getCreateTime());

		// 2. 更新机台
		daoFactory.getCommonDao().updateEntity(offerMachine);

		// 3. 更新开机费+印工计价
		if (offerMachine.getOfferMachineType() == OfferMachineType.START_PRINT)
		{
			OfferStartPrint offerStartPrint = offerMachine.getOfferStartPrint();
			if (offerStartPrint.getId() == null)
			{
				// 删除历史开机费和自定义公式
				_deleteMachineDetail(offerMachine.getId());

				// 新增开机费
				offerStartPrint.setCompanyId(UserUtils.getCompanyId());
				offerStartPrint.setMasterId(offerMachine.getId());
				daoFactory.getCommonDao().saveEntity(offerStartPrint);
			}

			offerStartPrint.setCompanyId(UserUtils.getCompanyId());
			offerStartPrint.setMasterId(offerMachine.getId());

			daoFactory.getCommonDao().updateEntity(offerStartPrint);
		}
		// 3. 更新自定义公式计价
		else if (offerMachine.getOfferMachineType() == OfferMachineType.CUSTOM)
		{
			OfferFormula offerFormula = offerMachine.getOfferFormula();
			// 切换保存，直接新增即可
			if (offerFormula.getId() == null)
			{
				// 删除历史开机费和自定义公式
				_deleteMachineDetail(offerMachine.getId());

				// 新增自定义公式
				offerFormula.setCode(UserUtils.createNo("GS"));
				offerFormula.setCompanyId(UserUtils.getCompanyId());
				offerFormula.setMasterId(offerMachine.getId());

				daoFactory.getCommonDao().saveEntity(offerFormula);
			}
			// 直接更新
			else
			{
				OfferFormula lockMachineFormula = getLockMachineFormula(offerFormula.getId());
				lockMachineFormula.setFormula(offerFormula.getFormula());
				daoFactory.getCommonDao().updateEntity(lockMachineFormula);
			}
		}

		return offerMachine;
	}

	/**
	 * <pre>
	 * 删除机台开机费和自定义公式
	 * </pre>
	 * @param masterId
	 * @since 1.0, 2017年12月15日 下午3:06:54, think
	 */
	private void _deleteMachineDetail(Long masterId)
	{
		// 删除机台开机费
		DynamicQuery query = new CompanyDynamicQuery(OfferStartPrint.class);
		query.eq("companyId", UserUtils.getCompanyId());
		query.eq("masterId", masterId);
		List<OfferStartPrint> offerStartPrintList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferStartPrint.class);
		daoFactory.getCommonDao().deleteAllEntity(offerStartPrintList);

		// 删除机台自定义公式
		DynamicQuery query2 = new CompanyDynamicQuery(OfferFormula.class);
		query2.eq("companyId", UserUtils.getCompanyId());
		query2.eq("masterId", masterId);
		List<OfferFormula> offerFormulaList = daoFactory.getCommonDao().findEntityByDynamicQuery(query2, OfferFormula.class);
		daoFactory.getCommonDao().deleteAllEntity(offerFormulaList);
	}

	@Override
	@Transactional
	public void deleteMachine(Long id)
	{
		/**
		 * 1. 删除开机费 或 自定义公式 
		 * 2. 删除机台
		 */
		// 1. 删除开机费
		OfferStartPrint machineStartPrint = getMachineStartPrint(id);
		if (null != machineStartPrint)
		{
			daoFactory.getCommonDao().deleteEntity(OfferStartPrint.class, machineStartPrint.getId());
		}
		// 1. 删除自定义公式
		OfferFormula machineFormula = getMachineFormula(id);
		if (null != machineFormula)
		{
			daoFactory.getCommonDao().deleteEntity(OfferFormula.class, machineFormula.getId());
		}
		// 2. 删除机台
		daoFactory.getCommonDao().deleteEntity(OfferMachine.class, id);
	}

	// ==================== 报价设置 - 印前费用 ====================

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getPrePrint(java.lang.Long)
	 */
	@Override
	public OfferPrePrint getPrePrint(OfferType offerType)
	{
		/**
		 * 获取印前费用数据
		 */
		DynamicQuery query = new CompanyDynamicQuery(OfferPrePrint.class);
		query.eq("companyId", UserUtils.getCompanyId());
		query.eq("offerType", offerType);
		OfferPrePrint offerPrePrint = daoFactory.getCommonDao().getByDynamicQuery(query, OfferPrePrint.class);
		return offerPrePrint;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#savePrePrint(com.huayin.printmanager.persist.entity.offer
	 * .OfferPrePrint)
	 */
	@Override
	@Transactional
	public OfferPrePrint savePrePrint(OfferPrePrint offerPrePrint)
	{
		/**
		 * 1.判断是否id是否为空，空值就是新增，非空就是更新 
		 * 2.保存后，返回一个保存后的对象，jsp页面刷新一次 
		 * 3.更新前锁定当前对象后再更新
		 */
		offerPrePrint.setCompanyId(UserUtils.getCompanyId());
		OfferPrePrint reObj = new OfferPrePrint();
		if (null == offerPrePrint.getId() || "".equals(offerPrePrint.getId()))
		{
			reObj = daoFactory.getCommonDao().saveEntity(offerPrePrint);
		}
		else
		{
			daoFactory.getCommonDao().updateEntity(offerPrePrint);
		}

		return reObj;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#lock(java.lang.Long)
	 */
	@Override
	public OfferPrePrint getLockPrePrint()
	{
		return getLockPrePrint(UserUtils.getCompanyId());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getLockPrePrint(java.lang.String)
	 */
	@Override
	public OfferPrePrint getLockPrePrint(String companyId)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferPrePrint.class);
		query.eq("companyId", companyId);
		OfferPrePrint offerPrePrint = daoFactory.getCommonDao().lockByDynamicQuery(query, OfferPrePrint.class, LockType.LOCK_WAIT).get(0);
		return offerPrePrint;
	}

	// ==================== 报价设置 - 利润设置 ====================

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getProfit(com.huayin.printmanager.persist.enumerate.
	 * OfferType)
	 */
	@Override
	public List<OfferProfit> getProfit(OfferType type)
	{
		return getProfit(type, UserUtils.getCompanyId());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getProfit(com.huayin.printmanager.persist.enumerate.
	 * OfferType, java.lang.String)
	 */
	@Override
	public List<OfferProfit> getProfit(OfferType type, String companyId)
	{
		DynamicQuery query = new DynamicQuery(OfferProfit.class);
		query.eq("companyId", companyId);
		query.eq("offerType", type);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferProfit.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#saveProfit(java.util.List)
	 */
	@Override
	@Transactional
	public List<OfferProfit> saveProfit(List<OfferProfit> list)
	{
		/**
		 * 1. 直接清空老数据 
		 * 2. 保存新数据
		 */

		// 1. 直接清空老数据
		daoFactory.getCommonDao().deleteAllEntity(getProfit(list.get(0).getOfferType()));

		// 2. 保存新数据
		for (OfferProfit profit : list)
		{
			profit.setCompanyId(UserUtils.getCompanyId());
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				profit.setCreateName(e.getName());
			}
			else
			{
				profit.setCreateName(UserUtils.getUserName());
			}
			profit.setCreateTime(new Date());
		}

		daoFactory.getCommonDao().saveAllEntity(list);

		return list;
	}

	// ==================== 报价设置 - 纸张设置 ====================

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getPaper(java.lang.Long)
	 */
	@Override
	public OfferPaper getPaper(Long id)
	{
		DynamicQuery query = new DynamicQuery(OfferPaper.class);
		query.eq("id", id);
		query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().getByDynamicQuery(query, OfferPaper.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getLockPaper(java.lang.Long)
	 */
	@Override
	public OfferPaper getLockPaper(Long id)
	{
		return daoFactory.getCommonDao().lockObject(OfferPaper.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getOfferPaperList(com.huayin.printmanager.persist.
	 * enumerate.OfferType)
	 */
	@Override
	public SearchResult<OfferPaper> getPaperList(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(OfferPaper.class);
		query.eq("companyId", UserUtils.getCompanyId());
		if (null != queryParam.getOfferType())
		{
			query.eq("offerType", queryParam.getOfferType());
		}
		if (StringUtils.isNotBlank(queryParam.getName()))
		{
			query.like("name", "%" + queryParam.getName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		query.desc("id");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OfferPaper.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#getPaperList(com.huayin.printmanager.persist.enumerate.
	 * OfferType, java.lang.String)
	 */
	@Override
	public List<OfferPaper> getPaperList(OfferType offerType, String name)
	{
		DynamicQuery query = new DynamicQuery(OfferPaper.class);
		query.eq("companyId", UserUtils.getCompanyId());
		query.eq("offerType", offerType);
		query.desc("weight");
		if (null != name && !"".equals(name))
		{
			query.eq("name", name);
		}
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferPaper.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#savePaperByHandle(com.huayin.printmanager.persist.entity.
	 * offer.OfferPaperMaterial)
	 */
	@Override
	@Transactional
	public OfferPaper savePaper(OfferPaper offerPaper)
	{
		/**
		 * 单个保存
		 */
		offerPaper.setCompanyId(UserUtils.getCompanyId());
		offerPaper.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			offerPaper.setCreateName(e.getName());
		}
		else
		{
			offerPaper.setCreateName(UserUtils.getUserName());
		}
		return daoFactory.getCommonDao().saveEntity(offerPaper);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#savePaperByBatch(com.huayin.printmanager.persist.entity.
	 * offer.OfferPaperMaterial)
	 */
	@Override
	@Transactional
	public void savePaperByBatch(List<OfferPaper> paperList)
	{
		/**
		 * 批量保存
		 * 
		 */
		String createName = "";
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			createName = e.getName();
		}
		else
		{
			createName = UserUtils.getUserName();
		}

		for (OfferPaper paper : paperList)
		{
			paper.setCompanyId(UserUtils.getCompanyId());
			paper.setCreateName(createName);
			paper.setCreateTime(new Date());
		}
		daoFactory.getCommonDao().saveAllEntity(paperList);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#savePaperBySelect(java.util.List)
	 */
	@Override
	@Transactional
	public void savePaperByMaterial(List<OfferPaper> paperList)
	{
		daoFactory.getCommonDao().saveAllEntity(paperList);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#updatePaper(com.huayin.printmanager.persist.entity.offer.
	 * OfferPaperMaterial)
	 */
	@Override
	@Transactional
	public OfferPaper updatePaper(OfferPaper offerPaper)
	{
		// 1.获取锁定的对象准备更新
		OfferPaper lockedPaper = getLockPaper(offerPaper.getId());
		// 2.设更新者名字
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			offerPaper.setUpdateName(e.getName());
		}
		else
		{
			offerPaper.setUpdateName(UserUtils.getUserName());
		}
		// 3.设更新时间
		offerPaper.setUpdateTime(new Date());
		offerPaper.setCompanyId(lockedPaper.getCompanyId());
		offerPaper.setCreateName(lockedPaper.getCreateName());
		offerPaper.setCreateTime(lockedPaper.getCreateTime());
		return daoFactory.getCommonDao().updateEntity(offerPaper);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#delPaper(java.lang.Long)
	 */
	@Override
	@Transactional
	public void deletePaper(Long id)
	{
		daoFactory.getCommonDao().deleteEntity(OfferPaper.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#deletePaperBatch(java.lang.Long[])
	 */
	@Override
	@Transactional
	public void deletePaperByBatch(Long[] ids)
	{
		DynamicQuery query = new DynamicQuery(OfferPaper.class);
		query.in("id", Arrays.asList(ids));
		List<OfferPaper> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferPaper.class);
		if (CollectionUtils.isNotEmpty(list))
		{
			serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(list);
		}
	}

	// ==================== 报价设置 - 坑纸设置 ====================
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getBflute(java.lang.Long)
	 */
	@Override
	public OfferBflute getBflute(Long id)
	{
		DynamicQuery query = new DynamicQuery(OfferBflute.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, OfferBflute.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getLockBflute(java.lang.Long)
	 */
	@Override
	public OfferBflute getLockBflute(Long id)
	{
		return daoFactory.getCommonDao().lockObject(OfferBflute.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getBfluteList(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<OfferBflute> getBfluteList(QueryParam queryParam)
	{
		/**
		 * 获取坑纸列表数据
		 */
		DynamicQuery query = new DynamicQuery(OfferBflute.class);
		query.eq("companyId", UserUtils.getCompanyId());
		query.eq("offerType", queryParam.getOfferType());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		query.desc("id");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OfferBflute.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getBfluteList(java.lang.String)
	 */
	@Override
	public List<OfferBflute> getBfluteList(String pit)
	{
		DynamicQuery query = new DynamicQuery(OfferBflute.class);
		query.eq("companyId", UserUtils.getCompanyId());
		if (StringUtils.isNotEmpty(pit))
		{
			query.eq("pit", pit);
		}
		query.desc("id");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferBflute.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#saveBflute(com.huayin.printmanager.persist.entity.offer.
	 * OfferBflute)
	 */
	@Override
	@Transactional
	public OfferBflute saveBflute(OfferBflute offerBflute)
	{
		/**
		 * 保存单条数据
		 */
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			offerBflute.setCreateName(e.getName());
		}
		else
		{
			offerBflute.setCreateName(UserUtils.getUserName());
		}
		offerBflute.setCompanyId(UserUtils.getCompanyId());
		offerBflute.setCreateTime(new Date());
		return daoFactory.getCommonDao().saveEntity(offerBflute);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#saveBfluteByBatch(com.huayin.printmanager.persist.entity.
	 * offer.OfferBflute)
	 */
	@Override
	@Transactional
	public void saveBfluteByBatch(List<OfferBflute> offerBfluteList)
	{
		/**
		 * 批量保存
		 * 
		 */
		String createName = "";
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			createName = e.getName();
		}
		else
		{
			createName = UserUtils.getUserName();
		}

		for (OfferBflute bflute : offerBfluteList)
		{
			bflute.setCompanyId(UserUtils.getCompanyId());
			bflute.setCreateName(createName);
			bflute.setCreateTime(new Date());
		}
		daoFactory.getCommonDao().saveAllEntity(offerBfluteList);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#saveBfluteBySelect(java.util.List)
	 */
	@Override
	@Transactional
	public void saveBfluteByMaterial(List<OfferBflute> bfluteList)
	{
		daoFactory.getCommonDao().saveAllEntity(bfluteList);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#updateBflute(com.huayin.printmanager.persist.entity.offer
	 * .OfferBflute)
	 */
	@Override
	@Transactional
	public OfferBflute updateBflute(OfferBflute offerBflute)
	{
		// 1.获取锁定的对象准备更新
		OfferBflute lockedBflute = getLockBflute(offerBflute.getId());
		// 2.设更新者名字
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			offerBflute.setUpdateName(e.getName());
		}
		else
		{
			offerBflute.setUpdateName(UserUtils.getUserName());
		}
		// 3.设更新时间
		offerBflute.setUpdateTime(new Date());
		offerBflute.setCompanyId(lockedBflute.getCompanyId());
		offerBflute.setCreateName(lockedBflute.getCreateName());
		offerBflute.setCreateTime(lockedBflute.getCreateTime());
		return daoFactory.getCommonDao().updateEntity(offerBflute);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#deleteBflute(java.lang.Long)
	 */
	@Override
	@Transactional
	public void deleteBflute(Long id)
	{
		daoFactory.getCommonDao().deleteEntity(OfferBflute.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#deleteBfluteBatch(java.lang.Long[])
	 */
	@Override
	@Transactional
	public void deleteBfluteByBatch(Long[] ids)
	{
		DynamicQuery query = new DynamicQuery(OfferBflute.class);
		query.in("id", Arrays.asList(ids));
		List<OfferBflute> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferBflute.class);
		if (CollectionUtils.isNotEmpty(list))
		{
			serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(list);
		}
	}

	// ==================== 报价设置 - 工序设置 ====================

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#getProcedure(com.huayin.printmanager.persist.enumerate.
	 * OfferType)
	 */
	@Override
	public List<OfferProcedure> getProcedure(OfferType type)
	{
		return getProcedure(type, UserUtils.getCompanyId());
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#getProcedure(com.huayin.printmanager.persist.enumerate.
	 * OfferType, java.lang.String)
	 */
	@Override
	public List<OfferProcedure> getProcedure(OfferType type, String companyId)
	{
		DynamicQuery query = new DynamicQuery(OfferProcedure.class);
		query.eq("companyId", companyId);
		query.eq("offerType", type);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferProcedure.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#getProcedure(com.huayin.printmanager.persist.enumerate.
	 * OfferType, java.lang.Long)
	 */
	@Override
	public OfferProcedure getProcedure(OfferType type, Long id)
	{
		DynamicQuery query = new DynamicQuery(OfferProcedure.class);
		query.eq("companyId", UserUtils.getCompanyId());
		query.eq("offerType", type);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, OfferProcedure.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#getProcedureByName(com.huayin.printmanager.persist.
	 * enumerate.OfferType, java.lang.String)
	 */
	@Override
	public OfferProcedure getProcedureByName(OfferType type, String name)
	{
		DynamicQuery query = new DynamicQuery(OfferProcedure.class);
		query.eq("companyId", UserUtils.getCompanyId());
		query.eq("offerType", type);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, OfferProcedure.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.offer.OfferSettingService#findProcedureByCondition(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<OfferProcedure> findProcedureByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferProcedure.class);
		query.setIsSearchTotalCount(true);
		// 报价类型
		if (queryParam.getOfferType() != null)
		{
			query.eq("offerType", queryParam.getOfferType());
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getName()))
		{
			query.like("name", "%" + queryParam.getName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OfferProcedure.class);
	}
	
	@Override
	public SearchResult<OfferProcedure> findProcedureDuplicate(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferProcedure.class);
		query.setIsSearchTotalCount(true);
		// 报价类型
		if (queryParam.getOfferType() != null)
		{
			query.eq("offerType", queryParam.getOfferType());
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getName()))
		{
			query.like("name", "%" + queryParam.getName() + "%");
		}
		
		// 子查询去除重复
		DynamicQuery queryDuplicate = new CompanyDynamicQuery(OfferProcedure.class, "b");
		queryDuplicate.addProjection(Projections.property("b.id"));
		queryDuplicate.addGourp("name");
		//queryDuplicate.addHaving(Restrictions.gt("count(*)", 1l));
		query.in("id", queryDuplicate);
		
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OfferProcedure.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferSettingService#saveProcedure(java.util.List)
	 */
	@Transactional
	@Override
	public List<OfferProcedure> saveProcedure(List<OfferProcedure> list)
	{
		/**
		 * 1. 构造工序必填数据 
		 * 2. 创建工序
		 */

		// 1. 构造工序必填数据
		// 报价类型
		OfferType offerType = list.get(0).getOfferType();
		// 查询历史数据
		List<OfferProcedure> oldList = getProcedure(offerType);
		// 当全部删除时的特殊处理
		if (list.get(0).getIsDeleteAll())
		{
			daoFactory.getCommonDao().deleteAllEntity(oldList);
			return null;
		}

		// 过滤出新增、修改、删除的对象
		Map<String, List<OfferProcedure>> filterCUD = ServiceHelper.filterCUD(oldList, list);
		List<OfferProcedure> listAdd = filterCUD.get(ServiceHelper.Cud.C);
		List<OfferProcedure> listUpdate = filterCUD.get(ServiceHelper.Cud.U);
		List<OfferProcedure> listDel = filterCUD.get(ServiceHelper.Cud.D);

		// 新增
		for (OfferProcedure offerProcedure : listAdd)
		{
			offerProcedure.setCompanyId(UserUtils.getCompanyId());
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				offerProcedure.setCreateName(e.getName());
			}
			else
			{
				offerProcedure.setCreateName(UserUtils.getUserName());
			}
			offerProcedure.setCreateTime(new Date());
		}

		// 修改
		for (OfferProcedure offerProcedure : listUpdate)
		{
			offerProcedure.setCompanyId(UserUtils.getCompanyId());
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				offerProcedure.setUpdateName(e.getName());
			}
			else
			{
				offerProcedure.setUpdateName(UserUtils.getUserName());
			}
			offerProcedure.setUpdateTime(new Date());
		}

		// 2. 创建工序
		daoFactory.getCommonDao().saveAllEntity(listAdd);
		daoFactory.getCommonDao().updateAllEntity(listUpdate);
		daoFactory.getCommonDao().deleteAllEntity(listDel);

		return list;
	}
}
