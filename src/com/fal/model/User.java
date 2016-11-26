package com.fal.model;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String email;
	private String firstname;
	private String lastname;
	private String biography;
	private List<Group> groups;
	
	
	

	


	public User(String email, String firstname, String lastname, String biography) {
		super();
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.biography = biography;
		this.groups = new ArrayList<>();
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getBiography() {
		return biography;
	}


	public void setBiography(String biography) {
		this.biography = biography;
	}


	public List<Group> getGroups() {
		return groups;
	}


	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	
	
	

}
