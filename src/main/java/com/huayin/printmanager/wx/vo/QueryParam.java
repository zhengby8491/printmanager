/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.huayin.common.util.DateTimeUtil;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.WXSumQueryType;

/**
 * <pre>
 * 微信 - 微信查询参数
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class QueryParam implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long[] ids;

	private Long supplierId;

	private Long customerId;

	private BillType billType;

	private OfferType offerType;

	private String name;

	private String code;

	/**
	 * 单据日期区间
	 */
	@DateTimeFormat(iso = ISO.DATE)
	private Date dateMin;

	@DateTimeFormat(iso = ISO.DATE)
	private Date dateMax;

	/**
	 * 对账日期
	 */
	@DateTimeFormat(iso = ISO.DATE)
	private Date reconcilDate;

	/**
	 * 交货日期
	 */
	@DateTimeFormat(iso = ISO.DATE)
	private Date deliverDate;

	// 单据编号
	private String billNo;

	// 是否完工标志
	private BoolValue completeFlag;

	// 非固定字段查询条件
	private String searchContent;

	private Integer pageSize;

	private Integer pageNumber;

	private String title;

	// 用户名
	private String userName;

	private String passWord;

	// 微信汇总查询方式
	private WXSumQueryType wXSumQueryType;

	/**
	 * 是否到期应收
	 */
	private BoolValue isExpire;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 公司ID
	 */
	private String companyId;

	public Date getDateMin()
	{
		if (dateMin != null)
		{
			return DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(dateMin) + " 00:00:01", DateTimeUtil.DATE_FORMAT_YMDHMS);
		}
		return dateMin;
	}

	public void setDateMin(Date dateMin)
	{

		this.dateMin = dateMin;
	}

	public Date getDateMax()
	{
		if (dateMax != null)
		{
			return DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(dateMax) + " 23:59:59", DateTimeUtil.DATE_FORMAT_YMDHMS);
		}
		return dateMax;
	}

	public void setDateMax(Date dateMax)
	{
		this.dateMax = dateMax;
	}

	public BoolValue getIsExpire()
	{
		return isExpire;
	}

	public void setIsExpire(BoolValue isExpire)
	{
		this.isExpire = isExpire;
	}

	public String getPassWord()
	{
		return passWord;
	}

	public void setPassWord(String passWord)
	{
		this.passWord = passWord;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public Integer getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	public Integer getPageNumber()
	{
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber)
	{
		this.pageNumber = pageNumber;
	}

	public String getSearchContent()
	{
		return searchContent;
	}

	public void setSearchContent(String searchContent)
	{
		this.searchContent = searchContent;
	}

	public Date getReconcilDate()
	{
		return reconcilDate;
	}

	public void setReconcilDate(Date reconcilDate)
	{
		this.reconcilDate = reconcilDate;
	}

	public Date getDeliverDate()
	{
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate)
	{
		this.deliverDate = deliverDate;
	}

	public BillType getBillType()
	{
		return billType;
	}

	public void setBillType(BillType billType)
	{
		this.billType = billType;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public WXSumQueryType getwXSumQueryType()
	{
		return wXSumQueryType;
	}

	public void setwXSumQueryType(WXSumQueryType wXSumQueryType)
	{
		this.wXSumQueryType = wXSumQueryType;
	}

	public BoolValue getCompleteFlag()
	{
		return completeFlag;
	}

	public void setCompleteFlag(BoolValue completeFlag)
	{
		this.completeFlag = completeFlag;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(String companyId)
	{
		this.companyId = companyId;
	}

	public Long[] getIds()
	{
		return ids;
	}

	public void setIds(Long[] ids)
	{
		this.ids = ids;
	}

	public Long getSupplierId()
	{
		return supplierId;
	}

	public void setSupplierId(Long supplierId)
	{
		this.supplierId = supplierId;
	}

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public OfferType getOfferType()
	{
		return offerType;
	}

	public void setOfferType(OfferType offerType)
	{
		this.offerType = offerType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

}
