package br.ufsm.brunodea.tcc.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import br.ufsm.brunodea.tcc.event.EventItem;

import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;
import com.readystatesoftware.mapviewballoons.R;

public class AddEventBalloonOverlayView <Item extends OverlayItem>
	extends BalloonOverlayView<EventItem> {
	
	private Spinner mSpinnerEventType;
	
	public AddEventBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
	}

	@Override
	protected void setupView(Context context, final ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.addevent_balloon, parent);
		
		mSpinnerEventType = (Spinner)v.findViewById(R.id.spinner_type_addevent_balloon);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, 
				R.array.event_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		mSpinnerEventType.setAdapter(adapter);
	}
	

	@Override
	protected void setBalloonData(EventItem item, ViewGroup parent) {
	}
}
