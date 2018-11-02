package com.huayin.printmanager.persist.enumerate;

public enum OrderState
{
	CANCELED("已取消", 0),
	WAT_PAY("待支付", 1), 
	COMPLETED("已完成",2);
	
	public String name;
	public int id;
	
	private OrderState(String name,int id){
		this.name = name;
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	

}
