package com.openclassrooms.safetynetalerts.dao;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.openclassrooms.safetynetalerts.model.Children;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Foyer;
import com.openclassrooms.safetynetalerts.model.Persons;

public interface JsonDao {

	public List<Persons> findAddressInPersons(String jsonStream, String address) throws IOException;

	public List<Children> findChild(int old) throws IOException, ParseException;

	public List<Firestations> findAddressInFirestations(String jsonStream, String address) throws IOException;;

	public List<Firestations> filterStation(String stationNumber);

	public List<Persons> filterAddressInPersons(String address);

	/*
	 * L'utilisateur accède à l’URL :
	 * http://localhost:8080/firestation?stationNumber=<station_number>
	 * 
	 * Le système retourne une liste des personnes (prénom, nom, adresse, numéro de
	 * téléphone) couvertes par la caserne de pompiers correspondante ainsi qu’un
	 * décompte du nombre d’adultes (>18 ans) et du nombre d’enfants (<=18 ans)
	 * 
	 * GET /persons/{firstName, lastName, address, phone}
	 * ?address=/firestation/{address} ?stationNumber=3
	 */
	public List<Persons> personsOfStationAdultsAndChild(String stationNumber) throws IOException;

	/*
	 * L'utilisateur accède à l’URL :
	 * http://localhost:8080/childAlert?adress=<adress>
	 * 
	 * Le système retourne une liste des enfants (<=18 ans) habitant à cette
	 * adresse. La liste doit comprendre : prénom, nom, âge et une liste des autres
	 * membres du foyer. S’il n’y a pas d’enfant, cette url peut renvoyer une chaîne
	 * vide.
	 */
	public List<Foyer> childPersonsAlertAddress(String address) throws IOException, ParseException;

}
