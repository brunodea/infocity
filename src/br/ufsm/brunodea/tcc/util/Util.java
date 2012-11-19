package br.ufsm.brunodea.tcc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufsm.brunodea.tcc.model.EventItem;
import br.ufsm.brunodea.tcc.model.EventItem.EventType;

import com.google.android.maps.GeoPoint;
import com.google.gson.Gson;


public class Util {
	public static String URL = "http://192.168.1.5:8000/events/";
	
	public static JSONObject toJSON(String modelname, Object obj) throws JSONException {
		String json = new Gson().toJson(obj);
		String root = "{'model':'events."+modelname+"','fields':"+json+"}";
			
		return new JSONObject(root);
	}
	
	public static EventItem createEventItem(double lat, double lon, String title, 
			String description, EventType type) {
		GeoPoint gp = new GeoPoint((int)(lat*1E6),(int)(lon*1E6));
		return new EventItem(gp, title,  description, type);
	}
	
	public static EventItem eventItemFromJSON(JSONObject model) throws JSONException, Exception {
		EventItem eventitem = null;
		if(model.has("fields")) {
			int pk = model.getInt("pk");
			
			model = model.getJSONObject("fields");
			String title = model.getString("title");
			String descr = model.getString("description");
			String coord = model.getString("geo_coord");
			String date  = model.getString("pub_date");
			String type  = model.getString("event_type");
			
			ArrayList<String> keywords = new ArrayList<String>();
			JSONArray jsonArray = model.getJSONArray("keywords"); 
			if(jsonArray != null) { 
				int len = jsonArray.length();
				for (int i=0;i<len;i++) { 
					keywords.add(jsonArray.get(i).toString());
				}
			}
			String[] c = coord.replace("POINT", "").replace("(", "").replace(")", "")
					.trim().split(" ");
			eventitem = createEventItem(Double.parseDouble(c[1]), Double.parseDouble(c[0]), 
					title, descr, EventType.fromString(type));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			eventitem.setPubDate(sdf.parse(date.replace("T", " ").replace("Z", "")));
			eventitem.setKeywords(keywords);
			
			eventitem.setPrimaryKey(pk);
		}
		return eventitem;
	}
}
