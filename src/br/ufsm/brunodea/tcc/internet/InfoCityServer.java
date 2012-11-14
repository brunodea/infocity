package br.ufsm.brunodea.tcc.internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import br.ufsm.brunodea.tcc.model.EventItem;
import br.ufsm.brunodea.tcc.util.Util;

public class InfoCityServer {
	private static String makeRequest(String url, JSONObject json) throws Exception 
	{
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    HttpPost httpost = new HttpPost(url);
	    StringEntity se = new StringEntity(json.toString());

	    httpost.setEntity(se);
	    httpost.setHeader("Accept", "application/json");
	    httpost.setHeader("Content-type", "application/json");
	    HttpResponse response = httpclient.execute(httpost);
	    
	    return responseToString(response);
	}
	
	private static String responseToString(HttpResponse response) {
		String result = null;
		InputStream is;
		try {
			is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "utf-8"));
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	        }
	        is.close();
	        result = sb.toString();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return result;
	}
	
	public static JSONObject saveEvent(EventItem event) {
		JSONObject ret = null;
		try {
			JSONObject req = new JSONObject("{event:"+event.toJSON().toString()+"}");
			String response = makeRequest(Util.URL+"add/?", req);
			if(response != null && !response.equals("")) {
				if(!response.startsWith("<!DOCTYPE")) {
					ret = new JSONObject(response);
				} else {
					Log.d("InfoCityServer.saveEvent", response);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}
