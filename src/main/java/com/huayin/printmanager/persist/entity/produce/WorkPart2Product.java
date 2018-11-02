/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.produce;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;

/**
 * <pre>
 * 生产管理 - 生产工单：工单部件产品表(N:N)
 * (单版时：产品1：N部件)
 * (合版时：产品N：1部件)
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年10月26日，zhaojt
 * @version 	   2.0, 2018年2月23日上午10:20:08, zhengby, 代码规范
 */
@Entity
@Table(name = "produce_work_part2product")
public class WorkPart2Product extends BaseTableIdEntity
{

	private static final long serialVersionUID = 1L;
	
	//拼版数	P数	单面P数	上机规格	印刷方式	印刷普色	印刷专色	印张正数	印版张数	放损%	放损数	总印张	备注
	
	/**
	 * 工单ID
	 */
	@Column(length = 50)
	private Long workId;
	/**
	 * 工单部件ID
	 */
	@Column(length = 50)
	private Long workPartId;

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

	public Long getWorkPartId()
	{
		return workPartId;
	}
	public void setWorkPartId(Long workPartId)
	{
		this.workPartId = workPartId;
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
	}	public void setProductName(String productName)
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
	public Long getWorkId()
	{
		return workId;
	}
	public void setWorkId(Long workId)
	{
		this.workId = workId;
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
