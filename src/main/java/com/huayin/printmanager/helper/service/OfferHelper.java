/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年11月3日 下午3:27:38
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.helper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.huayin.printmanager.helper.AbsHelper;
import com.huayin.printmanager.persist.entity.offer.OfferBflute;
import com.huayin.printmanager.persist.entity.offer.OfferPaper;
import com.huayin.printmanager.persist.entity.offer.OfferWaste;
import com.huayin.printmanager.persist.enumerate.OfferSettingType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.utils.CacheUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 报价系统 - 基础设置缓存
 * </pre>
 * @author       think
 * @since        1.0, 2017年11月3日
 */
public class OfferHelper extends AbsHelper
{
	/**
	 * CACHE_COMPANY缓存的key -> value
	 */
	public static final String CACHE_COMPANY_WASTESETTING = "company_waste_setting";

	/**
	 * 
	 * <pre>
	 * 生成缓存Key
	 * </pre>
	 * @param type
	 * @param settingType
	 * @return
	 * @since 1.0, 2017年11月3日 下午3:53:22, think
	 */
	private static String _generateKey(OfferType type, OfferSettingType settingType)
	{
		return type.name() + "_" + settingType.name();
	}
	
	/**
	 * 
	 * <pre>
	 * 生产缓存Key（重写）--自动报价
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2017年11月9日 上午10:36:56, zhengby
	 */
	@SuppressWarnings("unused")
	private static String _generateKey(OfferType type)
	{
		return type.name() + "_OFFERAUTO";
	}

	// ==================== 报价缓存 - 基础设置 ====================

	/**
	 * <pre>
	 * 获取基础设置缓存
	 * </pre>
	 * @param type
	 * @param settingType
	 * @return
	 * @since 1.0, 2017年11月3日 下午3:29:46, think
	 */
	@SuppressWarnings("unchecked")
	public static List<?> getBasicList(OfferType type, OfferSettingType settingType)
	{
		List<Object> list = null;
		if (type != null)
		{
			String cacheName = _generateKey(type, settingType);
			list = (Vector<Object>) (UserUtils.getCompanyCache(UserUtils.getCompanyId(), cacheName));
			if (list == null)
			{
				list = new Vector<Object>();
				list.addAll(serviceFactory.getCommonService().getCommBasicOfferList(type, settingType));
				UserUtils.putCompanyCacheMap(UserUtils.getCompanyId(), cacheName, list);
			}
		}
		return list;
	}

	/**
	 * <pre>
	 * 清除基础设置缓存
	 * </pre>
	 * @param type
	 * @param settingType
	 * @since 1.0, 2017年11月3日 下午3:55:21, think
	 */
	public static void clearCacheBasic(OfferType type, OfferSettingType settingType)
	{
		String cacheName = _generateKey(type, settingType);
		UserUtils.clearCompanyCache(UserUtils.getCompanyId(), cacheName);
	}

	// ==================== 报价缓存 - 损耗设置 ====================

	/**
	 * 
	 * <pre>
	 * 获取报价设置的枚举集合
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月17日 下午5:17:03, think
	 */
	public static List<OfferSettingType> findOfferSettingTypeList()
	{
		List<OfferSettingType> offerSettingTypeList = Lists.newArrayList();
		for (OfferSettingType offerSettingType : OfferSettingType.values())
		{
			offerSettingTypeList.add(offerSettingType);
		}
		return offerSettingTypeList;
	}

	// ==================== 报价缓存 - 自动报价  ====================

	/**
	 * 
	 * <pre>
	 * 获取纸张设置的纸张材料（自动报价下拉框选项）
	 * TODO 需要重载一个专门用于页面下拉的
	 * </pre>
	 * @param type
	 * @param name
	 * @return
	 * @since 1.0, 2017年11月9日 上午11:34:40, zhengby
	 */
	public static List<OfferPaper> getOfferPaperList(OfferType type,String name)
	{
//		List<Object> list = null;
//		if (type != null)
//		{
//			String cacheName = _generateKey(type);
//			list = (Vector<Object>) (UserUtils.getCompanyCache(UserUtils.getCompanyId(), cacheName));
//			if (list == null)
//			{
//				list = new Vector<Object>();
//				list.addAll(serviceFactory.getOfferSettingService().getPaperList(type, name));
//				UserUtils.putCompanyCacheMap(UserUtils.getCompanyId(), cacheName, list);
//			}
//		}
		
		Set<String> paperNameSet = Sets.newHashSet();
		List<OfferPaper> paperList = serviceFactory.getOfferSettingService().getPaperList(type,name);
		
		for(OfferPaper offerPaper : paperList)
		{
			paperNameSet.add(offerPaper.getName());
		}
		
		// 去除重复名称
		List<OfferPaper> paperListNew = Lists.newArrayList();
		for (String paperName : paperNameSet)
		{
			OfferPaper paper = new OfferPaper();
			paper.setName(paperName);
			paperListNew.add(paper);
		}
		
		return paperListNew;
	}
	
