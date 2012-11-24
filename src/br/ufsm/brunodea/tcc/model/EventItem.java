package br.ufsm.brunodea.tcc.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufsm.brunodea.tcc.util.Util;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * Classe que herda de Model (um OverlayItem) e que guarda os dados básicos
 * de um evento.
 * 
 * @author bruno
 *
 */
public class EventItem extends OverlayItem implements Model {
	private EventType mType;
	private ArrayList<String> mKeywords;
	private Date mPubDate;
	private int mPrimaryKey;
	
	public EventItem(GeoPoint pos, String title, String snippet, EventType type) {
		super(pos, title, snippet);
		mType = type;
		mKeywords = new ArrayList<String>();
		mPubDate = new Date();
		mPrimaryKey = -1;
	}
	
	public EventItem(int primarykey, GeoPoint pos, String title, String snippet, EventType type) {
		super(pos, title, snippet);
		mType = type;
		mKeywords = new ArrayList<String>();
		mPubDate = new Date();
		mPrimaryKey = primarykey;
	}
	
	public EventType getType() {
		return mType;
	}
	public ArrayList<String> getKeywords() {
		return mKeywords;
	}
	public String keywordsToString() {
		String kws = "";
		for(int i = 0; i < mKeywords.size(); i++) {
			kws += mKeywords.get(i);
			if(i + 1 < mKeywords.size()) {
				kws += ", ";
			}
		}
		
		return kws;
	}
	public void setKeywords(ArrayList<String> keywords) {
		mKeywords = keywords;
	}
	public void setPubDate(Date date) {
		mPubDate = date;
	}
	public Date getPubDate() {
		return mPubDate;
	}
	public void setPrimaryKey(int pk) {
		mPrimaryKey = pk;
	}
	public int getPrimaryKey() {
		return mPrimaryKey;
	}
	
	/**
	 * Classe criada apenas para ser passada para um Gson para ser transformada
	 * em um JSON. Foi criada pois deve haver uma correlação entre os nomes das
	 * variáveis aqui e no servidor. E também para evitar enviar informações
	 * que não serão aproveitadas.
	 */
	private class EventJSON {
		private String title;
		private String description;
		private String pub_date;
		private String geo_coord;
		
		public EventJSON(String title, String descr, String pubdate, String coord) {
			this.title = title;
			this.description = descr;
			this.pub_date = pubdate;
			this.geo_coord = coord;
		}
	}

	/**
	 * Método que constrói um JSONObject a partir deste EventItem e retorna ele.
	 */
	@Override
	public JSONObject toJSON() {
		double latitude = getPoint().getLatitudeE6() / 1E6;
		double longitude = getPoint().getLongitudeE6() / 1E6;
		EventJSON event = new EventJSON(mTitle, mSnippet, 
				new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(mPubDate),
				latitude+","+longitude);
		
		JSONObject json = null;
		try {
			json = Util.toJSON("event", event);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * Método que retorna uma lista com os dados deste evento.
	 */
	@Override
	public List<NameValuePair> getListNameValuePair() {
		JSONObject json = toJSON();
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("event", "["+json.toString()+"]"));
		params.add(new BasicNameValuePair("event_keywords", mKeywords.toString()));
		params.add(new BasicNameValuePair("event_type", mType.toString()));
		
		return params;
	}
}
