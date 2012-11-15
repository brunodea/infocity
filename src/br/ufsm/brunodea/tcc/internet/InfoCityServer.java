package br.ufsm.brunodea.tcc.internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import br.ufsm.brunodea.tcc.model.EventItem;
import br.ufsm.brunodea.tcc.util.Util;

public class InfoCityServer {
	private static String makeRequest(String url, List<NameValuePair> nameValuePairs) throws Exception {
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	    
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	    httppost.setHeader("Accept", "application/json");
	    httppost.setHeader("Content-type", "application/json; charset=UTF-8");
	    HttpResponse response = httpclient.execute(httppost);
	    
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
			String response = makeRequest(Util.URL+"add/?", event.getListNameValuePair());
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
		}
		
		return ret;
	}
}
