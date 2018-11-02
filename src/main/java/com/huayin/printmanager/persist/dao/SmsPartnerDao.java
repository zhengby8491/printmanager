package com.huayin.printmanager.persist.dao;

import java.util.List;

import com.huayin.common.persist.NestedSimplyDao;
import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.persist.enumerate.SmsPartnerState;
import com.huayin.printmanager.persist.enumerate.SmsSendType;

/**
 * 短信合作商持久接口
 * @author zhaojitao
 * @version 1.0, 2016-5-5
 */

public interface SmsPartnerDao extends NestedSimplyDao<SmsPartner>
{

	/**
	 * <pre>
	 * 根据终端类型查询短信合作商
	 * </pre>
	 * @param smsSendType 终端类型
	 * @return
	 */
	public List<SmsPartner> findSmsPartnerBySmsSendType(SmsSendType smsSendType);

	/**
	 * 查询短信合作商数量
	 * @param id
	 * @param name
	 * @param status
	 * @return
	 */
	public Long findSmsPartnerLogWithCount(Long id, String name, SmsPartnerState status);

	/**
	 * 查询短信合作商
	 * @param id
	 * @param name
	 * @param status
	 * @param pageIndex
	 * @param pageSize
	 * @param sortMap
	 * @return
	 */
	public List<SmsPartner> findSmsPartnerLogWithPage(Long id, String name, SmsPartnerState status, int pageIndex,
			int pageSize);
}
