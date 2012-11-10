package br.ufsm.brunodea.tcc.map;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.event.EventItem;
import br.ufsm.brunodea.tcc.event.EventItem.EventType;
import br.ufsm.brunodea.tcc.map.EventsItemizedOverlay.BalloonType;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class EventOverlayManager {
	
	private Context mContext;
	private MapView mMapView;
	private List<Overlay> mMapOverlays;
	
	private HashMap<EventType, EventsItemizedOverlay> sEIOMap;
	
	public EventOverlayManager(Context context, MapView mapview, 
			List<Overlay> overlays) {
		mContext = context;
		mMapView = mapview;
		sEIOMap = new HashMap<EventType, EventsItemizedOverlay>();
		mMapOverlays = overlays;
	}
	
	public EventsItemizedOverlay getEventOverlay(EventType type) {
		EventsItemizedOverlay eio = null;
		if(sEIOMap.containsKey(type)) {
			eio = sEIOMap.get(type);
		} else {
			BalloonType balloon_type = null;
			switch(type) {
			case ADD:
				balloon_type = BalloonType.ADD;
				break;
			default:
				balloon_type = BalloonType.INFO;
				break;
			}
			eio = new EventsItemizedOverlay(mContext, getEventTypeMarker(type),
					mMapView, balloon_type);
			
			sEIOMap.put(type, eio);
			mMapOverlays.add(eio);
		}
		
		return eio;
	}
	
	public void addEventItem(EventItem item) {
		getEventOverlay(item.getType()).addEventItem(item);
	}
	public void removeEventItem(EventItem item) {
		getEventOverlay(item.getType()).removeEventItem(item);
	}

	public Drawable getEventTypeMarker(EventType type) {
		Drawable marker = null;

		switch(type) {
		case UNKNOWN:
			marker = mContext.getResources().getDrawable(R.drawable.ic_launcher);
			break;
		default:
			marker = mContext.getResources().getDrawable(R.drawable.ic_launcher);
			break;
		}
		
		return marker;
	}
}
