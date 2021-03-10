package com.openclassrooms.safetynetalerts.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.openclassrooms.safetynetalerts.model.Allergies;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Medicalrecords;
import com.openclassrooms.safetynetalerts.model.Medications;
import com.openclassrooms.safetynetalerts.model.Persons;

public class ReadJsonFile {

	public String filepath_json = "data/dbJSON.json";

	public List<Persons> readfilejsonPersons() {

		List<Persons> listPersons = new ArrayList<>();
		Persons persons = new Persons();

		if (filepath_json != null) {
			byte[] bytesFile = null;
			try {
				bytesFile = Files.readAllBytes(new File(filepath_json).toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JsonIterator iter = JsonIterator.parse(bytesFile);
			Any any = null;
			try {
				any = iter.readAny();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Any personsAny = any.get("persons");

			for (Any element : personsAny) {
				persons = JsonIterator.deserialize(element.toString(), Persons.class);
				listPersons.add(persons);
			}
		}
		return listPersons;
	}

	public List<Firestations> readfilejsonFirestations() {

		List<Firestations> listFirestations = new ArrayList<>();
		Firestations firestations = new Firestations();

		if (filepath_json != null) {
			byte[] bytesFile = null;
			try {
				bytesFile = Files.readAllBytes(new File(filepath_json).toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JsonIterator iter = JsonIterator.parse(bytesFile);

			Any any = null;
			try {
				any = iter.readAny();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Any firestationsAny = any.get("firestations");

			for (Any element : firestationsAny) {
				firestations = JsonIterator.deserialize(element.toString(), Firestations.class);
				listFirestations.add(firestations);
			}
		}
		return listFirestations;
	}

	public List<Medicalrecords> readfilejsonMedicalrecords() {

		List<Medicalrecords> listMedicalrecords = new ArrayList<>();

		if (filepath_json != null) {
			byte[] bytesFile = null;
			try {
				bytesFile = Files.readAllBytes(new File(filepath_json).toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JsonIterator iter = JsonIterator.parse(bytesFile);
			Any any = null;
			try {
				any = iter.readAny();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Any medicalrecordsAny = any.get("medicalrecords");

			JsonIterator iter2;
			for (Any element : medicalrecordsAny) {
				iter2 = JsonIterator.parse(element.toString());
				Any any2 = null;
				try {
					any2 = iter2.readAny();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Any first_name = any2.get("firstName");
				Any last_name = any2.get("lastName");
				Any birthdate = any2.get("birthdate");

				// the lists of medications and allergies :
				Any medicationsAny = any2.get("medications");
				List<Medications> listMedications = new ArrayList<>(); // here we create a new list Medications for any
																		// element
				Medications medications;
				List<String> listStringMedications = new ArrayList<>();
				for (Any elementMedications : medicationsAny) {
					listStringMedications.add(elementMedications.toString());
				}
				medications = new Medications();
				medications.setMedications(listStringMedications);
				listMedications.add(medications);

				Any allergiesAny = any2.get("allergies");
				List<Allergies> listAllergies = new ArrayList<>(); // here we create a new list Allergies for any
																	// element
				Allergies allergies;
				List<String> listStringAllergies = new ArrayList<>();
				for (Any elementAllergies : allergiesAny) {
					listStringAllergies.add(elementAllergies.toString());
				}
				allergies = new Allergies();
				allergies.setAllergies(listStringAllergies);
				listAllergies.add(allergies);

				Medicalrecords medicalrecords2 = new Medicalrecords();
				medicalrecords2.setFirstName(first_name.toString());
				medicalrecords2.setLastName(last_name.toString());
				medicalrecords2.setBirthdate(birthdate.toString());
				medicalrecords2.setMedications(listStringMedications);
				medicalrecords2.setAllergies(listStringAllergies);
				listMedicalrecords.add(medicalrecords2);
			}
		}
		return listMedicalrecords;
	}
}
