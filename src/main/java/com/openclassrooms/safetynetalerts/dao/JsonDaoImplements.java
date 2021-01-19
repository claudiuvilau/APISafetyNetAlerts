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
import com.openclassrooms.safetynetalerts.model.ChildAlert;
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
	public List<Foyer> personsOfStationAdultsAndChild(String stationNumber) throws IOException, ParseException {

		int child_old = 18; // 18 age for the children. If the age is different you can modify the
							// listFindOld() too
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

		// We check the birth date in the list medical records
		List<Medicalrecords> listM = new ArrayList<>();
		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		Medicalrecords personsMedicalrecords = new Medicalrecords();
		List<Children> listChildren = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listM = readJsonFile.readfilejsonMedicalrecords();
		String namePersons;
		String nameMedicalrecords;
		for (Persons element_list_persons : listP) {
			namePersons = element_list_persons.getFirstName() + element_list_persons.getLastName();
			for (Medicalrecords element_list_medicalrecords : listM) {
				nameMedicalrecords = element_list_medicalrecords.getFirstName()
						+ element_list_medicalrecords.getLastName();
				if (namePersons.equals(nameMedicalrecords)) {
					personsMedicalrecords = new Medicalrecords();
					listMedicalrecords = new ArrayList<>();
					personsMedicalrecords.setFirstName(element_list_medicalrecords.getFirstName());
					personsMedicalrecords.setLastName(element_list_medicalrecords.getLastName());
					personsMedicalrecords.setBirthdate(element_list_medicalrecords.getBirthdate());
					listMedicalrecords.add(personsMedicalrecords);
					listChildren.addAll(listFindOld(listMedicalrecords, child_old));
				}
			}
		}

		Foyer foyer = new Foyer();
		List<Foyer> listFoyer = new ArrayList<>();
		List<Persons> listPersonsAdults = new ArrayList<>();
		List<Persons> listPersonsChildren = new ArrayList<>();
		String nameChildren;
		int find_child = 0;
		for (Persons element_list_persons : listP) {
			namePersons = element_list_persons.getFirstName() + element_list_persons.getLastName();
			for (Children element_list_children : listChildren) {
				nameChildren = element_list_children.getFirstName() + element_list_children.getLastName();
				if (namePersons.equals(nameChildren)) {
					find_child = 1;
				}
			}
			if (find_child == 0) {
				listPersonsAdults.add(element_list_persons);
				foyer.setListPersonsAdults(listPersonsAdults);
			} else {
				listPersonsChildren.add(element_list_persons);
				foyer.setListPersonsChildren(listPersonsChildren);
				find_child = 0;
			}
		}
		foyer.setListPersonsAdults(listPersonsAdults);
		foyer.setListPersonsChildren(listPersonsChildren);
		foyer.setDecompteAdult(Integer.toString(listPersonsAdults.size()));
		foyer.setDecompteChildren(Integer.toString(listPersonsChildren.size()));
		listFoyer.add(foyer);
		return listFoyer;
	}

	@Override
	public List<Persons> findAddressInPersons(String jsonStream, String address) throws IOException {

		Persons persons = new Persons();
		List<Persons> listPersons = new ArrayList<>();

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
	public List<ChildAlert> childPersonsAlertAddress(String address) throws IOException, ParseException {

		int child_old = 18; // <= 18
		int adult_old = 19; // >= 19
		List<Persons> listPersons = new ArrayList<>();
		List<Children> listChildrenAlert = new ArrayList<>();
		Children persons_child = new Children(); // he is a object with field : old
		List<Children> listChildren = new ArrayList<>();
		List<Children> listPersonsAdult = new ArrayList<>();
		List<Children> listAdultAlert = new ArrayList<>();
		List<ChildAlert> listChildAlert = new ArrayList<>();

		listChildren = findOld(child_old); // the list of children ...old
		String jsonStreamChild = JsonStream.serialize(listChildren); // here we transform the list in json object

		listPersons = filterAddressInPersons(address); // the list of the persons at the same address
		// listPersonsAdult = listFindChildOld(listPersons, child_old);
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

					listChildrenAlert.add(persons_child);
				}
				findChild = 0;
			}
		}

		if (!listChildrenAlert.isEmpty()) {
			findChild = 0;
			listPersonsAdult.addAll(listFindOld(listPersons, adult_old));
			for (Persons element_persons_list : listPersons) {
				for (Children element_child_list : listChildrenAlert) {
					if ((element_persons_list.getFirstName() + element_persons_list.getLastName())
							.equals(element_child_list.getFirstName() + element_child_list.getLastName())) {
						findChild = 1; // if child in the list so we dont't add the persons because the person is a
										// child
					}
				}
				if (findChild == 0) { // 0 = no person child
					listPersonsAdult.add(new Children("", element_persons_list.getFirstName(),
							element_persons_list.getLastName(), "")); // here we create the list of persons adults

				}
				findChild = 0;
			}

			// age of the adults
			List<Medicalrecords> listMedicalrecords = new ArrayList<>();
			List<Medicalrecords> listM;
			listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();
			Medicalrecords medicalrecords;
			String name;
			String name_medicalrecords;
			for (Children element_listPersonsAdult : listPersonsAdult) {
				name = element_listPersonsAdult.getFirstName() + element_listPersonsAdult.getLastName();
				for (Medicalrecords element_listMedicalrecords : listMedicalrecords) {
					name_medicalrecords = element_listMedicalrecords.getFirstName()
							+ element_listMedicalrecords.getLastName();
					if (name.equals(name_medicalrecords)) {
						medicalrecords = new Medicalrecords();
						listM = new ArrayList<>();
						medicalrecords.setFirstName(element_listMedicalrecords.getFirstName());
						medicalrecords.setLastName(element_listMedicalrecords.getLastName());
						medicalrecords.setBirthdate(element_listMedicalrecords.getBirthdate());
						listM.add(medicalrecords);
						listAdultAlert.addAll(listFindOld(listM, adult_old));
					}
				}
			}

			// create the decompte of the list

			int decompteList = listChildrenAlert.size();
			for (Children element_decompte : listChildrenAlert) {

				element_decompte.setDecompte(Integer.toString(decompteList));
				decompteList -= 1;
			}
			decompteList = listAdultAlert.size();
			for (Children element_decompte : listAdultAlert) {
				element_decompte.setDecompte(Integer.toString(decompteList));
				decompteList -= 1;
			}
			ChildAlert childAlert = new ChildAlert();
			childAlert.setListChildren(listChildrenAlert);
			childAlert.setListAdult(listAdultAlert);
			listChildAlert.add(childAlert);
		}
		// return listChildrenAlert;
		return listChildAlert;
	}

	@Override
	public List<Children> findOld(int old) throws IOException, ParseException {

		List<Children> listChild = new ArrayList<>();
		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();
		listChild = listFindOld(listMedicalrecords, old);

		return listChild;
	}

	@Override
	public List<Children> listFindOld(List<?> list, int old) throws IOException, ParseException {

		String person;
		if (old <= 18) { // change here for the age of the children
			person = "child";
		} else
			person = "adult";

		List<Children> listChild = new ArrayList<>();
		List<Children> listAdult = new ArrayList<>();
		Children children = new Children();

		String jsonstream = JsonStream.serialize(list); // here we transform the list in json object

		JsonIterator iter = JsonIterator.parse(jsonstream);
		Any any = null;
		any = iter.readAny();
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
						if (person.equals("child")) {
							if (periode.getYears() <= old) { // for the children
								children = JsonIterator.deserialize(element.toString(), Children.class);
								children.setOld(Integer.toString(periode.getYears()));
								listChild.add(children);
							}
						}
						if (person.equals("adult")) {
							if (periode.getYears() >= old) { // for the adults. use = too
								children = JsonIterator.deserialize(element.toString(), Children.class);
								children.setOld(Integer.toString(periode.getYears()));
								listAdult.add(children);
							}
						}
					}
					continue;
				default:
					iterator.skip();
				}
			}
		}

		// create decompte
		int decompte;
		if (person.equals("child")) {
			decompte = listChild.size();
			for (Children element_decompte : listChild) {
				element_decompte.setDecompte(Integer.toString(decompte));
				decompte -= 1;
			}
			return listChild;
		} else {
			decompte = listAdult.size();
			for (Children element_decompte : listAdult) {
				element_decompte.setDecompte(Integer.toString(decompte));
				decompte -= 1;
			}
			return listAdult;
		}
	}

	@Override
	public List<Persons> phoneAlertFirestation(String stationNumber) throws IOException {

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
}
