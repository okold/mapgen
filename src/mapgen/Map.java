package mapgen;

import java.awt.Point;

/**
 * The basis for an equirectangular world map using a grid system.
 * 
 * The number of cells in the grid is by default 1 for each degree of latitude and logitude.
 * The map can be scaled by a factor of a positive integer.
 * 
 * Latitude and longitude are measured in decimal degrees.
 * The top/north edge of the map is the 0th index at +90.0 degrees latitude.
 * The bottom/south edge of the map is by default the 180th index at -90.0 degrees latitude.
 * The left/west edge of the map is the 0th index at -180.0 degrees longitude.
 * The right/east edge of the map is by default the 360th index at +180.0 degrees longitude.
 * 
 * @author Olga Koldachenko
 */
public abstract class Map
{
	protected static final int DEFAULT_WIDTH = 360;
	protected static final int DEFAULT_HEIGHT = 180;
	private int scale;
	private int midpoint_y;
	private int midpoint_x;
	
	/**
	 * Creates a map using the default scale (1 index per degree lat/lon).
	 */
	public Map()
	{
		scale = 1;
		midpoint_y = DEFAULT_HEIGHT / 2;
		midpoint_x = DEFAULT_WIDTH / 2;
	}
	
	/**
	 * Creates a map using the given scale. Must be a positive integer.
	 */
	public Map(int scale)
	{
		// error handling bad values
		if (scale == 0)
		{
			scale = 1;
		}
		else if (scale < 0)
		{
			scale *= -1;
		}
		
		this.scale = scale;
		
		midpoint_y = DEFAULT_HEIGHT * scale / 2;
		midpoint_x = DEFAULT_WIDTH * scale / 2;
	}
	
	/**
	 * Returns the scale of the map.
	 */
	public int getScale()
	{
		return scale;
	}
	
	/**
	 * Returns the number of columns in the map.
	 */
	public int getWidth()
	{
		return scale * DEFAULT_WIDTH;
	}
	
	/**
	 * Returns the number of rows in the map.
	 */
	public int getHeight()
	{
		return scale * DEFAULT_HEIGHT;
	}
	
	/**
	 * Converts a Point to a Coordinate using this map's scale.
	 */
	public Point getPoint(Coordinate coord)
	{
		return new Point(getX(coord.getLongitude()), getY(coord.getLatitude()));
	}
	
	/**
	 * Converts a Coordinate to a Point using this map's scale.
	 */
	public Coordinate getCoordinate(Point point)
	{
		return new Coordinate(getLatitude(point.y), getLongitude(point.x));
	}
	
	/**
	 * Returns the latitude of the given row.
	 */
	public double getLatitude(int y)
	{
		if (y == midpoint_y)
		{
			return 0;
		}
		else if (y < midpoint_y)
		{
			return 90 - ((double) (y) / scale);
		}
		else
		{
			return -1 * ((double) (y - midpoint_y) / scale);
		}
	}
	
	/**
	 * Returns the longitude of the given column.
	 */
	public double getLongitude(int x)
	{
		if (x == midpoint_x)
		{
			return 0;
		}
		else if (x < midpoint_x)
		{
			return ((double) (x) / scale) - 180.0;
		}
		else
		{
			return (double) (x - midpoint_x) / scale;
		}
	}
	
	/**
	 * Returns the column of the given longitude.
	 */
	public int getX(double lon)
	{
		if (lon == 0)
		{
			return midpoint_x;
		}
		else if (lon < 0)
		{
			return (int) ((lon + 180) * scale);
		}
		else
		{
			return (int) ((lon * scale) + midpoint_x);
		}
	}
	
	/**
	 * Returns the row of the given latitude.
	 */
	public int getY(double lat)
	{
		if (lat == 0)
		{
			return midpoint_y;
		}
		else if (lat > 0)
		{
			return (int) ((90.0 - lat) * scale);
		}
		else
		{
			return (int) (midpoint_y - (lat * scale));
		}
	}
	
	/**
	 * Returns the adjusted y value, reflecting it along the top
	 * and bottom edges.
	 * 
	 * Gotta be a better way to do this.
	 */
	public int getYAdj(int y)
	{
		int y_adj = y;
		
		while (y_adj < 0)
		{
			y_adj *= -1;
			
			if (y_adj > getHeight())
				y_adj = getHeight() - (y_adj - getHeight());
		}
		
		if (y_adj >= getHeight())
			y_adj = getHeight() - 1;
		
		if (y_adj < 0)
		{
			y_adj = 0;
		}
		
		return y_adj;
	}
	
	/**
	 * Returns the adjusted x value, wrapping around the edges if
	 * greater than or less than the bounds of the grid.
	 */
	public int getXAdj(int x)
	{
		while (x < 0)
		{
			x = getWidth() + x;
		}
		
		return x % getWidth();
	}
}
