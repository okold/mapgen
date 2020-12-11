package gui;

public class MapPalette {

	public static int[] DEFAULT = {-14787972, -15054242, -15645860, -8610733, -7765919};
	public static int[] SEPIA = {-14787972, -15054242, -15645860, -8610733, -7765919};
	public static int[] MOON = {-14787972, -15054242, -15645860, -8610733, -7765919};
	public static int[] MARS = {-14787972, -15054242, -15645860, -8610733, -7765919};
	
	public int coast_colour;
	public int ocean_colour;
	public int deep_ocean_colour;
	public int land_colour;
	public int mountain_colour;

	public MapPalette(int[] colour) {
		super();
		this.coast_colour = colour[0];
		this.ocean_colour = colour[1];
		this.deep_ocean_colour = colour[2];
		this.land_colour = colour[3];
		this.mountain_colour = colour[4];
	}
}
