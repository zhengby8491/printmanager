/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年11月7日 下午5:11:25
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.helper.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.common.collect.Lists;
import com.huayin.common.util.ObjectHelper;
import com.huayin.printmanager.persist.entity.offer.OfferMachine;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.persist.entity.offer.OfferPart;
import com.huayin.printmanager.persist.entity.offer.OfferPartProcedure;
import com.huayin.printmanager.persist.entity.offer.OfferProcedure;
import com.huayin.printmanager.persist.entity.offer.OfferProfit;
import com.huayin.printmanager.persist.entity.offer.OfferStartPrint;
import com.huayin.printmanager.persist.entity.offer.OfferWaste;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OfferMachineType;
import com.huayin.printmanager.persist.enumerate.OfferPrintColorType;
import com.huayin.printmanager.persist.enumerate.OfferPrintStyleType;
import com.huayin.printmanager.persist.enumerate.OfferProcedureFormulaType;
import com.huayin.printmanager.persist.enumerate.OfferProfitType;
import com.huayin.printmanager.persist.enumerate.OfferSpotColorType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.PaperType;
import com.huayin.printmanager.persist.enumerate.ParamsLadderType;
import com.huayin.printmanager.persist.enumerate.ProcedureUnit;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 报价系统 - 公式计算
 * </pre>
 * @author       think
 * @since        1.0, 2017年11月7日
 */
public class OfferFormulaHelper
{
	/**
	 * <pre>
	 * 报价系统 - 拼版数
	 * </pre>
	 * @author       think
	 * @since        1.0, 2017年11月7日
	 */
	public static final class SheetNumber
	{
		/**
		 * 拼版数 - 长
		 */
		Integer sheetLen;

		/**
		 * 拼版数 - 宽
		 */
		Integer sheetWidth;

		/**
		 * 拼版数
		 */
		Integer sheetNum;

		SheetNumber(Integer sheetLen, Integer sheetWidth)
		{
			this.sheetLen = sheetLen;
			this.sheetWidth = sheetWidth;
			this.sheetNum = sheetLen * sheetWidth;
		}

		public Integer getSheetLen()
		{
			return sheetLen;
		}

		public void setSheetLen(Integer sheetLen)
		{
			this.sheetLen = sheetLen;
		}

		public Integer getSheetWidth()
		{
			return sheetWidth;
		}

		public void setSheetWidth(Integer sheetWidth)
		{
			this.sheetWidth = sheetWidth;
		}

		public Integer getSheetNum()
		{
			return sheetNum;
		}

		public void setSheetNum(Integer sheetNum)
		{
			this.sheetNum = sheetNum;
		}
	}

	/**
	 * <pre>
	 * 费用结果和费用公式
	 * </pre>
	 * @author zhengby
	 * @version 1.0, 2017年11月23日 下午5:08:29
	 */
	public static final class CustomCal
	{
		/**
		 * 费用
		 */
		String fee;

		/**
		 * 公式
		 */
		String formula;

		/**
		 * 包含印次
		 */
		Integer startSpeed;

		CustomCal(String fee, String formula, Integer startSpeed)
		{
			this.fee = fee;
			this.formula = formula;
			this.startSpeed = startSpeed;
		}

		public String getFee()
		{
			return fee;
		}

		public void setFee(String fee)
		{
			this.fee = fee;
		}

		public String getFormula()
		{
			return formula;
		}

		public void setFormula(String formula)
		{
			this.formula = formula;
		}

		public Integer getStartSpeed()
		{
			return startSpeed;
		}

		public void setStartSpeed(Integer startSpeed)
		{
			this.startSpeed = startSpeed;
		}

	}

	/**
	 * <pre>
	 * 拼版数计算
	 * 成品尺寸:大度32开(210*140)
	 * 最大上机尺寸740*530      最小上机尺寸390*270
	 * 
	 * ==========================================
	 * 第一种拼法:上机长/产品长  上机宽/产品宽
	 * 740/210=3.53       1 2 3
	 * 530/140=3.78       1 2 3
	 * 1.判断方法 上机尺寸最长边>最小上机尺寸最长边    推出合适的上机规格
	 *         上机尺寸最短边>最小上机尺寸最短边
	 * 
	 * 11   210*140 > 390*270     210>390   140>270   不满足
	 * 12   210*280 > 390*270     280>390   210>270   不满足
	 * 13   210*420 > 390*270     420>390   210>270   不满足
	 * 21   420*140 > 390*270     420>390   140>270   不满足  
	 * 22   420*280 > 390*270     420>390   280>270   满足
	 * 23   420*420 > 390*270     420>390   420>270   满足
	 * 31   630*140 > 390*270     630>390   140>270   不满足
	 * 32   630*280 > 390*270     630>390   280>270   满足
	 * 33   630*420 > 390*270     630>390   420>270   满足
	 * 合适规格:
	 * 拼版2*2=4   420*280  推算开数 8
	 * 拼版2*3=4   420*420  推算开数 4
	 * 拼版3*2=6   630*280  推算开数 4
	 * 拼版3*3=9   630*420  推算开数 2
	 * 
	 * 取拼版数*开数最大的
	 * 如果相等取拼版数最大的一种
	 * 
	 * ==========================================
	 * 第二种拼法:上机长/产品宽   上机宽/产品长
	 * 740/140=5.28  1 2 3 4 5
	 * 530/210=2.52  1 2
	 * 
	 * 11   140*210  >390*270     210>390   140>270   不满足
	 * 12   140*420  >390*270     420>390   140>270   不满足
	 * 21   280*210  >390*270     280>390   210>270   不满足
	 * 22   280*420  >390*270     420>390   280>270   满足
	 * 31   420*210  >390*270     420>390   210>270   不满足
	 * 32   420*420  >390*270     420>390   420>270   满足
	 * 41   560*210  >390*270     560>390   210>270   不满足
	 * 42   560*420  >390*270     560>390   420>270   满足
	 * 51   700*210  >390*270     700>390   210>270   不满足
	 * 52   700*420  >390*270     700>390   420>270   满足
	 * 合适规格:
	 * 拼版2*2=4    280*420  推算开数  8
	 * 拼版3*2=6    420*420  推算开数  4
	 * 拼版4*2=8    560*420  推算开数  4
	 * 拼版5*2=10   700*420  推算开数  2
	 * 
	 * 取拼版数*开数最大的
	 * 如果相等取拼版数最大的一种
	 * 
	 * ==========================================
	 * 最终比较：
	 * 最后将第一种方案与最第二种方案比较拼版数*开数最大的
	 * 如果相等,取拼版数最大的一种
	 * 
	 * </pre>
	 * @param maxStyle			机台最大上机（mm）
	 * @param minStyle			机台最小上机（mm）
	 * @param styleLength		展开的长
	 * @param styleWidth		展开的宽
	 * @return 拼版数
	 * 
	 * @since 1.0, 2017年11月7日 下午6:44:42, think
	 * @since 2.0, 2017年12月1日 上午10:20:42, think, 新的拼版算法
	 */
	public static List<SheetNumber> sheetNumber(String maxStyle, String minStyle, Integer styleLength, Integer styleWidth)
	{
		List<SheetNumber> ret = Lists.newArrayList();
		// 最大值
		String[] maxStyleSplit = maxStyle.split("\\*");
		Integer maxLen = Integer.parseInt(maxStyleSplit[0]);
		Integer maxWidth = Integer.parseInt(maxStyleSplit[1]);
		// 最小值
		String[] minStyleSplit = minStyle.split("\\*");
		Integer minLen = Integer.parseInt(minStyleSplit[0]);
		Integer minWidth = Integer.parseInt(minStyleSplit[1]);
		
		/**
		 * 上机尺寸最长边>最小上机尺寸最长边    推出合适的上机规格
	   * 上机尺寸最短边>最小上机尺寸最短边
		 */
		Integer temp = 0;
		if(minLen < minWidth)
		{
			temp = minLen;
			minLen = minWidth;
			minWidth = temp;
		}
		if(styleLength < styleWidth)
		{
			temp = styleLength;
			styleLength = styleWidth;
			styleWidth = temp;
		}

		// ===== 算法1： 长除以长，宽除以宽 =====
		int sheetLen = maxLen / styleLength;
		int sheetWidth = maxWidth / styleWidth;

		/**
		 * 11   210*140 > 390*270     210>390   140>270   不满足
		 * 12   210*280 > 390*270     280>390   210>270   不满足
		 * 13   210*420 > 390*270     420>390   210>270   不满足
		 * 21   420*140 > 390*270     420>390   140>270   不满足  
		 */
		for (int i = 1; i <= sheetLen; i++)
		{
			int _sheetLen = i * styleLength;
			// 必须满足才计算宽度
			if (_sheetLen >= minLen)
			{
				for (int j = 1; j <= sheetWidth; j++)
				{
					int _sheetWidth = j * styleWidth;

					if (_sheetWidth >= minWidth)
					{
						int _sheetNum = _sheetLen * _sheetWidth;
						if (_sheetNum > 0)
						{
							ret.add(new SheetNumber(i, j));
						}
					}
				}
			}
		}

		// ===== 算法2： 长除以宽，宽除以长 =====
		int sheetWidth2 = maxLen / styleWidth;
		int sheetLength2 = maxWidth / styleLength;

		/**
		 * 11   210*140 > 390*270     210>390   140>270   不满足
		 * 12   210*280 > 390*270     280>390   210>270   不满足
		 * 13   210*420 > 390*270     420>390   210>270   不满足
		 * 21   420*140 > 390*270     420>390   140>270   不满足  
		 */
		for (int i = 1; i <= sheetWidth2; i++)
		{
			int _sheetWidth = i * styleWidth;
			// 必须满足才计算宽度
			if (_sheetWidth >= minWidth)
			{
				for (int j = 1; j <= sheetLength2; j++)
				{
					int _sheetLength = j * styleLength;

					if (_sheetLength >= minLen)
					{
						int _sheetNum = _sheetWidth * _sheetLength;
						if (_sheetNum > 0)
						{
							ret.add(new SheetNumber(j, i));
						}
					}
				}
			}
		}

		return ret;
	}

