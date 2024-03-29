package com.openclassrooms.safetynetalerts;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.safetynetalerts.controller.EndPointsController;
import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;
import com.openclassrooms.safetynetalerts.model.AddressListFirestation;
import com.openclassrooms.safetynetalerts.model.ChildAlert;
import com.openclassrooms.safetynetalerts.model.CommunityEmail;
import com.openclassrooms.safetynetalerts.model.FireAddress;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Foyer;
import com.openclassrooms.safetynetalerts.model.PersonInfo;
import com.openclassrooms.safetynetalerts.model.Persons;
import com.openclassrooms.safetynetalerts.model.PersonsFireStation;
import com.openclassrooms.safetynetalerts.model.PhoneAlert;
import com.openclassrooms.safetynetalerts.service.LoggerApi;

@WebMvcTest(controllers = EndPointsController.class)
public class EndPointsControllerTest {

	private static Logger LOGGER = null;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private JsonDaoImplements jsonDaoImplements;

	@BeforeAll
	public static void setLogger() throws MalformedURLException {
		LoggerApi.setLoggerForTests();
		LOGGER = LogManager.getLogger();
	}

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
	@Test
	public void testGetpersonsOfStationAdultsAndChild() throws Exception {
		List<Foyer> listFoyer = new ArrayList<>();
		String station_number = "3";
		listFoyer.add(new Foyer("1", null, null, null, null));
		when(jsonDaoImplements.personsOfStationAdultsAndChild(station_number)).thenReturn(listFoyer);
		mockMvc.perform(get("/firestation").param("stationNumber", station_number)).andExpect(status().isOk());
		LOGGER.info("Fin test : Le système RETOURNE une liste des personnes (prénom, nom, adresse, numéro de\r\n"
				+ "téléphone) couvertes par la caserne de pompiers correspondante ainsi qu’un\r\n"
				+ "décompte du nombre d’adultes (>18 ans) et du nombre d’enfants (<=18 ans)");
	}

	@Test
	public void testGetNoPersonsOfStationAdultsAndChild() throws Exception {
		List<Foyer> listFoyer = new ArrayList<>();
		String station_number = "3";
		// no adults non children = "0"
		listFoyer.add(new Foyer("0", null, "0", null, null));
		when(jsonDaoImplements.personsOfStationAdultsAndChild(station_number)).thenReturn(listFoyer);
		mockMvc.perform(get("/firestation").param("stationNumber", station_number)).andExpect(status().is(404));
		LOGGER.info("Fin test : Le système NE retourne PAS une liste des personnes (prénom, nom, adresse, numéro de\r\n"
				+ "téléphone) couvertes par la caserne de pompiers correspondante ainsi qu’un\r\n"
				+ "décompte du nombre d’adultes (>18 ans) et du nombre d’enfants (<=18 ans)");
	}

	@Test
	public void testGetNoPersonsOfStationAdultsAndChildError() throws Exception {
		List<Foyer> listFoyer = new ArrayList<>();
		String station_number = "3";
		// no adults non children = "0"
		listFoyer = null;
		// listFoyer.add(new Foyer(null, null, null, null, null));
		when(jsonDaoImplements.personsOfStationAdultsAndChild(station_number)).thenReturn(listFoyer);
		mockMvc.perform(get("/firestation").param("stationNumber", station_number)).andExpect(status().is(404));
		LOGGER.info("Fin test : Le système NE retourne PAS une liste des personnes (prénom, nom, adresse, numéro de\r\n"
				+ "téléphone) couvertes par la caserne de pompiers correspondante ainsi qu’un\r\n"
				+ "décompte du nombre d’adultes (>18 ans) et du nombre d’enfants (<=18 ans). La liste est NULLE !");
	}

	/*
	 * L'utilisateur accède à l’URL :
	 *
	 * http://localhost:9090/childAlert?address=<address>
	 * 
	 * Le système retourne une liste des enfants (<=18 ans) habitant à cette
	 * adresse. La liste doit comprendre : prénom, nom, âge et une liste des autres
	 * membres du foyer. S’il n’y a pas d’enfant, cette url peut renvoyer une chaîne
	 * vide.
	 */
	@Test
	public void testGetchildPersonsAlertAddress() throws Exception {

		// empty list
		List<ChildAlert> listChildren = new ArrayList<>();
		String une_adresse = "TestUneAdresse";
		when(jsonDaoImplements.childPersonsAlertAddress(une_adresse)).thenReturn(listChildren);
		mockMvc.perform(get("/childAlert").param("address", une_adresse)).andExpect(status().isOk());

	}

