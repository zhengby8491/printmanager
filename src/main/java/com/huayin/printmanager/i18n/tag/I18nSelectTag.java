/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年12月29日 下午2:48:36
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.i18n.tag;

import java.util.Collection;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import com.huayin.common.web.tags.SelectTag;
import com.huayin.common.web.tags.TagConstant;
import com.huayin.printmanager.i18n.ResourceBundleMessageSource;

/**
 * <pre>
 * 国际化框架 - 标签
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 */
public class I18nSelectTag extends TagSupport
{
	public static class SingleObject
	{
		String label;

		String value;
	}

	private static final Log logger = LogFactory.getLog(SelectTag.class);

	private static final long serialVersionUID = -1551490822491431975L;

	public static boolean isChecked(SingleObject so, String selectValue)
	{
		boolean isSelected = false;
		if (selectValue != null)
		{
			String[] itemsArray = selectValue.split(TagConstant.INPUT_COLLECTION_SPLIT_REGEX);
			for (String string : itemsArray)
			{
				if (string.equalsIgnoreCase(so.value))
				{
					isSelected = true;
					break;
				}
			}
		}
		return isSelected;
	}

	public static SingleObject wrapperLabelValue(Object item, String textProperty, String valueProperty)
	{
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
		Object renderValue;
		Object renderLabel = null;
		if ((valueProperty != null) && (valueProperty.length() > 0))
		{
			renderValue = wrapper.getPropertyValue(valueProperty);
		}
		else if (item instanceof Enum<?>)
		{
			renderValue = ((Enum<?>) item).name();
			// 解决枚举国际化
			String i18nCode = "i18n.enum." + item.getClass().getSimpleName().toLowerCase() + "." + renderValue;
//			if (ResourceBundleMessageSource.hasMessage(i18nCode))
//			{
//				renderLabel = ResourceBundleMessageSource.getMessage(i18nCode);
//			}
			renderLabel = ResourceBundleMessageSource.getMessageNull(i18nCode);
		}
		else
		{
			renderValue = item;
		}

		if (renderLabel == null)
		{
			renderLabel = (((textProperty != null) && (textProperty.length() > 0)) ? wrapper.getPropertyValue(textProperty) : item);
		}

		SingleObject so = new SingleObject();
		so.label = renderLabel.toString();
		so.value = renderValue.toString();
		return so;
	}

