/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月17日 上午11:20:15
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.offer;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.offer.OfferBflute;
import com.huayin.printmanager.persist.entity.offer.OfferFormula;
import com.huayin.printmanager.persist.entity.offer.OfferMachine;
import com.huayin.printmanager.persist.entity.offer.OfferPaper;
import com.huayin.printmanager.persist.entity.offer.OfferPrePrint;
import com.huayin.printmanager.persist.entity.offer.OfferProcedure;
import com.huayin.printmanager.persist.entity.offer.OfferProfit;
import com.huayin.printmanager.persist.entity.offer.OfferStartPrint;
import com.huayin.printmanager.persist.entity.offer.OfferWaste;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 报价模块 - 报价设置
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月17日
 */
public interface OfferSettingService
{

	// ==================== 报价设置 - 损耗设置 ====================

	/**
	 * 
	 * <pre>
	 * 查询超级账号的公司损耗设置
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月16日 下午2:48:49, think
	 */
	public List<OfferWaste> findAdminWaste();

	/**
	 * 
	 * <pre>
	 * 查询公司损耗设置（锁定）
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月16日 下午5:50:27, think
	 */
	public List<OfferWaste> getlockWaste(String companyId);

	/**
	 * 
	 * <pre>
	 * 查询公司损耗设置
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月16日 下午2:47:05, think
	 */
	public List<OfferWaste> getWaste(String companyId);

	/**
	 * 
	 * <pre>
	 * 查询当前公司损耗设置
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月16日 下午4:20:19, think
	 */
	public List<OfferWaste> getWaste();

	/**
	 * 
	 * <pre>
	 * 保存或更新损耗设置
	 * </pre>
	 * @param list
	 * @return
	 * @since 1.0, 2017年10月16日 下午2:13:32, think
	 */
	public List<OfferWaste> saveWaste(List<OfferWaste> list);

	// ==================== 报价设置 - 机台 ====================

	/**
	 * 
	 * <pre>
	 * 获取机台 - 用于更新
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月23日 下午4:07:16, think
	 */
	public OfferMachine getLockMachine(Long id);

	/**
	 * 
	 * <pre>
	 * 获取机台
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月23日 上午11:28:25, think
	 */
	public OfferMachine getMachine(Long id);

	/**
	 * 
	 * <pre>
	 * 获取机台 - 开机费+印工计价
	 * </pre>
	 * @param master
	 * @return
	 * @since 1.0, 2017年10月23日 下午2:08:20, think
	 */
	public OfferStartPrint getMachineStartPrint(Long master);

	/**
	 * 
	 * <pre>
	 * 获取机台 - 自定义报价公式
	 * </pre>
	 * @param master
	 * @return
	 * @since 1.0, 2017年10月23日 下午2:09:01, think
	 */
	public OfferFormula getMachineFormula(Long master);

	/**
	 * 
	 * <pre>
	 * 获取机台 - 开机费+印工计价 - 用于更新
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月23日 下午4:07:57, think
	 */
	public OfferStartPrint getLockMachineStartPrint(Long id);

	/**
	 * 
	 * <pre>
	 * 获取机台 - 自定义报价公式 - 用于更新
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月23日 下午4:08:28, think
	 */
	public OfferFormula getLockMachineFormula(Long id);

	/**
	 * <pre>
	 * 获取所有机台的 开机费+印工计价
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月6日 下午1:49:51, think
	 */
	public List<OfferStartPrint> findAllMachineStartPrint();

	/**
	 * <pre>
	 * 获取所有机台的 自定义报价公式
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月6日 下午1:59:47, think
	 */
	public List<OfferFormula> findAllMachineFormula();

	/**
	 * 
	 * <pre>
	 * 机台列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月20日 下午5:22:09, think
	 */
	public SearchResult<OfferMachine> findMachineByCondition(QueryParam queryParam);

	/**
	 * 
	 * <pre>
	 * 获取机台
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月23日 上午11:28:25, think
	 */
	public OfferMachine findMachine(Long id);

	/**
	 * <pre>
	 * 获取所有机台
	 * </pre>
	 * @param offerType
	 * @return
	 * @since 1.0, 2017年11月6日 下午2:08:46, think
	 */
	public List<OfferMachine> findAllMachine(OfferType offerType);

	/**
	 * <pre>
	 * 根据机台名称查询机台
	 * </pre>
	 * @param offerType
	 * @param machineName
	 * @return
	 * @since 1.0, 2017年11月21日 下午2:38:46, think
	 */
	public OfferMachine findMachineByName(OfferType offerType, String machineName);

