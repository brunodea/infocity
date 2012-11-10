package br.ufsm.brunodea.tcc.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import br.ufsm.brunodea.tcc.event.EventItem;

import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class EventsItemizedOverlay extends BalloonItemizedOverlay<EventItem> {
	private ArrayList<EventItem> mEventOverlays;
	
	public enum BalloonType {
		INFO, ADD
	}

	private Drawable mMarker;
	private BalloonType mBalloonType;
	
	public EventsItemizedOverlay(Context c, Drawable marker, MapView mapView, 
			BalloonType balloon_type) {
		super(boundCenterBottom(marker), mapView);
		mEventOverlays = new ArrayList<EventItem>();
		
		mMarker = marker;
		mBalloonType = balloon_type;
	}
	
	public void addEventItem(EventItem eventitem) {
		mEventOverlays.add(eventitem);
		populate();
	}
	
	public void removeEventItem(EventItem eventitem) {
		boolean removed = false;
		for(int i = 0; i < mEventOverlays.size(); i++) {
			EventItem curr = mEventOverlays.get(i);
			if(curr.compareIdentifiers(eventitem)) {
				mEventOverlays.remove(i);
				removed = true;
				break;
			}
		}
		if(removed) {
			populate();
		}
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
	
	@Override
	protected BalloonOverlayView<EventItem> createBalloonOverlayView() {
		BalloonOverlayView<EventItem> res = null;
		switch(mBalloonType) {
		case INFO:
			res = super.createBalloonOverlayView();
			break;
		case ADD:
			res = new AddEventBalloonOverlayView<EventItem>(
					getMapView().getContext(), mMarker.getIntrinsicHeight());
			break;
		default:
			res = super.createBalloonOverlayView();
		}
		
		return res;
	}
}
