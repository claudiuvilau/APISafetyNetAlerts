package com.openclassrooms.safetynetalerts.model;

public class Allergies {

	private String allergies;

	public Allergies() {

	}

	public Allergies(String allergies) {
		super();
		this.allergies = allergies;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	@Override
	public String toString() {
		return "Allergies [allergies=" + allergies + "]";
	}

}
