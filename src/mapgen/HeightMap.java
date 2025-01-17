package mapgen;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * A map that holds height information in each cell.
 *
 * @author Olga Koldachenko
 */
public class HeightMap extends Map 
{
	public static final int MAX_HEIGHT = 255;
	private int[][] height_map;
	
	/**
	 * Creates a height map at the default scale.
	 */
	public HeightMap()
	{
		super();
		height_map = new int[DEFAULT_WIDTH + 1][DEFAULT_HEIGHT + 1];
	}
	
	/**
	 * Creates a height map at the given scale.
	 */
	public HeightMap(int scale)
	{
		super(scale);
		height_map = new int[getWidth()][getHeight()];
	}
	
	/**
	 * Returns the altitude at the given Point.
	 */
	public int getAltitude(Point p)
	{
		return height_map[p.x][p.y];
	}
	
	/**
	 * Sets the altitude at the given Point.
	 */
	public void setAltitude(Point p, int height)
	{
		height_map[p.x][p.y] = height;
	}
	
	/**
	 * Returns the height at the given Coordinate.
	 */
	public int getAltitude(Coordinate c)
	{
		return height_map[getX(c.getLongitude())][getY(c.getLatitude())];
	}
	
	/**
	 * Sets the height at the given Coordinate.
	 */
	public void setAltitude(Coordinate c, int height)
	{
		height_map[getX(c.getLongitude())][getY(c.getLatitude())] = height;
	}
	
	/**
	 * True if the given Point exists in the map.
	 */
	public boolean pointExists(Point p)
	{
		if (p.x >= 0 && p.x < getWidth() && p.y >= 0 && p.y < getHeight())
			return true;

		return false;
	}
	
	
	/**
	 * Blends the values in the map by averaging each point using the heights
	 * of the points around it, in a square the size of the strength parameter,
	 * and returns the blended map.
	 * 
	 * Note that the type of the new HeightMap, if calling using a child of this class, 
	 * becomes generic HeightMap, and calling generate() afterwards will create a 
	 * flat map.
	 * 
	 * Recurses over the process num_passes times.
	 * 
	 * Super expensive...
	 * 
	 */
	public HeightMap blend(int strength, int num_passes)
	{
		if (num_passes <= 0)
			return this;

		if (strength < 0)
			strength *= -1;
		
		HeightMap new_map = new HeightMap(getScale());
		
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getHeight(); y++)
			{
				Point p = new Point(x, y);
				int average = getAltitude(p);
				
				for (int i = -1 * strength; i <= strength; i++)
				{
					for (int j = -1 * strength; j <= strength; j++)
					{
						Point p2 = new Point(getXAdj(x + i), getYAdj(y + j));
						
						System.out.println(p2);
						int altitude_2 = getAltitude(p2);
						
						if (altitude_2 > 0)
						{
							average += altitude_2;
							average /= 2;
						}
					}
				}
				new_map.height_map[x][y] = average;
			}
		}
		
		return new_map.blend(strength, num_passes - 1);
	}
	
	/**
	 * Generates a flat map with every value being at the midpoint of the
	 * maximum possible height.
	 */
	public void generate()
	{
		int midpoint = MAX_HEIGHT / 2;
		
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getHeight(); y++)
			{
				height_map[x][y] = midpoint;
			}
		}
	}
	
	public Point getRandomPoint()
	{
		Random rand = new Random();
		return new Point(rand.nextInt(getWidth()), rand.nextInt(getHeight()));
	}
	
	public BufferedImage getBufferedImage()
	{
		Point p = new Point(0,0);
		BufferedImage image = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);

    	for (p.y = 0; p.y < getHeight(); p.y++)
		{
			for (p.x = 0; p.x < getWidth(); p.x++)
			{
				int val = height_map[p.x][p.y];
				image.setRGB(p.x, p.y, new Color(val, val, val).getRGB());
			}
		}
		return image;
	}
}
