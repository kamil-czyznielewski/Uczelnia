package miab.travel.booking.pojos;

import java.io.Serializable;

public class CreditCard implements Serializable {

	private String owner;
	private float balance;
	private String name;
	private boolean valid;
	
	public CreditCard(){}
	
	public CreditCard(String owner, float balance, String name) {
		super();
		this.owner = owner;
		this.balance = balance;
		this.name = name;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	@Override
	public String toString() {
		return "[ Owner: " +this.owner + " | Name: " +this.name + " ]";
		
	}
	
	
}
