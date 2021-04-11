package com.openclassrooms.safetynetalerts.dao;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.safetynetalerts.model.Persons;
import com.openclassrooms.safetynetalerts.service.FilterJsons;

@ExtendWith(MockitoExtension.class)
public class JsonDaoImplementsTest {

	JsonDaoImplements jsonDaoImplements = new JsonDaoImplements();

	@Mock
	FilterJsons filterJsons = new FilterJsons();

	@Test
	public void testfireAddress() {

		filterJsons = new FilterJsons();

		String address = "TestAdresse";
		List<Persons> listPersons = new ArrayList<>();
		Persons persons = new Persons();
		persons.setAddress(address);
		persons.setCity(address);
		persons.setEmail(address);
		persons.setFirstName(address);
		persons.setLastName(address);
		persons.setPhone(address);
		persons.setZip(address);
		listPersons.add(persons);

		when(filterJsons.filterAddressInPersons(address)).thenReturn(listPersons);
		jsonDaoImplements.fireAddress(address);

	}

}
