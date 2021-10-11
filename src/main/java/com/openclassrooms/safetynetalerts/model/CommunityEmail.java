package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class CommunityEmail {

	private List<String> listEmails = new ArrayList<>();

	public CommunityEmail() {
		super();
	}

	public CommunityEmail(List<String> listEmails) {
		super();
		this.listEmails = listEmails;
	}

	public List<String> getListEmails() {
		return listEmails;
	}

	public void setListEmails(List<String> listEmails) {
		this.listEmails = listEmails;
	}

	@Override
	public String toString() {
		return "CommunityEmail [listEmails=" + listEmails + "]";
	}

}
