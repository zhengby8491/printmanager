/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys;

import java.util.List;

import com.huayin.printmanager.persist.entity.sys.SmsPortal;

/**
 * <pre>
 * 系统模块 - 短信供应商
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface SmsPortalService
{
	/**
	 * <pre>
	 * 根据id获取短信供应商
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:43:03, think
	 */
	public SmsPortal get(Long id);

	/**
	 * <pre>
	 * 根据账户获取短信供应商
	 * </pre>
	 * @param accountId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:43:14, think
	 */
	public SmsPortal getByAccountId(String accountId);

	/**
	 * <pre>
	 * 获取短信供应商列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:43:24, think
	 */
	public List<SmsPortal> list();

	/**
	 * <pre>
	 * 保存短信供应商
	 * </pre>
	 * @param smsPortal
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:43:36, think
	 */
	public SmsPortal save(SmsPortal smsPortal);

	/**
	 * <pre>
	 * 更新短信供应商
	 * </pre>
	 * @param smsPortal
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:43:40, think
	 */
	public SmsPortal update(SmsPortal smsPortal);

	/**
	 * <pre>
	 * 删除短信供应商
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月25日 下午5:43:45, think
	 */
	public void delete(Long id);

}
