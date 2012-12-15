package br.ufsm.brunodea.tcc.internet.facebook;

import com.facebook.Session;
import com.facebook.model.GraphUser;

public class InfoCityFacebook {
	private static GraphUser sUser = null;
	private static Session mCurrSession = null;
	
	public static void setUser(GraphUser user) {
		sUser = user;
	}
	public static GraphUser getUser() {
		return sUser;
	}
	
	public static void setSession(Session session) {
		mCurrSession = session;
	}
	public static Session getSession() {
		return mCurrSession;
	}
}
