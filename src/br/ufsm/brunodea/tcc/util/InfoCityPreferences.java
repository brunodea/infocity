package br.ufsm.brunodea.tcc.util;

import android.content.Context;
import android.content.SharedPreferences;
import br.ufsm.brunodea.tcc.App;

public class InfoCityPreferences {
	private static final String ALOHAR_UID = "alohar_uid";
	
	private static void setStringPreference(String key, String value) {
		SharedPreferences prefs = App.instance().getSharedPreferences(
			      "br.ufsm.brunodea.tcc.InfoCity", Context.MODE_PRIVATE);
		prefs.edit().putString(key, value).commit();
	}
	private static String getStringPreference(String key) {
		SharedPreferences prefs = App.instance().getSharedPreferences(
			      "br.ufsm.brunodea.tcc.InfoCity", Context.MODE_PRIVATE);
		return prefs.getString(key, "");
	}
	
	public static void setAloharUID(String uid) {
		setStringPreference(ALOHAR_UID, uid);
	}
	public static String getAloharUID() {
		return getStringPreference(ALOHAR_UID);
	}
}
