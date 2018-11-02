/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月16日 上午10:04:45
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.util.DateTimeUtil;
import com.huayin.common.util.ObjectHelper;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.helper.service.OfferHelper;
import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.basic.ExchangeRate;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.ProcedureClass;
import com.huayin.printmanager.persist.entity.basic.ProductClass;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.basic.UnitConvert;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.offer.OfferWaste;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.Company_Menu;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.Role;
import com.huayin.printmanager.persist.entity.sys.Role_Menu;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.entity.sys.User_Role;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.persist.enumerate.InitStep;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.SmsType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.CompanyService;
import com.huayin.printmanager.service.sys.vo.CompanyVo;
import com.huayin.printmanager.service.sys.vo.RegisterInitVo;
import com.huayin.printmanager.service.sys.vo.RegisterMaterialVo;
import com.huayin.printmanager.service.sys.vo.RegisterProcedureVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;
import com.huayin.printmanager.utils.ValidateUtils;

/**
 * <pre>
 * 系统模块 - 公司管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月16日, 整理规范
 */
@Service
public class CompanyServiceImpl extends BaseServiceImpl implements CompanyService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CompanyService#get(java.lang.String)
	 */
	@Override
	public Company get(String id)
	{
		return daoFactory.getCommonDao().getEntity(Company.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CompanyService#lock(java.lang.String)
	 */
	@Override
	public Company lock(String id)
	{
		return daoFactory.getCommonDao().lockObject(Company.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CompanyService#getExchangeRate(com.huayin.printmanager.persist.enumerate.
	 * CurrencyType)
	 */
	@Override
	public ExchangeRate getExchangeRate(CurrencyType currencyType)
	{
		ExchangeRate exchangeRate = null;
		try
		{
			DynamicQuery query = new DynamicQuery(ExchangeRate.class);
			query.eq("standardCurrencyType", UserUtils.getCompany().getStandardCurrency());
			query.eq("currencyType", currencyType);
			exchangeRate = daoFactory.getCommonDao().getByDynamicQuery(query, ExchangeRate.class);
			if (exchangeRate == null)
			{
				exchangeRate = new ExchangeRate();
				exchangeRate.setRate(new BigDecimal(1));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return exchangeRate;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.CompanyService#findByCondition(com.huayin.printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<CompanyVo> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(Company.class, "c");
		query.addProjection(Projections.property("c, u"));
		query.createAlias(User.class, JoinType.LEFTJOIN, "u", "c.registerUserId=u.id");

		if (queryParam.getDateMin() != null)
		{
			query.ge("c.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("c.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getCompanyName()))
		{
			query.like("c.name", "%" + queryParam.getCompanyName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getCompanyLinkName()))
		{
			query.like("c.linkName", "%" + queryParam.getCompanyLinkName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getCompanyTel()))
		{
			query.like("c.tel", "%" + queryParam.getCompanyTel() + "%");
		}
		if (queryParam.getExpireTimeMin() != null)
		{
			query.ge("c.expireTime", queryParam.getExpireTimeMin());
		}
		if (queryParam.getExpireTimeMax() != null)
		{
			query.le("c.expireTime", queryParam.getExpireTimeMax());
		}
		if (queryParam.getCompanyState() != null)
		{
			query.eq("c.state", queryParam.getCompanyState());
		}
		if (queryParam.getCompanyType() != null)
		{
			query.eq("c.type", queryParam.getCompanyType());
		}
		if (queryParam.getInitStep() != null)
		{
			query.eq("c.initStep", queryParam.getInitStep());
		}
		if (queryParam.getIsFormal() != null)
		{
			query.eq("c.isFormal", queryParam.getIsFormal());
		}
		if (queryParam.getUserName() != null)
		{
			query.like("u.userName", "%" + queryParam.getUserName() + "%");
		}

		if (!UserUtils.getCompanyId().equals(SystemConfigUtil.getInitCompanyId()))
		{// 权限过滤
			query.eq("c.id", UserUtils.getCompanyId());
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("c.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<CompanyVo> result = new SearchResult<CompanyVo>();
		result.setResult(new ArrayList<CompanyVo>());
		for (Object[] objs : temp_result.getResult())
		{
			Company c = (Company) objs[0];

			User u = (User) objs[1];
			CompanyVo vo = new CompanyVo();
			vo.setCompany(c);
			vo.setRegisterUser(u);
			result.getResult().add(vo);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CompanyService#save(com.huayin.printmanager.persist.entity.sys.Company)
	 */
	@Override
	@Transactional
	public Company save(Company company)
	{
		return daoFactory.getCommonDao().saveEntity(company);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.CompanyService#initBasic(com.huayin.printmanager.service.sys.vo.RegisterInitVo)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void initBasic(RegisterInitVo initVo)
	{
		_initBasic(BasicType.SUPPLIERCLASS);
		_initBasic(BasicType.CUSTOMERCLASS);
		_initBasic(BasicType.DEPARTMENT);
		_initBasic(BasicType.POSITION);
		_initBasic(BasicType.TAXRATE);
		_initBasic(BasicType.PAYMENTCLASS);
		_initBasic(BasicType.SETTLEMENTCLASS);
		_initBasic(BasicType.WAREHOUSE);
		_initBasic(BasicType.DELIVERYCLASS);
		_initBasic(BasicType.UNIT);

		List<ProductClass> new_productClassList = new ArrayList<ProductClass>();
		for (Long productClassId : initVo.getProductClassIdList())
		{
			ProductClass productClass = serviceFactory.getProductClassService().get(SystemConfigUtil.getInitCompanyId(), productClassId);

			ProductClass new_productClass = new ProductClass();
			PropertyClone.copyProperties(new_productClass, productClass, false, new String[] { "productTypeText" });

			new_productClass.setCompanyId(UserUtils.getCompanyId());
			new_productClass.setVersion(0l);
			new_productClass.setId(null);
			new_productClassList.add(new_productClass);
		}
		if (new_productClassList != null && new_productClassList.size() > 0)
		{
			daoFactory.getCommonDao().saveAllEntity(new_productClassList);
		}

		List<Material> new_materialList = new ArrayList<Material>();
		for (RegisterMaterialVo vo : initVo.getMaterialVoList())
		{
			MaterialClass materialClass = serviceFactory.getMaterialClassService().get(SystemConfigUtil.getInitCompanyId(), vo.getMaterialClassId());
			// MaterialClass new_materialClass = ObjectUtils.clone(materialClass);

			MaterialClass new_materialClass = new MaterialClass();
			PropertyClone.copyProperties(new_materialClass, materialClass, false);
			new_materialClass.setCompanyId(UserUtils.getCompanyId());
			new_materialClass.setVersion(0l);
			new_materialClass.setId(null);
			new_materialClass = daoFactory.getCommonDao().saveEntity(new_materialClass);
			for (Material _material : vo.getMaterialList())
			{
				Material material = serviceFactory.getMaterialService().get(SystemConfigUtil.getInitCompanyId(), _material.getId());
				// Material new_material = ObjectUtils.clone(material);

				Material new_material = new Material();
				PropertyClone.copyProperties(new_material, material, false, new String[] { "saleUnitIdName", "stockUnitName", "produceUnitName", "materialClassName", "valuationUnitName" });
				new_material.setMaterialClassId(new_materialClass.getId());
				new_material.setCompanyId(UserUtils.getCompanyId());
				new_material.setVersion(0l);
				new_material.setId(null);
				new_material.setCreateName(UserUtils.getUserName());
				new_material.setCreateTime(new Date());

				String stockUnitName = UserUtils.getPublicBasicInfoFiledValue(BasicType.UNIT.name(), new_material.getStockUnitId(), "name").toString();
				Long newStockUnitId = ((List<Unit>) UserUtils.getBasicListParam(BasicType.UNIT.name(), "name", stockUnitName)).get(0).getId();
				new_material.setStockUnitId(newStockUnitId);

				String saleUnitName = UserUtils.getPublicBasicInfoFiledValue(BasicType.UNIT.name(), new_material.getSaleUnitId(), "name").toString();
				Long newSaleUnitId = ((List<Unit>) UserUtils.getBasicListParam(BasicType.UNIT.name(), "name", saleUnitName)).get(0).getId();
				new_material.setSaleUnitId(newSaleUnitId);

				String valuationUnitName = UserUtils.getPublicBasicInfoFiledValue(BasicType.UNIT.name(), new_material.getValuationUnitId(), "name").toString();
				Long newValuationUnitId = ((List<Unit>) UserUtils.getBasicListParam(BasicType.UNIT.name(), "name", valuationUnitName)).get(0).getId();
				new_material.setValuationUnitId(newValuationUnitId);

				String produceUnitName = UserUtils.getPublicBasicInfoFiledValue(BasicType.UNIT.name(), new_material.getProduceUnitId(), "name").toString();
				Long newProduceUnitId = ((List<Unit>) UserUtils.getBasicListParam(BasicType.UNIT.name(), "name", produceUnitName)).get(0).getId();
				new_material.setProduceUnitId(newProduceUnitId);

				new_materialList.add(new_material);
			}
		}
		if (new_materialList != null && new_materialList.size() > 0)
		{
			daoFactory.getCommonDao().saveAllEntity(new_materialList);
		}

		List<Procedure> new_procedureList = new ArrayList<Procedure>();
		for (RegisterProcedureVo vo : initVo.getProcedureVoList())
		{
			ProcedureClass procedureClass = serviceFactory.getProcedureClassService().get(SystemConfigUtil.getInitCompanyId(), vo.getProcedureClassId());
			// ProcedureClass new_procedureClass = ObjectUtils.clone(procedureClass);

			ProcedureClass new_procedureClass = new ProcedureClass();
			PropertyClone.copyProperties(new_procedureClass, procedureClass, false, new String[] { "procedureTypeText" });
			new_procedureClass.setCompanyId(UserUtils.getCompanyId());
			new_procedureClass.setVersion(0l);
			new_procedureClass.setId(null);
			new_procedureClass = daoFactory.getCommonDao().saveEntity(new_procedureClass);
			for (Procedure _procedure : vo.getProcedureList())
			{
				Procedure procedure = serviceFactory.getProcedureService().get(SystemConfigUtil.getInitCompanyId(), _procedure.getId());
				// Procedure new_procedure = ObjectUtils.clone(procedure);
				Procedure new_procedure = new Procedure();
				PropertyClone.copyProperties(new_procedure, procedure, false, new String[] { "procedureClassName" });
				new_procedure.setProcedureClassId(new_procedureClass.getId());
				new_procedure.setCompanyId(UserUtils.getCompanyId());
				new_procedure.setVersion(0l);
				new_procedure.setId(null);
				new_procedure.setCreateName(UserUtils.getUserName());
				new_procedure.setCreateTime(new Date());
				new_procedureList.add(new_procedure);
			}
		}
		if (new_procedureList != null && new_procedureList.size() > 0)
		{
			daoFactory.getCommonDao().saveAllEntity(new_procedureList);
		}
		// 其他数据初始化导入
		/*
		 * 1.部门信息 2.职位信息 3.税率信息 4.付款方式 5.结算方式 6.单位信息 7.仓库信息 8.送货方式
		 */

		// 初始化菜单数据
		_initMenu(initVo.getVersion());

		Company company = UserUtils.getCompany();
		// Company company=daoFactory.getCommonDao().getEntity(Company.class, UserUtils.getCompanyId());
		company.setInitStep(InitStep.OVER);
		company.setSystemVersion(initVo.getVersion());
		daoFactory.getCommonDao().updateEntity(company);
		UserUtils.updateUserPermissionIdentifier();
		UserUtils.updateSessionUser();
	}

	/**
	 * <pre>
	 * 导入基础资料
	 * </pre>
	 * @param type
	 * @since 1.0, 2017年10月25日 下午5:03:00, think
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	private void _initBasic(BasicType type)
	{
		List<BaseBasicTableEntity> old_basicList = (List<BaseBasicTableEntity>) UserUtils.getBasicList(type.name());
		if (old_basicList != null && old_basicList.size() > 0)
		{// 已经初始化过数据不需要重新初始化
			logger.info("已经初始化过[" + type.getDescription() + "]数据不需要重新初始化");
			return;
		}

		List<BaseBasicTableEntity> new_basicList = new ArrayList<BaseBasicTableEntity>();
		List<BaseBasicTableEntity> basicList = (List<BaseBasicTableEntity>) UserUtils.getBasicList(true, type.name());
		for (BaseBasicTableEntity basic : basicList)
		{
			BaseBasicTableEntity new_basic = ObjectHelper.byteClone(basic);
			new_basic.setCompanyId(UserUtils.getCompanyId());
			new_basic.setVersion(0l);
			new_basic.setId(null);
			if (new_basic instanceof Warehouse)
			{
				((Warehouse) new_basic).setIsBegin(BoolValue.NO);
			}
			new_basicList.add(new_basic);
		}
		daoFactory.getCommonDao().saveAllEntity(new_basicList);

		UserUtils.clearCacheBasic(type);

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CompanyService#initUnitConvert()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void initUnitConvert()
	{
		// 添加新增加的基础单位
		// 先查询模板的基础单位字段，查询当前账户是否有此单位，没有则复制模板的基础单位，并保存
		List<Unit> unitList = (List<Unit>) UserUtils.getBasicList(true, BasicType.UNIT.name());
		for (Unit basicUnit : unitList)
		{
			Unit unit = new Unit();
			unit = serviceFactory.getUnitService().getByName(basicUnit.getName());
			if ("".equals(unit) || null == unit)
			{
				// 1.首先复制模板的基础单位字段，在把模板换算单位复制，并保存到当前用户下
				unit = ObjectHelper.byteClone(basicUnit);
				unit.setCompanyId(UserUtils.getCompanyId());
				unit.setId(null);
				unit.setVersion(0l);
				serviceFactory.getUnitService().save(unit);
			}
		}
		List<UnitConvert> old_basicList = (List<UnitConvert>) UserUtils.getBasicList(BasicType.UNITCONVERT.name());
		// 判断是否已经添加初始化计算公式（origin="YES"）
		for (int i = 0; i < old_basicList.size(); i++)
		{
			if ("YES".equals(old_basicList.get(i).getIsOrigin()))
			{
				return;
			}
		}
		for (UnitConvert unitCovert : old_basicList)
		{
			unitCovert.getFormula();

		}
		List<UnitConvert> new_basicList = new ArrayList<UnitConvert>();

		List<UnitConvert> basicList = (List<UnitConvert>) UserUtils.getBasicList(true, BasicType.UNITCONVERT.name());

		for (UnitConvert basic : basicList)
		{
			UnitConvert new_basic = ObjectHelper.byteClone(basic);
			String[] str = basic.getName().split("换");
			Unit source_unit = serviceFactory.getUnitService().getByName(str[0]);
			if (!"".equals(source_unit) && source_unit != null)
			{
				new_basic.setSourceUnitId(source_unit.getId());
			}
			Unit convert_unit = serviceFactory.getUnitService().getByName(str[1]);
			if (!"".equals(convert_unit) && convert_unit != null)
			{
				new_basic.setConversionUnitId(convert_unit.getId());
			}
			new_basic.setCompanyId(UserUtils.getCompanyId());
			new_basic.setVersion(0l);
			new_basic.setId(null);

			new_basicList.add(new_basic);
		}
		if (new_basicList.size() != 0)
		{
			daoFactory.getCommonDao().saveAllEntity(new_basicList);
		}
		UserUtils.clearCacheBasic(BasicType.UNITCONVERT);
	}

	/**
	 * <pre>
	 * 初始化公司菜单
	 * </pre>
	 * @param version
	 * @since 1.0, 2017年10月25日 下午5:03:52, think
	 */
	@Transactional
	private void _initMenu(Integer version)
	{
		List<Menu> old_cmList = serviceFactory.getMenuService().findAll(UserUtils.getCompanyId());
		if (old_cmList != null && old_cmList.size() > 0)
		{
			logger.info("已经初始化过菜单数据不需要重新初始化");
			return;
		}

		List<Company_Menu> new_cmList = new ArrayList<Company_Menu>();

		// 默认【简易版】（业务流程拆分）
		long roleId = Long.parseLong(SystemConfigUtil.getInitCompanyMenuRoleId2());
		if (version == 2)
			roleId = Long.parseLong(SystemConfigUtil.getInitCompanyMenuRoleId());

		List<Role_Menu> rmList = serviceFactory.getMenuService().findMenuIdByRoleId(roleId);
		for (Role_Menu rm : rmList)
		{
			Company_Menu company_menu = new Company_Menu();
			company_menu.setCompanyId(UserUtils.getCompanyId());
			company_menu.setMenuId(rm.getMenuId());
			new_cmList.add(company_menu);
		}
		daoFactory.getCommonDao().saveAllEntity(new_cmList);

		_initRole(version);
	}

	/**
	 * <pre>
	 * 初始化角色
	 * </pre>
	 * @param version
	 * @since 1.0, 2017年10月25日 下午5:04:03, think
	 */
	@Transactional
	private void _initRole(Integer version)
	{
		List<Role_Menu> new_rmList = new ArrayList<Role_Menu>();

		// 默认【简易版】（业务流程拆分）
		String roleIds = SystemConfigUtil.getInitCompanyRoleIds2();
		long rid = Long.parseLong(SystemConfigUtil.getInitCompanyMenuRoleId2());
		if (version == 2)
		{
			roleIds = SystemConfigUtil.getInitCompanyRoleIds();
			rid = Long.parseLong(SystemConfigUtil.getInitCompanyMenuRoleId());
		}
		for (String roleId : roleIds.split(","))
		{
			Role template_role = serviceFactory.getDaoFactory().getCommonDao().getEntity(Role.class, Long.parseLong(roleId));
			// 初始化角色表
			Role new_role = new Role();
			new_role.setCompanyId(UserUtils.getCompanyId());
			new_role.setName(template_role.getName());
			new_role.setCreateName(UserUtils.getUserName());
			new_role.setCreateTime(new Date());
			new_role = daoFactory.getCommonDao().saveEntity(new_role);

			if (template_role.getId().equals(rid))
			{
				// 初始化用户管理员角色
				User_Role ur = new User_Role();
				ur.setRoleId(new_role.getId());
				ur.setUserId(UserUtils.getUserId());
				ur.setCompanyId(UserUtils.getCompanyId());
				daoFactory.getCommonDao().saveEntity(ur);
				new_role.setName("管理员");
				new_role = daoFactory.getCommonDao().updateEntity(new_role);
			}

			// 初始化角色菜单表
			List<Role_Menu> template_rmList = serviceFactory.getMenuService().findMenuIdByRoleId(template_role.getId());

			for (Role_Menu rm : template_rmList)
			{
				Role_Menu new_rm = new Role_Menu();
				new_rm.setRoleId(new_role.getId());
				new_rm.setMenuId(rm.getMenuId());
				new_rm.setCompanyId(UserUtils.getCompanyId());
				new_rmList.add(new_rm);
			}
		}

		daoFactory.getCommonDao().saveAllEntity(new_rmList);

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CompanyService#initWasteSetting()
	 */
	@Override
	@Transactional
	public void initWasteSetting()
	{
		/**
		 * <p>业务流程</p>
		 * 1. 查询当前公司的损耗设置（从缓存中查询）
		 * 2. 如果当前公司的损耗设置为空（或者长度不等于OfferType的枚举长度），则同步超级账号的损耗设置
		 *   2.1 清空当前公司所有的损耗
		 *   2.2 查询超级账号的损耗设置
		 *   2.3 同步损耗到当前公司
		 */

		// 1.查询当前公司的损耗设置
		List<OfferWaste> wasteSettingList = OfferHelper.findWasteSetting();

		// 2. 如果当前公司的损耗设置为空（或者长度不等于OfferType的枚举长度），则同步超级账号的损耗设置
		if (wasteSettingList.size() == 0 || OfferType.values().length != wasteSettingList.size())
		{
			// 2.1 清空当前公司所有的损耗
			daoFactory.getCommonDao().deleteAllEntity(wasteSettingList);

			// 2.2 查询超级账号的损耗设置
			List<OfferWaste> superWasteSettingList = OfferHelper.findAdminWasteSetting();

			// 2.3 同步损耗到当前公司
			List<OfferWaste> insertList = Lists.newArrayList();
			for (OfferWaste wasteSetting : superWasteSettingList)
			{
				OfferWaste newWasteSetting = ObjectHelper.byteClone(wasteSetting);
				newWasteSetting.setCompanyId(UserUtils.getCompanyId());
				newWasteSetting.setVersion(0l);
				newWasteSetting.setId(null);
				insertList.add(newWasteSetting);
			}
			// 存入数据库
			daoFactory.getCommonDao().saveAllEntity(insertList);

			// 更新缓存
			OfferHelper.putWasteSettingCache(UserUtils.getCompanyId(), insertList);

			logger.info("初始化公司[" + UserUtils.getCompanyId() + ", " + UserUtils.getCompany().getName() + "]的损耗设置");
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CompanyService#existCompanyName(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean existCompanyName(String name, String unCompanyId)
	{
		DynamicQuery query = new DynamicQuery(Company.class);
		query.eq("name", name);
		if (unCompanyId != null)
		{
			query.ne("id", unCompanyId.trim());
		}
		return daoFactory.getCommonDao().getByDynamicQuery(query, Company.class) == null ? false : true;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CompanyService#expireNotify()
	 */
	@Override
	public void expireNotify()
	{
		List<Company> list = expireQty(BoolValue.YES, Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.COMPANY_EXPIRE_NOTIFY_DAY)));
		if (list != null && list.size() > 0)
		{
			String msg = SystemConfigUtil.getConfig(SysConstants.COMPANY_EXPIRE_NOTIFY_CONTENT);
			for (Company c : list)
			{
				try
				{
					String mobile = null;
					if (c.getTel() != null && ValidateUtils.checkMobile(c.getTel()))
					{
						mobile = c.getTel();
					}
					else
					{// 取注册用户手机
						String userMobile = daoFactory.getUserDao().get(c.getRegisterUserId()).getMobile();
						if (userMobile != null && ValidateUtils.checkMobile(userMobile))
						{
							mobile = userMobile;
						}
					}
					if (mobile != null)
					{
						msg = msg.replace("{companyName}", c.getName());
						Integer hasDay = DateTimeUtil.getDayNum(new Date(), c.getExpireTime());
						msg = msg.replace("{hasDay}", String.valueOf(hasDay));
						serviceFactory.getSmsSendService().sendNow(mobile, msg, SmsType.WARN);
					}
				}
				catch (Exception e)
				{
					logger.error(c.getName() + ",到期提醒短信发送异常", e);
				}
			}
			serviceFactory.getSmsSendService().sendSystemAlarm("【印管家】共有" + list.size() + "个客户即将到期,请通知客服提醒客户并后续跟进！");
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.CompanyService#experienceNotify()
	 */
	@Override
	public void experienceNotify()
	{
		List<Company> list = expireQty(BoolValue.NO, 3);
		if (list != null && list.size() > 0)
		{
			String msg = "【印管家】尊敬的印管家客户,您试用的印管家还有3天就到期了，如果您需要购买，请联系深圳华印，联系电话：400-800-8755";
			for (Company c : list)
			{
				try
				{
					String mobile = null;
					if (c.getTel() != null && ValidateUtils.checkMobile(c.getTel()))
					{
						mobile = c.getTel();
					}
					else
					{// 取注册用户手机
						String userMobile = daoFactory.getUserDao().get(c.getRegisterUserId()).getMobile();
						if (userMobile != null && ValidateUtils.checkMobile(userMobile))
						{
							mobile = userMobile;
						}
					}
					if (mobile != null)
					{
						msg = msg.replace("{companyName}", c.getName());
						Integer hasDay = DateTimeUtil.getDayNum(new Date(), c.getExpireTime());
						msg = msg.replace("{hasDay}", String.valueOf(hasDay));
						serviceFactory.getSmsSendService().sendNow(mobile, msg, SmsType.WARN);
					}
				}
				catch (Exception e)
				{
					logger.error(c.getName() + ",到期提醒短信发送异常", e);
				}
			}
			serviceFactory.getSmsSendService().sendSystemAlarm("【印管家】共有" + list.size() + "个体验客户即将到期,请通知客服提醒客户并后续跟进！");
		}
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.CompanyService#expireQty(com.huayin.printmanager.persist.enumerate.BoolValue,
	 * java.lang.Integer)
	 */
	@Override
	public List<Company> expireQty(BoolValue isFormal, Integer expireDay)
	{
		DynamicQuery query = new DynamicQuery(Company.class);
		query.le("expireTime", DateTimeUtil.addDate(new Date(), expireDay));
		query.gt("expireTime", new Date());
		query.eq("isFormal", isFormal);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Company.class);
	}

}
