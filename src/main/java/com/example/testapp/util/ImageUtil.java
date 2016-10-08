/**
 * 
 */
package com.example.testapp.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.example.testapp.exception.InternalErrorException;

/**
 * @author cure
 *
 */
public class ImageUtil {

	/**
	 * 
	 * @param origImage
	 * @param newMinSize the length of the shorter side of the rectangle
	 * @param cropToSquare whether to crop the image to be a square image (centered)
	 * @return
	 */
	public static Image resizeImage(InputStream origImage, int newMinSize, boolean cropToSquare) {
		try {
			BufferedImage inputImage = ImageIO.read(origImage);
			int width = inputImage.getWidth();
			int height = inputImage.getHeight();
			int newWidth = 0;
			int newHeight = 0;
			if (width >= height) {
				if (height <= newMinSize) {
					return inputImage; // don't scale up
				}
				newHeight = newMinSize;
				newWidth = width * newHeight / height;
			} else {
				if (width <= newMinSize) {
					return inputImage; // don't scale up
				}
				newWidth = newMinSize;
				newHeight = height * newWidth / width;
			}
			BufferedImage outputImage = resizeImageWithHint(inputImage, newWidth, newHeight, inputImage.getType());
			if (cropToSquare) {
				if (newWidth < newHeight) {
					
				}
				int sideLength = newWidth < newHeight ? newWidth : newHeight;
				outputImage = outputImage.getSubimage((newWidth - sideLength)/2, (newHeight - sideLength)/2, sideLength, sideLength);
			}
			return outputImage;
		} catch (IOException e) {
			throw new InternalErrorException("Failed to scale image", e);
		}		
	}
	
	private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int width, int height, int type) {

		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		return resizedImage;
	}	
}
