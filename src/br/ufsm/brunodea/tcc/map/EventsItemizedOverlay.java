package br.ufsm.brunodea.tcc.map;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class EventsItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> mEventOverlays;
	
	public EventsItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker, mapView);
		mEventOverlays = new ArrayList<OverlayItem>();
	}

	public void addEvent(OverlayItem event_overlay) {
		mEventOverlays.add(event_overlay);
		populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return mEventOverlays.get(i);
	}

	@Override
	public int size() {
		return mEventOverlays.size();
	}
	
	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		return true;
	}
}
