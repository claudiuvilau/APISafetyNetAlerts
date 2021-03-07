package com.openclassrooms.safetynetalerts.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;
import com.openclassrooms.safetynetalerts.model.Firestations;

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
}
