
package com.bfm.acs.crazycricket;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bfm.acs.crazycricket.api.CrazyCricketController;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, TestConfiguration.class })
@WebAppConfiguration
public class ServiceTest {
	@InjectMocks
	private CrazyCricketController service;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	private static EmbeddedDatabase db;

	@Before
	public void initTests() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@BeforeClass
	public static void setUp() {
		db = new EmbeddedDatabaseBuilder().addScript("schema.sql").addScript("test-data.sql").build();
	}

	@AfterClass
	public static void tearDown() {
		db.shutdown();
	}

	@Test
	public void testLeaderBoard() throws Exception {
		mvc.perform(get("/api/leaderboard").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5))).andExpect(jsonPath("$[0]", hasEntry("sachin", 6)))
				.andExpect(jsonPath("$[1]", hasEntry("shubham", 5))).andExpect(jsonPath("$[2]", hasEntry("oscar", 3)))
				.andExpect(jsonPath("$[3]", hasEntry("andrew", 1))).andExpect(jsonPath("$[4]", hasEntry("imran", 1)));
	}

	@Test
	public void testNationalLeaderBoard() throws Exception {
		mvc.perform(get("/api/national_leaderboard").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(4))).andExpect(jsonPath("$[0]", hasEntry("India", 11)))
				.andExpect(jsonPath("$[1]", hasEntry("England", 3)))
				.andExpect(jsonPath("$[2]", hasEntry("Pakistan", 1))).andExpect(jsonPath("$[3]", hasEntry("USA", 1)));
	}

	@Test
	public void testNationalLeaderBoardDateValidation() throws Exception {
		mvc.perform(get("/api/national_leaderboard?start=20160101&end=20160731").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0]", hasEntry("India", 11))).andExpect(jsonPath("$[1]", hasEntry("England", 3)))
				.andExpect(jsonPath("$[2]", hasEntry("Pakistan", 1))).andExpect(jsonPath("$[3]", hasEntry("USA", 1)));
	}
}
