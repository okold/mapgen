package mapgen;

import java.awt.Point;
import java.util.Random;

/**
 * A height map made up of randomized points that spread out downward by
 * random slopes.
 *
 * @author Olga Koldachenko
 */
public class RecursivePeakMap extends HeightMap {

	//private static final int MAX_SLOPE = 100;
	private static Random rand;
	
	public RecursivePeakMap()
	{
		super();
		rand = new Random();
	}
	
	public RecursivePeakMap(int scale)
	{
		super(scale);
		rand = new Random();
	}
	
	/**
	 * Generates a random number of peaks with random slopes and places them
	 * at random points along the map. Without calling blend(), creates very
	 * blocky coastlines.
	 */
	@Override
	public void generate()
	{
		for (int x = 0; x < 4; x++)
		{
			generateContinent(getRandomPoint(), MAX_HEIGHT, 20);
		}
		
		for (int x = 0; x < 3; x++)
		{
			generateContinent(getRandomPoint(), MAX_HEIGHT - (MAX_HEIGHT / 4), 20);
		}
		
		for (int x = 0; x < 3; x++)
		{
			generateContinent(getRandomPoint(), MAX_HEIGHT / 2, 20);
		}
		
		for (int x = 0; x < 5; x++)
		{
			generateContinent(getRandomPoint(), MAX_HEIGHT / 4, 20);
		}
		
		
		
		
	}
	
	/**
	 * Generates a continent with the given Point at the centre.
	 */
	public void generateContinent(Point p, int peak_height, int num_peaks)
	{
		// Generates the highland/ main mountainous region of the continent
		int mountain_x = rand.nextInt(3) - 1;
		int mountain_y = rand.nextInt(3) - 1;
		generateDirectedLandmass(p, peak_height, 1, 100, 1, 10, mountain_x, mountain_y);
		
		generatePeak(p, (int) (peak_height * 0.9), 0, 2);
		
		Point new_point = generateNearPoint(p, 5, 30);
		for (int i = 0; i < num_peaks; i++)
		{
			generatePeak(new_point, (int) (peak_height * 0.95), 0, 2);
			new_point = generateNearPoint(p, 5, 30);
		}
		
		// Generates the low/mainland region of the continent
		new_point = generateNearPoint(p, 3, 20);
		for (int i = 0; i < num_peaks * 10; i++)
		{
			generatePeak(new_point, (int) (peak_height * 0.75), 0, 2);
			generateLandmass(new_point, (int) (peak_height * 0.75), 4, 20, 1, 2);
			new_point = generateNearPoint(new_point, 3, 20);
		}
		
		// Generates nearby islands
		new_point = generateNearPoint(p, 10, 40);
		for (int i = 0; i < num_peaks * 5; i++)
		{
			generateLandmass(new_point, (int) (peak_height * 0.7), 4, 60, 1, 2);
			new_point = generateNearPoint(new_point, 3, 20);
		}
		
		// Generates the coastal region of the continent
		new_point = generateNearPoint(p, 3, 20);
		for (int i = 0; i < num_peaks * 15; i++)
		{
			generatePeak(new_point, (int) (peak_height * 0.5), 0, 2);
			generateLandmass(new_point, (int) (peak_height * 0.5), 4, 30, 1, 2);
			new_point = generateNearPoint(new_point, 3, 20);
		}
		
	}
	
	public Point generateNearPoint(Point p, int distance, int variation)
	{
		int new_x, new_y;
		
		int x_var = rand.nextInt(distance + variation);
		switch (rand.nextInt(3))
		{
		case 0:
			new_x = getXAdj(p.x - x_var);
			break;
		case 1:
			new_x = getXAdj(p.x + x_var);
			break;
		default:
			new_x = p.x;
		}
		
		int y_var = rand.nextInt(distance + variation);
		switch (rand.nextInt(3))
		{
		case 0:
			new_y = getYAdj(p.y - y_var);
			break;
		case 1:
			new_y = getYAdj(p.y + y_var);
			break;
		default:
			new_y = p.y;
		}
		
		return new Point(new_x, new_y);
	}
	
	private void generatePeak(Point p, int altitude, int slope, int slopevar) {
		
		slope += rand.nextInt(slopevar);
		if (altitude > 0 && getAltitude(p) < altitude)
		{
			setAltitude(p,altitude);
			generatePeak(new Point(getXAdj(p.x - 1),p.y), altitude - slope, slope, slopevar);
			generatePeak(new Point(getXAdj(p.x + 1),p.y), altitude - slope, slope, slopevar);
			generatePeak(new Point(p.x,getYAdj(p.y + 1)), altitude - slope, slope, slopevar);
			generatePeak(new Point(p.x,getYAdj(p.y - 1)), altitude - slope, slope, slopevar);
			
		}
	}
	
	/**
	 * Creates long, snakey landmasses. Originally intended to generate mountain
	 * ranges. Also works to create small islands.
	 */
	private void generateLandmass(Point p, int altitude, int slope, int length, int disp_min, int disp_var)
	{
		
		if(length > 0)
		{
			generatePeak(p, altitude, slope, 2);
			generateLandmass(generateNearPoint(p, disp_min, disp_var), altitude, slope, length - 1, disp_min, disp_var);
		}
		
	}
	
	/**
	 * Creates long, snakey landmasses. Originally intended to generate mountain
	 * ranges. Also works to create small islands.
	 */
	private void generateDirectedLandmass(Point p, int altitude, int slope, int length, int disp_min, int disp_var, int x_dir, int y_dir)
	{
		Point new_point = generateNearPoint(p, disp_min, disp_var);
		new_point.x = getXAdj(new_point.x + x_dir);
		new_point.y = getYAdj(new_point.y + y_dir);
		if(length > 0)
		{
			generatePeak(p, altitude, slope, 2);
			generatePeak(generateNearPoint(p, disp_min, disp_var), altitude - altitude / 4, 0, 2);
			generateDirectedLandmass(new_point, altitude, slope, length - 1, disp_min, disp_var, x_dir, y_dir);
		}
		
	}
}