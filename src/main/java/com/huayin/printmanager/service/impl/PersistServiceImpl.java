/**
 * <pre>
 * Title: 		PersistServiceImpl.java
 * Author:		linriqing
 * Create:	 	2010-8-5 下午04:57:55
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.printmanager.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.CommonDao;
import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.service.PersistService;

/**
 * <pre>
 * 框架 - 持久层服务接口
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月11日
 */
@Service(PersistService.PERSISTSERVICE_DEFAULT_BEANNAME)
@Lazy
public class PersistServiceImpl implements PersistService
{
	@Autowired
	private CommonDao commonDao;

	@Override
	public int countByNamedQuery(String name, Object... parameters)
	{
		return commonDao.countByNamedQuery(null, name, parameters);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(AbstractEntity entity)
	{
		commonDao.deleteEntity(entity.getClass(), entity.getId());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T extends AbstractEntity> void delete(Class<T> type, Object id)
	{
		commonDao.deleteEntity(type, id);
	}

	@Override
	public <T extends AbstractEntity> List<T> findByNamedQuery(Class<T> type, String name, Object... parameters)
	{
		return commonDao.findEntityByNamedQuery(type, name, parameters);
	}

	@Override
	public <T extends AbstractEntity> ServiceResult<List<T>> findListAndCountByNamedQuery(Class<T> type, String listName, String countName, int pageSize, int pageIndex, Object... parameters)
	{
		ServiceResult<List<T>> result = new ServiceResult<List<T>>();
		result.setReturnValue(this.listByNamedQuery(type, listName, pageSize, pageIndex, parameters));
		result.setReturnListSize(this.countByNamedQuery(countName, parameters));
		return result;
	}

	@Override
	public <T extends AbstractEntity> T get(Class<T> type, Object id)
	{
		return commonDao.getEntity(type, id);
	}

	@Override
	public <T extends AbstractEntity> T getByNamedQuery(Class<T> type, String name, Object... parameters)
	{
		return commonDao.getEntityByNamedQuery(type, name, parameters);
	}

	@Override
	public <T extends AbstractEntity> List<T> list(Class<T> type)
	{
		return commonDao.listAllEntity(type);
	}

	@Override
	public <T extends AbstractEntity> List<T> listByNamedQuery(Class<T> type, String name, int pageSize, int pageIndex, Object... parameters)
	{
		return commonDao.listEntityByNamedQuery(type, name, pageSize, pageIndex, parameters);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveOrUpdate(AbstractEntity entity)
	{
		commonDao.saveOrUpdateEntity(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T extends AbstractEntity> T save(T entity)
	{
		return commonDao.saveEntity(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveAllObject(Collection<? extends AbstractEntity> entitys)
	{
		commonDao.saveAllEntity(entitys);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T extends AbstractEntity> T update(T entity)
	{
		return commonDao.updateEntity(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAllObject(Collection<? extends AbstractEntity> entitys)
	{
		commonDao.updateAllEntity(entitys);
	}

	@Override
	public int countByDynamicQuery(DynamicQuery query)
	{
		return commonDao.countByDynamicQuery(query, null);
	}

	@Override
	public <T extends AbstractEntity> List<T> findByDynamicQuery(Class<T> type, DynamicQuery query)
	{
		return commonDao.findEntityByDynamicQuery(query, type);
	}

	public <T extends AbstractEntity> T getByDynamicQuery(Class<T> type, DynamicQuery query)
	{
		List<T> result = findByDynamicQuery(type, query);
		if (result != null && result.size() > 0)
		{
			return result.get(0);
		}
		return null;
	}

	public <T extends AbstractEntity> SearchResult<T> findByDynamicQueryPage(Class<T> type, DynamicQuery query)
	{
		return commonDao.findEntityByDynamicQueryPage(query, type);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public <T extends AbstractEntity> T lockObject(Class<T> type, Object id)
	{
		return commonDao.lockObject(type, id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public <T extends AbstractEntity> List<T> lockByDynamicQuery(DynamicQuery query, Class<T> type, LockType lockType)
	{
		return commonDao.lockByDynamicQuery(query, type, lockType);
	}
}
