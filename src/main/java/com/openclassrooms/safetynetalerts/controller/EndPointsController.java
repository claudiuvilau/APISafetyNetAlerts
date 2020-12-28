package com.openclassrooms.safetynetalerts.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.safetynetalerts.dao.JsonDao;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Medicalrecords;
import com.openclassrooms.safetynetalerts.model.Persons;

@RestController
public class EndPointsController {

	@Autowired
	private JsonDao jsonDao;

	// Persons
	@GetMapping(value = "Persons")
	public JSONArray afficherPersonnes() {
		Persons persons = new Persons();
		return null;
	}

	// Fire stations
	@GetMapping(value = "Firestations")
	public JSONArray afficherFirestations() {
		Firestations firestations = new Firestations();
		return firestations.firestationsJson();
	}

	// Medical records
	@GetMapping(value = "Medicalrecords")
	public JSONArray afficherMedicalrecords() {
		Medicalrecords medicalrecords = new Medicalrecords();
		return medicalrecords.medicalrecordsJson();
	}

	// Fire stations N°
	@GetMapping(value = "firestations/{station}")
	public JSONObject afficherFirestationsNumber(@PathVariable String station) {
		return jsonDao.createPersonsCaserne("firestations", station);
	}

	// Persons2 pour tester l'affichage
	@GetMapping(value = "Persons2")
	public Persons afficherPersonnes2() {
		Persons persons = new Persons("Vilau", "Claudiu", null, null, null, null, null);
		return persons;
	}

}
