/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月16日 上午10:04:45
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.offer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.exception.ServiceException;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.helper.service.OfferFormulaHelper;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.persist.entity.offer.OfferOrderQuoteInner;
import com.huayin.printmanager.persist.entity.offer.OfferPart;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * 
 * <pre>
 * 报价控制层
 * </pre>
 * @author       houmaolong
 * @since        1.0, 2016年12月14日
 * @since        2.0, 2017年10月16日, think, 调整规范
 */
@Controller
public class OfferOrderController extends BaseController
{
	private DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();

	/**
	 * <pre>
	 * 功能 - 导出excel
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @param id
	 * @since 1.0, 2017年11月21日 上午11:03:35, zhengby
	 */
	@RequestMapping(value = "${basePath}/offer/viewEXCEL/{id}")
	@RequiresPermissions("offer:order:list:export")
	public void viewEXCEL(HttpServletRequest request, HttpServletResponse response, ModelMap map, @PathVariable Long id)
	{
		try
		{
			logger.info("开始导出报价单");
			OfferOrder offerOrder = serviceFactory.getOfferOrderService().get(id);
			// 导出EXECL时 后道工序无法通过嵌套<fx:forEach>标签输入，只能转换成字符串，再通过字段赋值
			for (OfferPart op : offerOrder.getOfferPartList())
			{
				op.setPartProcedureStr(op.getPartProcedureStr().replaceAll("&nbsp;&nbsp;", "  "));
			}

			// 书刊类装订工序.彩盒类成品工序
			if (offerOrder.getOfferType() == OfferType.ALBUMBOOK || offerOrder.getOfferType() == OfferType.CARTONBOX)
			{
				offerOrder.setProductProcedureStr(offerOrder.getProductProcedureStr().replaceAll("&nbsp;&nbsp;", "  "));
			}
			String fileName = offerOrder.getOfferNo() + ".xlsx";
			String srcFilePath = "";
			if (offerOrder.getOfferType() == OfferType.ALBUMBOOK)
			{
				srcFilePath = request.getSession().getServletContext().getRealPath("/excelTemplate/书刊类报价模板.xlsx");
			}
			else
			{
				srcFilePath = request.getSession().getServletContext().getRealPath("/excelTemplate/包装类报价模板.xlsx");
			}
			XLSTransformer transformer = new XLSTransformer();
			Map<String, OfferOrder> beans = new HashMap<String, OfferOrder>();
			beans.put("offerBean", offerOrder);
			InputStream is = new BufferedInputStream(new FileInputStream(srcFilePath));
			Workbook workbook = transformer.transformXLS(is, beans);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			is.close();
			os.flush();
			os.close();
			logger.info("导出报价单成功");
		}
		catch (IOException e)
		{
			logger.error("导出报价单失败", e);
			throw new ServiceException("导出报价单失败");
		}
		catch (ParsePropertyException e)
		{
			logger.error("导出报价单失败", e);
			throw new ServiceException("导出报价单失败");
		}
		catch (InvalidFormatException e)
		{
			logger.error("导出报价单失败", e);
			throw new ServiceException("导出报价单失败");
		}

	}

	/**
	 * <pre>
	 * 功能 - 导出PDF
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @param id
	 * @since 1.0, 2017年11月21日 上午11:04:19, zhengby
	 */
	@RequestMapping(value = "${basePath}/offer/viewPDF/{id}")
	@RequiresPermissions("offer:order:list:export")
	public void viewPDF(HttpServletRequest request, HttpServletResponse response, ModelMap map, @PathVariable Long id)
	{
		df.applyPattern("##.########");
		response.reset();
		OfferOrder offerOrder = serviceFactory.getOfferOrderService().get(id);
		ByteArrayOutputStream ba = new ByteArrayOutputStream();
		// 代码绘制pdf
		_createPDF(ba, offerOrder);
		response.setHeader("Content-disposition", "attachment; filename=" + offerOrder.getOfferNo() + ".pdf");
		response.setContentLength(ba.size());
		try
		{
			ServletOutputStream out = response.getOutputStream();
			ba.writeTo(out);
			out.flush();
			out.close();
			ba.close();
		}
		catch (IOException e)
		{
			logger.error("下载PDF文件出错了", e);
		}

	}

