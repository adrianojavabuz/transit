package com.example.service;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.example.config.AppConfig;
import com.example.model.TransitResult;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.TransitMode;
import com.google.maps.model.TravelMode;

@Service("transitService")
@Profile("prod")
public class TransitServiceImpl implements TransitService {
	

	@Autowired
	private AppConfig config;
	
	public List<TransitResult> getTransitResults(String targets) {
		
		List<TransitResult> result = new ArrayList<TransitResult>();
		
		if ("".equalsIgnoreCase(targets.trim())) return result;
		
		List<String> names = Arrays.asList(targets.trim().split("\n"));
		
		GeoApiContext context = new GeoApiContext().setApiKey(config.getGooleAPIKey());
		setProxy(context);
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
					DistanceMatrixApi.getDistanceMatrix(context,new String[]{name}, new String[]{getDestinationAddress()})
	    			.mode(TravelMode.TRANSIT)
	    			.transitModes(TransitMode.BUS, TransitMode.RAIL)
	    			.arrivalTime(arrivalTime).await().rows;
	    	
	    	result.add(buildSingleTransitResult(name,getDestinationAddress(),rows));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
			}
		}
		
		return result;
	}

	private void setProxy(GeoApiContext context) {
		if (!"".equalsIgnoreCase(config.getProxyUrl())
				&& !"".equalsIgnoreCase(config.getProxyPort()) ) {
			
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxyUrl(), Integer.valueOf(config.getProxyPort())));
			context.setProxy(proxy);
		}
	}

	private TransitResult buildSingleTransitResult(String origin, String destination, DistanceMatrixRow[] rows) {
		TransitResult t = new TransitResult(origin,destination,
				rows[0].elements[0].distance == null ? "NULL" : rows[0].elements[0].distance.toString(),
				rows[0].elements[0].duration == null ? "NULL" : rows[0].elements[0].duration.toString(),
				rows[0].elements[0].status.toString());
		return t;
	}

	private String getDestinationAddress() {
		return config.getWorkAddress();
	}

}
