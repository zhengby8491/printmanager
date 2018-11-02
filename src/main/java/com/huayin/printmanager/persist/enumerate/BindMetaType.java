package com.huayin.printmanager.persist.enumerate;

 /**
  * 
  * <pre>
  * TODO  报价绑定数据类型
  * </pre>
  * @author houmaolong
  * @version 1.0, 2016年12月28日
  */
public enum BindMetaType
{

	/**
	 * 工序分类
	 */
	PTY("工序分类"),
	/**
	 * 材料分类
	 */
	MTY("材料分类");
	 
	private String text;

	BindMetaType(String text)
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
