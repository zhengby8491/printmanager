package com.huayin.printmanager.persist.enumerate;

/**
 * 盘点类型
 * 根据盘点数量-库存数量盘点是盘盈还是盘亏，
 * 如果盘点数量<库存数量，则显示‘盘亏’，如果盘点数量>库存数量，则显示‘盘盈’
 * @ClassName: ReturnType
 * @author zhaojt
 * @date 2016年6月22日 上午9:59:18
 */
public enum InventoryType
{

	PROFIT("盘盈"),

	LOSS("盘亏");

	private String text;

	InventoryType(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
}
