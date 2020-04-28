package com.qiuxs.cuteframework.core.basic.img;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import com.qiuxs.cuteframework.core.basic.utils.DateFormatUtils;

public class ImageHandler {
	/**
	 * 
	 * @Title: 构造图片
	 * @Description: 生成水印并返回java.awt.image.BufferedImage
	 * @param file
	 *            源文件(图片)
	 * @param waterFile
	 *            水印文件(图片)
	 * @param x
	 *            距离右下角的X偏移量
	 * @param y
	 *            距离右下角的Y偏移量
	 * @param alpha
	 *            透明度, 选择值从0.0~1.0: 完全透明~完全不透明
	 * @return BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage watermark(File file, File waterFile, int x, int y, float alpha, 
			String str, int strX, int strY, int fontSize) throws IOException {
		// 获取底图
		BufferedImage buffImg = ImageIO.read(file);
		// 获取层图
		BufferedImage waterImg = ImageIO.read(waterFile);
		// 缩放图层
		waterImg = zoomOutImage(waterImg, 518, 515);
		// 创建Graphics2D对象，用在底图对象上绘图
		Graphics2D g2d = buffImg.createGraphics();
		int waterImgWidth = waterImg.getWidth();// 获取层图的宽度
		int waterImgHeight = waterImg.getHeight();// 获取层图的高度
		// 在图形和图像中实现混合和透明效果
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		// 绘制
		g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
		Font font = new Font("微软雅黑", Font.PLAIN, fontSize);
		
		Color color = new Color(255, 200, 0);
		g2d.setColor(color);
		
		// 字体
		g2d.setFont(font);
		g2d.drawString(str, strX, strY);
		
		g2d.dispose();// 释放图形上下文使用的系统资源
		return buffImg;
	}

	/**
	 * 缩放图片
	 *  
	 * @author qiuxs  
	 * @param originalImage
	 * @param targeWidth
	 * @param targetHeight
	 * @return
	 */
	public static BufferedImage zoomOutImage(BufferedImage originalImage, int targeWidth, int targetHeight) {
		BufferedImage newImage = new BufferedImage(targeWidth, targetHeight, originalImage.getType());
		Graphics g = newImage.getGraphics();
		g.drawImage(originalImage, 0, 0, targeWidth, targetHeight, null);
		g.dispose();
		return newImage;
	}

	/**
	 * 输出水印图片
	 * 
	 * @param buffImg
	 *            图像加水印之后的BufferedImage对象
	 * @param savePath
	 *            图像加水印之后的保存路径
	 */
	private static void generateWaterFile(BufferedImage buffImg, String savePath) {
		int temp = savePath.lastIndexOf(".") + 1;
		try {
			ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 *             IO异常直接抛出了
	 * @author bls
	 */
	public static void main(String[] args) throws IOException {
		String sourceFilePath = "C:\\Users\\qiuxs\\Desktop\\imgs\\report.png";
		String waterFilePath = "C:\\Users\\qiuxs\\Desktop\\imgs\\qr.png";
		String saveFilePath = "C:\\Users\\qiuxs\\Desktop\\imgs\\covered.png";
		// 构建叠加层
		BufferedImage buffImg = ImageHandler.watermark(new File(sourceFilePath), new File(waterFilePath), 280, 1296, 1.0f, "有效期至：" + DateFormatUtils.formatTime(new Date()), 450, 1880, 40);
		// 输出水印图片
		ImageHandler.generateWaterFile(buffImg, saveFilePath);
	}
}