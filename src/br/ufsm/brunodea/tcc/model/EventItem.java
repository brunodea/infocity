package br.ufsm.brunodea.tcc.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufsm.brunodea.tcc.util.Util;

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
	
	private class EventJSON {
		private String title;
		private String description;
		private String pub_date;
		private String geo_coord;
		
		public EventJSON(String title, String descr, String pubdate, String coord) {
			this.title = title;
			this.description = descr;
			this.pub_date = pubdate;
			this.geo_coord = coord;
		}
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		EventJSON event = new EventJSON(mTitle, mSnippet, 
				new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(mPubDate), 
				getPoint().toString());
		
		JSONObject json = Util.toJSON("event", event);

		return json;
	}

	@Override
	public List<NameValuePair> getListNameValuePair() throws JSONException {
		JSONObject json = toJSON();
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("event", "["+json.toString()+"]"));
		params.add(new BasicNameValuePair("event_keywords", mKeywords.toString()));
		params.add(new BasicNameValuePair("event_type", mType.toString()));
		
		return params;
	}
}
