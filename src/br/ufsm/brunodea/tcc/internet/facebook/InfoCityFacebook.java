package br.ufsm.brunodea.tcc.internet.facebook;

import com.facebook.model.GraphUser;

public class InfoCityFacebook {
	private static GraphUser sUser = null;
	
	public static void setUser(GraphUser user) {
		sUser = user;
	}
	public static GraphUser getUser() {
		return sUser;
	}
}
