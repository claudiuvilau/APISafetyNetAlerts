package com.openclassrooms.safetynetalerts.dao;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.openclassrooms.safetynetalerts.model.Children;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Foyer;
import com.openclassrooms.safetynetalerts.model.Medicalrecords;
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
		any = iter.readAny();

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
		any = iter.readAny();
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

	@Override
	public List<Foyer> childPersonsAlertAddress(String address) throws IOException, ParseException {

		int child_old = 18;
		List<Persons> listPersons = new ArrayList<>();
		List<Children> listChildrenAlert = new ArrayList<>();
		Children persons_child = new Children(); // he is a object with field : old
		List<Children> listChildren = new ArrayList<>();
		List<Children> listPersonsAdult = new ArrayList<>();

		listChildren = findChild(child_old); // the list of children ...old
		String jsonStreamChild = JsonStream.serialize(listChildren); // here we transform the list in json object

		listPersons = filterAddressInPersons(address); // the list of the persons at the same address
		String jsonStreamPersons = JsonStream.serialize(listPersons); // here we transform the list in json object

		JsonIterator iterChild = JsonIterator.parse(jsonStreamChild);
		Any anyChild = iterChild.readAny();
		JsonIterator iteratorChild;
		String first_name = "";
		String last_name = "";

		JsonIterator iterPersons = JsonIterator.parse(jsonStreamPersons);
		Any anyPersons = null;
		anyPersons = iterPersons.readAny();
		JsonIterator iteratorPersons;

		int findChild = 0;
		int decompte = 0;
		for (Any elementChild : anyChild) {
			iteratorChild = JsonIterator.parse(elementChild.toString());
			for (String fieldChild = iteratorChild.readObject(); fieldChild != null; fieldChild = iteratorChild
					.readObject()) {
				switch (fieldChild) {
				case "firstName":
					if (iteratorChild.whatIsNext() == ValueType.STRING) {
						first_name = iteratorChild.readString();
					}
					continue;
				case "lastName":
					if (iteratorChild.whatIsNext() == ValueType.STRING) {
						last_name = iteratorChild.readString();
					}
					continue;
				default:
					iteratorChild.skip();
				}
			}
			// verify if the child is in the list persons
			for (Any elementPersons : anyPersons) {
				iteratorPersons = JsonIterator.parse(elementPersons.toString());
				for (String fieldPersons = iteratorPersons
						.readObject(); fieldPersons != null; fieldPersons = iteratorPersons.readObject()) {
					switch (fieldPersons) {
					case "firstName":
						if (iteratorPersons.whatIsNext() == ValueType.STRING) {
							if (iteratorPersons.readString().equals(first_name)) {
								findChild += 1;
							}
						}
						continue;
					case "lastName":
						if (iteratorPersons.whatIsNext() == ValueType.STRING) {
							if (iteratorPersons.readString().equals(last_name)) {
								findChild += 1;
							}
						}
						continue;
					default:
						iteratorPersons.skip();
					}
				}
				if (findChild == 2) { // if the first name and last name, so 2 => we have a child in the home
					persons_child = JsonIterator.deserialize(elementChild.toString(), Children.class); // add
																										// element
																										// child
					decompte += 1;
					persons_child.setDecompte(decompte + ""); // => to string
					listChildrenAlert.add(persons_child);
				}
				findChild = 0;
			}
		}

		findChild = 0;
		decompte = 0;
		if (!listChildrenAlert.isEmpty()) {
			for (Persons element_persons_list : listPersons) {
				for (Children element_child_list : listChildrenAlert) {
					if ((element_persons_list.getFirstName() + element_persons_list.getLastName())
							.equals(element_child_list.getFirstName() + element_child_list.getLastName())) {
						findChild = 1; // if child in the list so we dont't add the persons because the person is a
										// child
					}
				}
				if (findChild == 0) { // 0 = no person child
					decompte += 1;
					listPersonsAdult.add(new Children(decompte + "", element_persons_list.getFirstName(),
							element_persons_list.getLastName(), "adult")); // here we create the list of persons adults;
																			// + "" => to string
				}
				findChild = 0;
			}
		}

		Foyer foyer = new Foyer();
		List<Foyer> listFoyer = new ArrayList<>();
		foyer.setListAdults(listPersonsAdult);
		foyer.setListChildren(listChildrenAlert);
		listFoyer.add(foyer);

		// return listChildren_Alert;
		return listFoyer;
	}

	@Override
	public List<Children> findChild(int old) throws IOException, ParseException {

		List<Children> listChild = new ArrayList<>();
		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		Children children = new Children();
		readJsonFile = new ReadJsonFile();

		listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

		String jsonstream = JsonStream.serialize(listMedicalrecords); // here we transform the list in json object

		JsonIterator iter = JsonIterator.parse(jsonstream);
		Any any = null;
		try {
			any = iter.readAny();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonIterator iterator;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = new GregorianCalendar();
		LocalDate now = LocalDate.now();
		Date date_birthday;
		LocalDate birthdate;
		Period periode;
		for (Any element : any) {
			iterator = JsonIterator.parse(element.toString());
			for (String field = iterator.readObject(); field != null; field = iterator.readObject()) {
				switch (field) {
				case "birthdate":
					if (iterator.whatIsNext() == ValueType.STRING) {
						date_birthday = sdf.parse(iterator.readString());
						calendar.setTime(date_birthday);
						birthdate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
								calendar.get(Calendar.DAY_OF_MONTH));
						periode = Period.between(birthdate, now);
						if (periode.getYears() <= old) {
							children = JsonIterator.deserialize(element.toString(), Children.class);
							children.setOld(periode.getYears() + ""); // => to string
							listChild.add(children);
						}
					}
					continue;
				default:
					iterator.skip();
				}
			}
		}
		return listChild;
	}
}
