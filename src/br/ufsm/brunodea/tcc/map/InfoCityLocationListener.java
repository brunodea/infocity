package br.ufsm.brunodea.tcc.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class InfoCityLocationListener implements LocationListener {

	public enum LocationAction {
		ADD_EVENT, GET_EVENTS
	}
	
	private InfoCityMap mInfoCityMap;
	private LocationAction mCurrAction;
	
	public InfoCityLocationListener(InfoCityMap infocity_map) {
		super();
		
		mInfoCityMap = infocity_map;
		mCurrAction = null;
	}
	
	public void setCurrAction(LocationAction action) {
		mCurrAction = action;
	}
	
	public void onLocationChanged(Location location) {
		mInfoCityMap.setLastKnownLocation(location);

		switch(mCurrAction) {
		case ADD_EVENT:
			mInfoCityMap.toggleWindowTitleAddEventProgressBar();
			mInfoCityMap.centerMapInLastKnownLocation();
			mInfoCityMap.addAddEventItemMarker();
			break;
		case GET_EVENTS:
			mInfoCityMap.fetchEvents();
			break;
		default:
			break;
		}

		mInfoCityMap.stopRequestLocationUpdates();
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
