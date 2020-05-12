package mapgen;

import java.util.Random;

public class ContinentMap extends HeightMap {
	
	private Random rand;
	private int num_continents;
	
	public ContinentMap()
	{
		super();
		rand = new Random();
		num_continents = 1;
	}
	
	public ContinentMap(int scale)
	{
		super(scale);
		rand = new Random();
		num_continents = 1;
	}
	@Override
	public void generate() {
		int count = num_continents;
		
		while (count > 0)
		{
			
			count--;
		}
		// TODO Auto-generated method stub

	}

}
