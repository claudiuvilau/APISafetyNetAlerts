package com.openclassrooms.safetynetalerts.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;

@SpringBootTest
@AutoConfigureMockMvc
public class EndPointsControllerCRUDiTest {

	@Autowired
	private MockMvc mockMvc;

	private JsonDaoImplements jsonDaoImplements = new JsonDaoImplements();

	@Test
	public void testAddPerson() throws Exception {

		String first_name_test = "TestFirstName";
		String last_name_test = "TestLastName";
		String body = "{\r\n" + "\"firstName\": \"" + first_name_test + "\",\r\n" + "\"lastName\": \"" + last_name_test
				+ "\",\r\n" + "\"address\": \"1509 Culver St\",\r\n" + "\"city\": \"Culver\",\r\n"
				+ "\"zip\": \"97451\",\r\n" + "\"phone\": \"841-874-6512\",\r\n" + "\"email\": \"jaboyd@email.com\"\r\n"
				+ "}";

		mockMvc.perform(post("/person").content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(201));
		mockMvc.perform(get("/person/{firstNamelastName}", first_name_test + last_name_test)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName", is(first_name_test)));

		// delete the test made
		jsonDaoImplements.deletePerson(first_name_test, last_name_test);
		// verify if it is deleted
		mockMvc.perform(get("/person/{firstNamelastName}", first_name_test + last_name_test))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testDeletePerson() throws Exception {
		// add person
		String first_name_test = "TestFirstName";
		String last_name_test = "TestLastName";
		String body = "{\r\n" + "\"firstName\": \"" + first_name_test + "\",\r\n" + "\"lastName\": \"" + last_name_test
				+ "\",\r\n" + "\"address\": \"1509 Culver St\",\r\n" + "\"city\": \"Culver\",\r\n"
				+ "\"zip\": \"97451\",\r\n" + "\"phone\": \"841-874-6512\",\r\n" + "\"email\": \"jaboyd@email.com\"\r\n"
				+ "}";
		mockMvc.perform(post("/person").content(body).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(delete("/person").param("firstName", first_name_test).param("lastName", last_name_test))
				.andExpect(status().isOk());

		// verify if it is deleted
		mockMvc.perform(get("/person/{firstNamelastName}", first_name_test + last_name_test))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testUpdatePerson() throws Exception {
		String first_name_test = "TestFirstName";
		String last_name_test = "TestLastName";
		String body = "{\r\n" + "\"firstName\": \"" + first_name_test + "\",\r\n" + "\"lastName\": \"" + last_name_test
				+ "\",\r\n" + "\"address\": \"1509 Culver St\",\r\n" + "\"city\": \"Culver\",\r\n"
				+ "\"zip\": \"97451\",\r\n" + "\"phone\": \"841-874-6512\",\r\n" + "\"email\": \"jaboyd@email.com\"\r\n"
				+ "}";

		String body_put = "{\r\n" + "\"firstName\": \"" + first_name_test + "\",\r\n" + "\"lastName\": \""
				+ last_name_test + "\",\r\n" + "\"address\": \"Test1509 Culver St\",\r\n"
				+ "\"city\": \"TestCulver\",\r\n" + "\"zip\": \"Test97451\",\r\n"
				+ "\"phone\": \"Test841-874-6512\",\r\n" + "\"email\": \"Testjaboyd@email.com\"\r\n" + "}";

		mockMvc.perform(post("/person").content(body).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(put("/person").content(body_put).contentType(MediaType.APPLICATION_JSON)
				.param("firstName", first_name_test).param("lastName", last_name_test)).andExpect(status().isOk());

		mockMvc.perform(get("/person/{firstNamelastName}", first_name_test + last_name_test)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].city", is("TestCulver")));

		// delete the test made
		jsonDaoImplements.deletePerson(first_name_test, last_name_test);
		// verify if it is deleted
		mockMvc.perform(get("/person/{firstNamelastName}", first_name_test + last_name_test))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testAddFirestation() throws Exception {

		// station added
		String station_added = "Test99";
		String adresse_added = "Test1509 Culver St";
		String body = " {\r\n" + "\"address\": \"" + adresse_added + "\",\r\n" + "\"station\": \"" + station_added
				+ "\"\r\n" + "}";

		mockMvc.perform(post("/firestation").content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(201));
		mockMvc.perform(get("/Firestations/{station}", station_added)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].address", is(adresse_added)));

		// delete the test made
		jsonDaoImplements.deleteFirestation("", station_added);
		// verify if it is deleted
		mockMvc.perform(get("/Firestations/{station}", station_added)).andExpect(status().isNotFound());

	}

	@Test
	public void testDeleteFirestation() throws Exception {
		// add person
		// station added
		String station_added = "Test99";
		String body = " {\r\n" + "        \"address\": \"Test1509 Culver St\",\r\n" + "        \"station\": \""
				+ station_added + "\"\r\n" + "    }";
		mockMvc.perform(post("/firestation").content(body).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(delete("/firestation").param("stationNumber", station_added).param("address", ""))
				.andExpect(status().isOk());

		mockMvc.perform(get("/Firestations/{station}", station_added)).andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteFirestationAddressNoBlankANDstationNumberIsBlank() throws Exception {

		String station_blank = "";
		String address_no_blank = "Test1509 Culver St";

		// add person
		// station added
		String station_added = "Test99";
		String body = " {\r\n" + "        \"address\": \"" + address_no_blank + "\",\r\n" + "        \"station\": \""
				+ station_added + "\"\r\n" + "    }";
		mockMvc.perform(post("/firestation").content(body).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(delete("/firestation").param("stationNumber", station_blank).param("address", address_no_blank))
				.andExpect(status().isOk());

		mockMvc.perform(get("/Firestations/{station}", station_added)).andExpect(status().isNotFound());

	}

	@Test
	public void testUpdatefirestation() throws Exception {

		String test_station_udated = "TestStation";
		String test_adresse_update = "Test1509 Culver St";
		String body = " {\r\n" + "        \"address\": \"" + test_adresse_update + "\",\r\n"
				+ "        \"station\": \"Test99\"\r\n" + "    }";

		String body_put = " {\r\n" + "        \"address\": \"" + test_adresse_update + "\",\r\n"
				+ "        \"station\": \"" + test_station_udated + "\"\r\n" + "    }";

		mockMvc.perform(post("/firestation").content(body).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(put("/firestation").content(body_put).contentType(MediaType.APPLICATION_JSON).param("address",
				test_adresse_update)).andExpect(status().isOk());

		mockMvc.perform(get("/Firestations/{station}", test_station_udated)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].address", is(test_adresse_update)));

		// delete the test made
		jsonDaoImplements.deleteFirestation("", test_station_udated);
		// verify if it is deleted
		mockMvc.perform(get("/Firestations/{station}", test_station_udated)).andExpect(status().isNotFound());

	}

	@Test
	public void testUpdatefirestationNoAddress() throws Exception {

		String test_station_udated = "TestStation";
		String test_adresse_update = "Test1509 Culver St";
		String test_no_adresse = "";

		String body_put = " {\r\n" + "        \"address\": \"" + test_adresse_update + "\",\r\n"
				+ "        \"station\": \"" + test_station_udated + "\"\r\n" + "    }";

		mockMvc.perform(put("/firestation").content(body_put).contentType(MediaType.APPLICATION_JSON).param("address",
				test_no_adresse)).andExpect(status().is(400));

	}

	@Test
	public void testAddMedicalrecords() throws Exception {

		String first_name_test = "TestFirstName";
		String last_name_test = "TestLastName";

		String body = "{\r\n" + "\"firstName\": \"" + first_name_test + "\",\r\n" + "\"lastName\": \"" + last_name_test
				+ "\",\r\n" + "\"birthdate\": \"03/06/1984\",\r\n" + "\"medications\": [\r\n" + "\"aznol:350mg\",\r\n"
				+ "\"hydrapermazol:100mg\"\r\n" + "],\r\n" + "\"allergies\": [\r\n" + "\"nillacilan\"\r\n" + "]\r\n"
				+ "}";

		mockMvc.perform(post("/medicalRecord").content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(201));

		mockMvc.perform(get("/medicalRecord/{firstNamelastName}", first_name_test + last_name_test))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].firstName", is(first_name_test)));

		// delete the test made
		jsonDaoImplements.deleteMedicalRecord(first_name_test, last_name_test);

		// verify if it is deleted
		mockMvc.perform(get("/medicalRecord/{firstNamelastName}", first_name_test + last_name_test))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testDeleteMedicalrecords() throws Exception {
		// add person
		String first_name_test = "TestFirstName";
		String last_name_test = "TestLastName";
		String body = "{\r\n" + "\"firstName\": \"" + first_name_test + "\",\r\n" + "\"lastName\": \"" + last_name_test
				+ "\",\r\n" + "\"birthdate\": \"03/06/1984\",\r\n" + "\"medications\": [\r\n" + "\"aznol:350mg\",\r\n"
				+ "\"hydrapermazol:100mg\"\r\n" + "],\r\n" + "\"allergies\": [\r\n" + "\"nillacilan\"\r\n" + "]\r\n"
				+ "}";
		mockMvc.perform(post("/medicalRecord").content(body).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(delete("/medicalRecord").param("firstName", first_name_test).param("lastName", last_name_test))
				.andExpect(status().isOk());

		// verify if it is deleted
		mockMvc.perform(get("/medicalRecord/{firstNamelastName}", first_name_test + last_name_test))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testUpdateMedicalrecords() throws Exception {

		String first_name_test = "TestFirstName";
		String last_name_test = "TestLastName";

		String body = "{\r\n" + "\"firstName\": \"" + first_name_test + "\",\r\n" + "\"lastName\": \"" + last_name_test
				+ "\",\r\n" + "\"birthdate\": \"03/06/1984\",\r\n" + "\"medications\": [\r\n" + "\"aznol:350mg\",\r\n"
				+ "\"hydrapermazol:100mg\"\r\n" + "],\r\n" + "\"allergies\": [\r\n" + "\"nillacilan\"\r\n" + "]\r\n"
				+ "}";

		String body_put = "{\r\n" + "\"firstName\": \"TestPutFirstName\",\r\n"
				+ "\"lastName\": \"TestPutLastName\",\r\n" + "\"birthdate\": \"03/06/1984\",\r\n"
				+ "\"medications\": [\r\n" + "\"Testaznol:350mg\",\r\n" + "\"hydrapermazol:100mg\"\r\n" + "],\r\n"
				+ "\"allergies\": [\r\n" + "\"nillacilan\"\r\n" + "]\r\n" + "}";

		mockMvc.perform(post("/medicalRecord").content(body).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(put("/medicalRecord").content(body_put).contentType(MediaType.APPLICATION_JSON)
				.param("firstName", first_name_test).param("lastName", last_name_test)).andExpect(status().isOk());

		mockMvc.perform(get("/medicalRecord/{firstNamelastName}", first_name_test + last_name_test))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].medications.[0]", is("Testaznol:350mg")));

		// delete the test made
		jsonDaoImplements.deleteMedicalRecord(first_name_test, last_name_test);
		// verify if it is deleted
		mockMvc.perform(get("/medicalRecord/{firstNamelastName}", first_name_test + last_name_test))
				.andExpect(status().isNotFound());

	}

}
