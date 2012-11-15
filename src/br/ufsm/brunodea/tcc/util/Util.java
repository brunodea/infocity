package br.ufsm.brunodea.tcc.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;


public class Util {
	public static String URL = "http://192.168.1.5:8000/events/";
	
	public static JSONObject toJSON(String modelname, Object obj) throws JSONException {
		String json = new Gson().toJson(obj);
		String root = "{'pk':null,'model':'events."+modelname+"','fields':"+json+"}";
		
		return new JSONObject(root);
	}
}
