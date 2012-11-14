package br.ufsm.brunodea.tcc.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public abstract class Model extends OverlayItem {

	public Model(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
	}

	public abstract JSONObject toJSON() throws JSONException;
}
