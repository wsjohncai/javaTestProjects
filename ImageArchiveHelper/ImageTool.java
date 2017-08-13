import java.awt.Image;
import javax.swing.ImageIcon;

public class ImageTool {

	private Image img;
	private int img_wid, con_wid;
	private int img_hgh, con_hgh;

	public ImageTool(String name, int width, int height) {
		ImageIcon icon = new ImageIcon(name);
		img = icon.getImage();
		img_wid = icon.getIconWidth();
		img_hgh = icon.getIconHeight();
		con_wid = width;
		con_hgh = height;
	}

	public ImageIcon getImageIcon() {
		Image newimg = img;
		if (img_wid > con_wid || img_hgh > con_hgh) {
			if (img_wid >= img_hgh)
				newimg = img.getScaledInstance(con_wid, con_wid * img_hgh / img_wid, Image.SCALE_SMOOTH);
			else
				newimg = img.getScaledInstance(con_hgh * img_wid / img_hgh, con_hgh, Image.SCALE_SMOOTH);
		}
		return new ImageIcon(newimg);
	}
	
	public ImageIcon getScaledIcon() {
		Image newimg = img;
		if (img_wid >= img_hgh)
			newimg = img.getScaledInstance(con_wid, con_wid * img_hgh / img_wid, Image.SCALE_FAST);
		else
			newimg = img.getScaledInstance(con_hgh * img_wid / img_hgh, con_hgh, Image.SCALE_FAST);
	
	return new ImageIcon(newimg);
	}

}
