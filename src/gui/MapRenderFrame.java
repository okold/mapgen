package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	private static MapPanel map_panel;
	
	private BiomePlanet planet;
	
	private static JSlider water_slider;
	private static JLabel water_level_label;
	private static JCheckBox mod_box;
	private static JCheckBox biome_box;
	private static JLabel tilt_label;
	private static JSlider tilt_slider;
	
	public MapRenderFrame(BiomePlanet planet)
	{
		super("Map Renderer");
        setSize(planet.getHeightMap().getWidth(),planet.getHeightMap().getHeight() + 90);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        this.planet = planet;
        
        JFileChooser file_chooser = new JFileChooser();
        
        JMenuBar menu_bar = new JMenuBar();
        JMenu file_menu = new JMenu("File");
        
        JMenuItem export_image = new JMenuItem("Save as PNG");
        export_image.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				file_chooser.setSelectedFile(new File("map.png"));
				int returnVal = file_chooser.showSaveDialog(MapRenderFrame.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                try {
	                	ImageIO.write(map_panel.getBufferedImage(), "png", file_chooser.getSelectedFile());
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            }
			}
        	
        });
        
        file_menu.add(export_image);
        menu_bar.add(file_menu);
        
		map_panel = new MapPanel(planet);
		JPanel info_panel = createInfoPanel();
		
		setJMenuBar(menu_bar);
		add(map_panel);
        add(info_panel, BorderLayout.SOUTH);
	}
	
	public void randomize()
	{
		planet.randomize();
		
		water_slider.setValue(planet.getWaterLevel());
		water_level_label.setText("Water Level: ");
		tilt_slider.setValue((int) planet.getTilt());
		tilt_label.setText("Axial Tilt: " + tilt_slider.getValue());
		
		map_panel.generateImage();
		
	}
    
    private JPanel createInfoPanel()
    {
    	JPanel info_panel = new JPanel();

    	// RANDOMIZE BUTTON
    			JButton randomize_button = new JButton("Randomize Height Map");
    	        randomize_button.addActionListener(new ActionListener() {

    				@Override
    				public void actionPerformed(ActionEvent e) {
    					randomize();
    				}
    	        });
    			info_panel.add(randomize_button);
    	
    	// TOGGLES
    	mod_box = new JCheckBox("Height Map Mode", false);
    	biome_box = new JCheckBox("Simulate Climate", false);
    	
        mod_box.addItemListener(new ItemListener() {   
			@Override
			public void itemStateChanged(ItemEvent e) {
				ModBoxLogic();
			}    
         });
        
        biome_box.addItemListener(new ItemListener() {   
			@Override
			public void itemStateChanged(ItemEvent e) {
				ModBoxLogic();
			}    
         });    
    	
        info_panel.add(mod_box);
        info_panel.add(biome_box);
		
        // WATER LEVEL SLIDER
		water_level_label = new JLabel("Water Level:");
		water_slider = new JSlider(JSlider.HORIZONTAL, 0, HeightMap.MAX_HEIGHT, this.planet.getWaterLevel());
		
		info_panel.add(water_level_label);
		info_panel.add(water_slider);
        water_slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				planet.setWaterLevel(water_slider.getValue());
				water_level_label.setText("Water Level:");
				map_panel.generateImage();
				
			}
        });
        
        // AXIAL TILT SLIDER
        tilt_label = new JLabel("Axial Tilt:");
        tilt_slider = new JSlider(JSlider.HORIZONTAL, 3, 44, (int) this.planet.getTilt());
		info_panel.add(tilt_label);
		info_panel.add(tilt_slider);
		tilt_slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				planet.setTilt(tilt_slider.getValue());
				tilt_label.setText("Axial Tilt: " + tilt_slider.getValue());
				map_panel.generateImage();
			}
		});
		
		return info_panel;
    }
    
    private void ModBoxLogic()
    {
    	if (mod_box.isSelected())
		{
			map_panel.setMode(0);
		}
		else
		{
			if (biome_box.isSelected())
			{
				planet.setDoHumidity(true);
				map_panel.setMode(2);
			}
			else
			{
				planet.setDoHumidity(false);
				map_panel.setMode(1);
			}
			
		}
		map_panel.generateImage();
    }
}
