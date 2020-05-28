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

	private static final int MAX_SLOPE = 100;
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
		for (int i = rand.nextInt(5) + 5; i > 0; i--)
		{
			generateLandmass(getRandomPoint(), (int) (rand.nextInt((int) (MAX_HEIGHT * 0.2)) + (MAX_HEIGHT * 0.8)), 1, 5000);
		}
		
		for (int i = rand.nextInt(100) + 100; i > 0; i--)
		{
			generateLandmass(getRandomPoint(), rand.nextInt((int) (MAX_HEIGHT * 0.8)), rand.nextInt(MAX_SLOPE), rand.nextInt(100));
		}
		
		for (int i = rand.nextInt(100) + 50; i > 0; i--)
		{
			generateLandmass(getRandomPoint(), rand.nextInt(200), 1, 5000);
		}
	}
	
	private void generatePeak(Point p, int altitude, int slope) {
		
		slope += rand.nextInt(MAX_SLOPE / 10);
		if (altitude > 0 && getAltitude(p) < altitude)
		{
			setAltitude(p,altitude);
			generatePeak(new Point(getXAdj(p.x - 1),p.y), altitude - slope, slope);
			generatePeak(new Point(getXAdj(p.x + 1),p.y), altitude - slope, slope);
			generatePeak(new Point(p.x,getYAdj(p.y + 1)), altitude - slope, slope);
			generatePeak(new Point(p.x,getYAdj(p.y - 1)), altitude - slope, slope);
			
		}
	}
	
	/**
	 * Creates long, snakey landmasses. Originally intended to generate mountain
	 * ranges. Also works to create small islands.
	 */
	private void generateLandmass(Point p, int altitude, int slope, int length)
	{
		int new_x, new_y;
		
		switch (rand.nextInt(3))
		{
		case 0:
			new_x = getXAdj(p.x - 3);
			break;
		case 1:
			new_x = getXAdj(p.x + 3);
			break;
		default:
			new_x = p.x;
		}
		
		
		switch (rand.nextInt(3))
		{
		case 0:
			new_y = getYAdj(p.y - 3);
			break;
		case 1:
			new_y = getYAdj(p.y + 3);
			break;
		default:
			new_y = p.y;
		}
		
		if(length > 0)
		{
			generatePeak(p, altitude, slope);
			generateLandmass(new Point(new_x, new_y), altitude, slope, length - 1);
		}
		
	}
}