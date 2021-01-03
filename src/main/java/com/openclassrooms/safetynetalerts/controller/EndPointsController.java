package com.openclassrooms.safetynetalerts.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.safetynetalerts.dao.JsonDao;
import com.openclassrooms.safetynetalerts.dao.ReadJsonFile;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Medicalrecords;
import com.openclassrooms.safetynetalerts.model.Persons;

@RestController
public class EndPointsController {

	@Autowired
	private JsonDao jsonDao;
	private ReadJsonFile readJsonFile;

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

		// String jsonStringFromObject = JsonStream.serialize(listP);
		// System.out.println("JSON String from Object: " + jsonStringFromObject);
		// System.out.println("String from list: " + listP);

		// List<Map<String, String>> data = new ArrayList<>();
		// Map<String, String> item1 = new HashMap<>();
		// item1.put("name", "Sample JSON Serialization");
		// item1.put("url", "https://simplesolution.dev");
		// data.add(item1);

		// Map<String, String> item2 = new HashMap<>();
		// item2.put("name", "Java Tutorials");
		// item2.put("url", "https://simplesolution.dev/java");
		// data.add(item2);

		// String jsonStringFromObject = JsonStream.serialize(data);
		// System.out.println("JSON String from Object: " + jsonStringFromObject);

		// return jsonStringFromObject;
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

	// Fire stations N°
	@GetMapping(value = "firestations/{station}")
	public List<?> afficherFirestationsNumber(@PathVariable String station) {
		List<?> listF = new ArrayList<>();
		listF = jsonDao.createPersonsCaserne(station);
		return listF;
	}

	// Persons2 pour tester l'affichage
	@GetMapping(value = "Persons2")
	public Persons afficherPersonnes2() {
		Persons persons = new Persons("Vilau", "Claudiu", null, null, null, null, null);
		return persons;
	}

}
