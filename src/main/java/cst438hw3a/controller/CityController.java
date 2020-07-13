package cst438hw3a.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cst438hw3a.domain.*;
import cst438hw3a.service.CityService;

@Controller
public class CityController {
	
	@Autowired
	private CityService cityService;
	
	@GetMapping("/cities/{city}")
	public String getWeather(@PathVariable("city") String cityName, Model model) {
		
		//Creates cityInfoPack based on cityName sent to cityService
		CityInfo cityInfoPack = cityService.getCityInfo(cityName);
		
		//If city is null, ie: city not found, return error HTML page
		if (cityInfoPack == null) {
			return "errorPage";
			
		} else {
		//If city found, return queryResult HTML page
			model.addAttribute("cityInfo", cityInfoPack);
			return "showcity";
	} 
	}
	
	@PostMapping("/cities/{city}")
	public String createReservation(
			@RequestParam("city") String cityName, 
			@RequestParam("level") String level, 
			@RequestParam("email") String email, 
			Model model) {
		
		model.addAttribute("city", cityName);
		model.addAttribute("level", level);
		model.addAttribute("email", email);
		cityService.requestReservation(cityName, level, email);
		return "request_reservation";
	}

	
}