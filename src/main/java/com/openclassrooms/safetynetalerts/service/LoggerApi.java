package com.openclassrooms.safetynetalerts.service;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class LoggerApi {

	public String loggerInfo(HttpServletRequest request) {

		Enumeration<?> entetes_headers = request.getHeaderNames();
		String entetesHeaders = "{";
		while (entetes_headers.hasMoreElements()) {
			String nomEntete = (String) entetes_headers.nextElement();
			entetesHeaders += "\"" + nomEntete + "\": \"" + request.getHeader(nomEntete) + "\"";
			if (entetes_headers.hasMoreElements()) {
				entetesHeaders += ", ";
			} else
				entetesHeaders += "}";
		}

		String log_info = "\r\nRequest Method: <[" + request.getMethod() + "]>" + " " + request.getRequestURI()
				+ "\r\nRequest URL: " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
				+ "\r\nContent Type: " + request.getContentType() + "\r\nHeader Name: " + entetesHeaders;

		return log_info;
	}

}
