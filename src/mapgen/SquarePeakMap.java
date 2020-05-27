package mapgen;

import java.awt.Point;
import java.util.Random;

/**
 * A height map made up of randomized points that spread out downward by
 * random slopes.
 * 
 * The values of the height range from 0 to 256.
 * 
 * Note/Bug: Draws very bottom-heavy maps??
 *
 * @author Olga Koldachenko
 */
public class SquarePeakMap extends HeightMap {

	private static final int MAX_PEAKS = 10000;
	private static final int MAX_SLOPE = 10;
	
	public SquarePeakMap()
	{
		super();
	}
	
	public SquarePeakMap(int scale)
	{
		super(scale);
	}
	
	/**
	 * Generates a random number of peaks with random slopes and places them
	 * at random points along the map. Without calling blend(), creates very
	 * blocky coastlines.
	 */
	@Override
	public void generate()
	{
		Random rand = new Random();
		int count = rand.nextInt(MAX_PEAKS * getScale());
		
		while (count > 0)
		{
			int peak_height = rand.nextInt(MAX_HEIGHT) + 1;
			int peak_slope = rand.nextInt(MAX_SLOPE) + 1;
			
			Point peak = new Point(rand.nextInt(getWidth()),rand.nextInt(getHeight()));
			setAltitude(peak, peak_height);
			
			int current_x_east = peak.x + 1;
			int current_x_west = peak.x - 1;
			int current_y_north = peak.y - 1;
			int current_y_south = peak.y + 1;
			int current_height = peak_height - peak_slope;
			
			while (current_height > 0)
			{
				// generates north and south edges of square
				for (int x = current_x_west; x <= current_x_east; x++)
				{
					int x_adj = getXAdj(x);
					
					Point north = new Point(x_adj, getYAdj(current_y_north));
					Point south = new Point(x_adj, getYAdj(current_y_south));
					
					if (getAltitude(north) < current_height);
						setAltitude(north,current_height);
					
					if (getAltitude(south) < current_height)
						setAltitude(south,current_height);
				}
				
				// generates the east and west edges of the square
				for (int y = current_y_north + 1; y <= current_y_south - 1; y++)
				{
					int east_adj, west_adj;
					
					if (y < 0 || y >= getHeight())
					{
						east_adj = getXAdj(current_x_east + getWidth() / 2);
						west_adj = getXAdj(current_x_west + getWidth() / 2);
					}
					else
					{
						east_adj = getXAdj(current_x_east);
						west_adj = getXAdj(current_x_west);
					}
					
					int y_adj = getYAdj(y);
					
					Point east = new Point(east_adj, y_adj);
					Point west = new Point(west_adj, y_adj); 
	
					if (getAltitude(west) < current_height)
						setAltitude(west,current_height);
					
					if (getAltitude(east) < current_height)
						setAltitude(east,current_height);
				}

				current_height -= peak_slope;
				current_x_west--;
				current_x_east++;
				current_y_north--;
				current_y_south++;
			}
			
			count--;
		}
	}
}
