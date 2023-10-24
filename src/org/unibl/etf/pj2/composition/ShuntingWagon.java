package org.unibl.etf.pj2.composition;

public final class ShuntingWagon extends Wagon {
	
	public ShuntingWagon() {
		super();
	}

	public ShuntingWagon(int length, String mark) {
		super(length,mark);
	}
	
	@Override
	public String toString() {
		return "\nShuntingWagon: " + mark + "\nLength: " + length;
	}
}
