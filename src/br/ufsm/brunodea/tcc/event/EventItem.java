package br.ufsm.brunodea.tcc.event;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class EventItem extends OverlayItem {
	
	public enum EventType {
		ADD, UNKNOWN,
	}
	
	private EventType mType;
	private ArrayList<String> mKeywords;
	
	public EventItem(GeoPoint pos, String title, String snippet, EventType type) {
		super(pos, title, snippet);
		mType = type;
		mKeywords = new ArrayList<String>();
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
}
