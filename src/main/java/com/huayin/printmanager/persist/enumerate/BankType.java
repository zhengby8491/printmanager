package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 银行列表
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年6月20日
 */
public enum BankType
{
	/** 主流银行 **/
	ICBC("icbc", "工商银行", 1),

	ABC("abc", "农业银行", 2),

	BOC("boc", "中国银行", 3),

	CCB("ccb", "建设银行", 4),

	BCM("bcm", "交通银行", 5),

	CMB("cmb", "招商银行", 6),

	CMBC("cmbc", "民生银行", 7),

	CCITICB("cciticb", "中信银行", 8),

	CIB("cib", "兴业银行", 9),

	CEB("ceb", "光大银行", 10),

	PAB("pab", "平安银行", 11),

	PSBC("psbc", "中国邮政储蓄银行", 12),

	/** 省市级银行 **/
	BOB("bjrcb", "北京银行", 21),

	GZCB("gzcb", "广州银行", 22),

	HZB("hzb", "杭州银行", 23),

	NBCB("nbcb", "宁波银行", 24),

	NJCB("njcb", "南京银行", 25),

	CBB("cbb", "渤海银行", 26),

	GDB("gdb", "广东发展银行", 27),

	SDB("sdb", "深圳发展银行", 28),

	SPDB("spdb", "上海浦东发展银行", 29),

	SRCB("srcb", "上海农村商业银行", 30),

	GZRCC("gdrcc", "广州市农村信用合作社", 31),

	/** 其它银行 **/
	CZB("czb", "浙商银行", 51),

	HSB("hsb", "徽商银行", 52),

	HXB("hxb", "华夏银行", 53),

	BEA("bea", "东亚银行", 54),

	OTHER("OTHER", "其它银行", 99);
	private String text;

	private String value;

	private int sort;

	BankType(String value, String text, int sort)
	{
		this.value = value;
		this.text = text;
		this.sort = sort;
	}

	public String getText()
	{
		return text;
	}

	public String getValue()
	{
		return value;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public void setValue(String value)
	{
		this.text = value;
	}

	public int getSort()
	{
		return sort;
	}

	public void setSort(int sort)
	{
		this.sort = sort;
	}
}