	/**
	 * <pre>
	 * 截断小数点
	 * </pre>
	 * @param val
	 * @param size
	 * @return
	 * @since 1.0, 2017年11月9日 下午6:33:35, think
	 */
	public static double scale(double val, Integer size)
	{
		BigDecimal decimal = new BigDecimal(val);
		return decimal.setScale(size, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * <pre>
	 * 截断小数点（默认2位）
	 * </pre>
	 * @param val
	 * @return
	 * @since 1.0, 2017年11月9日 下午6:32:52, think
	 */
	public static String scale(double val)
	{
		DecimalFormat df = new DecimalFormat("0.00");
		df.setRoundingMode(RoundingMode.HALF_UP);
		return df.format(val);
	}

	/**
	 * <pre>
	 * 上机规格(上机尺寸)：再以产品的展开的长乘以长的拼版数得到印张印刷的上机规格的长，产品的展宽乘以宽的拼版数得到印张印刷的上机规格的宽，
	 * 印张的印刷上机规格必须小于机台的最大上机规格。
	 * 
	 * 比如说上机规格最大是890*630,产品规格是210*285
	 * 1.一种拼版算法
	 * 890/210=4.23P　　取整即为890长上可拼4P
	 * 630/285=2.21P  取整即为630宽度上可拼2P 
	 * 
	 * 一个印张的拼版P数＝4*2=8
	 * 所以实际上机规格算出来:840*570
	 * 
	 * 纸张规格为889*1194,上机规格为840*570,可以进行两开，这样算出来一张大度纸单面就可以排16P
	 * </pre>
	 * @param styleLength		展开的长
	 * @param styleWidth		展开的宽
	 * @param sheetNumber		拼版数
	 * @return
	 * @since 1.0, 2017年11月7日 下午6:48:29, think
	 */
	public static String style(Integer styleLength, Integer styleWidth, SheetNumber sheetNumber)
	{
		return (styleLength * sheetNumber.getSheetLen()) + "*" + (styleWidth * sheetNumber.getSheetWidth());
	}

	/**
	 * <pre>
	 * 印张正数（书刊算法不同）
	 * </pre>
	 * @param amount
	 * @param sheetNum
	 * @param pNum
	 * @return
	 * @since 1.0, 2017年11月25日 下午2:08:46, zhengby
	 */
	public static Integer impositinNum(Integer amount, Integer sheetNum, Integer pNum, OfferPrintStyleType offerPrintStyleType, OfferOrder offerOrder)
	{

		// 印张正数
		Integer impositionNum = 0;
		// 印张正数：包装类：印刷数量/拼版数 || 书刊类：印刷数量*部件P数/单面拼数*（单面=1， 双面=2）
		if (offerOrder.getOfferType() == OfferType.ALBUMBOOK)
		{
			impositionNum = _impositinNum(amount, sheetNum, pNum, offerPrintStyleType);
		}
		else if (offerOrder.getOfferType() == OfferType.ASSOCIATEDSINGLECLASS)
		{
			// 联单类
			impositionNum = _impositionNum(amount, offerOrder.getPageType(), offerOrder.getSheetType().getValue(), sheetNum);
		}
		else if (offerOrder.getOfferType() == OfferType.NOTESLETTERFORM)
		{
			// 便签类
			impositionNum = _impositionNum(amount, offerOrder.getPageType(), sheetNum);
		}
		else
		{
			// 包装类
			impositionNum = _impositionNum(amount, sheetNum);
		}

		return impositionNum;
	}

	/**
	 * <pre>
	 * 印张正数：印刷数量*部件P数/单面拼数*2(书刊类)
	 * </pre>
	 * @param amount
	 * @param sheetNum
	 * @param pNum
	 * @param offerPrintStyleType
	 * @return
	 * @since 1.0, 2017年11月30日 下午6:01:57, think
	 */
	public static Integer _impositinNum(Integer amount, Integer sheetNum, Integer pNum, OfferPrintStyleType offerPrintStyleType)
	{
		int printColor = 2;
		if (OfferPrintStyleType.SINGLE == offerPrintStyleType)
		{
			printColor = 1;
		}
		return (int) Math.ceil((double) amount * pNum / sheetNum / printColor);
	}

	/**
	 * <pre>
	 * 印张正数：印刷数量/拼版数
	 * </pre>
	 * @param amount			印刷数量
	 * @param sheetNumber	拼版数
	 * @return
	 * @since 1.0, 2017年11月7日 下午6:53:42, think
	 */
	public static Integer _impositionNum(Integer amount, Integer sheetNum)
	{
		return (int) Math.ceil((double) amount / sheetNum);
	}

	/**
	 * <pre>
	 * 印张正数：印刷数量本数*页数*联次/拼版数(联单类)
	 * </pre>
	 * @param amount 印刷数量
	 * @param pages 页数
	 * @param bills 联次
	 * @param sheetNum 拼版数
	 * @return
	 * @since 1.0, 2017年12月6日 下午3:44:37, zhengby
	 */
	public static Integer _impositionNum(Integer amount, Integer pages, Integer bills, Integer sheetNum)
	{
		return (int) Math.ceil((double) amount * pages * bills / sheetNum);
	}

	/**
	 * <pre>
	 * 印张正数：印刷数量本数*页数/拼版数(便签类)
	 * </pre>
	 * @param amount 印刷数量本数
	 * @param pages 页数
	 * @param sheetNum 拼版数
	 * @return
	 * @since 1.0, 2017年12月6日 下午3:57:02, zhengby
	 */
	public static Integer _impositionNum(Integer amount, Integer pages, Integer sheetNum)
	{
		return (int) Math.ceil((double) amount * pages / sheetNum);
	}

	/**
	 * <pre>
	 * 损耗数：根据报价设置里的对应的损耗公式计算出损耗数
	 * 举例： 单色印刷机，起印印次为2000张，起印张数/版为150，每千次/版为50，如果要印刷5600张印张，那么印刷的损耗：150+（5600-2000）/1000*50=150+4*50=350
	 * </pre>
	 * @param offerOrder
	 * @param offerMachine
	 * @param offerPart
	 * @return
	 * @since 1.0, 2017年11月7日 下午6:59:15, think
	 */
	public static Integer waste(OfferOrder offerOrder, OfferMachine offerMachine, OfferPart offerPart)
	{
		OfferWaste offerWaste = OfferHelper.findWasteSettingByOfferType(offerOrder.getOfferType());
		// 起印张数/版
		Integer startSheetZQ = 0;
		// 每千印/版
		Integer thousandSheetZQ = 0;
		// 印刷数量
		Integer impositionNum = offerMachine.getImpositionNum();

		// 印刷方式（单面、双面）
		OfferPrintStyleType offerPrintStyleType = offerPart.getOfferPrintStyleType();
		// 印刷颜色
		OfferPrintColorType offerPrintColorType = offerPart.getOfferPrintColorType();
		// 专色
		OfferSpotColorType offerSpotColorType = offerPart.getOfferSpotColorType();
		// 印刷颜色2（双面印刷用到）
		OfferPrintColorType offerPrintColorType2 = offerPart.getOfferPrintColorType2();
		// 专色2（双面印刷用到）
		OfferSpotColorType offerSpotColorType2 = offerPart.getOfferSpotColorType2();
		// 计算总的颜色
		int color = offerPrintColorType.getValue() + offerSpotColorType.getValue();
		// 双面总的颜色
		if (OfferPrintStyleType.DOUBLE == offerPrintStyleType || OfferPrintStyleType.HEADTAIL == offerPrintStyleType || OfferPrintStyleType.CHAOS == offerPrintStyleType)
		{
			color = color + offerPrintColorType2.getValue() + offerSpotColorType2.getValue();
		}

		// 单面（计算到底是单面，还是双面）
		if (color <= 1)
		{
			startSheetZQ = offerWaste.getSpStartSheetZQ();
			thousandSheetZQ = offerWaste.getSpThousandSheetZQ();
		}
		// 双面
		else
		{
			startSheetZQ = offerWaste.getDpStartSheetZQ();
			thousandSheetZQ = offerWaste.getDpThousandSheetZQ();
		}
		// 如果勾选了印后工序需要追加 印后加工的损耗
		Integer workAfter = 0;
		if (offerPart.getOfferPartProcedureList() != null && offerPart.getOfferPartProcedureList().size() > 0)
		{
			workAfter = offerWaste.getWorkAfter();
		}
		// 单色印刷机，起印印次为2000张，起印张数/版为150，每千次/版为50，如果要印刷5600张印张，那么印刷的损耗：150+（5600-2000）/1000*50=150+4*50=350
		if (OfferMachineType.START_PRINT == offerMachine.getOfferMachineType())
		{
			Integer startSpeed = machineStartSpeed(offerMachine, offerOrder, offerPart);
			// 小于包含印次，直接返回起印张数/版
			if (impositionNum <= startSpeed)
			{
				return startSheetZQ + workAfter;
			}
			else
			{
				double speed = (impositionNum - startSpeed) / 1000d;
				return (int) Math.ceil(startSheetZQ + speed * thousandSheetZQ + workAfter);
			}
		}
		else if (OfferMachineType.CUSTOM == offerMachine.getOfferMachineType())
		{
			// TODO 自定义报价包含印次等于0（暂时）
			double speed = (impositionNum - 0) / 1000d <= 0 ? 1 : (impositionNum - 0) / 1000d;
			return (int) Math.ceil(startSheetZQ + speed * thousandSheetZQ + workAfter);
		}

		throw new IllegalArgumentException("找不到机台公式类型");
	}

	/**
	 * <pre>
	 * 计算机台的最低价格
	 * </pre>
	 * @param offerMachine
	 * @param offerOrder
	 * @param offerPart
	 * @return
	 * @since 1.0, 2017年11月6日 下午3:17:54, think
	 */
	public static CustomCal quoteInnerMachineOptimal(OfferMachine offerMachine, OfferOrder offerOrder, OfferPart offerPart)
	{
		if (OfferMachineType.START_PRINT == offerMachine.getOfferMachineType())
		{
			return quoteInnerMachineStartPrint(offerMachine, offerOrder, offerPart);
		}
		else if (OfferMachineType.CUSTOM == offerMachine.getOfferMachineType())
		{
			return quoteInnerMachineStartPrintCustom(offerMachine, offerOrder, offerPart);
		}

		throw new IllegalArgumentException("机台公式类型不存在");
	}

	/**
	 * <pre>
	 * 计算机台的最低价格
	 * </pre>
	 * @param offerMachine
	 * @param offerOrder
	 * @param offerPart
	 * @return
	 * @since 1.0, 2017年11月6日 下午3:18:02, think
	 */
	public static CustomCal quoteInnerMachineStartPrint(OfferMachine offerMachine, OfferOrder offerOrder, OfferPart offerPart)
	{
		/**
		 * 1. 判断计算方式
		 *   1-1. 普通计算（如： 零色|单色|双色|... + 无专）
		 *   1-2. 普色+专色（如： 单色|双色|... + 一专|二专|...）
		 *   1-3. 单印+专色（如： 零色 + 一专|二专|...）
		 * 2. 计算
		 */

		// 1. 判断计算方式
		// 印刷方式（单面、双面）
		OfferPrintStyleType offerPrintStyleType = offerPart.getOfferPrintStyleType();
		// 印刷颜色
		OfferPrintColorType offerPrintColorType = offerPart.getOfferPrintColorType();
		// 专色
		OfferSpotColorType offerSpotColorType = offerPart.getOfferSpotColorType();
		// 印刷颜色2（双面印刷用到）
		OfferPrintColorType offerPrintColorType2 = offerPart.getOfferPrintColorType2();
		// 专色2（双面印刷用到）
		OfferSpotColorType offerSpotColorType2 = offerPart.getOfferSpotColorType2();
		// 机台是否开启专色
		BoolValue joinSpotColor = offerMachine.getOfferStartPrint().getJoinSpotColor();
		// 计算总的颜色
		int pcolor = offerPrintColorType.getValue();
		int scolor = offerSpotColorType.getValue();
		int colos = pcolor + scolor;
		if (OfferPrintStyleType.DOUBLE == offerPrintStyleType || OfferPrintStyleType.HEADTAIL == offerPrintStyleType || OfferPrintStyleType.CHAOS == offerPrintStyleType)
		{
			pcolor = pcolor + offerPrintColorType2.getValue();
			scolor = scolor + offerSpotColorType2.getValue();
			colos = pcolor + scolor;
		}
		// 印版付数＝印刷面数，单面印刷＝1,双面印刷＝2
		int ybfs = offerPart.getOfferPrintStyleType().getValue();
		// 书刊的印版付数＝印刷面数*贴数，单面印刷＝1*贴数，双面印刷＝贴数*2
		if (OfferType.ALBUMBOOK == offerOrder.getOfferType())
		{
			Integer thread = OfferFormulaHelper.thread(offerPart.getPages(), offerMachine.getSheetNum(), offerPart.getOfferPrintStyleType());
			ybfs = offerPart.getOfferPrintStyleType().getValue() * thread;
		}

		// 2. 计算
		// 其他没有开启专色，则直接使用默认计算
		if (BoolValue.NO == joinSpotColor)
		{
			String printColor = "【普通】";
			return quoteInnerMachineStartPrintNormal(offerMachine, offerOrder, colos, printColor, ybfs);
		}

		// 普色+专色（如： 单色|双色|... + 一专|二专|...）
		if (BoolValue.YES == joinSpotColor && pcolor > 0 && scolor > 0)
		{
			String printColor = "【普色+专色】";
			return quoteInnerMachineStartPrintSpotColor(offerMachine, offerOrder, colos, printColor, ybfs);
		}
		// 单印+专色（如： 零色 + 一专|二专|...）
		else if (BoolValue.YES == joinSpotColor && pcolor == 0 && scolor > 0)
		{
			String printColor = "【单印+专色】";
			return quoteInnerMachineStartPrintSpotColor2(offerMachine, offerOrder, colos, printColor, ybfs);
		}
		// 普通计算（如： 零色|单色|双色|... + 无专）
		else
		{
			String printColor = "【普通】";
			return quoteInnerMachineStartPrintNormal(offerMachine, offerOrder, colos, printColor, ybfs);
		}

	}

	/**
	 * <pre>
	 * 计算机台费用自定义报价公式 
	 * </pre>
	 * @param offerMachine
	 * @param offerOrder
	 * @param offerPart
	 * @return
	 * @since 1.0, 2017年11月22日 下午7:49:28, zhengby
	 */
	public static CustomCal quoteInnerMachineStartPrintCustom(OfferMachine offerMachine, OfferOrder offerOrder, OfferPart offerPart)
	{
		if(StringUtils.isEmpty(offerMachine.getOfferFormula().getFormula()))
		{
			return null;
		}
		try
		{
			String formula = offerMachine.getOfferFormula().getFormula();
			return _feeCustomPart(formula, offerPart, offerOrder, null, offerMachine);
		}
		catch (ScriptException e)
		{
			e.printStackTrace();
		}
		return new CustomCal("0", "自定义公式错误", 0);
	}

	/**
	 * <pre>
	 * 普通计算（如： 零色|单色|双色|... + 无专）
	 * </pre>
	 * @param offerMachine
	 * @param offerOrder
	 * @param colors
	 * @return
	 * @since 1.0, 2017年11月6日 下午2:59:42, think
	 */
	public static CustomCal quoteInnerMachineStartPrintNormal(OfferMachine offerMachine, OfferOrder offerOrder, Integer colors, String printColor, int ybfs)
	{
		OfferStartPrint offerStartPrint = offerMachine.getOfferStartPrint();
		// 【普通】开机费
		Integer startFee = offerStartPrint.getStartFee();
		// 【普色+专色】包含印次
		Integer startSpeed = offerStartPrint.getStartSpeed();
		// 【普通】印工费 - 次（以下）
		Integer thousandSpeedBelow = offerStartPrint.getThousandSpeedBelow();
		// 【普通】印工费 - 每千次/元（以下）
		Integer thousandSpeedBelowMoney = offerStartPrint.getThousandSpeedBelowMoney();
		// 【普通】印工费 - 次（以上）
		Integer thousandSpeedAbove = offerStartPrint.getThousandSpeedAbove();
		// 【普通】印工费 - 每千次/元（以上）
		Integer thousandSpeedAboveMoney = offerStartPrint.getThousandSpeedAboveMoney();
		// 印刷正数
		Integer impositionNum = offerMachine.getImpositionNum();

		return _quoteInnerMachineStartPrint(startFee, startSpeed, thousandSpeedBelow, thousandSpeedBelowMoney, thousandSpeedAbove, thousandSpeedAboveMoney, impositionNum, offerStartPrint.getReamColorStartSpeed(), offerStartPrint.getReamColorMoney(), offerStartPrint.getReamColorCopyFee(), colors, printColor, ybfs);
	}

	/**
	 * <pre>
	 * 普色+专色（如： 单色|双色|... + 一专|二专|...）
	 * </pre>
	 * @param offerMachine
	 * @param offerOrder
	 * @param colors
	 * @return
	 * @since 1.0, 2017年11月6日 下午3:12:31, think
	 */
	public static CustomCal quoteInnerMachineStartPrintSpotColor(OfferMachine offerMachine, OfferOrder offerOrder, Integer colors, String printColor, int ybfs)
	{
		OfferStartPrint offerStartPrint = offerMachine.getOfferStartPrint();
		// 【普色+专色】开机费
		Integer startFee = offerStartPrint.getSpotColorStartFee();
		// 【普色+专色】包含印次
		Integer startSpeed = offerStartPrint.getSpotColorStartSpeed();
		// 【普色+专色】印工费 - 次（以下）
		Integer thousandSpeedBelow = offerStartPrint.getSpotColorThousandSpeedBelow();
		// 【普色+专色】印工费 - 每千次/元（以下）
		Integer thousandSpeedBelowMoney = offerStartPrint.getSpotColorThousandSpeedBelowMoney();
		// 【普色+专色】印工费 - 次（以上）
		Integer thousandSpeedAbove = offerStartPrint.getSpotColorThousandSpeedAbove();
		// 【普色+专色】印工费 - 每千次/元（以上）
		Integer thousandSpeedAboveMoney = offerStartPrint.getSpotColorThousandSpeedAboveMoney();
		// 印刷正数
		Integer impositionNum = offerMachine.getImpositionNum();

		return _quoteInnerMachineStartPrint(startFee, startSpeed, thousandSpeedBelow, thousandSpeedBelowMoney, thousandSpeedAbove, thousandSpeedAboveMoney, impositionNum, offerStartPrint.getReamColorStartSpeed(), offerStartPrint.getReamColorMoney(), offerStartPrint.getReamColorCopyFee(), colors, printColor, ybfs);
	}

	/**
	 * <pre>
	 * 单印+专色（如： 零色 + 一专|二专|...）
	 * </pre>
	 * @param offerMachine
	 * @param offerOrder
	 * @param colors
	 * @return
	 * @since 1.0, 2017年11月6日 下午3:14:50, think
	 */
	public static CustomCal quoteInnerMachineStartPrintSpotColor2(OfferMachine offerMachine, OfferOrder offerOrder, Integer colors, String printColor, int ybfs)
	{
		OfferStartPrint offerStartPrint = offerMachine.getOfferStartPrint();
		// 【单印+专色】开机费
		Integer startFee = offerStartPrint.getSpotColor2StartFee();
		// 【单印+专色】包含印次
		Integer startSpeed = offerStartPrint.getSpotColor2StartSpeed();
		// 【单印+专色】印工费 - 次（以下）
		Integer thousandSpeedBelow = offerStartPrint.getSpotColor2ThousandSpeedBelow();
		// 【单印+专色】印工费 - 每千次/元（以下）
		Integer thousandSpeedBelowMoney = offerStartPrint.getSpotColor2ThousandSpeedBelowMoney();
		// 【单印+专色】印工费 - 次（以上）
		Integer thousandSpeedAbove = offerStartPrint.getSpotColor2ThousandSpeedAbove();
		// 【单印+专色】印工费 - 每千次/元（以上）
		Integer thousandSpeedAboveMoney = offerStartPrint.getSpotColor2ThousandSpeedAboveMoney();
		// 印刷正数
		Integer impositionNum = offerMachine.getImpositionNum();

		return _quoteInnerMachineStartPrint(startFee, startSpeed, thousandSpeedBelow, thousandSpeedBelowMoney, thousandSpeedAbove, thousandSpeedAboveMoney, impositionNum, offerStartPrint.getReamColorStartSpeed(), offerStartPrint.getReamColorMoney(), offerStartPrint.getReamColorCopyFee(), colors, printColor, ybfs);
	}

	/**
	 * <pre>
	 * 计算机台 - 各种细节费用
	 * </pre>
	 * @param startFee									开机费
	 * @param startSpeed								包含印次
	 * @param thousandSpeedBelow				印工费 - 次（以下）
	 * @param thousandSpeedBelowMoney		印工费 - 每千次/元（以下）
	 * @param thousandSpeedAbove				印工费 - 次（以上）
	 * @param thousandSpeedAboveMoney		印工费 - 每千次/元（以上）
	 * @param impositionNum							印刷正数
	 * @param reamColorStartSpeed				【色令】色令印数开始值
	 * @param reamColorMoney						【色令】色令价
	 * @param reamColorCopyFee					【色令】版费
	 * @param colors										【色令】总颜色
	 * @return	
	 * @since 1.0, 2017年11月6日 下午3:04:02, think
	 */
	private static CustomCal _quoteInnerMachineStartPrint(Integer startFee, Integer startSpeed, Integer thousandSpeedBelow, Integer thousandSpeedBelowMoney, Integer thousandSpeedAbove, Integer thousandSpeedAboveMoney, Integer impositionNum, Integer reamColorStartSpeed, Integer reamColorMoney, Integer reamColorCopyFee, Integer colors, String printColor, int ybfs)
	{
		OfferStartPrint ret = new OfferStartPrint();
		ret.setStartFee(startFee);
		ret.setStartSpeed(startSpeed);
		ret.setThousandSpeedBelow(thousandSpeedBelow);
		ret.setThousandSpeedBelowMoney(thousandSpeedBelowMoney);
		ret.setThousandSpeedAbove(thousandSpeedAbove);
		ret.setThousandSpeedAboveMoney(thousandSpeedAboveMoney);
		ret.setAmount(impositionNum);
		ret.setReamColorStartSpeed(reamColorStartSpeed);
		ret.setReamColorMoney(reamColorMoney);
		ret.setReamColorCopyFee(reamColorCopyFee);

		StringBuilder str = new StringBuilder(printColor);
		// 【普通】计算(基本印工费 = 开机费 * 开机次数)
		double speed = (impositionNum - startSpeed) / 1000d;
		double append = 0;
		// 在包含印次内的价格都是起步价
		if (impositionNum <= startSpeed)
		{
			append = 0;
			ret.setMoney(0);
			str.append("开机费").append(startFee);
		}
		else
		{
			str.append("开机费").append(startFee);
			// 千次以下
			if (speed > 0 && impositionNum <= thousandSpeedBelow)
			{
				append = speed * thousandSpeedBelowMoney;
				ret.setMoney(thousandSpeedBelowMoney);
				str.append("+印工单价").append(thousandSpeedBelowMoney).append("*（印张正数-包含印次/1000）").append(speed);
			}
			// 千次以上
			else if (speed > 0 && impositionNum >= thousandSpeedAbove)
			{
				append = speed * thousandSpeedAboveMoney;
				ret.setMoney(thousandSpeedAboveMoney);
				str.append("+印工单价").append(thousandSpeedAboveMoney).append("*（印张正数-包含印次/1000）").append(speed);
			}
		}

		// 色令
		if (null != reamColorStartSpeed)
		{
			if (speed > 0 && impositionNum > reamColorStartSpeed)
			{
				// 总版费
				int allCopyFee = reamColorCopyFee * colors;
				// 印张整数
				speed = impositionNum.doubleValue() / 1000;
				// 版费
				// 公式：色令价*总颜色*印数/1000+版费*总颜色
				append = reamColorMoney * colors * speed + allCopyFee;
				ret.setMoney(reamColorMoney);
				// 色令价格不需要起步价了
				startFee = 0;
				str = new StringBuilder();
				str.append("色令价").append(reamColorMoney).append("*总颜色").append(colors);
				str.append("*（印张正数/1000）").append(speed).append("+版费").append(reamColorCopyFee);
				str.append("*总颜色").append(colors);
			}
		}
		// 最终计算的价格
		ret.setPrice(Double.valueOf(startFee + append));
		str.append("=").append(scale(Double.valueOf(ret.getPrice())));
		return new CustomCal(scale(startFee + append), str.toString(), startSpeed);
	}

	/**
	 * <pre>
	 * 计算机台 - 包含印次
	 * </pre>
	 * @param offerMachine
	 * @param offerOrder
	 * @param offerPart
	 * @return
	 * @since 1.0, 2017年11月9日 下午3:09:12, think
	 */
	public static Integer machineStartSpeed(OfferMachine offerMachine, OfferOrder offerOrder, OfferPart offerPart)
	{

		CustomCal customCal = quoteInnerMachineOptimal(offerMachine, offerOrder, offerPart);
		if (null != customCal)
		{
			return customCal.getStartSpeed();
		}
		return 0;
	}

	/**
	 * <pre>
	 * 大纸张数：（印张正数+损耗数）/纸开度
	 * </pre>
	 * @param impositionNum		印张正数
	 * @param waste						损耗数
	 * @param materialOpening	纸开度
	 * @return
	 * @since 1.0, 2017年11月7日 下午7:01:23, think
	 */
	public static Integer bigPaperNum(Integer impositionNum, Integer waste, Integer materialOpening)
	{
		return (int) Math.ceil((double) (impositionNum + waste) / materialOpening);
	}

	/**
	 * <pre>
	 * 坑纸数：印张正数+印后工序
	 * </pre>
	 * @param impositionNum	印张正数
	 * @param workAfter			印后工序
	 * @return
	 * @since 1.0, 2017年11月7日 下午7:02:54, think
	 */
	public static Integer bfluteNum(Integer impositionNum, Integer workAfter)
	{
		return impositionNum + workAfter;
	}

	/**
	 * <pre>
	 * 计算 - 开料开数
	 * 正度纸787*1092和大度纸889*1194，交叉相除，得出最大的值作为开数
	 * </pre>
	 * @param paperType 纸张类型
	 * @param style     上机规格
	 * @since 1.0, 2017年11月7日 下午5:15:02, think
	 */
	public static Integer materialOpening(PaperType paperType, String style)
	{
		String[] styleSplit = style.split("\\*");
		Integer len = Integer.parseInt(styleSplit[0]);
		Integer width = Integer.parseInt(styleSplit[1]);

		// ===== 算法1： 长除以长，宽除以宽 =====
		// 长度开数
		Integer openingLen = paperType.getLength() / len;
		// 宽度开数
		Integer openingWidth = paperType.getWidth() / width;
		// 材料开数
		Integer opening = openingLen * openingWidth;

		// ===== 算法2： 长除以宽，宽除以长 =====
		// 长度开数
		Integer openingLen2 = paperType.getLength() / width;
		// 宽度开数
		Integer openingWidth2 = paperType.getWidth() / len;
		// 材料开数
		Integer opening2 = openingLen2 * openingWidth2;

		return opening > opening2 ? opening : opening2;
	}

	/**
	 * <pre>
	 * 计算 - 纸张费
	 * </pre>
	 * @param lowerPrice
	 * @param offerPart
	 * @return
	 * @since 1.0, 2017年11月9日 下午2:06:37, think
	 */
	public static double paperFee(OfferMachine lowerPrice, OfferPart offerPart)
	{
		double bigPaperFee = bigPaperFee(lowerPrice.getPaperType(), offerPart.getPaperWeight(), lowerPrice.getBigPaperNum(), offerPart.getPaperTonPrice());
		double bflutePaperFee = bflutePaperFee(lowerPrice.getBfluteNum(), lowerPrice.getStyle(), offerPart.getBflutePrice());
		return bigPaperFee + bflutePaperFee;
	}

	/**
	 * <pre>
	 * 纸张费 - 大纸张费: 大纸尺寸的规格长/1000*大纸尺寸规格宽/1000*克重/1000/1000*大纸张数*吨价
	 * </pre>
	 * @param paperType				大纸尺寸
	 * @param paperWeight			克重
	 * @param bigPaperNum			大纸张数
	 * @param paperTonPrice		吨价
	 * @return
	 * @since 1.0, 2017年11月8日 下午2:07:37, think
	 */
	public static double bigPaperFee(PaperType paperType, Integer paperWeight, Integer bigPaperNum, BigDecimal paperTonPrice)
	{
		double fee = (double) paperType.getLength() / 1000 * paperType.getWidth() / 1000 * paperWeight / 1000 / 1000 * bigPaperNum * paperTonPrice.intValue();
		return Double.valueOf(scale(fee));
	}

	/**
	 * <pre>
	 * 纸张费 - 坑张费: B坑C3坑纸数5400x坑纸尺寸710*920x坑纸单价0.79（千平方英寸）=4319.16
	 * </pre>
	 * @param bfluteNum			坑纸数
	 * @param style					坑纸尺寸
	 * @param bflutePrice		坑纸单价
	 * @return
	 * @since 1.0, 2017年11月8日 下午4:46:06, think
	 */
	public static double bflutePaperFee(Integer bfluteNum, String style, BigDecimal bflutePrice)
	{
		double fee = bfluteCalNum(bfluteNum, style).doubleValue() * bflutePrice.doubleValue();
		return Double.valueOf(scale(fee));
	}

	/**
	 * <pre>
	 * 坑纸计价数量
	 * </pre>
	 * @param bfluteNum
	 * @param style
	 * @return
	 * @since 1.0, 2017年12月7日 下午6:25:12, zhengby
	 */
	public static BigDecimal bfluteCalNum(Integer bfluteNum, String style)
	{
		String[] styleSplit = style.split("\\*");
		Integer len = Integer.parseInt(styleSplit[0]);
		Integer width = Integer.parseInt(styleSplit[1]);
		// 比较两个单位，长度更长的作为长。
		if (len < width)
		{
			Integer ex = len;
			len = width;
			width = ex;
		}

		double baseQty = len.doubleValue() / 25.4;
		if (baseQty <= 14.5)
		{
			baseQty = 14.5;
		}
		else if (baseQty > 14.5 && baseQty <= 27.5)
		{
			baseQty = baseQty * 2;
			baseQty = (Math.ceil(baseQty) % 2 == 0) ? (Math.ceil(baseQty) + 1) / 2 : Math.ceil(baseQty) / 2;
		}
		else if (baseQty > 27.5 && baseQty <= 55)
		{
			baseQty = (Math.ceil(baseQty) % 2 == 0) ? (Math.ceil(baseQty) + 1) : Math.ceil(baseQty);
		}

		return new BigDecimal((double) baseQty * width / 25.4 * bfluteNum / 1000);
	}

	/**
	 * <pre>
	 * 手/贴数：P数/单面拼数，进位取整，不足1的按一贴算
	 * </pre>
	 * @param pNum
	 * @param sheetNum
	 * @param offerPrintStyleType
	 * @return
	 * @since 1.0, 2017年11月22日 下午6:01:01, think
	 */
	public static Integer thread(Integer pNum, Integer sheetNum, OfferPrintStyleType offerPrintStyleType)
	{
		// 自翻印刷除以1，其他除以2
		int rate = OfferPrintStyleType.CHAOS == offerPrintStyleType ? 1 : 2;
		int v = (int) Math.ceil((double) pNum / sheetNum / rate);
		return v <= 0 ? 1 : v;
	}

	/**
	 * <pre>
	 * 工序费用
	 * </pre>
	 * @param impositionNum
	 * @param amount
	 * @param threads
	 * @param style
	 * @param offerPart
	 * @param offerPartProcedure
	 * @return
	 * @since 1.0, 2017年11月22日 下午6:03:18, think
	 */
	public static CustomCal procedureFee(Integer amount, OfferPart offerPart, OfferPartProcedure offerPartProcedure, OfferProcedure offerProcedure, OfferMachine offerMachine, OfferOrder offerOrder)
	{
		if (OfferProcedureFormulaType.NORMAL == offerPartProcedure.getOfferProcedureFormulaType() && null != offerPart)
		{
			// 印张正数
			Integer impositionNum = OfferFormulaHelper.impositinNum(amount, offerMachine.getSheetNum(), offerPart.getPages(), offerPart.getOfferPrintStyleType(), offerOrder);
			// 总贴数
			Integer threads = (null == offerMachine.getThreads() ? 0 : offerMachine.getThreads());
			// P数
			Integer pages = offerPart.getPages();
			// style
			String style = offerMachine.getStyle();

			return _procedureFeeNormal(impositionNum, amount, threads, pages, style, offerOrder, offerPartProcedure);
			// 成品工序
		}
		else if (OfferProcedureFormulaType.NORMAL == offerPartProcedure.getOfferProcedureFormulaType() && null == offerPart)
		{
			return _procedureFeeNormal(0, amount, 0, 0, "0*0", offerOrder, offerPartProcedure);
		}
		else
		{
			// 自定义工序 计算自定义工序费时不需要机台的字段
			try
			{
				OfferOrder _offerOrder = ObjectHelper.byteClone(offerOrder);
				OfferPart _offerPart = ObjectHelper.byteClone(offerPart);
				OfferMachine _offerMachine = ObjectHelper.byteClone(offerMachine);
				_offerOrder.setAmount(amount);
				// 部件工序
				if (null != _offerPart)
				{
					// 印张正数
					Integer impositionNum = OfferFormulaHelper.impositinNum(amount, offerMachine.getSheetNum(), offerPart.getPages(), offerPart.getOfferPrintStyleType(), offerOrder);
					_offerMachine.setImpositionNum(impositionNum);
					// 印刷数量
					_offerPart.setAmount(amount);
					return _feeCustomPart(offerProcedure.getCustomFormula(), _offerPart, _offerOrder, offerPartProcedure, _offerMachine);
				}
				// 成品工序
				else
				{
					return _feeCustomProduct(offerProcedure.getCustomFormula(), _offerOrder, _offerMachine);
				}
			}
			catch (ScriptException e)
			{
				e.printStackTrace();
			}
			return new CustomCal(scale(0), "自定义公式", 0);
		}
	}

	/**
	 * <pre>
	 * 工序费用 - 普通公式
	 * </pre>
	 * @param impositionNum
	 * @param amount
	 * @param threads
	 * @param style
	 * @param offerPart
	 * @param offerPartProcedure
	 * @return
	 * @since 1.0, 2017年11月22日 下午6:05:32, think
	 */
	public static CustomCal _procedureFeeNormal(Integer impositionNum, Integer amount, Integer threads, Integer pages, String style, OfferOrder order, OfferPartProcedure offerPartProcedure)
	{
		CustomCal procedureCal = null;
		ProcedureUnit unit = offerPartProcedure.getProcedureUnit();
		BigDecimal price = offerPartProcedure.getPrice();
		BigDecimal startPrice = offerPartProcedure.getStartPrice();
		BigDecimal lowestPrice = offerPartProcedure.getLowestPrice();
		Integer length = offerPartProcedure.getLength();
		Integer width = offerPartProcedure.getWidth();

		if (ProcedureUnit.UNIT1 == unit)
		{
			procedureCal = _procedureFeeUnit1(impositionNum, price, startPrice);
		}
		else if (ProcedureUnit.UNIT2 == unit)
		{
			procedureCal = _procedureFeeUnit2(impositionNum, style, price, startPrice, lowestPrice);
		}
		else if (ProcedureUnit.UNIT3 == unit)
		{
			procedureCal = _procedureFeeUnit3(impositionNum, length, width, price, startPrice,lowestPrice);
		}
		else if (ProcedureUnit.UNIT4 == unit)
		{
			procedureCal = _procedureFeeUnit4(amount, price, startPrice);
		}
		else if (ProcedureUnit.UNIT5 == unit)
		{
			procedureCal = _procedureFeeUnit5(impositionNum, threads, price, startPrice);
		}
		else if (ProcedureUnit.UNIT6 == unit)
		{
			procedureCal = _procedureFeeUnit6(impositionNum, pages, price, startPrice);
		}
		else if (ProcedureUnit.UNIT7 == unit)
		{
			procedureCal = _procedureFeeUnit7(amount, price, startPrice);
		}
		return procedureCal;
	}

	/**
	 * <pre>
	 * 工序费用 - 部件工序自定义公式
	 * </pre>
	 * @param offerProcedure
	 * @param offerMachine
	 * @param offerOrder
	 * @param offerPartProcedure
	 * @param offerMachine
	 * @return
	 * @throws ScriptException 
	 * @since 1.0, 2017年11月24日 上午9:33:38, zhengby
	 */
	private static CustomCal _feeCustomPart(String formula, OfferPart offerPart, OfferOrder offerOrder, OfferPartProcedure offerPartProcedure, OfferMachine offerMachine) throws ScriptException
	{
		formula = ParamsLadderType.forPart(formula, offerMachine, offerPart, offerOrder, offerPartProcedure);
		ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
		BigDecimal fee = new BigDecimal(jse.eval(formula).toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		return new CustomCal(scale(fee.doubleValue()), "【自定义公式】", 0);
	}

	/**
	 * <pre>
	 * 工序费用 - 成品工序自定义公式
	 * </pre>
	 * @param formula
	 * @param offerOrder
	 * @param offerMachine
	 * @return
	 * @throws ScriptException
	 * @since 1.0, 2017年12月1日 上午11:06:49, think
	 */
	private static CustomCal _feeCustomProduct(String formula, OfferOrder offerOrder, OfferMachine offerMachine) throws ScriptException
	{
		formula = ParamsLadderType.forProduct(formula, offerMachine, offerOrder);
		ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
		BigDecimal fee = new BigDecimal(jse.eval(formula).toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		return new CustomCal(scale(fee.doubleValue()), "【自定义公式】", 0);
	}

	/**
	 * <pre>
	 * A元/印张数：印张数*单价
	 * （如果印张数*单价<起步价，那么就是起步价，否则是印张数*单价）
	 * </pre>
	 * @param impositionNum
	 * @param price
	 * @param startPrice
	 * @return
	 * @since 1.0, 2017年11月22日 下午5:13:52, think
	 */
	private static CustomCal _procedureFeeUnit1(Integer impositionNum, BigDecimal price, BigDecimal startPrice)
	{
		StringBuilder formula = new StringBuilder();
		double fee = impositionNum * price.doubleValue();

		formula.append("印张正数").append(impositionNum).append("*");
		formula.append("单价").append(price.doubleValue()).append("=").append(scale(fee));
		if (fee < startPrice.doubleValue())
		{
			fee = startPrice.doubleValue();
			formula.append("<起步价").append(scale(fee));
		}
		return new CustomCal(scale(fee), formula.toString(), 0);
	}

	/**
	* <pre>
	* 元/m2：印张数*上机长/1000*上机宽/1000*单价
	* （如果印张数*（如果上机长/1000*上机宽/1000*单价<每张最低单价，那么每张最低单价，否则是上机长/1000*上机宽/1000*单价）<起步价，
	* 那么起步价，否则是（如果上机长/1000*上机宽/1000*单价<每张最低单价，那么每张最低单价，否则是上机长/1000*上机宽/1000*单价）*印张数）
	* </pre>
	* @param impositionNum
	* @param style
	* @param price
	* @return
	* @since 1.0, 2017年11月9日 下午3:48:20, think
	*/
	private static CustomCal _procedureFeeUnit2(Integer impositionNum, String style, BigDecimal price, BigDecimal startPrice, BigDecimal lowestPrice)
	{
		StringBuilder formula = new StringBuilder();
		String[] styleSplit = style.split("\\*");
		Integer len = Integer.parseInt(styleSplit[0]);
		Integer width = Integer.parseInt(styleSplit[1]);
		formula.append("印张正数").append(impositionNum).append("*");

		// 如果上机长/1000*上机宽/1000*单价<每张最低单价，那么每张最低单价，否则是上机长/1000*上机宽/1000*单价
		double _price = (double) len / 1000 * width / 1000 * price.doubleValue();
		if (_price < lowestPrice.doubleValue())
		{
			_price = lowestPrice.doubleValue();
			formula.append("每张最低单价").append(_price);
		}
		else
		{
			formula.append("上机长").append(len).append("/1000*");
			formula.append("上机宽").append(width).append("/1000*");
			formula.append("单价").append(price.doubleValue());
		}
		// 印张数*（如果上机长/1000*上机宽/1000*单价<每张最低单价，那么每张最低单价，否则是上机长/1000*上机宽/1000*单价）
		double fee = impositionNum * _price;
		formula.append("=").append(scale(fee));
		// <起步价
		if (fee < startPrice.doubleValue())
		{
			fee = startPrice.doubleValue();
			formula.append("<起步价").append(scale(fee));
		}
		return new CustomCal(scale(fee), formula.toString(), 0);
	}

	/**
	 * <pre>
	 * 元/指定m2：印张数*指定长/1000*指定宽/1000*单价
	 * </pre>
	 * @param impositionNum
	 * @param length
	 * @param width
	 * @param price
	 * @return
	 * @since 1.0, 2017年11月22日 下午5:31:36, think
	 */
	private static CustomCal _procedureFeeUnit3(Integer impositionNum, Integer length, Integer width, BigDecimal price, BigDecimal startPrice, BigDecimal lowestPrice)
	{
//		StringBuilder formula = new StringBuilder();
//		double fee = impositionNum * length.doubleValue() / 1000 * width.doubleValue() / 1000 * price.doubleValue();
//		formula.append("印张正数").append(impositionNum).append("*");
//		formula.append("指定长").append(length).append("/1000*");
//		formula.append("指定宽").append(width).append("/1000*");
//		formula.append("单价").append(price.doubleValue());
//		formula.append("=").append(scale(fee));
//		if (fee < startPrice.doubleValue())
//		{
//			fee = startPrice.doubleValue();
//			formula.append("<起步价").append(scale(fee));
//		}
//		return new CustomCal(scale(fee), formula.toString(), 0);
		StringBuilder formula = new StringBuilder();
//		String[] styleSplit = style.split("\\*");
//		Integer len = Integer.parseInt(styleSplit[0]);
//		Integer width = Integer.parseInt(styleSplit[1]);
		formula.append("印张正数").append(impositionNum).append("*");

		// 如果上机长/1000*上机宽/1000*单价<每张最低单价，那么每张最低单价，否则是上机长/1000*上机宽/1000*单价
		double _price = (double) length / 1000 * width / 1000 * price.doubleValue();
		if (_price < lowestPrice.doubleValue())
		{
			_price = lowestPrice.doubleValue();
			formula.append("每张最低单价").append(_price);
		}
		else
		{
			formula.append("指定长").append(length).append("/1000*");
			formula.append("指定宽").append(width).append("/1000*");
			formula.append("单价").append(price.doubleValue());
		}
		double fee = impositionNum * _price;
		formula.append("=").append(scale(fee));
		// <起步价
		if (fee < startPrice.doubleValue())
		{
			fee = startPrice.doubleValue();
			formula.append("<起步价").append(scale(fee));
		}
		return new CustomCal(scale(fee), formula.toString(), 0);
	}

	/**
	 * <pre>
	 * 元/成品数：印刷数量*单价（如果印刷数量*单价<起步价，那么就是起步价，否则是印刷数量*单价）
	 * </pre>
	 * @param amount
	 * @param price
	 * @param startPrice
	 * @return
	 * @since 1.0, 2017年11月22日 下午5:37:30, think
	 */
	private static CustomCal _procedureFeeUnit4(Integer amount, BigDecimal price, BigDecimal startPrice)
	{
		StringBuilder formula = new StringBuilder();
		double fee = amount * price.doubleValue();
		formula.append("印刷数量").append(amount).append("*");
		formula.append("单价").append(price.doubleValue());
		formula.append("=").append(scale(fee));
		if (fee < startPrice.doubleValue())
		{
			fee = startPrice.doubleValue();
			formula.append("<起步价").append(scale(fee));
		}
		return new CustomCal(scale(fee), formula.toString(), 0);
	}

	/**
	 * <pre>
	 * 元/贴/手：印张数*总贴数*单价（如果印张数*总贴数*单价<起步价，那么就是起步价，否则是印张数量**总贴数*单价）封面和内页相加的贴数
	 * </pre>
	 * @param impositionNum
	 * @param threads
	 * @param price
	 * @param startPrice
	 * @return
	 * @since 1.0, 2017年11月22日 下午5:43:56, think
	 */
	private static CustomCal _procedureFeeUnit5(Integer impositionNum, Integer threads, BigDecimal price, BigDecimal startPrice)
	{
		StringBuilder formula = new StringBuilder();
		if (null == threads)
		{
			threads = 0;
		}
		double fee = impositionNum * threads * price.doubleValue();
		formula.append("印张正数").append(impositionNum).append("*");
		formula.append("总贴数").append(threads).append("*");
		formula.append("单价").append(price);
		formula.append("=").append(scale(fee));
		if (fee < startPrice.doubleValue())
		{
			fee = startPrice.doubleValue();
			formula.append("<起步价").append(scale(fee));
		}
		return new CustomCal(scale(fee), formula.toString(), 0);
	}

	/**
	 * <pre>
	 * 元/P：印张数*P数*单价（如果印张数*P数*单价<起步价，那么起步价，否则就是印张数*P数*单价）
	 * </pre>
	 * @param impositionNum
	 * @param p
	 * @param price
	 * @param startPrice
	 * @return
	 * @since 1.0, 2017年11月22日 下午5:45:09, think
	 */
	private static CustomCal _procedureFeeUnit6(Integer impositionNum, Integer p, BigDecimal price, BigDecimal startPrice)
	{
		StringBuilder formula = new StringBuilder();
		double fee = impositionNum * p * price.doubleValue();
		formula.append("印张正数").append(impositionNum).append("*");
		formula.append("P数").append(p).append("*");
		formula.append("单价").append(price);
		formula.append("=").append(scale(fee));
		if (fee < startPrice.doubleValue())
		{
			fee = startPrice.doubleValue();
			formula.append("<起步价").append(scale(fee));
		}
		return new CustomCal(scale(fee), formula.toString(), 0);
	}

	/**
	 * <pre>
	 * 元/本：印刷数量*单价（如果印刷数量*单价<起步价，那么就是起步价，否则是印刷数量*单价）这里的印刷数量是书刊报价和联单报价里的本数的数量
	 * </pre>
	 * @param amount
	 * @param price
	 * @param startPrice
	 * @return
	 * @since 1.0, 2017年11月22日 下午5:48:42, think
	 */
	private static CustomCal _procedureFeeUnit7(Integer amount, BigDecimal price, BigDecimal startPrice)
	{
		return _procedureFeeUnit4(amount, price, startPrice);
	}

	/**
	 * <pre>
	 * 纸张费 - 包装费: 包装价40/包装数400x印品数量5000=500
	 * </pre>
	 * @param amount
	 * @param packing
	 * @param packingPer
	 * @return
	 * @since 1.0, 2017年11月9日 下午5:03:08, think
	 */
	public static double packingFee(Integer amount, BigDecimal packing, Integer packingPer)
	{
		double fee = packing.doubleValue() / packingPer * amount;
		return Double.valueOf(scale(fee));
	}

	/**
	 * <pre>
	 * 利润
	 * </pre>
	 * @param amount
	 * @param profitList
	 * @param costMoney
	 * @return
	 * @since 1.0, 2017年11月9日 下午6:02:43, think
	 */
	public static double profitFee(Integer amount, List<OfferProfit> profitList, double costMoney)
	{
		double profitFee = 0;
		for (OfferProfit offerProfit : profitList)
		{
			Integer rangeStart = offerProfit.getRangeStart();
			Integer rangeEnd = (offerProfit.getRangeEnd() == null ? Integer.MAX_VALUE : offerProfit.getRangeEnd());
			// 按数量
			if (OfferProfitType.NUMBER == offerProfit.getOfferProfitType())
			{
				if (amount > rangeStart && amount <= rangeEnd)
				{
					// 按百分比
					if (offerProfit.getPercent() != null)
					{
						profitFee = costMoney * offerProfit.getPercent() / 100;
						break;
					}
					// 按固定
					else
					{
						profitFee = offerProfit.getMoney();
						break;
					}
				}
			}
			// 按金额
			else
			{
				if (costMoney > rangeStart && costMoney <= rangeEnd)
				{
					// 按百分比
					if (offerProfit.getPercent() != null)
					{
						profitFee = costMoney * offerProfit.getPercent() / 100;
						break;
					}
					// 按固定
					else
					{
						profitFee = offerProfit.getMoney();
						break;
					}
				}
			}
		}

		// 无利润
		return Double.valueOf(scale(profitFee));
	}

	/**
	 * <pre>
	 * 未税金额：成本金额+利润
	 * </pre>
	 * @param costMoney
	 * @param profitFee
	 * @return
	 * @since 1.0, 2017年11月9日 下午6:25:24, think
	 */
	public static double untaxedFee(double costMoney, double profitFee)
	{
		double untaxedFee = costMoney + profitFee;
		return Double.valueOf(scale(untaxedFee));
	}

	/**
	 * <pre>
	 * 未税单价：未税金额/数量
	 * </pre>
	 * @param amount
	 * @param untaxedFee
	 * @return
	 * @since 1.0, 2017年11月9日 下午6:26:58, think
	 */
	public static double untaxedPrice(Integer amount, double untaxedFee)
	{
		double untaxedPrice = untaxedFee / amount.doubleValue();
		return scale(untaxedPrice, 4);
	}

	/**
	 * <pre>
	 * 含税金额：未税金额*（1+税率）
	 * </pre>
	 * @param untaxedFee
	 * @param percent
	 * @return
	 * @since 1.0, 2017年11月9日 下午6:29:07, think
	 */
	public static double taxFee(double untaxedFee, Integer percent)
	{
		double taxFee = untaxedFee * (1 + ((double) percent / 100));
		return Double.valueOf(scale(taxFee));
	}

	/**
	 * <pre>
	 * 含税单价：含税金额/数量
	 * </pre>
	 * @param amount
	 * @param taxFee
	 * @return
	 * @since 1.0, 2017年11月21日 上午10:17:09, think
	 */
	public static double taxPrice(Integer amount, double taxFee)
	{
		return scale(taxFee / amount, 4);
	}

	/**
	 * <pre>
	 * 印版张数
	 * </pre>
	 * @param offerOrder
	 * @param offerPart
	 * @param thread
	 * @return
	 * @since 1.0, 2017年12月15日 下午2:57:07, zhengby
	 */
	public static Integer sheetZQ(OfferOrder offerOrder, OfferPart offerPart)
	{
		Integer proColors = offerPart.getOfferPrintColorType().getValue();
		Integer conColors = null == offerPart.getOfferPrintColorType2() ? 0 : offerPart.getOfferPrintColorType2().getValue();
		Integer proSpots = offerPart.getOfferSpotColorType().getValue();
		Integer conSpots = null == offerPart.getOfferSpotColorType2() ? 0 : offerPart.getOfferSpotColorType2().getValue();
		Integer v = new Integer(0);
		Integer thread = new Integer(1);
		if (OfferType.ALBUMBOOK == offerOrder.getOfferType())
		{
			// 书刊类的贴数
			thread = offerPart.getThread();
			// 自翻和单色=（正面颜色+正面专色）*贴数
			if (OfferPrintStyleType.CHAOS == offerPart.getOfferPrintStyleType() || OfferPrintStyleType.SINGLE == offerPart.getOfferPrintStyleType())
			{
				v = (proColors + proSpots) * thread;
			}
			// 双面和正反（正面颜色+正面专色+反面颜色+反面专色）*贴数
			else
			{
				v = (proColors + proSpots + conColors + conSpots) * thread;
			}
		}
		else
		{
			// 包装类的算法
			if (offerPart.getOfferPrintStyleType() == OfferPrintStyleType.SINGLE)
			{
				// 单面 = 正面普色数+正面专色
				v = proColors + proSpots;
			}
			else
			{
				// 双面 = 正反普色数+正反专色
				v = proColors + proSpots + conColors + conSpots;
			}
		}
		return v;
	}

	/**
	 * <pre>
	 * 材料面积（㎡）
	 * </pre>
	 * @param length
	 * @param width
	 * @return
	 * @since 1.0, 2017年11月29日 下午2:33:50, zhengby
	 */
	public static double materialArea(Integer length, Integer width)
	{
		return Double.valueOf(scale(length.doubleValue() / 1000 * width.doubleValue() / 1000));
	}

	/**
	 * <pre>
	 * 材料数量
	 * </pre>
	 * @param amount
	 * @param sheetNum
	 * @param materialOpening
	 * @param waste
	 * @return
	 * @since 1.0, 2017年11月29日 下午2:48:39, zhengby
	 */
	public static Integer materialAmount(Integer impositionNum, Integer materialOpening, Integer waste)
	{
		return bigPaperNum(impositionNum, waste, materialOpening);
	}

	/**
	 * <pre>
	 * 计价数量
	 * </pre>
	 * @param paperType
	 * @param paperWeight
	 * @param bigPaperNum
	 * @return
	 * @since 1.0, 2017年12月1日 上午8:58:23, zhengby
	 */
	public static BigDecimal calNum(PaperType paperType, Integer paperWeight, Integer bigPaperNum)
	{
		BigDecimal calNum = new BigDecimal(paperType.getLength().doubleValue() / 1000 * paperType.getWidth() / 1000 * paperWeight / 1000 / 1000 * bigPaperNum).setScale(6, RoundingMode.HALF_UP);
		return calNum;
	}
}
