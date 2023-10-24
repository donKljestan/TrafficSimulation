package org.unibl.etf.pj2.vehicle;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;



public class CreateVehicle extends Thread {
	
	private HashMap <String, Integer> speed = new HashMap<String, Integer>();
	private HashMap <String, Integer> numberOfVehicles = new HashMap<String, Integer>();
	private HashMap <String, Integer> createdVehicles = new HashMap<String, Integer>();
	private boolean stop = false;
	
	public CreateVehicle( HashMap <String, Integer> speed, 
			 				HashMap <String, Integer> numberOfVehicles,
			 				HashMap <String, Integer> createdVehicles) throws Exception {
		super();
		this.speed = speed;
		this.numberOfVehicles = numberOfVehicles;
		this.createdVehicles = createdVehicles;
	}
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	public HashMap <String, Integer> getCreatedVehicles() {
		return this.createdVehicles;
	}

	public void run() {
		Random rand = new Random();
		List<String> keysAsArray = new ArrayList<String>(speed.keySet());
		while(!stop) {
			String randomKey = keysAsArray.get(rand.nextInt(keysAsArray.size()));
			try {
				if(this.createdVehicles.get(randomKey) < this.numberOfVehicles.get(randomKey)) {
					/*Creating vehicle*/
					if(rand.nextBoolean() == true) { //create car
						Car car = new Car("Ford", "Focus", 10, 4, randomKey.substring(1 +
								randomKey.indexOf("_")));
						car.setSpeed(rand.nextInt(2000) + this.speed.get(randomKey));
						car.start();
					} else { //create truck
						Truck truck = new Truck("Mercedes", "100", 10, randomKey.substring(1 +
								randomKey.indexOf("_")), 5000);
						truck.setSpeed(rand.nextInt(2000) + this.speed.get(randomKey));
						truck.start();
					}
					this.createdVehicles.put(randomKey, this.createdVehicles.get(randomKey) + 1);
				}
				Thread.sleep(2000);
			}
			catch (Exception e) {
				Logger.getLogger(CreateVehicle.class.getName()).log(Level.SEVERE,e.toString());
			}

		}
	
	}
}
