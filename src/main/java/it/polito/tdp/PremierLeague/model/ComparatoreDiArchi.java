package it.polito.tdp.PremierLeague.model;

import java.util.Comparator;


public class ComparatoreDiArchi implements Comparator<Arco> {

	@Override
	public int compare(Arco o1, Arco o2) {
		// TODO Auto-generated method stub
		return -(int) (o1.getDelta()-o2.getDelta());
	}



}
