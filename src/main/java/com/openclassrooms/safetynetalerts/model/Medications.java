package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class Medications {

	private List<String> medications = new ArrayList<>();

	public Medications() {

	}

	public Medications(List<String> medications) {
		super();
		this.medications = medications;
	}

	public List<String> getMedications() {
		return medications;
	}

	public void setMedications(List<String> medications) {
		this.medications = medications;
	}

	@Override
	public String toString() {
		return "Medications [medications=" + medications + "]";
	}

}
