/**
 * <pre>
 * Author:		THINK
 * Create:	 	2017年11月1日 上午10:13:58
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.huayin.printmanager.helper.service.OfferFormulaHelper;
import com.huayin.printmanager.persist.entity.offer.OfferMachine;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.persist.entity.offer.OfferPart;
import com.huayin.printmanager.persist.entity.offer.OfferPartProcedure;

/**
 * <pre>
 * 阶梯报价数量参数
 * </pre>
 * @author       think
 * @since        1.0, 2017年11月20日
 */
public enum ParamsLadderType
{
	CPSL("产品数", "amount"), // 印刷数量
	YZZS("印张正数", "impositionNum"), // 印张正数
	SSS("损耗数", "waste"), // 损耗数
	ZYZS("总印张", "impositionNums"), // 印张正数+损耗数
	BJSL("部件数量", "partNumber"), // 印刷数量(便签：部件数量＝印刷数量（本）*页、联单：部件数量＝印刷数量（本）*页*联数)
	YSMS("印刷面数", "offerPrintStyleType"), // 单面印刷＝1,双面印刷=2
	MTZS("每贴正数", "stickUnitNumber"), // 书刊每贴印张正数
	YZMJ("印张面积", "impositionNumArea"), // 上机规格面积＝上机长/1000*上机宽/1000
	ZDMJ("指定面积", "customNumArea"), // 工序指定规格面积＝指定长/1000*指定宽/1000
	PBS("拼版数", "sheetNum"), // 计算出来拼版数
	SSZM("正面色数", "headColor"), // 普通颜色数 正面色数
	SSFM("反面色数", "tailColor"), // 普通颜色数 反面色数
	SS("色数", "colors"), // 普通颜色数,正面色数+反面色数
	ZSZM("正面专色", "headSpotColor"), // 专色数 正面专色
	ZSFM("反面专色", "tailSpotColor"), // 专色数 反面专色
	ZS("专色", "spotColor"), // 专色数,正面专色+反面专色
	YBZS("印版张数", "sheetZQ"), // 正面色数+反面色数+正面专色+反面专色（书刊的印版张数＝（正面色数+反面色数+正面专色+反面专色）*贴数）
	YBFS("印版付数", "sheetFQ"), // 印版付数＝印刷面数，单面印刷＝1,双面印刷＝2（书刊的印版付数＝印刷面数*贴数，单面印刷＝1*贴数，双面印刷＝贴数*2）
	PS("P数", "pages"), // 每个部件各自的p数
	TS("贴数", "ticks"), // 每个部件各自的贴数
	ZPS("总P数", "tpnumber"), // 总p数＝书刊每个部件P数之和
	ZTS("总贴数", "tstickNumber"), // 总贴数＝每个部件贴数之和
	CONSTANT("1", ""), // 就是1，不用计算
	CLKS("材料开数", "materialOpening"),; // 计算出来的开数

	private String text;

	private String fieldName;

