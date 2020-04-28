package com.qiuxs.cuteframework.core.basic.img;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;

/**
 * 二维码生成工具类
 * @author qiuxs
 *
 */
public class QrCodeUtils {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 保存二维码至指定路径
	 * @param toFileName
	 * 	目标路径
	 * @param width
	 * 	二维码图片宽度
	 * @param height
	 * 	二维码图片高度
	 * @param format
	 *  图片格式
	 * @param charset
	 *  内容编码
	 * @param content
	 *  文本内容
	 */
	public static void createQrCode2Path(String toFileName, int width, int height, String format, String charset, String content) {
		try {
			Path path = FileSystems.getDefault().getPath(toFileName);
			MatrixToImageWriter.writeToPath(createMatrix(width, height, charset, content), format, path);// 输出图像
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 输出至指定流
	 * @param out
	 * 	目标流
	 * @param width
	 * 	二维码图片宽度
	 * @param height
	 * 	二维码图片高度
	 * @param format
	 *  图片格式
	 * @param charset
	 *  内容编码
	 * @param content
	 *  文本内容
	 */
	public static void createQrCode2Stream(OutputStream out, int width, int height, String format, String charset, String content) {
		try {
			MatrixToImageWriter.writeToStream(createMatrix(width, height, charset, content), format, out);
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 创建图像矩阵
	 * @param width
	 * @param height
	 * @param charset
	 * @param content
	 * @return
	 * @throws WriterException
	 */
	private static BitMatrix createMatrix(int width, int height, String charset, String content) throws WriterException {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, charset);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
		return bitMatrix;
	}
	
	/**
	 * 创建图像矩阵，设置白边为0
	 * @param width
	 * @param height
	 * @param charset
	 * @param content
	 * @return
	 * @author zhucy  
	 * @throws WriterException
	 */
	private static BitMatrix createMatrixMarginZero(int width, int height, String charset, String content) throws WriterException {
	    Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
	    hints.put(EncodeHintType.CHARACTER_SET, charset);
	    hints.put(EncodeHintType.MARGIN, 0);
	    BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
	            BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
	    return bitMatrix;
	}

	/**
	 * 生成条码图片字节数组。
	 * @param content 要生成条码的内容
	 * @param format 条码格式
	 * @param width 条码宽度
	 * @param height 条码高度
	 * @param logoImage 在二维码中间显示的logo。为null时不显示。
	 * @param needLogoBorder 二维码中间显示的logo是否需要边框。
	 * @return 条码图片的字节数组。（png格式）
	 * @author lsh  
	 * @throws WriterException
	 * @throws IOException
	 */
	public static byte[] genBarcode(String content, BarcodeFormat format, int width, int height,
			InputStream logoImage, boolean needLogoBorder) throws WriterException, IOException {
		//创建图像矩阵
		BitMatrix bitMatrix = createMatrixMarginZero(width, height, Constants.DEFAULT_CHARSET, content);
		
		//将编码的二维码内容转到图片中
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        
        for(int x = 0;x < width; x++){  
            for(int y = 0; y <height; y++){  
                image.setRGB(x, y, bitMatrix.get(x, y) ? BLACK : WHITE);  
            }  
        }  

        if(logoImage != null) { //加入logo。logo长宽最大不超过二维码大小的1/16
	       Graphics2D gs = image.createGraphics();
	       Image img = ImageIO.read(logoImage);
	       
	       if (img != null) {
	    	   //计算logo可以绘制的长宽
	    	   int logoW = img.getWidth(null);
	    	   int logoH = img.getWidth(null);
	    	   logoW = logoW > width / 6 ||  logoW == -1 ? width / 6 : logoW;
	    	   logoH = logoH > height / 6 ||  logoH == -1 ? height / 6 : logoH;
	    	   
	    	   //计算logo绘制的位置
	    	   int logoX = (width - logoW) / 2;
	    	   int logoY = (height - logoH) / 2;
	    	   
	    	   gs.drawImage(img, logoX, logoY, logoW, logoH, Color.WHITE, null);
	    	   
	    	   if (needLogoBorder) {
	    		   //给logo画边框 。 构造一个具有指定线条宽度以及 cap 和 join 风格的默认值的实心 BasicStroke
	    		   gs.setStroke(new BasicStroke(2));
	    		   gs.setColor(Color.DARK_GRAY);
	    		   gs.drawRect(logoX - 1, logoY - 1, logoW + 2, logoH + 2);
	    	   }
	    	   
	    	   gs.dispose();  
	    	   img.flush();  
	       }
        }
        
        //输出图片
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		if(!ImageIO.write(image, "png", bos)) {
			throw new IOException("Could not write an image");    
		}  

		byte[] b = bos.toByteArray();
		bos.close();
		
		return b;
	}
}
