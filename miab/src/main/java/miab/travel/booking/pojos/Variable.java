package miab.travel.booking.pojos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Variable implements Serializable {

	public Map<String, Object> vars;
	public StringBuilder logger;
	
	public Variable() {
		this.vars = new HashMap<String, Object>();
		this.logger = new StringBuilder();
	}
	
	public boolean getBooleanValue(String key) {
		Boolean b = (Boolean)vars.get(key);
		return b.booleanValue();
	}
}