	ParamsLadderType(String text, String fieldName)
	{
		this.text = text;
		this.fieldName = fieldName;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	/**
	 * <pre>
	 * 部件工序-自定义公式
	 * </pre>
	 * @param formula
	 * @param offerMachine
	 * @param offerPart
	 * @param offerOrder
	 * @param offerPartProcedure
	 * @return
	 * @since 1.0, 2017年11月25日 下午3:31:50, think
	 */
	public static String forPart(String formula, OfferMachine offerMachine, OfferPart offerPart, OfferOrder offerOrder, OfferPartProcedure offerPartProcedure)
	{
		// 贴数
		Integer thread = offerMachine.getThread();
		if (OfferType.ALBUMBOOK == offerOrder.getOfferType())
		{
			// 画册书、彩盒刊要独立计算
			thread = OfferFormulaHelper.thread(offerPart.getPages(), offerMachine.getSheetNum(), offerPart.getOfferPrintStyleType());
		}
		// 产品数＝印刷数量（报价页面）
		formula = formula.replaceAll(ParamsLadderType.CPSL.getText(), String.valueOf(offerOrder.getAmount()));
		// 印张正数
		formula = formula.replaceAll(ParamsLadderType.YZZS.getText(), String.valueOf(offerMachine.getImpositionNum()));
		// 损耗数
		Integer waste = OfferFormulaHelper.waste(offerOrder, offerMachine, offerPart);
		formula = formula.replaceAll(ParamsLadderType.SSS.getText(), String.valueOf(waste));
		// 总印张=印张正数+损耗数
		formula = formula.replaceAll(ParamsLadderType.ZYZS.getText(), String.valueOf(offerMachine.getImpositionNum() + waste));
		// 便签：部件数量＝印刷数量（本）*页
		if (OfferType.NOTESLETTERFORM == offerOrder.getOfferType())
		{
			int v = offerOrder.getAmount() * offerOrder.getPageType();
			formula = formula.replaceAll(ParamsLadderType.BJSL.getText(), String.valueOf(v));
		}
		// 联单：部件数量＝印刷数量（本）*页*联数)
		else if (OfferType.ASSOCIATEDSINGLECLASS == offerOrder.getOfferType())
		{
			int v = offerOrder.getAmount() * offerOrder.getPageType() * offerOrder.getSheetType().getValue();
			formula = formula.replaceAll(ParamsLadderType.BJSL.getText(), String.valueOf(v));
		}
		// 部件数量＝印刷数量
		else
		{
			formula = formula.replaceAll(ParamsLadderType.BJSL.getText(), String.valueOf(offerOrder.getAmount()));
		}
		// 印刷面数(单面印刷＝1,双面印刷、正反、自翻=2)
		int ysms = 2;
		if (offerPart.getOfferPrintStyleType() == OfferPrintStyleType.SINGLE)
		{
			ysms = 1;
		}
		formula = formula.replaceAll(ParamsLadderType.YSMS.getText(), String.valueOf(ysms));
		// 印张面积＝上机规格面积＝上机长/1000*上机宽/1000
		String[] styleSplit = offerMachine.getStyle().split("\\*");
		Integer len = Integer.parseInt(styleSplit[0]);
		Integer width = Integer.parseInt(styleSplit[1]);
//		double v = len / 1000d * width / 1000d;
		double v = new BigDecimal(len / 1000d * width / 1000d).setScale(6, RoundingMode.HALF_UP).doubleValue();
		formula = formula.replaceAll(ParamsLadderType.YZMJ.getText(), String.valueOf(v));
		// 指定面积＝工序指定规格面积＝指定长/1000*指定宽/1000
		if (null != offerPartProcedure)
		{
			len = offerPartProcedure.getLength();
			width = offerPartProcedure.getWidth();
			if (null != len)
			{
				v = new BigDecimal(len / 1000d * width / 1000d).setScale(2, RoundingMode.HALF_UP).doubleValue();
				formula = formula.replaceAll(ParamsLadderType.ZDMJ.getText(), String.valueOf(v));
			}
		}
		// 拼版数=计算出来拼版数
		formula = formula.replaceAll(ParamsLadderType.PBS.getText(), String.valueOf(offerMachine.getSheetNum()));

		// ===============判断颜色（这里比较绕）===============

		OfferPrintStyleType offerPrintStyleType = offerPart.getOfferPrintStyleType();
		// 正面色数＝正面色数
		int headColor = offerPart.getOfferPrintColorType().getValue();
		formula = formula.replaceAll(ParamsLadderType.SSZM.getText(), String.valueOf(headColor));
		/**
		 * 反面色数
		 * 【单面】            反面色数=0
		 * 【双面|正反】  反面色数=页面提交的
		 * 【自翻】            反面色数=正面色数
		 */
		int tailColor = 0;
		if (OfferPrintStyleType.SINGLE == offerPrintStyleType)
		{
			// 【单面】反面色数=0
			tailColor = 0;
		}
		else if (OfferPrintStyleType.DOUBLE == offerPrintStyleType || OfferPrintStyleType.HEADTAIL == offerPrintStyleType)
		{
			// 【双面|正反】反面色数=页面提交的
			tailColor = offerPart.getOfferPrintColorType2().getValue();
		}
		if (OfferPrintStyleType.CHAOS == offerPrintStyleType)
		{
			// 【自翻】反面色数=正面色数
			tailColor = headColor;
		}
		formula = formula.replaceAll(ParamsLadderType.SSFM.getText(), String.valueOf(tailColor));
		/**
		 * 色数＝普通颜色数,正面色数+反面色数（单面）
		 * 【单面|双面|正反|自翻】  色数=正面色数+反面色数
		 */
		int colors = headColor + tailColor;
		formula = formula.replaceAll(ParamsLadderType.SS.getText(), String.valueOf(colors));
		// 正面专色＝正面专色
		int headSpotColor = offerPart.getOfferSpotColorType().getValue();
		formula = formula.replaceAll(ParamsLadderType.ZSZM.getText(), String.valueOf(headSpotColor));
		/**
		 * 反面专色＝反面专色
		 * 【单面】            反面专色=0
		 * 【双面|正反】  反面专色=页面提交的
		 * 【自翻】            反面专色=正面专色
		 */
		int tailSpotColor = 0;
		if (OfferPrintStyleType.SINGLE == offerPrintStyleType)
		{
			// 【单面】反面专色=0
			tailSpotColor = 0;
		}
		if (OfferPrintStyleType.DOUBLE == offerPrintStyleType || OfferPrintStyleType.HEADTAIL == offerPrintStyleType)
		{
			// 【双面|正反】  反面专色=页面提交的
			tailSpotColor = offerPart.getOfferSpotColorType2().getValue();
		}
		if (OfferPrintStyleType.CHAOS == offerPrintStyleType)
		{
			// 【自翻】            反面专色=正面专色
			tailSpotColor = headSpotColor;
		}
		formula = formula.replaceAll(ParamsLadderType.ZSFM.getText(), String.valueOf(tailSpotColor));
		// 专色＝专色数,正面专色+反面专色
		/**
		 * 专色＝正面专色+反面专色
		 * 【单面|双面|正反|自翻】  专色=正面专色+反面专色
		 */
		int spotColors = headSpotColor + tailSpotColor;
		formula = formula.replaceAll(ParamsLadderType.ZS.getText(), String.valueOf(spotColors));
		// 书刊的印版张数＝（正面色数+反面色数+正面专色+反面专色）*贴数）
		if (OfferType.ALBUMBOOK == offerOrder.getOfferType())
		{
			// 自翻和单色=（正面颜色+正面专色）*贴数
			if (OfferPrintStyleType.CHAOS == offerPrintStyleType || OfferPrintStyleType.SINGLE == offerPrintStyleType)
			{
				v = (headColor + headSpotColor) * thread;
			}
			// 双面和正反（正面颜色+正面专色+反面颜色+反面专色）*贴数
			else
			{
				v = (colors + spotColors) * thread;
			}
			formula = formula.replaceAll(ParamsLadderType.YBZS.getText(), String.valueOf(v));
		}
		// 印版张数＝正面色数+反面色数+正面专色+反面专色
		else
		{
			v = colors + spotColors;
			formula = formula.replaceAll(ParamsLadderType.YBZS.getText(), String.valueOf(v));
		}

		// 书刊的印版付数＝印刷面数*贴数，单面印刷＝1*贴数，双面印刷＝贴数*2
		if (OfferType.ALBUMBOOK == offerOrder.getOfferType())
		{
			v = offerPart.getOfferPrintStyleType().getValue() * thread;
			formula = formula.replaceAll(ParamsLadderType.YBFS.getText(), String.valueOf(v));
		}
		// 印版付数＝印刷面数，单面印刷＝1,双面印刷＝2
		else
		{
			v = offerPart.getOfferPrintStyleType().getValue();
			formula = formula.replaceAll(ParamsLadderType.YBFS.getText(), String.valueOf(v));
		}

		// 总P数＝书刊每个部件P数之和
		formula = formula.replaceAll(ParamsLadderType.ZPS.getText(), String.valueOf(offerMachine.getTpnumber()));
		// 总贴数＝每个部件贴数之和（必须放在贴数之前）
		formula = formula.replaceAll(ParamsLadderType.ZTS.getText(), String.valueOf(offerMachine.getThreads()));
		// 每贴正数=书刊每贴印张正数
		if (offerMachine.getThread() == 0)
		{
			formula = formula.replaceAll(ParamsLadderType.MTZS.getText(), String.valueOf(0));
		}else{
			formula = formula.replaceAll(ParamsLadderType.MTZS.getText(), String.valueOf(Math.ceil(offerMachine.getImpositionNum().doubleValue() / offerMachine.getThread())));
		}
		// P数＝每个部件各自的p数
		formula = formula.replaceAll(ParamsLadderType.PS.getText(), String.valueOf(offerPart.getPages()));
		// 贴数＝每个部件各自的贴数
		formula = formula.replaceAll(ParamsLadderType.TS.getText(), String.valueOf(thread));
	
		// 材料开数＝计算出来的开数
		formula = formula.replaceAll(ParamsLadderType.CLKS.getText(), String.valueOf(offerMachine.getMaterialOpening()));

		return formula.replaceAll("\\[", "").replaceAll("\\]", "");
	}

	/**
	 * <pre>
	 * 成品工序-自定义公式
	 * </pre>
	 * @param formula
	 * @param offerMachine
	 * @param offerOrder
	 * @return
	 * @since 1.0, 2017年12月1日 上午11:03:38, think
	 */
	public static String forProduct(String formula, OfferMachine offerMachine, OfferOrder offerOrder)
	{
		// 产品数＝印刷数量（报价页面）
		formula = formula.replaceAll(ParamsLadderType.CPSL.getText(), String.valueOf(offerOrder.getAmount()));
		// 部件数量＝印刷数量
		formula = formula.replaceAll(ParamsLadderType.BJSL.getText(), String.valueOf(offerOrder.getAmount()));
		// 总P数＝书刊每个部件P数之和
		formula = formula.replaceAll(ParamsLadderType.ZPS.getText(), String.valueOf(offerMachine.getTpnumber()));
		// 总贴数＝每个部件贴数之和
		formula = formula.replaceAll(ParamsLadderType.ZTS.getText(), String.valueOf(offerMachine.getThreads()));

		return formula.replaceAll("\\[", "").replaceAll("\\]", "");
	}
}
