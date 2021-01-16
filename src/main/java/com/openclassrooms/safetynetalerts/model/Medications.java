package com.openclassrooms.safetynetalerts.model;

public class Medications {

	private String medications;

	public Medications() {

	}

	public Medications(String medications) {
		super();
		this.medications = medications;
	}

	public String getMedications() {
		return medications;
	}

	public void setMedications(String medications) {
		this.medications = medications;
	}

	@Override
	public String toString() {
		return "Medications [medications=" + medications + "]";
	}

}
