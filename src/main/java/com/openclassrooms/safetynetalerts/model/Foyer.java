package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("monFiltrePersons")
public class Foyer {

	private String decompteAdult;
	private List<Persons> listPersonsAdults = new ArrayList<>();
	private String decompteChildren;
	private List<Persons> listPersonsChildren = new ArrayList<>();
	private List<Medicalrecords> listMedicalrecords = new ArrayList<>();

	public Foyer() {

	}

	public Foyer(String decompteAdult, List<Persons> listPersonsAdults, String decompteChildren,
			List<Persons> listPersonsChildren, List<Medicalrecords> listMedicalrecords) {
		super();
		this.decompteAdult = decompteAdult;
		this.listPersonsAdults = listPersonsAdults;
		this.decompteChildren = decompteChildren;
		this.listPersonsChildren = listPersonsChildren;
		this.listMedicalrecords = listMedicalrecords;
	}

	public String getDecompteAdult() {
		return decompteAdult;
	}

	public void setDecompteAdult(String decompteAdult) {
		this.decompteAdult = decompteAdult;
	}

	public List<Persons> getListPersonsAdults() {
		return listPersonsAdults;
	}

	public void setListPersonsAdults(List<Persons> listPersonsAdults) {
		this.listPersonsAdults = listPersonsAdults;
	}

	public String getDecompteChildren() {
		return decompteChildren;
	}

	public void setDecompteChildren(String decompteChildren) {
		this.decompteChildren = decompteChildren;
	}

	public List<Persons> getListPersonsChildren() {
		return listPersonsChildren;
	}

	public void setListPersonsChildren(List<Persons> listPersonsChildren) {
		this.listPersonsChildren = listPersonsChildren;
	}

	public List<Medicalrecords> getListMedicalrecords() {
		return listMedicalrecords;
	}

	public void setListMedicalrecords(List<Medicalrecords> listMedicalrecords) {
		this.listMedicalrecords = listMedicalrecords;
	}

	@Override
	public String toString() {
		return "Foyer [decompteAdult=" + decompteAdult + ", listPersonsAdults=" + listPersonsAdults
				+ ", decompteChildren=" + decompteChildren + ", listPersonsChildren=" + listPersonsChildren
				+ ", listMedicalrecords=" + listMedicalrecords + "]";
	}

}
