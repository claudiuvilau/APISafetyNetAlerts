package com.openclassrooms.safetynetalerts.dao;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.openclassrooms.safetynetalerts.model.ChildAlert;
import com.openclassrooms.safetynetalerts.model.Children;
import com.openclassrooms.safetynetalerts.model.CommunityEmail;
import com.openclassrooms.safetynetalerts.model.FireAddress;
import com.openclassrooms.safetynetalerts.model.Firestations;
import com.openclassrooms.safetynetalerts.model.Foyer;
import com.openclassrooms.safetynetalerts.model.PersonInfo;
import com.openclassrooms.safetynetalerts.model.Persons;
import com.openclassrooms.safetynetalerts.model.PersonsFireStation;
import com.openclassrooms.safetynetalerts.model.PhoneAlert;

public interface JsonDao {

	public List<Persons> findAddressInPersons(String jsonStream, String address) throws IOException;

	public List<Firestations> findAddressInFirestations(List<?> listFireStations, String address) throws IOException;;

	public List<Firestations> filterStation(String stationNumber);

	public List<Persons> filterAddressInPersons(String address) throws IOException;

	public List<Children> findOld(int old) throws IOException, ParseException;

	public List<Children> listFindOld(List<?> list, int old) throws IOException, ParseException;

	/*
	 * L'utilisateur accède à l’URL :
	 * 
	 * http://localhost:9090/firestation?stationNumber=<station_number>
	 * 
	 * Le système retourne une liste des personnes (prénom, nom, adresse, numéro de
	 * téléphone) couvertes par la caserne de pompiers correspondante ainsi qu’un
	 * décompte du nombre d’adultes (>18 ans) et du nombre d’enfants (<=18 ans)
	 * 
	 */
	public List<Foyer> personsOfStationAdultsAndChild(String stationNumber) throws IOException, ParseException;

	/*
	 * L'utilisateur accède à l’URL :
	 *
	 * http://localhost:9090/childAlert?address=<address>
	 * 
	 * Le système retourne une liste des enfants (<=18 ans) habitant à cette
	 * adresse. La liste doit comprendre : prénom, nom, âge et une liste des autres
	 * membres du foyer. S’il n’y a pas d’enfant, cette url peut renvoyer une chaîne
	 * vide.
	 */
	public List<ChildAlert> childPersonsAlertAddress(String address) throws IOException, ParseException;

	/*
	 * L'utilisateur accède à l’URL :
	 *
	 * http://localhost:9090/phoneAlert?firestation=< firestation _number>
	 *
	 * Le système retourne une liste des numéros de téléphone des résidents
	 * desservis par la caserne de pompiers.
	 */
	public List<PhoneAlert> phoneAlertFirestation(String stationNumber) throws IOException;

	/*
	 * L'utilisateur accède à l’URL :
	 * 
	 * http://localhost:9090/fire?address=<address>
	 * 
	 * Le système retourne une liste des habitants vivants à l’adresse donnée ainsi
	 * que le numéro de la caserne de pompiers la desservant. La liste doit inclure
	 * : le nom, le numéro de téléphone, l’âge et les antécédents médicaux
	 * (médicaments, posologie et allergies) de chaque personne.
	 * 
	 */
	public List<FireAddress> fireAddress(String address) throws IOException, ParseException;

	/*
	 * L'utilisateur accède à l’URL :
	 * 
	 * http://localhost:9090/flood/station?station=<a list of station_numbers>
	 * 
	 * Le système retourne une liste de tous les foyers desservis par la caserne.
	 * Cette liste doit regrouper les personnes par adresse. La liste doit inclure :
	 * le nom, le numéro de téléphone et l’âge des habitants et faire figurer les
	 * antécédents médicaux (médicaments, posologie et allergies) à côté de chaque
	 * nom.
	 * 
	 */
	public List<PersonsFireStation> stationListFirestation(List<String> station) throws IOException, ParseException;

	/*
	 * L'utilisateur accède à l’URL :
	 * 
	 * http://localhost:9090/personInfo?firstName=<firstName>&lastName=<lastName>
	 * 
	 * Le système retourne le nom, l’adresse, l’âge, l’adresse mail et les
	 * antécédents médicaux (médicaments, posologie et allergies) de chaque
	 * habitant. Si plusieurs personnes portent le même nom, elles doivent toutes
	 * apparaître.
	 * 
	 */
	public List<PersonInfo> personInfo(String firstName, String lastName) throws IOException, ParseException;

	/*
	 * L'utilisateur accède à l’URL :
	 * 
	 * http://localhost:9090/communityEmail?city=<city>
	 * 
	 * Le système retourne les adresses mail de tous les habitants de la ville.
	 * 
	 */
	public List<CommunityEmail> communityEmail(String city) throws IOException;

	/*
	 * http://localhost:9090/person Cet endpoint permettra d’effectuer les actions
	 * suivantes via Post/Put/Delete avec HTTP : ● ajouter une nouvelle personne ; ●
	 * mettre à jour une personne existante (pour le moment, supposons que le prénom
	 * et le nom de famille ne changent pas, mais que les autres champs peuvent être
	 * modifiés) ; ● supprimer une personne (utilisez une combinaison de prénom et
	 * de nom comme identificateur unique).
	 * 
	 */
	public void ajouterPerson(Persons persons) throws IOException;
}
