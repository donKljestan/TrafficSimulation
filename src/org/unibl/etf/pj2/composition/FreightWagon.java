package org.unibl.etf.pj2.composition;

public final class FreightWagon extends Wagon {

	private int maxCapacity;
	
	public FreightWagon() {
		super();
	}
	
	public FreightWagon(int length, String mark, int maxCapacity) {
		super(length, mark);
		this.maxCapacity = maxCapacity;
	}
	public int getMaxCapacity() {
		return maxCapacity;
	}
	
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	
	@Override
	public String toString() {
		return "\nFreightWagon: " + mark + "\nMax capacity: " + maxCapacity
				+ "\nLength: " + length;
	}

}
