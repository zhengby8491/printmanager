package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 权限菜单类型
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年6月20日
 */
public enum PermissionType
{
	/**
	 * 菜单
	 */
	MENU("菜单"),
	/**
	 * 功能
	 */
	FUNCTION("功能");
	private String text;

	PermissionType(String text)
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
