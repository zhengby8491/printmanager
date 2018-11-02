/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.persist.enumerate.EmployeeState;
import com.huayin.printmanager.persist.enumerate.MaterialType;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.ProduceType;
import com.huayin.printmanager.persist.enumerate.ScheduleDataSource;
import com.huayin.printmanager.persist.enumerate.SexType;
import com.huayin.printmanager.persist.enumerate.SupplierType;
import com.huayin.printmanager.persist.enumerate.YieldReportingType;

/**
 * <pre>
 * 公共 - 对象操作工具类, 继承org.apache.commons.lang3.ObjectUtils类
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils
{
	/**
	 * <pre>
	 * 注解到对象复制，只复制能匹配上的方法。
	 * </pre>
	 * @param annotation
	 * @param object
	 * @since 1.0, 2017年10月26日 上午10:38:31, think
	 */
	public static void annotationToObject(Object annotation, Object object)
	{
		if (annotation != null)
		{
			Class<?> annotationClass = annotation.getClass();
			Class<?> objectClass = object.getClass();
			for (Method m : objectClass.getMethods())
			{
				if (StringUtils.startsWith(m.getName(), "set"))
				{
					try
					{
						String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
						Object obj = annotationClass.getMethod(s).invoke(annotation);
						if (obj != null && !"".equals(obj.toString()))
						{
							if (object == null)
							{
								object = objectClass.newInstance();
							}
							m.invoke(object, obj);
						}
					}
					catch (Exception e)
					{
						// 忽略所有设置失败方法
					}
				}
			}
		}
	}

	/**
	 * <pre>
	 * 序列化对象
	 * </pre>
	 * @param object
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:38:38, think
	 */
	public static byte[] serialize(Object object)
	{
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try
		{
			if (object != null)
			{
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				return baos.toByteArray();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <pre>
	 * 反序列化对象
	 * </pre>
	 * @param bytes
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:38:46, think
	 */
	public static Object unserialize(byte[] bytes)
	{
		ByteArrayInputStream bais = null;
		try
		{
			if (bytes != null && bytes.length > 0)
			{
				bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				return ois.readObject();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param text
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:38:52, think
	 */
	public static EmployeeState getEmployeeState(String text)
	{
		for (EmployeeState o : EmployeeState.values())
		{
			if (o.getText().equals(text))
			{
				return o;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param text
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:38:55, think
	 */
	public static SexType getSexType(String text)
	{
		for (SexType o : SexType.values())
		{
			if (o.getText().equals(text))
			{
				return o;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param text
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:38:57, think
	 */
	public static CurrencyType getCurrencyType(String text)
	{
		for (CurrencyType o : CurrencyType.values())
		{
			if (o.getText().equals(text))
			{
				return o;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param text
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:39:00, think
	 */
	public static BoolValue getBoolValue(String text)
	{
		for (BoolValue o : BoolValue.values())
		{
			if (o.getText().equals(text))
			{
				return o;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param text
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:39:02, think
	 */
	public static SupplierType getSupplierType(String text)
	{
		for (SupplierType o : SupplierType.values())
		{
			if (o.getText().equals(text))
			{
				return o;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param text
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:39:04, think
	 */
	public static ProcedureType getProcedureType(String text)
	{
		for (ProcedureType o : ProcedureType.values())
		{
			if (o.getText().equals(text))
			{
				return o;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param text
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:39:07, think
	 */
	public static ProduceType getProduceType(String text)
	{
		for (ProduceType o : ProduceType.values())
		{
			if (o.getText().equals(text))
			{
				return o;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param text
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:39:18, think
	 */
	public static YieldReportingType getYieldReportingType(String text)
	{
		for (YieldReportingType o : YieldReportingType.values())
		{
			if (o.getText().equals(text))
			{
				return o;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param text
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:39:16, think
	 */
	public static ScheduleDataSource getScheduleDataSource(String text)
	{
		for (ScheduleDataSource o : ScheduleDataSource.values())
		{
			if (o.getText().equals(text))
			{
				return o;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param text
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:39:14, think
	 */
	public static MaterialType getMaterialType(String text)
	{
		for (MaterialType o : MaterialType.values())
		{
			if (o.getText().equals(text))
			{
				return o;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param obj
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年10月26日 上午10:39:10, think
	 */
	public static Map<String, Object> objectToMap(Object obj) throws Exception
	{
		if (obj == null)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();

		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors)
		{
			String key = property.getName();
			if (key.compareToIgnoreCase("class") == 0)
			{
				continue;
			}
			Method getter = property.getReadMethod();
			Object value = getter != null ? getter.invoke(obj) : null;
			map.put(key, value);
		}

		return map;
	}
}
