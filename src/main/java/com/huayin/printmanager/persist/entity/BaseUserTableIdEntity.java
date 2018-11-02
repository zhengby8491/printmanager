/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月5日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * <pre>
 * 框架 - 用户单据基类
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月5日
 * @since        2.0, 2018年1月5日 下午17:07:00, think, 规范和国际化
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseUserTableIdEntity extends BaseTableIdEntity
{
	private static final long serialVersionUID = -9064495946832615405L;

	/**
	 * 用户编号 生成规则：公司编号+5位自增流水号： 如果公司编号为 110,当前公司对应的当前已生成用户编号的序列最大值为200 那么投注卡号为：11000201
	 */
	@Column(length = 50)
	private String userNo;

	public String getUserNo()
	{
		return userNo;
	}

	public void setUserNo(String userNo)
	{
		this.userNo = userNo;
	}
}