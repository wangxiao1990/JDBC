package jdbc;

import java.sql.Date;

public class Invoice {
	private int id;
	private Date shippedOn;
	
	public Invoice() {}
	public Invoice(int id, Date shippedOn) {
		super();
		this.id = id;
		this.shippedOn = shippedOn;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public Date getShippedOn() {
		return shippedOn;
	}
	
	public void setShippedOn(Date shippedOn) {
		this.shippedOn = shippedOn;
	}
}
