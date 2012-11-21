package br.ufsm.brunodea.tcc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufsm.brunodea.tcc.model.EventItem;
import br.ufsm.brunodea.tcc.model.EventItem.EventType;

import com.google.android.maps.GeoPoint;
import com.google.gson.Gson;

/**
 * Classe auxiliar.
 * 
 * @author bruno
 *
 */
public class Util {
	//URL básica do servidor.
	public static String URL = "http://192.168.1.5:8000/events/";
	
	/**
	 * Cria um JSONObject a partir de um objeto.
	 * Porém, esse JSON será de acordo com os JSON criados no servidor para
	 * seus modelos.
	 * 
	 * @param modelname
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject toJSON(String modelname, Object obj) throws JSONException {
		String json = new Gson().toJson(obj);
		String root = "{'model':'events."+modelname+"','fields':"+json+"}";
			
		return new JSONObject(root);
	}
	
	/**
	 * Cria um EventItem.
	 * 
	 * @param lat
	 * @param lon
	 * @param title
	 * @param description
	 * @param type
	 * @return
	 */
	public static EventItem createEventItem(double lat, double lon, String title, 
			String description, EventType type) {
		GeoPoint gp = new GeoPoint((int)(lat*1E6),(int)(lon*1E6));
		return new EventItem(gp, title,  description, type);
	}
	
	/**
	 * Cria um EventItem a partir de um JSONObject.
	 * 
	 * @param model
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
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
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date.replace("T", " ").replace("Z", "")));
			cal.add(Calendar.HOUR_OF_DAY, -2); //bug do servidor que retorna duas horas adiantado.
			
			eventitem.setPubDate(cal.getTime());
			eventitem.setKeywords(keywords);
			
			eventitem.setPrimaryKey(pk);
		}
		return eventitem;
	}
}
