package org.unibl.etf.pj2.display;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.Simulation;
import org.unibl.etf.pj2.composition.*;
import org.unibl.etf.pj2.vehicle.*;
public class Display extends Thread {

	public Display() {
		super();
	}
	
	private static String symbol(Object object) {
		if(object instanceof Locomotive) {
			return ((Locomotive) object).getEngine() == LocomotiveEngine.ELECTRIC ? "electricLocomotive" : "locomotive";
		} else if(object instanceof Wagon) {
			return "wagon";
		} else if(object instanceof Car) {
			return "car";
		} else if(object instanceof Truck) {
			return "truck";
		}
		return null;
	}
	
	public void run() {
		try {
			DisplayMap displayMap = new DisplayMap();
			displayMap.setVisible(true);
			while(true) {
				String[][] frame = new String[Simulation.SIZE_OF_MAP_X][Simulation.SIZE_OF_MAP_Y];
				for(int i = 0; i < Simulation.SIZE_OF_MAP_X; i++) {
					for(int j = 0; j < Simulation.SIZE_OF_MAP_Y; j++) {
						frame[i][j] = symbol(Simulation.map[i][j]);
					}
				}
				displayMap.render(frame);
				Thread.sleep(50);
			}
		} catch(Exception e) {
			Logger.getLogger(Display.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
}
