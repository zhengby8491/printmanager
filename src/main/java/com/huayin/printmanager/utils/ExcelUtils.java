/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * <pre>
 * 公共 - Excel工具
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class ExcelUtils
{
	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param inputSream
	 * @return
	 * @throws IOException
	 * @since 1.0, 2017年10月26日 上午10:30:57, think
	 */
	public static Workbook initBook(InputStream inputSream) throws IOException
	{
		Workbook book = null;
		try
		{
			book = new XSSFWorkbook(inputSream);
		}
		catch (Exception ex)
		{
			book = new HSSFWorkbook(inputSream);
		}
		return book;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param book
	 * @param pageIndex
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @since 1.0, 2017年10月26日 上午10:31:01, think
	 */
	public static List<ArrayList<String>> readXlsx(Workbook book, Integer pageIndex) throws FileNotFoundException, IOException
	{
		List<ArrayList<String>> sheetList = new ArrayList<ArrayList<String>>();

		Sheet sheet = book.getSheetAt(pageIndex);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		Row row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++)
		{
			row = sheet.getRow(i);
			int j = 0;
			ArrayList<String> rowlist = new ArrayList<String>();
			while (j < colNum)
			{
				rowlist.add(ExcelUtils.getCellFormatValue(row.getCell((short) j)).trim());
				j++;
			}
			sheetList.add(rowlist);
		}
		return sheetList;
	}

	/**
	 * <pre>
	 * 根据Cell类型设置数据
	 * </pre>
	 * @param cell
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:31:03, think
	 */
	private static String getCellFormatValue(Cell cell)
	{
		String cellvalue = "";
		if (cell != null)
		{
			// 判断当前Cell的Type
			switch (cell.getCellType())
			{
				// 如果当前Cell的Type为NUMERIC
				case HSSFCell.CELL_TYPE_NUMERIC:
				case HSSFCell.CELL_TYPE_FORMULA:
				{
					// 判断当前的cell是否为Date
					try
					{
						if (HSSFDateUtil.isCellDateFormatted(cell))
						{
							Date date = cell.getDateCellValue();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
							cellvalue = sdf.format(date);
						}
						// 如果是纯数字
						else
						{
							// 取得当前Cell的数值
							cellvalue = new BigDecimal(cell.getNumericCellValue()).toString();
						}
					}
					catch (IllegalStateException e)
					{
						cellvalue = String.valueOf(cell.getRichStringCellValue());
					}
					break;
				}
					// 如果当前Cell的Type为STRIN
				case HSSFCell.CELL_TYPE_STRING:
					// 取得当前的Cell字符串
					cellvalue = cell.getRichStringCellValue().getString();
					break;
				// 默认的Cell值
				default:
					cellvalue = " ";
			}
		}
		else
		{
			cellvalue = "";
		}
		return cellvalue;

	}
}
