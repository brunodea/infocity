package br.ufsm.brunodea.tcc.context.supplier;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import br.ufsm.brunodea.tcc.context.ContextData;
import br.ufsm.brunodea.tcc.context.ContextSupplier;
import br.ufsm.brunodea.tcc.model.EventType;
import br.ufsm.brunodea.tcc.model.EventTypeManager;
import br.ufsm.brunodea.tcc.util.InfoCityPreferences;

public class InfoCityFilterContext implements ContextSupplier {

	private Context mContext;
	private Location mLocation;
	
	public InfoCityFilterContext(Context context) {
		mContext = context;
		mLocation = null;
	}
	
	public void setLocation(Location location) {
		mLocation = location;
	}
	
	@Override
	public ContextData getContextData() {
		ContextData context_data = new ContextData();
		
		EventType eventtype = InfoCityPreferences.eventTypeFilter(mContext);
		if(eventtype.getName() != EventTypeManager.instance().type_all().getName()) {
			context_data.setFilterEventType(eventtype.getName().toString());
		}
		context_data.setLatitude(mLocation.getLatitude());
		context_data.setLongitude(mLocation.getLongitude());
		context_data.setFromSupplier(toString());

		return context_data;
	}

	@Override
	public void setHandler(Handler handler) {		
	}

	@Override
	public boolean isReady() {
		return mLocation != null;
	}
	
	@Override
	public String toString() {
		return "Filtro";
	}
}
