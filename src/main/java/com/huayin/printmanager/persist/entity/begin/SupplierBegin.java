/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月8日 上午9:30:23
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
 * 基础设置 - 供应商期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月8日
 * @since        2.0, 2018年1月8日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_supplierBegin")
public class SupplierBegin extends BaseBeginEntity
{

	private static final long serialVersionUID = 1L;

	@Transient
	private List<SupplierBeginDetail> detailList;

	public List<SupplierBeginDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<SupplierBeginDetail> detailList)
	{
		this.detailList = detailList;
	}
}
