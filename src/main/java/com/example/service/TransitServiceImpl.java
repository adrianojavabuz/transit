package com.example.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.config.AppConfig;
import com.example.model.TransitResult;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.TransitMode;
import com.google.maps.model.TravelMode;

@Service("transitService")
public class TransitServiceImpl implements TransitService {
	

	@Autowired
	private AppConfig config;
	
	public List<TransitResult> getTransitResults(String targets) {
		
		List<TransitResult> result = new ArrayList<TransitResult>();
		
		if ("".equalsIgnoreCase(targets.trim())) return result;
		
		List<String> names = Arrays.asList(targets.trim().split("\n"));
		
		GeoApiContext context = new GeoApiContext().setApiKey(config.getGooleAPIKey());
		ReadableInstant arrivalTime = new MutableDateTime(2016,4,1,9,0,0,0);
		ArrayList<String> origins = new ArrayList<String>();
		ArrayList<String> destinations = new ArrayList<String>();
		
		for (String name : names) {
			name = name.trim();
			if (!"".equalsIgnoreCase(name)) {
				origins.add(name);
				destinations.add(getDestinationAddress());
		
		try {
			DistanceMatrixRow rows[] = 
					//DistanceMatrixApi.getDistanceMatrix(context,origins.toArray(new String[origins.size()]), destinations.toArray(new String[origins.size()]))
					DistanceMatrixApi.getDistanceMatrix(context,new String[]{name}, new String[]{getDestinationAddress()})
	    			.mode(TravelMode.TRANSIT)
	    			.transitModes(TransitMode.BUS, TransitMode.RAIL)
	    			.arrivalTime(arrivalTime).await().rows;
	    	
	    	//result = buildTransitResult(origins,destinations,rows);
	    	result.add(buildSingleTransitResult(name,getDestinationAddress(),rows));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
			}
		}
		
		return result;
	}

	private TransitResult buildSingleTransitResult(String origin, String destination, DistanceMatrixRow[] rows) {
		TransitResult t = new TransitResult(origin,destination,
				rows[0].elements[0].distance == null ? "NULL" : rows[0].elements[0].distance.toString(),
				rows[0].elements[0].duration == null ? "NULL" : rows[0].elements[0].duration.toString(),
				rows[0].elements[0].status.toString());
		return t;
	}

	private List<TransitResult> buildTransitResult(ArrayList<String> origins, ArrayList<String> destinations, DistanceMatrixRow[] rows) {
		
		List<TransitResult> result = new ArrayList<TransitResult>();
		int i = 0;
		
		for (DistanceMatrixRow r : rows) {
			result.add(new TransitResult(
				origins.get(i),
				destinations.get(i++),
				r.elements[0].distance == null ? "NULL" : r.elements[0].distance.toString(),
				r.elements[0].duration == null ? "NULL" : r.elements[0].duration.toString(),
				r.elements[0].status.toString()));
		}

		return result;
	}
	
	private String getDestinationAddress() {
		return config.getWorkAddress();
	}

}
