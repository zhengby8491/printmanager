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

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;

/**
 * <pre>
 * 生产管理 - 产量上报
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2017年5月7日, minxl
 * @version 	   2.0, 2018年2月23日上午10:26:14, zhengby, 代码规范
 */
@Entity
@Table(name = "produce_work_report")
public class WorkReport extends BaseBillMasterTableEntity{
	

	private static final long serialVersionUID = -7918331779348439822L;

	/**
	 * 员工id
	 */
	private Long employeeId;
	
	/**
	 * 员工姓名
	 */
	@Column(length = 50)
	private String employeeName;
	
	/**
	 * 上报日期
	 */
	private Date reportTime = new Date();
	
	@Transient
	private List<WorkReportDetail> reportList;

	public List<WorkReportDetail> getReportList() {
		return reportList;
	}

	public void setReportList(List<WorkReportDetail> reportList) {
		this.reportList = reportList;
	}

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	
}
