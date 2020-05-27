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
	
	private static HeightMap height_map;
	private static int water_level;
	private static TiltedPlanet tilt;
	
	private static JSlider water_slider;
	private static JLabel water_level_label;
	private static JLabel tilt_label;
	
	public static void main(String[] args)
	{
		height_map = new SquarePeakMap(3);
		water_level = 100;
		tilt = new TiltedPlanet();
		rand = new Random();
		map_panel = new ImagePanel(createImage());
		
		JPanel info_panel = new JPanel();

		water_level_label = new JLabel("Water Level:");
		tilt_label = new JLabel("Axial Tilt:");
		water_slider = new JSlider(JSlider.HORIZONTAL, 0, height_map.MAX_HEIGHT, water_level);
		
		JButton randomize_button = new JButton("Randomize");
		info_panel.add(water_level_label);
		info_panel.add(water_slider);
		info_panel.add(tilt_label);
		info_panel.add(randomize_button);
		
		MapRenderer window = new MapRenderer();
		window.add(map_panel);
        window.add(info_panel, BorderLayout.SOUTH);
		
        randomize_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				randomize();
			}
        });
        
        water_slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				water_level = water_slider.getValue();
				water_level_label.setText("Water Level: " + water_level);
				map_panel.setImage(createImage());
				map_panel.repaint();
				
			}
        	
        });
        
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
		height_map = new SquarePeakMap(3);
		height_map.generate();
		height_map = height_map.blend(2, 50);
		water_level = rand.nextInt(255);
		water_slider.setValue(water_level);
		tilt.setTilt(rand.nextInt(90));
		map_panel.setImage(createImage());
		map_panel.revalidate();
		map_panel.repaint();
		water_level_label.setText("Water Level: " + water_level);
		tilt_label.setText("Axial Tilt: " + tilt.getTilt());
	}
	
    public static BufferedImage createImage()
    {
    	Point p = new Point(0,0);
    	BufferedImage image = new BufferedImage(height_map.getWidth(),height_map.getHeight(),BufferedImage.TYPE_INT_ARGB);
    	
    	for (p.y = 0; p.y < height_map.getHeight(); p.y++)
		{
			for (p.x = 0; p.x < height_map.getWidth(); p.x++)
			{
				int altitude = height_map.getAltitude(p);
				int depth = HeightMap.MAX_HEIGHT - water_level;
				
				if (altitude <= water_level)
				{
					if (altitude < 0.75 * water_level)
					{
						image.setRGB(p.x, p.y, new Color(153, 204, 255).getRGB());
					}
					else
					{
						image.setRGB(p.x, p.y, new Color(204, 229, 255).getRGB());
					}
				}
				else if (tilt.isFrigid(height_map.getLatitude(p.y)))
				{
					image.setRGB(p.x, p.y, Color.WHITE.getRGB());
				}
				else if (height_map.getAltitude(p) > HeightMap.MAX_HEIGHT - (depth / 3))
				{
					image.setRGB(p.x, p.y, Color.GRAY.getRGB());
				}
				else if (tilt.isTropic(height_map.getLatitude(p.y)))
				{
					image.setRGB(p.x, p.y, new Color(51, 102, 0).getRGB());
				}
				else
				{
					image.setRGB(p.x,p.y, new Color(102,204,0).getRGB());
				}
			}
		}
    	
    	for (p.y = 0; p.y < height_map.getHeight(); p.y++)
		{
			for (p.x = 0; p.x < height_map.getWidth(); p.x++)
			{
				if (height_map.getAltitude(p) > water_level)
				for (int i = -1; i <= 1; i++)
				{
					for (int j = -1; j <= 1; j++)
					{
						Point p2 = new Point(p.x + i, p.y + j);
						if (height_map.pointExists(p2) && height_map.getAltitude(p2) <= water_level)
						{
							image.setRGB(p.x, p.y, Color.BLACK.getRGB());
						}
					}
				}
			}
		}
    	return image;
    }
}
