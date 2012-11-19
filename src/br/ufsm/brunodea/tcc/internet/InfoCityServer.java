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
	    HttpResponse response = null;
        try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

		    httppost.setHeader("Accept", "application/json");
		    httppost.setHeader("Content-type", "application/json; charset=UTF-8");
		    response = httpclient.execute(httppost);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return responseToString(response);
	}
	
	private static String getRequest(String url) {
		HttpResponse response = null;    
        HttpClient client = new DefaultHttpClient();
        
        HttpGet request = new HttpGet();
        HttpParams params = request.getParams();
        
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
        
        request.setParams(params);
        try {
			request.setURI(new URI(url));
			response = client.execute(request);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return responseToString(response);
	}

	private static String responseToString(HttpResponse response) {
		if(response == null) {
			return null;
		}
		String res = null;
		try {
			res = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			res = new String(res.getBytes("UTF-8"), "UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return res;
	}
	
	private static JSONObject checkResponse(Context c, String response) {
		JSONObject ret = null;
		try {
			if(response != null && !response.equals("")) {
				if(!response.startsWith("<!DOCTYPE")) {
					ret = new JSONObject(response);
				} else {
					Log.d("InfoCityServer.checkResponse", response);
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
