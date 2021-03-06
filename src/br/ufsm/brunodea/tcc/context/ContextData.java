package br.ufsm.brunodea.tcc.context;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

import com.google.gson.Gson;

public class ContextData {
	private String place_name; //nome do restaurante, nome do evento, nome do shopping, etc.
	private String place_type; //parada de ônibus, hospital, congresso, restaurante, etc.
	private String movement_state; //parado, caminhando, andando de carro, etc.
	private boolean on_commute; //esta indo de um lugar a outro.
	private String address;
	private String from_supplier; //de onde este contexto foi retirado;
	private double longitude;
	private double latitude;
	private String filter_eventtype;
	
	public ContextData() {
		place_name = "";
		place_type = "";
		movement_state = "";
		address = "";
		on_commute = false;
		longitude = -1;
		latitude = -1;
		from_supplier = "";
		filter_eventtype = "";
	}
	
	public void setPlaceName(String place_name) {
		this.place_name = place_name;
	}
	public void setPlaceType(String place_type) {
		this.place_type = place_type;
	}
	public void setMovementState(String movement_state) {
		this.movement_state = movement_state;
	}
	public void setOnCommute(boolean oncommute) {
		this.on_commute = oncommute;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setFromSupplier(String from) {
		this.from_supplier = from;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	public double getLatitude() {
		return this.latitude;
	}
	
	public void setFilterEventType(String eventtype) {
		filter_eventtype = eventtype;
	}
	
	public Location getLocation() {
		Location location = new Location(from_supplier);
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		
		return location;
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		String gson = new Gson().toJson(this);
		try {
			json = new JSONObject(gson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;
	}

	@Override
	public String toString() {
		return toJSON().toString().replace("false", "False").replace("true", "True");
	}
}
