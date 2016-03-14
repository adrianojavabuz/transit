package com.example.service;

import java.util.List;

import com.example.model.TransitResult;

public interface TransitService {

	List<TransitResult> getTransitResults(String targets);

}
