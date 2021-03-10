package com.openclassrooms.safetynetalerts.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.openclassrooms.safetynetalerts.model.Children;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Persons;

public interface InterfaceFilterJsons {

	public List<Firestations> filterStation(String caserne);

	public List<Persons> findAddressInPersons(String jsonStream, String address) throws IOException;

	public List<Firestations> findAddressInFirestations(List<?> listFireStations, String address) throws IOException;;

	public List<Persons> filterAddressInPersons(String address) throws IOException;

	public List<Children> findOld(int old) throws IOException, ParseException;

	public List<Children> listFindOld(List<?> list, int old) throws IOException, ParseException;

}
