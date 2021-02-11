package com.openclassrooms.safetynetalerts;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;
import com.openclassrooms.safetynetalerts.model.ChildAlert;
import com.openclassrooms.safetynetalerts.model.CommunityEmail;
import com.openclassrooms.safetynetalerts.model.FireAddress;
import com.openclassrooms.safetynetalerts.model.Foyer;
import com.openclassrooms.safetynetalerts.model.PersonInfo;
import com.openclassrooms.safetynetalerts.model.PersonsFireStation;
import com.openclassrooms.safetynetalerts.model.PhoneAlert;

public class JsonDaoImplementsTestANNUL {

	@Autowired
	private JsonDaoImplements jsonDaoImplements;

	@Test
	public void testPersonsOfStationAdultsAndChildNoFirestation() throws IOException, ParseException {

		jsonDaoImplements = new JsonDaoImplements();
		String stationNumber = "";

		List<Foyer> listFoyer = new ArrayList<>();
		listFoyer.addAll(jsonDaoImplements.personsOfStationAdultsAndChild(stationNumber));

		int decompte = Integer.parseInt(listFoyer.get(0).getDecompteAdult())
				+ Integer.parseInt(listFoyer.get(0).getDecompteChildren());
		assertThat(decompte == 0).isTrue();
	}

	@Test
	public void testPersonsOfStationAdultsAndChildWithFirestation() throws IOException, ParseException {

		jsonDaoImplements = new JsonDaoImplements();
		String stationNumber = "1";

		List<Foyer> listFoyer = new ArrayList<>();
		listFoyer.addAll(jsonDaoImplements.personsOfStationAdultsAndChild(stationNumber));

		int decompte = Integer.parseInt(listFoyer.get(0).getDecompteAdult())
				+ Integer.parseInt(listFoyer.get(0).getDecompteChildren());
		assertThat(decompte > 0).isTrue();
	}

	@Test
	public void testChildPersonsAlertAddressWithChild() throws IOException, ParseException {

		/*
		 * Le système retourne une liste des enfants (<=18 ans) habitant à cette
		 * adresse. La liste doit comprendre : prénom, nom, âge et une liste des autres
		 * membres du foyer. S’il n’y a pas d’enfant, cette url peut renvoyer une chaîne
		 * vide.
		 */

		jsonDaoImplements = new JsonDaoImplements();
		String adresse = "1509 Culver St";

		List<ChildAlert> listChildrenAlert = new ArrayList<>();
		listChildrenAlert.addAll(jsonDaoImplements.childPersonsAlertAddress(adresse));

		assertThat(!listChildrenAlert.get(0).getListChildren().get(0).getDecompte().equals("")).isTrue();

	}

	@Test
	public void testChildPersonsAlertAddressNoChild() throws IOException, ParseException {

		/*
		 * Le système retourne une liste des enfants (<=18 ans) habitant à cette
		 * adresse. La liste doit comprendre : prénom, nom, âge et une liste des autres
		 * membres du foyer. S’il n’y a pas d’enfant, cette url peut renvoyer une chaîne
		 * vide.
		 */

		jsonDaoImplements = new JsonDaoImplements();
		String adresse = "";

		List<ChildAlert> listChildrenAlert = new ArrayList<>();
		listChildrenAlert.addAll(jsonDaoImplements.childPersonsAlertAddress(adresse));

		assertThat(listChildrenAlert.size() == 0).isTrue();

	}

	@Test
	public void testPhoneAlertFirestation() throws IOException, ParseException {

		/*
		 * Le système retourne une liste des numéros de téléphone des résidents
		 * desservis par la caserne de pompiers.
		 */

		jsonDaoImplements = new JsonDaoImplements();
		String stationNumber = "1";

		List<PhoneAlert> listPhoneAlert = new ArrayList<>();
		listPhoneAlert.addAll(jsonDaoImplements.phoneAlertFirestation(stationNumber));

		assertThat(listPhoneAlert.size() > 0).isTrue();

	}

	@Test
	public void testFireAddress() throws IOException, ParseException {

		/*
		 * Le système retourne une liste des habitants vivants à l’adresse donnée ainsi
		 * que le numéro de la caserne de pompiers la desservant. La liste doit inclure
		 * : le nom, le numéro de téléphone, l’âge et les antécédents médicaux
		 * (médicaments, posologie et allergies) de chaque personne.
		 */

		jsonDaoImplements = new JsonDaoImplements();
		String adresse = "1509 Culver St";

		List<FireAddress> listFireAddress = new ArrayList<>();
		listFireAddress.addAll(jsonDaoImplements.fireAddress(adresse));

		assertThat(!listFireAddress.get(0).getFirestation().isEmpty()).isTrue();

	}

	@Test
	public void testFireAddressNoAddress() throws IOException, ParseException {

		/*
		 * Le système retourne une liste des habitants vivants à l’adresse donnée ainsi
		 * que le numéro de la caserne de pompiers la desservant. La liste doit inclure
		 * : le nom, le numéro de téléphone, l’âge et les antécédents médicaux
		 * (médicaments, posologie et allergies) de chaque personne.
		 */

		jsonDaoImplements = new JsonDaoImplements();
		String adresse = "";

		List<FireAddress> listFireAddress = new ArrayList<>();
		listFireAddress.addAll(jsonDaoImplements.fireAddress(adresse));

		assertThat(listFireAddress.size() == 0).isTrue();

	}

	@Test
	public void testStationListFirestation() throws IOException, ParseException {

		/*
		 * Le système retourne une liste de tous les foyers desservis par la caserne.
		 * Cette liste doit regrouper les personnes par adresse. La liste doit inclure :
		 * le nom, le numéro de téléphone et l’âge des habitants et faire figurer les
		 * antécédents médicaux (médicaments, posologie et allergies) à côté de chaque
		 * nom.
		 */

		jsonDaoImplements = new JsonDaoImplements();
		List<String> station = new ArrayList<>();
		station.add("1");
		station.add("3");

		List<PersonsFireStation> listPersonsFireStation = new ArrayList<>();
		listPersonsFireStation.addAll(jsonDaoImplements.stationListFirestation(station));

		assertThat(listPersonsFireStation.size() > 0).isTrue();

	}

	@Test
	public void testPersonInfo() throws IOException, ParseException {

		/*
		 * Le système retourne le nom, l’adresse, l’âge, l’adresse mail et les
		 * antécédents médicaux (médicaments, posologie et allergies) de chaque
		 * habitant. Si plusieurs personnes portent le même nom, elles doivent toutes
		 * apparaître.
		 */

		jsonDaoImplements = new JsonDaoImplements();
		String firstName = "John";
		String lastName = "Boyd";

		List<PersonInfo> listPersonInfo = new ArrayList<>();
		listPersonInfo.addAll(jsonDaoImplements.personInfo(firstName, lastName));

		assertThat(listPersonInfo.size() > 0).isTrue();

	}

	@Test
	public void testCommunityEmail() throws IOException, ParseException {

		/*
		 * Le système retourne les adresses mail de tous les habitants de la ville.
		 */

		jsonDaoImplements = new JsonDaoImplements();
		String city = "Culver";

		List<CommunityEmail> listCommunityEmail = new ArrayList<>();
		listCommunityEmail.addAll(jsonDaoImplements.communityEmail(city));

		assertThat(listCommunityEmail.size() > 0).isTrue();

	}
}