	public static SingleObject wrapperMapLabelValue(Map.Entry<?, ?> entry, String textProperty, String valueProperty)
	{
		Object mapKey = entry.getKey();
		Object mapValue = entry.getValue();
		BeanWrapper mapKeyWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapKey);
		BeanWrapper mapValueWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapValue);
		Object renderValue = (((valueProperty != null) && (valueProperty.length() > 0)) ? mapKeyWrapper.getPropertyValue(valueProperty) : mapKey.toString());
		Object renderLabel = (((textProperty != null) && (textProperty.length() > 0)) ? mapValueWrapper.getPropertyValue(textProperty) : mapValue.toString());
		SingleObject so = new SingleObject();
		so.label = renderLabel.toString();
		so.value = renderValue.toString();
		return so;
	}

	private String defaultOption;

	private String defaultValue;

	private String inputType;

	private Object items;

	private boolean multiple;

	private String name;

	private Object onblur;

	private Object onchange;

	private String selected;

	private String textProperty;

	private String type;

	private String valueProperty;

	private String cssClass;

	private String cssStyle;

	public I18nSelectTag()
	{
		super();
	}

	public int doEndTag() throws JspException
	{
		return super.doEndTag();
	}

	public int doStartTag() throws JspException
	{
		super.doStartTag();
		try
		{
			if (inputType == null || inputType.equalsIgnoreCase(TagConstant.ENUMTAG_INPUTTYPE_SELECT_NAME))
			{
				pageContext.getOut().print(renderInput4Select(name, items, type, textProperty, valueProperty, selected, onchange, multiple, onblur));
			}
			else
			{
				pageContext.getOut().print(renderInput4Multiple(items, inputType, type, name, valueProperty, textProperty, selected));
			}
		}
		catch (Exception e)
		{
			logger.error("do EnumTag failed, page:" + ((Servlet) this.pageContext.getPage()).toString() + ", falt detail:" + e.toString(), e);
		}
		return SKIP_BODY;
	}

	public String getDefaultOption()
	{
		return defaultOption;
	}

	public String getDefaultValue()
	{
		return defaultValue;
	}

	public String getInputType()
	{
		return inputType;
	}

	public Object getItems()
	{
		return items;
	}

	public String getName()
	{
		return name;
	}

	public Object getOnblur()
	{
		return onblur;
	}

	public Object getOnchange()
	{
		return onchange;
	}

	public String getSelected()
	{
		return selected;
	}

	public String getTextProperty()
	{
		return textProperty;
	}

	public String getType()
	{
		return type;
	}

	public String getValueProperty()
	{
		return valueProperty;
	}

	public boolean isMultiple()
	{
		return multiple;
	}

	public void setDefaultOption(String defaultText)
	{
		this.defaultOption = defaultText;
	}

	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	public void setInputType(String controller)
	{
		this.inputType = controller;
	}

	public void setItems(Object items) throws JspTagException
	{
		this.items = items;
	}

	public void setMultiple(boolean multiple)
	{
		this.multiple = multiple;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setOnblur(Object onblur)
	{
		this.onblur = onblur;
	}

	public void setOnchange(Object onchange)
	{
		this.onchange = onchange;
	}

	public void setSelected(String selected)
	{
		this.selected = selected;
	}

	public void setTextProperty(String showProperty)
	{
		this.textProperty = showProperty;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setValueProperty(String valueProperty)
	{
		this.valueProperty = valueProperty;
	}

	public String getCssClass()
	{
		return cssClass;
	}

	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}

	public String getCssStyle()
	{
		return cssStyle;
	}

	public void setCssStyle(String cssStyle)
	{
		this.cssStyle = cssStyle;
	}

	protected StringBuffer renderInput4Multiple(Object requestItems, String inputType, String type, String name, String valueProperty, String textProperty, String selected) throws JspException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException
	{
		StringBuffer sb = new StringBuffer();

		Object itemsObject = requestItems;
		if (itemsObject == null)
		{
			Class<?> enumType = Class.forName(type);
			if (enumType != null && enumType.isEnum())
			{
				itemsObject = enumType.getEnumConstants();
			}
		}
		if (itemsObject != null)
		{
			if (itemsObject.getClass().isArray())
			{
				Object[] itemsArray = (Object[]) itemsObject;
				for (Object item : itemsArray)
				{
					SingleObject so = wrapperLabelValue(item, textProperty, valueProperty);
					sb.append(renderInputMultuple(name, inputType, selected, so));
				}
			}
			else if (itemsObject instanceof Collection<?>)
			{
				final Collection<?> optionCollection = (Collection<?>) itemsObject;
				for (Object item : optionCollection)
				{
					SingleObject so = wrapperLabelValue(item, textProperty, valueProperty);
					sb.append(renderInputMultuple(name, inputType, selected, so));
				}
			}
			else if (itemsObject instanceof Map<?, ?>)
			{
				final Map<?, ?> optionMap = (Map<?, ?>) itemsObject;
				for (Map.Entry<?, ?> entry : optionMap.entrySet())
				{
					SingleObject so = wrapperMapLabelValue(entry, textProperty, valueProperty);
					sb.append(renderInputMultuple(name, inputType, selected, so));
				}
			}
			else
			{
				throw new IllegalArgumentException("Attribute 'items' must be a Collection, an Array or a Map");
			}
		}
		return sb;
	}

	protected StringBuffer renderInput4Select(String name, Object requestItems, String type, String textProperty, String valueProperty, String selected, Object onchange, boolean multiple, Object onblur) throws JspException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException
	{
		String onblurchange = "";
		if (onblur != null)
		{
			onblurchange = "onblur=\"" + onblur + "\" ";
		}
		if (onchange != null)
		{
			onblurchange = onblurchange + "onchange=\"" + onchange + "\" ";
		}
		StringBuffer sb = new StringBuffer();
		Object itemsObject = requestItems;
		if (itemsObject == null)
		{
			if (type != null)
			{
				Class<?> enumType = Class.forName(type);
				if (enumType != null && enumType.isEnum())
				{
					itemsObject = enumType.getEnumConstants();
				}
			}
		}
		String _multiple = "";
		if (multiple)
		{
			_multiple = "multiple='true'";
		}
		sb.append("<select style=\"" + (cssStyle == null ? "" : cssStyle) + "\" class=\"" + (cssClass == null ? "" : cssClass) + "\" id=\"" + name + "\" name=\"" + name + "\" " + onblurchange + " " + _multiple + ">");
		if ((defaultValue != null) || (defaultOption != null))
		{
			sb.append("<option value='");
			if (defaultValue != null)
			{
				sb.append(defaultValue);
			}
			sb.append("'>");
			if (defaultOption != null)
			{
				sb.append(defaultOption);
			}
			else
			{
				sb.append("--请选择--");
			}
			sb.append("</option>");
		}
		if (itemsObject != null)
		{
			if (itemsObject.getClass().isArray())
			{
				Object[] itemsArray = (Object[]) itemsObject;
				for (Object item : itemsArray)
				{
					SingleObject so = wrapperLabelValue(item, textProperty, valueProperty);
					sb.append(renderInputOption(selected, so, selected));
				}
			}
			else if (itemsObject instanceof Collection<?>)
			{
				final Collection<?> optionCollection = (Collection<?>) itemsObject;
				for (Object item : optionCollection)
				{
					SingleObject so = wrapperLabelValue(item, textProperty, valueProperty);
					sb.append(renderInputOption(selected, so, selected));
				}
			}
			else if (itemsObject instanceof Map<?, ?>)
			{
				final Map<?, ?> optionMap = (Map<?, ?>) itemsObject;
				for (Map.Entry<?, ?> entry : optionMap.entrySet())
				{
					SingleObject so = wrapperMapLabelValue(entry, textProperty, valueProperty);
					sb.append(renderInputOption(selected, so, selected));
				}
			}
			else
			{
				throw new IllegalArgumentException("Attribute 'items' must be a Collection, an Array or a Map");
			}
		}
		sb.append("</select>");
		return sb;
	}

	private String renderInputMultuple(String name, String type, String selectValue, SingleObject so) throws JspException
	{
		String onblurchange = "";
		if (this.onblur != null)
		{
			onblurchange = "onblur=\"" + this.onblur + "\" ";
		}
		if (this.onchange != null)
		{
			onblurchange = onblurchange + "onchange=\"" + this.onchange + "\" ";
		}
		StringBuffer string = new StringBuffer();
		if (type.equals("text"))
		{
			if (isChecked(so, selectValue))
			{
				String showName = "";
				if (StringUtils.isNotEmpty(name))
				{
					showName = name + "_label";
				}
				string.append("<input id=\"" + showName + "\" type=\"" + type + "\" name=\"" + showName + "\" value=\"" + so.label + "\" " + onblurchange + "/>");
				string.append("<input id=\"" + name + "\" type=\"hidden\" name=\"" + name + "\" value=\"" + so.value + "\" " + onblurchange + "/>");
			}
		}
		else if (type.equals("checkbox") || type.equals("radio"))
		{
			if (isChecked(so, selectValue))
			{
				string.append("<label><input id=\"" + name + "\" type=\"" + type + "\" name=\"" + name + "\" value=\"" + so.value + "\" checked=\"checked\" " + onblurchange + "/>" + so.label + "</label>");
			}
			else
			{
				string.append("<label><input id=\"" + name + "\" type=\"" + type + "\" name=\"" + name + "\" value=\"" + so.value + "\" " + onblurchange + "/>" + so.label + "</label>");
			}
		}
		else if (type.equals("li"))
		{
			string.append("<li value=\"" + so.label + "\" class=\"" + (cssClass == null ? "" : cssClass) + "\">" + so.label + "</li>");
		}
		return string.toString();
	}

	private String renderInputOption(Object value, SingleObject so, String selectValue) throws JspException
	{
		String htmlElement = "";
		if (isChecked(so, selectValue))
		{
			htmlElement = "<option value=\"" + so.value + "\" selected=\"selected\">" + so.label + "</option>";
		}
		else
		{
			htmlElement = "<option value=\"" + so.value + "\">" + so.label + "</option>";
		}
		return htmlElement;
	}
}
