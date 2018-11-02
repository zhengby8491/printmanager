/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月2日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.basic;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.i18n.ResourceBundleMessageSource;
import com.huayin.printmanager.i18n.service.BasicI18nResource;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.UpdateNameType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 产品信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月2日
 * @since        2.0, 2018年1月2日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/product")
public class ProductController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 产品信息列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:37:30, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:product:list")
	public String list()
	{
		return "basic/product/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:37:23, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<Product> ajaxList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getProductService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 产品新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:37:17, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:product:create")
	public String create(ModelMap map)
	{
		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.PRODUCT));
		return "basic/product/create";
	}

	/**
	 * <pre>
	 * 页面 - 产品复制
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:37:10, think
	 */
	@RequestMapping(value = "copyCreate/{id}")
	@RequiresPermissions("basic:product:copyCreate")
	public String copyCreate(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Product product = serviceFactory.getProductService().get(id);

		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.PRODUCT));
		map.put("product", product);
		return "basic/product/copyCreate";
	}

	/**
	 * <pre>
	 * 页面 - 新增产品
	 * </pre>
	 * @param product
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:37:04, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:product:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "产品信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Product product, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(product.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Product obj = serviceFactory.getProductService().getByName(product.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		if (!Validate.validateObjectsNullOrEmpty(product.getCustomerMaterialCode()))
		{
			Product _obj = serviceFactory.getProductService().getByCustomerMaterialCode(product.getCustomerMaterialCode());
			if (!Validate.validateObjectsNullOrEmpty(_obj))
			{
				return returnErrorBody(BasicI18nResource.PRODUCT_VALIDATE_CUSTOMER_CODE_EXIST);
			}
		}
		serviceFactory.getProductService().save(product);
		UserUtils.clearCacheBasic(BasicType.PRODUCT);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 产品修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:37:41, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:product:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Product product = serviceFactory.getProductService().get(id);
		map.put("product", product);
		return "basic/product/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改产品
	 * </pre>
	 * @param product
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:37:53, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:product:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "产品信息", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody Product product, ModelMap map)
	{
		Product _product = serviceFactory.getProductService().get(product.getId());
		Product obj = serviceFactory.getProductService().getByName(product.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_product.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		if (!Validate.validateObjectsNullOrEmpty(product.getCustomerMaterialCode()))
		{
			Product _obj = serviceFactory.getProductService().getByCustomerMaterialCode(product.getCustomerMaterialCode());
			if (!Validate.validateObjectsNullOrEmpty(_obj))
			{
				if (_obj.getId().longValue() != _product.getId().longValue())
				{
					return returnErrorBody(BasicI18nResource.PRODUCT_VALIDATE_CUSTOMER_CODE_EXIST);
				}
			}
		}
		_product.setCode(product.getCode());
		_product.setName(product.getName());
		_product.setProductClassId(product.getProductClassId());
		_product.setSpecifications(product.getSpecifications());
		_product.setCustomerMaterialCode(product.getCustomerMaterialCode());
		_product.setUnitId(product.getUnitId());
		_product.setSalePrice(product.getSalePrice());
		_product.setWeight(product.getWeight());
		_product.setShelfLife(product.getShelfLife());
		_product.setIsPublic(product.getIsPublic());
		_product.setIsValid(product.getIsValid());
		_product.setSort(product.getSort());
		_product.setpNum(product.getpNum());
		_product.setMemo(product.getMemo());
		_product.setCustomerList(product.getCustomerList());
		_product.setUpdateName(UserUtils.getUser().getUserName());
		_product.setUpdateTime(new Date());
		_product.setFileName(product.getFileName());
		serviceFactory.getProductService().update(_product);
		UserUtils.clearCacheBasic(BasicType.PRODUCT);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除产品
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:38:04, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:product:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "产品信息", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			Product product = serviceFactory.getProductService().get(id);
			boolean isSuc = serviceFactory.getCommonService().delete(BasicType.PRODUCT, id);
			if (isSuc)
			{
				// 删除产品的同时删除产品
				delImg(product.getFileName(),null);
			}
			serviceFactory.getProductService().deleteProductCustomerById(id);
			return isSuc;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * 功能 - 批量删除产品
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:38:17, think
	 */
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	@RequiresPermissions("basic:product:batchDelete")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "产品信息", Operation = Operation.DELETE)
	public AjaxResponseBody batchDelete(@RequestParam("ids[]") Long[] ids)
	{
		if (ids.length == 0)
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Product product = new Product();

		for (Long id : ids)
		{
			product = serviceFactory.getProductService().get(id);

			if (serviceFactory.getCommonService().isUsed(BasicType.PRODUCT, id))
			{
				return returnErrorBody(ResourceBundleMessageSource.i18nFormatter(BasicI18nResource.PRODUCT_VALIDATE_MSG1, product.getName()));
			}
			else
			{
				delImg(product.getFileName(),null);
			}
		}
		serviceFactory.getProductService().deleteByIds(ids);
		serviceFactory.getProductService().deleteProductCustomerByIds(ids);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 上传产品图片
	 * </pre>
	 * @param upfile
	 * @param request
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:38:30, think
	 */
	@RequestMapping(value = "upload")
	@ResponseBody
	public Map<String, String> uploadFile(@RequestParam(value = "upfile", required = false) MultipartFile upfile, HttpServletRequest request)
	{
		Map<String, String> map = null;
		try
		{
			map = new HashMap<String, String>();
			String companyId = UserUtils.getCompanyId();
			String fileDirPath = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_BASE_REALPATH).concat(File.separator).concat(companyId).concat(File.separator).concat("productImg");
			String randCode = _genRandomCode(16);
			// 在文件前面加上一个随机码
			String fileName = randCode.concat(upfile.getOriginalFilename());

			new File(fileDirPath).mkdirs();
			File destDir = new File(fileDirPath.concat(File.separator).concat(fileName));
			upfile.transferTo(destDir);
			String prefixUrl = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_URL) + "/" + companyId + "/";
			// 本地测试专用
			// String prefixUrl = "/file/" + companyId + "/";
			map.put("state", "SUCCESS");
			map.put("url", prefixUrl.concat("productImg/").concat(fileName));
			map.put("title", fileName);
			map.put("original", fileName);
		}
		catch (IllegalStateException e)
		{
			map.put("state", I18nResource.UPLOAD_FAIL);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			map.put("state", I18nResource.UPLOAD_FAIL);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * <pre>
	 * 功能 - 删除产品图片
	 * </pre>
	 * @param fileName
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:38:52, think
	 */
	@RequestMapping(value = "delImg")
	@ResponseBody
	public AjaxResponseBody delImg(String fileName, Long id)
	{
		if (fileName != "" && fileName != null)
		{
			String companyId = UserUtils.getCompanyId();
			String fileDirPath = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_BASE_REALPATH).concat(File.separator).concat(companyId).concat(File.separator).concat("productImg");
			File file = new File(fileDirPath.concat(File.separator).concat(fileName));
			logger.info("文件是否存在？" + file.exists());
			if (file.exists())
			{
				file.delete();
			}
			// 如果有传递id过来，就要把产品上的fileName字段设置成null，防止页面上删除图片但是又没有保存引发的bug
			if (null != id )
			{
				Product	product = serviceFactory.getProductService().get(id);
				product.setFileName(null);
				serviceFactory.getProductService().update(product);
			}
			return returnSuccessBody(I18nResource.DELETE_SUCCESS);
		}
		else
		{
			return returnErrorBody(I18nResource.VALIDATE_PIC_NOT_EXIST);
		}
	}

	/**
	 * <pre>
	 * 私有方法，获取一个随机数
	 * </pre>
	 * @param length
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:39:10, think
	 */
	private String _genRandomCode(int length)
	{
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	/**
	 * <pre>
	 * 页面 - 跳转到变更名称页面
	 * </pre>
	 * @param id
	 * @param name
	 * @param code
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月7日 下午3:00:35, zhengby
	 */
	@RequestMapping(value = "updateName/{id}")
	@RequiresPermissions("basic:product:updateName")
	public String updateName(@PathVariable Long id, ModelMap map)
	{
		Product product = serviceFactory.getProductService().get(id);
		map.put("id", id);
		map.put("name", product.getName());
		map.put("code", product.getCode());
		return "basic/product/updateName";
	}
	
	/**
	 * <pre>
	 * 功能 - 全局变更供应商名称
	 * </pre>
	 * @param product
	 * @return
	 * @since 1.0, 2018年2月7日 下午3:00:16, zhengby
	 */
	@RequestMapping(value = "updateName")
	@ResponseBody
	@RequiresPermissions("basic:product:updateName")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "产品信息", Operation = Operation.UPDATE)
	public AjaxResponseBody updateName(@RequestBody Product product)
	{
		if (serviceFactory.getCommonService().isExist(product.getName(), UpdateNameType.PRODUCT))
		{
			return returnErrorBody("已存在该名称");
		}else
		{
			serviceFactory.getCommonService().updateBasicName(product.getId(), product.getName(), UpdateNameType.PRODUCT);
		}
		return returnSuccessBody();
	}
}
