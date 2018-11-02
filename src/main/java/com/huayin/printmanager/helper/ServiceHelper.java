/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月30日 下午2:09:41
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.helper;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.persist.entity.AbstractTableIdEntity;

/**
 * <pre>
 * 框架 - 业务常用功能
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月30日
 */
public final class ServiceHelper
{
	/**
	 * 
	 * <pre>
	 * 过滤出新增、修改、删除对象的常量
	 * </pre>
	 * @author       think
	 * @since        1.0, 2017年10月31日
	 */
	public static interface Cud
	{
		/**
		 * 新增
		 */
		public static final String C = "C";

		/**
		 * 修改
		 */
		public static final String U = "U";
		
		/**
		 * 删除
		 */
		public static final String D = "D";
	}

	/**
	 * <pre>
	 * 过滤出新增、修改、删除的对象
	 * A : List（新增）
	 * U : List（修改）
	 * D : List（删除）
	 * </pre>
	 * @param <T>
	 * @param list
	 * @return
	 * @since 1.0, 2017年10月30日 上午11:25:55, think
	 */
	public static <T> Map<String, List<T>> filterCUD(List<T> oldList, List<T> list)
	{
		Map<String, List<T>> maps = Maps.newHashMap();
		// 新增集合
		List<T> listAdd = Lists.newArrayList();
		// 修改集合
		List<T> listUpdate = Lists.newArrayList();
		// 删除集合
		List<T> listDel = Lists.newArrayList();
		maps.put(Cud.C, listAdd);
		maps.put(Cud.U, listUpdate);
		maps.put(Cud.D, listDel);

		// 修改集合id_value
		Map<Long, Long> mapUpdate = Maps.newHashMap();

		for (T t : list)
		{
			// 这里转换为父类（只要获取ID即可）
			AbstractTableIdEntity entity = (AbstractTableIdEntity) t;
			// 新增集合
			if (entity.getId() == null)
			{
				listAdd.add(t);
			}
			// 修改集合
			else
			{
				// 修改
				listUpdate.add(t);
				// 修改id_value（用于过滤删除）
				mapUpdate.put(entity.getId(), entity.getId());
			}
		}

		// 不在修改集合中的数据都为已删除
		for (T t : oldList)
		{
			// 这里转换为父类（只要获取ID即可）
			AbstractTableIdEntity entity = (AbstractTableIdEntity) t;
			if (null == mapUpdate.get(entity.getId()))
			{
				listDel.add(t);
			}
		}

		return maps;
	}
}
