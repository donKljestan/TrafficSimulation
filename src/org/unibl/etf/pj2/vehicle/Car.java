package org.unibl.etf.pj2.vehicle;

public final class Car extends Vehicle {

	private int numberOfDoors;
	
	public Car(String mark, String model, int age, int numberOfDoors, String road) throws Exception {
		super(mark,model,age,road);
		this.numberOfDoors = numberOfDoors;
	}
	
	public void setNumberOfDoors(int numberOfDoors) {
		this.numberOfDoors = numberOfDoors;
	}
	public int getNumberOfDoors() {
		return this.numberOfDoors;
	}
}
