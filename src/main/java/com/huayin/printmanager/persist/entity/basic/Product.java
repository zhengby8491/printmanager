/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月2日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 产品信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月2日
 * @since        2.0, 2018年1月2日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_product")
public class Product extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 产品代码
	 */
	@Column(length = 50)
	private String code;

	/**
	 * 成品名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 产品分类id
	 */
	@Column(length = 50)
	private Long productClassId;

	/**
	 * 规格
	 */
	private String specifications;

	/**
	 * 客户料号
	 */
	@Column(length = 50)
	private String customerMaterialCode;

	/**
	 * 销售单位ID
	 */
	private Long unitId;

	/**
	 * 销售单价
	 */
	private BigDecimal salePrice;

	/**
	 * 重量
	 */
	private Integer weight;

	/**
	 * 保质期
	 */
	private Integer shelfLife;

	/**
	 * 是否公共产品(默认:否)
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isPublic;

	/**
	 * 是否有效(默认:是)
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isValid = BoolValue.YES;

	/**
	 * P数
	 */
	private Integer pNum;

	/**
	 * 创建人
	 */
	@Column(length = 50)
	private String createName;

	/**
	 * 修改人
	 */
	@Column(length = 50)
	private String updateName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 产品分类
	 */
	@Transient
	private ProductClass productClass;

	/**
	 * 图片名称
	 */
	private String fileName;

	/**
	 * 归属客户
	 */
	@Transient
	private List<Customer> customerList;

	/**
	 * 返回产品图片真实路径（页面查看的真实路径）
	 */
	@Transient
	private String imgUrl;

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Long getProductClassId()
	{
		return productClassId;
	}

	public void setProductClassId(Long productClassId)
	{
		this.productClassId = productClassId;
	}

	public String getSpecifications()
	{
		return specifications;
	}

	public void setSpecifications(String specifications)
	{
		this.specifications = specifications;
	}

	public String getCustomerMaterialCode()
	{
		return customerMaterialCode;
	}

	public void setCustomerMaterialCode(String customerMaterialCode)
	{
		this.customerMaterialCode = customerMaterialCode;
	}

	public BigDecimal getSalePrice()
	{
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice)
	{
		this.salePrice = salePrice;
	}

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public Integer getShelfLife()
	{
		return shelfLife;
	}

	public void setShelfLife(Integer shelfLife)
	{
		this.shelfLife = shelfLife;
	}

	public BoolValue getIsPublic()
	{
		return isPublic;
	}

	public void setIsPublic(BoolValue isPublic)
	{
		this.isPublic = isPublic;
	}

	public BoolValue getIsValid()
	{
		return isValid;
	}

	public void setIsValid(BoolValue isValid)
	{
		this.isValid = isValid;
	}

	public Long getUnitId()
	{
		return unitId;
	}

	public void setUnitId(Long unitId)
	{
		this.unitId = unitId;
	}

	public Integer getpNum()
	{
		return pNum;
	}

	public void setpNum(Integer pNum)
	{
		this.pNum = pNum;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public String getUpdateName()
	{
		return updateName;
	}

	public void setUpdateName(String updateName)
	{
		this.updateName = updateName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public ProductClass getProductClass()
	{
		return productClass;
	}

	public void setProductClass(ProductClass productClass)
	{
		this.productClass = productClass;
	}

	public List<Customer> getCustomerList()
	{
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList)
	{
		this.customerList = customerList;
	}

	public String getProductClassName()
	{
		if (productClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.PRODUCTCLASS.name(), productClassId, "name");
		}
		return "-";
	}

	public String getUnitName()
	{
		if (unitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), unitId, "name");
		}
		return "-";
	}

	public String getIsPublicText()
	{
		if (isPublic != null)
		{
			return isPublic.getText();
		}
		return "-";
	}

	public String getIsValidText()
	{
		if (isValid != null)
		{
			return isValid.getText();
		}
		return "-";
	}

	public String getImgUrl()
	{
		if (fileName != null && fileName != "")
		{
			String companyId = UserUtils.getCompanyId();
			String prefixUrl = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_URL) + "/" + companyId + "/";
			return prefixUrl.concat("productImg/").concat(fileName);
		}
		return "";
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

}
