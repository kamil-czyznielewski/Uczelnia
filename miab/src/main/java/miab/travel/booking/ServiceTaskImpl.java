package miab.travel.booking;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import miab.travel.booking.pojos.Car;
import miab.travel.booking.pojos.CreditCard;
import miab.travel.booking.pojos.Hotel;
import miab.travel.booking.pojos.Variable;

public class ServiceTaskImpl {

	public Variable checkCreditCard(Variable v) {

		try {
			CreditCard card = (CreditCard)v.vars.get("credit_card");
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","postgres");
			try {
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM credit_cards WHERE owner = ?");
				stmt.setString(1, card.getOwner());
				
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					card.setValid(true);
					v.logger.append("Card -> " + card.toString() + " is valid credit card!");
				} else {
					card.setValid(false);
					v.logger.append("Card -> " + card.toString() + " is not valid credit card!");
				}
				
			}
			finally {
				v.vars.put("credit_card", card);
				conn.close();
			}
		} catch(Exception e) {
			v.logger.append("\n----\tERROR\t----\n");
			v.logger.append(e.getMessage() + "\n");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			v.logger.append(errors.toString());
			throw new RuntimeException("ERROR");
		}
		return v;
	}

	
	public Variable carReservation(Variable v) {
		try {
			String tourName = (String) v.vars.get("tour");
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","postgres");
			int idTour = 0;
			Car car = new Car();
			try {
				PreparedStatement stmt = conn.prepareStatement("SELECT id FROM tours WHERE place = ?");
				stmt.setString(1, tourName);
				
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					idTour = rs.getInt("id");
				}
				
				stmt = conn.prepareStatement("SELECT * FROM cars_rental WHERE tour = ?");
				stmt.setInt(1, idTour);
				
				rs = stmt.executeQuery();
				while(rs.next()) {
					car.setName(rs.getString("car_name"));
					car.setPrice(rs.getFloat("price"));
				}
				
			}
			finally {
				v.vars.put("car", car);
				conn.close();
			}
		} catch(Exception e) {
			v = new Variable();
			v.logger.append("\n----\tERROR\t----\n");
			v.logger.append(e.getMessage() + "\n");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			v.logger.append(errors.toString());
			throw new RuntimeException("ERROR");
		}
		
		return v;
	}
	
	
}
