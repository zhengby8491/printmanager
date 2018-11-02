package com.huayin.printmanager.persist.enumerate;

/**
 * 报价类型
 * @author houmaolong
 *
 */
public enum OfferType
{
	SINGLE("单张类", "turn_single", "fa-file-text-o", "single"),

	ALBUMBOOK("画册书刊", "turn_book", "fa-book", "book"),

	CARTONBOX("彩盒纸箱", "turn_carton", "fa-inbox", "carton"),

	NOTESLETTERFORM("便签信纸", "turn_note", "fa-hdd-o", "note"),

	TAGCARD("吊牌卡片", "turn_card", "fa-tags", "card"),

	ENVELOPETYPE("封套类", "turn_sheath", "fa-archive", "sheath"),

	PRESSURESENSITIVEADHSIVE("不干胶", "turn_glue", "fa-building-o", "glue"),

	ASSOCIATEDSINGLECLASS("联单类", "turn_bill", "fa-copy", "bill"),

	MAILERTYPE("信封类", "turn_envelope", "fa-send", "envelope"),

	CUP("纸杯类", "turn_pagerCup", "fa-glass", "paperCup");

	private String text;

	private String id;

	private String icon;

	private String mapping;

	OfferType(String text, String id, String icon, String mapping)
	{
		this.text = text;
		this.id = id;
		this.icon = icon;
		this.mapping = mapping;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getMapping()
	{
		return mapping;
	}

	public void setMapping(String mapping)
	{
		this.mapping = mapping;
	}

}
