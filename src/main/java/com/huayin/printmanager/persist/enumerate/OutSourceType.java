package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 外发类型
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年6月20日
 */
public enum OutSourceType
{

	PRODUCT("整单发外"),

	PROCESS("工序发外");

	private String text;

	OutSourceType(String text)
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
	public static String get(String key){
		for(OutSourceType o:OutSourceType.values()){
			if(o.name().equals(key)){
				return o.getText();
			}
		}
		return "";
	}
}
