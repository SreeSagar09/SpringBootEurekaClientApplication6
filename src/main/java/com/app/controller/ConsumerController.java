package com.app.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = "/consumer")
public class ConsumerController {
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	@GetMapping(path = "/getData")
	public ResponseEntity<Map<String, String>> getData(){
		
		ResponseEntity<Map<String, String>> responseEntity = null;
		try{
			ServiceInstance serviceInstance = loadBalancerClient.choose("SPRINGBOOTEUREKACLIENTAPPLICATION5");
			URI uri = serviceInstance.getUri();
			
			String urlString = uri+"/producer/getData";
			
			RestTemplate restTemplate = new RestTemplate();
			
			Map<String, String> map = restTemplate.getForObject(urlString, Map.class);
		
			responseEntity = new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

}
