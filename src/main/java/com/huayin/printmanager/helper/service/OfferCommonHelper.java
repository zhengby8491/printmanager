/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年11月9日 上午8:57:07
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.helper.service;

import java.util.List;

import com.google.common.collect.Lists;
import com.huayin.printmanager.persist.entity.offer.OfferPart;
import com.huayin.printmanager.persist.entity.offer.OfferPartProcedure;
import com.huayin.printmanager.persist.enumerate.OfferPrintStyleType;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 报价系统 - 常用功能
 * </pre>
 * @author       think
 * @since        1.0, 2017年11月9日
 */
public class OfferCommonHelper
{

	/**
	 * <pre>
	 * 去除重复
	 * 例如：80克单铜纸, 80双铜纸
	 * </pre>
	 * @param list
	 * @param symbol
	 * @return
	 * @since 1.0, 2017年11月9日 上午9:12:37, think
	 */
	public static String removeDuplicate(List<String> list, String symbol)
	{
		if (list.isEmpty())
		{
			return "";
		}

		// 默认使用,间隔
		if (StringUtils.isEmpty(symbol))
		{
			symbol = ",";
		}

		// 去除重复
		List<String> listNew = Lists.newArrayList();
		for (String paperName : list)
		{
			if (!listNew.contains(paperName))
			{
				listNew.add(paperName);
			}
		}
		
		// 例如：80克单铜纸, 80双铜纸
		StringBuilder duplicatePaperName = new StringBuilder();
		for (String paperName : listNew)
		{
			duplicatePaperName.append(paperName).append(symbol);
		}
		
		// 去除最后一个结尾的符号
		duplicatePaperName = duplicatePaperName.replace(duplicatePaperName.length() - 1, duplicatePaperName.length(), "");
		return duplicatePaperName.toString();
	}

	/**
	 * <pre>
	 * 去除重复
	 * 例如：80克单铜纸, 80双铜纸
	 * </pre>
	 * @param set
	 * @return
	 * @since 1.0, 2017年11月9日 上午9:17:03, think
	 */
	public static String removeDuplicate(List<String> list)
	{
		return removeDuplicate(list, null);
	}

	/**
	 * <pre>
	 * 去除重复 - 印刷纸张
	 * 例如：80克单铜纸, 80双铜纸
	 * </pre>
	 * @param offerPartList
	 * @param symbol
	 * @return
	 * @since 1.0, 2017年11月9日 上午9:07:19, think
	 */
	public static String removeDuplicatePrintName(List<OfferPart> offerPartList, String symbol)
	{
		if (null == offerPartList)
			throw new NullPointerException();

		List<String> list = Lists.newArrayList();

		for (OfferPart offerPart : offerPartList)
		{
			// 纸张 + 克重
			if (null != offerPart.getBflutePit())
			{
				list.add(offerPart.getPaperName() + offerPart.getPaperWeight() + offerPart.getBflutePit() + offerPart.getBflutePaperQuality());
			}
			else
			{
				list.add(offerPart.getPaperName() + offerPart.getPaperWeight());
			}

		}

		return removeDuplicate(list, symbol);
	}

	/**
	 * <pre>
	 * 去除重复 - 印刷纸张
	 * 例如：80克单铜纸, 80双铜纸
	 * </pre>
	 * @param offerPartList
	 * @return
	 * @since 1.0, 2017年11月9日 上午9:39:55, think
	 */
	public static String removeDuplicatePrintName(List<OfferPart> offerPartList)
	{
		return removeDuplicatePrintName(offerPartList, null);
	}

	/**
	 * <pre>
	 * 去除重复 - 印刷颜色
	 * 例如：四色+四专/四色+四专,单色+无专
	 * </pre>
	 * @param offerPartList
	 *  @param symbol
	 * @return
	 * @since 1.0, 2017年11月9日 上午9:15:22, think
	 */
	public static String removeDuplicatePrintColor(List<OfferPart> offerPartList, String symbol)
	{
		if (null == offerPartList)
			throw new NullPointerException();

		List<String> list = Lists.newArrayList();

		for (OfferPart offerPart : offerPartList)
		{
			// 单色印刷
			String color1 = offerPart.getOfferPrintColorType().getText() + "+" + offerPart.getOfferSpotColorType().getText();
			if (OfferPrintStyleType.SINGLE == offerPart.getOfferPrintStyleType())
			{
				list.add(color1);
			}
			// 双面印刷
			else
			{
				String color2 = offerPart.getOfferPrintColorType2().getText() + "+" + offerPart.getOfferSpotColorType2().getText();
				list.add(color1 + "/" + color2);
			}
		}

		return removeDuplicate(list, symbol);
	}

	/**
	 * <pre>
	 * 去除重复 - 印刷颜色
	 * 例如：四色+四专/四色+四专,单色+无专
	 * </pre>
	 * @param offerPartList
	 * @return
	 * @since 1.0, 2017年11月9日 上午9:40:51, think
	 */
	public static String removeDuplicatePrintColor(List<OfferPart> offerPartList)
	{
		return removeDuplicatePrintColor(offerPartList, null);
	}

	/**
	 * <pre>
	 * 去除重复 - 加工工序
	 * </pre>
	 * @param offerPartList
	 * @param symbol
	 * @return
	 * @since 1.0, 2017年11月13日 下午4:19:58, think
	 */
	public static String removeDuplicatePrintProcedure(List<OfferPart> offerPartList,List<OfferPartProcedure> partProcedureList, String symbol)
	{
		if (null == offerPartList)
			throw new NullPointerException();

		List<String> list = Lists.newArrayList();

		for (OfferPart offerPart : offerPartList)
		{
			if (null != offerPart.getOfferPartProcedureList())
			{
				for (OfferPartProcedure offerPartProcedure : offerPart.getOfferPartProcedureList())
				{
					list.add(offerPartProcedure.getProcedureName());
				}
			}
		}
		// 加上成品工序
		if(null != partProcedureList)
		{
			for(OfferPartProcedure partProcedure : partProcedureList)
			{
				list.add(partProcedure.getProcedureName());
			}
		}
		return removeDuplicate(list, symbol);
	}

	/**
	 * <pre>
	 * 去除重复 - 加工工序
	 * </pre>
	 * @param offerPartList
	 * @return
	 * @since 1.0, 2017年11月13日 下午4:20:27, think
	 */
	public static String removeDuplicatePrintProcedure(List<OfferPart> offerPartList,List<OfferPartProcedure> partProcedureList )
	{
		return removeDuplicatePrintProcedure(offerPartList, partProcedureList, null);
	}
}
