package com.openclassrooms.safetynetalerts.service;

import java.io.FileWriter;
import java.io.IOException;

import com.openclassrooms.safetynetalerts.dao.ReadJsonFile;

public class JsonPathFileToWriter {

	public FileWriter jsonPathFileToWriter() throws IOException {

		ReadJsonFile readJsonFile = new ReadJsonFile();
		FileWriter fileWriter = new FileWriter(readJsonFile.filepath_json);

		return fileWriter;
	}
}
