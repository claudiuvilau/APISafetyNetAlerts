package com.openclassrooms.safetynetalerts;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.safetynetalerts.controller.EndPointsController;
import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;
import com.openclassrooms.safetynetalerts.model.ChildAlert;
import com.openclassrooms.safetynetalerts.model.FireAddress;
import com.openclassrooms.safetynetalerts.model.Foyer;

@WebMvcTest(controllers = EndPointsController.class)
public class EndPointsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private JsonDaoImplements jsonDaoImplements;

	@Test
	public void testGetpersonsOfStationAdultsAndChild() throws Exception {
		List<Foyer> listFoyer = new ArrayList<>();
		String station_number = "3";
		listFoyer.add(new Foyer("1", null, null, null, null));
		when(jsonDaoImplements.personsOfStationAdultsAndChild(station_number)).thenReturn(listFoyer);
		mockMvc.perform(get("/firestation").param("stationNumber", station_number)).andExpect(status().isOk());
	}

	@Test
	public void testGetNoPersonsOfStationAdultsAndChild() throws Exception {
		List<Foyer> listFoyer = new ArrayList<>();
		String station_number = "3";
		// no adults non children = "0"
		listFoyer.add(new Foyer("0", null, "0", null, null));
		when(jsonDaoImplements.personsOfStationAdultsAndChild(station_number)).thenReturn(listFoyer);
		mockMvc.perform(get("/firestation").param("stationNumber", station_number)).andExpect(status().is(422));
	}

	@Test
	public void testGetchildPersonsAlertAddress() throws Exception {

		List<ChildAlert> listChildren = new ArrayList<>();
		listChildren.add(new ChildAlert(null, null));
		String une_adresse = "TestUneAdresse";
		when(jsonDaoImplements.childPersonsAlertAddress(une_adresse)).thenReturn(listChildren);
		mockMvc.perform(get("/childAlert").param("address", une_adresse)).andExpect(status().isOk());

	}

	@Test
	public void testGetphoneAlertFirestation() throws Exception {

		mockMvc.perform(get("/phoneAlert?firestation==1")).andExpect(status().isOk());

	}

	@Test
	public void testGetfireNoAddress() throws Exception {

		List<FireAddress> listFireAddress = new ArrayList<>();
		String une_adresse = "TestUneAdresse";
		// if listFireAddress is Empty
		when(jsonDaoImplements.fireAddress(une_adresse)).thenReturn(listFireAddress);
		mockMvc.perform(get("/fire").param("address", une_adresse)).andExpect(status().is(422));
	}

	@Test
	public void testGetfireAddress() throws Exception {

		List<FireAddress> listFireAddress = new ArrayList<>();
		String une_adresse = "TestUneAdresse";
		listFireAddress.add(new FireAddress("1", null, null, null, null, null, null));
		// if listFireAddress is not Empty
		when(jsonDaoImplements.fireAddress(une_adresse)).thenReturn(listFireAddress);
		mockMvc.perform(get("/fire").param("address", une_adresse)).andExpect(status().isOk());
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
