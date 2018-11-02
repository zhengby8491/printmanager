/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月5日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.begin;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 基础设置 - 客户期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月5日
 * @since        2.0, 2018年1月5日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_customerBegin")
public class CustomerBegin extends BaseBeginEntity
{
	private static final long serialVersionUID = 1L;

	@Transient
	private List<CustomerBeginDetail> detailList;

	public List<CustomerBeginDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<CustomerBeginDetail> detailList)
	{
		this.detailList = detailList;
	}
}
