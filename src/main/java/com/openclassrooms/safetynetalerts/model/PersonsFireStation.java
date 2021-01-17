package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class PersonsFireStation {

	private int decompte;
	private List<Persons> listPersonsAdults = new ArrayList<>();
	private List<Persons> listPersonsChildren = new ArrayList<>();
	private int old;

	public PersonsFireStation() {

	}

	public PersonsFireStation(int decompte, List<Persons> listPersonsAdults, List<Persons> listPersonsChildren,
			int old) {
		super();
		this.decompte = decompte;
		this.listPersonsAdults = listPersonsAdults;
		this.listPersonsChildren = listPersonsChildren;
		this.old = old;
	}

	public int getDecompte() {
		return decompte;
	}

	public void setDecompte(int decompte) {
		this.decompte = decompte;
	}

	public List<Persons> getListPersonsAdults() {
		return listPersonsAdults;
	}

	public void setListPersonsAdults(List<Persons> listPersonsAdults) {
		this.listPersonsAdults = listPersonsAdults;
	}

	public List<Persons> getListPersonsChildren() {
		return listPersonsChildren;
	}

	public void setListPersonsChildren(List<Persons> listPersonsChildren) {
		this.listPersonsChildren = listPersonsChildren;
	}

	public int getOld() {
		return old;
	}

	public void setOld(int old) {
		this.old = old;
	}

	@Override
	public String toString() {
		return "PersonsFireStation [decompte=" + decompte + ", listPersonsAdults=" + listPersonsAdults
				+ ", listPersonsChildren=" + listPersonsChildren + ", old=" + old + "]";
	}

}
