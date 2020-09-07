package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private static MapPanel map_panel;
	
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
		water_level_label.setText("Water Level: " + water_slider.getValue());
		tilt_slider.setValue((int) planet.getTilt());
		tilt_label.setText("Axial Tilt: " + tilt_slider.getValue());
		tilt_label.setText("Axial Tilt: " + planet.getTilt());
		
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
				map_panel.generateImage();
				
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
				map_panel.generateImage();
			}
		});
		
		return info_panel;
    }
}
