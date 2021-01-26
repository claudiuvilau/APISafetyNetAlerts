package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class CollectionsRessources {

	private List<Persons> persons = new ArrayList<>();
	private List<Firestations> firestations = new ArrayList<>();
	private List<Medicalrecords> medicalrecords = new ArrayList<>();

	public CollectionsRessources() {
		super();
	}

	public CollectionsRessources(List<Persons> persons, List<Firestations> firestations,
			List<Medicalrecords> medicalrecords) {
		super();
		this.persons = persons;
		this.firestations = firestations;
		this.medicalrecords = medicalrecords;
	}

	public List<Persons> getPersons() {
		return persons;
	}

	public void setPersons(List<Persons> persons) {
		this.persons = persons;
	}

	public List<Firestations> getFirestations() {
		return firestations;
	}

	public void setFirestations(List<Firestations> firestations) {
		this.firestations = firestations;
	}

	public List<Medicalrecords> getMedicalrecords() {
		return medicalrecords;
	}

	public void setMedicalrecords(List<Medicalrecords> medicalrecords) {
		this.medicalrecords = medicalrecords;
	}

	@Override
	public String toString() {
		return "CollectionsRessources [persons=" + persons + ", firestations=" + firestations + ", medicalrecords="
				+ medicalrecords + "]";
	}

}
