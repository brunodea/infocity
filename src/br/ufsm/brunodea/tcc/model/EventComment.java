package br.ufsm.brunodea.tcc.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class EventComment implements Model {
	private String mUserID;
	private String mUserName;
	private String mComment;
	private Date mDate;
	private int mEventPrimaryKey;
	
	public EventComment(int event_pk, String user_id, String user_name,
			String comment,	Date date) {
		mEventPrimaryKey = event_pk;
		mUserID = user_id;
		mUserName = user_name;
		mComment = comment;
		mDate = date;
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
	public Date getDate() {
		return mDate;
	}
	public void setDate(Date date) {
		mDate = date;
	}
	public void setDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			setDate(sdf.parse(date));
		} catch(Exception e) {
			mDate = null;
		}
	}
	public String dateToReprString() {
		return SimpleDateFormat.getDateTimeInstance().format(getDate());
	}
	
	private String dateToString() {
		return SimpleDateFormat.getDateTimeInstance().format(getDate());
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("event_id", getEventPrimaryKey());
		json.put("user_id", getUserID());
		json.put("user_name", getUserName());
		json.put("comment", getComment());
		json.put("date", dateToString());
		
		return json;
	}

	@Override
	public List<NameValuePair> getListNameValuePair() {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("event_id", getEventPrimaryKey()+""));
		list.add(new BasicNameValuePair("user_id", getUserID()));
		list.add(new BasicNameValuePair("user_name", getUserName()));
		list.add(new BasicNameValuePair("comment", getComment()));
		list.add(new BasicNameValuePair("date", dateToString()));
		
		return list;
	}
}
