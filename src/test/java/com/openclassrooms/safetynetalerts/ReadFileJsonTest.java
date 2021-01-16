package com.openclassrooms.safetynetalerts;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.openclassrooms.safetynetalerts.dao.ReadJsonFile;

public class ReadFileJsonTest {

	@Test
	public void testReadFileJson() throws IOException {

		ReadJsonFile readJsonFile = new ReadJsonFile();
		readJsonFile.readfilejsonPersons();
		readJsonFile.readfilejsonFirestations();
		readJsonFile.readfilejsonMedicalrecords();
	}
}
