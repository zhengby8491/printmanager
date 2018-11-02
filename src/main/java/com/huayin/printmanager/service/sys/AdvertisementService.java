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

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.Advertisement;
import com.huayin.printmanager.persist.entity.sys.AdvertisementAccesslog;
import com.huayin.printmanager.persist.enumerate.AdvertisementType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统模块 - 广告管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface AdvertisementService
{
	/**
	 * <pre>
	 * 根据id获取广告信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:22:23, think
	 */
	public Advertisement get(Long id);

	/**
	 * <pre>
	 * 多条件查询广告信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:22:48, think
	 */
	public SearchResult<Advertisement> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 多条件查询广告访问记录信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:22:55, think
	 */
	public SearchResult<AdvertisementAccesslog> findAccesslogByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询所有已发布广告
	 * </pre>
	 * @param advertisementType
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:34:56, think
	 */
	public List<Advertisement> findAllPublish(AdvertisementType advertisementType);

	/**
	 * <pre>
	 * 新增系统广告
	 * </pre>
	 * @param advertisement
	 * @param upfile
	 * @since 1.0, 2017年10月25日 下午4:35:03, think
	 */
	public void save(Advertisement advertisement, MultipartFile upfile);

	/**
	 * <pre>
	 * 新增访问记录
	 * </pre>
	 * @param advertisementAccesslog
	 * @since 1.0, 2017年10月25日 下午4:35:10, think
	 */
	public void saveAccesslog(AdvertisementAccesslog advertisementAccesslog);

	/**
	 * <pre>
	 * 更新系统广告信息
	 * </pre>
	 * @param advertisement
	 * @param upfile
	 * @since 1.0, 2017年10月25日 下午4:35:23, think
	 */
	public void update(Advertisement advertisement, MultipartFile upfile);

	/**
	 * <pre>
	 * 点击统计
	 * </pre>
	 * @param id
	 * @param ip
	 * @param address
	 * @param iip
	 * @since 1.0, 2017年10月25日 下午4:35:29, think
	 */
	public void statistics(Long id, String ip, String address, String iip);

	/**
	 * <pre>
	 * 删除系统广告
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月25日 下午4:35:16, think
	 */
	public void delete(Long id);
}
