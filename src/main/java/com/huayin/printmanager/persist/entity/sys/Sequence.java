/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.printmanager.persist.enumerate.ResetCycle;

/**
 * <pre>
 * 系统模块 - 交易流水号
 * </pre>
 * <code>
 * long startSequence = SequenceServiceImpl.getInstance().getSequence("TestSequence", 10);
 * </code>
 * @author       think
 * @since        1.0, 2017年10月25日
 */
@Entity
@Table(name = "sys_sequence")
public class Sequence extends AbstractEntity
{
	private static final long serialVersionUID = -350096206545947549L;

	/**
	 * 缓存的流水号个数，即每次从持久层获取的流水号个数
	 */
	private java.lang.Integer cacheCount;

	/**
	 * 当前的流水号计数
	 */
	@Column(nullable = false)
	private java.lang.Long indexNum;

	/**
	 * 流水号最大允许的计数，达到改数后从1开始循环
	 */
	private java.lang.Long modMaxNum;

	/**
	 * 流水号名称
	 */
	@Id
	@Column(length = 100)
	private java.lang.String name;

	/**
	 * 复位周期
	 */
	@Column(length = 20)
	@Enumerated(javax.persistence.EnumType.STRING)
	ResetCycle cycle;

	/**
	 * 当前日期节点
	 */
	private String currentDateNode;

	/**
	 * @return 缓存的流水号个数，即每次从持久层获取的流水号个数
	 */
	public java.lang.Integer getCacheCount()
	{
		return cacheCount;
	}

	@Override
	public Object getId()
	{
		return null;
	}

	/**
	 * @return 当前的流水号计数
	 */
	public java.lang.Long getIndexNum()
	{
		return indexNum;
	}

	/**
	 * @return 流水号最大允许的计数，达到改数后从1开始循环
	 */
	public java.lang.Long getModMaxNum()
	{
		return modMaxNum;
	}

	/**
	 * @return 流水号名称
	 */
	public java.lang.String getName()
	{
		return name;
	}

	/**
	 * @param cacheCount 缓存的流水号个数，即每次从持久层获取的流水号个数
	 */
	public void setCacheCount(java.lang.Integer cacheCount)
	{
		this.cacheCount = cacheCount;
	}

	/**
	 * @param indexNum 当前的流水号计数
	 */
	public void setIndexNum(java.lang.Long indexNum)
	{
		this.indexNum = indexNum;
	}

	/**
	 * @param modMaxNum 流水号最大允许的计数，达到改数后从1开始循环
	 */
	public void setModMaxNum(java.lang.Long modMaxNum)
	{
		this.modMaxNum = modMaxNum;
	}

	/**
	 * @param name 流水号名称
	 */
	public void setName(java.lang.String name)
	{
		this.name = name;
	}

	public ResetCycle getCycle()
	{
		return cycle;
	}

	public void setCycle(ResetCycle cycle)
	{
		this.cycle = cycle;
	}

	public String getCurrentDateNode()
	{
		return currentDateNode;
	}

	public void setCurrentDateNode(String currentDateNode)
	{
		this.currentDateNode = currentDateNode;
	}

}