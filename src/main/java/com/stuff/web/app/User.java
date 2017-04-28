package com.stuff.web.app;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
	
	@Id
	public String phoneNumber;
	
	@OneToMany(mappedBy = "lender")
	private Set<Item> items = new HashSet<>();
	
	public Set<Item> getItems() {
		return items;
	}
	
	public String getAccessCode() {
		return accessCode;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	@JsonIgnore
	public String accessCode;
	
	public User(String number) {
		this.phoneNumber = number;
	}
	
	User() {
	}
	
}
