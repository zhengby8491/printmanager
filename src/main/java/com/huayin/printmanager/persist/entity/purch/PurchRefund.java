/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.purch;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;

/**
 * <pre>
 * 采购管理 - 采购退货：采购退货表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年5月24日 下午3:28:39, zhong
 * @version 	   2.0, 2018年2月23日上午11:37:34, zhengby, 代码规范
 */
@Entity
@Table(name = "purch_refund")
public class PurchRefund extends PurchBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 退货方式
	 */
	private ReturnGoodsType returnGoodsType;

	/**
	 * 明细表
	 */
	@Transient
	List<PurchRefundDetail> detailList=new ArrayList<PurchRefundDetail>();

	public ReturnGoodsType getReturnGoodsType()
	{
		return returnGoodsType;
	}
	public void setReturnGoodsType(ReturnGoodsType returnGoodsType)
	{
		this.returnGoodsType = returnGoodsType;
	}
	public List<PurchRefundDetail> getDetailList()
	{
		return detailList;
	}
	public void setDetailList(List<PurchRefundDetail> detailList)
	{
		this.detailList = detailList;
	}

	public String getReturnGoodsTypeText(){
		if(returnGoodsType!=null){
			return returnGoodsType.getText();
		}
		return "-";
	}
}