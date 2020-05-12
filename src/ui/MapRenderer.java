package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import mapgen.WorldMap;

public class MapRenderer extends JFrame {

	private static final long serialVersionUID = -349577414898672371L;
	private static BufferedImage map_image;
	private static WorldMap world_map;
	
	public static void main(String[] args)
	{
		world_map = new WorldMap();
		map_image = new BufferedImage(world_map.getWidth(),world_map.getHeight(),BufferedImage.TYPE_INT_ARGB);
		generate();
		
		
		MapRenderer window = new MapRenderer();
        window.setVisible(true);
	}

	public MapRenderer()
	{
		super("Map Renderer");
        setSize(770, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}
	
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(map_image, 25, 50, null);
    }
    
    public static void generate()
    {
    	for (int y = 0; y < world_map.getHeight(); y++)
		{
			for (int x = 0; x < world_map.getWidth(); x++)
			{
				if (world_map.isWater(x,y))
				{
					map_image.setRGB(x, y, new Color(204, 229, 255).getRGB());
				}
				else if (world_map.isFrigid(y) || world_map.getAltitude(x, y) > 200)
				{
					map_image.setRGB(x, y, Color.WHITE.getRGB());
				}
				else if (world_map.getAltitude(x, y) > 150)
				{
					map_image.setRGB(x, y, Color.GRAY.getRGB());
				}
				else if (world_map.isTropic(y))
				{
					map_image.setRGB(x, y, new Color(51, 102, 0).getRGB());
				}
				else
				{
					map_image.setRGB(x, y, new Color(102,204,0).getRGB());
				}
			}
		}
    }
}
