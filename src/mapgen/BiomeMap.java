package mapgen;

import java.awt.Point;
import java.util.Random;

public class BiomeMap extends AxialTilt
{
	private int scale;
	private int water_level;
	private HeightMap height_map;
	
	public BiomeMap()
	{
		super();
		
		Random rand = new Random();
		setTilt(rand.nextInt((int) MAX_TILT));
		water_level = rand.nextInt(height_map.MAX_HEIGHT / 2);
		
		scale = 2;
		height_map = new RandomPeakMap(scale);
		height_map.generate();
	}
	
	public boolean isWater(Point p)
	{
		return height_map.getAltitude(p) <= water_level;
	}
	
	public boolean isCoast(Point p)
	{
		int altitude = height_map.getAltitude(p);
		
		return altitude <= water_level && altitude >= water_level * 0.75;
	}
	
	public boolean isOcean(Point p)
	{
		return isWater(p) && !isCoast(p);
	}
	
	// allow access of circle zones by point
	
	public boolean isFrigid(int y)
	{
		return isFrigid(height_map.getLatitude(y));
	}
	
	public boolean isTropic(int y)
	{
		return isTropic(height_map.getLatitude(y));
	}
	
	public boolean isTemperate(int y)
	{
		return isTemperate(height_map.getLatitude(y));
	}
	
	// access height map attributes
	
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
	
	public int getAltitude(Coordinate c)
	{
		return height_map.getAltitude(c);
	}
}
