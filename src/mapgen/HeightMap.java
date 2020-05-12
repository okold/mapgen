package mapgen;

import java.awt.Point;

/**
 * A map that holds height information in each cell.
 *
 * @author Olga Koldachenko
 */
abstract class HeightMap extends Map 
{
	public static final int MAX_HEIGHT = 256;
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
	
	public abstract void generate();
}
