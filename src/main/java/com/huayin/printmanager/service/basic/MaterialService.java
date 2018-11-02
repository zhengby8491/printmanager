/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月2日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 材料信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月2日
 * @since        2.0, 2018年1月2日 下午17:07:00, think, 规范和国际化
 */
public interface MaterialService
{
	/**
	 * <pre>
	 * 根据id获取材料信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:09:20, think
	 */
	public Material get(Long id);

	/**
	 * <pre>
	 * 根据id获取材料信息
	 * </pre>
	 * @param companyId
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:09:26, think
	 */
	public Material get(String companyId, Long id);

	/**
	 * <pre>
	 * 根据name获取材料信息
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:09:34, think
	 */
	public Material getByName(String name);

	/**
	 * <pre>
	 * 保存材料信息
	 * </pre>
	 * @param material
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:09:42, think
	 */
	public Material save(Material material);
	
	/**
	 * <pre>
	 * 保存材料信息（快速添加）
	 * </pre>
	 * @param materialClassId
	 * @param name
	 * @param weight
	 * @param valuationUnitId
	 * @param stockUnitId
	 * @param lastPurchPrice
	 * @return
	 * @since 1.0, 2018年2月9日 上午10:53:57, think
	 */
	public Material saveQuick(Long materialClassId, String name, Integer weight, Long valuationUnitId, Long stockUnitId, BigDecimal lastPurchPrice);

	/**
	 * <pre>
	 * 修改材料信息
	 * </pre>
	 * @param material
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:09:49, think
	 */
	public Material update(Material material);

	/**
	 * <pre>
	 * 批量删除材料信息
	 * </pre>
	 * @param ids
	 * @since 1.0, 2018年1月2日 上午9:09:57, think
	 */
	public void deleteByIds(Long[] ids);

	/**
	 * <pre>
	 * 得到全部材料信息
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:10:04, think
	 */
	public List<Material> findAll();

	/**
	 * <pre>
	 * 按分类查询材料列表
	 * </pre>
	 * @param materialClassId
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:10:11, think
	 */
	public List<Material> findByMaterialClassId(Long materialClassId);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:10:21, think
	 */
	public SearchResult<Material> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 快捷查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:10:28, think
	 */
	public SearchResult<Material> quickFindByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * excel材料导入
	 * </pre>
	 * @param inputStream
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年1月2日 上午9:10:35, think
	 */
	public Integer importFromExcel(InputStream inputStream) throws Exception;

	/**
	 * <pre>
	 * 计算单张材料成本
	 * </pre>
	 * @param material
	 * @param width
	 * @param length
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:10:50, think
	 */
	public double caculUnitPrice(Material material, int width, int length);
}
