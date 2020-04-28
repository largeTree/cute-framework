package com.qiuxs.cuteframework.core.basic.img;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.qiuxs.cuteframework.core.basic.utils.DateFormatUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

/**
 * 图片处理工具类：<br>
 * 功能：缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印等
 * @author Administrator
 */
public class ImageUtils {

	/**
	 * 几种常见的图片格式
	 */
	public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
	public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
	public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
	public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
	public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop

	/**
	 * 程序入口：用于测试
	 * @param args
	 */
	public static void main(String[] args) {
		//        // 1-缩放图像：
		//        // 方法一：按比例缩放
		//        ImageUtils.scale("e:/abc.jpg", "e:/abc_scale.jpg", 2, true);//测试OK
		//        // 方法二：按高度和宽度缩放
		//        ImageUtils.scale2("e:/abc.jpg", "e:/abc_scale2.jpg", 500, 300, true);//测试OK
		//
		//
		//        // 2-切割图像：
		//        // 方法一：按指定起点坐标和宽高切割
		//        ImageUtils.cut("e:/abc.jpg", "e:/abc_cut.jpg", 0, 0, 400, 400 );//测试OK
		//        // 方法二：指定切片的行数和列数
		//        ImageUtils.cut2("e:/abc.jpg", "e:/", 2, 2 );//测试OK
		//        // 方法三：指定切片的宽度和高度
		//        ImageUtils.cut3("e:/abc.jpg", "e:/", 300, 300 );//测试OK
		//
		//
		//        // 3-图像类型转换：
		//        ImageUtils.convert("e:/abc.jpg", "GIF", "e:/abc_convert.gif");//测试OK
		//
		//
		//        // 4-彩色转黑白：
		//        ImageUtils.gray("e:/abc.jpg", "e:/abc_gray.jpg");//测试OK
		//
		//
		//        // 5-给图片添加文字水印：
		//        // 方法一：
		//        ImageUtils.pressText("我是水印文字","e:/abc.jpg","e:/abc_pressText.jpg","宋体",Font.BOLD,Color.white,80, 0, 0, 0.5f);//测试OK
		//        // 方法二：
		//        ImageUtils.pressText2("我也是水印文字", "e:/abc.jpg","e:/abc_pressText2.jpg", "黑体", 36, Color.white, 80, 0, 0, 0.5f);//测试OK
		//        
		//        // 6-给图片添加图片水印：
		//        ImageUtils.pressImage("e:/abc2.jpg", "e:/abc.jpg","e:/abc_pressImage.jpg", 0, 0, 0.5f);//测试OK
		String sourceFilePath = "C:\\Users\\qiuxs\\Desktop\\imgs\\report.png";
		String waterFilePath = "C:\\Users\\qiuxs\\Desktop\\imgs\\qr.png";
		String saveFilePath = "C:\\Users\\qiuxs\\Desktop\\imgs\\covered2.png";
		Color color = new Color(255, 200, 0);
		ImageUtils.pressImage(waterFilePath, sourceFilePath, saveFilePath, 280, 1296, 1.0f, 518, 515, "有效期至" + DateFormatUtils.format(new Date(), DateFormatUtils.yyyy_MM_dd_HH_mm_ss), 450, 1880, "微软雅黑", Font.PLAIN, 40, color);
	}

