package de.freewarepoint.whohasmystuff.database;

import java.util.Date;

public class Item {

	private User lender;

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

    public Date getDateCreated() { return dateCreated; }

    public Date getDateModified() { return dateModified; }

    public boolean updateItem(Long id, String lendeeNumber, User lender, String itemType, String description) {
        this.lendeeNumber = lendeeNumber;
        this.lender = lender;
        this.itemType = itemType;
        this.description = description;
        this.dateModified = new Date();
        return true;
    }
    
	public Item(User lend, String number, String type, String desc) {
		this.lender =  lend;
		this.lendeeNumber = number;
		this.itemType = type;
		this.description = desc;
        this.dateCreated = new Date();
        this.dateModified = this.dateCreated;
	}

	public Item(Long id, User lend, String number, String type, String desc,  Date dateCreated, Date dateModified) {
        this.id = id;
        this.lender =  lend;
        this.lendeeNumber = number;
        this.itemType = type;
        this.description = desc;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }
	
	Item() {
	}
}
