package com.openclassrooms.safetynetalerts.dao;

import java.io.FileWriter;
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

import javax.json.JsonException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.openclassrooms.safetynetalerts.model.AddressListFirestation;
import com.openclassrooms.safetynetalerts.model.ChildAlert;
import com.openclassrooms.safetynetalerts.model.Children;
import com.openclassrooms.safetynetalerts.model.CollectionsRessources;
import com.openclassrooms.safetynetalerts.model.CommunityEmail;
import com.openclassrooms.safetynetalerts.model.FireAddress;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Foyer;
import com.openclassrooms.safetynetalerts.model.Medicalrecords;
import com.openclassrooms.safetynetalerts.model.PersonInfo;
import com.openclassrooms.safetynetalerts.model.Persons;
import com.openclassrooms.safetynetalerts.model.PersonsFireStation;
import com.openclassrooms.safetynetalerts.model.PhoneAlert;
import com.openclassrooms.safetynetalerts.service.LoggerApi;

@Repository
public class JsonDaoImplements implements JsonDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonDaoImplements.class);
	private ReadJsonFile readJsonFile;
	private LoggerApi loggerApi;

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
	public List<Persons> filterAddressInPersons(String address) throws IOException {

		List<Persons> listP = new ArrayList<>();
		List<Persons> listPersons = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listP = readJsonFile.readfilejsonPersons(); // here we have a list of objects Persons from json
		// file
		String jsonstream = JsonStream.serialize(listP); // here we transform the list in json object

		// We will read the json object and if we have an address == address we
		// will make another list
		listPersons = findAddressInPersons(jsonstream, address);
		return listPersons;
	}

	@Override
	public List<Foyer> personsOfStationAdultsAndChild(String stationNumber) {

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
		try {
			any = iter.readAny();
		} catch (JsonException e) {
			System.out.println("JsonException. Essayez de nouveau. " + e);
		} catch (Exception e) {
			loggerApi = new LoggerApi();
			LOGGER.error(loggerApi.loggerErr(e));
			return null;
		}

		JsonIterator iterator;
		for (Any element : any) {
			iterator = JsonIterator.parse(element.toString());
			iterator = null;
			try {
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
			} catch (NullPointerException e) {
				loggerApi = new LoggerApi();
				LOGGER.error("test990 : " + loggerApi.loggerErr(e));
				return null;
			} catch (Exception e) {
				loggerApi = new LoggerApi();
				LOGGER.error("test999 : " + loggerApi.loggerErr(e));
				return null;
			}

		}

		// We check the birth date in the list medical records
		List<Medicalrecords> listM = new ArrayList<>();
		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		Medicalrecords personsMedicalrecords = new Medicalrecords();
		List<Children> listChildren = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		try {
			listM = readJsonFile.readfilejsonMedicalrecords();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
					try {
						listChildren.addAll(listFindOld(listMedicalrecords, child_old));
					} catch (IOException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
	public List<Firestations> findAddressInFirestations(List<?> listFireStations, String address) throws IOException {

		String jsonstream = JsonStream.serialize(listFireStations); // here we transform the list in json object

		List<Firestations> listFirestations = new ArrayList<>();
		Firestations firestations = new Firestations();
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
		Children persons_child = new Children(); // this is a object with field : old
		List<Children> listChildren = new ArrayList<>();
		List<Children> listPersonsAdult = new ArrayList<>();
		List<Children> listAdultAlert = new ArrayList<>();
		List<ChildAlert> listChildAlert = new ArrayList<>();

		listChildren = findOld(child_old); // the list of children ...old => all the child in Medicalrecords
		String jsonStreamChild = JsonStream.serialize(listChildren); // here we transform the list in json object

		listPersons = filterAddressInPersons(address); // the list of the persons at the same address

		if (listPersons.isEmpty()) { // if the address does not exist
			return null;
		}

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
			int findChildMedicalRecords = 0;
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
						findChildMedicalRecords = 1;
						break;
					}
				}
				if (findChildMedicalRecords == 0) { // if not medical records for this persons add without age - if add
													// only a person
					Children noPerson = new Children();
					noPerson.setFirstName(element_listPersonsAdult.getFirstName());
					noPerson.setLastName(element_listPersonsAdult.getLastName());
					noPerson.setOld(null); // no medical records for this person so old is null
					listAdultAlert.add(noPerson);
				} else
					findChildMedicalRecords = 0;

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
	public List<PhoneAlert> phoneAlertFirestation(String stationNumber) throws IOException {

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

		PhoneAlert phoneAlert = new PhoneAlert();
		List<PhoneAlert> listPhoneAlert = new ArrayList<>();
		List<String> listPhones = new ArrayList<>();
		for (Persons element_listP : listP) {
			listPhones.add(element_listP.getPhone());
		}

		// remove the the duplicate phones
		List<String> listPhonesNoDuplicate = new ArrayList<>();
		for (int i = 0; i < listPhones.size(); i++) {
			if (!listPhones.get(i).isEmpty()) { // if empty it was duplicate
				for (int j = i + 1; j < listPhones.size(); j++) {
					if (listPhones.get(i).equals(listPhones.get(j))) {
						listPhones.set(j, ""); // if duplicate set to empty
					}
				}
				listPhonesNoDuplicate.add(listPhones.get(i)); // we create the new list without duplicate
			}
		}
		phoneAlert.setListPhones(listPhonesNoDuplicate);
		listPhoneAlert.add(phoneAlert);

		return listPhoneAlert;
	}

	@Override
	public List<FireAddress> fireAddress(String address) throws IOException, ParseException {

		// create a list persons to address
		List<Persons> listPersons = new ArrayList<>();
		listPersons = filterAddressInPersons(address);

		// create a list fire stations of address
		List<Firestations> listFirestations = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listFirestations = readJsonFile.readfilejsonFirestations();
		listFirestations = findAddressInFirestations(listFirestations, address);
		String no_firestation = "";
		for (Firestations element_firestations : listFirestations) {
			no_firestation = element_firestations.getStation();
			break;
		}

		// create a list medical records
		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

		// create a list age old children and adult because he use the methods to crate
		// theses lists
		int old_children = 18;
		int old_adult = 19;
		List<Children> listChildren = new ArrayList<>();
		listChildren = findOld(old_children);
		List<Children> listAdult = new ArrayList<>();
		listAdult = findOld(old_adult);
		// create only one list age
		List<Children> listAge = new ArrayList<>();
		listAge.addAll(listChildren);
		listAge.addAll(listAdult);

		// create the list fire address with first name, last name, phone and medical
		// records
		FireAddress fireAddress;
		List<FireAddress> listFireAddress = new ArrayList<>();
		String name_person;
		String name_person_medicalrecords;
		boolean find_medicalrecords = false;
		for (Persons element_persons : listPersons) {
			name_person = element_persons.getFirstName() + element_persons.getLastName();
			for (Medicalrecords element_medicalrecords : listMedicalrecords) {
				name_person_medicalrecords = element_medicalrecords.getFirstName()
						+ element_medicalrecords.getLastName();
				if (name_person.equals(name_person_medicalrecords)) {
					fireAddress = new FireAddress();
					fireAddress.setFirestation(no_firestation);
					fireAddress.setFirstName(element_persons.getFirstName());
					fireAddress.setLastName(element_persons.getLastName());
					fireAddress.setPhone(element_persons.getPhone());
					fireAddress.setListMedications(element_medicalrecords.getMedications());
					fireAddress.setListAllergies(element_medicalrecords.getAllergies());
					listFireAddress.add(fireAddress);
					find_medicalrecords = true;
					break;
				}
			}
			if (find_medicalrecords == false) {
				fireAddress = new FireAddress();
				fireAddress.setFirestation(no_firestation);
				fireAddress.setFirstName(element_persons.getFirstName());
				fireAddress.setLastName(element_persons.getLastName());
				fireAddress.setPhone(element_persons.getPhone());
				fireAddress.setListMedications(null); // if not the medical records add the person with null records
				fireAddress.setListAllergies(null); // if not medical records add the person with null records
				listFireAddress.add(fireAddress);
			} else
				find_medicalrecords = false;
		}

		// set the age of persons
		String name_person_age;
		for (FireAddress element_fireaddress : listFireAddress) {
			name_person = element_fireaddress.getFirstName() + element_fireaddress.getLastName();
			for (Children element_listAge : listAge) {
				name_person_age = element_listAge.getFirstName() + element_listAge.getLastName();
				if (name_person_age.equals(name_person)) {
					element_fireaddress.setOld(element_listAge.getOld());
					break;
				}
			}
		}

		return listFireAddress;
	}

	@Override
	public List<PersonsFireStation> stationListFirestation(List<String> station) throws IOException, ParseException {

		List<PersonsFireStation> listPersonsFireStation = new ArrayList<>();
		PersonsFireStation personsFireStation;

		// the lists of fire stations with the number of station
		List<Firestations> listFirestations = new ArrayList<>();
		for (int i = 0; i < station.size(); i++) {
			listFirestations.addAll(filterStation(station.get(i)));
		}

		// create a list age old children and adult because he use the methods to crate
		// theses lists
		int old_children = 18;
		int old_adult = 19;
		List<Children> listChildren = new ArrayList<>();
		listChildren = findOld(old_children);
		List<Children> listAdult = new ArrayList<>();
		listAdult = findOld(old_adult);
		// create only one list age
		List<Children> listAge = new ArrayList<>();
		listAge.addAll(listChildren);
		listAge.addAll(listAdult);
		String name_person_age;

		// create a list medical records
		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

		// check the persons with the same address in persons = the same address in fire
		// station
		List<Persons> listP = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listP = readJsonFile.readfilejsonPersons();
		String jsonstream = JsonStream.serialize(listP); // here we transform the list in json object
		List<Persons> listPersons;
		AddressListFirestation addressListFirestation;
		List<AddressListFirestation> listAddressListFirestation;
		String name_person;
		String name_person_medicalrecords;
		for (Firestations element_firestation : listFirestations) {
			listPersons = new ArrayList<>();
			listPersons.addAll(findAddressInPersons(jsonstream, element_firestation.getAddress()));
			// create the list address list fire station with first name, last name, phone
			// and medical
			// records
			listAddressListFirestation = new ArrayList<>();
			boolean find_medicalrecords = false;
			for (Persons element_persons : listPersons) {
				name_person = element_persons.getFirstName() + element_persons.getLastName();
				for (Medicalrecords element_medicalrecords : listMedicalrecords) {
					name_person_medicalrecords = element_medicalrecords.getFirstName()
							+ element_medicalrecords.getLastName();
					if (name_person.equals(name_person_medicalrecords)) {
						addressListFirestation = new AddressListFirestation();
						addressListFirestation.setFirstName(element_persons.getFirstName());
						addressListFirestation.setLastName(element_persons.getLastName());
						addressListFirestation.setPhone(element_persons.getPhone());
						addressListFirestation.setListMedications(element_medicalrecords.getMedications());
						addressListFirestation.setListAllergies(element_medicalrecords.getAllergies());
						listAddressListFirestation.add(addressListFirestation);
						find_medicalrecords = true;
						break;
					}
				}
				if (find_medicalrecords == false) {
					addressListFirestation = new AddressListFirestation();
					addressListFirestation.setFirstName(element_persons.getFirstName());
					addressListFirestation.setLastName(element_persons.getLastName());
					addressListFirestation.setPhone(element_persons.getPhone());
					addressListFirestation.setListMedications(null); // if no medical records add the person without
																		// medication with null records
					addressListFirestation.setListAllergies(null); // // if no medical records add the person without
																	// allergies with null records
					listAddressListFirestation.add(addressListFirestation);
				} else
					find_medicalrecords = false;
			}
			// set the age of persons
			for (AddressListFirestation element_fireaddress : listAddressListFirestation) {
				name_person = element_fireaddress.getFirstName() + element_fireaddress.getLastName();
				for (Children element_listAge : listAge) {
					name_person_age = element_listAge.getFirstName() + element_listAge.getLastName();
					if (name_person_age.equals(name_person)) {
						element_fireaddress.setOld(element_listAge.getOld());
						break;
					}
				}
			}
			personsFireStation = new PersonsFireStation();
			personsFireStation.setAddress(element_firestation.getAddress());
			personsFireStation.setListAddressFirestations(listAddressListFirestation);
			listPersonsFireStation.add(personsFireStation);
		}

		return listPersonsFireStation;
	}

	@Override
	public List<PersonInfo> personInfo(String firstName, String lastName) throws IOException, ParseException {

		// create a list of persons
		List<Persons> listPersons = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listPersons = readJsonFile.readfilejsonPersons();

		// create a list of medical records
		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

		// the list person with only the get of the firsName and lastName
		List<PersonInfo> listPersonInfo = new ArrayList<>();
		PersonInfo personInfo;
		boolean find_medicalrecords = false;
		for (Persons element_persons : listPersons) {
			if ((element_persons.getFirstName().equals(firstName) && element_persons.getLastName().equals(lastName))
					|| element_persons.getLastName().equals(lastName)) {
				personInfo = new PersonInfo();
				personInfo.setFirstName(element_persons.getFirstName());
				personInfo.setLastName(element_persons.getLastName());
				personInfo.setAddress(element_persons.getAddress());
				personInfo.setEmail(element_persons.getEmail());
				for (Medicalrecords element_medicalrecords : listMedicalrecords) {
					if (element_medicalrecords.getFirstName().equals(element_persons.getFirstName())
							&& element_medicalrecords.getLastName().equals(element_persons.getLastName())) {
						personInfo.setListMedications(element_medicalrecords.getMedications());
						personInfo.setListAllergies(element_medicalrecords.getAllergies());
						find_medicalrecords = true;
						break;
					}
				}
				if (find_medicalrecords == false) {
					personInfo.setListMedications(null); // add the person with the null data in not medical records
					personInfo.setListAllergies(null); // add the person with the null data in not medical records
				} else
					find_medicalrecords = false;
				listPersonInfo.add(personInfo);
			}
		}

		// create a list age old children and adult because he use the methods to crate
		// theses lists
		int old_children = 18;
		int old_adult = 19;
		List<Children> listChildren = new ArrayList<>();
		listChildren = findOld(old_children);
		List<Children> listAdult = new ArrayList<>();
		listAdult = findOld(old_adult);
		// create only one list age
		List<Children> listAge = new ArrayList<>();
		listAge.addAll(listChildren);
		listAge.addAll(listAdult);
		String name_person_age;
		// set the age of persons
		String name_person;
		for (PersonInfo element_personInfo : listPersonInfo) {
			name_person = element_personInfo.getFirstName() + element_personInfo.getLastName();
			for (Children element_listAge : listAge) {
				name_person_age = element_listAge.getFirstName() + element_listAge.getLastName();
				if (name_person_age.equals(name_person)) {
					element_personInfo.setOld(element_listAge.getOld());
					break;
				}
			}
		}

		return listPersonInfo;
	}

	@Override
	public List<CommunityEmail> communityEmail(String city) throws IOException {

		List<CommunityEmail> listCommunityEmail = new ArrayList<>();

		List<Persons> listPersons = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listPersons = readJsonFile.readfilejsonPersons();

		CommunityEmail communityEmail;
		List<String> listEmail = new ArrayList<>();
		for (Persons element_persons : listPersons) {
			if (element_persons.getCity().equals(city)) {
				listEmail.add(element_persons.getEmail());
			}
		}

		// remove the the duplicate email
		List<String> listEmailNoDuplicate = new ArrayList<>();

		for (int i = 0; i < listEmail.size(); i++) {
			if (!listEmail.get(i).isEmpty()) { // if empty it was duplicate
				for (int j = i + 1; j < listEmail.size(); j++) {
					if (listEmail.get(i).equals(listEmail.get(j))) {
						listEmail.set(j, ""); // if duplicate set to empty
					}
				}
				listEmailNoDuplicate.add(listEmail.get(i)); // we create the new list without duplicate
			}
		}

		communityEmail = new CommunityEmail();
		communityEmail.setListEmails(listEmailNoDuplicate);
		listCommunityEmail.add(communityEmail);

		return listCommunityEmail;
	}

	@Override
	public Persons addPerson(Persons persons) throws IOException {

		readJsonFile = new ReadJsonFile();

		List<Persons> listPersons = new ArrayList<>();
		listPersons = readJsonFile.readfilejsonPersons();

		// verify if the persons is exist in the persons if not = add
		boolean find_persons = false;
		for (Persons element : listPersons) {
			if ((element.getFirstName() + element.getLastName())
					.equals(persons.getFirstName() + persons.getLastName())) {
				find_persons = true;
				break;
			}
		}

		// add the persons if find_persons is false
		if (find_persons == false) {
			listPersons.add(persons); // add the body

			// create fire stations
			List<Firestations> listFirestations = new ArrayList<>();
			listFirestations = readJsonFile.readfilejsonFirestations();
			// create medical records
			List<Medicalrecords> listMedicalrecords = new ArrayList<>();
			listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

			CollectionsRessources collectionsRessources = new CollectionsRessources();
			collectionsRessources.setPersons(listPersons);
			collectionsRessources.setFirestations(listFirestations);
			collectionsRessources.setMedicalrecords(listMedicalrecords);

			String jsonstream = JsonStream.serialize(collectionsRessources); // here we transform the list in json
																				// object
			FileWriter writer = new FileWriter(readJsonFile.filepath_json);
			writer.write(jsonstream);
			writer.flush();
			writer.close();

			return persons;
		}
		return null;
	}

	@Override
	public boolean updatePerson(Persons persons, String firstName, String lastName) throws IOException {

		readJsonFile = new ReadJsonFile();

		List<Persons> listPersons = new ArrayList<>();
		listPersons = readJsonFile.readfilejsonPersons();

		// find the person and update
		String firstNamelastName = firstName + lastName;
		for (Persons element : listPersons) {
			if ((element.getFirstName() + element.getLastName()).equals(firstNamelastName)) {
				if (persons.getAddress() != null) {
					element.setAddress(persons.getAddress());
				}
				if (persons.getCity() != null) {
					element.setCity(persons.getCity());
				}
				if (persons.getEmail() != null) {
					element.setEmail(persons.getEmail());
				}
				if (persons.getPhone() != null) {
					element.setPhone(persons.getPhone());
				}
				if (persons.getZip() != null) {
					element.setZip(persons.getZip());
				}

				// create fire stations
				List<Firestations> listFirestations = new ArrayList<>();
				listFirestations = readJsonFile.readfilejsonFirestations();
				// create medical records
				List<Medicalrecords> listMedicalrecords = new ArrayList<>();
				listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

				CollectionsRessources collectionsRessources = new CollectionsRessources();
				collectionsRessources.setPersons(listPersons);
				collectionsRessources.setFirestations(listFirestations);
				collectionsRessources.setMedicalrecords(listMedicalrecords);

				String jsonstream = JsonStream.serialize(collectionsRessources); // here we transform the list in json
																					// object

				FileWriter writer = new FileWriter(readJsonFile.filepath_json);
				writer.write(jsonstream);
				writer.flush();
				writer.close();

				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deletePerson(String firstName, String lastName) throws IOException {

		readJsonFile = new ReadJsonFile();

		List<Persons> listPersons = new ArrayList<>();
		listPersons = readJsonFile.readfilejsonPersons();

		// find the person and delete
		String firstNamelastName = firstName + lastName;
		for (Persons element : listPersons) {
			if ((element.getFirstName() + element.getLastName()).equals(firstNamelastName)) {
				listPersons.remove(element);
				// create fire stations
				List<Firestations> listFirestations = new ArrayList<>();
				listFirestations = readJsonFile.readfilejsonFirestations();
				// create medical records
				List<Medicalrecords> listMedicalrecords = new ArrayList<>();
				listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

				CollectionsRessources collectionsRessources = new CollectionsRessources();
				collectionsRessources.setPersons(listPersons);
				collectionsRessources.setFirestations(listFirestations);
				collectionsRessources.setMedicalrecords(listMedicalrecords);

				String jsonstream = JsonStream.serialize(collectionsRessources); // here we transform the list in json
																					// object

				FileWriter writer = new FileWriter(readJsonFile.filepath_json);
				writer.write(jsonstream);
				writer.flush();
				writer.close();
				return true;
			}
		}
		return false;
	}

	@Override
	public void addFirestation(Firestations firestation) throws IOException {

		readJsonFile = new ReadJsonFile();

		List<Firestations> listFirestations = new ArrayList<>();
		listFirestations = readJsonFile.readfilejsonFirestations();

		// verify if the fire station exist in the fires stations if not = add
		boolean find_firestation = false;
		for (Firestations element : listFirestations) {
			if (element.getAddress().equals(firestation.getAddress())) {
				find_firestation = true;
				break;
			}
		}

		// add the fire station if find_firestation is false
		if (find_firestation == false) {
			listFirestations.add(firestation); // add the body

			// create persons
			List<Persons> listPersons = new ArrayList<>();
			listPersons = readJsonFile.readfilejsonPersons();
			// create medical records
			List<Medicalrecords> listMedicalrecords = new ArrayList<>();
			listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

			CollectionsRessources collectionsRessources = new CollectionsRessources();
			collectionsRessources.setPersons(listPersons);
			collectionsRessources.setFirestations(listFirestations);
			collectionsRessources.setMedicalrecords(listMedicalrecords);

			String jsonstream = JsonStream.serialize(collectionsRessources); // here we transform the list in json
																				// object

			FileWriter writer = new FileWriter(readJsonFile.filepath_json);
			writer.write(jsonstream);
			writer.flush();
			writer.close();
		}

	}

	@Override
	public void updateFirestation(Firestations firestation, String address) throws IOException {

		readJsonFile = new ReadJsonFile();

		List<Firestations> listFirestations = new ArrayList<>();
		listFirestations = readJsonFile.readfilejsonFirestations();

		// find the fire station and update
		for (Firestations element : listFirestations) {
			if (element.getAddress().equals(address)) {
				if (firestation.getStation() != null) {
					element.setStation(firestation.getStation());
				}
				break;
			}
		}

		// create persons
		List<Persons> listPersons = new ArrayList<>();
		listPersons = readJsonFile.readfilejsonPersons();
		// create medical records
		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

		CollectionsRessources collectionsRessources = new CollectionsRessources();
		collectionsRessources.setPersons(listPersons);
		collectionsRessources.setFirestations(listFirestations);
		collectionsRessources.setMedicalrecords(listMedicalrecords);

		String jsonstream = JsonStream.serialize(collectionsRessources); // here we transform the list in json
																			// object

		FileWriter writer = new FileWriter(readJsonFile.filepath_json);
		writer.write(jsonstream);
		writer.flush();
		writer.close();

	}

	@Override
	public void deleteFirestation(String address, String stationNumber) throws IOException {

		// if only one request parameter is used
		if ((address != null && stationNumber == null) || (stationNumber != null && address == null)) {

			readJsonFile = new ReadJsonFile();

			List<Firestations> listF = new ArrayList<>();
			List<Firestations> listFirestations = new ArrayList<>();
			Firestations firestations;
			listF = readJsonFile.readfilejsonFirestations();

			// if there are address in URI
			if (address != null && stationNumber == null) {
				for (Firestations element : listF) {
					if (!element.getAddress().equals(address)) {
						firestations = new Firestations();
						firestations.setAddress(element.getAddress());
						firestations.setStation(element.getStation());
						listFirestations.add(firestations);
					}
				}
			}
			// if there are station number in URI
			if (stationNumber != null && address == null) {
				for (Firestations element : listF) {
					if (!element.getStation().equals(stationNumber)) {
						firestations = new Firestations();
						firestations.setAddress(element.getAddress());
						firestations.setStation(element.getStation());
						listFirestations.add(firestations);
					}
				}
			}

			// create persons
			List<Persons> listPersons = new ArrayList<>();
			listPersons = readJsonFile.readfilejsonPersons();
			// create medical records
			List<Medicalrecords> listMedicalrecords = new ArrayList<>();
			listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

			CollectionsRessources collectionsRessources = new CollectionsRessources();
			collectionsRessources.setPersons(listPersons);
			collectionsRessources.setFirestations(listFirestations);
			collectionsRessources.setMedicalrecords(listMedicalrecords);

			String jsonstream = JsonStream.serialize(collectionsRessources); // here we transform the list in json
																				// object

			FileWriter writer = new FileWriter(readJsonFile.filepath_json);
			writer.write(jsonstream);
			writer.flush();
			writer.close();

		}
	}

	@Override
	public void addMedicalRecord(Medicalrecords medicalRecord) throws IOException {

		readJsonFile = new ReadJsonFile();
		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

		// verify if the persons is exist in the medical records if not = add
		boolean find_persons = false;
		for (Medicalrecords element : listMedicalrecords) {
			if ((element.getFirstName() + element.getLastName())
					.equals(medicalRecord.getFirstName() + medicalRecord.getLastName())) {
				find_persons = true;
				break;
			}
		}

		// add the fire station if find_firestation is false
		if (find_persons == false) {
			listMedicalrecords.add(medicalRecord); // add the body

			// create persons
			List<Persons> listPersons = new ArrayList<>();
			listPersons = readJsonFile.readfilejsonPersons();
			// create fire stations
			List<Firestations> listFirestations = new ArrayList<>();
			listFirestations = readJsonFile.readfilejsonFirestations();

			CollectionsRessources collectionsRessources = new CollectionsRessources();
			collectionsRessources.setPersons(listPersons);
			collectionsRessources.setFirestations(listFirestations);
			collectionsRessources.setMedicalrecords(listMedicalrecords);

			String jsonstream = JsonStream.serialize(collectionsRessources); // here we transform the list in json
																				// object

			FileWriter writer = new FileWriter(readJsonFile.filepath_json);
			writer.write(jsonstream);
			writer.flush();
			writer.close();
		}
	}

	@Override
	public void updateMedicalRecord(Medicalrecords medicalRecord, String firstName, String lastName)
			throws IOException {
		readJsonFile = new ReadJsonFile();

		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

		// find the person and update
		String firstNamelastName = firstName + lastName;
		for (Medicalrecords element : listMedicalrecords) {
			if ((element.getFirstName() + element.getLastName()).equals(firstNamelastName)) {
				if (medicalRecord.getAllergies() != null) {
					element.setAllergies(medicalRecord.getAllergies());
				}
				if (medicalRecord.getBirthdate() != null) {
					element.setBirthdate(medicalRecord.getBirthdate());
				}
				if (medicalRecord.getMedications() != null) {
					element.setMedications(medicalRecord.getMedications());
				}
				break;
			}
		}

		// create persons
		List<Persons> listPersons = new ArrayList<>();
		listPersons = readJsonFile.readfilejsonPersons();
		// create fire stations
		List<Firestations> listFirestations = new ArrayList<>();
		listFirestations = readJsonFile.readfilejsonFirestations();

		CollectionsRessources collectionsRessources = new CollectionsRessources();
		collectionsRessources.setPersons(listPersons);
		collectionsRessources.setFirestations(listFirestations);
		collectionsRessources.setMedicalrecords(listMedicalrecords);

		String jsonstream = JsonStream.serialize(collectionsRessources); // here we transform the list in json
																			// object

		FileWriter writer = new FileWriter(readJsonFile.filepath_json);
		writer.write(jsonstream);
		writer.flush();
		writer.close();

	}

	@Override
	public void deleteMedicalRecord(String firstName, String lastName) throws IOException {
		readJsonFile = new ReadJsonFile();

		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();

		// find the person and delete
		String firstNamelastName = firstName + lastName;
		for (Medicalrecords element : listMedicalrecords) {
			if ((element.getFirstName() + element.getLastName()).equals(firstNamelastName)) {
				listMedicalrecords.remove(element);
				break;
			}
		}

		// create persons
		List<Persons> listPersons = new ArrayList<>();
		listPersons = readJsonFile.readfilejsonPersons();
		// create fire stations
		List<Firestations> listFirestations = new ArrayList<>();
		listFirestations = readJsonFile.readfilejsonFirestations();

		CollectionsRessources collectionsRessources = new CollectionsRessources();
		collectionsRessources.setPersons(listPersons);
		collectionsRessources.setFirestations(listFirestations);
		collectionsRessources.setMedicalrecords(listMedicalrecords);

		String jsonstream = JsonStream.serialize(collectionsRessources); // here we transform the list in json
																			// object

		FileWriter writer = new FileWriter(readJsonFile.filepath_json);
		writer.write(jsonstream);
		writer.flush();
		writer.close();

	}

	@Override
	public List<Persons> getAPerson(String firstNamelastName) throws IOException {
		List<Persons> listP = new ArrayList<>();
		List<Persons> listPersons = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listP = readJsonFile.readfilejsonPersons(); // here we have a list of objects Persons from json
		for (Persons element : listP) {
			if ((element.getFirstName() + element.getLastName()).equals(firstNamelastName)) {
				listPersons.add(element);
			}
		}
		return listPersons;
	}

	@Override
	public List<Medicalrecords> getAMedicalrecord(String firstNamelastName) throws IOException {
		List<Medicalrecords> listM = new ArrayList<>();
		List<Medicalrecords> listMedicalreords = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listM = readJsonFile.readfilejsonMedicalrecords(); // here we have a list of objects Medicalrecords from json
		for (Medicalrecords element : listM) {
			if ((element.getFirstName() + element.getLastName()).equals(firstNamelastName)) {
				listMedicalreords.add(element);
			}
		}
		return listMedicalreords;
	}
}
