package it.polito.tdp.extflightdelays.model;

public class Event implements Comparable<Event>{
	
	public enum EventType {
		START_TRIP;
	}
	
	private EventType type;
	private Integer date;
	private String actualState;
	
	public Event(EventType type, Integer date, String actualState) {
		super();
		this.type = type;
		this.date = date;
		this.actualState = actualState;
	}

	public EventType getType() {
		return type;
	}
	public Integer getDate() {
		return date;
	}
	public String getActualState() {
		return actualState;
	}

	
	@Override
	public int compareTo(Event o) {
		return this.date.compareTo(o.date);
	}
}
