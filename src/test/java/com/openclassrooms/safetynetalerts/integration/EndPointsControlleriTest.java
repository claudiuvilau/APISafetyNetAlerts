package com.openclassrooms.safetynetalerts.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;
import com.openclassrooms.safetynetalerts.model.PhoneAlert;

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
		mockMvc.perform(get("/firestation").param("stationNumber", param_station)).andExpect(status().isOk()).andExpect(jsonPath("$[0].listPersonsAdults.[0].firstName", is("Peter")));
		
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
		mockMvc.perform(get("/firestation").param("stationNumber", param_station)).andExpect(status().isOk()).equals(null);
		
	}
	
	@Test
	public void testChildPersonsAlertAddressWithChild() throws Exception {

		/*
		 *  * L'utilisateur accède à l’URL :
		 *
		 * http://localhost:9090/childAlert?address=<address>
		 * 
		 * Le système retourne une liste des enfants (<=18 ans) habitant à cette
		 * adresse. La liste doit comprendre : prénom, nom, âge et une liste des autres
		 * membres du foyer. S’il n’y a pas d’enfant, cette url peut renvoyer une chaîne
		 * vide.
		 */

		String param_address = "1509 Culver St";
		mockMvc.perform(get("/childAlert").param("address", param_address)).andExpect(status().isOk()).andExpect(jsonPath("$[0].listChildren.[0].decompte", is(notNullValue())));
	}

	@Test
	public void testChildPersonsAlertAddressNoChild() throws Exception {

		/*
		 *  * L'utilisateur accède à l’URL :
		 *
		 * http://localhost:9090/childAlert?address=<address>
		 * 
		 * Le système retourne une liste des enfants (<=18 ans) habitant à cette
		 * adresse. La liste doit comprendre : prénom, nom, âge et une liste des autres
		 * membres du foyer. S’il n’y a pas d’enfant, cette url peut renvoyer une chaîne
		 * vide.
		 */

		String param_address = "";
		mockMvc.perform(get("/childAlert").param("address", param_address)).andExpect(status().isOk()).equals(null);
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
		mockMvc.perform(get("/phoneAlert").param("firestation", param_station)).andExpect(status().isOk()).andExpect(jsonPath("$[0].listPhones", is(notNullValue())));
	}

	@Test
	public void testPhoneAlertNoFirestation() throws Exception {

		/*
		 * L'utilisateur accède à l’URL :
		 *
		 * http://localhost:9090/phoneAlert?firestation=< firestation _number>
		 *
		 * Le système retourne une liste des numéros de téléphone des résidents
		 * desservis par la caserne de pompiers.
		 */

		String param_station = "";
		mockMvc.perform(get("/phoneAlert").param("firestation", param_station)).andExpect(status().isOk()).equals(null);
	}

}
