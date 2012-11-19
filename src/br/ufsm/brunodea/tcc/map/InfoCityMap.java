package br.ufsm.brunodea.tcc.map;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.ufsm.brunodea.tcc.App;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.internet.InfoCityServer;
import br.ufsm.brunodea.tcc.internet.Internet;
import br.ufsm.brunodea.tcc.map.InfoCityLocationListener.LocationAction;
import br.ufsm.brunodea.tcc.model.EventItem;
import br.ufsm.brunodea.tcc.model.EventItem.EventType;
import br.ufsm.brunodea.tcc.util.Util;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class InfoCityMap extends MapActivity implements OnClickListener {

	private MapView mMapView;
	private MapController mMapController;
	private List<Overlay> mMapOverlays;
	
	private LocationManager mLocationManager;
	private InfoCityLocationListener mLocationListener;
	
	private Location mLastKnownLocation;
	
	private ImageButton mWindowTitleButtonRefresh;
	private ImageButton mWindowTitleButtonCenterOn;
	private ImageButton mWindowTitleButtonAddEvent;
	private ProgressBar mWindowTitleProgressBar;
	
	@Override
	public void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.map);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.windowtitle);

		initGUIElements();
		init();
		
		Internet.hasConnection(this, true);
		onClick(mWindowTitleButtonRefresh);
	}
	
	private void init() {
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.getController().setZoom(20);
		mMapView.setSatellite(true);
		
		mMapController = mMapView.getController();
		
		mMapOverlays = mMapView.getOverlays();
		
		App.instance().initEventOverlayManager(this, mMapView, mMapOverlays);
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationListener = new InfoCityLocationListener(this);

		mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(mLastKnownLocation == null) {
			mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
	
		centerMapInLastKnownLocation();
	}
	
	private void initGUIElements() {
		mWindowTitleButtonAddEvent = (ImageButton)findViewById(R.id.button_add_event);
		mWindowTitleButtonAddEvent.setOnClickListener(this);
		
		mWindowTitleProgressBar = (ProgressBar)findViewById(R.id.progressbar_window_title);
		mWindowTitleProgressBar.setVisibility(View.GONE);
		
		mWindowTitleButtonCenterOn = (ImageButton)findViewById(R.id.button_centeron_marker);
		mWindowTitleButtonCenterOn.setOnClickListener(this);
		
		mWindowTitleButtonRefresh = (ImageButton)findViewById(R.id.button_refresh_events);
		mWindowTitleButtonRefresh.setOnClickListener(this);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public void startRequestLocationUpdates() {
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				0, 5, mLocationListener);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				0, 5, mLocationListener);
	}
	
	public void stopRequestLocationUpdates() {
		mLocationManager.removeUpdates(mLocationListener);
	}
	
	public void centerMapInLastKnownLocation() {
		if(mLastKnownLocation != null) {
			mMapController.setCenter(
					new GeoPoint(
						(int)(mLastKnownLocation.getLatitude()*1E6), 
						(int)(mLastKnownLocation.getLongitude()*1E6)
				    ));
		}
	}

	@Override
	public void onBackPressed() {
		boolean closed_balloons = false;
		for(EventsItemizedOverlay e : App.instance().getEventOverlayManager().getItemizedOverlays()) {
			if(e.getFocus() != null) {
				e.setFocus(null);
				closed_balloons = true;
			}
		}
		
		if(!closed_balloons) {
			super.onBackPressed();
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v == mWindowTitleButtonAddEvent) {
			toggleWindowTitleAddEventProgressBar();
			mLocationListener.setCurrAction(LocationAction.ADD_EVENT);
			startRequestLocationUpdates();
		} else if(v == mWindowTitleButtonCenterOn) {
			GeoPoint p = App.instance().getEventOverlayManager().
					getEventOverlay(EventType.ADD, this).getItem(0).getPoint();
			mMapController.setCenter(p);
		} else if(v == mWindowTitleButtonRefresh) {
			toggleRefreshAnimation();
			mLocationListener.setCurrAction(LocationAction.GET_EVENTS);
			startRequestLocationUpdates();
		}
	}
	
	public void setLastKnownLocation(Location location) {
		mLastKnownLocation = location;
	}	
	
	public void toggleWindowTitleAddEventProgressBar() {
		int pb_vis = mWindowTitleProgressBar.getVisibility();
		mWindowTitleProgressBar.setVisibility(
				pb_vis == View.VISIBLE ? View.GONE : View.VISIBLE);
		TextView tv = (TextView)findViewById(R.id.textview_gettingpos_window_title);
		int tv_vis = tv.getVisibility();
		tv.setVisibility(tv_vis == View.VISIBLE ? View.GONE : View.VISIBLE);
		
		mWindowTitleButtonAddEvent.setEnabled(!mWindowTitleButtonAddEvent.isEnabled());
	}
	
	public void toggleWindowTitleAddEventCenterOn() {
		int add_vis = mWindowTitleButtonAddEvent.getVisibility();
		mWindowTitleButtonAddEvent.setVisibility(
				add_vis == View.VISIBLE ? View.GONE : View.VISIBLE);
		mWindowTitleButtonCenterOn.setVisibility(add_vis);
	}
	
	public void addAddEventItemMarker() {
		EventItem new_event = Util.createEventItem(mLastKnownLocation.getLatitude(),
				mLastKnownLocation.getLongitude(), "New Event", "New Event", EventType.ADD);
		App.instance().getEventOverlayManager().addEventItem(new_event, this);

		toggleWindowTitleAddEventCenterOn();
	}
	
	public void addEventItem(EventItem event_item) {
		App.instance().getEventOverlayManager().addEventItem(event_item, this);
	}

	private void toggleRefreshAnimation() {
		boolean clickable = mWindowTitleButtonRefresh.isClickable();
		mWindowTitleButtonRefresh.setClickable(!clickable);
		if(mWindowTitleButtonRefresh.isClickable()) {
			mWindowTitleButtonRefresh.getAnimation().cancel();
		} else {
			mWindowTitleButtonRefresh.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
		}
	}
	
	public void fetchEvents() {
		final Handler done_handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				toggleRefreshAnimation();
				if(msg.what == 0) {
					//good
					mMapView.invalidate();
				} else if(msg.what == 1) {
					Toast.makeText(InfoCityMap.this, getResources()
							.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		final Handler event_handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				EventItem event = (EventItem) msg.obj;
				if(!App.instance().getEventOverlayManager()
					.getEventOverlay(event.getType(), InfoCityMap.this)
					.containsEventItem(event)) {
					
					addEventItem(event);
				}
			}
		};
		
		Thread t = new Thread() {
			@Override
			public void run() {
				boolean fine = false;
				JSONObject res = InfoCityServer.getEvents(InfoCityMap.this, mLastKnownLocation, 50000f,
						App.instance().getEventOverlayManager().getAllPks());
				if(res != null && res.has("size")) {
					try {
						//App.instance().getEventOverlayManager().clearItemizedOverlays();
						int size = res.getInt("size");
						for(int i = 0; i < size; i++) {
							if(res.has("event_"+i)) {
								JSONObject event = res.getJSONObject("event_"+i);
								try {
									Message msg = event_handler.obtainMessage();
									msg.obj = Util.eventItemFromJSON(event);
									event_handler.sendMessage(msg);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						fine = true;
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if(fine) {
					done_handler.sendEmptyMessage(0);
				} else {
					done_handler.sendEmptyMessage(1);
				}
			}
		};
		
		t.start();
	}
}
