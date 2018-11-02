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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.SmsLog;
import com.huayin.printmanager.persist.enumerate.SmsLogState;
import com.huayin.printmanager.persist.enumerate.SmsSendType;
import com.huayin.printmanager.persist.enumerate.SmsType;

/**
 * <pre>
 * 系统模块 - 短信日志
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface SmsLogService
{
	/**
	 * <pre>
	 * 根据userNo获取短信日志
	 * </pre>
	 * @param userNo
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:33:03, think
	 */
	public SmsLog getSmsLogByUserNo(String userNo);

	/**
	 * <pre>
	 * 工具公司id获取短信日志
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:33:21, think
	 */
	public SmsLog getSmsLogByCompanyId(String companyId);

	/**
	 * <pre>
	 * 根据手机号码获取短信日志
	 * </pre>
	 * @param mobile
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:33:32, think
	 */
	public SmsLog getByMobile(String mobile);

	/**
	 * <pre>
	 * 根据短信内容获取短信日志
	 * </pre>
	 * @param content
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:33:53, think
	 */
	public SmsLog getByContent(String content);

	/**
	 * <pre>
	 * 多条件查询短信记录
	 * </pre>
	 * @param mobile
	 * @param content
	 * @param smsSendType
	 * @param type
	 * @param state
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:34:33, think
	 */
	public SearchResult<SmsLog> findByCondition(String mobile, String content, SmsSendType smsSendType, SmsType type, SmsLogState state, Integer pageIndex, Integer pageSize);
}
