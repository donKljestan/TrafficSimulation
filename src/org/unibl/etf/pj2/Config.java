package org.unibl.etf.pj2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/** Paths from 'configuration.txt'. The file is read once; relative paths are resolved against the working directory. */
public final class Config {

	public static final String FILE_PATH = System.getProperty("simulation.config",
			System.getProperty("user.dir") + File.separator + "configuration.txt");
	public static final String FILE_NAME = new File(FILE_PATH).getName();

	public static final String RAILWAY_COORDINATES = "RAILWAY COORDINATE DIRECTORY";
	public static final String LEVEL_CROSSINGS = "LEVEL CROSSINGS DIRECTORY";
	public static final String RAILWAY_STATIONS = "COORDINATES OF RAILWAY STATIONS DIRECTORY";
	public static final String DIRECTIONS = "DIRECTIONS FOR THE NEXT STATIONS";
	public static final String TRAFFIC_SIMULATION = "TRAFFIC SIMULATION DIRECTORY";
	public static final String ROAD_COORDINATES = "COORDINATES OF ROADS DIRECTORY";
	public static final String ROAD_LEVEL_CROSSINGS = "LEVEL CROSSINGS ON ROADS DIRECTORY";
	public static final String MOVEMENTS = "MOVEMENTS DIRECTORY";
	public static final String TRAINS = "TRAINS";
	public static final String VEHICLES_SECTION = "VEHICLES#";

	private static Map<String, String> paths;

	private Config() {
	}

	/** Returns the path for the given key, with separators adapted to the current OS. */
	public static synchronized String get(String key) throws Exception {
		if (paths == null) {
			paths = load();
		}
		String path = paths.get(key.toUpperCase());
		if (path == null || path.isEmpty()) {
			throw new Exception("In the '" + FILE_NAME + "' document there is no path for '" + key + "'.");
		}
		return path;
	}

	private static Map<String, String> load() throws Exception {
		Map<String, String> result = new HashMap<>();
		try (BufferedReader bf = Files.newBufferedReader(new File(FILE_PATH).toPath())) {
			String line;
			while ((line = bf.readLine()) != null && !line.toUpperCase().startsWith(VEHICLES_SECTION)) {
				int separator = line.indexOf("::");
				if (separator > 0) {
					String value = line.substring(separator + 2).trim()
							.replace('\\', File.separatorChar).replace('/', File.separatorChar);
					result.put(line.substring(0, separator).trim().toUpperCase(), value);
				}
			}
		} catch (IOException e) {
			throw new Exception("Failed opening '" + FILE_NAME + "'.");
		}
		return result;
	}
}
