package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class ChildAlert {

	private List<Children> listChildren = new ArrayList<>();
	private List<Children> listAdult = new ArrayList<>();

	public ChildAlert() {
		super();
	}

	public ChildAlert(List<Children> listChildren, List<Children> listAdult) {
		super();
		this.listChildren = listChildren;
		this.listAdult = listAdult;
	}

	public List<Children> getListChildren() {
		return listChildren;
	}

	public void setListChildren(List<Children> listChildren) {
		this.listChildren = listChildren;
	}

	public List<Children> getListAdult() {
		return listAdult;
	}

	public void setListAdult(List<Children> listAdult) {
		this.listAdult = listAdult;
	}

	@Override
	public String toString() {
		return "ChildAlert [listChildren=" + listChildren + ", listAdult=" + listAdult + "]";
	}

}
