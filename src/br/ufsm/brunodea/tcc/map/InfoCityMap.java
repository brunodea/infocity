package br.ufsm.brunodea.tcc.map;

import java.util.List;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.event.EventItem;
import br.ufsm.brunodea.tcc.event.EventItem.EventType;
import br.ufsm.brunodea.tcc.internet.Internet;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class InfoCityMap extends MapActivity {

	private MapView mMapView;
	private MapController mMapController;
	private List<Overlay> mMapOverlays;
	
	@Override
	public void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.map);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.windowtitle);
		getWindow().setFormat(PixelFormat.RGBA_8888);
	 
		init();
		addTestMarkers();
		
		Internet.hasConnection(this, true);
	}
	
	private void init() {
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setBuiltInZoomControls(true);
		mMapView.getController().setZoom(20);
		mMapView.setSatellite(true);
		
		mMapController = mMapView.getController();
		
		mMapOverlays = mMapView.getOverlays();
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private void addTestMarkers() {
		EventsItemizedOverlay eio = 
				new EventsItemizedOverlay(this, EventType.UNKNOWN, mMapView);
		
		EventItem event = createTestEventMarker(-29.708332, -53.818674);
		EventItem event2 = createTestEventMarker(-29.696031, -53.860044);
		
		eio.addEventItem(event);
		eio.addEventItem(event2);

		mMapOverlays.add(eio);
		
		mMapController.animateTo(event.getPoint());
	}
	
	private EventItem createTestEventMarker(double lat, double lon) {
		GeoPoint gp = new GeoPoint((int)(lat*1E6),(int)(lon*1E6));
		EventItem event = new EventItem(gp, "teste", "esse é um teste.", 
				EventType.UNKNOWN);
		return event;
	}
}
