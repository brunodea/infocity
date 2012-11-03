package br.ufsm.brunodea.tcc.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import br.ufsm.brunodea.tcc.util.DialogHelper;

public class Internet {
	private static boolean hasConnection(Context c) {
		ConnectivityManager connMgr = (ConnectivityManager) 
	           c.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	}
	
	public static boolean hasConnection(Context c, boolean show_dialog) {
		boolean has = Internet.hasConnection(c);
		if(show_dialog) {
			if(!has) {
				DialogHelper.showNoConnectionDialog(c);
			}
		}
		return has;
	}
}
