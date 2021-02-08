package com.openclassrooms.safetynetalerts.integration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileWriter;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;
import com.openclassrooms.safetynetalerts.model.Persons;
import com.openclassrooms.safetynetalerts.service.JsonPathFileToWriter;

@SpringBootTest
@AutoConfigureMockMvc
public class EndPointsControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private JsonPathFileToWriter jsonPathFileToWriter;
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

		// JsonPathFileToWriter jsonPathFileToWriter = mock(JsonPathFileToWriter.class);
		// jsonPathFileToWriter = new JsonPathFileToWriter();
		FileWriter fileWriter = new FileWriter("data/TestdbJSON.json");
		when(jsonPathFileToWriter.jsonPathFileToWriter()).thenReturn(fileWriter);
		Persons persons = new Persons();
		persons.setFirstName("TestFirstName5");
		persons.setLastName("TestLastName5");
		JsonDaoImplements jsonDaoImplements = new JsonDaoImplements();
		jsonDaoImplements.addPerson(persons);
		//mockMvc.perform(post("/person")).param("name", "claudiu").andExpect(status().isOk());
		mockMvc.perform(post("/person").param("name", "vilau"));
		
	}

}
