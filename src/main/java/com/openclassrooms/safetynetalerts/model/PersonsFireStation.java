package com.openclassrooms.safetynetalerts.model;

public class PersonsFireStation {

	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private int old;

	public PersonsFireStation() {

	}

	public PersonsFireStation(String firstName, String lastName, String address, String phone, int old) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phone = phone;
		this.old = old;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getOld() {
		return old;
	}

	public void setOld(int old) {
		this.old = old;
	}

	@Override
	public String toString() {
		return "PersonsFireStation [firstName=" + firstName + ", lastName=" + lastName + ", address=" + address
				+ ", phone=" + phone + ", old=" + old + "]";
	}

}
