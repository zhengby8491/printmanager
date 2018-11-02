package com.huayin.printmanager.persist.entity;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * <pre>
 * 单据主表基类
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-1-29
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseBillDetailTableEntity extends BaseBillTableEntity
{
	private static final long serialVersionUID = -9064495946832615405L;
	/**
	 * 主表ID
	 */
	private Long masterId;

	public Long getMasterId()
	{
		return masterId;
	}
	public void setMasterId(Long masterId)
	{
		this.masterId = masterId;
	}
}