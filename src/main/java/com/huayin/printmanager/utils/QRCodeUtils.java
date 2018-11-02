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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;

/**
 * <pre>
 * 公共 - 二维码生成
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class QRCodeUtils
{
	/**
	 * 图片高
	 */
	public static int HEIGHT = 20;

	/**
	 * 图片宽
	 */
	public static int WEIGHT = 60;

	/**
	 * 根据编号找到图片内容
	 * @param code
	 * @return
	 */
	public static String getQRCodeImgFile2(String code)
	{
		return new File(getDir().concat(code + ".gif")).getPath();
		// 如果图片不存在直接生成图片并返回

	}

	/**
	 * 根据编号找到图片内容
	 * @param code
	 * @return
	 */
	public static BufferedImage getQRCodeImgFile(String code)
	{
		File file = new File(getDir().concat(code + ".gif"));
		// 如果图片不存在直接生成图片并返回
		if (!file.isFile())
		{
			return savaQRCodeImg(code);
		}
		InputStream is = null;
		BufferedImage read = null;
		if (file.exists())
		{
			try
			{
				is = new FileInputStream(file);
				read = ImageIO.read(is);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				read.flush();
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return read;

	}

	/**
	 * 保存图片
	 * @param code
	 */
	public static BufferedImage savaQRCodeImg(String code)
	{
		BufferedImage image = QRCodeCreate(code, WEIGHT, HEIGHT);
		String fileName = code + ".gif";
		new File(getDir()).mkdirs();
		try
		{
			ImageIO.write(image, "gif", new File(getDir() + "/" + fileName));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			image.flush();
		}
		return image;
	}

	/**
	 * 获取二维码图片存放路径
	 * @return
	 */
	public static String getDir()
	{
		return SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_BASE_REALPATH).concat(File.separator).concat(UserUtils.getCompanyId()).concat(File.separator).concat(SysConstants.QRCODE_DIR);
	}

	/**
	 * 创建图片并且返回 url访问地址
	 * @param content
	 * @return
	 * @throws IllegalStateException
	 */
	public static String createUrl(String content) throws IllegalStateException
	{
		if (savaQRCodeImg(content) != null)
		{
			String prefixUrl = SystemConfigUtil.getConfig(SysConstants.ATTACH_FILE_URL) + "/" + UserUtils.getCompanyId() + "/" + SysConstants.QRCODE_DIR + "/";
			return prefixUrl.concat(content + ".gif");
		}
		;
		return "";
	}

	/**
	 * 
	 * @param content
	 * @return
	 * @throws IllegalStateException
	 */
	public static String createLocalUrl(String content) throws IllegalStateException
	{
		HttpServletRequest request = ServletUtils.getRequest();
		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath() + "print/" + "public/qrcode/" + content;
		return url;
	}

	/**
	 * @Title: QRCodeCreate 
	 * @Description: 生成二维码
	 * @param @param content
	 * @param @param W
	 * @param @param H
	 * @param @return    设定文件 
	 * @return BufferedImage    返回类型 
	 * @throws
	 */
	public static BufferedImage QRCodeCreate(String content, Integer W, Integer H)
	{
		// 生成二维码
		MultiFormatWriter mfw = new MultiFormatWriter();
		BitMatrix bitMatrix = null;
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);
		try
		{
			bitMatrix = mfw.encode(content, BarcodeFormat.CODE_39, W, H, hints);
		}
		catch (WriterException e)
		{
			e.printStackTrace();
		}
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		return image;
	}

}
