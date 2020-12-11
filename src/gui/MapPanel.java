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
	private MapPalette palette;
	private int mode;
	
	MapPanel()
	{
		super();
		palette = new MapPalette(MapPalette.DEFAULT);
		mode = 1;
		coast_outlines = false;
	}
	
	MapPanel(BufferedImage image) 
	{
		super(image);
		palette = new MapPalette(MapPalette.DEFAULT);
		mode = 1;
		coast_outlines = false;
	}
	
	MapPanel(BiomePlanet bp)
	{
		super();
		planet = bp;
		coast_outlines = true;
		palette = new MapPalette(MapPalette.DEFAULT);
		mode = 1;
		generateImage();
	}
	
	public void setPalette(int[] list)
	{
		palette = new MapPalette(list);
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
	
	public void setMode(int i) {
		mode = i;
	}
	
	public int getMode()
	{
		return mode;
	}
	
	public void generateImage()
	{
    	Point p = new Point(0,0);
    	
    	HeightMap height_map = planet.getHeightMap();
    	
    	if (mode == 1)
    	{
    		BufferedImage image = new BufferedImage(height_map.getWidth(),height_map.getHeight(),BufferedImage.TYPE_INT_ARGB);
        	
        	for (p.y = 0; p.y < height_map.getHeight(); p.y++)
    		{
    			for (p.x = 0; p.x < height_map.getWidth(); p.x++)
    			{
    				if (planet.isCoast(p))
    				{
    					image.setRGB(p.x, p.y, palette.coast_colour);
    				}
    				else if(planet.isOcean(p))
    				{
    					image.setRGB(p.x, p.y, palette.ocean_colour);
    				}
    				else if(planet.isDeepOcean(p))
    				{
    					image.setRGB(p.x, p.y, palette.deep_ocean_colour);
    				}
    				else if(planet.isMountain(p))
    				{
    					image.setRGB(p.x, p.y, palette.mountain_colour);
    				}
    				else
    				{
    					image.setRGB(p.x, p.y, palette.land_colour);
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
    	}
    	else
    	{
    		setImage(height_map.getBufferedImage());
    	}
    	
    	revalidate();
    	repaint();
	}
	
	public int mixColours(int RGB_1, int RGB_2, double strength)
	{
		Color c_2 = new Color(RGB_2);
		Color c_1 = new Color(RGB_1);
		
		if (strength > 1)
		{
			strength = 1;
		}
		
		int red = (int) (c_1.getRed() * strength) + (int) (c_2.getRed() * (1.0 - strength));
		int green = (int) (c_1.getGreen() * strength) + (int) (c_2.getGreen() * (1.0 - strength));
		int blue = (int) (c_1.getBlue() * strength) + (int) (c_2.getBlue() * (1.0 - strength));
		
		return new Color(red,green,blue).getRGB();
	}
}
