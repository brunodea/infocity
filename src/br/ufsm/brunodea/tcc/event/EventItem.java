package br.ufsm.brunodea.tcc.event;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class EventItem extends OverlayItem {
	
	public enum EventType {
		ADD, UNKNOWN,
	}
	
	private EventType mType;
	
	public EventItem(GeoPoint pos, String title, String snippet, EventType type) {
		super(pos, title, snippet);
		mType = type;
	}
	
	public EventType getType() {
		return mType;
	}
}
