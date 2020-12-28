package com.openclassrooms.safetynetalerts.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Medicalrecords;
import com.openclassrooms.safetynetalerts.model.Medications;
import com.openclassrooms.safetynetalerts.model.Persons;

public class ReadJsonFile {

	List<Persons> listPersons = new ArrayList<>();
	List<Firestations> listFirestations = new ArrayList<>();
	List<Medicalrecords> listMedicalRecords = new ArrayList<>();
	List<Medications> listMedications = new ArrayList<>();

	public String readfilejson() throws IOException {

		String filepath_json = "data/dbJSON.json";
		String line = "";
		String jsonStringJson = "";
		String jsonStringPersons = "";
		String jsonStringFirestations = "";
		String jsonStringMedicalrecords = "";
		String typeCollection = "";
		Persons persons = new Persons();
		Firestations firestations = new Firestations();
		Medicalrecords medicalrecords = new Medicalrecords();

		JsonIterator iterator;
/*
 *   	
    	
    	String filePath = "src/main/resources/data.json";
    	byte[] bytesFile = Files.readAllBytes(new File(filePath).toPath());
        
    	JsonIterator iter = JsonIterator.parse(bytesFile);
    	Any any = iter.readAny();
    	Any personAny = any.get("persons");


 */
		if (filepath_json != null) {
			BufferedReader reader = new BufferedReader(new FileReader(filepath_json));
			line = reader.readLine();
			while (line != null) {
				jsonStringJson += line.trim();
				if (line.trim().contains("persons")) {
					typeCollection = "persons";
				}
				if (line.trim().contains("firestations")) {
					typeCollection = "firestations";
				}
				if (line.trim().contains("medicalrecords")) {
					typeCollection = "medicalrecords";
				}

				if (!line.trim().equals("{") && !line.trim().contains("persons")
						&& !line.trim().contains("firestations") && !line.trim().equals("],")
						&& !line.trim().contains("medicalrecords") && !line.trim().equals("]")) {
					line = line.trim().replace("},", "}");
					if (typeCollection.equals("persons")) {
						persons = JsonIterator.deserialize(line, Persons.class);
						listPersons.add(persons);
					}
					if (typeCollection.equals("firestations")) {
						firestations = JsonIterator.deserialize(line, Firestations.class);
						listFirestations.add(firestations);
					}
					if (typeCollection.equals("medicalrecords")) {
						// medicalrecords = JsonIterator.deserialize(line, Medicalrecords.class);
						// listMedicalRecords.add(medicalrecords);
					}
				}
				line = reader.readLine();
			}
			reader.close();
		}

		System.out.println("Liste personnes : " + listPersons);
		System.out.println("Liste fire station : " + listFirestations);
		System.out.println("Liste dossiers médicaux : " + listMedicalRecords);
		System.out.println("Liste dossiers médications : " + listMedications);
		System.out.println("Liste jsons file : " + jsonStringJson);

		return jsonStringPersons;
	}

	public int calc(JsonIterator iter) throws IOException {
		int totalTagsCount = 0;
		for (String field = iter.readObject(); field != null; field = iter.readObject()) {
			switch (field) {
			case "medicalrecords":
				while (iter.readArray()) {
					for (String field2 = iter.readObject(); field2 != null; field2 = iter.readObject()) {
						switch (field2) {
						case "tags":
							while (iter.readArray()) {
								iter.skip();
								totalTagsCount++;
							}
							break;
						default:
							iter.skip();
						}
					}
				}
				break;
			default:
				iter.skip();
			}
		}
		return totalTagsCount;
	}

