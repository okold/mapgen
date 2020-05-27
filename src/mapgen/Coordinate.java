package mapgen;

/**
 * A latitude/longitude coordinate.
 */
public class Coordinate
{
	private double latitude;
	private double longitude;
	
	/**
	 * Creates a coordinate with the given latitude and longitude.
	 */
	public Coordinate(double lat, double lon)
	{
		latitude = lat;
		longitude = lon;
	}
	
	/**
	 * Returns the latitude.
	 */
	public double getLatitude()
	{
		return latitude;
	}
	
	/**
	 * Sets the latitude to the given value.
	 */
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	/**
	 * Returns the longitude.
	 */
	public double getLongitude() 
	{
		return longitude;
	}

	/**
	 * Sets the longitude to the given value.
	 */
	public void setLongitude(double longitude) 
	{
		this.longitude = longitude;
	}
}