	@Test
	public void testGetchildPersonsAlertNoAddressValid() throws Exception {

		List<ChildAlert> listChildren = new ArrayList<>();
		listChildren = null;
		String une_adresse = "TestUneAdresse";
		when(jsonDaoImplements.childPersonsAlertAddress(une_adresse)).thenReturn(listChildren);
		mockMvc.perform(get("/childAlert").param("address", une_adresse)).andExpect(status().is(404));

	}

	/*
	 * L'utilisateur accède à l’URL :
	 *
	 * http://localhost:9090/phoneAlert?firestation=< firestation _number>
	 *
	 * Le système retourne une liste des numéros de téléphone des résidents
	 * desservis par la caserne de pompiers.
	 */

	@Test
	public void testGetphoneAlertFirestation() throws Exception {
		List<PhoneAlert> listPhoneAlert = new ArrayList<>();
		List<String> listPhones = new ArrayList<>();
		listPhones.add("123456-789");
		listPhoneAlert.add(new PhoneAlert(listPhones));
		String no_firestation = "1";
		when(jsonDaoImplements.phoneAlertFirestation(no_firestation)).thenReturn(listPhoneAlert);
		mockMvc.perform(get("/phoneAlert").param("firestation", no_firestation)).andExpect(status().isOk());
	}

	@Test
	public void testGetNoPhoneAlertFirestation() throws Exception {
		List<PhoneAlert> listPhoneAlert = new ArrayList<>();
		List<String> listPhones = new ArrayList<>();
		// listPhones.add("123456-789");
		listPhoneAlert.add(new PhoneAlert(listPhones));
		String no_firestation = "1";
		when(jsonDaoImplements.phoneAlertFirestation(no_firestation)).thenReturn(listPhoneAlert);
		mockMvc.perform(get("/phoneAlert").param("firestation", no_firestation)).andExpect(status().is(404));
	}

	@Test
	public void testGetPhoneAlertNoFirestation() throws Exception {
		String no_firestation = "";
		mockMvc.perform(get("/phoneAlert").param("firestation", no_firestation)).andExpect(status().is(400));
	}

	/*
	 * L'utilisateur accède à l’URL :
	 * 
	 * http://localhost:9090/fire?address=<address>
	 * 
	 * Le système retourne une liste des habitants vivants à l’adresse donnée ainsi
	 * que le numéro de la caserne de pompiers la desservant. La liste doit inclure
	 * : le nom, le numéro de téléphone, l’âge et les antécédents médicaux
	 * (médicaments, posologie et allergies) de chaque personne.
	 * 
	 */

