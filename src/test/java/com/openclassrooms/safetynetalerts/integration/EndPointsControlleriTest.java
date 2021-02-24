package com.openclassrooms.safetynetalerts.integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EndPointsControlleriTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testPersonsOfStationAdultsAndChildWithFirestation() throws Exception {

		/*
		 * L'utilisateur accède à l’URL :
		 * 
		 * http://localhost:9090/firestation?stationNumber=<station_number>
		 * 
		 * Le système retourne une liste des personnes (prénom, nom, adresse, numéro de
		 * téléphone) couvertes par la caserne de pompiers correspondante ainsi qu’un
		 * décompte du nombre d’adultes (>18 ans) et du nombre d’enfants (<=18 ans)
		 * 
		 */
		String param_station = "1";
		mockMvc.perform(get("/firestation").param("stationNumber", param_station)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].listPersonsAdults.[0].firstName", is("Peter")));

	}

	@Test
	public void testPersonsOfStationAdultsAndChildNoFirestation() throws Exception {

		/*
		 * L'utilisateur accède à l’URL :
		 * 
		 * http://localhost:9090/firestation?stationNumber=<station_number>
		 * 
		 * Le système retourne une liste des personnes (prénom, nom, adresse, numéro de
		 * téléphone) couvertes par la caserne de pompiers correspondante ainsi qu’un
		 * décompte du nombre d’adultes (>18 ans) et du nombre d’enfants (<=18 ans)
		 * 
		 */
		String param_station = "";
		mockMvc.perform(get("/firestation").param("stationNumber", param_station)).andExpect(status().is(400))
				.equals(null);

	}

	@Test
	public void testChildPersonsAlertAddressWithChild() throws Exception {

		/*
		 * * L'utilisateur accède à l’URL :
		 *
		 * http://localhost:9090/childAlert?address=<address>
		 * 
		 * Le système retourne une liste des enfants (<=18 ans) habitant à cette
		 * adresse. La liste doit comprendre : prénom, nom, âge et une liste des autres
		 * membres du foyer. S’il n’y a pas d’enfant, cette url peut renvoyer une chaîne
		 * vide.
		 */

		String param_address = "1509 Culver St";
		mockMvc.perform(get("/childAlert").param("address", param_address)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].listChildren.[0].decompte", is(notNullValue())));
	}

	@Test
	public void testPhoneAlertFirestation() throws Exception {

		/*
		 * L'utilisateur accède à l’URL :
		 *
		 * http://localhost:9090/phoneAlert?firestation=< firestation _number>
		 *
		 * Le système retourne une liste des numéros de téléphone des résidents
		 * desservis par la caserne de pompiers.
		 */

		String param_station = "1";
		mockMvc.perform(get("/phoneAlert").param("firestation", param_station)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].listPhones", is(notNullValue())));
	}

	@Test
	public void testFireAddress() throws Exception {

		/*
		 * L'utilisateur accède à l’URL :
		 * 
		 * http://localhost:9090/fire?address=<address>
		 *
		 * Le système retourne une liste des habitants vivants à l’adresse donnée ainsi
		 * que le numéro de la caserne de pompiers la desservant. La liste doit inclure
		 * : le nom, le numéro de téléphone, l’âge et les antécédents médicaux
		 * (médicaments, posologie et allergies) de chaque personne.
		 */

		String param_address = "1509 Culver St";
		mockMvc.perform(get("/fire").param("address", param_address)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].lastName", is("Boyd")));

	}

	@Test
	public void testStationListFirestation() throws Exception {

		/*
		 * L'utilisateur accède à l’URL :
		 * 
		 * http://localhost:9090/flood/station?station=<a list of station_numbers>
		 * 
		 * Le système retourne une liste de tous les foyers desservis par la caserne.
		 * Cette liste doit regrouper les personnes par adresse. La liste doit inclure :
		 * le nom, le numéro de téléphone et l’âge des habitants et faire figurer les
		 * antécédents médicaux (médicaments, posologie et allergies) à côté de chaque
		 * nom.
		 * 
		 */

		List<String> param_station = new ArrayList<>();
		param_station.add("1");
		param_station.add("3");
		mockMvc.perform(get("/flood/station").param("station", param_station.get(0).toString()).param("station",
				param_station.get(1).toString())).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].address", containsString("644")))
				.andExpect(jsonPath("$[1].address", containsString("908 73rd St")))
				.andExpect(jsonPath("$[*].address").isArray());
	}

	@Test
	public void testPersonInfo() throws Exception {

		/*
		 * L'utilisateur accède à l’URL :
		 * 
		 * http://localhost:9090/personInfo?firstName=<firstName>&lastName=<lastName>
		 * 
		 * Le système retourne le nom, l’adresse, l’âge, l’adresse mail et les
		 * antécédents médicaux (médicaments, posologie et allergies) de chaque
		 * habitant. Si plusieurs personnes portent le même nom, elles doivent toutes
		 * apparaître.
		 * 
		 */

		String param_firstName = "John";
		String param_lastName = "Boyd";
		mockMvc.perform(get("/personInfo").param("firstName", param_firstName).param("lastName", param_lastName))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].lastName", is("Boyd")));
	}

	@Test
	public void testCommunityEmail() throws Exception {

		/*
		 * L'utilisateur accède à l’URL :
		 * 
		 * http://localhost:9090/communityEmail?city=<city>
		 * 
		 * Le système retourne les adresses mail de tous les habitants de la ville.
		 * 
		 */

		String param_city = "Culver";
		mockMvc.perform(get("/communityEmail").param("city", param_city)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].listEmails", is(notNullValue())));

	}

}
