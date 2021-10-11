package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class PersonsFireStation {

	private String address;
	private List<AddressListFirestation> listAddressFirestations = new ArrayList<>();

	public PersonsFireStation() {
		super();
	}

	public PersonsFireStation(String address, List<AddressListFirestation> listAddressFirestations) {
		super();
		this.address = address;
		this.listAddressFirestations = listAddressFirestations;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<AddressListFirestation> getListAddressFirestations() {
		return listAddressFirestations;
	}

	public void setListAddressFirestations(List<AddressListFirestation> listAddressFirestations) {
		this.listAddressFirestations = listAddressFirestations;
	}

	@Override
	public String toString() {
		return "PersonsFireStation [address=" + address + ", listAddressFirestations=" + listAddressFirestations + "]";
	}

}
