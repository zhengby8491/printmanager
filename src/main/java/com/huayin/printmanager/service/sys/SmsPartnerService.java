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

import java.util.Date;
import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.persist.enumerate.SmsPartnerState;

/**
 * <pre>
 * 系统模块 - 短信渠道
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface SmsPartnerService
{
	/**
	 * <pre>
	 * 根据商户编号查询短信渠道
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:38:40, think
	 */
	public SmsPartner get(Long id);
	
	/**
	 * <pre>
	 * 根据短信渠道名查询短信渠道
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:38:47, think
	 */
	public SmsPartner getByName(String name);

	/**
	 * <pre>
	 * 多条件查询短信渠道
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param smsPartnerName
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:38:55, think
	 */
	public SearchResult<SmsPartner> findByCondition(Date dateMin, Date dateMax, String smsPartnerName, Integer pageIndex, Integer pageSize);

	/**
	 * <pre>
	 * 根据状态获取短信渠道
	 * </pre>
	 * @param state
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:38:34, think
	 */
	public List<SmsPartner> list(SmsPartnerState state);
	
	/**
	 * <pre>
	 * 保存短信渠道信息
	 * </pre>
	 * @param smsPartner
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:39:02, think
	 */
	public SmsPartner save(SmsPartner smsPartner);

	/**
	 * <pre>
	 * 修改短息渠道信息
	 * </pre>
	 * @param smsPartner
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:39:08, think
	 */
	public SmsPartner update(SmsPartner smsPartner);

	/**
	 * <pre>
	 * 删除短信渠道
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月25日 下午5:39:13, think
	 */
	public void delete(Long id);

}
