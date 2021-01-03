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

@Repository
public class JsonDaoImplements implements JsonDao {

	@Override
	public List<?> createPersonsCaserne(String caserne) {

		ReadJsonFile readJsonFile = new ReadJsonFile();
		List<Firestations> listF = new ArrayList<>();
		List<Firestations> listFirestations = new ArrayList<>();
		Firestations firestations = new Firestations();
		try {
			listF = readJsonFile.readfilejsonFirestations(); // here we have a list of objects from json file
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

}
