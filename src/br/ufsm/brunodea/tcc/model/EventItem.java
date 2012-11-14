package br.ufsm.brunodea.tcc.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

public class EventItem extends Model {
	
	public enum EventType {
		ADD, UNKNOWN,
	}
	
	private EventType mType;
	private ArrayList<String> mKeywords;
	private Date mPubDate;
	
	public EventItem(GeoPoint pos, String title, String snippet, EventType type) {
		super(pos, title, snippet);
		mType = type;
		mKeywords = new ArrayList<String>();
		mPubDate = new Date();
	}
	
	public EventType getType() {
		return mType;
	}
	public ArrayList<String> getKeywords() {
		return mKeywords;
	}
	public String keywordsToString() {
		String kws = "";
		for(int i = 0; i < mKeywords.size(); i++) {
			kws += mKeywords.get(i);
			if(i + 1 < mKeywords.size()) {
				kws += ", ";
			}
		}
		
		return kws;
	}
	public void setKeywords(ArrayList<String> keywords) {
		mKeywords = keywords;
	}
	public void setPubDate(Date date) {
		mPubDate = date;
	}
	public Date getPubDate() {
		return mPubDate;
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		
		String json = "{\"pk\": null, \"model\": \"events.event\", \"fields\": " +
				"{" +
				"\"title\":\"" + getTitle() + "\"," +
				"\"description\":\"" + getSnippet() + "\"," +
				"\"geo_coord\":\"" + getPoint().toString() + "\"," +
				"\"pub_date\":\"" + sdf.format(mPubDate) + "\"," +
				"\"event_type\":" + eventTypeToJSON(mType) + ",";
		if(mKeywords.size() > 0) {
			json += "\"keywords\": {";
			for(int i = 0; i < mKeywords.size(); i++) {
				json += "\"" + i + "\": \"" + mKeywords.get(i) + "\"";
				if(i + 1 < mKeywords.size()) {
					json += ",";
				}
			}
			json += "}";
		}
		json += "}}";
		return new JSONObject(json);
	}
	
	public static JSONObject eventTypeToJSON(EventType event_type) throws JSONException {
		String json = "{\"pk\": null, \"model\": \"events.eventtype\", \"fields\": " +
				"{" +
				"\"name\":\"" + event_type.toString() + "\"" + 
				"}}";
		
		return new JSONObject(json);
	}
}
