package org.unibl.etf.pj2.display;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.unibl.etf.pj2.*;

public class DisplayMap extends JFrame {

	private static final String[] ICON_NAMES = { "car", "truck", "electricLocomotive", "locomotive", "wagon" };
	private static final String[] ICON_ERRORS = { "car", "truck", "electric locomotive", "locomotive", "wagon" };
	private static final int FIELD_SIZE = 10;

	private static class Sprite {
		final Image image;
		final int x;
		final int y;

		Sprite(Image image, int x, int y) {
			this.image = image;
			this.x = x;
			this.y = y;
		}
	}

	private final BackgroundPanel back;
	private final Map<String, ImageIcon> icons = new HashMap<>();
	private final Map<String, int[]> mapping = new HashMap<>();
	private volatile List<Sprite> sprites = new ArrayList<>();

	public DisplayMap() throws Exception {
		
		setUndecorated(true);
		setBackground(new Color(0,0,0,0));
		
		File dir = new File(Config.get(Config.TRAFFIC_SIMULATION));
		File mapImage = new File(dir, "map.png");
		if(!mapImage.isFile()) {
			throw new Exception("Unable to load map.");
		}
		back = new BackgroundPanel(new ImageIcon(mapImage.getPath()));
		back.setBackground(new Color(0,0,0,0));
		
		setSize(617,554);
		setLocation(400, 100);	
		getContentPane().add(back);
		
		for(int i = 0; i < ICON_NAMES.length; i++) {
			File icon = new File(dir, ICON_NAMES[i] + ".png");
			if(!icon.isFile()) {
				throw new Exception("Unable to load " + ICON_ERRORS[i] + " symbol.");
			}
			icons.put(ICON_NAMES[i], new ImageIcon(icon.getPath()));
		}
		
		/*Field coordinates on the map in pixels: "x,y:X,Y".*/
		for(String line : Files.readAllLines(new File(dir, "mappingCoordinates.txt").toPath())) {
			if(line.trim().isEmpty()) {
				continue;
			}
			String[] tmp = line.split(":");
			String[] XY = tmp[1].split(",");
			mapping.put(tmp[0].trim(), new int[] { Integer.parseInt(XY[0].trim()), Integer.parseInt(XY[1].trim()) });
		}
	}

	/*Draws the whole map at once. 'objects[x][y]' is the symbol name, or null if the field is empty.
	 * The whole frame is replaced in one step, so there is no flicker.*/
	public void render(String[][] objects) {
		List<Sprite> frame = new ArrayList<>();
		for(int x = 0; x < objects.length; x++) {
			for(int y = 0; y < objects[x].length; y++) {
				ImageIcon icon = objects[x][y] == null ? null : icons.get(objects[x][y]);
				int[] XY = mapping.get(x + "," + y);
				if(icon != null && XY != null) {
					frame.add(new Sprite(icon.getImage(), 
							XY[0] + (FIELD_SIZE - icon.getIconWidth()) / 2,
							XY[1] + (FIELD_SIZE - icon.getIconHeight()) / 2));
				}
			}
		}
		sprites = frame;
		back.repaint();
	}

	class BackgroundPanel extends JPanel {
		
		ImageIcon icon;
		
		public BackgroundPanel(ImageIcon icon) {
			this.icon = icon;
		}
		
		public void setIcon(ImageIcon icon) {
			this.icon = icon;
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(this.icon != null) {
				g.drawImage(icon.getImage(), 0, 0, this);
			}
			for(Sprite s : sprites) {
				g.drawImage(s.image, s.x, s.y, this);
			}
		}
	}

}
