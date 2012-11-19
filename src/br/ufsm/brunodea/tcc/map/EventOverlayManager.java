package br.ufsm.brunodea.tcc.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.map.EventsItemizedOverlay.BalloonType;
import br.ufsm.brunodea.tcc.model.EventItem;
import br.ufsm.brunodea.tcc.model.EventItem.EventType;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class EventOverlayManager {
	
	private Context mContext;
	private MapView mMapView;
	private List<Overlay> mMapOverlays;
	
	private HashMap<EventType, EventsItemizedOverlay> mEIOMap;
	
	public EventOverlayManager(Context context, MapView mapview, 
			List<Overlay> overlays) {
		mContext = context;
		mMapView = mapview;
		mEIOMap = new HashMap<EventType, EventsItemizedOverlay>();
		mMapOverlays = overlays;
	}
	
	public EventsItemizedOverlay getEventOverlay(EventType type, InfoCityMap infocitymap) {
		EventsItemizedOverlay eio = null;
		if(mEIOMap.containsKey(type)) {
			eio = mEIOMap.get(type);
		} else {
			BalloonType balloon_type = null;
			boolean draggable = false;
			switch(type) {
			case ADD:
				balloon_type = BalloonType.ADD;
				draggable = true;
				break;
			default:
				balloon_type = BalloonType.INFO;
				break;
			}
			eio = new EventsItemizedOverlay(mContext, getEventTypeMarker(type),
					mMapView, balloon_type, type, infocitymap);
			eio.setDraggable(draggable);
			
			mEIOMap.put(type, eio);
			mMapOverlays.add(eio);
		}
		
		return eio;
	}
	
	public void addEventItem(EventItem item, InfoCityMap infocitymap) {
		getEventOverlay(item.getType(), infocitymap).addEventItem(item);
	}
	public void removeEventItem(EventItem item) {
		getEventOverlay(item.getType(), null).removeEventItemByPk(item.getPrimaryKey());
	}
	public void clearItemizedOverlays() {
		for(EventType type : mEIOMap.keySet()) {
			getEventOverlay(type, null).clearOverlays();
		}
	}
	public ArrayList<Integer> getAllPks() {
		ArrayList<Integer> pks = new ArrayList<Integer>();
		for(EventsItemizedOverlay eio : mEIOMap.values()) {
			pks.addAll(eio.getAllPks());
		}
		return pks;
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
	
	public Collection<EventsItemizedOverlay> getItemizedOverlays() {
		return mEIOMap.values();
	}
}
