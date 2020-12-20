package com.openclassrooms.safetynetalerts.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Medicalrecords {

	private String firstName;
	private String lastName;
	private String birthdate;
	private HashMap medication;

	public Medicalrecords() {
	}

	public Medicalrecords(String firstName, String lastName, String birthdate, HashMap medication) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.medication = medication;
	}

	public JSONArray medicalrecordsJson() {
		JSONObject jsonO = new JSONObject();
		JSONParser jsonP = new JSONParser();
		JSONArray jsonA = new JSONArray();
		try {
			jsonO = (JSONObject) jsonP.parse(new FileReader("data/dbJSON.json"));
			jsonA = (JSONArray) jsonO.get("medicalrecords");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonA;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public HashMap getMedication() {
		return medication;
	}

	public void setMedication(HashMap medication) {
		this.medication = medication;
	}

	@Override
	public String toString() {
		return "Medicalrecords [firstName=" + firstName + ", lastName=" + lastName + ", birthdate=" + birthdate
				+ ", medication=" + medication + "]";
	}

}
