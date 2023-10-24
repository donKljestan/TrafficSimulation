package org.unibl.etf.pj2.composition;



public class Locomotive {
	
	private int power;
	private String mark;
	private LocomotiveType type;
	private LocomotiveEngine engine;
	
	public Locomotive() {
		super();
	}
	
	public Locomotive(int power, String mark, LocomotiveType type, LocomotiveEngine engine) {
		super();
		this.power = power;
		this.mark = mark;
		this.type = type;
		this.engine = engine;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public void setType(LocomotiveType type) {
		this.type = type;
	}
	
	public void setEngine(LocomotiveEngine engine) {
		this.engine = engine;
	}
	
	public int getPower() {
		return power;
	}
	
	public String getmark() {
		return mark;
	}
	
	public LocomotiveType getType() {
		return type;
	}
	
	public LocomotiveEngine getEngine() {
		return engine;
	}
	
	@Override
	public String toString() {
		return "\nLocomotive: " + mark + "\nType: " + type + 
				"\nEngine: " + engine + "\nPower: " + power;
	}

}
