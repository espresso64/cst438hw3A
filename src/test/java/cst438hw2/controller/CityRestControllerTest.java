package cst438hw2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;

import cst438hw2.domain.*;
import cst438hw2.service.CityService;
import cst438hw2.service.WeatherService;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(CityRestController.class)

public class CityRestControllerTest {
	
	@MockBean
	private WeatherService weatherService;

	@MockBean
	private CityService cityService;
	
	@Autowired
	private MockMvc mvc;

	// This object will be magically initialized by the initFields method below.
	private JacksonTester<CityInfo> json;

	@Before
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	public void contextLoads() {
	}

	// References testing techniques/code from DemoRestApplication
	
	//Note: city service covers scenario of when there is more than 1 city
	
	// Tests to see if valid city info is processed correctly by controller
	@Test
	public void getCityInfo() throws Exception {
		// Initialized test variables
		City city = new City(1, "Test", "FOO", "Test District", 9000);
		
		//Testing with alternate constructor
		given(cityService.getCityInfo("Test")).willReturn(new CityInfo(city, "Test Country", 80.0, "5:31 PM"));

		MockHttpServletResponse response = mvc.perform(get("/api/cities/Test")).andReturn().getResponse();

		// checks to see that 200 response is given
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

		// convert returned data from JSON string format to City object
		CityInfo testResult = json.parseObject(response.getContentAsString());

		CityInfo expectedResult = new CityInfo(1, "Test", "FOO", "Test Country", "Test District",
				9000, 80.0,"5:31 PM");

		// Assertions
		//checks that json result contents match expected
		assertThat(testResult).isEqualTo(expectedResult);
	}

	// Tests to see if unavailable city info is processed correctly by controller
	@Test
	public void noCityInfo() throws Exception {
		
		MockHttpServletResponse response = mvc.perform(get("/api/cities/UnavailableCity"))
				.andReturn().getResponse();

		// verify that result is as expected, 404 response
		//Assertions
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

	}


}
