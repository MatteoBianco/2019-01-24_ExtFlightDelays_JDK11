package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private ExtFlightDelaysDAO dao;
	private Graph<String, DefaultWeightedEdge> graph;
	private List<String> listStates;
	private List<StatePair> listStatePairs;
	
	private Simulator simulator;
	
	public Model() {
		this.dao = new ExtFlightDelaysDAO();
	}

	public void createGraph() {
		this.graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.listStates = this.dao.getListStates();
		Graphs.addAllVertices(this.graph, this.listStates);
		this.listStatePairs = this.dao.getStatePairs();
		for(StatePair sp : this.listStatePairs) {
			if(this.graph.getEdge(sp.getOrigin(), sp.getDestination()) == null) {
				Graphs.addEdge(this.graph, sp.getOrigin(), sp.getDestination(), sp.getWeight());
			}
		}
	}
	
	public void simulate(Integer T, Integer G, String state) {
		this.simulator = new Simulator();
		this.simulator.init(T, G, state, this.graph);
		this.simulator.run();
	}

	public Map<String, Integer> turistsInEachState() {
		return this.simulator.getTuristsInEachState();
	}
	
	public List<String> getListStates() {
		return listStates;
	}

	public List<StatePair> getAllDestinations(String state) {
		List<StatePair> destinations = new ArrayList<>();
		for(StatePair sp : this.listStatePairs) {
			if(sp.getOrigin().equals(state))
				destinations.add(sp);
		}
		destinations.sort(new Comparator<StatePair>() {

			@Override
			public int compare(StatePair o1, StatePair o2) {
				return -o1.getWeight().compareTo(o2.getWeight());
			}
			
		});
		return destinations;
	}

}
