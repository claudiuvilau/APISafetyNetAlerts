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
	private ReadJsonFile readJsonFile;
	private List<Firestations> listFirestations = new ArrayList<>();
	private List<Persons> listPersons = new ArrayList<>();
	private LoggerApi loggerApi;

	@Bean
	public HttpTraceRepository htttpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	}

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

	// get a person
	@GetMapping(value = "person/{firstNamelastName}")
	public ResponseEntity<List<Persons>> getAPerson(@PathVariable String firstNamelastName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if (firstNamelastName.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		List<Persons> listP = new ArrayList<>();
		listP = jsonDao.getAPerson(firstNamelastName);

		if (listP.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<List<Persons>>(listP, HttpStatus.valueOf(response.getStatus()));
	}

	// add a person
	@PostMapping(value = "/person")
	public ResponseEntity<Void> addPerson(@RequestBody Persons persons) throws IOException {

		// LOGGER.trace("addPerson");
		// LOGGER.info("Current Request : " +
		// ServletUriComponentsBuilder.fromCurrentRequest().build());

		// if persons == null the end point is bad request because @RequestBody
		if (persons == null) {
			// LOGGER.error("Person in body is null.");
			return ResponseEntity.badRequest().build();
		}

		Persons newPerson = jsonDao.addPerson(persons);

		if (newPerson == null) {
			// LOGGER.error("The person is not added.");
			return ResponseEntity.notFound().build();
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{firstName+lastName}")
				.buildAndExpand(newPerson.getFirstName() + newPerson.getLastName()).toUri();
		LOGGER.info("A new person is added successful. The URL is : " + location);
		return ResponseEntity.created(location).build();
	}

	// update person
	@PutMapping(value = "/person")
	public ResponseEntity<Void> updatePerson(@RequestBody Persons persons, @RequestParam String firstName,
			@RequestParam String lastName) throws IOException {

		if (firstName.isBlank() || lastName.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		boolean update = false;

		update = jsonDao.updatePerson(persons, firstName, lastName);

		if (update == false) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();
	}

	// delete person
	@DeleteMapping(value = "/person")
	public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName)
			throws IOException {

		if (firstName.isBlank() || lastName.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		boolean del = false;

		del = jsonDao.deletePerson(firstName, lastName);

		if (del == false) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();
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
	public ResponseEntity<Void> addFirestations(@RequestBody Firestations firestation) throws IOException {

		if (firestation.equals(null)) {
			return ResponseEntity.badRequest().build();
		}

		Firestations newFirestation = new Firestations();
		newFirestation = jsonDao.addFirestation(firestation);

		if (newFirestation.equals(null)) {
			return ResponseEntity.notFound().build();
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{station}")
				.buildAndExpand(newFirestation.getStation()).toUri();
		LOGGER.info("A new fire station is added successful. The URL is : " + location);
		return ResponseEntity.created(location).build();
	}

	// update fire station
	@PutMapping(value = "/firestation")
	public ResponseEntity<Void> updateFirestations(@RequestBody Firestations firestation, @RequestParam String address)
			throws IOException {

		if (address.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		boolean update_station = jsonDao.updateFirestation(firestation, address);

		if (update_station == false) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();
	}

	// delete fire station
	@DeleteMapping(value = "/firestation")
	public ResponseEntity<Void> deleteFirestation(@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "stationNumber", required = false) String stationNumber) throws IOException {

		if (address.isBlank() && stationNumber.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		boolean del = false;

		del = jsonDao.deleteFirestation(address, stationNumber);

		if (del == false) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();
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
	public ResponseEntity<List<Medicalrecords>> getAMedicalRecord(@PathVariable String firstNamelastName,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (firstNamelastName.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		List<Medicalrecords> listM = new ArrayList<>();
		listM = jsonDao.getAMedicalrecord(firstNamelastName);

		if (listM.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<List<Medicalrecords>>(listM, HttpStatus.valueOf(response.getStatus()));
	}

	// add a medical records
	@PostMapping(value = "/medicalRecord")
	public ResponseEntity<Void> addMedicalRecord(@RequestBody Medicalrecords medicalRecord) throws IOException {

		if (medicalRecord.equals(null)) {
			return ResponseEntity.badRequest().build();
		}

		Medicalrecords newMedicalRecord = jsonDao.addMedicalRecord(medicalRecord);

		if (newMedicalRecord.equals(null)) {
			return ResponseEntity.notFound().build();
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{firstName+lastName}")
				.buildAndExpand(newMedicalRecord.getFirstName() + newMedicalRecord.getLastName()).toUri();
		// LOGGER.info("A new person is added successful. The URL is : " + location);
		return ResponseEntity.created(location).build();

	}

	// update medical records
	@PutMapping(value = "/medicalRecord")
	public ResponseEntity<Void> updateMedicalRecord(@RequestBody Medicalrecords medicalRecord,
			@RequestParam String firstName, @RequestParam String lastName) throws IOException {

		if (firstName.isBlank() || lastName.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		boolean update = false;

		update = jsonDao.updateMedicalRecord(medicalRecord, firstName, lastName);

		if (update == false) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();
	}

	// delete medical records
	@DeleteMapping(value = "/medicalRecord")
	public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName)
			throws IOException {

		if (firstName.isBlank() || lastName.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		boolean del = false;

		del = jsonDao.deleteMedicalRecord(firstName, lastName);

		if (del == false) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();
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
	public ResponseEntity<List<Firestations>> listFirestationsNumber(@PathVariable String station,
			HttpServletRequest request, HttpServletResponse response) {

		if (station.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		InterfaceFilterJsons filterJsons = createFilterJsons();
		listFirestations = filterJsons.filterStation(station);

		if (listFirestations.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return new ResponseEntity<List<Firestations>>(listFirestations, HttpStatus.valueOf(response.getStatus()));
	}

	// Address
	@GetMapping(value = "Persons/{address}")
	public List<?> listPersonsOfAddress(@PathVariable String address) throws IOException {
		listPersons = jsonDao.filterAddressInPersons(address);
		return listPersons;
	}

	@GetMapping("firestation")
	public ResponseEntity<List<Foyer>> firestationStationNumber(@RequestParam String stationNumber,
			HttpServletRequest request, HttpServletResponse response) {

		loggerApi = new LoggerApi();

		if (stationNumber == null || stationNumber.length() == 0) {
			response.setStatus(400);
			LOGGER.info("The param does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, stationNumber));
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

		loggerApi = new LoggerApi();

		if (address == null || address.length() == 0) {
			response.setStatus(400);
			LOGGER.info("The param does not exist " + response.getStatus() + ":"
					+ loggerApi.loggerInfo(request, response, address));
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
	public ResponseEntity<List<PhoneAlert>> phoneAlertStationNumber(@RequestParam String firestation)
			throws IOException {

		if (firestation.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		List<PhoneAlert> listPhoneAlert = new ArrayList<>();
		listPhoneAlert = jsonDao.phoneAlertFirestation(firestation);

		if (listPhoneAlert.get(0).getListPhones().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return new ResponseEntity<List<PhoneAlert>>(listPhoneAlert, HttpStatus.OK);
	}

	@GetMapping("fire")
	public ResponseEntity<List<FireAddress>> fireAddress(@RequestParam String address)
			throws IOException, ParseException {

		if (address == null || address.length() == 0) {
			return ResponseEntity.badRequest().build();
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
			return ResponseEntity.badRequest().build();
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
			return ResponseEntity.badRequest().build();
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
			return ResponseEntity.badRequest().build();
		}

		List<CommunityEmail> listCommunityEmail = new ArrayList<>();
		listCommunityEmail = jsonDao.communityEmail(city);

		if (listCommunityEmail.get(0).getListEmails().isEmpty()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		return new ResponseEntity<List<CommunityEmail>>(listCommunityEmail, HttpStatus.OK);
	}

	protected InterfaceFilterJsons createFilterJsons() {
		return new FilterJsons();
	}

}
