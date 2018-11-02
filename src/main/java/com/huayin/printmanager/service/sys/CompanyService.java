/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月16日 上午10:04:45
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.ExchangeRate;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.service.sys.vo.CompanyVo;
import com.huayin.printmanager.service.sys.vo.RegisterInitVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统模块 - 公司管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月16日, 整理规范
 */
public interface CompanyService
{
	/**
	 * 
	 * <pre>
	 * 根据id查询公司
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:58:26, think
	 */
	public Company get(String id);

	/**
	 * 
	 * <pre>
	 * 根据id查询公司（锁定表）
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:58:39, think
	 */
	public Company lock(String id);

	/**
	 * <pre>
	 * 根据本位币币别、订单币别获取税率对象
	 * </pre>
	 * @param currencyType
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:59:26, think
	 */
	public ExchangeRate getExchangeRate(CurrencyType currencyType);

	/**
	 * <pre>
	 * 后台多条件查询公司
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:59:33, think
	 */
	public SearchResult<CompanyVo> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 新增公司
	 * </pre>
	 * @param company
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:59:41, think
	 */
	public Company save(Company company);

	/**
	 * <pre>
	 * 初始化基础资料
	 * </pre>
	 * @param initVo
	 * @since 1.0, 2017年10月25日 下午4:59:59, think
	 */
	public void initBasic(RegisterInitVo initVo);

	/**
	 * <pre>
	 * 独立初始化单位换算
	 * </pre>
	 * @since 1.0, 2017年10月25日 下午5:00:04, think
	 */
	public void initUnitConvert();

	/**
	 * 
	 * <pre>
	 * 初始化公司的损耗设置
	 * </pre>
	 * @since 1.0, 2017年10月16日 下午3:04:23, think
	 */
	public void initWasteSetting();

	/**
	 * <pre>
	 * 校验公司名是否存在
	 * </pre>
	 * @param name
	 * @param unCompanyId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:00:13, think
	 */
	public boolean existCompanyName(String name, String unCompanyId);

	/**
	 * <pre>
	 * 到期通知
	 * </pre>
	 * @since 1.0, 2017年10月25日 下午5:00:19, think
	 */
	public void expireNotify();

	/**
	 * <pre>
	 * 体验到期通知
	 * </pre>
	 * @since 1.0, 2017年10月25日 下午5:00:26, think
	 */
	public void experienceNotify();

	/**
	 * <pre>
	 * 查询到期用户
	 * </pre>
	 * @param isFormal 是否正式用户,expireDay 到期天数
	 * @param expireDay
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:00:33, think
	 */
	public List<Company> expireQty(BoolValue isFormal, Integer expireDay);
}
