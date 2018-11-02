/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月11日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service;

import java.util.Collection;
import java.util.List;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;

/**
 * <pre>
 * 框架 - 持久层服务接口
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月11日
 */
public interface PersistService
{
	/**
	 * jpa持久层服务接口实现
	 */
	public static final String PERSISTSERVICE_DEFAULT_BEANNAME = "persistService";

	/**
	 * jdbc持久层服务接口实现
	 */
	public static final String PERSISTSERVICE_JDBC_BEANNAME = "jdbcPersistService";

	/**
	 * <pre>
	 * 保存或更新实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param entity 实体对象
	 */
	public void saveOrUpdate(AbstractEntity entity);

	/**
	 * <pre>
	 * 持久化实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param entity 实体对象
	 * @return 持久化后的实体对象
	 */
	<T extends AbstractEntity> T save(T entity);

	/**
	 * <pre>
	 * 持久化实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param entity 实体对象
	 * @return 持久化后的实体对象
	 */
	void saveAllObject(Collection<? extends AbstractEntity> entitys);

	/**
	 * <pre>
	 * 根据主键获取持久化的实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param id 实体主键
	 * @return 持久化的实体对象
	 */
	<T extends AbstractEntity> T get(Class<T> type, Object id);

	/**
	 * <pre>
	 * 更新持久化实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param entity 实体对象
	 * @return 更新后的持久化实体对象
	 */
	<T extends AbstractEntity> T update(T entity);

	/**
	 * <pre>
	 * 更新持久化实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param entity 实体对象
	 * @return 持久化后的实体对象
	 */
	void updateAllObject(Collection<? extends AbstractEntity> entitys);

	/**
	 * <pre>
	 * 获取所有持久化的实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @return 实体对象集合
	 */
	<T extends AbstractEntity> List<T> list(Class<T> type);

	/**
	 * <pre>
	 * 根据命名查询获取实体对象集合条数
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param name 命名查询名称
	 * @param parameters 参数集合
	 * @return 实体对象集合
	 */
	int countByNamedQuery(String name, Object... parameters);

	/**
	 * <pre>
	 * 根据动态查询获取实体对象集合条数
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param query 动态查询对象
	 * @return 实体对象集合记录数
	 */
	int countByDynamicQuery(DynamicQuery query);

	/**
	 * <pre>
	 * 根据动态查询获取实体对象集合条数
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param query 动态查询对象
	 * @return 实体对象集合
	 */
	<T extends AbstractEntity> List<T> findByDynamicQuery(Class<T> type, DynamicQuery query);

	public <T extends AbstractEntity> T getByDynamicQuery(Class<T> type, DynamicQuery query);

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param <T>
	 * @param type
	 * @param listName
	 * @param countName
	 * @param pageSize
	 * @param pageIndex
	 * @param parameters
	 * @return
	 */
	<T extends AbstractEntity> ServiceResult<List<T>> findListAndCountByNamedQuery(Class<T> type, String listName, String countName, int pageSize, int pageIndex, Object... parameters);

	/**
	 * <pre>
	 * 根据命名查询获取实体对象集合
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param name 命名查询名称
	 * @param pageSize 每页记录数
	 * @param pageIndex 当前页码
	 * @param parameters 参数集合
	 * @return 实体对象集合
	 */
	<T extends AbstractEntity> List<T> listByNamedQuery(Class<T> type, String name, int pageSize, int pageIndex, Object... parameters);

	/**
	 * <pre>
	 * 根据命名查询获取实体对象集合
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param name 命名查询名称
	 * @param parameters 参数集合
	 * @return 实体对象集合
	 */
	<T extends AbstractEntity> List<T> findByNamedQuery(Class<T> type, String name, Object... parameters);

	/**
	 * <pre>
	 * 删除持久化的实体对象
	 * </pre>
	 * @param entity 实体对象
	 */
	void delete(AbstractEntity entity);

	/**
	 * <pre>
	 * 删除持久化的实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param id 实体主键
	 */
	<T extends AbstractEntity> void delete(Class<T> type, Object id);

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param class1
	 * @param string
	 * @param name
	 * @return
	 */
	<T extends AbstractEntity> T getByNamedQuery(Class<T> type, String name, Object... parameters);

	public <T extends AbstractEntity> SearchResult<T> findByDynamicQueryPage(Class<T> type, DynamicQuery query);

	public <T extends AbstractEntity> T lockObject(Class<T> type, Object id);

	public <T extends AbstractEntity> List<T> lockByDynamicQuery(DynamicQuery query, Class<T> type, LockType lockType);

}
