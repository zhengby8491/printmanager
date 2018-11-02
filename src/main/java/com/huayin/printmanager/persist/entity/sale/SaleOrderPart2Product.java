/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年02月22日 下午17:53:23
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;

/**
 * <pre>
 * 销售管理 - 销售订单：销售部件产品表(N:N)
 * (单版时：产品1：N部件)
 * (合版时：产品N：1部件)
 * </pre>
 * @author think
 * @version 1.0, 2017年9月18日
 */
@Entity
@Table(name = "sale_order_part2product")
public class SaleOrderPart2Product extends BaseTableIdEntity
{

	private static final long serialVersionUID = 1L;

	// 拼版数 P数 单面P数 上机规格 印刷方式 印刷普色 印刷专色 印张正数 印版张数 放损% 放损数 总印张 备注

	/**
	 * 销售明细ID
	 */
	@Column(length = 50)
	private Long saleDetailId;

	/**
	 * 销售明细部件ID
	 */
	@Column(length = 50)
	private Long salePartId;

	/**
	 * 产品ID
	 */
	private Long productId;

	/**
	 * 成品名称
	 */
	private String productName;

	/**
	 * 生产数量
	 */
	private Integer produceQty;

	/**
	 * （包装：拼版数）/(书刊：单面P数）
	 */
	private Integer pieceNum;

	/**
	 * P数
	 */
	private Integer pageNum;

	public Long getSalePartId()
	{
		return salePartId;
	}

	public void setSalePartId(Long salePartId)
	{
		this.salePartId = salePartId;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public Integer getPieceNum()
	{
		return pieceNum;
	}

	public void setPieceNum(Integer pieceNum)
	{
		this.pieceNum = pieceNum;
	}

	public Integer getPageNum()
	{
		return pageNum;
	}

	public void setPageNum(Integer pageNum)
	{
		this.pageNum = pageNum;
	}

	public Long getSaleDetailId()
	{
		return saleDetailId;
	}

	public void setSaleDetailId(Long saleDetailId)
	{
		this.saleDetailId = saleDetailId;
	}

	public Integer getProduceQty()
	{
		return produceQty;
	}

	public void setProduceQty(Integer produceQty)
	{
		this.produceQty = produceQty;
	}

}
