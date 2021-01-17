package com.openclassrooms.safetynetalerts.model;

import java.util.ArrayList;
import java.util.List;

public class Children {

	private String decompte;
	private List<Persons> child = new ArrayList<>();
	private String old;

	public Children() {

	}

	public Children(String decompte, String old, List<Persons> child) {
		super();
		this.decompte = decompte;
		this.old = old;
		this.child = child;
	}

	public String getDecompte() {
		return decompte;
	}

	public void setDecompte(String decompte) {
		this.decompte = decompte;
	}

	public String getOld() {
		return old;
	}

	public void setOld(String old) {
		this.old = old;
	}

	public List<Persons> getChild() {
		return child;
	}

	public void setChild(List<Persons> child) {
		this.child = child;
	}

	@Override
	public String toString() {
		return "Children [decompte=" + decompte + ", old=" + old + ", child=" + child + "]";
	}

}
