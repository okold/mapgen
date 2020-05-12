package mapgen;

/**
 * A map that holds height information in each cell.
 *
 * @author Olga Koldachenko
 */
abstract class HeightMap extends Map 
{
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
	 * Returns the height at the given x and y coordinates.
	 */
	public int getAltitude(int x, int y)
	{
		return height_map[x][y];
	}
	
	/**
	 * Sets the height at the given x and y coordinates.
	 */
	public void setAltitude(int x, int y, int height)
	{
		height_map[x][y] = height;
	}
	
	/**
	 * Returns the height at the given latitude and longitude.
	 */
	public int getAltitude(double lat, double lon)
	{
		return height_map[getXFromLon(lon)][getYFromLat(lat)];
	}
	
	/**
	 * Sets the height at the given latitude and longitude.
	 */
	public void setAltitude(double lat, double lon, int height)
	{
		height_map[getXFromLon(lon)][getYFromLat(lat)] = height;
	}
	
	public abstract void generate();
}
