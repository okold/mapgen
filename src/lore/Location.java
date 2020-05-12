package lore;

import mapgen.Coordinate;

public class Location extends LoreItem
{
	private Coordinate coord;
	
	public Location()
	{
		super();
		coord = new Coordinate(0,0);
	}
	
	public Coordinate getCoordinate()
	{
		return coord;
	}
	
	public void setCoordinate(Coordinate coord)
	{
		this.coord = coord; 
	}
}
