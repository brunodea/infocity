package br.ufsm.brunodea.tcc.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.event.EventItem.EventType;

public class Util {
	public static Drawable getEventTypeMarker(Context c, EventType type) {
		Drawable marker = null;

		switch(type) {
		case UNKNOWN:
			marker = c.getResources().getDrawable(R.drawable.ic_launcher);
			break;
		default:
			marker = c.getResources().getDrawable(R.drawable.ic_launcher);
			break;
		}
		
		return marker;
	}
}
