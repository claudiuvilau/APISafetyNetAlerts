package com.openclassrooms.safetynetalerts.service;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class LoggerApi {

	public static void setLoggerForTests() {
		System.setProperty("log4j.configurationFile", "log4j2-test.xml");
	}

	public static void setLogger() {
		System.setProperty("log4j.configurationFile", "log4j2.xml");
	}

	public String loggerInfo(HttpServletRequest request, HttpServletResponse response, String param) {

		String log_info = "\r\nRequest Method: <[" + request.getMethod() + "]>" + " " + request.getRequestURI()
				+ "\r\nRequest URL: " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString();

		if (System.getProperty("log4j.configurationFile") == ("log4j2-test.xml")) {

			log_info += " @Param/Path: " + request.getParameterMap().keySet() + "=[" + param + "]"
					+ "\r\nResponse Code: " + response.getStatus();

		} else {
			Enumeration<?> entetes_headers = request.getHeaderNames();
			String entetesHeaders = "{";
			while (entetes_headers.hasMoreElements()) {
				String nomEntete = (String) entetes_headers.nextElement();
				entetesHeaders += "\"" + nomEntete + "\": \"" + request.getHeader(nomEntete) + "\"";
				if (entetes_headers.hasMoreElements()) {
					entetesHeaders += ", ";
				}
			}
			entetesHeaders += "}";

			log_info += "\r\nContent Type: " + request.getContentType() + "\r\nResponse Code: " + response.getStatus()
					+ "\r\nHeader Name: " + entetesHeaders;
		}

		return log_info;

	}

	public String loggerErr(Exception e, String param) {

		String log_err = "";

		if (System.getProperty("log4j.configurationFile") == ("log4j2-test.xml")) {
			log_err = "Request URL: " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
					+ " @Param/Path: [" + param + "]";
		} else {
			log_err = "\r\nRequest URL: " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
					+ "\r\nException: " + e;
		}

		return log_err;

	}

	public String loggerDebug(String param) {

		String log_deb = "";

		if (System.getProperty("log4j.configurationFile") == ("log4j2-test.xml")) {
			log_deb = "Request URL: " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
					+ " @Param/Path: [" + param + "]";
		} else {
			log_deb = "Request URL: " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
		}

		return log_deb;
	}
}
