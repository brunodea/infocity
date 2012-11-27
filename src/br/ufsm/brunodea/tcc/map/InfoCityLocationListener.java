package br.ufsm.brunodea.tcc.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Classe chamada toda vez que se quer receber a localização atual do usuário.
 * A ação tomada em onLocationChanged é determinada pelo LocationAction setado.
 * 
 * @author bruno
 *
 */
public class InfoCityLocationListener implements LocationListener {

	/**
	 * Determina que ação será tomada no onLocationChanged.
	 * (e em outras partes do código se vir ao caso)
	 */
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
			if(!location.getProvider().equals("QrCode")) {
				mInfoCityMap.toggleWindowTitleAddEventProgressBar();
				mInfoCityMap.centerMapInLastKnownLocation();
			}
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
		if(status == 0 || status == 1) {
			mInfoCityMap.toggleWindowTitleAddEventProgressBar();
			mInfoCityMap.stopRequestLocationUpdates();
		}
	}

}
