package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.extflightdelays.model.Event.EventType;

public class Simulator {
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	
	//MODELLO DEL MONDO
	private Map<String, Integer> turistsInEachState;
	private Graph<String, DefaultWeightedEdge> graph;
	private Integer actualDay;
	
	//PARAMETRI DI SIMULAZIONE
	private Integer T;
	private Integer G;
	private String start;

	//OUTPUT
	// La mappa che è il modello del mondo continene tutte le informazioni necessarie
	
	public void init(Integer T, Integer G, String start, Graph<String, DefaultWeightedEdge> graph) {
		this.queue = new PriorityQueue<>();
		this.turistsInEachState = new HashMap<>();
		
		this.graph = graph;
		this.T = T;
		this.G = G;
		this.start = start;
		
		this.actualDay = 1;
		
		for(String state : this.graph.vertexSet())
			this.turistsInEachState.put(state, 0);
		this.turistsInEachState.replace(start, T);
		
		for(int i = 0; i < T; i++) {
			this.queue.add(new Event(EventType.START_TRIP, this.actualDay, start));
		}
	}
	
	public void run() {
		while(! this.queue.isEmpty()) {
			this.processEvent(this.queue.poll());
		}
	}
	
	private void processEvent(Event e) {
		switch(e.getType()) {
		case START_TRIP:
			
			Integer totWeight = this.getTotalWeightForOutgoingFlights(e.getActualState());
			Integer random = (int) (Math.random()*totWeight) + 1;
						
			Integer targetValue = 0;
			for(String destination : Graphs.successorListOf(this.graph, e.getActualState())) {
				Integer thisWeight = (int) this.graph.getEdgeWeight(this.graph.getEdge(e.getActualState(), destination));
				if(targetValue < random && (targetValue + thisWeight) >= random) {
					
					this.turistsInEachState.replace(e.getActualState(), (this.turistsInEachState.get(e.getActualState()) - 1));
					this.turistsInEachState.replace(destination, (this.turistsInEachState.get(destination) + 1));
									
					if((e.getDate() + 1) <= this.G)
						this.queue.add(new Event(EventType.START_TRIP, (e.getDate() + 1), destination));
				}
				targetValue += thisWeight;
			}
		}
	}
	
	private Integer getTotalWeightForOutgoingFlights(String state) {
		Integer totWeight = 0;
		for(String destination : Graphs.successorListOf(this.graph, state)) {
			totWeight += (int) this.graph.getEdgeWeight(this.graph.getEdge(state, destination));
		}
		return totWeight;
	}

	public Map<String, Integer> getTuristsInEachState() {
		return turistsInEachState;
	}
	
	public void setGraph(Graph<String, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}
	public void setT(Integer t) {
		T = t;
	}
	public void setG(Integer g) {
		G = g;
	}
	public void setStart(String start) {
		this.start = start;
	}
	
	
	
}
