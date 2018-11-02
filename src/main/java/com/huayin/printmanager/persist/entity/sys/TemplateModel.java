package com.huayin.printmanager.persist.entity.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.PrintModleName;

@Entity
@Table(name = "sys_template_model")
public class TemplateModel extends BaseTableIdEntity
{
	private static final long serialVersionUID = -4969975632609308675L;
	/**
	 * 所属订单类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private PrintModleName billType;
	/**
	 * 模板标题
	 */
	@Column(length = 100)
	private String title;
	/**
	 * 模板内容
	 */
	@Column(length = 999999)
	private String context;
	/**
	 * 创建人
	 */
	@Column(length = 50)
	private Long createUserId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人
	 */
	@Column(length = 50)
	private Long updateUserId;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 公司名称
	 */
	@Transient
	private String companyName;
	/**
	 * 是否正式
	 */
	@Transient
	private String isFormal;
	
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue isPublic;
	
	/**
	 * 联系电话
	 */
	@Transient
	private String tel;
	public String getContext()
	{
		return context;
	}
	public void setContext(String context)
	{
		this.context = context;
	}
	 
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	public Long getCreateUserId()
	{
		return createUserId;
	}
	public void setCreateUserId(Long createUserId)
	{
		this.createUserId = createUserId;
	}
	 
	public Long getUpdateUserId()
	{
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId)
	{
		this.updateUserId = updateUserId;
	}
	public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	 
	public PrintModleName getBillType() {
		return billType;
	}
	public void setBillType(PrintModleName billType) {
		this.billType = billType;
	}
	public String getBillTypeText()
	{
		if(billType!=null){
			return billType.getText();
		}
		return "";
		
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getIsFormal() {
		return isFormal;
	}
	public void setIsFormal(String isFormal) {
		this.isFormal = isFormal;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public BoolValue getIsPublic() {
		return isPublic;
	}
	public String getIsPublicText() {
		if(isPublic!=null){
			return isPublic.getText();
		}
		return null;
	}
	public void setIsPublic(BoolValue isPublic) {
		this.isPublic = isPublic;
	}
	
}
