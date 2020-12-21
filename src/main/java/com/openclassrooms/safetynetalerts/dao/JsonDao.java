package com.openclassrooms.safetynetalerts.dao;

import org.json.simple.JSONObject;

public interface JsonDao {

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
	public JSONObject createPersonsCaserne(String collection, String caserne);

}
