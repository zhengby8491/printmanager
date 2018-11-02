/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.vo;

import java.math.BigDecimal;

import com.huayin.printmanager.persist.enumerate.OutSourceType;

/**
 * <pre>
 * 年汇总VO
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年8月5日, raintear
 * @version 	   2.0, 2018年2月26日上午9:34:20, zhengby, 代码规范
 */
public class SumVo
{

	/**
	 * 供应商/材料/采购员/工序/加工商/发外类型/产品/客户/销售员名称 
	 */
	private Long id;

	private String name;

	/**
	 * 供应商/材料/采购员/工序/加工商/发外类型/产品/客户/销售员分类名
	 */
	private String className;

	/**
	 * 规格
	 */
	private String style;

	/**
	 * 总金额
	 */
	private BigDecimal sumMoney;

	// 一到十二月
	private BigDecimal january;

	private BigDecimal february;

	private BigDecimal march;

	private BigDecimal april;

	private BigDecimal may;

	private BigDecimal june;

	private BigDecimal july;

	private BigDecimal august;

	private BigDecimal september;

	private BigDecimal october;

	private BigDecimal november;

	private BigDecimal december;

	public SumVo()
	{
		super();
	}

	public SumVo(Long id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}

	public SumVo(Long id, String name, String className, String style, BigDecimal sumMoney, BigDecimal january, BigDecimal february, BigDecimal march, BigDecimal april, BigDecimal may, BigDecimal june, BigDecimal july, BigDecimal august, BigDecimal september, BigDecimal october, BigDecimal november, BigDecimal december)
	{
		super();
		this.id = id;
		this.name = name;
		this.className = className;
		this.style = style;
		this.sumMoney = sumMoney;
		this.january = january;
		this.february = february;
		this.march = march;
		this.april = april;
		this.may = may;
		this.june = june;
		this.july = july;
		this.august = august;
		this.september = september;
		this.october = october;
		this.november = november;
		this.december = december;
	}

	public SumVo(Long id, String name, BigDecimal sumMoney, BigDecimal january, BigDecimal february, BigDecimal march, BigDecimal april, BigDecimal may, BigDecimal june, BigDecimal july, BigDecimal august, BigDecimal september, BigDecimal october, BigDecimal november, BigDecimal december)
	{
		super();
		this.id = id;
		this.name = name;
		this.sumMoney = sumMoney;
		this.january = january;
		this.february = february;
		this.march = march;
		this.april = april;
		this.may = may;
		this.june = june;
		this.july = july;
		this.august = august;
		this.september = september;
		this.october = october;
		this.november = november;
		this.december = december;
	}

	public String getName()
	{
		return name;
	}

	public String getOutSourceTypeText()
	{
		if (name != null)
		{
			return OutSourceType.get(name);
		}
		return "-";
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public BigDecimal getSumMoney()
	{
		return sumMoney;
	}

	public void setSumMoney(BigDecimal sumMoney)
	{
		this.sumMoney = sumMoney;
	}

	public BigDecimal getJanuary()
	{
		return january;
	}

	public void setJanuary(BigDecimal january)
	{
		this.january = january;
	}

	public BigDecimal getFebruary()
	{
		return february;
	}

	public void setFebruary(BigDecimal february)
	{
		this.february = february;
	}

	public BigDecimal getMarch()
	{
		return march;
	}

	public void setMarch(BigDecimal march)
	{
		this.march = march;
	}

	public BigDecimal getApril()
	{
		return april;
	}

	public void setApril(BigDecimal april)
	{
		this.april = april;
	}

	public BigDecimal getMay()
	{
		return may;
	}

	public void setMay(BigDecimal may)
	{
		this.may = may;
	}

	public BigDecimal getJune()
	{
		return june;
	}

	public void setJune(BigDecimal june)
	{
		this.june = june;
	}

	public BigDecimal getJuly()
	{
		return july;
	}

	public void setJuly(BigDecimal july)
	{
		this.july = july;
	}

	public BigDecimal getAugust()
	{
		return august;
	}

	public void setAugust(BigDecimal august)
	{
		this.august = august;
	}

	public BigDecimal getSeptember()
	{
		return september;
	}

	public void setSeptember(BigDecimal september)
	{
		this.september = september;
	}

	public BigDecimal getOctober()
	{
		return october;
	}

	public void setOctober(BigDecimal october)
	{
		this.october = october;
	}

	public BigDecimal getNovember()
	{
		return november;
	}

	public void setNovember(BigDecimal november)
	{
		this.november = november;
	}

	public BigDecimal getDecember()
	{
		return december;
	}

	public void setDecember(BigDecimal december)
	{
		this.december = december;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

}
