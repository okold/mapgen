package mapgen;

import java.awt.Point;
import java.util.Random;

/**
 * A height map made up of randomized points that spread out downward by
 * random slopes. Creates a very square, abstract-looking map.
 * 
 * The values of the height range from 0 to 256.
 *
 * @author Olga Koldachenko
 */
public class SquarePeakMap extends HeightMap {

	private static final int MAX_PEAKS = 1000;
	private int num_peaks;
	private Random rand;
	
	public SquarePeakMap()
	{
		super();
		rand = new Random();
		num_peaks = rand.nextInt(MAX_PEAKS);
	}
	
	public SquarePeakMap(int scale)
	{
		super(scale);
		rand = new Random();
		num_peaks = rand.nextInt(MAX_PEAKS) * getScale();
	}
	
	public void setNumPeaks(int num)
	{
		num_peaks = num;
	}
	
	public void randomizeNumPeaks()
	{
		num_peaks = rand.nextInt(MAX_PEAKS) * getScale();
	}
	
	@Override
	public void generate()
	{
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getHeight(); y++)
			{
				setAltitude(new Point(x, y), 0);
			}
		}
		
		int count = num_peaks;
		
		while (count > 0)
		{
			int peak_height = rand.nextInt(MAX_HEIGHT);
			int peak_slope = rand.nextInt(10) + 1;
			Point peak = new Point(rand.nextInt(getWidth()),rand.nextInt(getHeight()));
			
			/*
			if (getAltitude(peak) > 0)
			{
				peak_height = getAltitude(peak);
			}
			*/
			
			setAltitude(peak, peak_height);
			
			int current_x_east = peak.x + 1;
			int current_x_west = peak.x - 1;
			int current_y_north = peak.y - 1;
			int current_y_south = peak.y + 1;
			int current_height = peak_height - peak_slope;
			
			
			while (current_height > 0)
			{
				// edge case handling
				if (current_y_north < 0)
				{
					current_y_north = 0;
				}
				
				if (current_y_south >= getHeight())
				{
					current_y_south = getHeight() - 1;
				}
				
				// generates north and south edges of square
				for (int x = current_x_west; x <= current_x_east; x++)
				{
					int x_adj;
					
					if (x >= 0)
					{
						x_adj = x % getWidth();
					}
					else
					{
						x_adj = getWidth() + x;
					}
					
					Point north = new Point(x_adj, current_y_north);
					Point south = new Point(x_adj, current_y_south);
					
					if (getAltitude(north) == 0 || getAltitude(north) < current_height);
					{
						setAltitude(north,current_height);
					}
					
					if (getAltitude(south) == 0 || getAltitude(south) < current_height)
					{
						setAltitude(south,current_height);
					}
				}
				
				// generates the east and west edges of the square
				for (int y = current_y_north + 1; y <= current_y_south - 1; y++)
				{
					
					Point east = new Point(current_x_east % getWidth(), y);
					Point west; 
					
					if (current_x_west < 0)
					{
						west = new Point(360 + current_x_west, y);
					}
					else
					{
						west = new Point(current_x_west, y);
					}
					
					if (getAltitude(west) == 0 || getAltitude(west) < current_height)
					{
						setAltitude(west,current_height);
					}
					
					if (getAltitude(east) == 0 || getAltitude(east) < current_height)
					{
						setAltitude(east,current_height);
					}
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

	@Override
	public HeightMap blend(int strength, int num_passes) 
	{
		if (num_passes <= 0)
			return this;

		HeightMap new_map = new SquarePeakMap(getScale());
		
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
						Point p2 = new Point(getXAdj(x + i), y + j);
						if (pointExists(p2))
						{
							average = (average + getAltitude(p2)) / 2;
						}
					}
				}
				new_map.setAltitude(p, average);
			}
		}
		
		return new_map.blend(strength, num_passes - 1);
	}

	@Override
	public HeightMap addNoise(int strength) {
		HeightMap new_map = new SquarePeakMap(getScale());
		
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getHeight(); y++)
			{
				int noise_value = rand.nextInt(255);
				Point p = new Point(x, y);
				
				new_map.setAltitude(p, (noise_value + getAltitude(p) * 5) / 6);
			}
		}
		new_map = new_map.blend(10, 4);
		return new_map;
	}
}
