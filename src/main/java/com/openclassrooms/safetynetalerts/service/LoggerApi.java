package com.openclassrooms.safetynetalerts.service;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class LoggerApi {

	public String loggerInfo(HttpServletRequest request, HttpServletResponse response) {

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

		String log_info = "\r\nRequest Method: <[" + request.getMethod() + "]>" + " " + request.getRequestURI()
				+ "\r\nRequest URL: " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
				+ "\r\nContent Type: " + request.getContentType() + "\r\nResponse Code: " + response.getStatus()
				+ "\r\nHeader Name: " + entetesHeaders;

		return log_info;
	}

	public String loggerErr(Exception e) {

		String log_err = "\r\nRequest URL: " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
				+ "\r\nException: " + e;

		return log_err;

	}
	
	public String loggerDebug() {

		String log_deb = "Request URL: " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString();

		return log_deb;
	}
}
