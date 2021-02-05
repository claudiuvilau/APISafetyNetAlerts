package com.openclassrooms.safetynetalerts.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class EndPointsControllerITest {

	@Autowired
	private MockMvc mockMvc;

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

		String une_adresse = "1509 Culver St";
		MvcResult mvcResult = mockMvc.perform(get("/childAlert?address=" + une_adresse)).andExpect(status().isOk())
				.andReturn();

		// S’il n’y a pas d’enfant, cette url peut renvoyer une chaîne vide.
		boolean liste_vide = false;
		if (!mvcResult.getResponse().getContentAsString().isBlank()) {
			liste_vide = true;
		}
		assertThat(liste_vide == true);
	}

	@Test
	public void testGetphoneAlertFirestation() throws Exception {

		/*
		 * Le système retourne une liste des numéros de téléphone des résidents
		 * desservis par la caserne de pompiers.
		 */
		String fire_station = "1";
		MvcResult mvcResult = mockMvc.perform(get("/phoneAlert?firestation==" + fire_station))
				.andExpect(status().isOk()).andReturn();
		boolean liste_vide = false;
		if (!mvcResult.getResponse().getContentAsString().isEmpty()) {
			liste_vide = true;
		}
		assertThat(liste_vide == true);
	}

	@Test
	public void testGetfireAddress() throws Exception {

		mockMvc.perform(get("/fire?address=UneAdresse")).andExpect(status().isOk());

	}

	@Test
	public void testGetstationListFirestation() throws Exception {

		mockMvc.perform(get("/flood/station?station=1,3")).andExpect(status().isOk());

	}

	@Test
	public void testGetpersonInfo() throws Exception {

		mockMvc.perform(get("/personInfo?firstName=OnefirstName>&lastName=OnelastName")).andExpect(status().isOk());

	}

	@Test
	public void testGetcommunityEmail() throws Exception {

		mockMvc.perform(get("/communityEmail?city=Culver")).andExpect(status().isOk());

	}
}
