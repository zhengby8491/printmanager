package com.huayin.printmanager.persist.enumerate;

import com.huayin.common.util.Reflections;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;

/**
 * 平方报价面积参数
 * @author houmaolong
 */
public enum ParamsSquareAType
{
	YZMJ("印张面积", "sheetArea"), 
	ZDMJ("指定面积", "procedureSize");

	private String text;

	private String fieldName;

	ParamsSquareAType(String text, String fieldName)
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
	 * 参数替换
	 * </pre>
	 * @param formula 公式
	 * @param offerBean 数据实体
	 * @return
	 */
	public String forPack(String formula, OfferOrder offerBean)
	{
		if (formula.indexOf(this.getText()) > -1 && this != ParamsSquareAType.ZDMJ && !"".equals(this.getFieldName()))
		{
			Object value = Reflections.getFieldValue(offerBean, this.getFieldName());
			if (value != null)
			{
				formula = formula.replaceAll(this.getText(), value.toString());
			}

		}
		return formula;
	}
}
