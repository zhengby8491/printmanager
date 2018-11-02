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

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.entity.sys.Advertisement;
import com.huayin.printmanager.persist.entity.sys.AdvertisementAccesslog;
import com.huayin.printmanager.persist.enumerate.AdvertisementType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.AdvertisementService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 广告管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class AdvertisementServiceImpl extends BaseServiceImpl implements AdvertisementService
{
	/**
	 * 广告图片存放路径
	 */
	private final String separator = "advertisement";

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.AdvertisementService#get(java.lang.Long)
	 */
	@Override
	public Advertisement get(Long id)
	{
		DynamicQuery query = new DynamicQuery(Advertisement.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Advertisement.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.AdvertisementService#findByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<Advertisement> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(Advertisement.class);
		query.setIsSearchTotalCount(true);
		/*
		 * if (queryParam.getDateMin() != null) { query.ge("noticeTime", queryParam.getDateMin()); } if
		 * (queryParam.getDateMax() != null) { query.le("noticeTime", queryParam.getDateMax()); }
		 */
		if (StringUtils.isNotEmpty(queryParam.getNoticeTitle()))
		{
			query.like("title", "%" + queryParam.getNoticeTitle() + "%");
		}
		if (queryParam.getPublish() != null)
		{
			query.eq("publish", queryParam.getPublish());
		}
		query.desc("createTime");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Advertisement.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.AdvertisementService#findAccesslogByCondition(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<AdvertisementAccesslog> findAccesslogByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(AdvertisementAccesslog.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		query.eq("advertisementId", queryParam.getId());
		query.desc("createTime");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, AdvertisementAccesslog.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.AdvertisementService#findAllPublish(com.huayin.printmanager.persist.enumerate.
	 * AdvertisementType)
	 */
	@Override
	public List<Advertisement> findAllPublish(AdvertisementType advertisementType)
	{
		DynamicQuery query = new DynamicQuery(Advertisement.class);
		query.eq("publish", BoolValue.YES);
		query.eq("advertisementType", advertisementType);
		query.asc("sort");
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Advertisement.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.AdvertisementService#save(com.huayin.printmanager.persist.entity.sys.
	 * Advertisement, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	@Transactional
	public void save(Advertisement advertisement, MultipartFile upfile)
	{
		try
		{
			String fileDirPath = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_BASE_REALPATH).concat(File.separator).concat(separator);
			String fileName = upfile.getOriginalFilename();
			new File(fileDirPath).mkdirs();
			File destDir = new File(fileDirPath.concat(File.separator).concat(fileName));
			upfile.transferTo(destDir);
			String prefixUrl = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_URL) + "/" + separator + "/" + upfile.getOriginalFilename();
			advertisement.setPhotoUrl(prefixUrl);
			advertisement.setCreateTime(new Date());
			advertisement.setCreateName(UserUtils.getUserName());
			daoFactory.getCommonDao().saveEntity(advertisement);
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.AdvertisementService#saveAccesslog(com.huayin.printmanager.persist.entity.sys.
	 * AdvertisementAccesslog)
	 */
	@Override
	@Transactional
	public void saveAccesslog(AdvertisementAccesslog advertisementAccesslog)
	{
		daoFactory.getCommonDao().saveEntity(advertisementAccesslog);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.AdvertisementService#update(com.huayin.printmanager.persist.entity.sys.
	 * Advertisement, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	@Transactional
	public void update(Advertisement advertisement, MultipartFile upfile)
	{
		try
		{
			Advertisement advertisementTemp = this.get(advertisement.getId());
			if (upfile != null)
			{
				String fileDirPath = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_BASE_REALPATH).concat(File.separator).concat(separator);
				String fileName = upfile.getOriginalFilename();
				new File(fileDirPath).mkdirs();
				File destDir = new File(fileDirPath.concat(File.separator).concat(fileName));
				upfile.transferTo(destDir);
				String prefixUrl = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_URL) + "/" + separator + "/" + upfile.getOriginalFilename();
				advertisementTemp.setPhotoUrl(prefixUrl);
			}
			advertisementTemp.setTitle(advertisement.getTitle());
			advertisementTemp.setLinkedUrl(advertisement.getLinkedUrl());
			advertisementTemp.setPublish(advertisement.getPublish());
			advertisementTemp.setAdvertisementType(advertisement.getAdvertisementType());
			advertisementTemp.setSort(advertisement.getSort());
			daoFactory.getCommonDao().updateEntity(advertisementTemp);
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.AdvertisementService#statistics(java.lang.Long, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void statistics(Long id, String ip, String address, String iip)
	{
		AdvertisementAccesslog advertisementAccesslog = new AdvertisementAccesslog();
		advertisementAccesslog.setIp(ip);
		advertisementAccesslog.setAddress(address);
		advertisementAccesslog.setIip(iip);
		try
		{
			advertisementAccesslog.setCompanyId(UserUtils.getCompanyId());
			advertisementAccesslog.setUserId(UserUtils.getUserId());
			advertisementAccesslog.setUserName(UserUtils.getUserName());
		}
		catch (Exception e)
		{
			advertisementAccesslog.setUserName("游客");
		}
		advertisementAccesslog.setAdvertisementId(id);
		advertisementAccesslog.setCreateTime(new Date());

		Advertisement advertisement = this.get(id);
		advertisement.setClickCount(advertisement.getClickCount() + 1);
		saveAccesslog(advertisementAccesslog);
		daoFactory.getCommonDao().updateEntity(advertisement);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.AdvertisementService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id)
	{
		daoFactory.getCommonDao().deleteByIds(Advertisement.class, id);
	}
}
