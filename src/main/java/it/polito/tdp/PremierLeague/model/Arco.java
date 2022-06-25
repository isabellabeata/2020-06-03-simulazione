package it.polito.tdp.PremierLeague.model;

public class Arco {

	private int giocatore1;
	private int giocatore2;
	private double delta;
	
	
	
	public Arco(int giocatore1, int giocatore2, double delta) {
		super();
		this.giocatore1 = giocatore1;
		this.giocatore2 = giocatore2;
		this.delta=delta;
	}
	public int getGiocatore1() {
		return giocatore1;
	}
	public void setGiocatore1(int giocatore1) {
		this.giocatore1 = giocatore1;
	}
	public int getGiocatore2() {
		return giocatore2;
	}
	public void setGiocatore2(int giocatore2) {
		this.giocatore2 = giocatore2;
	}
	public double getDelta() {
		return delta;
	}
	public void setDelta(int delta) {
		this.delta = delta;
	}
	
	
	
}