	/**
	 * 
	 * <pre>
	 * 获取坑纸设置的坑形（彩盒自动报价下拉框选项）
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年11月9日 上午11:34:34, zhengby
	 */
	public static List<OfferBflute> getOfferBfluteList(String name)
	{
		Set<String> paperNameSet = Sets.newHashSet();
		List<OfferBflute> paperList = serviceFactory.getOfferSettingService().getBfluteList(name);
		
		for(OfferBflute offerBflute : paperList)
		{
			paperNameSet.add(offerBflute.getPit());
		}
		
		// 去除重复名称
		List<OfferBflute> bfluteListNew = Lists.newArrayList();
		for (String paperName : paperNameSet)
		{
			OfferBflute bflute = new OfferBflute();
			bflute.setPit(paperName);
			bfluteListNew.add(bflute);
		}
		return bfluteListNew;
	}
	/**
	 * 
	 * <pre>
	 * 查询超级账号缓存的损耗设置
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月17日 上午10:26:49, think
	 */
	@SuppressWarnings("unchecked")
	public static List<OfferWaste> findAdminWasteSetting()
	{
		/**
		 * 1. 查询超级账号缓存的损耗设置
		 * 2. 为null则查询数据库并设置到缓存中
		 */

		// 1. 查询超级账号缓存的损耗设置
		List<OfferWaste> wasteSettingList = (ArrayList<OfferWaste>) CacheUtils.get(UserUtils.CACHE_SUPER_WASTESETTING);

		// 2. 为null则查询数据库并设置到缓存中
		if (null == wasteSettingList)
		{
			wasteSettingList = Lists.newArrayList();
			wasteSettingList.addAll(serviceFactory.getOfferSettingService().findAdminWaste());

			CacheUtils.put(UserUtils.CACHE_SUPER_WASTESETTING, wasteSettingList);
		}

		return wasteSettingList;
	}

	/**
	 * 
	 * <pre>
	 * 清空超级账号缓存的损耗设置
	 * </pre>
	 * @since 1.0, 2017年10月18日 下午7:15:46, think
	 */
	public static void clearAdminWasteSettingCache()
	{
		CacheUtils.remove(UserUtils.CACHE_SUPER_WASTESETTING);
	}

	/**
	 * 
	 * <pre>
	 * 查询公司账号缓存的损耗设置
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月18日 下午7:04:32, think
	 */
	@SuppressWarnings("unchecked")
	public static List<OfferWaste> findWasteSetting(String companyId)
	{
		/**
		 * 1. 查询公司账号缓存的损耗设置
		 * 2. 为null则查询数据库并设置到缓存中
		 */

		// 1. 查询公司账号缓存的损耗设置
		List<OfferWaste> wasteSettingList = (ArrayList<OfferWaste>) UserUtils.getCompanyCache(companyId, CACHE_COMPANY_WASTESETTING);

		// 2. 为null则查询数据库并设置到缓存中
		if (null == wasteSettingList)
		{
			wasteSettingList = Lists.newArrayList();
			wasteSettingList.addAll(serviceFactory.getOfferSettingService().getWaste(companyId));
			putWasteSettingCache(companyId, wasteSettingList);
		}

		return wasteSettingList;
	}

	/**
	 * 
	 * <pre>
	 * 查询当前登陆公司账号缓存的损耗设置
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月18日 下午7:05:38, think
	 */
	public static List<OfferWaste> findWasteSetting()
	{
		return findWasteSetting(UserUtils.getCompanyId());
	}

	/**
	 * <pre>
	 * 根据报价类型，查询当前登陆公司账号缓存的损耗设置
	 * </pre>
	 * @param offerType
	 * @return
	 * @since 1.0, 2017年11月7日 下午2:20:28, think
	 */
	public static OfferWaste findWasteSettingByOfferType(OfferType offerType)
	{
		List<OfferWaste> offerWasteList = findWasteSetting();
		for (OfferWaste offerWaste : offerWasteList)
		{
			if (offerType == offerWaste.getOfferType())
			{
				return offerWaste;
			}
		}
		throw new IllegalArgumentException("找不到损耗设置");
	}
	
	/**
	 * <pre>
	 * 更新公司账号缓存的损耗设置
	 * </pre>
	 * @param companyId
	 * @param wasteSettingList
	 * @since 1.0, 2017年12月15日 上午10:17:45, think
	 */
	public static void putWasteSettingCache(String companyId, List<OfferWaste> wasteSettingList)
	{
		if (!wasteSettingList.isEmpty())
		{
			UserUtils.putCompanyCacheMap(companyId, CACHE_COMPANY_WASTESETTING, wasteSettingList);
		}
	}

	/**
	 * 
	 * <pre>
	 * 清空公司账号缓存的损耗设置
	 * </pre>
	 * @param companyId
	 * @since 1.0, 2017年10月18日 下午7:14:28, think
	 */
	public static void clearWasteSettingCache(String companyId)
	{
		UserUtils.clearCompanyCache(companyId, CACHE_COMPANY_WASTESETTING);
	}

	/**
	 * 
	 * <pre>
	 * 清空当前登陆公司账号缓存的损耗设置
	 * </pre>
	 * @since 1.0, 2017年10月18日 下午7:14:28, think
	 */
	public static void clearWasteSettingCache()
	{
		clearWasteSettingCache(UserUtils.getCompanyId());
	}

}
