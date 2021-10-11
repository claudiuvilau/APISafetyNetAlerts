package com.openclassrooms.safetynetalerts.model;

public class Firestations {

	private String address;
	private String station;

	public Firestations() {

	}

	public Firestations(String address, String station) {
		this.address = address;
		this.station = station;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	@Override
	public String toString() {
		return "Firestations [address=" + address + ", station=" + station + "]";
	}

}
