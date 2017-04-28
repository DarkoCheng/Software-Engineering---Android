package de.freewarepoint.whohasmystuff.database;

import java.util.HashSet;
import java.util.Set;

public class User {

	public String phoneNumber;

	private Set<Item> items = new HashSet<Item>();
	
	public Set<Item> getItems() {
		return items;
	}
	
	public String getAccessCode() {
		return accessCode;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String accessCode;
	
	public User(String number) {
		this.phoneNumber = number;
	}
	
	User() {
	}
	
}
