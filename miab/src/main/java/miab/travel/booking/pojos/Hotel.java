package miab.travel.booking.pojos;

import java.io.Serializable;

public class Hotel implements Serializable {

	private String hotelName;
	private float price;	
	private HotelState state;
	
	public Hotel() {}
	
	public Hotel(String hotelName, float hotel) {
		super();
		this.hotelName = hotelName;
		this.price = hotel;
	}
	
	
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
}
