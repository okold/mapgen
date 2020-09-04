package gui;

import mapgen.BiomePlanet;
import mapgen.RecursivePeakMap;

public class Main {

	public static void main(String[] args) 
	{
		BiomePlanet planet = new BiomePlanet(23.5, 100, new RecursivePeakMap(3));
		MapRenderer window = new MapRenderer(planet);
		window.setVisible(true);
	}

}
