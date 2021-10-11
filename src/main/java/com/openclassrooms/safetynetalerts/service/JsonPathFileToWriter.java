package com.openclassrooms.safetynetalerts.service;

import java.io.FileWriter;
import java.io.IOException;

public class JsonPathFileToWriter {

	public FileWriter jsonPathFileToWriter() throws IOException {

		ReadJsonFile readJsonFile = new ReadJsonFile();
		FileWriter fileWriter = new FileWriter(readJsonFile.filepath_json);

		return fileWriter;
	}
}
