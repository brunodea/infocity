package br.ufsm.brunodea.tcc.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public interface Model {
	public JSONObject toJSON() throws JSONException;
	public List<NameValuePair> getListNameValuePair() throws JSONException;
}
