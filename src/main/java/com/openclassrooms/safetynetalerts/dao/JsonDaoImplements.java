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

	@Override
	public List<Children> childPersonsAlertAddress(String address) throws IOException, ParseException {

		int child_old = 18;
		List<Persons> listPersons = new ArrayList<>();
		List<Children> listPersons_ChildAlert = new ArrayList<>();
		Children persons_child = new Children();
		List<Children> listChild = new ArrayList<>();
		List<Children> listPersonsWithChild = new ArrayList<>();

		listPersons = filterAddressInPersons(address);
		String jsonstream = JsonStream.serialize(listPersons); // here we transform the list in json object

		JsonIterator iter = JsonIterator.parse(jsonstream);
		Any any = null;
		try {
			any = iter.readAny();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonIterator iterator;
		String first_name = "";
		String last_name = "";
		for (Any element : any) {
			iterator = JsonIterator.parse(element.toString());
			for (String field = iterator.readObject(); field != null; field = iterator.readObject()) {
				switch (field) {
				case "firstName":
					if (iterator.whatIsNext() == ValueType.STRING) {
						first_name = iterator.readString();
					}
					continue;
				case "lastName":
					if (iterator.whatIsNext() == ValueType.STRING) {
						last_name = iterator.readString();
					}
					continue;
				default:
					iterator.skip();
				}
			}

			// here we will check if the first name and last name is in the list of children
			int find_child = 0;
			int no_child = 0;
			listChild = findChild(child_old);
			String jsonstream_child = JsonStream.serialize(listChild); // here we transform the list in json object
			JsonIterator iter_child = JsonIterator.parse(jsonstream_child);
			Any any_child = null;
			try {
				any_child = iter_child.readAny();
			} catch (IOException e_child) {
				e_child.printStackTrace();
			}
			JsonIterator iterator_child;
			for (Any element_child : any_child) {
				iterator_child = JsonIterator.parse(element_child.toString());
				for (String field_child = iterator_child.readObject(); field_child != null; field_child = iterator_child
						.readObject()) {
					switch (field_child) {
					case "firstName":
						if (iterator_child.whatIsNext() == ValueType.STRING) {
							if (first_name.equals(iterator_child.readString())) { // if the fist name of persons with
																					// address is in the list child
								find_child += 1;
							}
						}
						continue;
					case "lastName":
						if (iterator_child.whatIsNext() == ValueType.STRING) {
							if (last_name.equals(iterator_child.readString())) { // if the last name of persons with
																					// address is in the list child
								find_child += 1;
							}
						}
						continue;
					default:
						iterator_child.skip();
					}
				}
				if (find_child == 2) { // if we have the first name and the last name in the list child
					no_child += 1;
					persons_child = JsonIterator.deserialize(element.toString(), Children.class); // add the element
																									// (not
																									// element of child)
					listPersons_ChildAlert.add(persons_child);
				}
				find_child = 0;
				if (no_child == 1) {
					listPersonsWithChild.add(persons_child);
				}
			}
		}
		return listPersons_ChildAlert;
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
		for (Any element : any) {
			iterator = JsonIterator.parse(element.toString());
			for (String field = iterator.readObject(); field != null; field = iterator.readObject()) {
				switch (field) {
				case "birthdate":
					if (iterator.whatIsNext() == ValueType.STRING) {
						Date date_birthday = sdf.parse(iterator.readString());
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(date_birthday);
						LocalDate now = LocalDate.now();
						LocalDate birthdate = LocalDate.of(calendar.get(Calendar.YEAR),
								calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
						Period periode = Period.between(birthdate, now);
						if (periode.getYears() <= old) {
							children = JsonIterator.deserialize(element.toString(), Children.class);
							listChild.add(children);
							// listChild.set(old, children);
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
