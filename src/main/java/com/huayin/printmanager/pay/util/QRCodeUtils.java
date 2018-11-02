package com.huayin.printmanager.pay.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 生成二维码
 * @author mys
 *
 */
public class QRCodeUtils {
	
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String generateQrcode(String basePath,String codeurl) {
        File foldler = new File(basePath);
        if(!foldler.exists()) {
            foldler.mkdirs();
        }
        String f_name = UUID.randomUUID() + ".png";
        File f = new File(basePath, f_name);
        FileOutputStream fio = null;
        try {
        	fio = new FileOutputStream(f);
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map hints = Maps.newHashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); //设置字符集编码类型
            BitMatrix bitMatrix = null;
            bitMatrix = multiFormatWriter.encode(codeurl, BarcodeFormat.QR_CODE, 300, 300,hints);
            BufferedImage image = toBufferedImage(bitMatrix);
            //输出二维码图片流
            ImageIO.write(image, "png", fio);
            return (f_name);
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        } finally {
        	try {
        		if(null != fio )
        			fio.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
        }
    }
    
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
}
