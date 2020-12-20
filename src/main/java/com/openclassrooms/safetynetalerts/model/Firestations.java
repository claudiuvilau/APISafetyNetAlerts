package com.openclassrooms.safetynetalerts.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Firestations {

	private String address;
	private String station;

	public Firestations() {

	}

	public Firestations(String address, String station) {
		this.address = address;
		this.station = station;
	}

	public JSONArray firestationsJson() {
		JSONObject jsonO = new JSONObject();
		JSONParser jsonP = new JSONParser();
		JSONArray jsonA = new JSONArray();
		try {
			jsonO = (JSONObject) jsonP.parse(new FileReader("data/dbJSON.json"));
			jsonA = (JSONArray) jsonO.get("firestations");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonA;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	@Override
	public String toString() {
		return "Firestations [address=" + address + ", station=" + station + "]";
	}

}
