/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月12日 上午11:52:38
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.util.DateTimeUtil;
import com.huayin.common.util.ObjectHelper;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.exterior.dto.ResponseDto;
import com.huayin.printmanager.exterior.service.ExteriorRegisterService;
import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.ProcedureClass;
import com.huayin.printmanager.persist.entity.basic.ProductClass;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.Company_Menu;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.Role;
import com.huayin.printmanager.persist.entity.sys.Role_Menu;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.entity.sys.User_Role;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CompanyState;
import com.huayin.printmanager.persist.enumerate.CompanyType;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.persist.enumerate.InitStep;
import com.huayin.printmanager.persist.enumerate.State;
import com.huayin.printmanager.persist.enumerate.UserSource;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * TODO 输入类型说明
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月12日上午11:52:38, zhengby
 */
@Service
public class ExteriorRegisterServiceImpl extends BaseServiceImpl implements ExteriorRegisterService
{
	@Transactional
	@Override
	public boolean register(ResponseDto responseDto)
	{
		User user = new User();
		user.setMobile(responseDto.getRspBody().getMobile());
		user.setUserName(user.getMobile());// 用户名为手机号
		user.setPassword(UserUtils.entryptPassword(user.getMobile().trim()));//暂时以手机号为初始密码
		user.setEmail(responseDto.getRspBody().getEmail());
		user.setState(State.NORMAL);
		user.setCreateTime(new Date());
		user.setResource(UserSource.YSJ);//标记来源于印刷家
		user.setLoginErrCount(0);
		user.setLoginCount(1);
		user.setLastLoginTime(new Date());
		// 属于印刷家的字段
		user.setToken(responseDto.getRspBody().getToken());
		user.setUid(responseDto.getRspBody().getUid());
		user.setIsSign(BoolValue.YES); // 来源印刷家的账户默认绑定印刷家

		Company company = new Company();
		company.setId(UserUtils.createCompanyId());
		logger.info("开始添加基础资料" + company.getId());
		company.setName(responseDto.getRspBody().getExtendInfo().getCompanyName());
		company.setLinkName(responseDto.getRspBody().getLinkMan());
		company.setTel(responseDto.getRspBody().getMobile());
		company.setEmail(responseDto.getRspBody().getEmail());
//		company.setProvince(vo.getProvince());
//		company.setCity(vo.getCity());
//		company.setCounty(vo.getStreet());
		company.setExpireTime(DateTimeUtil.addDate(new Date(), Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.COMPANY_TRYDAY_NUM))));// 设置使用天数
		company.setCreateTime(new Date());
		company.setState(CompanyState.ONSALING);
		company.setIsFormal(BoolValue.NO);
		company.setInitStep(InitStep.INIT_BASIC);
		company.setStandardCurrency(CurrencyType.RMB);
		company.setType(CompanyType.NORMAL);
		company = serviceFactory.getPersistService().save(company);

		user.setCompanyId(company.getId());
		user.setCompany(company);
		user.setUserNo(UserUtils.createUserNo(company.getId()));
		user.setToken(responseDto.getRspBody().getToken()); // 印刷家的用户token

		serviceFactory.getPersistService().save(user);

		company = daoFactory.getCommonDao().getEntity(Company.class, company.getId());
		// 将注册用户ID关联到公司表
		company.setRegisterUserId(user.getId());
		company.setCreateName(user.getUserName());
		serviceFactory.getPersistService().update(company);

		// 快捷登录，注册成功后，并绑定账号
