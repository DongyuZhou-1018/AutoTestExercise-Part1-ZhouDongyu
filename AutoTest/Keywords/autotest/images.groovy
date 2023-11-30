package autotest

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


import internal.GlobalVariable

/**
 * Refered from internet, the copyright as below
 * @BelongsProject: maven-demo
 * @BelongsPackage: com.aliyun.picture.demo
 * @Author: Guoyh
 * @CreateTime: 2018-10-12 15:25
 * @Description: 对比图片相似度
 */

public class images {
	@Keyword
	public Double imageComparison(InputStream sampleInputStream,InputStream contrastInputStream ) throws IOException {
		//
		int[] photoArrayTwo = getPhotoArray(contrastInputStream);
		int[] photoArrayOne = getPhotoArray(sampleInputStream);

		//
		int hammingDistance = getHammingDistance(photoArrayOne, photoArrayTwo);
		// [0.0, 1.0]
		double similarity = calSimilarity(hammingDistance);

		//
		return  similarity;
	}


	public static BufferedImage convertToBufferedFrom(Image srcImage) {
		BufferedImage bufferedImage = new BufferedImage(srcImage.getWidth(null),
				srcImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(srcImage, null, null);
		g.dispose();
		return bufferedImage;
	}

	public static BufferedImage toGrayscale(Image image) {
		BufferedImage sourceBuffered = convertToBufferedFrom(image);
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(cs, null);
		BufferedImage grayBuffered = op.filter(sourceBuffered, null);
		return grayBuffered;
	}

	// 32x32像素缩略图
	public static Image scale(Image image) {
		image = image.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		return image;
	}

	//
	public static int[] getPixels(Image image) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		int[] pixels = convertToBufferedFrom(image).getRGB(0, 0, width, height,
				null, 0, width);
		return pixels;
	}

	public static int getAverageOfPixelArray(int[] pixels) {
		Color color;
		long sumRed = 0;
		for (int i = 0; i < pixels.length; i++) {
			color = new Color(pixels[i], true);
			sumRed += color.getRed();
		}
		int averageRed = (int) (sumRed / pixels.length);
		return averageRed;
	}

	//
	public static int[] getPixelDeviateWeightsArray(int[] pixels, final int averageColor) {
		Color color;
		int[] dest = new int[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			color = new Color(pixels[i], true);
			dest[i] = color.getRed() - averageColor > 0 ? 1 : 0;
		}
		return dest;
	}

	// 获取两个缩略图的平均像素比较数组的汉明距离（距离越大差异越大）
	public static int getHammingDistance(int[] a, int[] b) {
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i] == b[i] ? 0 : 1;
		}
		return sum;
	}

	//获取灰度像素的比较数组
	public static int[] getPhotoArray(InputStream inputStream) throws IOException {
		Image image = ImageIO.read(inputStream);
		//        Image image = ImageIO.read(imageFile);
		// 转换至灰度
		image = toGrayscale(image);
		// 缩小成32x32的缩略图
		image = scale(image);
		// 获取灰度像素数组
		int[] pixels = getPixels(image);
		// 获取平均灰度颜色
		int averageColor = getAverageOfPixelArray(pixels);
		// 获取灰度像素的比较数组（即图像指纹序列）
		pixels = getPixelDeviateWeightsArray(pixels, averageColor);

		return pixels;
	}

	// 通过汉明距离计算相似度
	public static double calSimilarity(int hammingDistance) {
		int length = 32 * 32;
		double similarity = (length - hammingDistance) / (double) length;

		// 使用指数曲线调整相似度结果
		similarity = java.lang.Math.pow(similarity, 2);
		return similarity;
	}


	/**
	 * @param args
	 * @return void
	 * @author Guoyh
	 * @date 2018/10/12 15:27
	 */
//	public static void main(String[] args) throws Exception {
//
//		//(数据类型)(最小值+Math.random()*(最大值-最小值+1))
//		for (int i = 18; i <= 21; i++) {
//			Double imageComparison =  imageComparison(new FileInputStream("G:/oss/pk/" + 18 + ".jpg"),new FileInputStream("G:/oss/pk/" +i + ".jpg"));
//			System.out.println("\t" + "\t"+"G:/oss/pk/" + 18 + ".jpg"+"\t"+"与"+"\t"+"G:/oss/pk/" + i + ".jpg"+"\t"+"两张图片的相似度为：" + imageComparison * 100 + "%");
//		}
//	}
}
