package br.ufsm.brunodea.tcc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class InfoCityPreferences {
	private static final String ALOHAR_UID = "alohar_uid";
	
	private static void setStringPreference(Context c, String key, String value) {
		SharedPreferences prefs = c.getSharedPreferences(
			      "br.ufsm.brunodea.tcc.InfoCity", Context.MODE_PRIVATE);
		prefs.edit().putString(key, value).commit();
	}
	private static String getStringPreference(Context c, String key, String _default) {
		SharedPreferences prefs = c.getSharedPreferences(
			      "br.ufsm.brunodea.tcc.InfoCity", Context.MODE_PRIVATE);
		return prefs.getString(key, _default);
	}
	private static float getFloatDefaultPreference(Context c, String key, float _default) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		return prefs.getFloat(key, _default);
	}
	
	public static void setAloharUID(Context c, String uid) {
		setStringPreference(c, ALOHAR_UID, uid);
	}
	public static String getAloharUID(Context c) {
		return getStringPreference(c, ALOHAR_UID, "");
	}
	
	public static float getEventMaxRadius(Context c) {
		return 50f;
		//return getFloatDefaultPreference(c, "event_radius", 50000f);
	}
}
