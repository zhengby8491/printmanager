/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.aspect;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huayin.common.web.core.UserAgentUtils;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.persist.dao.DaoFactory;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.sys.SystemLog;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.service.ServiceFactory;
import com.huayin.printmanager.utils.ServletUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架 - 记录用户操作日志
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Aspect
@Component
public class SystemLogAspect
{
	public static final String BILLNO = "billNo";

	public static final String PRODUCERESULT = "produceResult";

	public static final String USERNAME = "userName";

	@Autowired
	public ServiceFactory serviceFactory;

	@Autowired
	public DaoFactory daoFactory;

	// Controller层切点
	@Pointcut("@annotation(com.huayin.printmanager.domain.annotation.SystemControllerLog)")
	public void controllerAspect()
	{

	}

	/**
	 * <pre>
	 * 前置通知 用于拦截Controller层记录用户的操作
	 * </pre>
	 * @param joinPoint
	 * @since 1.0, 2017年10月25日 下午6:38:13, think
	 */
	@AfterReturning("controllerAspect()")
	public void doAfter(JoinPoint joinPoint)
	{
		try
		{
			SystemControllerLog systemControllerLog = getControllerMethodAnnotation(joinPoint);
			HttpServletRequest request = ServletUtils.getRequest();
			OperationResult operationResult = (OperationResult) request.getAttribute(SystemLogAspect.PRODUCERESULT);
			String billNo = (String) request.getAttribute(SystemLogAspect.BILLNO);
			User user = UserUtils.getUser();
			String employeeName = "";
			try
			{
				if (user == null)
				{
					String username = (String) request.getAttribute(SystemLogAspect.USERNAME);
					user = serviceFactory.getUserService().getByUserName(username);
					employeeName = daoFactory.getCommonDao().getEntity(Employee.class, user.getEmployeeId()).getName();
				}
				else
				{
					employeeName = UserUtils.getUserEmployeeName();
				}
			}
			catch (Exception e)
			{
				try
				{
					if (user.getEmployeeId() != null)
					{
						employeeName = daoFactory.getCommonDao().getEntity(Employee.class, user.getEmployeeId()).getName();
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			if (user != null)
			{

				SystemLog log = new SystemLog();
				log.setEmployeeName(employeeName);
				log.setUserId(user.getId());
				log.setCreateTime(new Date());
				log.setExecTime(new Date());
				log.setModule(systemControllerLog.BillTypeText() + (billNo != null ? "(" + billNo + ")" : ""));
				log.setOperationResult(systemControllerLog.Operation().getText().concat(systemControllerLog.Description()).concat(operationResult != null ? operationResult.getText() : systemControllerLog.OperationResult().getText()));
				log.setType(systemControllerLog.SystemLogType());
				log.setCompanyId(user.getCompanyId());
				log.setOperatorIp(UserAgentUtils.getRemoteAddr(request));
				log.setBrowser(UserAgentUtils.getBrowser(request).toString());
				log.setDeviceType(UserAgentUtils.getDeviceType(request).name());
				log.setUserAgent(UserAgentUtils.getUserAgent(request).toString());
				serviceFactory.getSystemLogService().addLog(log);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * <pre>
	 * 前置通知 用于拦截Controller层记录用户的操作
	 * </pre>
	 * @param joinPoint
	 * @since 1.0, 2017年10月25日 下午6:38:23, think
	 */
	@AfterThrowing("controllerAspect()")
	public void doAfterThrowing(JoinPoint joinPoint)
	{
		try
		{
			SystemControllerLog systemControllerLog = getControllerMethodAnnotation(joinPoint);
			HttpServletRequest request = ServletUtils.getRequest();
			String billNo = (String) request.getAttribute(SystemLogAspect.BILLNO);
			User user = UserUtils.getUser();
			if (user == null)
			{
				String username = request.getParameter(SystemLogAspect.USERNAME);
				user = serviceFactory.getUserService().getByUserName(username);
			}
			String employeeName = "";
			try
			{
				employeeName = UserUtils.getUserEmployeeName();
			}
			catch (Exception e)
			{
			}
			if (user != null)
			{

				SystemLog log = new SystemLog();
				log.setEmployeeName(employeeName);
				log.setUserId(user.getId());
				log.setCreateTime(new Date());
				log.setExecTime(new Date());
				log.setModule(systemControllerLog.BillTypeText() + (billNo != null ? "(" + billNo + ")" : ""));
				log.setOperationResult(systemControllerLog.Operation().getText().concat(systemControllerLog.Description()).concat(OperationResult.EXCEPTION.getText()));
				log.setType(systemControllerLog.SystemLogType());
				log.setCompanyId(user.getCompanyId());
				log.setOperatorIp(UserAgentUtils.getRemoteAddr(request));
				log.setBrowser(UserAgentUtils.getBrowser(request).toString());
				log.setDeviceType(UserAgentUtils.getDeviceType(request).name());
				log.setUserAgent(UserAgentUtils.getUserAgent(request).toString());
				serviceFactory.getSystemLogService().addLog(log);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * <pre>
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 * </pre>
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年10月25日 下午6:38:32, think
	 */
	public static SystemControllerLog getControllerMethodAnnotation(JoinPoint joinPoint) throws Exception
	{
		SystemControllerLog systemControllerLog = null;
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		// 反射生成目标对象
		Class<?> targetClass = Class.forName(targetName);
		// 获取目前对象的所有方法
		java.lang.reflect.Method[] methods = targetClass.getMethods();
		for (java.lang.reflect.Method method : methods)
		{
			// 方法名相同
			if (method.getName().equals(methodName))
			{
				Class<?>[] clazzs = method.getParameterTypes();
				// 参数长度相同，防止重载
				if (clazzs.length == arguments.length)
				{
					systemControllerLog = method.getAnnotation(SystemControllerLog.class);
					break;
				}
			}
		}
		return systemControllerLog;
	}
}
