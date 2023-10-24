package org.unibl.etf.pj2;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import org.unibl.etf.pj2.railwayStation.*;
import org.unibl.etf.pj2.vehicle.LoadVehicle;
import org.unibl.etf.pj2.composition.LoadComposition;
import org.unibl.etf.pj2.coordinates.*;
import org.unibl.etf.pj2.display.Display;
import org.unibl.etf.pj2.display.StartFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.EventQueue;
import java.io.*;
import java.nio.file.Files;

public class Simulation {
	
	public static final int SIZE_OF_MAP_X = 30;
	public static final int SIZE_OF_MAP_Y = 30;
	public static Object[][] map = new Object[SIZE_OF_MAP_X][SIZE_OF_MAP_Y];
	public static HashMap<String, CountDownLatch> Lines = new HashMap<String, CountDownLatch>();
	public static HashMap<String, CountDownLatch> LevelCrossing = new HashMap<String, CountDownLatch>();
	public static final HashMap<String, RailwayStation> RailwayStations = loadRailwayStations();
			
	/*Loads the railway stations into a HashMap. */
	private static HashMap<String,RailwayStation> loadRailwayStations() {
		HashMap<String, RailwayStation> railwayStations = new HashMap<String, RailwayStation>();
		
		try {
			File dir = new File(Config.get(Config.RAILWAY_STATIONS));
			String[] files = dir.list();
			if(files == null) {
				throw new Exception("Error opening documents with train station coordinates.");
			}
			for(String s : files) {
				String name = s.substring(0, s.indexOf("."));
				String tmp[] = Files.readAllLines(new File(dir, s).toPath()).get(0).split(":");
				Coordinates coordinate = new Coordinates(tmp.length);
				for(String st : tmp) {
					coordinate.addX(Integer.valueOf(st.split(",")[0]));
					coordinate.addY(Integer.valueOf(st.split(",")[1]));
				}
				railwayStations.put(name, new RailwayStation(Station.valueOf(name), coordinate));
			}
			return railwayStations;
		} catch (Exception e) {
			Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE,e.toString());
			throw new IllegalStateException(e);
		}
	}
	
	/*Loads the line segments into a HashMap. */
	private static void loadLines() throws Exception {
		String[] files = new File(Config.get(Config.RAILWAY_COORDINATES)).list();
		if(files == null) {
			throw new Exception("Error opening the folder with the coordinates of the railways.");
		}
		for(String s : files) {
			Simulation.Lines.put(s.substring(0, s.indexOf(".")), new CountDownLatch(0));
		}
		Simulation.Lines.put(null, new CountDownLatch(0));
	}
	
	/*Loads the level crossings into a HashMap.*/
	private static void loadLevelCrossing() throws Exception {
		File dir = new File(Config.get(Config.LEVEL_CROSSINGS));
		String[] lines = dir.list();
		if(lines == null) {
			throw new Exception("Error opening the folder with the coordinates of the level crossings.");
		}
		for(String s : lines) {
			for(String sd : new File(dir, s).list()) {
				Simulation.LevelCrossing.put(s + ":" + sd.substring(0, sd.indexOf(".")), new CountDownLatch(0));
			}
		}
		Simulation.LevelCrossing.put(null, new CountDownLatch(0));
	}
	
	/*Creates the folder if it does not exist, otherwise deletes the old files in it.*/
	private static void clearFolder(String key) throws Exception {
		File dir = new File(Config.get(key));
		if(!dir.isDirectory() && !dir.mkdirs()) {
			throw new Exception("Unable to create the folder '" + dir.getPath() + "'.");
		}
		for(File f : dir.listFiles()) {
			f.delete();
		}
	}
	
	public static void main(String[] args) {
	
		
		try {
			clearFolder(Config.TRAINS);
			clearFolder(Config.MOVEMENTS);
			loadLines();
			loadLevelCrossing();
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						StartFrame frame = new StartFrame();
						frame.setVisible(true);
						if(Boolean.getBoolean("simulation.autostart")) {
							new Display().start();
						}
					} catch (Exception e) {
						Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE,e.toString());
					}
				}
			});
			
			LoadComposition lc = new LoadComposition();
			lc.start();
				
			LoadVehicle lv = new LoadVehicle();
			lv.start();

		} catch(Exception e) {
			Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
	

}
