/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月11日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.huayin.printmanager.persist.dao.DaoFactory;
import com.huayin.printmanager.service.ServiceFactory;

/**
 * <pre>
 * 框架 - 基础业务接口
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月11日
 */
public class BaseServiceImpl
{
	public Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	public DaoFactory daoFactory;

	@Autowired
	public ServiceFactory serviceFactory;
}