package com.fal.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private String name;
	private String description;
	private String admin;
	private List<User> members;
	private List<String> discussionBoard;
	
	
	/**
	* Constructor.
	* This function is called when we create an Group object
	* @since 1.0
	*/
	public Group(String name, String description, String admin) {
		super();
		this.name = name;
		this.description = description;
		this.admin = admin;
		this.members = new ArrayList<>();
		this.discussionBoard = new ArrayList<>();
	}

	/**
	* Allow to have the name of the group.
	* This function is called when we need to have the name of the required group
	* @return name of the required group
	* @since 1.0
	*/
	public String getName() {
		return name;
	}

	/**
	* Allow to set the name of a group.
	* This function is called when we need to set the name of a required group
	* @param name the name of the group
	* @since 1.0
	*/
	public void setName(String name) {
		this.name = name;
	}

	/**
	* Allow to have the description of the group.
	* This function is called when we need to have the description of the required group
	* @return description of the required group
	* @since 1.0
	*/
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	* Allow to have the admin of the group.
	* This function is called when we need to have the admin of the required group
	* @return admin of the required group
	* @since 1.0
	*/
	public String getAdmin() {
		return admin;
	}

	/**
	* Allow to set the admin of a group.
	* This function is called when we need to set the admin of a required group
	* @param admin the admin of the group
	* @since 1.0
	*/
	public void setAdmin(String admin) {
		this.admin = admin;
	}

	/**
	* Allow to have the different members of a group.
	* This function is called when we need to have the members list of the required group
	* @return list members of the required group
	* @since 1.0
	*/
	public List<User> getMembers() {
		return members;
	}

	/**
	* Allow to set the members list of a group.
	* This function is called when we need to set the members list of a required group
	* @param members the members list of the group
	* @since 1.0
	*/
	public void setMembers(List<User> members) {
		this.members = members;
	}

	/**
	* Allow to have the discussion board of the group.
	* This function is called when we need to have the list of the messages in the board of the required group
	* @return list of the messages in the board of the required group
	* @since 1.0
	*/
	public List<String> getDiscussionBoard() {
		return discussionBoard;
	}

	/**
	* Allow to set the discussion board of a group.
	* This function is called when we need to set the discussion board as a list of message of a required group
	* @param discussionBoard the discussion board as a list of message of the group
	* @since 1.0
	*/
	public void setDiscussionBoard(List<String> discussionBoard) {
		this.discussionBoard = discussionBoard;
	}
	
	

}
