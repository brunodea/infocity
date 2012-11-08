package br.ufsm.brunodea.tcc.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class InfoCityLocationListener implements LocationListener {

	private InfoCityMap mInfoCityMap;	
	
	public InfoCityLocationListener(InfoCityMap infocity_map) {
		super();
		
		mInfoCityMap = infocity_map;
	}
	
	public void onLocationChanged(Location location) {
		mInfoCityMap.setLastKnownLocation(location);
		mInfoCityMap.toggleWindowTitleAddEventProgressBar();
		mInfoCityMap.centerMapInLastKnownLocation();
		mInfoCityMap.addAddEventItemMarker();
		mInfoCityMap.stopRequestLocationUpdates();
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
