package org.unibl.etf.pj2.vehicle;

public class Truck extends Vehicle {

	private int capacity;
	
	public Truck(String mark, String model, int age, String road, int capacity) throws Exception {
		super(mark,model,age,road);
		this.capacity = capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public int getCapacity() {
		return capacity;
	}
}
