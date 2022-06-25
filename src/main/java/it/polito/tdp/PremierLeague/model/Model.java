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
	
	Graph<Player, DefaultWeightedEdge> grafo;
	PremierLeagueDAO dao;
	List<Player> vertici;
	Map<Integer, Player> idMap;
	
	
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
	
}
