package com.example.model;

public class TransitResult {

	private String origin;
	private String destination;
	private String distance;
	private String duration;
	private String status;
	
	public TransitResult(String origin, String destination, String distance, String duration,String status) {
		this.origin = origin;
		this.destination = destination;
		this.distance = distance;
		this.duration = duration;
		this.status = status;
	}

	public String getDistance() {
		return distance;
	}

	public String getDuration() {
		return duration;
	}

	public String getStatus() {
		return status;
	}

	public String getOrigin() {
		return origin;
	}

	public String getDestination() {
		return destination;
	}
	
	
	
}
