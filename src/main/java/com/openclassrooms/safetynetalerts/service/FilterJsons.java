package com.openclassrooms.safetynetalerts.service;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;
import com.openclassrooms.safetynetalerts.model.Children;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Medicalrecords;
import com.openclassrooms.safetynetalerts.model.Persons;

@Service
public class FilterJsons implements InterfaceFilterJsons {

	private ReadJsonFile readJsonFile;
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonDaoImplements.class);
	private LoggerApi loggerApi;

	public FilterJsons() {
		super();
	}

	public FilterJsons(ReadJsonFile readJsonFile, LoggerApi loggerApi) {
		super();
		this.readJsonFile = readJsonFile;
		this.loggerApi = loggerApi;
	}

	public ReadJsonFile getReadJsonFile() {
		return readJsonFile;
	}

	public void setReadJsonFile(ReadJsonFile readJsonFile) {
		this.readJsonFile = readJsonFile;
	}

	public LoggerApi getLoggerApi() {
		return loggerApi;
	}

	public void setLoggerApi(LoggerApi loggerApi) {
		this.loggerApi = loggerApi;
	}

	public static Logger getLogger() {
		return LOGGER;
	}

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
			LOGGER.error(loggerApi.loggerErr(e, caserne));
		}

		return listFirestations;
	}

	public List<Persons> filterAddressInPersons(String address) {

		List<Persons> listP = new ArrayList<>();
		List<Persons> listPersons = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listP = readJsonFile.readfilejsonPersons(); // here we have a list of objects Persons from json
		// file
		String jsonstream = JsonStream.serialize(listP); // here we transform the list in json object

		// We will read the json object and if we have an address == address we
		// will make another list
		try {
			listPersons = findAddressInPersons(jsonstream, address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listPersons;
	}

	public List<Persons> findAddressInPersons(String jsonStream, String address) throws IOException {

		Persons persons = new Persons();
		List<Persons> listPersons = new ArrayList<>();

		JsonIterator iter = JsonIterator.parse(jsonStream);

		Any any = null;
		try {
			any = iter.readAny();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonIterator iterator;
		for (Any element : any) {
			iterator = JsonIterator.parse(element.toString());
			for (String field = iterator.readObject(); field != null; field = iterator.readObject()) {
				switch (field) {
				case "address":
					try {
						if (iterator.whatIsNext() == ValueType.STRING) {
							if (iterator.readString().equals(address)) {
								persons = JsonIterator.deserialize(element.toString(), Persons.class);
								listPersons.add(persons);
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				default:
					try {
						iterator.skip();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return listPersons;
	}

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

	public List<Children> findOld(int old) throws IOException, ParseException {

		List<Children> listChild = new ArrayList<>();
		List<Medicalrecords> listMedicalrecords = new ArrayList<>();
		readJsonFile = new ReadJsonFile();
		listMedicalrecords = readJsonFile.readfilejsonMedicalrecords();
		listChild = listFindOld(listMedicalrecords, old);

		return listChild;
	}

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

}
