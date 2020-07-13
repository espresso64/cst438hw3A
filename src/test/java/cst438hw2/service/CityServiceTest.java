package cst438hw2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.ArgumentMatchers.anyString;

import cst438hw2.domain.*;

@SpringBootTest
public class CityServiceTest {

	@MockBean
	private WeatherService weatherService;

	@Autowired
	private CityService cityService;

	@MockBean
	private CityRepository cityRepository;

	@MockBean
	private CountryRepository countryRepository;

	@Test
	public void contextLoads() {
	}

	// References testing techniques/code from DemoRestApplication

	//Tests logic of when city is found
	@Test
	public void testCityFound() throws Exception {

		// Initialized test variables
		City city = new City(1, "Test City", "FOO", "Test District", 9000);

		Country country = new Country("FOO", "Test Country");

		// TempAndTime object includes temperature K, unix time, timezone offset
		TempAndTime TTInfoPack = new TempAndTime(299.817, 1594024278, 32400);

		//Add city to list
		List<City> cityList = new ArrayList<City>();
		cityList.add(city);

		// When given the name, "Test City" stub will give initialized cityList variable
		given(cityRepository.findByName("Test City")).willReturn(cityList);


		// When given the country code of FOO, stub will give initialized country variable
		given(countryRepository.findByCode("FOO")).willReturn(country);

		// When given the name, "Test City", stub will give initialized TTInfoPack
		given(weatherService.getTempAndTime("Test City")).willReturn(TTInfoPack);

		CityInfo testResult = cityService.getCityInfo("Test City");

		CityInfo expectedResult = new CityInfo(1, "Test City", "FOO", "Test Country", "Test District", 9000, 80.0,
				"5:31 PM");

		// Assertions
		// verify that result is as expected
		assertThat(testResult).isEqualTo(expectedResult);
	}

	//Tests logic of when city is not found
	@Test
	public void testCityNotFound() {
		// Initialized test variables
		City city = new City(1, "Test City", "FOO", "Test District", 9000);

		Country country = new Country("FOO", "Test Country");

		TempAndTime TTInfoPack = new TempAndTime(299.817, 1594024278, 32400);

		List<City> cityList = new ArrayList<City>();

		cityList.add(city);


		// When given the name, "Test City", stub will give initialized cityList variable
		given(cityRepository.findByName("Test City")).willReturn(cityList);


		// When given the country code of FOO, stub will give initialized country variable
		given(countryRepository.findByCode("FOO")).willReturn(country);


		// When given the name, "Test City", stub will give initialized TTInfoPack
		given(weatherService.getTempAndTime("Test City")).willReturn(TTInfoPack);

		CityInfo testResult = cityService.getCityInfo("Not Test City");

		CityInfo expectedResult = null;

		// Assertions
		// verify that result is as expected
		assertThat(testResult).isEqualTo(expectedResult);
	}

	//Test logic when multiple cities have the same name
	@Test
	public void testCityMultiple() {
		// Initialized test variables
		
		//All cities have the same name, but different code, district, population
		City city = new City(1, "Test City", "FOO", "Test District", 9000);
		City city2 = new City(2, "Test City", "FOO2", "Test District2", 9001);
		City city3 = new City(3, "Test City", "FOO3", "Test District3", 9002);
		City city4 = new City(4, "Test City", "FOO4", "Test District4", 9003);
		
		//Each city comes from a different country
		Country country = new Country("FOO", "Test Country");
		Country country2 = new Country("FOO2", "Test Country2");
		Country country3 = new Country("FOO3", "Test Country3");
		Country country4 = new Country("FOO4", "Test Country4");

		// TempAndTime object includes temperature K, unix time, timezone offset
		TempAndTime TTInfoPack = new TempAndTime(299.817, 1594024278, 32400);

		List<City> cityList = new ArrayList<City>();

		cityList.add(city);
		cityList.add(city2);
		cityList.add(city3);
		cityList.add(city4);

		// stub calls and return data for cityRepository
		// When given the name, "Test City" will give initialized cityList variable
		given(cityRepository.findByName("Test City")).willReturn(cityList);

		// stub calls and return data for countryRepository
		// When given the country code of FOO(2-4), will give initialized country variable
		given(countryRepository.findByCode("FOO")).willReturn(country);
		given(countryRepository.findByCode("FOO2")).willReturn(country2);
		given(countryRepository.findByCode("FOO3")).willReturn(country3);
		given(countryRepository.findByCode("FOO4")).willReturn(country4);

		// stub calls and return data for weather service
		// When given the name, "Test City", will give initialized TTInfoPack
		given(weatherService.getTempAndTime("Test City")).willReturn(TTInfoPack);

		//Test will fail if incorrect city/country is selected by the cityService
		//Code, country name, district, etc. will cause mismatch error
		//City service must select first on the list of identical cities
		CityInfo testResult = cityService.getCityInfo("Test City");

		CityInfo expectedResult = new CityInfo(1, "Test City", "FOO", "Test Country", "Test District", 9000, 80.0,
				"5:31 PM");

		// Assertions
		// verify that result matches expected City Info
		assertThat(testResult).isEqualTo(expectedResult);
	}

}
