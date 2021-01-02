package com.openclassrooms.safetynetalerts.dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetynetalerts.model.Firestations;

@Repository
public class JsonDaoImplements implements JsonDao {

	JSONArray jsonA = new JSONArray();

	@Override
	public JSONObject createPersonsCaserne(String collection, String caserne) {

		Firestations firestations = new Firestations();
		// jsonA = firestations.firestationsJson();

		JSONArray jsonACaserne = new JSONArray(); // the new array with only the number of the caserne
		boolean personsCaserne = false;

		for (int i = 0; i < jsonA.size(); i++) {
			JSONObject jsonObjectFor = (JSONObject) jsonA.get(i);
			if (jsonObjectFor.get("station").equals(caserne.trim())) {
				jsonACaserne.add(jsonObjectFor); // it create the new object with the number of station
				personsCaserne = true;
			}
		}

		JSONObject jsonO = new JSONObject();

		if (personsCaserne) {
			jsonO.put("firestations", jsonACaserne);
			return jsonO; // if the address with the number of caserne
		} else
			jsonO.put("firestations", jsonA);
		return jsonO;
	}

}
