/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.session.ExpiringSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.exception.ServiceResultFactory;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.DateTimeUtil;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.ErrorCodeConstants;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.DataShare;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.entity.sys.UserShare;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CompanyState;
import com.huayin.printmanager.persist.enumerate.CompanyType;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.persist.enumerate.InitStep;
import com.huayin.printmanager.persist.enumerate.State;
import com.huayin.printmanager.persist.enumerate.UserShareType;
import com.huayin.printmanager.persist.enumerate.UserSource;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.UserService;
import com.huayin.printmanager.service.sys.vo.RegisterVo;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 用户管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService
{
	private static final ExecutorService threadPool = Executors.newCachedThreadPool();

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#get(java.lang.Long)
	 */
	@Override
	public User get(Long id)
	{
		DynamicQuery query = new DynamicQuery(User.class);
		query.eq("id", id);
		User user = daoFactory.getCommonDao().getByDynamicQuery(query, User.class);
		return user;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#getAll()
	 */
	@Override
	public List<User> getAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(User.class);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, User.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#getById(java.lang.Long)
	 */
	@Override
	public User getById(Long id)
	{
		DynamicQuery query = null;

		if (UserUtils.isSystemCompany())
		{
			query = new DynamicQuery(User.class);
		}
		else
		{
			query = new CompanyDynamicQuery(User.class);
		}
		query.eq("id", id);
		User user = daoFactory.getCommonDao().getByDynamicQuery(query, User.class);
		return user;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#getByUserName(java.lang.String)
	 */
	public User getByUserName(String userName)
	{
		DynamicQuery query = new DynamicQuery(User.class);
		query.eq("userName", userName);
		User user = daoFactory.getCommonDao().getByDynamicQuery(query, User.class);
		if (user == null)
		{
			DynamicQuery query2 = new DynamicQuery(User.class);
			query2.eq("mobile", userName);
			user = daoFactory.getCommonDao().getByDynamicQuery(query2, User.class);
		}
		return user;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#findSharedEmployeeIds(java.lang.Long)
	 */
	@Override
	public Long[] findSharedEmployeeIds(Long userId)
	{
		try
		{
			User user = serviceFactory.getUserService().getById(userId);
			List<Long> employeeIds = new ArrayList<Long>();
			DynamicQuery query = new CompanyDynamicQuery(DataShare.class);
			query.eq("sharedUserId", userId);
			// query.eq("companyId", UserUtils.getCompanyId());
			List<DataShare> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, DataShare.class);
			for (DataShare d : list)
			{
				if (employeeIds.contains(d.getEmployeeId()))
				{
					continue;
				}
				employeeIds.add(d.getEmployeeId());
			}
			if (employeeIds.size() > 0 && !employeeIds.contains(user.getEmployeeId()))
			{
				// 添加自己
				if (user.getEmployeeId() != null)
				{
					employeeIds.add(user.getEmployeeId());
				}
			}
			int size = employeeIds.size();
			return (Long[]) employeeIds.toArray(new Long[size]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#findByCondition(java.util.Date, java.util.Date,
	 * java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public SearchResult<User> findByCondition(Date dateMin, Date dateMax, String userName, Integer pageIndex, Integer pageSize)
	{
		DynamicQuery query = new CompanyDynamicQuery(User.class);
		query.setIsSearchTotalCount(true);
		if (dateMin != null)
		{
			query.ge("createTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(dateMin) + " 00:00:01", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (dateMax != null)
		{
			query.le("createTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(dateMax) + " 23:59:59", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (StringUtils.isNotEmpty(userName))
		{
			query.like("userName", "%" + userName + "%");
		}
		// query.eq("companyId", UserUtils.getCompanyId());
		query.desc("createTime");
		query.setPageIndex(pageIndex);
		query.setPageSize(pageSize);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, User.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#findPwd(javax.servlet.http.HttpServletRequest, java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	public ServiceResult<Boolean> findPwd(HttpServletRequest request, Long userId, String newPwd)
	{
		ServiceResult<Boolean> result = ServiceResultFactory.getServiceResult(false);
		User _user = serviceFactory.getDaoFactory().getUserDao().get(userId);
		if (_user.getCompanyId().equals(SystemConfigUtil.getConfig(SysConstants.ADMIN_COMPANYID)))
		{
			result.setMessage("权限非法");
			result.setCode(ErrorCodeConstants.CommonCode.COMMON_PERMISSION_ERROR);
			result.setIsSuccess(false);
		}
		else
		{// 判断系统权限，防止恶意篡改
			_user.setPassword(UserUtils.entryptPassword(newPwd));
			serviceFactory.getDaoFactory().getUserDao().update(_user);
		}
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#save(com.huayin.printmanager.persist.entity.sys.User)
	 */
	@Override
	@Transactional
	public User save(User user)
	{
		return daoFactory.getCommonDao().saveEntity(user);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#updateShared(java.lang.Long, java.lang.Long[])
	 */
	@Transactional
	public void updateShared(Long sharedUserId, Long[] employeeIds)
	{
		DynamicQuery query = new CompanyDynamicQuery(DataShare.class);
		query.eq("sharedUserId", sharedUserId);
		List<DataShare> deleteList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, DataShare.class);
		daoFactory.getCommonDao().deleteAllEntity(deleteList);
		List<DataShare> addList = new ArrayList<DataShare>();
		if (employeeIds != null)
		{
			for (Long employeeId : employeeIds)
			{
				DataShare newDateShare = new DataShare();
				newDateShare.setSharedUserId(sharedUserId);
				newDateShare.setEmployeeId(employeeId);
				newDateShare.setCompanyId(UserUtils.getCompanyId());
				addList.add(newDateShare);
			}
		}
		daoFactory.getCommonDao().saveAllEntity(addList);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#checkShared(java.lang.Long, java.lang.Long)
	 */
	public Boolean checkShared(Long sharedUserId, Long employeeId)
	{
		DynamicQuery query = new CompanyDynamicQuery(DataShare.class);
		query.eq("sharedUserId", sharedUserId);
		query.eq("employeeId", employeeId);
		// query.eq("companyId", UserUtils.getCompanyId());
		List<DataShare> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, DataShare.class);
		return list.size() > 0;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#resetPwd(javax.servlet.http.HttpServletRequest,
	 * java.lang.Long, java.lang.String)
	 */
	@Override
	public ServiceResult<Boolean> resetPwd(HttpServletRequest request, Long userId, String newPwd)
	{
		ServiceResult<Boolean> result = ServiceResultFactory.getServiceResult(false);
		User _user = serviceFactory.getDaoFactory().getUserDao().get(userId);
		if (UserUtils.getCompanyId().equals(_user.getCompanyId()))
		{// 判断系统权限，防止恶意篡改
			_user.setPassword(UserUtils.entryptPassword(newPwd));
			serviceFactory.getDaoFactory().getUserDao().update(_user);
		}
		else
		{
			result.setMessage("权限非法");
			result.setCode(ErrorCodeConstants.CommonCode.COMMON_PERMISSION_ERROR);
			result.setIsSuccess(false);
		}
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#existUserName(java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean existUserName(String userName, Long unUserId)
	{
		DynamicQuery query = new DynamicQuery(User.class);
		query.eq("userName", userName);
		if (unUserId != null)
		{
			query.ne("id", unUserId);
		}
		return daoFactory.getCommonDao().getByDynamicQuery(query, User.class) == null ? false : true;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#existMobile(java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean existMobile(String mobile, Long unUserId)
	{
		DynamicQuery query = new DynamicQuery(User.class);
		query.eq("mobile", mobile);
		if (unUserId != null)
		{
			query.ne("id", unUserId);
		}
		return daoFactory.getCommonDao().getByDynamicQuery(query, User.class) == null ? false : true;

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#onLoginSuccess(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ServiceResult<User> onLoginSuccess(HttpServletRequest request)
	{
		ServiceResult<User> result = ServiceResultFactory.getServiceResult(User.class);
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if (user != null)
		{
			// 校验用户状态
			User update_user = serviceFactory.getDaoFactory().getUserDao().get(user.getId());

			update_user.setLoginCount((user.getLoginCount() == null ? 0 : user.getLoginCount()) + 1);
			update_user.setLastLoginIp(request.getRemoteAddr());
			update_user.setLastLoginTime(new Date());
			serviceFactory.getPersistService().update(update_user);
			result.setReturnValue(user);
			// 开辟线程，判断是否单一用户登录模式下，是则踢出其它已登录用户
			final String currSessionId = request.getRequestedSessionId();
			final String userName = user.getUserName();
			final String userId = String.valueOf(user.getId());
			if (SysConstants.NO.equals(SystemConfigUtil.getConfig(SysConstants.DEFAULT_SAME_USER_LOGIN_MULTIPLE)) && !userName.equals(SystemConfigUtil.getConfig(SysConstants.SITE_DEMO_USERNAME)))
			{// 多地点重复登录，且非演示用户，则剔除其它已登录用户
				threadPool.execute(new Runnable()
				{
					public void run()
					{
						Collection<ExpiringSession> sessions = serviceFactory.getDaoFactory().getSessionDao().getActiveSessions(true, userId, currSessionId);
						if (sessions.size() > 0)
						{
							for (ExpiringSession session : sessions)
							{
								logger.warn("login: user[" + userName + "],在其它地方登录，被迫下线,踢出旧的sessionId[" + session.getId() + "],保留新的sessionId=" + currSessionId + "");
								serviceFactory.getDaoFactory().getSessionDao().delete(session.getId());
							}
						}
					}
				});
			}
			if (!State.NORMAL.equals(user.getState()))
			{
				result.setIsSuccess(false);
				result.setMessage("账号已停用");
			}
			else
			{
				Company company = serviceFactory.getCompanyService().get(user.getCompanyId());
				if (company == null)
				{
					result.setIsSuccess(false);
					result.setMessage("没有找到公司信息");
				}
				else if (company.getExpireTime() == null || company.getExpireTime().before(new Date()))
				{
					result.setIsSuccess(false);
					if (company.getIsFormal() == BoolValue.YES)
					{
						result.setMessage("尊敬的印管家客户，您的印管家已过期，如果您需要继续使用，请联系深圳华印，联系电话：400-800-8755");
					}
					else
					{
						result.setMessage("尊敬的印管家客户，您的印管家试用已过期，如果您需要购买正式版，请联系深圳华印续费，联系电话：400-800-8755");
					}

				}
				else
				{
					// 设置用户公司
					user.setCompany(company);
					// 更新用户权限
					UserUtils.updateUserPermissionIdentifier();
					UserUtils.updateSessionUser();
				}
			}
		}
		else
		{
			result.setIsSuccess(false);
			result.setMessage("由于目前登录人数过多，请稍后登录!");
		}

		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserService#register(com.huayin.printmanager.service.sys.vo.RegisterVo,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	@Transactional
	public ServiceResult<User> register(RegisterVo vo, HttpServletRequest request)
	{
		User user = new User();
		user.setUserName(vo.getUserName());
		user.setMobile(vo.getMobile());
		user.setPassword(UserUtils.entryptPassword(vo.getPassword().trim()));
		user.setState(State.NORMAL);
		user.setCreateTime(new Date());
		user.setResource(UserSource.WEB);
		user.setLoginErrCount(0);
		user.setLoginCount(1);
		user.setLastLoginIp(vo.getIp());
		user.setLastLoginTime(new Date());

		Company company = new Company();
		company.setId(UserUtils.createCompanyId());
		company.setExpireTime(DateTimeUtil.addDate(new Date(), Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.COMPANY_TRYDAY_NUM))));// 设置使用天数
		company.setCreateTime(new Date());
		company.setState(CompanyState.ONSALING);
		company.setIsFormal(BoolValue.NO);
		company.setInitStep(InitStep.INIT_COMPANY);
		company.setStandardCurrency(CurrencyType.RMB);
		company.setType(CompanyType.NORMAL);
		company.setTel(vo.getMobile());
		company = daoFactory.getCommonDao().saveEntity(company);

		// }
		user.setCompanyId(company.getId());
		user.setCompany(company);
		user.setUserNo(UserUtils.createUserNo(company.getId()));

		serviceFactory.getUserService().save(user);

		company = daoFactory.getCommonDao().getEntity(Company.class, company.getId());
		// 将注册用户ID关联到公司表
		company.setRegisterUserId(user.getId());
		company.setCreateName(user.getUserName());
		daoFactory.getCommonDao().updateEntity(company);

		// 快捷登录，注册成功后，并绑定账号
		if (UserUtils.getSessionCache("userShareType") != null && UserUtils.getSessionCache("openid") != null)
		{
			UserShare userShare = new UserShare();
			userShare.setIdentifier((String) UserUtils.getSessionCache("openid"));
			userShare.setUserId(user.getId());
			userShare.setUserType((UserShareType) UserUtils.getSessionCache("userShareType"));
			serviceFactory.getUserShareService().save(userShare);
		}

		// 注册后自动登录
		UserUtils.login(user);
		return ServiceResultFactory.getServiceResult(user);
	}
}
