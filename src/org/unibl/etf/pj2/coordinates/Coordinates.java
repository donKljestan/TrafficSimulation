package org.unibl.etf.pj2.coordinates;

import java.io.Serializable;
import java.util.LinkedList;

public class Coordinates implements Serializable  {
	
	private LimitedQueue<Integer> positionsX;
	private LimitedQueue<Integer> positionsY;
	
	public Coordinates() {
		super();
	}
	
	public Coordinates(int length) {
		super();
		positionsX = new LimitedQueue<Integer>(length);
		positionsY = new LimitedQueue<Integer>(length);
	}
	
	public Coordinates(LimitedQueue<Integer> positionsX, LimitedQueue<Integer> positionsY) {
		super();
		this.positionsX = positionsX;
		this.positionsY = positionsY;
	}
	
	public Coordinates(Integer x, Integer y) {
		super();
		positionsX = new LimitedQueue<Integer>(1);
		positionsY = new LimitedQueue<Integer>(1);
		positionsX.add(x);
		positionsY.add(y);
	}
	
	/*Checks whether the coordinates x and y are contained in positionsX and positionsY.*/
	public boolean checkCoordinates(Integer x, Integer y) {
		return (positionsX.contains(x) && positionsY.contains(y));
		//note: since railway stations are square, this is correct
		//if the stations were not square, the method should return true
		//only when x and y are found at the same index in positionsX and positionsY
	}
	
	public Integer getLastX() {
		return positionsX.getLast();
	}
	
	public Integer getLastY() {
		return positionsY.getLast();
	}
	
	public Integer getX(int i) {
		return positionsX.get(i);
	}
	
	public Integer getY(int i) {
		return positionsY.get(i);
	}
	
	public Integer getFirstX() {
		return positionsX.getFirst();
	}
	
	public Integer getFirstY() {
		return positionsY.getFirst();
	}
	public void add(Coordinates coordinate) {
		this.positionsX.add(coordinate.getFirstX());
		this.positionsY.add(coordinate.getFirstY());
	}
	public void addX(Integer value) {
		this.positionsX.add(value);
	}
	public void addY(Integer value) {
		this.positionsY.add(value);
	}
	
	public void setPositionsX(int i, Integer value) {
		positionsX.set(i, value);
	}
	
	public void setPositionsY(int i, Integer value) {
		positionsY.set(i, value);
	}
	
	public void setPositionsX(LimitedQueue<Integer> positionsX) {
		this.positionsX = positionsX;
	}
	
	public void setPositionsY(LimitedQueue<Integer> positionsY) {
		this.positionsY = positionsY;
	}
	
	public LimitedQueue<Integer> getPositionsX() {
		return this.positionsX;
	}
	
	public LimitedQueue<Integer> getPositionsY() {
		return this.positionsY;
	}
	
	@Override
	public String toString() {
		return "X: " + String.valueOf(positionsX) + "\nY: " + String.valueOf(positionsY);
	}
	
	public class LimitedQueue<E> extends LinkedList<E> {

	    private int limit;

	    public LimitedQueue(int limit) {

	        this.limit = limit;
	        for(int i = 0; i < limit; i++) {
	        	super.add(null);
	        }
	    }

	    @Override
	    public boolean add(E o) {

	        super.add(o);
	        while (size() > limit) { super.remove(); }
	        return true;
	    }
	}
}
