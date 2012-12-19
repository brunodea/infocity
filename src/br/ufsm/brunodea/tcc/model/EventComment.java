package br.ufsm.brunodea.tcc.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class EventComment implements Model {
	private String mUserID;
	private String mUserName;
	private String mComment;
	private int mEventPrimaryKey;
	
	public EventComment(int event_pk, String user_id, String user_name, String comment) {
		mEventPrimaryKey = event_pk;
		mUserID = user_id;
		mUserName = user_name;
		mComment = comment;
	}
	
	public int getEventPrimaryKey() {
		return mEventPrimaryKey;
	}
	public void setEventPrimaryKey(int pk) {
		mEventPrimaryKey = pk;
	}
	public String getUserID() {
		return mUserID;
	}
	public void setUserID(String user_id) {
		mUserID = user_id;
	}
	public String getUserName() {
		return mUserName;
	}
	public void setUserName(String user_name) {
		mUserName = user_name;
	}
	public String getComment() {
		return mComment;
	}
	public void setComment(String comment) {
		mComment = comment;
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("event_id", getEventPrimaryKey());
		json.put("user_id", getUserID());
		json.put("user_name", getUserName());
		json.put("comment", getComment());
		
		return json;
	}

	@Override
	public List<NameValuePair> getListNameValuePair() throws JSONException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("event_id", getEventPrimaryKey()+""));
		list.add(new BasicNameValuePair("user_id", getUserID()));
		list.add(new BasicNameValuePair("user_name", getUserName()));
		list.add(new BasicNameValuePair("comment", getComment()));
		
		return list;
	}
}
