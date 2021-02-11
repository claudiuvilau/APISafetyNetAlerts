package com.openclassrooms.safetynetalerts.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.text.IsBlankString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;

@SpringBootTest
@AutoConfigureMockMvc
public class EndPointsControllerITest {

	@Autowired
	private MockMvc mockMvc;
	
	private JsonDaoImplements jsonDaoImplements = new JsonDaoImplements();

	/*
	 * @Test public void testGetpersonsOfStationAdultsAndChild() throws Exception {
	 * 
	 * /* Le système retourne une liste des personnes (prénom, nom, adresse, numéro
	 * de téléphone) couvertes par la caserne de pompiers correspondante ainsi qu’un
	 * décompte du nombre d’adultes (>18 ans) et du nombre d’enfants (<=18 ans)
	 * 
	 * mockMvc.perform(get("/firestation?stationNumber=1")).andExpect(status().isOk(
	 * )) .andExpect(jsonPath("$[0].listPersonsAdults.[0].firstName", is("Peter")));
	 * 
	 * /* MvcResult mvcResult =
	 * mockMvc.perform(get("/firestation?stationNumber=3")).andExpect(status().isOk(
	 * )) .andReturn();
	 * 
	 * 
	 * }
	 */

	@Test
	public void testAddPerson() throws Exception {
		String body = "{\r\n"
				+ "        \"firstName\": \"TestFirstName\",\r\n"
				+ "        \"lastName\": \"TestLastName\",\r\n"
				+ "        \"address\": \"1509 Culver St\",\r\n"
				+ "        \"city\": \"Culver\",\r\n"
				+ "        \"zip\": \"97451\",\r\n"
				+ "        \"phone\": \"841-874-6512\",\r\n"
				+ "        \"email\": \"jaboyd@email.com\"\r\n"
				+ "    }";
		
		
		mockMvc.perform(post("/person").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201));
		mockMvc.perform(get("/person/TestFirstNameTestLastName")).andExpect(status().isOk()).andExpect(jsonPath("$[0].firstName", is("TestFirstName")));
		
		//delete the test made
		jsonDaoImplements.deletePerson("TestFirstName", "TestLastName");
		
	}
	
	@Test
	public void testDeletePerson() throws Exception {
		//add person
		String body = "{\r\n"
				+ "        \"firstName\": \"TestFirstName\",\r\n"
				+ "        \"lastName\": \"TestLastName\",\r\n"
				+ "        \"address\": \"1509 Culver St\",\r\n"
				+ "        \"city\": \"Culver\",\r\n"
				+ "        \"zip\": \"97451\",\r\n"
				+ "        \"phone\": \"841-874-6512\",\r\n"
				+ "        \"email\": \"jaboyd@email.com\"\r\n"
				+ "    }";
		mockMvc.perform(post("/person").content(body).contentType(MediaType.APPLICATION_JSON));
		
		mockMvc.perform(delete("/person").param("firstName", "TestFirstName").param("lastName", "TestLastName")).andExpect(status().isOk());
		
		mockMvc.perform(get("/person/TestFirstNameTestLastName")).andExpect(status().isOk()).equals(null);
	}
	
	@Test
	public void testUpdatePerson() throws Exception {
		String body = "{\r\n"
				+ "        \"firstName\": \"TestFirstName\",\r\n"
				+ "        \"lastName\": \"TestLastName\",\r\n"
				+ "        \"address\": \"1509 Culver St\",\r\n"
				+ "        \"city\": \"Culver\",\r\n"
				+ "        \"zip\": \"97451\",\r\n"
				+ "        \"phone\": \"841-874-6512\",\r\n"
				+ "        \"email\": \"jaboyd@email.com\"\r\n"
				+ "    }";

		String body_put = "{\r\n"
				+ "        \"firstName\": \"TestFirstName\",\r\n"
				+ "        \"lastName\": \"TestLastName\",\r\n"
				+ "        \"address\": \"Test1509 Culver St\",\r\n"
				+ "        \"city\": \"TestCulver\",\r\n"
				+ "        \"zip\": \"Test97451\",\r\n"
				+ "        \"phone\": \"Test841-874-6512\",\r\n"
				+ "        \"email\": \"Testjaboyd@email.com\"\r\n"
				+ "    }";

		mockMvc.perform(post("/person").content(body).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(put("/person").content(body_put).contentType(MediaType.APPLICATION_JSON).param("firstName", "TestFirstName").param("lastName", "TestLastName")).andExpect(status().isOk());

		mockMvc.perform(get("/person/TestFirstNameTestLastName")).andExpect(status().isOk()).andExpect(jsonPath("$[0].city", is("TestCulver")));
		
		//delete the test made
		jsonDaoImplements.deletePerson("TestFirsName", "TestLastName");
	}
	
	@Test
	public void testAddFirestation() throws Exception {
		String body = " {\r\n"
				+ "        \"address\": \"Test1509 Culver St\",\r\n"
				+ "        \"station\": \"Test99\"\r\n"
				+ "    }";
		
		
		mockMvc.perform(post("/firestation").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200));
		mockMvc.perform(get("/Firestations/Test99")).andExpect(status().isOk()).andExpect(jsonPath("$[0].address", is("Test1509 Culver St")));
		
		//delete the test made
		jsonDaoImplements.deleteFirestation(null, "Test99");
		
	}
	
	@Test
	public void testDeleteFirestation() throws Exception {
		//add person
		String body = " {\r\n"
				+ "        \"address\": \"Test1509 Culver St\",\r\n"
				+ "        \"station\": \"Test99\"\r\n"
				+ "    }";
		mockMvc.perform(post("/firestation").content(body).contentType(MediaType.APPLICATION_JSON));
		
		mockMvc.perform(delete("/firestation").param("station", "Test99")).andExpect(status().isOk());
		
		mockMvc.perform(get("/Firestations/Test99")).andExpect(status().isOk()).equals(null);
	}
	
	@Test
	public void testUpdatefirestation() throws Exception {
		String body = " {\r\n"
				+ "        \"address\": \"Test1509 Culver St\",\r\n"
				+ "        \"station\": \"Test99\"\r\n"
				+ "    }";

		String body_put = " {\r\n"
				+ "        \"address\": \"Test1509 Culver St\",\r\n"
				+ "        \"station\": \"TestStation\"\r\n"
				+ "    }";

		mockMvc.perform(post("/firestation").content(body).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(put("/firestation").content(body_put).contentType(MediaType.APPLICATION_JSON).param("address", "Test1509 Culver St")).andExpect(status().isOk());

		mockMvc.perform(get("/Firestations/TestStation")).andExpect(status().isOk()).andExpect(jsonPath("$[0].address", is("Test1509 Culver St")));
		
		//delete the test made
		jsonDaoImplements.deleteFirestation(null, "TestStation");
	}

}
