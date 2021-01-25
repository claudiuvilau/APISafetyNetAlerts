package com.openclassrooms.safetynetalerts.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
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
	private List<Foyer> listFoyer = new ArrayList<>();

	// Persons
	@GetMapping(value = "Persons")
	public List<Persons> afficherPersonnes() {
		// String js = "";
		List<Persons> listP = new ArrayList<>();
		try {
			readJsonFile = new ReadJsonFile();
			listP = readJsonFile.readfilejsonPersons();
			// js = JsonStream.serialize(listP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listP;
	}

	// ajouter une nouvelle personne
	@PostMapping(value = "/person")
	public void ajouterPerson(@RequestBody Persons persons) throws IOException {
		jsonDao.ajouterPerson(persons);
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
	public List<Foyer> firestationStationNumber(@RequestParam String stationNumber) throws IOException, ParseException {
		listFoyer = jsonDao.personsOfStationAdultsAndChild(stationNumber);
		return listFoyer;
	}

	@GetMapping("childAlert")
	public List<ChildAlert> childAlert(@RequestParam String address) throws IOException, ParseException {
		List<ChildAlert> listM = new ArrayList<>();
		listM = jsonDao.childPersonsAlertAddress(address);
		return listM;
	}

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

	@GetMapping("fire")
	public List<FireAddress> fireAddress(@RequestParam String address) throws IOException, ParseException {
		List<FireAddress> listM = new ArrayList<>();
		listM = jsonDao.fireAddress(address);
		return listM;
	}

	@GetMapping("flood/station")
	public List<PersonsFireStation> fireAddressListFireStation(@RequestParam List<String> station)
			throws IOException, ParseException {
		List<PersonsFireStation> listM = new ArrayList<>();
		listM = jsonDao.stationListFirestation(station);
		return listM;
	}

	@GetMapping("personInfo")
	public List<PersonInfo> personInfo(@RequestParam String firstName, String lastName)
			throws IOException, ParseException {
		List<PersonInfo> listM = new ArrayList<>();
		listM = jsonDao.personInfo(firstName, lastName);
		return listM;
	}

	@GetMapping("communityEmail")
	public List<CommunityEmail> communityEmail(@RequestParam String city) throws IOException {
		List<CommunityEmail> listM = new ArrayList<>();
		listM = jsonDao.communityEmail(city);
		return listM;
	}

}
