package mapgen;

import java.util.Random;

public class RandomPeakMap extends HeightMap {

	private static final int MAX_PEAKS = 100;
	private int num_peaks;
	private Random rand;
	
	public RandomPeakMap()
	{
		super();
		rand = new Random();
		num_peaks = rand.nextInt(MAX_PEAKS);
	}
	
	public RandomPeakMap(int scale)
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
		int count = num_peaks;
		
		while (count > 0)
		{
			int peak_height = rand.nextInt(256);
			int peak_slope = rand.nextInt(10) + 1;
			int peak_x = rand.nextInt(getWidth());
			int peak_y = rand.nextInt(getHeight());
			
			if (getAltitude(peak_x,peak_y) > 0)
			{
				peak_height = getAltitude(peak_x,peak_y);
			}
			
			setAltitude(peak_x, peak_y, peak_height);
			
			int current_x_east = peak_x + 1;
			int current_x_west = peak_x - 1;
			int current_y_north = peak_y - 1;
			int current_y_south = peak_y + 1;
			int current_height = peak_height - peak_slope;
			
			
			while (current_height > 0)
			{
				int x;
				int y = current_y_north;
				
				if (current_y_north < 0)
				{
					current_y_north = 0;
				}
				
				if (current_y_south >= getHeight())
				{
					current_y_south = getHeight() - 1;
				}
				
				for (x = current_x_west; x <= current_x_east; x++)
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
					
					if (getAltitude(x_adj,current_y_north) == 0)
					{
						setAltitude(x_adj,current_y_north,current_height);
					}
					else
					{
						setAltitude(x_adj,current_y_north,(getAltitude(x_adj,current_y_north) + current_height) / 2);
					}
					
					if (getAltitude(x_adj,current_y_south) == 0)
					{
						setAltitude(x_adj,current_y_south,current_height);
					}
					else
					{
						setAltitude(x_adj,current_y_south,(getAltitude(x_adj,current_y_south) + current_height) / 2);
					}
				}
				
				for (y = current_y_north + 1; y <= current_y_south - 1; y++)
				{
					int x_west_adj;
					
					if (current_x_west < 0)
					{
						x_west_adj = 360 + current_x_west;
					}
					else
					{
						x_west_adj = current_x_west;
					}
					
					if (getAltitude(x_west_adj,y) == 0)
					{
						setAltitude(x_west_adj,y,current_height);
					}
					else
					{
						setAltitude(x_west_adj,y,(getAltitude(x_west_adj,y) + current_height) / 2);
					}
					
					if (getAltitude(current_x_east % getWidth(), y) == 0)
					{
						setAltitude(current_x_east % getWidth(),y,current_height);
					}
					else
					{
						setAltitude(current_x_east % getWidth(),y,(getAltitude(current_x_east % getWidth(),y) + current_height) / 2);
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
}
