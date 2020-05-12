package mapgen;

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
	
	public boolean isWater(int x, int y)
	{
		if (height_map.getAltitude(x, y) <= water_level)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isFrigid(int y)
	{
		double lat = height_map.getLatFromY(y);
		if (axial_tilt.isFrigid(lat))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isTropic(int y)
	{
		double lat = height_map.getLatFromY(y);
		if (axial_tilt.isTropic(lat))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isTemperate(int y)
	{
		double lat = height_map.getLatFromY(y);
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
	
	public int getAltitude(int x, int y)
	{
		return height_map.getAltitude(x, y);
	}
	
}
