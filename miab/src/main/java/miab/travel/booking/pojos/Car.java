package miab.travel.booking.pojos;

import java.io.Serializable;

public class Car implements Serializable {

	private String name;
	private float price;
	
	public Car() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Car(String name, float price) {
		super();
		this.name = name;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
	
}
