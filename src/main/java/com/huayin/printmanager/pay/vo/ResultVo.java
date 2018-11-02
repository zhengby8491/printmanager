package com.huayin.printmanager.pay.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 公共返回结果Vo
 * 
 * @author mys
 */
public class ResultVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 数据
	 */
	public Object data;
	
	/**
	 * 返回码
	 */
	public String code;
	
	/**
	 * 错误描述信息
	 */
	public String errorMessage;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String toString()
    {
        try
        {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
        catch (Exception e)
        {
        }
        return super.toString();
    }
}
