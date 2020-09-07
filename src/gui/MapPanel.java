package gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import mapgen.BiomePlanet;
import mapgen.HeightMap;

public class MapPanel extends ImagePanel {

	private static final long serialVersionUID = -7529934880042656672L;

	private BiomePlanet planet;
	private boolean coast_outlines;
	
	MapPanel()
	{
		super();
		coast_outlines = false;
	}
	
	MapPanel(BufferedImage image) 
	{
		super(image);
		coast_outlines = false;
	}
	
	MapPanel(BiomePlanet bp)
	{
		super();
		planet = bp;
		coast_outlines = true;
		generateImage();
	}
	
	public void setPlanet(BiomePlanet bp)
	{
		planet = bp;
		generateImage();
	}

	public void setCoastOutlines(boolean bool)
	{
		coast_outlines = bool;
		generateImage();
	}
	
	public BufferedImage generateImage()
	{
		int coast_colour = new Color(30, 90, 124).getRGB();
		int ocean_colour = new Color(26, 74, 94).getRGB();
		int deep_ocean_colour = new Color(17, 67, 92).getRGB();
		int frigid_mountain_colour = new Color(252, 253, 255).getRGB();
		int frigid_colour = new Color(228, 231, 238).getRGB();
		//int frigid_water = new Color(185, 190, 209).getRGB();
		int mountain_colour = new Color(137, 128, 97).getRGB();
		int temperate_colour = new Color(124, 156, 83).getRGB();
		int tropical_colour = new Color(87, 133, 68).getRGB();
		
    	Point p = new Point(0,0);
    	
    	HeightMap height_map = planet.getHeightMap();
    	
    	BufferedImage image = new BufferedImage(height_map.getWidth(),height_map.getHeight(),BufferedImage.TYPE_INT_ARGB);
    	
    	for (p.y = 0; p.y < height_map.getHeight(); p.y++)
		{
			for (p.x = 0; p.x < height_map.getWidth(); p.x++)
			{
				
				if (planet.isCoast(p))
				{
					image.setRGB(p.x, p.y, coast_colour);
				}
				else if(planet.isOcean(p))
				{
					image.setRGB(p.x, p.y, ocean_colour);
				}
				else if(planet.isDeepOcean(p))
				{
					image.setRGB(p.x, p.y, deep_ocean_colour);
				} 
				else if(planet.isFrigid(p))
				{
					if(planet.isMountain(p))
						image.setRGB(p.x, p.y, frigid_mountain_colour);
					else
						image.setRGB(p.x, p.y, frigid_colour);
				}
				else if(planet.isTemperate(p))
				{
					if(planet.isMountainPeak(p))
						image.setRGB(p.x, p.y, mountain_colour);
					else if (planet.isMountain(p))
						image.setRGB(p.x, p.y, mountain_colour);
					else
						image.setRGB(p.x, p.y, temperate_colour);
				}
				else
				{
					if (planet.isMountain(p))
						image.setRGB(p.x, p.y, mountain_colour);
					else
						image.setRGB(p.x, p.y, tropical_colour);
				}
			}
		}
    	
    	if (coast_outlines)
    	{
    		if (planet.getWaterLevel() > 0)
        	{
        		for (p.y = 0; p.y < height_map.getHeight(); p.y++)
        		{
        			for (p.x = 0; p.x < height_map.getWidth(); p.x++)
        			{
        				if (height_map.getAltitude(p) > planet.getWaterLevel())
        				for (int i = -1; i <= 1; i++)
        				{
        					for (int j = -1; j <= 1; j++)
        					{
        						Point p2 = new Point(p.x + i, p.y + j);
        						if (height_map.pointExists(p2) && height_map.getAltitude(p2) <= planet.getWaterLevel())
        						{
        							image.setRGB(p.x, p.y, Color.BLACK.getRGB());
        						}
        					}
        				}
        			}
        		}
        	}
    	}
    	
    	setImage(image);
    	revalidate();
    	repaint();
    	
    	return image;
	}
}
