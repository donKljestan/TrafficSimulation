package org.unibl.etf.pj2.composition;


public final class PassengerWagon extends Wagon {

	private Gadget gadget;
	private TypeOfPassengerWagon type;
	
	public PassengerWagon() {
		super();
	}
	
	public PassengerWagon(int length, String power, Gadget gadget, TypeOfPassengerWagon type) {
		super(length, power);
		this.gadget = gadget;
		this.type = type;
	}
	public Gadget getGadget() {
		return gadget;
	}
	
	public void setGadget(Gadget gadget) {
		this.gadget = gadget;
	}
	
	public TypeOfPassengerWagon getType() {
		return type;
	}
	
	public void setType(TypeOfPassengerWagon type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "\nPassenger wagon: " + mark + "\nType: " + type +"\n" + gadget + "\nLength: " + length;
	}
}
