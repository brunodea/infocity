package br.ufsm.brunodea.tcc.map;

import java.util.ArrayList;

import android.content.Context;
import br.ufsm.brunodea.tcc.event.EventItem;
import br.ufsm.brunodea.tcc.event.EventItem.EventType;
import br.ufsm.brunodea.tcc.util.Util;

import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class EventsItemizedOverlay extends BalloonItemizedOverlay<EventItem> {
	private ArrayList<EventItem> mEventOverlays;
	
	public enum BalloonType {
		INFO, ADD
	}

	private EventType mEventType;
	private BalloonType mBalloonType;
	
	private Context mContext;
	
	public EventsItemizedOverlay(Context c, EventType event_type, MapView mapView, 
			BalloonType balloon_type) {
		super(boundCenterBottom(Util.getEventTypeMarker(c, event_type)), mapView);
		mEventOverlays = new ArrayList<EventItem>();
		
		mEventType = event_type;
		mBalloonType = balloon_type;
		mContext = c;
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
	
	@Override
	protected BalloonOverlayView<EventItem> createBalloonOverlayView() {
		BalloonOverlayView<EventItem> res = null;
		switch(mBalloonType) {
		case INFO:
			res = super.createBalloonOverlayView();
			break;
		case ADD:
			res = new AddEventBalloonOverlayView<EventItem>(
					getMapView().getContext(), 
					Util.getEventTypeMarker(mContext, mEventType).getIntrinsicHeight());
			break;
		default:
			res = super.createBalloonOverlayView();
		}
		
		return res;
	}
}
