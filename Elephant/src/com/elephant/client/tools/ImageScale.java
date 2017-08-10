package com.elephant.client.tools;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageScale {

	public ImageIcon imageScale(int width, Image img) {
		Image newimg;
		int owidth = img.getWidth(null);
		int oheight = img.getHeight(null);
		newimg = img.getScaledInstance(width, width * oheight / owidth, Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}

}
