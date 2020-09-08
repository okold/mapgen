package mapgen;

/**
 * A planet with an axial tilt.
 * 
 * Note:
 * Negative tilts will be calculated as positive tilts.
 * 
 * Calculates the:
 * - Latitude of the Arctic and Antarctic Circles
 * - Latitude of the Northern and Southern Tropics
 * 
 * Determines:
 * - Whether a given latitude is in the Arctic, Tropic, or Temperate zones
 * 
 * All values are in decimal latitude.
 * 
 * @author Olga Koldachenko
 */
public class TiltedPlanet
{
	public static final double DEFAULT_TILT = 23.5; //Earth's tilt
	public static final double MAX_TILT = 90;
	
	protected double tilt;
	
	/**
	 * Creates a circle of latitude calculator with Earth's axial tilt (23.5 degrees).
	 */
	public TiltedPlanet()
	{
		tilt = DEFAULT_TILT;
	}
	
	/**
	 * Creates a circle of latitude calculator with a custom axial tilt.
	 */
	public TiltedPlanet(double tilt)
	{
		if (tilt < 0)
		{
			tilt *= -1;
		}
		
		while (tilt > MAX_TILT)
		{
			tilt = tilt - MAX_TILT;
		}
		
		this.tilt = tilt;
	}
	
	/**
	 * Returns the axial tilt.
	 */
	public double getTilt()
	{
		return tilt;
	}
	
	/**
	 * Sets the axial tilt.
	 */
	public void setTilt(double tilt)
	{
		this.tilt = tilt;
	}

	/**
	 * Returns the latitude of the Arctic Circle.
	 */
	public double getArcticCircle()
	{
		return 90 - tilt;
	}
	
	/**
	 * Returns the latitude of the Antarctic Circle.
	 */
	public double getAntarcticCircle()
	{
		return -1 * (90 - tilt);
	}
	
	/**
	 * Returns the latitude of the Northern Tropic.
	 */
	public double getNorthernTropic()
	{
		return tilt;
	}
	
	/**
	 * Returns the latitude of the Southern Tropic.
	 */
	public double getSouthernTropic()
	{
		return -1 * tilt;
	}
	
	/**
	 * Returns true if the Arctic and Antarctic start at the poles. If the tilt is
	 * >= 45 degrees, then the planet is reverse to Earth and the tropics are at the
	 * poles.
	 */
	public boolean arcticAtPoles()
	{
		return tilt < 45;
	}
	
	/**
	 * Returns true if the given latitude is within the Frigid climate zone.
	 */
	public boolean isFrigid(double lat)
	{

		if (arcticAtPoles())
		{
			return lat > getArcticCircle() || lat < getAntarcticCircle();
		}
		else
		{
			return lat < getArcticCircle() && lat > getAntarcticCircle();
		}
	}
	
	/**
	 * Returns true if the given latitude is within the Tropic climate zone.
	 */
	public boolean isTropic(double lat)
	{
		if (arcticAtPoles())
		{
			return lat < getNorthernTropic() && lat > getSouthernTropic();
		}
		else
		{
			return lat > getNorthernTropic() || lat < getSouthernTropic();
		}
	}
	
	/**
	 * Returns true if the given latitude is within the Temperate climate zone.
	 */
	public boolean isTemperate(double lat)
	{
		if (arcticAtPoles())
		{
			return (lat <= getArcticCircle() && lat >= getNorthernTropic())
					|| (lat <= getSouthernTropic() && lat >= getAntarcticCircle());
		}
		else
		{
			return (lat <= getNorthernTropic() && lat >= getArcticCircle())
					|| (lat <= getAntarcticCircle() && lat >= getSouthernTropic());
		}
	}
}
