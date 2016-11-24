package com.duver_flament.model;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name="currency")
@XmlAccessorType(XmlAccessType.FIELD)
public class Currency {
	private int id;
	private String name;
	private String country;
	private int yearAdopted;
	
	
	public Currency() {
		super();
	}
	public Currency( String country,String name, int yearAdopted,int id) {
		super();
		this.id = id;
		this.name = name;
		this.country = country;
		this.yearAdopted = yearAdopted;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getYearAdopted() {
		return yearAdopted;
	}
	public void setYearAdopted(int yearAdopted) {
		this.yearAdopted = yearAdopted;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
