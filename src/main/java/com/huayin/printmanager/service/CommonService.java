/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月11日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service;

import java.util.List;
import java.util.Map;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BeginBillType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InitStep;
import com.huayin.printmanager.persist.enumerate.OfferSettingType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.UpdateNameType;

/**
 * <pre>
 * 框架 - 缓存常用功能
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月11日
 */
public interface CommonService
{
	/**
	 * <pre>
	 * 清除所有数据
	 * </pre>
	 * @return
	 */
	public boolean clearAllData(String companyId);

	/**
	 * <pre>
	 * 清除所有单据数据资料
	 * </pre>
	 * @return
	 */
	public boolean clearAllBillData(String companyId);
	
	/**
	 * <pre>
	 * 重新设置公司初始化状态
	 * </pre>
	 * @param initType
	 * @return
	 * @since 1.0, 2018年9月22日 下午4:09:33, zhengby
	 */
	public boolean resetCompanyState(InitStep initType,String companyId);

	/**
	 * <pre>
	 * 基础资料删除
	 * </pre>
	 * @param basicType 基础资料类型
	 * @param id 主键ID
	 * @return
	 */
	public boolean delete(BasicType basicType, Long id);

	/**
	 * <pre>
	 * 单据审核
	 * </pre>
	 * @param billType 单据类型
	 * @param id 单据ID
	 * @param flag 审核标记
	 * @return
	 */
	public boolean audit(BillType billType, Long id, BoolValue flag);
	
	/**
	 * <pre>
	 * 判断基础数据是否被下游引用
	 * </pre>
	 * @param basicType
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 下午3:49:37, zhengby
	 */
	public boolean isUsed(BasicType basicType, Long id);
	
	/**
	 * <pre>
	 * 判断单据是否被下游引用
	 * </pre>
	 * @param billType
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 下午3:49:26, zhengby
	 */
	public boolean isUsed(BillType billType, Long id);

	/**
	 * <pre>
	 * 判断期初单据是否被下游引用
	 * </pre>
	 * @param basicType
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 下午3:49:20, zhengby
	 */
	public boolean isUsed(BeginBillType basicType, List<Long> id);
	/**
	 * 
	 * <pre>
	 * 查询下级单据引用的订单号
	 * </pre>
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, List> findRefBillNo(BillType billType, Long id);

	/**
	 * <pre>
	 * 强制完工:批量修改明细标识
	 * </pre>
	 * @param cla 明细表类型
	 * @param ids 明细表ID数组
	 * @param flag 强制完工标识
	 * @return
	 */
	public boolean forceDetailComplete(Class<? extends BaseBillDetailTableEntity> cla, Long[] ids, BoolValue flag);

	/**
	 * <pre>
	 * 强制完工: 修改主表标识
	 * </pre>
	 * @param cla 明细表类型
	 * @param id 主表ID数组
	 * @param flag 强制完工标识
	 * @return
	 */
	public boolean forceMasterComplete(Class<? extends BaseBillMasterTableEntity> cla, Long[] id, BoolValue flag);

	/**
	 * <pre>
	 * 强制完工
	 * </pre>
	 * @param companyId
	 * @param cla
	 * @param ids
	 * @param flag
	 * @return
	 * @since 1.0, 2018年3月20日 上午9:46:39, think
	 */
	public boolean forceComplete(String companyId, Class<? extends BaseBillTableEntity> cla, Long[] ids, BoolValue flag);

	/**
	 * <pre>
	 * 强制完工
	 * </pre>
	 * @param cla
	 * @param id
	 * @param flag
	 * @return
	 * @since 1.0, 2018年3月20日 上午9:46:46, think
	 */
	public boolean forceComplete(Class<? extends BaseBillTableEntity> cla, Long[] id, BoolValue flag);

	/**
	 * <pre>
	 * 业务流程拆分 强制完工用 这个
	 * </pre>
	 * @param cla
	 * @param ids
	 * @param flag
	 * @return
	 * @since 1.0, 2018年3月20日 上午9:47:07, think
	 */
	public boolean forceComplete2(Class<? extends BaseBillTableEntity> cla, List<Long> ids, BoolValue flag);

	/**
	 * <pre>
	 * 得到基础资料信息列表
	 * </pre>
	 * @param type 基础资料表类型
	 * @return
	 */
	public List<BaseBasicTableEntity> getBasicInfoList(BasicType type);

	/**
	 * 
	 * <pre>
	 * 获取公共基础资料信息列表
	 * </pre>
	 * @param type
	 * @return
	 */
	public List<BaseBasicTableEntity> getCommBasicInfoList(BasicType type);

	/**
	 * 
	 * <pre>
	 * 报价系统 - 获取公共基础缓存数据
	 * </pre>
	 * @param type
	 * @param settingType
	 * @return
	 * @since 1.0, 2017年11月3日 下午3:11:52, think
	 */
	public List<BaseBasicTableEntity> getCommBasicOfferList(OfferType type, OfferSettingType settingType);

	/**
	 * <pre>
	 * 计算下一个排序值
	 * </pre>
	 * @param type 基础资料表类型
	 * @return
	 */
	public int getNextSort(BasicType type);

	/**
	 * <pre>
	 * 计算下一个代码值
	 * </pre>
	 * @param type 基础资料表类型
	 * @return
	 */
	public String getNextCode(BasicType type);

	/**
	 * <pre>
	 * 更新基础资料的name字段
	 * </pre>
	 * @param id
	 * @param newName
	 * @param oldName
	 * @param type
	 * @since 1.0, 2018年2月6日 下午4:30:10, zhengby
	 */
	public void updateBasicName(Long id, String newName, UpdateNameType type);

	/**
	 * <pre>
	 * 查询是否存在相同的名称
	 * </pre>
	 * @param name
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月6日 下午4:58:12, zhengby
	 */
	public Boolean isExist(String name, UpdateNameType type);

}
