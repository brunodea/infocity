package br.ufsm.brunodea.tcc.map;

import java.util.ArrayList;

import android.content.Context;
import br.ufsm.brunodea.tcc.event.EventItem;
import br.ufsm.brunodea.tcc.event.EventItem.EventType;
import br.ufsm.brunodea.tcc.util.Util;

import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class EventsItemizedOverlay extends BalloonItemizedOverlay<EventItem> {
	private ArrayList<EventItem> mEventOverlays;
	
	public EventsItemizedOverlay(Context c, EventType type, MapView mapView) {
		super(boundCenter(Util.getEventTypeMarker(c, type)), mapView);
		mEventOverlays = new ArrayList<EventItem>();
	}

	public void addEventItem(EventItem eventitem) {
		mEventOverlays.add(eventitem);
		populate();
	}
	
	@Override
	protected EventItem createItem(int i) {
		return mEventOverlays.get(i);
	}

	@Override
	public int size() {
		return mEventOverlays.size();
	}
	
	@Override
	protected boolean onBalloonTap(int index, EventItem item) {
		return true;
	}
}