//		if (UserUtils.getSessionCache("userShareType") != null && UserUtils.getSessionCache("openid") != null)
//		{
//			UserShare userShare = new UserShare();
//			userShare.setIdentifier((String) UserUtils.getSessionCache("openid"));
//			userShare.setUserId(user.getId());
//			userShare.setUserType((UserShareType) UserUtils.getSessionCache("userShareType"));
//			serviceFactory.getUserShareService().save(userShare);
//		}
		System.out.println("开始登录");
		// 注册后自动登录
		UserUtils.login(user);
		// 登录后自动添加基础资料
		initBasic();
		// 注册完毕后退出登录
		UserUtils.loginout();
		return true;
	}
	@SuppressWarnings("unchecked")
	@Transactional
	private void initBasic()
	{
		Long begin = System.currentTimeMillis();
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
		List<ProductClass> list = findAllProductClassByInitCompanyId();
		for (ProductClass productClass : list)
		{
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
		// 复制全部材料信息
		// 获取模板公司的全部材料信息
		List<MaterialClass> materialClassList = findAllMaterialClassByInitCompanyId();

		List<Material> new_materilList = new ArrayList<>();
		for (MaterialClass materialClass : materialClassList)
		{
			MaterialClass new_materialClass = new MaterialClass();
			PropertyClone.copyProperties(new_materialClass, materialClass, false);
			new_materialClass.setCompanyId(UserUtils.getCompanyId());
			new_materialClass.setVersion(0l);
			new_materialClass.setId(null);
			new_materialClass = daoFactory.getCommonDao().saveEntity(new_materialClass);
			// 根据材料分类与公司id查找材料名称
			List<Material> materialList = findMaterialClassIdByInitCompany(materialClass.getId());
			for (Material _material : materialList)
			{
				Material material = serviceFactory.getMaterialService().get(SystemConfigUtil.getInitCompanyId(), _material.getId());
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

				new_materilList.add(new_material);
			}
		}
		if (new_materilList != null && new_materilList.size() > 0)
		{
			daoFactory.getCommonDao().saveAllEntity(new_materilList);
		}

		List<Procedure> new_procedureList = new ArrayList<Procedure>();
		List<ProcedureClass> procedureClassList = findAllProcedureClassByInitCompanyId();
		for (ProcedureClass procedureClass : procedureClassList)
		{
			ProcedureClass new_procedureClass = new ProcedureClass();
			PropertyClone.copyProperties(new_procedureClass, procedureClass, false, new String[] { "procedureTypeText" });
			new_procedureClass.setCompanyId(UserUtils.getCompanyId());
			new_procedureClass.setVersion(0l);
			new_procedureClass.setId(null);
			new_procedureClass = daoFactory.getCommonDao().saveEntity(new_procedureClass);
			List<Procedure> procedureList = findAllProcedureByInitComanyId(procedureClass.getId());
			for (Procedure procedure : procedureList)
			{
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
		_initMenu(2);

		Company company = UserUtils.getCompany();
		// Company company=daoFactory.getCommonDao().getEntity(Company.class, UserUtils.getCompanyId());
		company.setInitStep(InitStep.OVER);
		company.setSystemVersion(2);
		daoFactory.getCommonDao().updateEntity(company);
		UserUtils.updateUserPermissionIdentifier();
		UserUtils.updateSessionUser();
//		createCustomer();// 创建客户
//		createSupplier();// 创建供应商
//		createEmployee();// 创建员工
		// 创建产品
		long end = System.currentTimeMillis();
		logger.info("基础信息添加结束:" + UserUtils.getCompanyId() + ",运行时间:" + (end - begin) / 1000);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	private void _initBasic(BasicType type)
	{
		List<BaseBasicTableEntity> old_basicList = (List<BaseBasicTableEntity>) UserUtils.getBasicList(type.name());
		if (old_basicList != null && old_basicList.size() > 0)
		{// 已经初始化过数据不需要重新初始化
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
	
	/**
	 * <pre>
	 * 查找超级用户公司的基础信息-材料分类
	 * </pre>
	 * @param materialClassId
	 * @return
	 * @since 1.0, 2018年7月9日 上午9:16:17, zhengby
	 */
	private List<Material> findMaterialClassIdByInitCompany(Long materialClassId)
	{
		DynamicQuery query = new DynamicQuery(Material.class);
		query.eq("materialClassId", materialClassId);
		query.eq("isValid", BoolValue.YES);
		query.eq("companyId", SystemConfigUtil.getInitCompanyId());
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Material.class);
	}
	
	/**
	 * <pre>
	 * 查找超级用户公司的基础信息-所有产品
	 * </pre>
	 * @return
	 * @since 1.0, 2018年7月9日 上午9:17:34, zhengby
	 */
	public List<ProductClass> findAllProductClassByInitCompanyId()
	{
		DynamicQuery query = new DynamicQuery(ProductClass.class);
		query.asc("sort");
		query.eq("companyId", SystemConfigUtil.getInitCompanyId());
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, ProductClass.class);
	}
	
	/**
	 * <pre>
	 * 查找超级用户公司的基础信息-所有工序
	 * </pre>
	 * @param procedureClassId
	 * @return
	 * @since 1.0, 2018年7月9日 上午9:18:42, zhengby
	 */
	public List<Procedure> findAllProcedureByInitComanyId(Long procedureClassId)
	{
		DynamicQuery query = new DynamicQuery(Procedure.class);
		query.eq("companyId", SystemConfigUtil.getInitCompanyId());
		query.eq("procedureClassId", procedureClassId);
		query.getSqlResult().getSql();
		List<Procedure> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Procedure.class);
		return list;
	}
	
	/**
	 * <pre>
	 * 查找超级用户公司的基础信息-所有工序分类
	 * </pre>
	 * @return
	 * @since 1.0, 2018年7月9日 上午9:19:08, zhengby
	 */
	public List<ProcedureClass> findAllProcedureClassByInitCompanyId()
	{
		DynamicQuery query = new DynamicQuery(ProcedureClass.class);
		query.eq("companyId", SystemConfigUtil.getInitCompanyId());
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, ProcedureClass.class);
	}
	
	/**
	 * <pre>
	 * 查找超级用户公司的基础信息-所有材料分类
	 * </pre>
	 * @return
	 * @since 1.0, 2018年7月9日 上午9:19:26, zhengby
	 */
	public List<MaterialClass> findAllMaterialClassByInitCompanyId()
	{
		DynamicQuery query = new DynamicQuery(MaterialClass.class);
		query.eq("companyId", SystemConfigUtil.getInitCompanyId());
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, MaterialClass.class);
	}

}
