package com.openclassrooms.safetynetalerts.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.safetynetalerts.dao.JsonDao;
import com.openclassrooms.safetynetalerts.dao.ReadJsonFile;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Medicalrecords;
import com.openclassrooms.safetynetalerts.model.Persons;

@RestController
public class EndPointsController {

	@Autowired
	private JsonDao jsonDao;
	private ReadJsonFile readJesonFile;

	// Persons
	@GetMapping(value = "Persons")
	public String afficherPersonnes() {
		String js = "";
		List<Persons> listP = new ArrayList<>();
		try {
			readJesonFile = new ReadJsonFile();
			listP = readJesonFile.readfilejsonPersons();
			js = JsonStream.serialize(listP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return js;
	}

	// Fire stations
	@GetMapping(value = "Firestations")
	public String afficherFirestations() {
		Firestations firestations = new Firestations();
		// return firestations.firestationsJson();
		return firestations.getAddress();
	}

	// Medical records
	@GetMapping(value = "Medicalrecords")
	public String afficherMedicalrecords() {
		Medicalrecords medicalrecords = new Medicalrecords();
		// return medicalrecords.medicalrecordsJson();
		return medicalrecords.getFirstName();
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
