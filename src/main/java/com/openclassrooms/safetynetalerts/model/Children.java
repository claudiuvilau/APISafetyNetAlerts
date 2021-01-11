package com.openclassrooms.safetynetalerts.model;

public class Children {

	private String firstName;
	private String lastName;
	private int old;

	public Children() {

	}

	public Children(String firstName, String lastName, int old) {
		this.firstName = firstName;
		this.lastName = lastName;
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

	public int getOld() {
		return old;
	}

	public void setOld(int old) {
		this.old = old;
	}

	@Override
	public String toString() {
		return "Children [firstName=" + firstName + ", lastName=" + lastName + ", old=" + old + "]";
	}

}
