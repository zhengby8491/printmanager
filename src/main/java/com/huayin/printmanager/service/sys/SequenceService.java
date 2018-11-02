/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys;

import com.huayin.printmanager.persist.enumerate.ResetCycle;

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
public interface SequenceService
{
	public static final String CONTEXT_BEAN_NAME_SEQUENCESERVICE = "sequenceService";

	/**
	 * <pre>
	 * 根据流水号名称获取一个流水号
	 * </pre>
	 * @param sequenceName 流水号名称
	 * @return
	 */
	long getSequence(String sequenceName);

	/**
	 * <pre>
	 * 获取一批流水号
	 * </pre>
	 * @param sequenceName 流水号名称
	 * @param sequenceCount 流水号个数
	 * @return 该批流水号初始值
	 */
	public long getSequence(String sequenceName, long sequenceCount);

	/**
	 * <pre>
	 * 获取一批流水号
	 * </pre>
	 * @param sequenceName 流水号名称
	 * @param sequenceCount 流水号个数
	 * @param modMaxNum 流水号最大允许的计数，达到改数后从1开始循环
	 * @return 该批流水号初始值
	 */
	public long getSequence(String sequenceName, long sequenceCount, Long modMaxNum);

	/**
	 * <pre>
	 * 获取一批流水号
	 * </pre>
	 * @param sequenceName 流水号名称
	 * @param sequenceCount 流水号个数
	 * @param modMaxNum 流水号最大允许的计数，达到改数后从1开始循环
	 * @return 该批流水号初始值
	 */
	public long getSequence(String sequenceName, long sequenceCount, Long modMaxNum, Integer cacheCount);

	/**
	 * <pre>
	 * 每次获取一个流水号，没有缓存
	 * </pre>
	 */
	public long getNoCacheSequence(String sequenceName);

	/**
	 * <pre>
	 * 每次按生成周期获取一个流水号，没有缓存
	 * </pre>
	 */
	public long getNoCacheSequence(String sequenceName, ResetCycle cycle);
}
