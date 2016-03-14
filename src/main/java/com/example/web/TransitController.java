package com.example.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.TransitResult;
import com.example.service.TransitService;

@Controller
public class TransitController {
	
	@Autowired
	private TransitService service;

	@RequestMapping(path="/process", method=RequestMethod.POST)
	//@ModelAttribute("results")
	public String calculate(@RequestParam("targets") String targets, ModelMap model) {
		List<TransitResult> results = service.getTransitResults(targets);
		model.addAttribute("results",results);
		return "result";
	}
		
}
