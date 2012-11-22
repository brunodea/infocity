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
	private static String getStringDefaultPreference(Context c, String key, String _default) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		return prefs.getString(key, _default);
	}
	private static boolean getBooleanDefaultPreference(Context c, String key, boolean _default) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		return prefs.getBoolean(key, _default);
	}
	
	public static void setAloharUID(Context c, String uid) {
		setStringPreference(c, ALOHAR_UID, uid);
	}
	public static String getAloharUID(Context c) {
		return getStringPreference(c, ALOHAR_UID, "");
	}
	
	public static float getEventMaxRadius(Context c) {
		return Float.parseFloat(getStringDefaultPreference(c, "event_radius", "50000"));
	}
	
	public static int getServerPort(Context c) {
		return Integer.parseInt(getStringDefaultPreference(c, "server_port",  "8000"));
	}
	public static String getServerIP(Context c) {
		return getStringDefaultPreference(c, "server_ip",  "192.168.1.5");
	}
	
	public static String getServerBaseURI(Context c) {
		return "http://"+getServerIP(c)+":"+getServerPort(c)+"/events/";
	}
	
	public static boolean shouldEnableMyLocation(Context c) {
		return getBooleanDefaultPreference(c, "enable_mylocation", true);
	}
	public static boolean shouldEnableCompass(Context c) {
		return getBooleanDefaultPreference(c, "enable_compass", false);
	}
}
