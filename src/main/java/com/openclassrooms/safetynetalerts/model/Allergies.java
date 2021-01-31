package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class Allergies {

	private List<String> allergies = new ArrayList<>();

	public Allergies() {

	}

	public Allergies(List<String> allergies) {
		super();
		this.allergies = allergies;
	}

	public List<String> getAllergies() {
		return allergies;
	}

	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}

	@Override
	public String toString() {
		return "Allergies [allergies=" + allergies + "]";
	}

}
