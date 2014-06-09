package com.check.cool_model;


import com.google.appengine.api.datastore.Entity;

public class Stamp {
	
	public static final String NAME = "name";
	public static final String DATE = "date";
	public static final String ALT = "alt";
	public static final String LON = "lon";
	
	private String name;
	private String date;
	private double alt;
	private double lon;

	public static Stamp build(Entity result) {
		Stamp list = new Stamp();
		list.name = (String)result.getProperty(NAME);
		list.date = (String)result.getProperty(DATE);
		list.alt = (double)result.getProperty(ALT);
		list.lon = (double)result.getProperty(LON);
		return list;
	}

	public String getName() {
		return name;
	}

	public String getDate() {
		return date;
	}

	public double getAlt() {
		return alt;
	}

	public double getLon() {
		return lon;
	} 
	
}
