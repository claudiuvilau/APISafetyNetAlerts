package com.openclassrooms.safetynetalerts.controller;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.openclassrooms.safetynetalerts.ApiSafetyNetAlertsApplication;
import com.openclassrooms.safetynetalerts.dao.JsonDao;
import com.openclassrooms.safetynetalerts.dao.ReadJsonFile;
import com.openclassrooms.safetynetalerts.model.ChildAlert;
import com.openclassrooms.safetynetalerts.model.Children;
import com.openclassrooms.safetynetalerts.model.CommunityEmail;
import com.openclassrooms.safetynetalerts.model.FireAddress;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Foyer;
import com.openclassrooms.safetynetalerts.model.Medicalrecords;
import com.openclassrooms.safetynetalerts.model.PersonInfo;
import com.openclassrooms.safetynetalerts.model.Persons;
import com.openclassrooms.safetynetalerts.model.PersonsFireStation;
import com.openclassrooms.safetynetalerts.model.PhoneAlert;

@RestController
public class EndPointsController {

	@Autowired
	private JsonDao jsonDao;
	private ReadJsonFile readJsonFile;
	private List<Firestations> listFirestations = new ArrayList<>();
	private List<Persons> listPersons = new ArrayList<>();
	

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiSafetyNetAlertsApplication.class);
	
	
	// Persons
	@GetMapping(value = "Persons")
	public List<Persons> afficherPersonnes() {
		
		List<Persons> listP = new ArrayList<>();
		try {
			readJsonFile = new ReadJsonFile();
			listP = readJsonFile.readfilejsonPersons();
		} catch (IOException e) {
			e.printStackTrace();
		}

		LOGGER.trace("A TRACE Message");
		LOGGER.debug("A DEBUG Message");
		LOGGER.info("An INFO Message");
		LOGGER.warn("A  Message");
		LOGGER.error("An ERROR Message");
		return listP;
	}

	// get a person
	@GetMapping(value = "person/{firstNamelastName}")
	public List<Persons> getAPerson(@PathVariable String firstNamelastName) throws IOException {
		List<Persons> listP = new ArrayList<>();
		listP = jsonDao.getAPerson(firstNamelastName);
		return listP;
	}

	// add a person
	@PostMapping(value = "/person")
	public ResponseEntity<Void> addPerson(@RequestBody Persons persons) throws IOException {

		LOGGER.info("Current Request : " + ServletUriComponentsBuilder.fromCurrentRequest().build());

		// if persons == null the end point is bad request because @RequestBody
		if (persons == null) {
			LOGGER.error("Person in body is null.");
			return ResponseEntity.noContent().build();
		}

		Persons newPerson = jsonDao.addPerson(persons);

		if (newPerson == null) {
			LOGGER.error("The person is not added.");
			return ResponseEntity.unprocessableEntity().build();
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{firstName+lastName}")
				.buildAndExpand(newPerson.getFirstName() + newPerson.getLastName()).toUri();
		LOGGER.info("A new person is added successful. The URL is : " + location);
		return ResponseEntity.created(location).build();
	}

	// update person
	@PutMapping(value = "/person")
	public void updatePerson(@RequestBody Persons persons, @RequestParam String firstName,
			@RequestParam String lastName) throws IOException {
		jsonDao.updatePerson(persons, firstName, lastName);
	}

	// delete person
	@DeleteMapping(value = "/person")
	public void deletePerson(@RequestParam String firstName, @RequestParam String lastName) throws IOException {
		jsonDao.deletePerson(firstName, lastName);
	}

	// Fire stations
	@GetMapping(value = "Firestations")
	public List<Firestations> afficherFirestations() {
		List<Firestations> listF = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		try {
			listF = readJsonFile.readfilejsonFirestations();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listF;
	}

	// add fire station
	@PostMapping(value = "/firestation")
	public void addFirestations(@RequestBody Firestations firestation) throws IOException {
		jsonDao.addFirestation(firestation);
	}

	// update fire station
	@PutMapping(value = "/firestation")
	public void updateFirestations(@RequestBody Firestations firestation, @RequestParam String address)
			throws IOException {
		jsonDao.updateFirestation(firestation, address);
	}

	// delete fire station
	@DeleteMapping(value = "/firestation")
	public void deleteFirestation(@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "stationNumber", required = false) String stationNumber) throws IOException {
		jsonDao.deleteFirestation(address, stationNumber);
	}

	// Medical records
	@GetMapping(value = "Medicalrecords")
	public List<Medicalrecords> afficherMedicalrecords() {
		List<Medicalrecords> listM = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		try {
			listM = readJsonFile.readfilejsonMedicalrecords();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listM;
	}

	// get a medical record
	@GetMapping(value = "medicalRecord/{firstNamelastName}")
	public List<Medicalrecords> getAMedicalRecord(@PathVariable String firstNamelastName) throws IOException {
		List<Medicalrecords> listM = new ArrayList<>();
		listM = jsonDao.getAMedicalrecord(firstNamelastName);
		return listM;
	}

	
	// add a medical records
	@PostMapping(value = "/medicalRecord")
	public void addMedicalRecord(@RequestBody Medicalrecords medicalRecord) throws IOException {
		jsonDao.addMedicalRecord(medicalRecord);
	}

	// update medical records
	@PutMapping(value = "/medicalRecord")
	public void updateMedicalRecord(@RequestBody Medicalrecords medicalRecord, @RequestParam String firstName,
			@RequestParam String lastName) throws IOException {
		jsonDao.updateMedicalRecord(medicalRecord, firstName, lastName);
	}

	// delete medical records
	@DeleteMapping(value = "/medicalRecord")
	public void deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) throws IOException {
		jsonDao.deleteMedicalRecord(firstName, lastName);
	}

	// Find all children
	@GetMapping(value = "Children")
	public List<Children> afficherChildren(@RequestParam int old) throws IOException, ParseException {
		List<Children> listM = new ArrayList<>();
		listM = jsonDao.findOld(old);
		return listM;
	}

	// Fire stations N°
	@GetMapping(value = "Firestations/{station}")
	public List<Firestations> listFirestationsNumber(@PathVariable String station) {
		listFirestations = jsonDao.filterStation(station);
		return listFirestations;
	}

	// Address
	@GetMapping(value = "Persons/{address}")
	public List<?> listPersonsOfAddress(@PathVariable String address) throws IOException {
		listPersons = jsonDao.filterAddressInPersons(address);
		return listPersons;
	}

	@GetMapping("firestation")
	public ResponseEntity<List<Foyer>> firestationStationNumber(@RequestParam String stationNumber) throws IOException, ParseException {

		if (stationNumber == null || stationNumber.length() == 0) {
			return ResponseEntity.noContent().build();
		}

		List<Foyer> listFoyer = new ArrayList<>();
		listFoyer = jsonDao.personsOfStationAdultsAndChild(stationNumber);
		
		// if we have 0 adult 0 children
		if (listFoyer.get(0).getDecompteAdult().equals("0") && listFoyer.get(0).getDecompteChildren().equals("0")) {
			return ResponseEntity.unprocessableEntity().build();
		}
		return new ResponseEntity<List<Foyer>>(listFoyer, HttpStatus.OK);
	}

	@GetMapping("childAlert")
	public ResponseEntity<List<ChildAlert>> childAlert(@RequestParam String address) throws IOException, ParseException {
		
		if (address == null || address.length() == 0) {
			return ResponseEntity.noContent().build();
		}
		
		List<ChildAlert> listChildren = new ArrayList<>();
		listChildren = jsonDao.childPersonsAlertAddress(address);
		
		if (listChildren == null) {
			return ResponseEntity.notFound().build();
		}
		
		return new ResponseEntity<List<ChildAlert>>(listChildren, HttpStatus.OK);

	}

	/*
	@GetMapping("phoneAlert")
	public MappingJacksonValue phoneAlertStationNumber(@RequestParam String firestation) throws IOException {

		List<PhoneAlert> listPhoneAlert = new ArrayList<>();
		listPhoneAlert = jsonDao.phoneAlertFirestation(firestation);

		SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("firstName");

		FilterProvider listDeMesFiltres = new SimpleFilterProvider().addFilter("monFiltrePhoneAlert", monFiltre);

		MappingJacksonValue phoneAlertStationNumberFiltres = new MappingJacksonValue(listPhoneAlert);

		phoneAlertStationNumberFiltres.setFilters(listDeMesFiltres);

		return phoneAlertStationNumberFiltres;
		// return listPersons;
	}
*/
	@GetMapping("phoneAlert")
	public ResponseEntity<List<PhoneAlert>> phoneAlertStationNumber(@RequestParam String firestation) throws IOException {

		if (firestation.isBlank()) {
			return ResponseEntity.noContent().build();
		}
		
		List<PhoneAlert> listPhoneAlert = new ArrayList<>();
		listPhoneAlert = jsonDao.phoneAlertFirestation(firestation);

		if (listPhoneAlert.get(0).getListPhones().isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return new ResponseEntity<List<PhoneAlert>>(listPhoneAlert, HttpStatus.OK);
	}
	
	@GetMapping("fire")
	public ResponseEntity<List<FireAddress>> fireAddress(@RequestParam String address) throws IOException, ParseException {

		if (address == null || address.length() == 0) {
			return ResponseEntity.noContent().build();
		}

		List<FireAddress> listFireAddress = new ArrayList<>();
		listFireAddress = jsonDao.fireAddress(address);
		
		if (listFireAddress.isEmpty()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		return new ResponseEntity<List<FireAddress>>(listFireAddress, HttpStatus.OK);

	}

	@GetMapping("flood/station")
	public ResponseEntity<List<PersonsFireStation>> fireAddressListFireStation(@RequestParam List<String> station)
			throws IOException, ParseException {
		
		if (station.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		List<PersonsFireStation> listPersonsFireStation = new ArrayList<>();
		listPersonsFireStation = jsonDao.stationListFirestation(station);
		
		if (listPersonsFireStation.isEmpty()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		return new ResponseEntity<List<PersonsFireStation>>(listPersonsFireStation, HttpStatus.OK);
	}

	@GetMapping("personInfo")
	public ResponseEntity<List<PersonInfo>> personInfo(@RequestParam String firstName, @RequestParam String lastName)
			throws IOException, ParseException {
		
		if (firstName.isBlank() || lastName.isBlank()) {
			return ResponseEntity.noContent().build();
		}
		
		List<PersonInfo> listPeronInfo = new ArrayList<>();
		listPeronInfo = jsonDao.personInfo(firstName, lastName);
		
		if (listPeronInfo.isEmpty()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		return new ResponseEntity<List<PersonInfo>>(listPeronInfo, HttpStatus.OK);
	}

	@GetMapping("communityEmail")
	public ResponseEntity<List<CommunityEmail>> communityEmail(@RequestParam String city) throws IOException {
		
		if (city.isBlank()) {
			return ResponseEntity.noContent().build();
		}
		
		List<CommunityEmail> listCommunityEmail = new ArrayList<>();
		listCommunityEmail = jsonDao.communityEmail(city);
		
		if (listCommunityEmail.get(0).getListEmails().isEmpty()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		return new ResponseEntity<List<CommunityEmail>>(listCommunityEmail, HttpStatus.OK);
	}

}
