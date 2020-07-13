package cst438hw2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import cst438hw2.domain.*;
import cst438hw2.service.CityService;

@RestController
public class CityRestController {

	@Autowired
	private CityService cityService;

	@GetMapping("/api/cities/{city}")
	public ResponseEntity<CityInfo> getWeather(@PathVariable("city") String cityName) {

		// Sends city name to cityService
		// cityService will check to see if city exists
		CityInfo city = cityService.getCityInfo(cityName);

		// If there is at least 1 matching city (not null)
		// return 200 status code (OK) and information in JSON format
		if (city != null) {
			return new ResponseEntity<CityInfo>(city, HttpStatus.OK);
		}

		// If there are no matches (ie:null), return NOT_FOUND (404)
		else {
			return new ResponseEntity<CityInfo>(HttpStatus.NOT_FOUND);
		}
	}

}
