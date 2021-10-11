package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class PhoneAlert {

	private List<String> listPhones = new ArrayList<>();

	public PhoneAlert() {
		super();
	}

	public PhoneAlert(List<String> listPhones) {
		super();
		this.listPhones = listPhones;
	}

	public List<String> getListPhones() {
		return listPhones;
	}

	public void setListPhones(List<String> listPhones) {
		this.listPhones = listPhones;
	}

	@Override
	public String toString() {
		return "PhoneAlert [listPhones=" + listPhones + "]";
	}

}
