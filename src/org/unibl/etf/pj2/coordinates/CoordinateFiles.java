package org.unibl.etf.pj2.coordinates;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Files with one "x,y" point per line. Each file is read only once. */
public final class CoordinateFiles {

	private static final Map<String, List<int[]>> CACHE = new ConcurrentHashMap<>();

	private CoordinateFiles() {
	}

	public static List<int[]> load(File file) throws Exception {
		List<int[]> points = CACHE.get(file.getPath());
		if (points == null) {
			points = read(file);
			CACHE.put(file.getPath(), points);
		}
		return points;
	}

	/** The point that follows (x, y), or null if (x, y) is the last point. */
	public static int[] next(List<int[]> points, int x, int y) throws Exception {
		for (int i = 0; i < points.size(); i++) {
			int[] p = points.get(i);
			if (p[0] == x && p[1] == y) {
				return i + 1 < points.size() ? points.get(i + 1) : null;
			}
		}
		throw new Exception("Coordinates (" + x + "," + y + ") are not located on the line.");
	}

	private static List<int[]> read(File file) throws Exception {
		List<int[]> points = new ArrayList<>();
		try {
			for (String line : Files.readAllLines(file.toPath())) {
				if (line.trim().isEmpty()) {
					continue;
				}
				String[] tmp = line.trim().split(",");
				if (tmp.length != 2) {
					throw new NumberFormatException();
				}
				points.add(new int[] { Integer.parseInt(tmp[0].trim()), Integer.parseInt(tmp[1].trim()) });
			}
		} catch (IOException e) {
			throw new Exception("Failed opening '" + file.getPath() + "'.");
		} catch (NumberFormatException e) {
			throw new Exception("The coordinates in the '" + file.getPath()
					+ "' document are not specified in the correct format.");
		}
		if (points.isEmpty()) {
			throw new Exception("The '" + file.getPath() + "' document contains no coordinates.");
		}
		return points;
	}
}
