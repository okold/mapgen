package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mapgen.*;

public class MapRenderer extends JFrame {

	private static final long serialVersionUID = -349577414898672371L;
	private static ImagePanel map_panel;
	
	private static Random rand;
	private static BiomePlanet planet;
	
	private static JSlider water_slider;
	private static JLabel water_level_label;
	private static JLabel tilt_label;
	
	public static void main(String[] args)
	{
		HeightMap height_map = new SquarePeakMap(3);
		int water_level = 100;
		double tilt = 23.5;
		planet = new BiomePlanet(tilt, water_level, height_map);
		
		rand = new Random();
		map_panel = new ImagePanel(createImage());
		
		JPanel info_panel = new JPanel();

		// RANDOMIZE BUTTON
		JButton randomize_button = new JButton("Randomize");
        randomize_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				randomize();
			}
        });
		info_panel.add(randomize_button);
        
		
		// BLEND BUTTON
		JButton blend_button = new JButton("Blend");
        blend_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				planet.setHeightMap(planet.getHeightMap().blend(2, 1));
				map_panel.setImage(createImage());
				map_panel.revalidate();
				map_panel.repaint();
				
			}
        });
		info_panel.add(blend_button);
		
		
        // WATER LEVEL SLIDER
		water_level_label = new JLabel("Water Level:");
		water_slider = new JSlider(JSlider.HORIZONTAL, 0, HeightMap.MAX_HEIGHT, water_level);
		
		info_panel.add(water_level_label);
		info_panel.add(water_slider);
        water_slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				planet.setWaterLevel(water_slider.getValue());
				water_level_label.setText("Water Level: " + water_slider.getValue());
				map_panel.setImage(createImage());
				map_panel.repaint();
				
			}
        	
        });
        
        
        // TODO: AXIAL TILT SLIDER
        tilt_label = new JLabel("Axial Tilt:");
		info_panel.add(tilt_label);

		
		MapRenderer window = new MapRenderer();
		window.add(map_panel);
        window.add(info_panel, BorderLayout.SOUTH);
        window.setVisible(true);
	}

	public MapRenderer()
	{
		super("Map Renderer");
        setSize(1130,660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}
	
	public static void randomize()
	{
		HeightMap height_map = new RecursivePeakMap(3);
		height_map.generate();
		int water_level = rand.nextInt(255);
		
		planet.setTilt(rand.nextInt(90));
		planet.setHeightMap(height_map);
		planet.setWaterLevel(water_level);
		
		map_panel.setImage(createImage());
		map_panel.revalidate();
		map_panel.repaint();
		water_level_label.setText("Water Level: " + water_level);
		tilt_label.setText("Axial Tilt: " + planet.getTilt());
	}
	
    public static BufferedImage createImage()
    {
		int coast_colour = new Color(30, 90, 124).getRGB();
		int ocean_colour = new Color(26, 74, 94).getRGB();
		int deep_ocean_colour = new Color(17, 67, 92).getRGB();
		int frigid_mountain_colour = new Color(252, 253, 255).getRGB();
		int frigid_colour = new Color(228, 231, 238).getRGB();
		int mountain_colour = new Color(137, 128, 97).getRGB();
		int temperate_colour = new Color(124, 156, 83).getRGB();
		int tropical_colour = new Color(87, 133, 68).getRGB();
		
    	Point p = new Point(0,0);
    	
    	HeightMap height_map = planet.getHeightMap();
    	
    	BufferedImage image = new BufferedImage(height_map.getWidth(),height_map.getHeight(),BufferedImage.TYPE_INT_ARGB);
    	
    	for (p.y = 0; p.y < height_map.getHeight(); p.y++)
		{
			for (p.x = 0; p.x < height_map.getWidth(); p.x++)
			{
				
				if (planet.isCoast(p))
				{
					image.setRGB(p.x, p.y, coast_colour);
				}
				else if(planet.isOcean(p))
				{
					image.setRGB(p.x, p.y, ocean_colour);
				}
				else if(planet.isDeepOcean(p))
				{
					image.setRGB(p.x, p.y, deep_ocean_colour);
				} 
				else if(planet.isFrigid(p))
				{
					if(planet.isMountain(p))
						image.setRGB(p.x, p.y, frigid_mountain_colour);
					else
						image.setRGB(p.x, p.y, frigid_colour);
				}
				else if(planet.isTemperate(p))
				{
					if(planet.isMountainPeak(p))
						image.setRGB(p.x, p.y, frigid_mountain_colour);
					else if (planet.isMountain(p))
						image.setRGB(p.x, p.y, mountain_colour);
					else
						image.setRGB(p.x, p.y, temperate_colour);
				}
				else
				{
					if (planet.isMountain(p))
						image.setRGB(p.x, p.y, mountain_colour);
					else
						image.setRGB(p.x, p.y, tropical_colour);
				}
			}
		}
    	
    	/* This works, but is STUPIDLY EXPENSIVE
    	// Borders between deep/mid ocean
    	for (p.y = 0; p.y < height_map.getHeight(); p.y++)
		{
			for (p.x = 0; p.x < height_map.getWidth(); p.x++)
			{
				if (planet.isOcean(p))
				for (int i = -10; i <= 10; i++)
				{
					for (int j = -10; j <= 10; j++)
					{
						Point p2 = new Point(p.x + i, p.y + j);
						if (height_map.pointExists(p2) && planet.isDeepOcean(p2))
						{
							image.setRGB(p.x, p.y, (ocean_colour + deep_ocean_colour) / 2);
						}
					}
				}
			}
		}
    	*/
    	
    	// Black borders around coastlines
    	if (planet.getWaterLevel() > 0)
    	{
    		for (p.y = 0; p.y < height_map.getHeight(); p.y++)
    		{
    			for (p.x = 0; p.x < height_map.getWidth(); p.x++)
    			{
    				if (height_map.getAltitude(p) > planet.getWaterLevel())
    				for (int i = -1; i <= 1; i++)
    				{
    					for (int j = -1; j <= 1; j++)
    					{
    						Point p2 = new Point(p.x + i, p.y + j);
    						if (height_map.pointExists(p2) && height_map.getAltitude(p2) <= planet.getWaterLevel())
    						{
    							image.setRGB(p.x, p.y, Color.BLACK.getRGB());
    						}
    					}
    				}
    			}
    		}
    	}
    	
    	
    	return image;
    }
}