	/**
	 * 缩放图像（按比例缩放）
	 * @param srcImageFile 源图像文件地址
	 * @param destImageFile 缩放后的图像地址
	 * @param scale 缩放比例
	 * @param flag 缩放选择:true 放大; false 缩小;
	 * @return 
	 */
	public final static BufferedImage scale(String srcImageFile, String destImageFile, int scale, boolean flag) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
			int width = src.getWidth(); // 得到源图宽
			int height = src.getHeight(); // 得到源图长
			if (flag) {// 放大
				width = width * scale;
				height = height * scale;
			} else {// 缩小
				width = width / scale;
				height = height / scale;
			}
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			BufferedImage target = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = target.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			if (StringUtils.isNotBlank(destImageFile)) {
				ImageIO.write(target, "JPEG", new File(destImageFile));// 输出到文件流
			}
			return target;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 缩放图像（按高度和宽度缩放）
	 * @param srcImageFile 源图像文件地址
	 * @param destImageFile 缩放后的图像地址
	 * @param height 缩放后的高度
	 * @param width 缩放后的宽度
	 * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
	 * @return 
	 */
	public final static Image scale2(String srcImageFile, String destImageFile, int height, int width, boolean bb) {
		try {
			return scale2(ImageIO.read(new File(srcImageFile)), destImageFile, height, width, bb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 缩放图像（按高度和宽度缩放）
	 * @param srcImageFile 源图像
	 * @param destImageFile 缩放后的图像地址
	 * @param height 缩放后的高度
	 * @param width 缩放后的宽度
	 * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
	 * @return 
	 */
	public final static Image scale2(BufferedImage srcImageFile, String destImageFile, int height, int width, boolean bb) {
		try {
			double ratio = 0.0; // 缩放比例
			BufferedImage bi = srcImageFile;
			Image dest = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue()
							/ bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform
						.getScaleInstance(ratio, ratio), null);
				dest = op.filter(bi, null);
			}
			if (bb) {//补白
				BufferedImage image = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == dest.getWidth(null))
					g.drawImage(dest, 0, (height - dest.getHeight(null)) / 2,
							dest.getWidth(null), dest.getHeight(null),
							Color.white, null);
				else
					g.drawImage(dest, (width - dest.getWidth(null)) / 2, 0,
							dest.getWidth(null), dest.getHeight(null),
							Color.white, null);
				g.dispose();
				dest = image;
			}
			if (StringUtils.isNotBlank(destImageFile)) {
				ImageIO.write((BufferedImage) dest, "JPEG", new File(destImageFile));
			}
			return dest;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * @param srcImageFile 源图像地址
	 * @param destImageFile 切片后的图像地址
	 * @param x 目标切片起点坐标X
	 * @param y 目标切片起点坐标Y
	 * @param width 目标切片宽度
	 * @param height 目标切片高度
	 * @return 
	 */
	public final static BufferedImage cut(String srcImageFile, String destImageFile,
			int x, int y, int width, int height) {
		try {
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight,
						Image.SCALE_DEFAULT);
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
				Image img = Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(image.getSource(),
								cropFilter));
				BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics g = dest.getGraphics();
				g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
				g.dispose();
				if (StringUtils.isNotBlank(destImageFile)) {
					// 输出为文件
					ImageIO.write(dest, "JPEG", new File(destImageFile));
				}
				return dest;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 图像切割（指定切片的行数和列数）
	 * @param srcImageFile 源图像地址
	 * @param destDir 切片目标文件夹
	 * @param rows 目标切片行数。默认2，必须是范围 [1, 20] 之内
	 * @param cols 目标切片列数。默认2，必须是范围 [1, 20] 之内
	 * @return 
	 */
	public final static List<BufferedImage> cut2(String srcImageFile, String destDir,
			int rows, int cols) {
		List<BufferedImage> dests = new ArrayList<BufferedImage>();
		try {
			if (rows <= 0 || rows > 20)
				rows = 2; // 切片行数
			if (cols <= 0 || cols > 20) {
				cols = 2; // 切片列数
			}
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				Image img;
				ImageFilter cropFilter;
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int destWidth = srcWidth; // 每张切片的宽度
				int destHeight = srcHeight; // 每张切片的高度
				// 计算切片的宽度和高度
				if (srcWidth % cols == 0) {
					destWidth = srcWidth / cols;
				} else {
					destWidth = (int) Math.floor(srcWidth / cols) + 1;
				}
				if (srcHeight % rows == 0) {
					destHeight = srcHeight / rows;
				} else {
					destHeight = (int) Math.floor(srcWidth / rows) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
								destWidth, destHeight);
						img = Toolkit.getDefaultToolkit().createImage(
								new FilteredImageSource(image.getSource(),
										cropFilter));
						BufferedImage dest = new BufferedImage(destWidth,
								destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = dest.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						if (StringUtils.isNotBlank(destDir)) {
							// 输出为文件
							ImageIO.write(dest, "JPEG", new File(destDir + "_r" + i + "_c" + j + ".jpg"));
						}
						dests.add(dest);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dests;
	}

	/**
	 * 图像切割（指定切片的宽度和高度）
	 * @param srcImageFile 源图像地址
	 * @param destDir 切片目标文件夹
	 * @param destWidth 目标切片宽度。默认200
	 * @param destHeight 目标切片高度。默认150
	 * @return 
	 */
	public final static List<BufferedImage> cut3(String srcImageFile, String destDir, int destWidth, int destHeight) {
		List<BufferedImage> dests = new ArrayList<BufferedImage>();
		try {
			if (destWidth <= 0)
				destWidth = 200; // 切片宽度
			if (destHeight <= 0)
				destHeight = 150; // 切片高度
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > destWidth && srcHeight > destHeight) {
				Image img;
				ImageFilter cropFilter;
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int cols = 0; // 切片横向数量
				int rows = 0; // 切片纵向数量
				// 计算切片的横向和纵向数量
				if (srcWidth % destWidth == 0) {
					cols = srcWidth / destWidth;
				} else {
					cols = (int) Math.floor(srcWidth / destWidth) + 1;
				}
				if (srcHeight % destHeight == 0) {
					rows = srcHeight / destHeight;
				} else {
					rows = (int) Math.floor(srcHeight / destHeight) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
								destWidth, destHeight);
						img = Toolkit.getDefaultToolkit().createImage(
								new FilteredImageSource(image.getSource(),
										cropFilter));
						BufferedImage dest = new BufferedImage(destWidth,
								destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = dest.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						if (StringUtils.isNotBlank(destDir)) {
							// 输出为文件
							ImageIO.write(dest, "JPEG", new File(destDir + "_r" + i + "_c" + j + ".jpg"));
						}
						dests.add(dest);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dests;
	}

	/**
	 * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
	 * @param srcImageFile 源图像地址
	 * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param destImageFile 目标图像地址
	 */
	public final static void convert(String srcImageFile, String formatName, String destImageFile) {
		try {
			File f = new File(srcImageFile);
			if (f.canRead()) {
				BufferedImage src = ImageIO.read(f);
				ImageIO.write(src, formatName, new File(destImageFile));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 彩色转为黑白 
	 * @param srcImageFile 源图像地址
	 * @param destImageFile 目标图像地址
	 * @return 
	 */
	public final static BufferedImage gray(String srcImageFile, String destImageFile) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			BufferedImage dest = op.filter(src, null);
			if (StringUtils.isNotBlank(destImageFile)) {
				ImageIO.write(dest, "JPEG", new File(destImageFile));
			}
			return dest;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 给图片添加文字水印
	 * @param pressText 水印文字
	 * @param srcImageFile 源图像地址
	 * @param destImageFile 目标图像地址
	 * @param fontName 水印的字体名称
	 * @param fontStyle 水印的字体样式
	 * @param color 水印的字体颜色
	 * @param fontSize 水印的字体大小
	 * @param x 修正值
	 * @param y 修正值
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return 
	 */
	public final static BufferedImage pressText(String pressText,
			String srcImageFile, String destImageFile, String fontName,
			int fontStyle, Color color, int fontSize, int x,
			int y, float alpha) {
		try {
			File img = new File(srcImageFile);
			Image src = ImageIO.read(img);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));
			// 在指定坐标绘制水印文字
			g.drawString(pressText, (width - (getLength(pressText) * fontSize))
					/ 2 + x, (height - fontSize) / 2 + y);
			g.dispose();
			if (StringUtils.isNotBlank(destImageFile)) {
				ImageIO.write(image, "JPEG", new File(destImageFile));// 输出到文件流
			}
			return image;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 给图片添加文字水印
	 * @param pressText 水印文字
	 * @param srcImageFile 源图像地址
	 * @param destImageFile 目标图像地址
	 * @param fontName 字体名称
	 * @param fontStyle 字体样式
	 * @param color 字体颜色
	 * @param fontSize 字体大小
	 * @param x 修正值
	 * @param y 修正值
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return 
	 */
	public final static BufferedImage pressText2(String pressText, String srcImageFile, String destImageFile,
			String fontName, int fontStyle, Color color, int fontSize, int x,
			int y, float alpha) {
		try {
			File img = new File(srcImageFile);
			Image src = ImageIO.read(img);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));
			// 在指定坐标绘制水印文字
			g.drawString(pressText, (width - (getLength(pressText) * fontSize))
					/ 2 + x, (height - fontSize) / 2 + y);
			g.dispose();
			if (StringUtils.isNotBlank(destImageFile)) {
				ImageIO.write(image, "JPEG", new File(destImageFile));
			}
			return image;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 给图片添加图片水印
	 * @param pressImg 水印图片
	 * @param srcImageFile 源图像地址
	 * @param destImageFile 目标图像地址
	 * @param x 修正值。 默认在中间
	 * @param y 修正值。 默认在中间
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return 
	 */
	public final static BufferedImage pressImage(String pressImg, String srcImageFile, String destImageFile,
			int x, int y, float alpha) {
		return pressImage(pressImg, srcImageFile, destImageFile, x, y, alpha, 0, 0, (String) null, 0, 0, (String)null, 0, 0, (Color) null);
	}

	public final static BufferedImage pressImage(InputStream pressImg, InputStream srcImage, String destImageFile,
			int x, int y, float alpha, int pressWidth, int pressHeight, String textPress, int strX, int strY, 
			String fontName, int fontStyle, int fontSize, Color color) {
		try {
			return pressImage(ImageIO.read(pressImg), ImageIO.read(srcImage), destImageFile, x, y, alpha, pressWidth, pressHeight, textPress, strX, strY, fontName, fontStyle, fontSize, color);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 添加水印
	 * 支持同时添加文字和图片水印
	 *  
	 * @author qiuxs  
	 * @param pressImg
	 * @param srcImageFile
	 * @param destImageFile
	 * @param x
	 * @param y
	 * @param alpha
	 * @param pressWidth
	 * @param pressHeight
	 * @param textPress
	 * @param strX
	 * @param strY
	 * @param fontSize
	 * @param fontColor
	 * @return
	 */
	public final static BufferedImage pressImage(BufferedImage pressImg, BufferedImage srcImage, String destImageFile,
			int x, int y, float alpha, int pressWidth, int pressHeight, String textPress, int strX, int strY, 
			String fontName, int fontStyle, int fontSize, Color color) {
		try {
			BufferedImage src = srcImage;
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// 水印文件
			Image src_biao;
			if (pressWidth > 0 && pressHeight > 0) {
				src_biao = scale2(pressImg, null, pressHeight, pressWidth, true);
			} else {
				src_biao = pressImg;
			}
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			g.drawImage(src_biao, x, y, wideth_biao, height_biao, null);

			// 文字水印
			if (StringUtils.isNotBlank(textPress)) {
				Font font = new Font(fontName, fontStyle, fontSize);
				if (color != null) {
					g.setColor(color);
				}
				// 字体
				g.setFont(font);
				g.drawString(textPress, strX, strY);
			}

			// 水印文件结束
			g.dispose();
			if (StringUtils.isNotBlank(destImageFile)) {
				ImageIO.write(image, "JPEG", new File(destImageFile));
			}
			return image;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 添加水印
	 * 支持同时添加文字和图片水印
	 *  
	 * @author qiuxs  
	 * @param pressImg
	 * @param srcImageFile
	 * @param destImageFile
	 * @param x
	 * @param y
	 * @param alpha
	 * @param pressWidth
	 * @param pressHeight
	 * @param textPress
	 * @param strX
	 * @param strY
	 * @param fontSize
	 * @param fontColor
	 * @return
	 */
	public final static BufferedImage pressImage(String pressImg, String srcImageFile, String destImageFile,
			int x, int y, float alpha, int pressWidth, int pressHeight, String textPress, int strX, int strY, 
			String fontName, int fontStyle, int fontSize, Color fontColor) {
		try {
			return pressImage(ImageIO.read(new File(pressImg)), ImageIO.read(new File(srcImageFile)), destImageFile, x, y, alpha, pressWidth, pressHeight, textPress, strX, strY, fontName, fontStyle, fontSize, fontColor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 计算text的长度（一个中文算两个字符）
	 * @param text
	 * @return
	 */
	public final static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length / 2;
	}
}