	private void iteratorJsonString(String jsonString, String typeCollection) throws IOException {
		Persons personsIte = new Persons();
		Firestations firestationsIte = new Firestations();
		Medicalrecords medicalrecordsIte = new Medicalrecords();
		Medications medicationsIte = new Medications();
		JsonIterator iterator = JsonIterator.parse(jsonString);

		for (String field = iterator.readObject(); field != null; field = iterator.readObject()) {
			switch (field) {
			case "persons":

				for (String field_persons = iterator
						.readObject(); field_persons != "firestations"; field_persons = iterator.readObject()) {

				}
			case "lastName":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					personsIte.setLastName(iterator.readString());
				}
				continue;
			case "address":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					personsIte.setAddress(iterator.readString());
				}
				continue;
			case "city":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					personsIte.setCity(iterator.readString());
				}
				continue;
			case "zip":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					personsIte.setZip(iterator.readString());
				}
				continue;
			case "phone":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					personsIte.setPhone(iterator.readString());
				}
				continue;
			case "email":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					personsIte.setEmail(iterator.readString());
				}
				continue;

			case "firestations":
				switch (field) {
				case "address":
					if (iterator.whatIsNext() == ValueType.STRING) { //
						firestationsIte.setAddress(iterator.readString());
					}
					continue;
				case "station":
					if (iterator.whatIsNext() == ValueType.STRING) { //
						firestationsIte.setStation(iterator.readString());
					}
					continue;
				}
			default:
				iterator.skip();
			}
		}

		// if (typeCollection.equals("persons")) {
		listPersons.add(personsIte);
		// }
		// if (typeCollection.equals("firestations")) {
		listFirestations.add(firestationsIte);
		// }
		// if (typeCollection.equals("medicalrecords")) {
		listMedicalRecords.add(medicalrecordsIte);
		// }
	}

	private void iteratorJson(String jsonString, String typeCollection) throws IOException {
		Persons personsIte = new Persons();
		Firestations firestationsIte = new Firestations();
		Medicalrecords medicalrecordsIte = new Medicalrecords();
		Medications medicationsIte = new Medications();
		JsonIterator iterator = JsonIterator.parse(jsonString);
		for (String field = iterator.readObject(); field != null; field = iterator.readObject()) {
			switch (field) {
			case "firstName":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					if (typeCollection.equals("persons")) {
						personsIte.setFirstName(iterator.readString());
					}
					if (typeCollection.equals("medicalrecords")) {
						medicalrecordsIte.setFirstName(iterator.readString());
					}
				}
				continue;
			case "lastName":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					if (typeCollection.equals("persons")) {
						personsIte.setLastName(iterator.readString());
					}
					if (typeCollection.equals("medicalrecords")) {
						medicalrecordsIte.setLastName(iterator.readString());
					}
				}
				continue;
			case "address":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					personsIte.setAddress(iterator.readString());
				}
				continue;
			case "city":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					personsIte.setCity(iterator.readString());
				}
				continue;
			case "zip":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					personsIte.setZip(iterator.readString());
				}
				continue;
			case "phone":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					personsIte.setPhone(iterator.readString());
				}
				continue;
			case "email":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					personsIte.setEmail(iterator.readString());
				}
				continue;
			case "birthdate":
				if (iterator.whatIsNext() == ValueType.STRING) { //
					if (typeCollection.equals("medicalrecords")) {
						medicalrecordsIte.setBirthdate(iterator.readString());
					}
				}
				continue;
			case "medications":
				for (String field_medication = iterator
						.readObject(); field_medication != null; field_medication = iterator.readObject()) {
					if (iterator.whatIsNext() == ValueType.STRING) { //
						if (typeCollection.equals("medicalrecords")) {
							// medicalrecordsIte.setMedication(iterator.readString());
						}
					}
				}
				continue;

			default:
				iterator.skip();
			}
		}
		if (typeCollection.equals("persons")) {
			listPersons.add(personsIte);
		}
		if (typeCollection.equals("firestations")) {
			listFirestations.add(firestationsIte);
		}
		if (typeCollection.equals("medicalrecords")) {
			listMedicalRecords.add(medicalrecordsIte);
		}
	}
}
