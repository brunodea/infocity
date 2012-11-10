package br.ufsm.brunodea.tcc.event;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class EventItem extends OverlayItem {
	
	public enum EventType {
		ADD, UNKNOWN,
	}
	
	private EventType mType;
	private String mIdentifier;
	
	public EventItem(GeoPoint pos, String title, String snippet, EventType type) {
		super(pos, title, snippet);
		mType = type;
		mIdentifier = String.valueOf(pos.getLatitudeE6())+
				String.valueOf(pos.getLongitudeE6()) + (((int)Math.random())*1000) +
				title + type.toString();
	}
	
	public EventType getType() {
		return mType;
	}
	public String getIdentifier() {
		return mIdentifier;
	}
	
	public boolean compareIdentifiers(EventItem event_item) {
		return event_item.getIdentifier().equals(mIdentifier);
	}
}
