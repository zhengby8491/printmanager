/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.cis.ComponentContextLoader;
import com.huayin.printmanager.persist.entity.sys.Sequence;
import com.huayin.printmanager.persist.enumerate.ResetCycle;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.SequenceService;

/**
 * <pre>
 * 系统模块 - 交易流水号
 * </pre>
 * <code>
 * long startSequence = SequenceServiceImpl.getInstance().getSequence("TestSequence", 10);
 * </code>
 * @author       think
 * @since        1.0, 2017年10月25日
 */
@Component(SequenceService.CONTEXT_BEAN_NAME_SEQUENCESERVICE)
@Lazy
public class SequenceServiceImpl extends BaseServiceImpl implements SequenceService
{

	public static long defaultMaxNum = 100000000;

	public static int defaultCacheCount = 1000;

	class SequenceCacheInfo
	{
		public int cacheCount = defaultCacheCount;

		public long cacheSequenceNo = 0;

		public boolean isInit = false;

		public long modMaxNum = defaultMaxNum;

		public String sequenceName;

		public long usedCount = 0;

	}

	public static final Map<String, SequenceCacheInfo> sequencecache = new HashMap<String, SequenceCacheInfo>();

	/**
	 * <pre>
	 * 线程安全/单例模式的获取实例方法
	 * 注意:对于Spring来说不是安全的单例, 因为Spring会使用反射调用私有的构造函数...
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:17:24, think
	 */
	public final static SequenceService getInstance()
	{
		return ComponentContextLoader.getBean(SequenceService.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SequenceService#getSequence(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public long getSequence(String sequenceName)
	{
		return getSequence(sequenceName, 1);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SequenceService#getSequence(java.lang.String, long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public long getSequence(String sequenceName, long sequenceCount)
	{
		return getSequence(sequenceName, sequenceCount, null);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SequenceService#getSequence(java.lang.String, long, java.lang.Long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public long getSequence(String sequenceName, long sequenceCount, Long modMaxNum)
	{
		return getSequence(sequenceName, sequenceCount, modMaxNum, null);
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param sequenceName
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:16:50, think
	 */
	private SequenceCacheInfo _getSequenceCacheInfo(String sequenceName)
	{
		if (sequencecache.containsKey(sequenceName))
		{
			return sequencecache.get(sequenceName);
		}
		else
		{
			synchronized (sequencecache)
			{
				if (sequencecache.containsKey(sequenceName))
				{
					return sequencecache.get(sequenceName);
				}
				else
				{
					SequenceCacheInfo sequencecacheinfo = new SequenceCacheInfo();
					sequencecache.put(sequenceName, sequencecacheinfo);
					return sequencecacheinfo;
				}
			}
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SequenceService#getSequence(java.lang.String, long, java.lang.Long,
	 * java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public long getSequence(String sequenceName, long sequenceCount, Long modMaxNum, Integer cacheCount)
	{
		long rtnSequence = 0;
		SequenceCacheInfo sequencecacheinfo = this._getSequenceCacheInfo(sequenceName);
		// 一次从数据库中取500个消息流水号保存在静态成员中，先从静态成员分配消息流水号，不够时从数据库再读取500个
		synchronized (sequencecacheinfo)
		{
			// 判断流水号信息是否已经初始化到内存中
			if (!sequencecacheinfo.isInit)
			{
				// 流水号信息还未缓存到内存中, 从数据库查询当前消息流水号到缓存中
				Sequence sequence = daoFactory.getCommonDao().lockObject(Sequence.class, sequenceName);

				if (sequence == null)
				{
					// 如果数据库中没有消息流水号则新增消息流水号纪录
					sequence = new Sequence();
					sequence.setIndexNum(1L);
					sequence.setName(sequenceName);
					if ((modMaxNum != null) && (modMaxNum > 0))
					{
						sequence.setModMaxNum(modMaxNum);
					}
					else
					{
						sequence.setModMaxNum(sequencecacheinfo.modMaxNum);
					}
					if ((cacheCount != null) && (cacheCount > 0))
					{
						sequence.setCacheCount(cacheCount);
					}
					else
					{
						sequence.setCacheCount(sequencecacheinfo.cacheCount);
					}
					daoFactory.getCommonDao().saveEntity(sequence);
				}

				// 当前流水号
				sequencecacheinfo.cacheSequenceNo = sequence.getIndexNum();
				sequencecacheinfo.cacheCount = sequence.getCacheCount();
				sequencecacheinfo.modMaxNum = sequence.getModMaxNum();
				sequencecacheinfo.sequenceName = sequence.getName();

				// 取一段流水号
				if (sequenceCount > sequencecacheinfo.cacheCount)
				{
					// 更新后的流水号
					long modAfterSequenceNo = (sequence.getIndexNum() + sequenceCount) % sequencecacheinfo.modMaxNum;
					sequence.setIndexNum(modAfterSequenceNo);
				}
				else
				{
					// 更新后的流水号
					long modAfterSequenceNo = (sequence.getIndexNum() + sequencecacheinfo.cacheCount) % sequencecacheinfo.modMaxNum;
					sequence.setIndexNum(modAfterSequenceNo);
				}
				// 保存本次批量投注流水号
				daoFactory.getCommonDao().updateEntity(sequence);

				// 当前获取的流水号
				rtnSequence = sequencecacheinfo.cacheSequenceNo;
				// 更新获取后缓存中的流水号
				sequencecacheinfo.cacheSequenceNo = sequencecacheinfo.cacheSequenceNo + sequenceCount;
				// 更新获取后缓存中已用掉的流水号个数
				sequencecacheinfo.usedCount = sequenceCount;
				// 设置缓存初始化状态
				sequencecacheinfo.isInit = true;
			}
			else
			{
				// 已经初始化到内存中
				// 判断缓存的流水号是否够用
				if ((sequencecacheinfo.usedCount + sequenceCount) > sequencecacheinfo.cacheCount)
				{
					// 不够用, 只能浪费剩下的流水号了, 需要重新取出一批流水号
					// 从数据库同步当前消息流水号到缓存中
					Sequence sequence = daoFactory.getCommonDao().lockObject(Sequence.class, sequenceName);

					// 当前流水号
					sequencecacheinfo.cacheSequenceNo = sequence.getIndexNum();
					sequencecacheinfo.cacheCount = sequence.getCacheCount();
					sequencecacheinfo.modMaxNum = sequence.getModMaxNum();
					sequencecacheinfo.sequenceName = sequence.getName();

					// 取一段流水号
					if (sequenceCount > sequencecacheinfo.cacheCount)
					{
						// 更新后的流水号
						long modAfterSequenceNo = (sequence.getIndexNum() + sequenceCount) % sequencecacheinfo.modMaxNum;
						sequence.setIndexNum(modAfterSequenceNo);
					}
					else
					{
						// 更新后的流水号
						long modAfterSequenceNo = (sequence.getIndexNum() + sequencecacheinfo.cacheCount) % sequencecacheinfo.modMaxNum;
						sequence.setIndexNum(modAfterSequenceNo);
					}
					// 保存本次批量投注流水号
					daoFactory.getCommonDao().updateEntity(sequence);

					// 当前获取的流水号
					rtnSequence = sequencecacheinfo.cacheSequenceNo;
					// 更新获取后缓存中的流水号
					sequencecacheinfo.cacheSequenceNo = sequencecacheinfo.cacheSequenceNo + sequenceCount;
					// 更新获取后缓存中已用掉的流水号个数
					sequencecacheinfo.usedCount = sequenceCount;
				}
				else
				{
					// 仍然够用, 直接在缓存中同步
					// 当前获取的流水号
					rtnSequence = sequencecacheinfo.cacheSequenceNo;
					// 更新获取后缓存中的流水号
					sequencecacheinfo.cacheSequenceNo = sequencecacheinfo.cacheSequenceNo + sequenceCount;
					// 更新获取后缓存中已用掉的流水号个数
					sequencecacheinfo.usedCount = sequencecacheinfo.usedCount + sequenceCount;
				}
			}

		}
		return rtnSequence;

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SequenceService#getNoCacheSequence(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public long getNoCacheSequence(String sequenceName)
	{
		return getNoCacheSequence(sequenceName, null);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SequenceService#getNoCacheSequence(java.lang.String,
	 * com.huayin.printmanager.persist.enumerate.ResetCycle)
	 */
	@Override
	public long getNoCacheSequence(String sequenceName, ResetCycle cycle)
	{

		// 流水号信息还未缓存到内存中, 从数据库查询当前消息流水号到缓存中
		Sequence sequence = daoFactory.getCommonDao().lockObject(Sequence.class, sequenceName);
		if (sequence == null)
		{
			// 如果数据库中没有消息流水号则新增消息流水号纪录
			sequence = new Sequence();
			sequence.setIndexNum(1L);
			sequence.setName(sequenceName);
			sequence.setModMaxNum(defaultMaxNum);
			sequence.setCacheCount(1);
			if (cycle != null)
			{
				sequence.setCycle(cycle);
				sequence.setCurrentDateNode(cycle.getCurrentDateNode());
			}
			daoFactory.getCommonDao().saveEntity(sequence);
		}
		else
		{
			if (cycle != null)
			{
				if (!sequence.getCurrentDateNode().equals(cycle.getCurrentDateNode()))
				{// 判断周期日期节点是否一致，不一致则重置序号
					sequence.setIndexNum(0L);
					sequence.setCycle(cycle);
					sequence.setCurrentDateNode(cycle.getCurrentDateNode());
				}
			}
			// 更新当前序号
			long afterSequenceNo = (sequence.getIndexNum() + 1) % sequence.getModMaxNum();
			sequence.setIndexNum(afterSequenceNo);
			daoFactory.getCommonDao().updateEntity(sequence);
		}
		return sequence.getIndexNum();
	}

}
