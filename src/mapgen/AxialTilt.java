package mapgen;

/**
 * A calculator for the major circles of latitude using axial tilt.
 * 
 * Negative tilts will be calculated as positive tilts.
 * Tilts over 90 degrees will calculate as 90 degrees.
 * 
 * Calculates the:
 * - Latitude of the Arctic and Antarctic Circles
 * - Latitude of the Northern and Southern Tropics
 * 
 * Determines:
 * - Whether a given latitude is in the Arctic or Tropic
 * 
 * All values are in decimal latitude.
 * 
 * @author Olga Koldachenko
 */
public class AxialTilt
{
	private static final double DEFAULT_TILT = 23.5;
	private double tilt;
	
	/**
	 * Creates a circle of latitude calculator with Earth's axial tilt (23.5 degrees).
	 */
	public AxialTilt()
	{
		tilt = DEFAULT_TILT;
	}
	
	/**
	 * Creates a circle of latitude calculator with a custom axial tilt.
	 */
	public AxialTilt(double tilt)
	{
		// correction for extreme values
		if (tilt < 0)
		{
			tilt *= -1;
		}
		if (tilt > 90)
		{
			tilt = 90;
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
		return tilt - 90;
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
		if (tilt < 45)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns true if the given latitude is within the Frigid climate zone.
	 */
	public boolean isFrigid(double lat)
	{
		if (arcticAtPoles())
		{
			if (lat >= getArcticCircle() || lat <= getAntarcticCircle())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			if (lat <= getArcticCircle() && lat >= getAntarcticCircle())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	/**
	 * Returns true if the given latitude is within the Tropic climate zone.
	 */
	public boolean isTropic(double lat)
	{
		if (arcticAtPoles())
		{
			if (lat <= getNorthernTropic() && lat >= getSouthernTropic())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			if (lat >= getNorthernTropic() || lat <= getSouthernTropic())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	/**
	 * Returns true if the given latitude is within the Temperate climate zone.
	 */
	public boolean isTemperate(double lat)
	{
		if (arcticAtPoles())
		{
			if ((lat < getArcticCircle() && lat > getNorthernTropic())
					|| (lat < getSouthernTropic() && lat > getAntarcticCircle()))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			if ((lat < getNorthernTropic() && lat > getArcticCircle())
					|| (lat < getAntarcticCircle() && lat > getSouthernTropic()))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
}
