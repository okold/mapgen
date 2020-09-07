package gui;

import mapgen.BiomePlanet;
import mapgen.RecursivePeakMap;

public class Main {

	public static void main(String[] args) 
	{
		BiomePlanet planet = new BiomePlanet(23.5, 175, new RecursivePeakMap(3));
		MapRenderFrame window = new MapRenderFrame(planet);
		window.randomize();
		window.setVisible(true);
	}

}
