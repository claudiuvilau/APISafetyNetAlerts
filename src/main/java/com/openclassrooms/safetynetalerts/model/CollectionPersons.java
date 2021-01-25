package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class CollectionPersons {
	
	private List<Persons> persons = new ArrayList<>();

	public CollectionPersons() {
		super();
	}

	public CollectionPersons(List<Persons> persons) {
		super();
		this.persons = persons;
	}

	public List<Persons> getPersons() {
		return persons;
	}

	public void setPersons(List<Persons> persons) {
		this.persons = persons;
	}

	@Override
	public String toString() {
		return "CollectionPersons [persons=" + persons + "]";
	}
	
	

}
