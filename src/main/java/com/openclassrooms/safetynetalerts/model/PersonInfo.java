package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class PersonInfo {
	private String firstName;
	private String lastName;
	private String address;
	private String old;
	private String email;
	private List<String> listMedications = new ArrayList<>();
	private List<String> listAllergies = new ArrayList<>();

	public PersonInfo() {
		super();
	}

	public PersonInfo(String firstName, String lastName, String address, String old, String email,
			List<String> listMedications, List<String> listAllergies) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.old = old;
		this.email = email;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOld() {
		return old;
	}

	public void setOld(String old) {
		this.old = old;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getListMedications() {
		return listMedications;
	}

	public void setListMedications(List<String> listMedications) {
		this.listMedications = listMedications;
	}

	public List<String> getListAllergies() {
		return listAllergies;
	}

	public void setListAllergies(List<String> listAllergies) {
		this.listAllergies = listAllergies;
	}

	@Override
	public String toString() {
		return "PersonInfo [firstName=" + firstName + ", lastName=" + lastName + ", address=" + address + ", old=" + old
				+ ", email=" + email + ", listMedications=" + listMedications + ", listAllergies=" + listAllergies
				+ "]";
	}

}
