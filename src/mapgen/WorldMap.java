package mapgen;

import java.awt.Point;
import java.util.Random;

public class WorldMap
{
	private int scale;
	private int water_level;
	private HeightMap height_map;
	private AxialTilt axial_tilt;
	
	public WorldMap()
	{
		Random rand = new Random();
		int tilt = rand.nextInt(90);
		axial_tilt = new AxialTilt(tilt);
		scale = 2;
		water_level = rand.nextInt(100);
		height_map = new RandomPeakMap(scale);
		height_map.generate();
	}
	
	public boolean isWater(Point p)
	{
		if (height_map.getAltitude(p) <= water_level)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isCoast(Point p)
	{
		int altitude = height_map.getAltitude(p);
		
		if (altitude <= water_level && altitude >= water_level * 0.75)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isOcean(Point p)
	{
		if (isWater(p) && !isCoast(p))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isFrigid(int y)
	{
		double lat = height_map.getLatitude(y);
		if (axial_tilt.isFrigid(lat))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isTropic(int y)
	{
		double lat = height_map.getLatitude(y);
		if (axial_tilt.isTropic(lat))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isTemperate(int y)
	{
		double lat = height_map.getLatitude(y);
		if (axial_tilt.isTemperate(lat))
		{
			return true;
		}
		
		return false;
	}
	
	public int getWidth()
	{
		return height_map.getWidth();
	}
	
	public int getHeight()
	{
		return height_map.getHeight();
	}
	
	public int getAltitude(Point p)
	{
		return height_map.getAltitude(p);
	}
	
}