	/**
	 * 
	 * <pre>
	 * 创建机台
	 * </pre>
	 * @param offerMachine
	 * @return
	 * @since 1.0, 2017年10月20日 下午3:48:49, think
	 */
	public OfferMachine saveMachine(OfferMachine offerMachine);

	/**
	 * 
	 * <pre>
	 * 更新机台
	 * </pre>
	 * @param offerMachine
	 * @return
	 * @since 1.0, 2017年10月23日 下午3:06:29, think
	 */
	public OfferMachine updateMachine(OfferMachine offerMachine);

	/**
	 * 
	 * <pre>
	 * 删除机台
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月23日 下午4:53:17, think
	 */
	public void deleteMachine(Long id);

	// ==================== 报价设置 - 印前费用 ====================

	/**
	 * 
	 * <pre>
	 * 获取印前费用锁定对象
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月19日 下午2:20:27, THINK
	 */
	public OfferPrePrint getLockPrePrint(String companyId);

	/**
	 * 
	 * <pre>
	 * 获取印前费用锁定对象
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月19日 下午1:57:45, THINK
	 */
	public OfferPrePrint getLockPrePrint();

	/**
	 * 
	 * <pre>
	 * 获取
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月19日 下午1:58:47, THINK
	 */
	public OfferPrePrint getPrePrint(OfferType offerType);

	/**
	 * 
	 * <pre>
	 * 保存或更新当前公司印前费用
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月19日 上午11:29:41, THINK
	 */
	public OfferPrePrint savePrePrint(OfferPrePrint offerPrePrint);

	// ==================== 报价设置 - 利润设置 ====================

	/**
	 * 
	 * <pre>
	 * 查询当前公司利润设置
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月26日 下午2:24:40, think
	 */
	public List<OfferProfit> getProfit(OfferType type);

	/**
	 * 
	 * <pre>
	 * 查询公司利润设置
	 * </pre>
	 * @param type
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月26日 下午2:26:08, think
	 */
	public List<OfferProfit> getProfit(OfferType type, String companyId);

	/**
	 * 
	 * <pre>
	 * 保存公司利润设置
	 * </pre>
	 * @param list
	 * @return
	 * @since 1.0, 2017年10月26日 下午2:39:49, think
	 */
	public List<OfferProfit> saveProfit(List<OfferProfit> list);

	// ==================== 报价设置 - 纸张设置 ====================

	/**
	 * 
	 * <pre>
	 * 获取纸张 - 纸张材料列表
	 * </pre>
	 * @param offerType
	 * @return
	 * @since 1.0, 2017年10月30日 下午4:07:57, THINK
	 */
	public SearchResult<OfferPaper> getPaperList(QueryParam queryParam);

	/**
	 * 
	 * <pre>
	 * 获取纸张 - 自动报价纸张选择下拉菜单
	 * </pre>
	 * @param type
	 * @param name
	 * @return
	 * @since 1.0, 2017年11月8日 下午5:43:17, zhengby
	 */
	public List<OfferPaper> getPaperList(OfferType offerType, String name);

	/**
	 * 
	 * <pre>
	 * 获取纸张
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月31日 下午4:02:22, THINK
	 */
	public OfferPaper getPaper(Long id);

	/**
	 * 
	 * <pre>
	 * 获取纸张 - 用于更新
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月30日 下午2:51:52, THINK
	 */
	public OfferPaper getLockPaper(Long id);

	/**
	 * 
	 * <pre>
	 * 手动保存纸张材料
	 * </pre>
	 * @param offerPaperMaterial
	 * @return
	 * @since 1.0, 2017年10月30日 下午2:37:56, THINK
	 */
	public OfferPaper savePaper(OfferPaper offerPaper);

	/**
	 * 
	 * <pre>
	 * 批量保存纸张材料
	 * </pre>
	 * @param offerPaperMaterial
	 * @since 1.0, 2017年10月30日 下午6:22:06, THINK
	 */
	public void savePaperByBatch(List<OfferPaper> offerPaper);

	/**
	 * 
	 * <pre>
	 * 保存材料列表的选择
	 * </pre>
	 * @param materialList
	 * @since 1.0, 2017年10月31日 下午3:21:12, THINK
	 */
	public void savePaperByMaterial(List<OfferPaper> paperList);

	/**
	 * 
	 * <pre>
	 * 更新纸张设置
	 * </pre>
	 * @param offerPaperMaterial
	 * @return
	 * @since 1.0, 2017年10月31日 下午4:14:48, THINK
	 */
	public OfferPaper updatePaper(OfferPaper offerPaper);

