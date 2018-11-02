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
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.entity.sys.AgentQuotient;
import com.huayin.printmanager.persist.enumerate.AgentType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.AgentQuotientService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 代理商管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class AgentQuotientServiceImpl extends BaseServiceImpl implements AgentQuotientService
{
	/**
	 * 代理商图片存放路径
	 */
	private final String separator = "agent";

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.AgentQuotientService#get(java.lang.Long)
	 */
	@Override
	public AgentQuotient get(Long id)
	{
		DynamicQuery query = new DynamicQuery(AgentQuotient.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, AgentQuotient.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.AgentQuotientService#findByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<AgentQuotient> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(AgentQuotient.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getName()))
		{
			query.like("name", "%" + queryParam.getName() + "%");
		}
		query.desc("createTime");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, AgentQuotient.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.AgentQuotientService#findAgentQuotient()
	 */
	@Override
	public List<AgentQuotient> findAgentQuotient()
	{
		DynamicQuery query = new DynamicQuery(AgentQuotient.class);
		query.add(Restrictions.or(Restrictions.eq("agentType", AgentType.AGENT), Restrictions.eq("agentType", AgentType.COMPREHESIVE)));
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, AgentQuotient.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.AgentQuotientService#save(com.huayin.printmanager.persist.entity.sys.
	 * AgentQuotient, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	@Transactional
	public void save(AgentQuotient agentQuotient, MultipartFile upfile)
	{
		try
		{
			String fileDirPath = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_BASE_REALPATH).concat(File.separator).concat(separator);
			String fileName = upfile.getOriginalFilename();
			new File(fileDirPath).mkdirs();
			File destDir = new File(fileDirPath.concat(File.separator).concat(fileName));
			upfile.transferTo(destDir);
			String prefixUrl = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_URL) + "/" + separator + "/" + upfile.getOriginalFilename();
			agentQuotient.setPhotoUrl(prefixUrl);
			agentQuotient.setCreateTime(new Date());
			agentQuotient.setCreateName(UserUtils.getUserName());
			daoFactory.getCommonDao().saveEntity(agentQuotient);
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
	 * @see com.huayin.printmanager.service.sys.AgentQuotientService#update(com.huayin.printmanager.persist.entity.sys.
	 * AgentQuotient, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	@Transactional
	public void update(AgentQuotient agentQuotient, MultipartFile upfile)
	{
		try
		{
			AgentQuotient advertisementTemp = this.get(agentQuotient.getId());
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
			advertisementTemp.setName(agentQuotient.getName());
			advertisementTemp.setLinkName(agentQuotient.getLinkName());
			advertisementTemp.setTelNum(agentQuotient.getTelNum());
			advertisementTemp.setAddress(agentQuotient.getAddress());
			advertisementTemp.setArea(agentQuotient.getArea());
			advertisementTemp.setAgentType(agentQuotient.getAgentType());
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
	 * @see com.huayin.printmanager.service.sys.AgentQuotientService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id)
	{
		daoFactory.getCommonDao().deleteByIds(AgentQuotient.class, id);
	}
}
