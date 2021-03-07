package com.openclassrooms.safetynetalerts;

import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Foyer;
import com.openclassrooms.safetynetalerts.service.FilterJsons;
import com.openclassrooms.safetynetalerts.service.LoggerApi;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonDaoImplementsTest {

	private static Logger LOGGER = null;

	// @Autowired
	// private JsonDaoImplements jsonDaoImplements;

	// @MockBean
	// private ReadJsonFile readJsonFile;

	@MockBean
	FilterJsons filterJsons;

	// @Autowired
	// InterfaceFilterJsons interfaceFilterJsons;

	// private JsonDaoImplements jsonDaoImplements;

	@BeforeAll
	public static void setLogger() throws MalformedURLException {
		LoggerApi.setLoggerForTests();
		LOGGER = LogManager.getLogger();
	}

	@Test
	public void testGetpersonsOfStationAdultsAndChild() throws Exception {
		JsonDaoImplements jsonDaoImplements = new JsonDaoImplements();
		List<Foyer> listFoyer = new ArrayList<>();
		List<Firestations> listFirestations = new ArrayList<>();
		// Firestations firestations = new Firestations();
		// firestations.setAddress("");
		// firestations.setStation("");
		// listFirestations.add(firestations);

		LOGGER.info("list firestation vide ? " + listFirestations);
		when(filterJsons.filterStation("1")).thenReturn(listFirestations);
		listFoyer = jsonDaoImplements.personsOfStationAdultsAndChild("1");

		LOGGER.info(listFoyer);

	}

}
