package com.openclassrooms.safetynetalerts.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Persons;

@Repository
public class JsonDaoImplements implements JsonDao {

	private ReadJsonFile readJsonFile;

	@Override
	public List<Firestations> filterStation(String caserne) {

		List<Firestations> listF = new ArrayList<>();
		List<Firestations> listFirestations = new ArrayList<>();
		Firestations firestations = new Firestations();
		readJsonFile = new ReadJsonFile();
		try {
			listF = readJsonFile.readfilejsonFirestations(); // here we have a list of objects Fire Stations from json
																// file

			String jsonstream = JsonStream.serialize(listF); // here we transform the list in json object

			// We will read the json object and if we have a station == n° of caserne we
			// will make another list

			JsonIterator iter = JsonIterator.parse(jsonstream);
			Any any = iter.readAny();

			JsonIterator iterator;
			for (Any element : any) {
				iterator = JsonIterator.parse(element.toString());
				for (String field = iterator.readObject(); field != null; field = iterator.readObject()) {
					switch (field) {
					case "station":
						if (iterator.whatIsNext() == ValueType.STRING) {
							if (iterator.readString().equals(caserne)) {
								firestations = JsonIterator.deserialize(element.toString(), Firestations.class);
								listFirestations.add(firestations);
							}
						}
						continue;
					default:
						iterator.skip();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listFirestations;
	}

	@Override
	public List<Persons> filterAddressInPersons(String address) {

		List<Persons> listP = new ArrayList<>();
		List<Persons> listPersons = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		try {
			listP = readJsonFile.readfilejsonPersons(); // here we have a list of objects Persons from json
			// file
		} catch (IOException e) {
			e.printStackTrace();
		}

		String jsonstream = JsonStream.serialize(listP); // here we transform the list in json object

		// We will read the json object and if we have an address == address we
		// will make another list
		try {
			listPersons = findAddressInPersons(jsonstream, address);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listPersons;
	}

	@Override
	public List<Persons> personsOfStationAdultsAndChild(String stationNumber) throws IOException {
		List<Firestations> listFirestations = new ArrayList<>();
		List<Persons> listPersons = new ArrayList<>();
		List<Persons> listP = new ArrayList<>();
		listFirestations = filterStation(stationNumber);

		String jsonstream = JsonStream.serialize(listFirestations); // here we transform the list in json object

		String address = "";

		JsonIterator iter = JsonIterator.parse(jsonstream);
		Any any = null;
		try {
			any = iter.readAny();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JsonIterator iterator;
		for (Any element : any) {
			iterator = JsonIterator.parse(element.toString());
			for (String field = iterator.readObject(); field != null; field = iterator.readObject()) {
				switch (field) {
				case "address":
					if (iterator.whatIsNext() == ValueType.STRING) {
						address = iterator.readString();
						listPersons = filterAddressInPersons(address); // it will check the address in the Persons
						listP.addAll(listPersons); // it will make the list of the persons = address
					}
					continue;
				default:
					iterator.skip();
				}
			}
		}
		return listP;
	}

	@Override
	public List<Persons> findAddressInPersons(String jsonStream, String address) throws IOException {

		Persons persons = new Persons();
		List<Persons> listPersons = new ArrayList<>();

		JsonIterator iter = JsonIterator.parse(jsonStream);
		Any any = null;
		try {
			any = iter.readAny();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JsonIterator iterator;
		for (Any element : any) {
			iterator = JsonIterator.parse(element.toString());
			for (String field = iterator.readObject(); field != null; field = iterator.readObject()) {
				switch (field) {
				case "address":
					if (iterator.whatIsNext() == ValueType.STRING) {
						if (iterator.readString().equals(address)) {
							persons = JsonIterator.deserialize(element.toString(), Persons.class);
							listPersons.add(persons);
						}
					}
					continue;
				default:
					iterator.skip();
				}
			}
		}
		return listPersons;
	}

	@Override
	public List<Firestations> findAddressInFirestations(String jsonStream, String address) throws IOException {

		List<Firestations> listFirestations = new ArrayList<>();
		Firestations firestations = new Firestations();
		JsonIterator iter = JsonIterator.parse(jsonStream);
		Any any = null;
		try {
			any = iter.readAny();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JsonIterator iterator;
		for (Any element : any) {
			iterator = JsonIterator.parse(element.toString());
			for (String field = iterator.readObject(); field != null; field = iterator.readObject()) {
				switch (field) {
				case "address":
					if (iterator.whatIsNext() == ValueType.STRING) {
						if (iterator.readString().equals(address)) {
							firestations = JsonIterator.deserialize(element.toString(), Firestations.class);
							listFirestations.add(firestations);
						}
					}
					continue;
				default:
					iterator.skip();
				}
			}
		}
		return listFirestations;
	}

}
