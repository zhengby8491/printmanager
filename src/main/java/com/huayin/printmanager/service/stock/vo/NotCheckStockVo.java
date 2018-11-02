package com.huayin.printmanager.service.stock.vo;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.huayin.printmanager.persist.enumerate.BillType;

/**
 * <pre>
 * 未审核单据Vo类
 * </pre>
 * @author raintear
 * @version 1.0, 2016年8月5日
 */
public class NotCheckStockVo
{
	/**
	 * 单据Id
	 */
	private Long Id;
	
	/**
	 * 单据编号
	 */
	private String billNo;
	
	/**
	 * 单据类型
	 */
	@Enumerated(EnumType.STRING)
	private BillType billType;

	public Long getId()
	{
		return Id;
	}

	public void setId(Long id)
	{
		Id = id;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public BillType getBillType()
	{
		return billType;
	}

	public void setBillType(BillType billType)
	{
		this.billType = billType;
	}
	
	public String getBillTypeText(){
		if (billType!=null)
		{
			return billType.getText();
		}
		return "-";
	}
	
}
