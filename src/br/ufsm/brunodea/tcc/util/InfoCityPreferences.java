package br.ufsm.brunodea.tcc.util;

import android.content.Context;
import android.content.SharedPreferences;

public class InfoCityPreferences {
	private static final String ALOHAR_UID = "alohar_uid";
	
	private static void setStringPreference(Context c, String key, String value) {
		SharedPreferences prefs = c.getSharedPreferences(
			      "br.ufsm.brunodea.tcc.InfoCity", Context.MODE_PRIVATE);
		prefs.edit().putString(key, value).commit();
	}
	private static String getStringPreference(Context c, String key) {
		SharedPreferences prefs = c.getSharedPreferences(
			      "br.ufsm.brunodea.tcc.InfoCity", Context.MODE_PRIVATE);
		return prefs.getString(key, "");
	}
	
	public static void setAloharUID(Context c, String uid) {
		setStringPreference(c, ALOHAR_UID, uid);
	}
	public static String getAloharUID(Context c) {
		return getStringPreference(c, ALOHAR_UID);
	}
}
