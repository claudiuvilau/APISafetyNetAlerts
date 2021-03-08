package com.openclassrooms.safetynetalerts.controller;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
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

import com.openclassrooms.safetynetalerts.dao.JsonDao;
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
import com.openclassrooms.safetynetalerts.service.FilterJsons;
import com.openclassrooms.safetynetalerts.service.InterfaceFilterJsons;
import com.openclassrooms.safetynetalerts.service.LoggerApi;
import com.openclassrooms.safetynetalerts.service.ReadJsonFile;

@RestController
public class EndPointsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EndPointsController.class);

	@Autowired
	private JsonDao jsonDao;

	private LoggerApi loggerApi = new LoggerApi();

	private ReadJsonFile readJsonFile;

	private List<Firestations> listFirestations = new ArrayList<>();
	private List<Persons> listPersons = new ArrayList<>();

	@Bean
	public HttpTraceRepository htttpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	}

	// get a person
	@GetMapping(value = "person/{firstNamelastName}")
	public ResponseEntity<List<Persons>> getAPerson(@PathVariable String firstNamelastName, HttpServletRequest request,
			HttpServletResponse response) {

		if (firstNamelastName.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The path does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		List<Persons> listP = new ArrayList<>();
		try {
			listP = jsonDao.getAPerson(firstNamelastName);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, firstNamelastName));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (listP.isEmpty()) {
			response.setStatus(404);
			LOGGER.info("The list is empty. No persons with this first name and this last name " + response.getStatus()
					+ ":" + loggerApi.loggerInfo(request, response, firstNamelastName));
			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":"
				+ loggerApi.loggerInfo(request, response, firstNamelastName));
		return new ResponseEntity<List<Persons>>(listP, HttpStatus.valueOf(response.getStatus()));
	}

	// add a person
	@PostMapping(value = "/person")
	public ResponseEntity<Void> addPerson(@RequestBody Persons persons, HttpServletRequest request,
			HttpServletResponse response) {

		// if persons == null the end point is bad request because @RequestBody
		if (persons == null) {
			response.setStatus(400);
			LOGGER.error("The body person does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		Persons newPerson;
		try {
			newPerson = jsonDao.addPerson(persons);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (newPerson == null) {
			response.setStatus(404);
			LOGGER.info("The person is empty. No persons added " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(201);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{firstName+lastName}")
				.buildAndExpand(newPerson.getFirstName() + newPerson.getLastName()).toUri();
		LOGGER.info("A new person is added successful. The URL is : " + location);
		LOGGER.info("Response status " + response.getStatus() + ":" + loggerApi.loggerInfo(request, response, ""));
		return ResponseEntity.created(location).build();
	}

	// update person
	@PutMapping(value = "/person")
	public ResponseEntity<Void> updatePerson(@RequestBody Persons persons, @RequestParam String firstName,
			@RequestParam String lastName, HttpServletRequest request, HttpServletResponse response) {

		if (firstName.isBlank() || lastName.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The params does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		boolean update = false;

		try {
			update = jsonDao.updatePerson(persons, firstName, lastName);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, firstName + " " + lastName));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (update == false) {
			response.setStatus(404);
			LOGGER.info("The person is not updeted " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, firstName + " " + lastName));
			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":"
				+ loggerApi.loggerInfo(request, response, firstName + " " + lastName));
		return ResponseEntity.status(response.getStatus()).build();
	}

	// delete person
	@DeleteMapping(value = "/person")
	public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName,
			HttpServletRequest request, HttpServletResponse response) {

		if (firstName.isBlank() || lastName.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The params does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		boolean del = false;

		try {
			del = jsonDao.deletePerson(firstName, lastName);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, firstName + " " + lastName));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (del == false) {
			response.setStatus(404);
			LOGGER.info("The person is not deleted " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, firstName + " " + lastName));
			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":"
				+ loggerApi.loggerInfo(request, response, firstName + " " + lastName));
		return ResponseEntity.status(response.getStatus()).build();
	}

	// add fire station
	@PostMapping(value = "/firestation")
	public ResponseEntity<Void> addFirestations(@RequestBody Firestations firestation, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if (firestation == null) {
			response.setStatus(400);
			LOGGER.error("The body does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		Firestations newFirestation = new Firestations();
		newFirestation = jsonDao.addFirestation(firestation);

		if (newFirestation == null) {
			response.setStatus(404);

			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(201);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{station}")
				.buildAndExpand(newFirestation.getStation()).toUri();
		LOGGER.info("A new fire station is added successful. The URL is : " + location);
		return ResponseEntity.created(location).build();
	}

	// update fire station
	@PutMapping(value = "/firestation")
	public ResponseEntity<Void> updateFirestations(@RequestBody Firestations firestation, @RequestParam String address,
			HttpServletRequest request, HttpServletResponse response) {

		if (address.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The param does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		boolean update_station;
		try {
			update_station = jsonDao.updateFirestation(firestation, address);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, address));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (update_station == false) {
			response.setStatus(404);
			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":" + loggerApi.loggerInfo(request, response, address));
		return ResponseEntity.status(response.getStatus()).build();
	}

	// delete fire station
	@DeleteMapping(value = "/firestation")
	public ResponseEntity<Void> deleteFirestation(@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "stationNumber", required = false) String stationNumber, HttpServletRequest request,
			HttpServletResponse response) {

		if (address.isBlank() && stationNumber.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The param does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		boolean del = false;

		try {
			del = jsonDao.deleteFirestation(address, stationNumber);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, address + " " + stationNumber));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (del == false) {
			response.setStatus(404);
			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":"
				+ loggerApi.loggerInfo(request, response, address + " " + stationNumber));

		return ResponseEntity.status(response.getStatus()).build();
	}

	// get a medical record
	@GetMapping(value = "medicalRecord/{firstNamelastName}")
	public ResponseEntity<List<Medicalrecords>> getAMedicalRecord(@PathVariable String firstNamelastName,
			HttpServletRequest request, HttpServletResponse response) {

		if (firstNamelastName.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The path does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		List<Medicalrecords> listM = new ArrayList<>();
		try {
			listM = jsonDao.getAMedicalrecord(firstNamelastName);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, firstNamelastName));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (listM.isEmpty()) {
			response.setStatus(404);

			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":"
				+ loggerApi.loggerInfo(request, response, firstNamelastName));
		return new ResponseEntity<List<Medicalrecords>>(listM, HttpStatus.valueOf(response.getStatus()));
	}

	// add a medical records
	@PostMapping(value = "/medicalRecord")
	public ResponseEntity<Void> addMedicalRecord(@RequestBody Medicalrecords medicalRecord, HttpServletRequest request,
			HttpServletResponse response) {

		if (medicalRecord == null) {
			response.setStatus(400);
			LOGGER.error("The body does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		Medicalrecords newMedicalRecord = new Medicalrecords();
		try {
			newMedicalRecord = jsonDao.addMedicalRecord(medicalRecord);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (newMedicalRecord == null) {
			response.setStatus(404);
			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(201);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{firstName+lastName}")
				.buildAndExpand(newMedicalRecord.getFirstName() + newMedicalRecord.getLastName()).toUri();
		LOGGER.info("A new medical record is added successful. The URL is : " + location);
		LOGGER.info("Response status " + response.getStatus() + ":" + loggerApi.loggerInfo(request, response, ""));
		return ResponseEntity.created(location).build();

	}

	// update medical records
	@PutMapping(value = "/medicalRecord")
	public ResponseEntity<Void> updateMedicalRecord(@RequestBody Medicalrecords medicalRecord,
			@RequestParam String firstName, @RequestParam String lastName, HttpServletRequest request,
			HttpServletResponse response) {

		if (firstName.isBlank() || lastName.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The params does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		boolean update = false;

		try {
			update = jsonDao.updateMedicalRecord(medicalRecord, firstName, lastName);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, firstName + " " + lastName));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (update == false) {
			response.setStatus(404);

			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":"
				+ loggerApi.loggerInfo(request, response, firstName + " " + lastName));
		return ResponseEntity.status(response.getStatus()).build();
	}

	// delete medical records
	@DeleteMapping(value = "/medicalRecord")
	public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName,
			HttpServletRequest request, HttpServletResponse response) {

		if (firstName.isBlank() || lastName.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The params does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		boolean del = false;

		try {
			del = jsonDao.deleteMedicalRecord(firstName, lastName);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, firstName + " " + lastName));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (del == false) {
			response.setStatus(404);
			LOGGER.info("The medical record is not deleted " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, firstName + " " + lastName));
			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":"
				+ loggerApi.loggerInfo(request, response, firstName + " " + lastName));
		return ResponseEntity.status(response.getStatus()).build();
	}

	// Fire stations N°
	@GetMapping(value = "Firestations/{station}")
	public ResponseEntity<List<Firestations>> listFirestationsNumber(@PathVariable String station,
			HttpServletRequest request, HttpServletResponse response) {

		if (station.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The path does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		InterfaceFilterJsons filterJsons = createFilterJsons();
		listFirestations = filterJsons.filterStation(station);

		if (listFirestations.isEmpty()) {
			response.setStatus(404);

			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":" + loggerApi.loggerInfo(request, response, station));
		return new ResponseEntity<List<Firestations>>(listFirestations, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("firestation")
	public ResponseEntity<List<Foyer>> firestationStationNumber(@RequestParam String stationNumber,
			HttpServletRequest request, HttpServletResponse response) {

		if (stationNumber.isBlank()) {
			response.setStatus(400);
			LOGGER.info("The param does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		List<Foyer> listFoyer = new ArrayList<>();
		listFoyer = jsonDao.personsOfStationAdultsAndChild(stationNumber);

		// if we have 0 adult 0 children or list is empty"

		if (listFoyer == null) {
			response.setStatus(404);
			LOGGER.info("The list is null " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, stationNumber));
			return ResponseEntity.status(response.getStatus()).build();
		} else {
			if (listFoyer.get(0).getDecompteAdult().equals("0") && listFoyer.get(0).getDecompteChildren().equals("0")) {
				response.setStatus(404);
				LOGGER.info("The list is empty. No children and no adult " + response.getStatus() + ":"
						+ loggerApi.loggerInfo(request, response, stationNumber));
				return ResponseEntity.status(response.getStatus()).build();
			}
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":"
				+ loggerApi.loggerInfo(request, response, stationNumber));
		return new ResponseEntity<List<Foyer>>(listFoyer, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("childAlert")
	public ResponseEntity<List<ChildAlert>> childAlert(@RequestParam String address, HttpServletRequest request,
			HttpServletResponse response) {

		if (address.isBlank()) {
			response.setStatus(400);
			LOGGER.info("The param does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		List<ChildAlert> listChildren = new ArrayList<>();
		try {
			listChildren = jsonDao.childPersonsAlertAddress(address);
		} catch (IOException e) {
			LOGGER.error(loggerApi.loggerErr(e, address));
		} catch (ParseException e) {
			LOGGER.error(loggerApi.loggerErr(e, address));
		}

		if (listChildren == null) {
			response.setStatus(404);
			LOGGER.info("The list is null " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, address));
			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":" + loggerApi.loggerInfo(request, response, address));
		return new ResponseEntity<List<ChildAlert>>(listChildren, HttpStatus.valueOf(response.getStatus()));

	}

	@GetMapping("phoneAlert")
	public ResponseEntity<List<PhoneAlert>> phoneAlertStationNumber(@RequestParam String firestation,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (firestation.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The param does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		List<PhoneAlert> listPhoneAlert = new ArrayList<>();
		listPhoneAlert = jsonDao.phoneAlertFirestation(firestation);

		if (listPhoneAlert.get(0).getListPhones().isEmpty()) {
			response.setStatus(404);

			return ResponseEntity.status(response.getStatus()).build();
		}

		response.setStatus(200);
		LOGGER.info(
				"Response status " + response.getStatus() + ":" + loggerApi.loggerInfo(request, response, firestation));
		return new ResponseEntity<List<PhoneAlert>>(listPhoneAlert, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("fire")
	public ResponseEntity<List<FireAddress>> fireAddress(@RequestParam String address, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {

		if (address.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The param does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		List<FireAddress> listFireAddress = new ArrayList<>();
		listFireAddress = jsonDao.fireAddress(address);

		if (listFireAddress.isEmpty()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":" + loggerApi.loggerInfo(request, response, address));
		return new ResponseEntity<List<FireAddress>>(listFireAddress, HttpStatus.valueOf(response.getStatus()));

	}

	@GetMapping("flood/station")
	public ResponseEntity<List<PersonsFireStation>> fireAddressListFireStation(@RequestParam List<String> station,
			HttpServletRequest request, HttpServletResponse response) {

		if (station.isEmpty()) {
			response.setStatus(400);
			LOGGER.error("The param does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		List<PersonsFireStation> listPersonsFireStation = new ArrayList<>();
		try {
			listPersonsFireStation = jsonDao.stationListFirestation(station);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, station.toString()));
			return ResponseEntity.status(response.getStatus()).build();
		} catch (ParseException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, station.toString()));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (listPersonsFireStation.isEmpty()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":"
				+ loggerApi.loggerInfo(request, response, station.toString()));
		return new ResponseEntity<List<PersonsFireStation>>(listPersonsFireStation,
				HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("personInfo")
	public ResponseEntity<List<PersonInfo>> personInfo(@RequestParam String firstName, @RequestParam String lastName,
			HttpServletRequest request, HttpServletResponse response) {

		if (firstName.isBlank() || lastName.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The param does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		List<PersonInfo> listPeronInfo = new ArrayList<>();
		try {
			listPeronInfo = jsonDao.personInfo(firstName, lastName);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, firstName + " " + lastName));
			return ResponseEntity.status(response.getStatus()).build();
		} catch (ParseException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, firstName + " " + lastName));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (listPeronInfo.isEmpty()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":"
				+ loggerApi.loggerInfo(request, response, firstName + " " + lastName));
		return new ResponseEntity<List<PersonInfo>>(listPeronInfo, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("communityEmail")
	public ResponseEntity<List<CommunityEmail>> communityEmail(@RequestParam String city, HttpServletRequest request,
			HttpServletResponse response) {

		if (city.isBlank()) {
			response.setStatus(400);
			LOGGER.error("The param does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, ""));
			return ResponseEntity.status(response.getStatus()).build();
		}

		List<CommunityEmail> listCommunityEmail = new ArrayList<>();
		try {
			listCommunityEmail = jsonDao.communityEmail(city);
		} catch (IOException e) {
			response.setStatus(404);
			LOGGER.error(loggerApi.loggerErr(e, city));
			return ResponseEntity.status(response.getStatus()).build();
		}

		if (listCommunityEmail.get(0).getListEmails().isEmpty()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		response.setStatus(200);
		LOGGER.info("Response status " + response.getStatus() + ":" + loggerApi.loggerInfo(request, response, city));
		return new ResponseEntity<List<CommunityEmail>>(listCommunityEmail, HttpStatus.valueOf(response.getStatus()));
	}

	protected InterfaceFilterJsons createFilterJsons() {
		return new FilterJsons();
	}

	// tests au début pour voir comment je récupère des jsons du fichier

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
		return listP;
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

	// Address
	@GetMapping(value = "Persons/{address}")
	public List<?> listPersonsOfAddress(@PathVariable String address) throws IOException {
		listPersons = jsonDao.filterAddressInPersons(address);
		return listPersons;
	}

	// Find all children
	@GetMapping(value = "Children")
	public List<Children> afficherChildren(@RequestParam int old) throws IOException, ParseException {
		List<Children> listM = new ArrayList<>();
		listM = jsonDao.findOld(old);
		return listM;
	}

}
