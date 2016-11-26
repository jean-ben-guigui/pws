package com.fal.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private String name;
	private String description;
	private String admin;
	private List<User> members;
	private List<String> discussionBoard;
	
	

	public Group(String name, String description, String admin) {
		super();
		this.name = name;
		this.description = description;
		this.admin = admin;
		this.members = new ArrayList<>();
		this.discussionBoard = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public List<String> getDiscussionBoard() {
		return discussionBoard;
	}

	public void setDiscussionBoard(List<String> discussionBoard) {
		this.discussionBoard = discussionBoard;
	}
	
	

}
