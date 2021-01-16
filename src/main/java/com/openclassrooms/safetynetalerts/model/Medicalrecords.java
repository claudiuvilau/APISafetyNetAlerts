package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class Medicalrecords {

	private String firstName;
	private String lastName;
	private String birthdate;
	private List<Medications> listMedications = new ArrayList<>();
	private List<Allergies> listAllergies = new ArrayList<>();

	public Medicalrecords() {
	}

	public Medicalrecords(String firstName, String lastName, String birthdate, List<Medications> listMedications,
			List<Allergies> listAllergies) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.listMedications = listMedications;
		this.listAllergies = listAllergies;
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

	public List<Medications> getListMedications() {
		return listMedications;
	}

	public void setListMedications(List<Medications> listMedications) {
		this.listMedications = listMedications;
	}

	public List<Allergies> getListAllergies() {
		return listAllergies;
	}

	public void setListAllergies(List<Allergies> listAllergies) {
		this.listAllergies = listAllergies;
	}

	@Override
	public String toString() {
		return "Medicalrecords [firstName=" + firstName + ", lastName=" + lastName + ", birthdate=" + birthdate
				+ ", listMedications=" + listMedications + ", listAllergies=" + listAllergies + "]";
	}

}
