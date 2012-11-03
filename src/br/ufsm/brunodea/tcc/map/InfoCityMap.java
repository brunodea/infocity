package br.ufsm.brunodea.tcc.map;

import android.os.Bundle;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.internet.Internet;

import com.google.android.maps.MapActivity;

public class InfoCityMap extends MapActivity {

	@Override
	public void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);
		
		setContentView(R.layout.map);
		Internet.hasConnection(this, true);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
