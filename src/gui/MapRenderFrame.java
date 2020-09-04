package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mapgen.*;

public class MapRenderFrame extends JFrame {

	private static final long serialVersionUID = -349577414898672371L;
	private static ImagePanel map_panel;
	
	private BiomePlanet planet;
	
	private static JSlider water_slider;
	private static JLabel water_level_label;
	private static JLabel tilt_label;
	private static JSlider tilt_slider;
	
	public MapRenderFrame(BiomePlanet planet)
	{
		super("Map Renderer");
        setSize(1130,660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        this.planet = planet;
        
        JFileChooser file_chooser = new JFileChooser();
        file_chooser.setSelectedFile(new File("map.png"));
        
        JMenuBar menu_bar = new JMenuBar();
        JMenu file_menu = new JMenu("File");
        
        JMenuItem export_image = new JMenuItem("Save as PNG");
        export_image.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = file_chooser.showSaveDialog(MapRenderFrame.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                try {
						ImageIO.write(map_panel.getImage(), "png", file_chooser.getSelectedFile());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	                }
			}
        	
        });
        
        file_menu.add(export_image);
        menu_bar.add(file_menu);
        

        
		map_panel = new ImagePanel(createImage());
		JPanel info_panel = createInfoPanel();
		
		setJMenuBar(menu_bar);
		add(map_panel);
        add(info_panel, BorderLayout.SOUTH);
	}
	
	public void randomize()
	{
		planet.randomize();
		
		water_slider.setValue(planet.getWaterLevel());
		water_level_label.setText("Water Level: " + water_slider.getValue());
		tilt_slider.setValue((int) planet.getTilt());
		tilt_label.setText("Axial Tilt: " + tilt_slider.getValue());
		tilt_label.setText("Axial Tilt: " + planet.getTilt());
		
		map_panel.setImage(createImage());
		map_panel.revalidate();
		map_panel.repaint();
		
	}
	
    public BufferedImage createImage()
    {
		int coast_colour = new Color(30, 90, 124).getRGB();
		int ocean_colour = new Color(26, 74, 94).getRGB();
		int deep_ocean_colour = new Color(17, 67, 92).getRGB();
		int frigid_mountain_colour = new Color(252, 253, 255).getRGB();
		int frigid_colour = new Color(228, 231, 238).getRGB();
		//int frigid_water = new Color(185, 190, 209).getRGB();
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
    	
    	/*
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
    	*/
    	return image;
    }
    
    private JPanel createInfoPanel()
    {
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
        
		
        // WATER LEVEL SLIDER
		water_level_label = new JLabel("Water Level:");
		water_slider = new JSlider(JSlider.HORIZONTAL, 0, HeightMap.MAX_HEIGHT, this.planet.getWaterLevel());
		
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
        
        
        // AXIAL TILT SLIDER
        tilt_label = new JLabel("Axial Tilt:");
        tilt_slider = new JSlider(JSlider.HORIZONTAL, 0, 90, (int) this.planet.getTilt());
		info_panel.add(tilt_label);
		info_panel.add(tilt_slider);
		tilt_slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				planet.setTilt(tilt_slider.getValue());
				tilt_label.setText("Axial Tilt: " + tilt_slider.getValue());
				map_panel.setImage(createImage());
				map_panel.repaint();
			}
		});
		
		return info_panel;
    }
}
