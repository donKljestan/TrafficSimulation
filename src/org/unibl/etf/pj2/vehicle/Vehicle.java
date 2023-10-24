package org.unibl.etf.pj2.vehicle;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.Config;
import org.unibl.etf.pj2.Simulation;
import org.unibl.etf.pj2.coordinates.CoordinateFiles;

public abstract class Vehicle extends Thread {

	/*Level crossing on a road: the point where the vehicle stops and the latches it has to wait for.*/
	private static class RoadCrossing {
		final int[] point;
		final List<String> latches;

		RoadCrossing(int[] point, List<String> latches) {
			this.point = point;
			this.latches = latches;
		}
	}

	private static final Map<String, List<RoadCrossing>> ROAD_CROSSINGS = new ConcurrentHashMap<>();

	protected String mark;
	protected String model;
	protected int age;
	protected int speed;
	protected String road;
	protected int positionX;
	protected int positionY;
	protected boolean finish = false;
	
	public Vehicle(String mark, String model, int age, String road) throws Exception {
		super();
		this.mark = mark;
		this.model = model;
		this.age = age;
		this.road = road;
		setStartCoordinates();
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	private List<int[]> roadPoints() throws Exception {
		return CoordinateFiles.load(new File(Config.get(Config.ROAD_COORDINATES), road + ".txt"));
	}

	protected void setStartCoordinates() throws Exception {
		int[] start = roadPoints().get(0);
		positionX = start[0];
		positionY = start[1];
	}

	protected void incrementPosition() throws Exception {
		int[] next = CoordinateFiles.next(roadPoints(), positionX, positionY);
		if(next == null) {
			finish = true;
			return;
		}
		positionX = next[0];
		positionY = next[1];
	}

	private List<RoadCrossing> roadCrossings() throws Exception {
		List<RoadCrossing> result = ROAD_CROSSINGS.get(road);
		if(result == null) {
			File dir = new File(Config.get(Config.ROAD_LEVEL_CROSSINGS), road);
			String[] files = dir.list();
			result = new ArrayList<>();
			if(files != null) {
				for(int i = 1; i <= files.length; i++) {
					File crossingDir = new File(dir, String.valueOf(i));
					int[] point = CoordinateFiles.load(new File(crossingDir, "Coordinates.txt")).get(0);
					List<String> latches = Files.readAllLines(new File(crossingDir, "LevelCrossing.txt").toPath());
					result.add(new RoadCrossing(point, latches));
				}
			}
			ROAD_CROSSINGS.put(road, result);
		}
		return result;
	}
	
	protected void checkRailwayCrossing() throws Exception {
		try {
			for(RoadCrossing crossing : roadCrossings()) {
				if(crossing.point[0] == positionX && crossing.point[1] == positionY) {
					for(String latch : crossing.latches) {
						Simulation.LevelCrossing.get(latch).await();
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE,e.toString());
		}
	}

	public void run() {
		int previousX = this.positionX, previousY = this.positionY;
		try {
			while(!finish) {
				while((Simulation.map[this.positionX][this.positionY]) != null) {
					Thread.sleep(100);
				}
				Simulation.map[previousX][previousY] = null;
				Simulation.map[this.positionX][this.positionY] = this;
				Thread.sleep(speed);
				checkRailwayCrossing();
				previousX = this.positionX;
				previousY = this.positionY;
				incrementPosition();
				
			}
		} catch(Exception e) {
			Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE,e.toString());
		}
		System.out.println("Vehicle " + this.mark + " " + model + " has completed"
				+ " its movement on the road " + this.road +  ".");
		Simulation.map[this.positionX][this.positionY] = null;
	}
}
