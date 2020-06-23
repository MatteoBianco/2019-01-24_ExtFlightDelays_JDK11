package it.polito.tdp.extflightdelays.model;

public class StatePair {
	
	private String origin;
	private String destination;
	private Integer weight;
	
	public StatePair(String origin, String destination, Integer weight) {
		super();
		this.origin = origin;
		this.destination = destination;
		this.weight = weight;
	}

	public String getOrigin() {
		return origin;
	}

	public String getDestination() {
		return destination;
	}

	public Integer getWeight() {
		return weight;
	}
	
}
