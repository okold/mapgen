package gui;

import java.awt.*;
import java.awt.image.*;
import javax.swing.JPanel;

public class ImagePanel extends JPanel 
{
	private static final long serialVersionUID = -3336983084599822678L;
	private BufferedImage image;
	
	ImagePanel(BufferedImage image)
	{
		super();
		this.image = image;
	}
	
    public void paint(Graphics g) 
    {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(image, 25, 50, null);
    }
    
    public BufferedImage getImage()
    {
    	return image;
    }
    
	public void setImage(BufferedImage image)
	{
		this.image = image;
	}
	
}
