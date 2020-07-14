package cst438hw3a.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cst438hw3a.domain.*;

@Service
public class CityService {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private WeatherService weatherService;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private FanoutExchange fanout;
	 
	public void requestReservation( 
				   String cityName, 
				   String level, 
				   String email) {
		String msg  = "{\"cityName\": \""+ cityName + 
			   "\" \"level\": \""+level+
			   "\" \"email\": \""+email+"\"}" ;
		System.out.println("Sending message:"+msg);
		rabbitTemplate.convertSendAndReceive(
				fanout.getName(), 
				"",   // routing key none.
				msg);
	}

	

	public CityInfo getCityInfo(String cityName) {

		// References code from DemoRestApplication
		// Checks database for city name, selects first one from list of results
		List<City> cityMatchList = cityRepository.findByName(cityName);

		// If city list is zero, return null result
		if (cityMatchList.size() == 0) {
			return null;
			
		} else {

			// in case of multiple cities, select first city from list
			City selectedCity = cityMatchList.get(0);

			//Get country information
			Country selectedCountry = countryRepository.findByCode(selectedCity.getCountryCode());
			
			//Access weather service to get weather information based on cityName
			TempAndTime weatherResult = weatherService.getTempAndTime(cityName);
			
			//Temp conversion
			double tempF = Math.round((weatherResult.getTemp() - 273.15) * 9.0/5.0 + 32.0);
			
			//Time conversion
			long timeTotal = (weatherResult.getTime() + weatherResult.getTimezone()) * 1000;
		    Date date = new Date (timeTotal); 
		    SimpleDateFormat simpleDate = new SimpleDateFormat("h:mm a");
		    simpleDate.setTimeZone(TimeZone.getTimeZone("UTC"));
		    String dateString = simpleDate.format(date);
		    
			//Return statement that returns newly formed CityInfo
			//Accesses added get functions in TempAndTime to get temp and time formatted
			return new CityInfo(selectedCity, selectedCountry.getName(), 
					tempF, dateString);
	
		}

	}

}
