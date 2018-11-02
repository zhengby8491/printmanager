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

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.utils.DateUtils;

/**
 * <pre>
 * 生产管理 - 产量上报
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2017年5月7日, minxl
 * @version 	   2.0, 2018年2月23日上午10:28:35, zhengby, 代码规范
 */
@Entity
@Table(name = "produce_work_report_detail")
public class WorkReportDetail extends BaseBillDetailTableEntity{
	

	private static final long serialVersionUID = -5529287079791292336L;

	/**
	 * 生产任务id
	 */
	private Long taskId;
	
	/**
	 * 上报数
	 */
	private BigDecimal reportQty=new BigDecimal(0);
	
	/**
	 * 合格数量
	 */
	private BigDecimal qualifiedQty;
	
	/**
	 *不合格数量
	 */
	private BigDecimal unqualified;
	
	/**
	 * 生产开始时间
	 */
	private Date startTime;
	
	/**
	 * 生产结束时间
	 */
	private Date endTime;
	
	/**
	 * 工时
	 */
	@Column(length = 50)
	private String workHour;
	
	/**
	 * 上报单号
	 */
	@Transient
	private String masterBillNo;

	/**
	 * 是否作废
	 */
	@Transient
	@Enumerated(EnumType.STRING)
	private BoolValue isCancel=BoolValue.NO;

	/**
	 * 是否已审核
	 */
	@Transient
	@Enumerated(EnumType.STRING)
	private BoolValue isCheck=BoolValue.NO;
	
	@Transient
	private String employeeName;
	
	/**
	 * 生产任务对象
	 */
	@Transient
	private WorkReportTask task;
	
	@Transient
	private WorkReport report;
	
	public BigDecimal getReportQty() {
		return reportQty;
	}

	public void setReportQty(BigDecimal reportQty) {
		this.reportQty = reportQty;
	}

	public BigDecimal getQualifiedQty() {
		return qualifiedQty;
	}

	public void setQualifiedQty(BigDecimal qualifiedQty) {
		this.qualifiedQty = qualifiedQty;
	}

	public BigDecimal getUnqualified() {
		return unqualified;
	}

	public void setUnqualified(BigDecimal unqualified) {
		this.unqualified = unqualified;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public WorkReportTask getTask() {
		return task;
	}

	public void setTask(WorkReportTask task) {
		this.task = task;
	}

	public String getMasterBillNo() {
		return masterBillNo;
	}

	public void setMasterBillNo(String masterBillNo) {
		this.masterBillNo = masterBillNo;
	}

	public BoolValue getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(BoolValue isCancel) {
		this.isCancel = isCancel;
	}

	public BoolValue getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(BoolValue isCheck) {
		this.isCheck = isCheck;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}


	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setStartTime(String startTime) {
		Date parseDate = DateUtils.parseDate(startTime);
		this.startTime = parseDate;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public void setEndTime(String endTime) {
		Date parseDate = DateUtils.parseDate(endTime);
		this.endTime = parseDate;
	}
	
	public String getWorkHour() {
		return workHour;
	}

	public void setWorkHour(String workHour) {
		this.workHour = workHour;
	}
	
	
	public WorkReport getReport() {
		return report;
	}

	public void setReport(WorkReport report) {
		this.report = report;
	}

	public static void main(String[] args) {
		WorkReportDetail reportDetail = new WorkReportDetail();
		reportDetail.setStartTime("2017-10-10 11:11:11");
		System.out.println(reportDetail.getStartTime());
	}
}
