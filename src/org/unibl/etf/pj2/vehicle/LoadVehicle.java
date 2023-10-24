package org.unibl.etf.pj2.vehicle;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.Config;

public class LoadVehicle extends Thread {
	
	private HashMap <String, Integer> speed = new HashMap<String, Integer>();
	private HashMap <String, Integer> numberOfVehicles = new HashMap<String, Integer>();
	private HashMap <String, Integer> createdVehicles = new HashMap<String, Integer>();
	
	public LoadVehicle() throws Exception {
		super();
		init();
	}
	
	public HashMap <String, Integer> getSpeed() {
		return this.speed;
	}
	public HashMap <String, Integer> getNumberOfVehicles() {
		return this.numberOfVehicles;
	}

	/*Reads the "ROAD::SPEED::NUMBER" lines from the vehicles section. The file is read every time because it can change while the simulation runs.*/
	private List<String[]> readVehicleLines() throws Exception {
		List<String[]> lines = new ArrayList<>();
		try (BufferedReader bf = Files.newBufferedReader(new File(Config.FILE_PATH).toPath())) {
			String data;
			boolean inSection = false;
			while((data = bf.readLine()) != null) {
				if(inSection) {
					if(!data.trim().isEmpty()) {
						lines.add(data.trim().split("::"));
					}
				} else if(data.toUpperCase().startsWith(Config.VEHICLES_SECTION)) {
					inSection = true;
				}
			}
		}
		return lines;
	}

	private void init() throws Exception {
		try {
			for(String[] tmp : readVehicleLines()) {
				speed.put(tmp[0], Integer.valueOf(tmp[1]));
				numberOfVehicles.put(tmp[0], Integer.valueOf(tmp[2]));
				createdVehicles.put(tmp[0], 0);
			}
		} catch (Exception e) {
			Logger.getLogger(LoadVehicle.class.getName()).log(Level.SEVERE,e.toString());
		}
	}

	
	public void run() {
		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Path watchDir = new File(Config.FILE_PATH).getAbsoluteFile().getParentFile().toPath();
			watchDir.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);

			
			while(true) {
				CreateVehicle cv = new CreateVehicle(speed, numberOfVehicles, createdVehicles);
				cv.start();
				WatchKey key = null;
				try {
					key = watchService.take();
				} catch (InterruptedException e) {
					Logger.getLogger(LoadVehicle.class.getName()).log(Level.SEVERE,e.toString());
				}
				
				for (WatchEvent<?> event : key.pollEvents()) {
					if(String.valueOf(event.context()).equals(Config.FILE_NAME)) {
						try {
							cv.setStop(true);
							this.createdVehicles = cv.getCreatedVehicles();
							for(String[] tmp : readVehicleLines()) {
								speed.put(tmp[0], Integer.valueOf(tmp[1]));
								if(Integer.valueOf(tmp[2]) > this.numberOfVehicles.get(tmp[0])) {
									numberOfVehicles.put(tmp[0], Integer.valueOf(tmp[2]));
								}
							}
						} catch(Exception e) {
							throw new Exception("Failed read from '" + Config.FILE_NAME + "'");
						}
					}
				}
				
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
		} catch (Exception e) {
			Logger.getLogger(LoadVehicle.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
	
}
