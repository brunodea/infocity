package br.ufsm.brunodea.tcc.internet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.location.Location;
import android.util.Log;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.model.EventItem;
import br.ufsm.brunodea.tcc.util.Util;

public class InfoCityServer {
	private static String postRequest(String url, List<NameValuePair> nameValuePairs) {
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	    HttpParams params = httppost.getParams();

	    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	    HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
	    
	    httppost.setParams(params);
	    
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

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
	
	public static JSONObject saveEvent(Context c, EventItem event) {
		JSONObject ret = null;
		try {			
			String response = makeRequest(Util.URL+"add/?", event.getListNameValuePair());
			if(response != null && !response.equals("")) {
				if(!response.startsWith("<!DOCTYPE")) {
					ret = new JSONObject(response);
				} else {
					Log.d("InfoCityServer.saveEvent", response);
				}
			} else {
				ret = new JSONObject();
				ret.put("error", c.getResources().getString(R.string.server_error));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			ret = new JSONObject();
			try {
				ret.put("error", c.getResources().getString(R.string.server_error));
			} catch (NotFoundException e1) {
			} catch (JSONException e1) {
			}
		} catch (Exception e) {
			ret = new JSONObject();
			try {
				ret.put("error",  c.getResources().getString(R.string.conn_error));
			} catch (NotFoundException e1) {
			} catch (JSONException e1) {
			}
		}
		
		return ret;
	}
}
