package com.openclassrooms.safetynetalerts;

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.openclassrooms.safetynetalerts.dao.ReadJsonFile;

public class ReadFileJsonTest {

	@Test
	public void testReadFileJson() {
		ReadJsonFile rjf = new ReadJsonFile();
		String line = "";
		try {
			line = rjf.readfilejson();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(line);
		assertNotEquals(line, "");
	}

}
