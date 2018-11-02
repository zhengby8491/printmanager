/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.helper.service;

import java.math.BigDecimal;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.huayin.printmanager.helper.AbsHelper;
import com.huayin.printmanager.persist.entity.basic.UnitConvert;

/**
 * <pre>
 * 公共 - 常用业务功能
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class CommonHelper extends AbsHelper
{
	/**
	 * <pre>
	 * 单位换算
	 * </pre>
	 * @param specifications
	 * @param purchUnitId
	 * @param valuationUnitId
	 * @param ValuationUnitAccuracy
	 * @param weight
	 * @param qty
	 * @return
	 * @throws ScriptException
	 * @since 1.0, 2018年2月27日 上午11:44:13, zhengby
	 */
	public static BigDecimal getValuationQty(String specifications, Long purchUnitId, Long valuationUnitId, Integer ValuationUnitAccuracy, Integer weight, BigDecimal qty) throws ScriptException
	{
		String[] spec = specifications.split("\\*");
		BigDecimal length = new BigDecimal(0);
		BigDecimal width = new BigDecimal(0);
		BigDecimal height = new BigDecimal(0);
		if (spec.length == 1)
		{
			length = new BigDecimal(spec[0]);
			width = new BigDecimal(spec[0]);
			height = new BigDecimal(spec[0]);
		} else if (spec.length == 2)
		{
			length = new BigDecimal(spec[0]);
			width = new BigDecimal(spec[1]);
			height = new BigDecimal(0);
		} else if (spec.length == 3)
		{
			length = new BigDecimal(spec[0]);
			width = new BigDecimal(spec[1]);
			height = new BigDecimal(spec[2]);
		}
		
		BigDecimal valuationQty = new BigDecimal(0);
		UnitConvert obj = serviceFactory.getUnitConvertService().getByUnit(purchUnitId, valuationUnitId);
		// TODO 系统预定的基础单位需加字段标识
		if (obj.getConversionUnitName().equals("千平方英寸"))
		{
			double baseQty = length.divide(BigDecimal.valueOf(25.4)).doubleValue();
			if (baseQty <= 14.5)
			{
				baseQty = 14.5;
			}
			else if (baseQty > 14.5 && baseQty <= 27.5)
			{
				baseQty = baseQty * 2;
				baseQty = ((baseQty) % 2 == 0) ? (baseQty + 1) / (2) : baseQty / (2);
			}
			else if (baseQty > 27.5 && baseQty <= 55)
			{
				baseQty = ((baseQty) % 2 == 0) ? (baseQty + 1) : baseQty;
			}
			valuationQty = new BigDecimal(baseQty).multiply(width).divide(new BigDecimal(25.4)).multiply(qty).divide(new BigDecimal(1000));
		}
		else
		{
			String formula = obj.getFormula();

			formula = formula.replaceAll("length", length.toString());
			formula = formula.replaceAll("width", width.toString());
			formula = formula.replaceAll("height", height.toString());
			formula = formula.replaceAll("weight", weight.toString());
			formula = formula.replaceAll("qty", qty.toString());

			ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
			valuationQty = new BigDecimal(jse.eval(formula).toString()).setScale(ValuationUnitAccuracy, BigDecimal.ROUND_HALF_UP);
		}
		return valuationQty;
	}

}