	@Test
	public void testGetfireNoPersonToAddress() throws Exception {

		List<FireAddress> listFireAddress = new ArrayList<>();
		String une_adresse = "TestUneAdresse";
		// if listFireAddress is Empty
		when(jsonDaoImplements.fireAddress(une_adresse)).thenReturn(listFireAddress);
		mockMvc.perform(get("/fire").param("address", une_adresse)).andExpect(status().is(404));
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
	public void testGetfireNoAddress() throws Exception {

		String une_adresse = "";
		mockMvc.perform(get("/fire").param("address", une_adresse)).andExpect(status().is(400));
	}

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
	@Test
	public void testGetstationListFirestation() throws Exception {

		List<PersonsFireStation> listPersonsFireStation = new ArrayList<>();
		PersonsFireStation personsFireStation = new PersonsFireStation();
		personsFireStation.setAddress("TestAddress");
		List<AddressListFirestation> listAddressListFirestation = new ArrayList<>();
		personsFireStation.setListAddressFirestations(listAddressListFirestation);
		listPersonsFireStation.add(personsFireStation);
		List<String> param_station = new ArrayList<>();
		param_station.add("1");
		param_station.add("3");
		when(jsonDaoImplements.stationListFirestation(param_station)).thenReturn(listPersonsFireStation);
		mockMvc.perform(get("/flood/station").param("station", param_station.get(0).toString()).param("station",
				param_station.get(1).toString())).andExpect(status().isOk());
	}

	@Test
	public void testGetstationNoListFirestation() throws Exception {

		// empty list
		List<PersonsFireStation> listPersonsFireStation = new ArrayList<>();
		List<String> param_station = new ArrayList<>();
		param_station.add("1");
		param_station.add("3");
		when(jsonDaoImplements.stationListFirestation(param_station)).thenReturn(listPersonsFireStation);
		mockMvc.perform(get("/flood/station").param("station", param_station.get(0).toString()).param("station",
				param_station.get(1).toString())).andExpect(status().is(404));
	}

	@Test
	public void testGetstationNoFirestation() throws Exception {

		List<String> param_station = new ArrayList<>();
		param_station.add("");
		mockMvc.perform(get("/flood/station").param("station", param_station.get(0).toString()))
				.andExpect(status().is(400));
	}

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

	@Test
	public void testGetpersonInfo() throws Exception {

		String first_name = "TestFirstName";
		String last_name = "TestLastName";
		List<PersonInfo> listPeronInfo = new ArrayList<>();
		listPeronInfo.add(new PersonInfo(first_name, last_name, null, null, null, null, null));
		when(jsonDaoImplements.personInfo(first_name, last_name)).thenReturn(listPeronInfo);
		mockMvc.perform(get("/personInfo").param("firstName", first_name).param("lastName", last_name))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetNoPersonsInfo() throws Exception {

		String first_name = "TestFirstName";
		String last_name = "TestLastName";
		List<PersonInfo> listPeronInfo = new ArrayList<>();
		when(jsonDaoImplements.personInfo(first_name, last_name)).thenReturn(listPeronInfo);
		mockMvc.perform(get("/personInfo").param("firstName", first_name).param("lastName", last_name))
				.andExpect(status().is(404));
	}

	@Test
	public void testGetNoNamePerson() throws Exception {

		String first_name = "";
		String last_name = "";
		mockMvc.perform(get("/personInfo").param("firstName", first_name).param("lastName", last_name))
				.andExpect(status().is(400));
	}

	/*
	 * L'utilisateur accède à l’URL :
	 * 
	 * http://localhost:9090/communityEmail?city=<city>
	 * 
	 * Le système retourne les adresses mail de tous les habitants de la ville.
	 * 
	 */
	@Test
	public void testGetcommunityEmail() throws Exception {

		List<CommunityEmail> listCommunityEmail = new ArrayList<>();
		List<String> listEmail = new ArrayList<>();
		listEmail.add("test@test.com");
		listCommunityEmail.add(new CommunityEmail(listEmail));
		String test_city = "TestCity";
		when(jsonDaoImplements.communityEmail(test_city)).thenReturn(listCommunityEmail);
		mockMvc.perform(get("/communityEmail").param("city", test_city)).andExpect(status().isOk());
	}

	@Test
	public void testGetcommunityNoEmail() throws Exception {

		List<CommunityEmail> listCommunityEmail = new ArrayList<>();
		List<String> listEmail = new ArrayList<>();
		// listEmail.add("");
		listCommunityEmail.add(new CommunityEmail(listEmail));
		String test_city = "TestCity";
		when(jsonDaoImplements.communityEmail(test_city)).thenReturn(listCommunityEmail);
		mockMvc.perform(get("/communityEmail").param("city", test_city)).andExpect(status().is(404));
	}

	@Test
	public void testGetcommunityEmailCityBlank() throws Exception {

		String test_city = "";
		mockMvc.perform(get("/communityEmail").param("city", test_city)).andExpect(status().is(400));
	}

	@Test
	public void testDeletePersonNoFirstNameAndNoLastName() throws Exception {

		mockMvc.perform(delete("/person").param("firstName", "").param("lastName", "")).andExpect(status().is(400));
	}

	@Test
	public void testDeletePersonNoPerson() throws Exception {

		when(jsonDaoImplements.deletePerson("testFirstName", "testLastName")).thenReturn(false);
		mockMvc.perform(delete("/person").param("firstName", "testFirstName").param("lastName", "testLastName"))
				.andExpect(status().is(404));
	}

	@Test
	public void testUpdatePersonNoFirstNameAndNoLastName() throws Exception {

		String body_put = "{\r\n" + "        \"firstName\": \"TestFirstName\",\r\n"
				+ "        \"lastName\": \"TestLastName\",\r\n" + "        \"address\": \"Test1509 Culver St\",\r\n"
				+ "        \"city\": \"TestCulver\",\r\n" + "        \"zip\": \"Test97451\",\r\n"
				+ "        \"phone\": \"Test841-874-6512\",\r\n" + "        \"email\": \"Testjaboyd@email.com\"\r\n"
				+ "    }";

		mockMvc.perform(put("/person").content(body_put).contentType(MediaType.APPLICATION_JSON).param("firstName", "")
				.param("lastName", "")).andExpect(status().is(400));
	}

	@Test
	public void testUpdatePersonNoPerson() throws Exception {

		String body_put = "{\r\n" + "        \"firstName\": \"TestFirstName\",\r\n"
				+ "        \"lastName\": \"TestLastName\",\r\n" + "        \"address\": \"Test1509 Culver St\",\r\n"
				+ "        \"city\": \"TestCulver\",\r\n" + "        \"zip\": \"Test97451\",\r\n"
				+ "        \"phone\": \"Test841-874-6512\",\r\n" + "        \"email\": \"Testjaboyd@email.com\"\r\n"
				+ "    }";

		Persons persons = new Persons();
		when(jsonDaoImplements.updatePerson(persons, "testFirstName", "testLastName")).thenReturn(false);
		mockMvc.perform(put("/person").content(body_put).contentType(MediaType.APPLICATION_JSON)
				.param("firstName", "testFirstName").param("lastName", "testLastName")).andExpect(status().is(404));
	}

	@Test
	public void testlistFirestationsNumberStationNumberBlank() throws Exception {
		String station_number = " ";
		mockMvc.perform(get("/Firestations/" + station_number)).andExpect(status().is(400));
		LOGGER.info("Fin test : vous avez mis un espace au lieu d'un numéro de caserne.");
	}

	@Test
	public void testdeleteFirestationNoAddressNoStationNumber() throws Exception {
		String station_number = "";
		String address = "";
		mockMvc.perform(delete("/firestation").param("address", address).param("stationNumber", station_number))
				.andExpect(status().is(400));
		LOGGER.info("Fin test : vous n'avez pas saisi ni adresse ni numéro de caserne.");
	}

	@Test
	public void testdeleteFirestationNotDeleted() throws Exception {
		String station_number = "1";
		String address = "TestAddress";
		when(jsonDaoImplements.deleteFirestation(address, station_number)).thenReturn(false);
		mockMvc.perform(delete("/firestation").param("address", address).param("stationNumber", station_number))
				.andExpect(status().is(404));
		LOGGER.info("Fin test : la caserne n'a pas été supprimée.");
	}

	@Test
	public void testupdateFirestationsNoUpdate() throws Exception {
		Firestations firestation = new Firestations();
		String address = "TestAddress";
		String station_added = "1";

		String body = " {\r\n" + "\"address\": \"" + address + "\",\r\n" + "\"station\": \"" + station_added + "\"\r\n"
				+ "}";

		when(jsonDaoImplements.updateFirestation(firestation, address)).thenReturn(false);
		mockMvc.perform(
				put("/firestation").content(body).contentType(MediaType.APPLICATION_JSON).param("address", address))
				.andExpect(status().is(404));
		LOGGER.info("Fin test : la caserne n'a pas été supprimée.");
	}

	@Test
	public void testdeleteMedicalRecordNoFirstName() throws Exception {
		String firstName = "";
		String lastName = "Test";
		mockMvc.perform(delete("/medicalRecord").param("firstName", firstName).param("lastName", lastName))
				.andExpect(status().is(400));
		LOGGER.info("Fin test : vous n'avez pas saisi le prénom.");
	}

	@Test
	public void testdeleteMedicalRecordNoLastName() throws Exception {
		String firstName = "Test";
		String lastName = "";
		mockMvc.perform(delete("/medicalRecord").param("firstName", firstName).param("lastName", lastName))
				.andExpect(status().is(400));
		LOGGER.info("Fin test : vous n'avez pas saisi le nom.");
	}

	@Test
	public void testdeleteMedicalRecordNoDeleted() throws Exception {
		String firstName = "Test";
		String lastName = "Test";
		when(jsonDaoImplements.deleteMedicalRecord(firstName, lastName)).thenReturn(false);
		mockMvc.perform(delete("/medicalRecord").param("firstName", firstName).param("lastName", lastName))
				.andExpect(status().is(404));
		LOGGER.info("Fin test : vous n'avez pas saisi le nom.");
	}

	@Test
	public void testupdateMedicalRecordNotUpdated() throws Exception {
		String first_name_test = "TestFirstName";
		String last_name_test = "TestLastName";

		String body_put = "{\r\n" + "\"firstName\": \"TestPutFirstName\",\r\n"
				+ "\"lastName\": \"TestPutLastName\",\r\n" + "\"birthdate\": \"03/06/1984\",\r\n"
				+ "\"medications\": [\r\n" + "\"Testaznol:350mg\",\r\n" + "\"hydrapermazol:100mg\"\r\n" + "],\r\n"
				+ "\"allergies\": [\r\n" + "\"nillacilan\"\r\n" + "]\r\n" + "}";

		when(jsonDaoImplements.updateMedicalRecord(null, first_name_test, last_name_test)).thenReturn(false);
		mockMvc.perform(put("/medicalRecord").content(body_put).contentType(MediaType.APPLICATION_JSON)
				.param("firstName", first_name_test).param("lastName", last_name_test)).andExpect(status().is(404));
		LOGGER.info("Fin test : le medical records n'a pas été mis à jour.");
	}

	@Test
	public void testUpdateMedicalrecordsNoName() throws Exception {

		String first_name_test = "";
		String last_name_test = "";

		String body_put = "{\r\n" + "\"firstName\": \"TestPutFirstName\",\r\n"
				+ "\"lastName\": \"TestPutLastName\",\r\n" + "\"birthdate\": \"03/06/1984\",\r\n"
				+ "\"medications\": [\r\n" + "\"Testaznol:350mg\",\r\n" + "\"hydrapermazol:100mg\"\r\n" + "],\r\n"
				+ "\"allergies\": [\r\n" + "\"nillacilan\"\r\n" + "]\r\n" + "}";

		mockMvc.perform(put("/medicalRecord").content(body_put).contentType(MediaType.APPLICATION_JSON)
				.param("firstName", first_name_test).param("lastName", last_name_test)).andExpect(status().is(400));

	}

	@Test
	public void testAddMedicalrecordsNoAdded() throws Exception {

		String first_name_test = "TestFirstName";
		String last_name_test = "TestLastName";

		String body = "{\r\n" + "\"firstName\": \"" + first_name_test + "\",\r\n" + "\"lastName\": \"" + last_name_test
				+ "\",\r\n" + "\"birthdate\": \"03/06/1984\",\r\n" + "\"medications\": [\r\n" + "\"aznol:350mg\",\r\n"
				+ "\"hydrapermazol:100mg\"\r\n" + "],\r\n" + "\"allergies\": [\r\n" + "\"nillacilan\"\r\n" + "]\r\n"
				+ "}";

		when(jsonDaoImplements.addMedicalRecord(null)).thenReturn(null);
		mockMvc.perform(post("/medicalRecord").content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(404));
	}

	@Test
	public void testgetAPerson() throws Exception {
		String firstNamelastName = " ";
		mockMvc.perform(get("/person/" + firstNamelastName)).andExpect(status().is(400));
		LOGGER.info("Fin test : vous avez mis un espace au lieu du nom + prénom de la personne.");
	}

	@Test
	public void testAddPersonNoAdded() throws Exception {

		String first_name_test = "TestFirstName";
		String last_name_test = "TestLastName";
		String body = "{\r\n" + "\"firstName\": \"" + first_name_test + "\",\r\n" + "\"lastName\": \"" + last_name_test
				+ "\",\r\n" + "\"address\": \"1509 Culver St\",\r\n" + "\"city\": \"Culver\",\r\n"
				+ "\"zip\": \"97451\",\r\n" + "\"phone\": \"841-874-6512\",\r\n" + "\"email\": \"jaboyd@email.com\"\r\n"
				+ "}";

		when(jsonDaoImplements.addPerson(null)).thenReturn(null);
		mockMvc.perform(post("/person").content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(404));
	}

	@Test
	public void testAddFirestationNoAdded() throws Exception {

		// station added
		String station_added = "Test99";
		String adresse_added = "Test1509 Culver St";
		String body = " {\r\n" + "\"address\": \"" + adresse_added + "\",\r\n" + "\"station\": \"" + station_added
				+ "\"\r\n" + "}";

		when(jsonDaoImplements.addFirestation(null)).thenReturn(null);
		mockMvc.perform(post("/firestation").content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(404));
	}

	@Test
	public void testgetAMedicalRecordNoFirstNameNoLastName() throws Exception {

		String first_name_test = " ";
		String last_name_test = " ";

		mockMvc.perform(get("/medicalRecord/{firstNamelastName}", first_name_test + last_name_test))
				.andExpect(status().is(400));
		LOGGER.info("Fin test : vous avez mis un espace au lieu du nom + prénom de la personne.");

	}

	@Test
	public void testchildAlertNoAddress() throws Exception {

		String param_address = " ";

		mockMvc.perform(get("/childAlert").param("address", param_address)).andExpect(status().is(400));

		LOGGER.info("Fin test : vous avez mis un espace au lieu de l'adresse.");
	}

}