	/**
	 * 
	 * <pre>
	 * 删除纸张设置
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月31日 下午5:25:04, THINK
	 */
	public void deletePaper(Long id);

	/**
	 * 
	 * <pre>
	 * 批量删除删除纸张设置
	 * </pre>
	 * @param ids
	 * @since 1.0, 2017年11月2日 上午10:45:48, zhengby
	 */
	public void deletePaperByBatch(Long[] ids);

	// ==================== 报价设置 - 坑纸设置 ====================

	/**
	 * 
	 * <pre>
	 * 坑纸设置 - 获取坑纸设置列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:16:49, THINK
	 */
	public SearchResult<OfferBflute> getBfluteList(QueryParam queryParam);

	/**
	 * 
	 * <pre>
	 * 坑纸设置 - 获取坑纸列表（自动报价下拉框）
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年11月9日 上午11:21:07, zhengby
	 */
	public List<OfferBflute> getBfluteList(String name);

	/**
	 * 
	 * <pre>
	 * 坑纸设置 - 获取单条坑纸设置数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:11:27, THINK
	 */
	public OfferBflute getBflute(Long id);

	/**
	 * 
	 * <pre>
	 * 坑纸设置 - 获取并锁定单条坑纸设置数据（用于更新）
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:13:23, THINK
	 */
	public OfferBflute getLockBflute(Long id);

	/**
	 * 
	 * <pre>
	 * 坑纸设置 - 保存坑纸设置
	 * </pre>
	 * @param offerBflute
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:09:23, THINK
	 */
	public OfferBflute saveBflute(OfferBflute offerBflute);

	/**
	 * 
	 * <pre>
	 * 坑纸设置 - 批量保存坑纸设置
	 * </pre>
	 * @param offerBflute
	 * @since 1.0, 2017年11月1日 上午11:10:13, THINK
	 */
	public void saveBfluteByBatch(List<OfferBflute> offerBflute);

	/**
	 * 
	 * <pre>
	 * 坑纸设置 - 保存从材料选择列表选择的坑纸设置数据
	 * </pre>
	 * @param bfluteList
	 * @since 1.0, 2017年11月1日 上午11:10:38, THINK
	 */
	public void saveBfluteByMaterial(List<OfferBflute> bfluteList);

	/**
	 * 
	 * <pre>
	 * 坑纸设置 - 更新坑纸设置
	 * </pre>
	 * @param offerBflute
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:14:17, THINK
	 */
	public OfferBflute updateBflute(OfferBflute offerBflute);

	/**
	 * 
	 * <pre>
	 * 坑纸设置 - 删除单条坑纸设置数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:15:47, THINK
	 */
	public void deleteBflute(Long id);

	/**
	 * 
	 * <pre>
	 * 坑纸设置 - 删除批量坑纸设置数据
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:16:21, THINK
	 */
	public void deleteBfluteByBatch(Long[] ids);

	// ==================== 报价设置 - 工序设置 ====================

	/**
	 * 
	 * <pre>
	 * 查询当前公司的工序设置
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月30日 上午11:13:29, think
	 */
	public List<OfferProcedure> getProcedure(OfferType type);

	/**
	 * <pre>
	 * 查询公司的工序设置
	 * </pre>
	 * @param type
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月30日 上午11:13:57, think
	 */
	public List<OfferProcedure> getProcedure(OfferType type, String companyId);

	/**
	 * <pre>
	 * 根据id查询公司的工序设置
	 * </pre>
	 * @param type
	 * @param id
	 * @return
	 * @since 1.0, 2017年11月24日 上午9:09:29, zhengby
	 */
	public OfferProcedure getProcedure(OfferType type, Long id);

	/**
	 * <pre>
	 * 根据name查询公司的工序设置
	 * </pre>
	 * @param type
	 * @param name
	 * @return
	 * @since 1.0, 2018年2月28日 下午6:12:48, think
	 */
	public OfferProcedure getProcedureByName(OfferType type, String name);

	/**
	 * <pre>
	 * 工序列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月30日 上午10:25:18, think
	 */
	public SearchResult<OfferProcedure> findProcedureByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询去除重复得工序列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月7日 下午5:13:05, think
	 */
	public SearchResult<OfferProcedure> findProcedureDuplicate(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存工序
	 * </pre>
	 * @param list
	 * @return
	 * @since 1.0, 2017年10月30日 上午11:08:52, think
	 */
	public List<OfferProcedure> saveProcedure(List<OfferProcedure> list);

}
