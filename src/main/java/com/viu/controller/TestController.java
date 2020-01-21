package com.viu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	
	
	@GetMapping(value = "/get")
	public String testMethod() {
		
		System.out.println("Added logger");
		System.out.println("Added logger");
		return "hello";
	}

}
