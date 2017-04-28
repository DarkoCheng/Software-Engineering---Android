package com.stuff.web.app;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Item {

	@JsonIgnore
	@ManyToOne
	private User lender;

	@Id
	@GeneratedValue
	private Long id;
	
	private String lendeeNumber;
	private String itemType;
	private String description;
	private Date dateCreated;
	private Date dateModified;
	
	public User getLender() {
        return this.lender;
    }

    public Long getId() {
        return this.id;
    }

    public String getLendee() {
        return this.lendeeNumber;
    }
    
    public String getType() {
    	return this.itemType;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateCreated() {
    	return dateCreated;
    }
    
    public Date getDateModified() {
    	return dateModified;
    }
	
    public boolean updateItem(Long id, String lendeeNumber, User lender, String itemType, String description) {
    		this.id = id;
    		this.lendeeNumber = lendeeNumber;
    		this.lender = lender;
    		this.itemType = itemType;
    		this.description = description;
    		this.dateModified = new Date();
    		return true;
    }
    
	public Item(User lend, String number, String type, String desc, Date dateCreated) {
		this.lender =  lend;
		this.lendeeNumber = number;
		this.itemType = type;
		this.description = desc;
		this.dateCreated = dateCreated;
		this.dateModified = new Date();
	}
	
	//For overwriting the lend object with an empty one only holding a phone number. Prevents circular arrays or escape of unnecessary data.
	public Item(User lend, Item item) {
		this.id = item.getId();
		this.lender =  lend;
		this.lendeeNumber = item.getLendee();
		this.itemType = item.getType();
		this.description = item.getDescription();
		this.dateCreated = item.getDateCreated();
		this.dateModified = item.getDateModified();
	}
	
	Item() {
	}
}