	/**
	 * <pre>
	 * 动态绘制表格
	 * </pre>
	 * @param ba
	 * @param offerOrder
	 * @since 1.0, 2017年11月15日 上午9:34:59, zhengby
	 */
	private void _createPDF(ByteArrayOutputStream ba, OfferOrder offerOrder)
	{
		try
		{
			Boolean isBook = (offerOrder.getOfferType() == OfferType.ALBUMBOOK) ? true : false;
			// 1-创建文本对象 Document
			Rectangle rectPageSize = new Rectangle(PageSize.A4);// 设置纸张大小
			rectPageSize = rectPageSize.rotate();// 设置为横向
			Document document = new Document(rectPageSize, 2, 2, 20, 20);

			// 2-初始化 pdf输出对象 PdfWriter
			PdfWriter.getInstance(document, ba);

			// 3-打开 Document
			document.open();

			// 4-往 Document 添加内容
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			// Font titleFont = new Font(bfChinese, 16, Font.BOLD);// 标题
			Font tableTitleFont = new Font(bfChinese, 12, Font.BOLD);// 表格标题
			// Font conyentFont = new Font(bfChinese, 10, Font.NORMAL);// 内容

			PdfPTable table = new PdfPTable(isBook ? 14 : 12); // 设置表格列数
			table.setWidthPercentage(95); // 设置表格宽度百分比
			// 定义一个表格单元
			// 标题
			String offerTypeName = offerOrder.getProductName() + "-报价单";
			PdfPCell cell = new PdfPCell(new Paragraph(offerTypeName, tableTitleFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setColspan(isBook ? 14 : 12);// 定义一个表格单元的跨度
			cell.setFixedHeight(24f);
			table.addCell(cell);
			// -------------------------------- 第一行 -----------------------------
			_createCell(table, 2, 1, "成品信息");
			_createCell(table, 1, "客户名称");
			_createCell(table, 1, "客户地址");
			_createCell(table, 1, "联系电话");
			_createCell(table, isBook ? 1 : 2, "成品名称");
			_createCell(table, isBook ? 2 : 1, "成品规格");
			_createCell(table, isBook ? 2 : 1, "报价数量");
			_createCell(table, isBook ? 2 : 1, "报价单号");
			_createCell(table, 1, "交货日期");
			_createCell(table, 1, "报价人");
			_createCell(table, 1, "报价日期");

			_createCell(table, 1, offerOrder.getCustomerName());
			_createCell(table, 1, offerOrder.getLinkAddress());
			String linkName = offerOrder.getLinkName() + offerOrder.getPhone();
			_createCell(table, 1, linkName);
			_createCell(table, isBook ? 1 : 2, offerOrder.getProductName());
			_createCell(table, isBook ? 2 : 1, offerOrder.getSpecification());
			_createCell(table, isBook ? 2 : 1, offerOrder.getAmount().toString());
			_createCell(table, isBook ? 2 : 1, offerOrder.getOfferNo());
			_createCell(table, 1, offerOrder.getDeliveryDateStr());
			_createCell(table, 1, offerOrder.getCreateName());
			_createCell(table, 1, offerOrder.getCreateDateTimeStr());

			// -------------------------------- 部件--------------------------
			for (OfferPart offerPart : offerOrder.getOfferPartList())
			{
				Integer span = 5;
				if (BoolValue.YES == offerPart.getContainBflute())
				{
					span = 6;
				}
				_createCell(table, span, 1, offerPart.getPartName());
				if (isBook)
				{
					_createCell(table, 1, "P数");
				}
				_createCell(table, 1, "印刷机");
				_createCell(table, 1, "上机规格");
				_createCell(table, 1, "印刷方式");
				_createCell(table, 1, "正反普色");
				_createCell(table, 1, "正反专色");
				_createCell(table, 1, "印版付数");
				_createCell(table, 1, "印版张数");
				_createCell(table, 1, isBook ? "单面拼数" : "拼版数");
				if (isBook)
				{
					_createCell(table, 1, "手/贴数");
				}
				_createCell(table, 1, "印张正数");
				_createCell(table, 1, "放损数");
				_createCell(table, 1, "总印张");

				if (isBook)
				{
					_createCell(table, 1, offerPart.getPages().toString());
				}
				_createCell(table, 1, offerPart.getMachineName());
				_createCell(table, 1, offerPart.getMachineSpec());
				_createCell(table, 1, offerPart.getOfferPrintStyleType().getText());
				_createCell(table, 1, offerPart.getProsConsColor());
				_createCell(table, 1, offerPart.getProsConsSpot());
				if (isBook)
				{
					_createCell(table, 1, String.valueOf(offerPart.getOfferPrintStyleType().getValue() * offerPart.getThread()));
				}
				else
				{
					_createCell(table, 1, offerPart.getOfferPrintStyleType().getValue().toString());
				}

				_createCell(table, 1, offerPart.getSheetZQ().toString());
				_createCell(table, 1, offerPart.getSheetNum().toString());
				if (isBook)
				{
					_createCell(table, 1, offerPart.getThread().toString());
				}
				_createCell(table, 1, offerPart.getImpositionNum().toString());
				_createCell(table, 1, offerPart.getWaste().toString());
				_createCell(table, 1, offerPart.getPaperTotal().toString());

				_createCell(table, 1, "材料分类");
				_createCell(table, 1, "材料名称");
				_createCell(table, 1, "材料规格");
				_createCell(table, isBook ? 1 : 2, "克重");
				_createCell(table, isBook ? 2 : 1, "上机数量");
				_createCell(table, isBook ? 2 : 1, "材料开数");
				_createCell(table, isBook ? 2 : 1, "材料数量");
				_createCell(table, 1, "计价数量");
				_createCell(table, 1, "材料单价");
				_createCell(table, 1, "材料金额");

				_createCell(table, 1, offerPart.getPaperName());
				_createCell(table, 1, offerPart.getPaperName());
				_createCell(table, 1, offerPart.getPaperType().getStyle());
				_createCell(table, isBook ? 1 : 2, offerPart.getPaperWeight().toString());
				_createCell(table, isBook ? 2 : 1, offerPart.getPaperTotal().toString());
				_createCell(table, isBook ? 2 : 1, offerPart.getMaterialOpening().toString());
				_createCell(table, isBook ? 2 : 1, offerPart.getMaterialAmount().toString());
				_createCell(table, 1, offerPart.getCalNum().toString());
				_createCell(table, 1, OfferFormulaHelper.scale(offerPart.getPaperTonPrice().doubleValue()));
				String materialMoney = OfferFormulaHelper.scale(offerPart.getPaperTonPrice().doubleValue() * offerPart.getCalNum().floatValue());
				_createCell(table, 1, materialMoney);

				if (BoolValue.YES == offerPart.getContainBflute())
				{
					_createCell(table, 1, offerPart.getBflutePit());
					_createCell(table, 1, offerPart.getBflutePaperQuality());
					_createCell(table, 1, offerPart.getMachineSpec());
					_createCell(table, 2, "");
					_createCell(table, 1, offerPart.getBfluteNum().toString());
					_createCell(table, 1, "1");
					_createCell(table, 1, offerPart.getBfluteNum().toString());
					_createCell(table, 1, offerPart.getBfluteCalNum().toString());
					_createCell(table, 1, offerPart.getBflutePrice().toString());
					String materialMoney2 = OfferFormulaHelper.scale(offerPart.getBflutePrice().doubleValue() * offerPart.getBfluteCalNum().floatValue());
					_createCell(table, 1, materialMoney2);
				}
				// 后道工序
				_createCell(table, 1, "后道工序");
				String partProcedureStr = "";
				if (null != offerPart.getPartProcedureStr())
				{
					partProcedureStr = offerPart.getPartProcedureStr().replaceAll("&nbsp;", "  ");
				}
				_createCell(table, 0, isBook ? 12 : 10, partProcedureStr, Element.ALIGN_LEFT);
			}
			// ------------------------------------成品工序-----------------------------------
			_createCell(table, 1, "成品工序");
			_createCell(table, 1, "成品工序");
			String productProcedureStr = "";
			if (null != offerOrder.getProductProcedureStr())
			{
				productProcedureStr = offerOrder.getProductProcedureStr().replaceAll("&nbsp;", "  ");
			}
			_createCell(table, 0, isBook ? 12 : 10, productProcedureStr, Element.ALIGN_LEFT);

			// -----------------------------------阶梯数据-----------------------------------
			_createCell(table, offerOrder.getLadderCol() + 1, 1, "阶梯数据");
			_createCell(table, 1, "数量");
			_createCell(table, 1, "纸张费");
			_createCell(table, 1, "印刷费");
			_createCell(table, 1, "工序费");
			_createCell(table, 1, "其他费");
			_createCell(table, 1, "运费");
			_createCell(table, 1, "成本金额");
			_createCell(table, 1, "利润");
			_createCell(table, 1, "未税金额");
			_createCell(table, isBook ? 2 : 1, "含税金额");
			_createCell(table, isBook ? 2 : 1, "含税单价");
			for (OfferOrderQuoteInner innerList : offerOrder.getOfferOrderQuoteInnerList())
			{
				_createCell(table, 1, OfferFormulaHelper.scale(innerList.getAmount()));
				_createCell(table, 1, OfferFormulaHelper.scale(innerList.getPaperFee()));
				_createCell(table, 1, OfferFormulaHelper.scale(innerList.getPrintFee()));
				_createCell(table, 1, OfferFormulaHelper.scale(innerList.getProcedureFee()));
				_createCell(table, 1, OfferFormulaHelper.scale(innerList.getOhterFee()));
				_createCell(table, 1, OfferFormulaHelper.scale(innerList.getFreightFee()));
				_createCell(table, 1, OfferFormulaHelper.scale(innerList.getCostMoney()));
				_createCell(table, 1, OfferFormulaHelper.scale(innerList.getProfitFee()));
				_createCell(table, 1, OfferFormulaHelper.scale(innerList.getUntaxedFee()));
				_createCell(table, isBook ? 2 : 1, OfferFormulaHelper.scale(innerList.getTaxFee()));
				_createCell(table, isBook ? 2 : 1, StringUtils.toString(OfferFormulaHelper.scale(innerList.getTaxPrice(),4)));
			}
			document.add(table);// 将表格添加到文档中

			// 5-关闭 Document
			document.close();
		}
		catch (DocumentException de)
		{
			logger.error("创建PDF文件出错了", de);
		}
		catch (IOException e)
		{
			logger.error("打开PDF模板文件出错了", e);
		}
		catch (Exception e)
		{
			logger.error("打开PDF模板文件出错了", e);
		}
	}

	/**
	 * <pre>
	 * 绘制单元格公共方法
	 * </pre>
	 * @param table
	 * @param colspan
	 * @param field
	 * @param font
	 * @since 1.0, 2017年11月14日 下午4:21:54, zhengby
	 */
	private void _createCell(PdfPTable table, int colspan, String field)
	{
		_createCell(table, 0, colspan, field, -1);
	}

	/**
	 * <pre>
	 * 绘制单元格公共方法(跨行)
	 * </pre>
	 * @param table
	 * @param rowspan
	 * @param colspan
	 * @param field
	 * @param font
	 * @since 1.0, 2017年11月14日 下午4:44:41, zhengby
	 */
	private void _createCell(PdfPTable table, int rowspan, int colspan, String field)
	{
		_createCell(table, rowspan, colspan, field, -1);
	}

	private void _createCell(PdfPTable table, int rowspan, int colspan, String field, int element)
	{
		try
		{
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font conyentFont = new Font(bfChinese, 10, Font.NORMAL);
			PdfPCell cell = new PdfPCell(new Paragraph(field, conyentFont));
			if (-1 != element)
			{
				cell.setHorizontalAlignment(element);
			}
			else
			{
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			}
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setColspan(colspan);
			if (0 < rowspan)
			{
				cell.setRowspan(rowspan);
			}
			cell.setFixedHeight(24f);
			table.addCell(cell);
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * <pre>
	 * 页面 - 查看报价单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2017年11月21日 上午11:10:07, zhengby
	 */
	@RequestMapping(value = "${basePath}/offer/view/{id}")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}

		try
		{
			OfferOrder offerBean = serviceFactory.getOfferOrderService().get(id);
			// 针对微信端保存的报价单
			map.put("offerBean", offerBean);
			if (offerBean.getOfferType() == OfferType.ALBUMBOOK)
			{
				return "offer/order/viewBook";
			}

			return "offer/order/viewPack";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}
	
	/**
	 * <pre>
	 * 页面 - 查看报价单
	 * </pre>
	 * @param request
	 * @param map
	 * @param offerNo
	 * @return
	 * @since 1.0, 2018年2月28日 下午4:28:21, think
	 */
	@RequestMapping(value = "${basePath}/offer/view/no/{offerNo}")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable String offerNo)
	{
		if (Validate.validateObjectsNullOrEmpty(offerNo))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}

		try
		{
			OfferOrder offerBean = serviceFactory.getOfferOrderService().get(offerNo);
			// 针对微信端保存的报价单
			map.put("offerBean", offerBean);
			if (offerBean.getOfferType() == OfferType.ALBUMBOOK)
			{
				return "offer/order/viewBook";
			}

			return "offer/order/viewPack";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * <pre>
	 * 功能 -获取 报价单详情
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年11月21日 上午11:07:15, zhengby
	 */
	@RequestMapping(value = "${basePath}/offer/getOfferBean/{id}")
	@ResponseBody
	public AjaxResponseBody getOfferBean(@PathVariable Long id)
	{
		try
		{
			OfferOrder offerBean = serviceFactory.getOfferOrderService().get(id);
			return returnSuccessBody(offerBean);
		}
		catch (Exception e)
		{
			logger.error("查询后台数据发生异常", e);
			return returnErrorBody("查询后台数据发生异常!");
		}
	}

	/**
	 * <pre>
	 * 功能 - 删除
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年11月21日 上午11:06:57, zhengby
	 */
	@RequestMapping(value = "${basePath}/offer/deleteOfferBean/{id}")
	@ResponseBody
	public AjaxResponseBody deleteOfferBean(@PathVariable Long id)
	{
		serviceFactory.getOfferOrderService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 报价单据列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午11:06:12, zhengby
	 */
	@RequestMapping(value = "${basePath}/offer/list")
	public String list()
	{
		return "offer/order/list";
	}

	/**
	 * <pre>
	 * 功能 - 报价单据列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月21日 上午11:05:54, zhengby
	 */
	@RequestMapping(value = "${basePath}/offer/ajaxList")
	@ResponseBody
	public SearchResult<OfferOrder> ajaxList(@RequestBody QueryParam queryParam)
	{
		try
		{
			SearchResult<OfferOrder> result = serviceFactory.getOfferOrderService().findByCondition(queryParam);
			// 查询所有对内报价，并转为map（<orderId, <amount, OfferOrderQuoteInner>>）
			Map<Long, Map<Integer, OfferOrderQuoteInner>> latestQuoteInner = Maps.newHashMap();
			List<OfferOrderQuoteInner> allQuoteInner = serviceFactory.getOfferOrderService().findAllQuoteInner();
			for(OfferOrderQuoteInner inner : allQuoteInner)
			{
				Map<Integer, OfferOrderQuoteInner> m = latestQuoteInner.get(inner.getMasterId());
				if(m == null)
				{
					m = Maps.newHashMap();
					latestQuoteInner.put(inner.getMasterId(), m);
				}
				m.put(inner.getAmount(), inner);
			}
			// 获取数量的含税金额和含税单价
			for(OfferOrder offerOrder : result.getResult())
			{
				Map<Integer, OfferOrderQuoteInner> m = latestQuoteInner.get(offerOrder.getId());
				double price = 0;
				double fee = 0;
				if(m != null)
				{
					OfferOrderQuoteInner inner = m.get(offerOrder.getAmount());
					if(null != inner)
					{
						price = inner.getTaxPrice();
						fee = inner.getTaxFee();
					}
				}
				offerOrder.setUnitPrice(new BigDecimal(price).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
				offerOrder.setCostMoney(new BigDecimal(fee).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <pre>
	 * 功能 - 审核报价订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月7日 上午9:24:17, think
	 */
	@RequestMapping(value = "${basePath}/offer/check/{id}")
	@ResponseBody
	@RequiresPermissions("offer:order:audit")
	public AjaxResponseBody check(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		serviceFactory.getOfferOrderService().check(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 反审核报价订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月28日 上午11:55:17, think
	 */
	@RequestMapping(value = "${basePath}/offer/checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("offer:order:audit:cancel")
	public AjaxResponseBody checkBack(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		serviceFactory.getOfferOrderService().checkBack(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 强制完工报价订单
	 * </pre>
	 * @param idsstr
	 * @return
	 * @since 1.0, 2018年2月28日 上午11:55:45, think
	 */
	@RequestMapping(value = "${basePath}/offer/complete")
	@ResponseBody
	@RequiresPermissions("sale:offer:complete")
	public AjaxResponseBody complete(@RequestParam("ids[]") List<String> idsstr)
	{
		if (Validate.validateObjectsNullOrEmpty(idsstr))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}

		List<Long> ids = Lists.newArrayList();
		for (int i = 0; i < idsstr.size(); i++)
		{
			ids.add(Long.valueOf(idsstr.get(i)));
		}
		boolean flag = serviceFactory.getOfferOrderService().forceComplete(ids, BoolValue.YES);

		if (flag)
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}
	
	/**
	 * <pre>
	 * 功能 - 取消强制完工报价订单
	 * </pre>
	 * @param idsstr
	 * @return
	 * @since 1.0, 2018年2月28日 下午1:33:24, think
	 */
	@RequestMapping(value = "${basePath}/offer/completeCancel")
	@ResponseBody
	@RequiresPermissions("sale:offer:complete:cancel")
	public AjaxResponseBody completeCancel(@RequestParam("ids[]") List<String> idsstr)
	{
		if (Validate.validateObjectsNullOrEmpty(idsstr))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}

		List<Long> ids = Lists.newArrayList();
		for (int i = 0; i < idsstr.size(); i++)
		{
			ids.add(Long.valueOf(idsstr.get(i)));
		}
		boolean flag = serviceFactory.getOfferOrderService().forceComplete(ids, BoolValue.NO);

		if (flag)
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}
}
