package com.huayin.printmanager.persist.enumerate;
/**
 * 
 * <pre>
 * TODO 自定义模板类型
 * </pre>
 * @author houmaolong
 * @version 1.0, 2017年1月4日
 */
public enum PrintModleName
{
	DEFAULT("--请选择--"),
	SALE_SO("销售订单"),
	SALE_IV("销售送货"),
	SALE_IR("销售退货"),
	SALE_SK("销售对账"),
	PRODUCE_MO("生产工单"),
	OUTSOURCE_OP("发外订单"),
	OUTSOURCE_OA("发外到货"),
	OUTSOURCE_OR("发外退货"),
	PURCH_PO("采购订单"),
	PURCH_PN("采购入库"),
	PURCH_PR("采购退货"),
	PURCH_PK("采购对账"),
	OUTSOURCE_OC("发外对账"),
	STOCK_MR("生产领料单"),
	STOCK_SPIN("成品入库"),
	//二期
	STOCK_SM("生产补料"),
	STOCK_RM("生产退料"),
	STOCK_SMOI("材料其它入库"),
	STOCK_SMOO("材料其它出库"),
	STOCK_SMA("材料库存调整"),
	STOCK_SMT("材料库存调拨"),
	STOCK_SMI("材料库存盘点"),
	STOCK_SPOI("成品其它入库"),
	STOCK_SPOO("成品其它出库"),
	STOCK_SPA("成品库存调整"),
	STOCK_SPT("成品库存调拨"),
	STOCK_SPI("成品库存盘点"),
	STOCK_SLT("材料分切"),
	//三期
	FINALCE_FK("付款单"),
	FINALCE_SK("收款单"),
	FINALCE_FKHX("付款核销单"),
	FINALCE_SKHX("收款核销单"),
	FINALCE_OFK("其他付款单"),
	FINALCE_OSK("其他收款单"),
	//四期
	OEM_EO("代工订单"),
	OEM_ED("代工送货单"),
	OEM_ER("代工退货单"),
	OEM_EC("代工对账单"),
	;
	private String text;

	PrintModleName(String text)
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
