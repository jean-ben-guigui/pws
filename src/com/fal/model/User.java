package com.fal.model;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String email;
	private String firstname;
	private String lastname;
	private String biography;
	private List<Group> groups;
	

	

	public User() {
		super();
	}

	/**
	* Constructor.
	* This function is called when we create a Group object
	* @since 1.0
	*/
	public User(String email, String firstname, String lastname, String biography) {
		super();
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.biography = biography;
		this.groups = new ArrayList<>();
	}

	/**
	* Allow to have the email of the user.
	* This function is called when we need to have the email of the required user
	* @return email of the required group
	* @since 1.0
	*/
	public String getEmail() {
		return email;
	}

	/**
	* Allow to set the email of the user.
	* This function is called when we need to set the email of the user
	* @param email the email of the user
	* @since 1.0
	*/
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	* Allow to have the first name of the user.
	* This function is called when we need to have the first name of the required user
	* @return firstname of the required group
	* @since 1.0
	*/
	public String getFirstname() {
		return firstname;
	}

	/**
	* Allow to set the first name of the user.
	* This function is called when we need to set the first name of the user
	* @param firstname the firstname of the user
	* @since 1.0
	*/
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	* Allow to have the last name of the user.
	* This function is called when we need to have the last name of the required user
	* @return lastname of the required group
	* @since 1.0
	*/
	public String getLastname() {
		return lastname;
	}

	/**
	* Allow to set the last name of the user.
	* This function is called when we need to set the last name of the user
	* @param lastname the last name of the user
	* @since 1.0
	*/
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	/**
	* Allow to have the biography of the user.
	* This function is called when we need to have the biography of the required user
	* @return biography of the required group
	* @since 1.0
	*/
	public String getBiography() {
		return biography;
	}

	/**
	* Allow to set the biography of the user.
	* This function is called when we need to set the biography of the user
	* @param biography the biography of the user
	* @since 1.0
	*/
	public void setBiography(String biography) {
		this.biography = biography;
	}

	/**
	* Allow to have the group list of the user.
	* This function is called when we need to have the different groups where the user is
	* @return groups of the user
	* @since 1.0
	*/
	public List<Group> getGroups() {
		return groups;
	}

	/**
	* Allow to set the groups list of the user.
	* This function is called when we need to register the user in the different groups
	* @param groups the groups list of the user
	* @since 1.0
	*/
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	
	
	

}
