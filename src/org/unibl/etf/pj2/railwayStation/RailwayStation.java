package org.unibl.etf.pj2.railwayStation;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.unibl.etf.pj2.Config;
import org.unibl.etf.pj2.Simulation;
import org.unibl.etf.pj2.coordinates.*;

public class RailwayStation {

	private static final Map<String, List<int[]>> CROSSINGS = new ConcurrentHashMap<>();
	private static List<String> directions;
	private static int sl = 1000;

	private Station station;
	private final Coordinates COORDINATES;
	
	public RailwayStation(Station station, Coordinates COORDINATES) {
		super();
		this.station = station;
		this.COORDINATES = COORDINATES;
	}
	public Station getStation() {
		return this.station;
	}
	
	public Coordinates getCoordinates() {
		return this.COORDINATES;
	}

	private File line(RailwayStation from, RailwayStation to) throws Exception {
		return new File(Config.get(Config.RAILWAY_COORDINATES), from.getStation() + "-" + to.getStation() + ".txt");
	}
	
	/*Sets the coordinates for a train leaving the current station towards the next station.
	 * The current station calls this method and passes the next station as the argument.
	 * The 'length' argument is the length of the composition. */
	public Coordinates setExitCoordination(RailwayStation nextStation, int length) throws Exception {
		int[] first = CoordinateFiles.load(line(this, nextStation)).get(0);
		Coordinates coordinates = new Coordinates(length);
		for(int i=0; i < length; i++) {
			coordinates.setPositionsX(i, first[0]);
			coordinates.setPositionsY(i, first[1]);
		}
		return coordinates;
	}

	/*Sets the new coordinates of the composition on the given line. The next station
	 * calls this method and passes the station the composition started from. 
	 * 'positionX' and 'positionY' are the current position of the composition. */
	public Coordinates incrementPosition(RailwayStation previousStation, int positionX, int positionY) throws Exception {
		int[] next = CoordinateFiles.next(CoordinateFiles.load(line(previousStation, this)), positionX, positionY);
		if(next == null) {
			throw new Exception("The composition is at the end of the railway line " + 
					previousStation.getStation() + "-" + this.getStation() + ".");
		}
		return new Coordinates(next[0], next[1]);
	}


	/*The next station the composition has to visit calls this method; it returns 'true' 
	 * if the composition has arrived at it.*/
	public synchronized boolean atTheStation(Integer positionX, Integer positionY) {
		return this.COORDINATES.checkCoordinates(positionX, positionY);
	}
	

	private static synchronized List<String> loadDirections() throws Exception {
		if(directions == null) {
			try {
				directions = Files.readAllLines(new File(Config.get(Config.DIRECTIONS)).toPath());
			} catch (java.io.IOException e) {
				throw new Exception("Failed opening the 'Directions' document.");
			}
		}
		return directions;
	}

	/*The station the composition is currently at calls this method. It returns 
	 * the next station the composition has to visit on its way to the destination.*/
	public RailwayStation getDirections(RailwayStation finalDestination) throws Exception {
		try {
			for(String data : loadDirections()) {
				String[] tmp = data.split("->");
				if(this.getStation().toString().equals(tmp[0]) &&
					finalDestination.getStation().toString().equals(String.valueOf(tmp[1].charAt(0)))) {
					return Simulation.RailwayStations.get(data.split(":")[1]);
				}
			}
		} catch (RuntimeException e) {
			throw new Exception("The coordinates in the 'Directions' document are not specified in "
				 		+ "the correct format.");
		}
		return null;
	}

	/*Departure delay of a train leaving the station (1 to 4 s, rotating).*/
	public long getSleep() {
		return nextSleep();
	}

	private static synchronized long nextSleep() {
		sl = sl > 3000 ? 1000 : (sl + 1000); 
		return sl;
	}
	
	/*Level crossing coordinates on the line from 'previousStation' to this station.*/
	private List<int[]> crossings(RailwayStation previousStation) throws Exception {
		String name = previousStation.getStation() + "-" + this.getStation();
		List<int[]> result = CROSSINGS.get(name);
		if(result == null) {
			File dir = new File(Config.get(Config.LEVEL_CROSSINGS), name);
			String[] files = dir.list();
			result = new ArrayList<>();
			if(files != null) {
				for(int i = 1; i <= files.length; i++) {
					result.add(CoordinateFiles.load(new File(dir, i + ".txt")).get(0));
				}
			}
			CROSSINGS.put(name, result);
		}
		return result;
	}
	
	/*Returns the number of level crossings on the line. 
	 * Called by the station the composition is heading to, with
	 * the last station the composition visited as the argument.*/
	public int numberRailwayCrossing(RailwayStation previousStation) throws Exception {
		return crossings(previousStation).size();
	}

	/*Called by the station the composition is heading to.
	 * The argument is the last station the composition visited.
	 * 'x' and 'y' are the current location of the composition and 'speed' is the speed
	 * at which it moves. Checks whether the composition has passed a level 
	 * crossing; once it has, the vehicles waiting at 
	 * the corresponding level crossing are released.*/
	public void checkRailwayCrossing(RailwayStation previousStation,
			Integer x, Integer y, int speed, boolean isElectric) throws Exception {
		List<int[]> crossings = crossings(previousStation);
		for(int i = 0; i < crossings.size(); i++) {
			int[] crossing = crossings.get(i);
			if(x == crossing[0] && y == crossing[1]) {
				Thread.sleep(speed);
				if(isElectric) {
					Thread.sleep(speed);
				}
				Simulation.LevelCrossing.get(previousStation.getStation() + "-" + 
						this.getStation() + ":" + (i + 1)).countDown();
			}
		}
	}

}
