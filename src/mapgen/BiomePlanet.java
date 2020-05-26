package mapgen;

import java.awt.Point;
import java.util.Random;

public class BiomePlanet extends TiltedPlanet
{
	private int scale;
	private int water_level;
	private HeightMap height_map;
	private Random rand;
	
	public BiomePlanet()
	{
		super();
		
		rand = new Random();
		
		setTilt(rand.nextInt((int) MAX_TILT));
		water_level = rand.nextInt(HeightMap.MAX_HEIGHT / 2);
		
		scale = 2;
		height_map = new SquarePeakMap(scale);
		height_map.generate();
	}
	
	public void randomize()
	{
		setTilt(rand.nextInt((int) MAX_TILT));
		water_level = rand.nextInt(HeightMap.MAX_HEIGHT / 2);
		height_map.generate();
	}
	
	
	// getters and setters
	
	public int getWaterLevel()
	{
		return water_level;
	}
	
	public void setWaterLevel(int level)
	{
		water_level = level;
	}
	
	// biome determination
	
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
	
	// allow access of circle zones by Point
	
	public boolean isFrigid(Point p)
	{
		return isFrigid(height_map.getLatitude(p.y));
	}
	
	public boolean isTropic(Point p)
	{
		return isTropic(height_map.getLatitude(p.y));
	}
	
	public boolean isTemperate(Point p)
	{
		return isTemperate(height_map.getLatitude(p.y));
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
