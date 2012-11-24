package br.ufsm.brunodea.tcc;

import java.util.List;

import android.app.Application;
import android.content.Context;
import br.ufsm.brunodea.tcc.map.EventOverlayManager;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Singleton que guarda a única instância de EventOverlayManager que deve existir.
 * @author bruno
 *
 */
public class App extends Application {
	private static App sInstance = null;

	private EventOverlayManager mEOManager;

	private App() {
		super();
	}
	
	public void initEventOverlayManager(Context c, MapView mv, List<Overlay> overlays) {
		mEOManager = new EventOverlayManager(c, mv, overlays);
	}

	public EventOverlayManager getEventOverlayManager() {
		return mEOManager;
	}
	
	public static App instance() {
		if(sInstance == null) {
			sInstance = new App();
		}
		
		return sInstance;
	}
}
