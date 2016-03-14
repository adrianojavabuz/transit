package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.example.model.TransitResult;

@Service("transitService")
@Profile("dev")
public class TransitServiceMockImpl implements TransitService {
	
	public List<TransitResult> getTransitResults(String targets) {
		
		List<TransitResult> result = new ArrayList<TransitResult>();
		
		for (int i = 0; i < 10; i++) {
			result.add(new TransitResult("origin"+i, "destination"+i, String.valueOf(i*2), String.valueOf(i*3), "OK"+i));
		}
		
		return result;
	}

}
