package com.openclassrooms.safetynetalerts.service;

import java.util.List;

import com.openclassrooms.safetynetalerts.model.Firestations;

public interface InterfaceFilterJsons {

	public List<Firestations> filterStation(String caserne);

}
