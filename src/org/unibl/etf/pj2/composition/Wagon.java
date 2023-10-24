package org.unibl.etf.pj2.composition;

public abstract class Wagon {
	
		protected int length;
		protected String mark;
		
		public Wagon() {
			super();
		}
		
		public Wagon(int length, String mark) {
			super();
			this.length = length;
			this.mark = mark;
		}
		
		public int getLength() {
			return length;
		}
		
		public void setLength(int length) {
			this.length = length;
		}
		
		public String getMark() {
			return mark;
		}
		
		public void setMark(String mark) {
			this.mark = mark;
		}
		
}
