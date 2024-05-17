package com.sotk.managers;

import java.awt.image.BufferedImage; 
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class AssetsManager {
//	private static AssetsManager instance = new AssetsManager();
//	
//	public static AssetsManager getInstance() {
//		return instance;
//	}
	public static BufferedImage loadImage(String path) {
	
		try {
			return ImageIO.read(AssetsManager.class.getResourceAsStream(path));
		} catch (IOException e) {
			System.out.println("Error Reading Image");
			e.printStackTrace();
			System.exit(1);
		}
		return null;
		
	}
	
	public static BufferedImage getMirrorImage(BufferedImage img) {
		int width = img.getWidth();
        int height = img.getHeight();
  
        // BufferedImage for mirror image
        BufferedImage mimg = new BufferedImage(
            width, height, BufferedImage.TYPE_INT_ARGB);
  
        // Create mirror image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int lx = 0, rx = width - 1; lx < width; lx++, rx--) {
                  
                 // lx starts from the left side of the image
                // rx starts from the right side of the
                // image lx is used since we are getting
                // pixel from left side rx is used to set
                // from right side get source pixel value
                int p = img.getRGB(lx, y);
  
                // set mirror image pixel value
                mimg.setRGB(rx, y, p);
            }
        }
        return mimg;
	}
}
