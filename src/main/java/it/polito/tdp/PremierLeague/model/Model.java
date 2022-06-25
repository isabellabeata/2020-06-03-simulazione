package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	
	double grado=0;
	Graph<Player, DefaultWeightedEdge> grafo;
	PremierLeagueDAO dao;
	List<Player> vertici;
	Map<Integer, Player> idMap;

	List<Player> best;


	public Model() {
		this.dao= new PremierLeagueDAO();


	}

	public void creaGrafo(double goal) {

		this.grafo= new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.idMap= new HashMap<Integer, Player>();
		this.vertici= new ArrayList<Player>(dao.getVertici(goal, idMap));
		Graphs.addAllVertices(this.grafo, vertici);

		for(Arco a : this.dao.getArchi(idMap)){
			if(a.getDelta()>0) {
				Graphs.addEdgeWithVertices(this.grafo, idMap.get(a.getGiocatore1()), idMap.get(a.getGiocatore2()), a.getDelta());
			}else if( a.getDelta()>0) {
				Graphs.addEdgeWithVertices(this.grafo, idMap.get(a.getGiocatore2()), idMap.get(a.getGiocatore1()), -(a.getDelta()));
			}
		}


	}


	public String nVertici() {
		return "Grafo creato!"+"\n"+"#verici: "+ this.grafo.vertexSet().size()+"\n";
	}

	public String nArchi() {
		return "#archi: "+ this.grafo.edgeSet().size()+"\n";
	}

	public String topPlayer() {
		int max=0;
		Player p=null;
		String s="";
		List<Arco> list= new LinkedList<Arco>();
		for(Player pi: this.grafo.vertexSet()) {
			int cnt= this.grafo.outDegreeOf(pi);
			if(cnt>max) {
				max=cnt;
				p=pi;
			}
		}

		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(p)) {
			Player p1= this.grafo.getEdgeSource(e);
			Player p2= this.grafo.getEdgeTarget(e);
			Arco a = new Arco(p1.getPlayerID(), p2.getPlayerID(), this.grafo.getEdgeWeight(e));

			list.add(a);
			Collections.sort(list, new ComparatoreDiArchi());

		}

		for(Arco aa: list) {
			s+= idMap.get(aa.getGiocatore2())+"\n";
		}
		return "Il top player è: "+ p+"\n Gli avversari battuti sono: \n"+s;

	}

	public String dreamTeam(int k){
		List<Player> parziale= new ArrayList<Player>();
		double grado1=0;
		String s="";
		cerca_ricorsiva(k, parziale, grado, grado1);

		for(Player p: this.best) {
			s+= p.toString()+"\n";
		}
		return s+ " "+this.grado;

	}

	private void cerca_ricorsiva(int k, List<Player> parziale, double grado, double grado1) {

		if(parziale.size()==k && grado1>grado ) {
			this.grado=grado1;
			this.best= new ArrayList<Player>(parziale);
		}
		for(Player p : this.grafo.vertexSet()) {
			for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(p)) {

				grado1+= this.grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e: this.grafo.incomingEdgesOf(p)) {

				grado1-= this.grafo.getEdgeWeight(e);
			}
			if(!parziale.contains(p) && this.aggiunta(p, parziale)==true) {
						parziale.add(p);
						cerca_ricorsiva(k, parziale,this.grado, grado1);
						parziale.remove(parziale.size()-1);

						for(DefaultWeightedEdge ei: this.grafo.outgoingEdgesOf(p)) {

							grado1-= this.grafo.getEdgeWeight(ei);
						}
						for(DefaultWeightedEdge ei: this.grafo.incomingEdgesOf(p)) {

							grado1+= this.grafo.getEdgeWeight(ei);
						}
					}
				}
			}

	private boolean aggiunta(Player p, List<Player> parziale) {
		boolean b= false;
		if(parziale.size()>0) {
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(parziale.get(parziale.size()-1))){
			if(!(this.grafo.getEdgeTarget(e).equals(p))) {
				b=true;
				}
			}
		}else b=true;
		
		
		
	return b;	
	}

}
