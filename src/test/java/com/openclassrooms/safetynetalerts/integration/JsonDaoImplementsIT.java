package com.openclassrooms.safetynetalerts.integration;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;
import com.openclassrooms.safetynetalerts.dao.ReadJsonFile;
import com.openclassrooms.safetynetalerts.model.Persons;
import com.openclassrooms.safetynetalerts.service.JsonPathFileToWriter;

@ExtendWith(MockitoExtension.class)
public class JsonDaoImplementsIT {

	@Mock
	private JsonPathFileToWriter jsonPathFileToWriter;
	private ReadJsonFile readJsonFile;

	// @MockBean
	// private ReadJsonFile readJsonFile;

	@Test
	public void testPostAPerson() throws IOException {

		// when(jsonPathFileToWriter.jsonPathFileToWriter()).thenReturn(new
		// FileWriter("data/TestdbJSON.json"));
		Persons persons = new Persons();
		persons.setFirstName("TestFirstName91");
		persons.setLastName("TestLastName91");
		List<Persons> listPersons = new ArrayList<>();
		listPersons.add(persons);
		readJsonFile = new ReadJsonFile();
		when(readJsonFile.readfilejsonPersons()).thenReturn(listPersons);
		JsonDaoImplements jsonDaoImplements = new JsonDaoImplements();
		// when(jsonDaoImplements.addPerson(persons)).thenReturn(persons);
		jsonDaoImplements.addPerson(persons);

		// assertThat(fileWriter.equals(fileWriter)).isTrue();
	}

}
