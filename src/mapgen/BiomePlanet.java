package mapgen;

import java.awt.Point;

/**
 * A tilted planet with a height map and water level, determining
 * coastlines and biomes.
 *
 * @author Olga Koldachenko
 */
public class BiomePlanet extends TiltedPlanet
{
	private int water_level;
	private HeightMap height_map;
	
	/**
	 * Creates a BiomePlanet with the given parameters.
	 */
	public BiomePlanet(double tilt, int water_level, HeightMap height_map)
	{
		super(tilt);
		this.water_level = water_level;
		this.height_map = height_map;
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
	
	public HeightMap getHeightMap()
	{
		return height_map;
	}
	
	public void setHeightMap(HeightMap height_map)
	{
		this.height_map = height_map;
	}
	
	// biome determination
	
	/**
	 * True if the given point contains water.
	 */
	public boolean isWater(Point p)
	{
		return water_level > 0 && height_map.getAltitude(p) <= water_level;
	}
	
	/**
	 * True if the given point is shallow water, in the top 25%
	 * of the planet's water level.
	 */
	public boolean isCoast(Point p)
	{
		int altitude = height_map.getAltitude(p);
		
		return water_level > 0 && altitude <= water_level && altitude >= water_level * 0.75;
	}
	
	/**
	 * True if the given point is deep water, in the bottom 25%
	 * of the planet's water level.
	 */
	public boolean isDeepOcean(Point p)
	{
		return water_level > 0 && height_map.getAltitude(p) < water_level * 0.33;
	}
	
	/**
	 * True if the given point is ocean water, between the coasts and deep ocean.
	 */
	public boolean isOcean(Point p)
	{
		return isWater(p) && !isCoast(p) && !isDeepOcean(p);
	}
	
	/**
	 * True if the given point is in the top 75% of landmass.
	 */
	public boolean isMountain(Point p)
	{
		int altitude = height_map.getAltitude(p);
		int total_height = HeightMap.MAX_HEIGHT - water_level;
		
		return altitude >= HeightMap.MAX_HEIGHT - (0.25 * total_height);
	}
	
	/**
	 * True if the given point is in the top 90% of landmass.
	 */
	public boolean isMountainPeak(Point p)
	{
		int altitude = height_map.getAltitude(p);
		int total_height = HeightMap.MAX_HEIGHT - water_level;
		
		return altitude >= HeightMap.MAX_HEIGHT - (0.1 * total_height);
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
}
