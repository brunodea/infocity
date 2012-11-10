package br.ufsm.brunodea.tcc.map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import br.ufsm.brunodea.tcc.App;
import br.ufsm.brunodea.tcc.event.EventItem;
import br.ufsm.brunodea.tcc.util.DialogHelper;

import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;
import com.readystatesoftware.mapviewballoons.R;

public class AddEventBalloonOverlayView <Item extends OverlayItem>
	extends BalloonOverlayView<EventItem> implements OnClickListener {
	
	private EditText mEditTextTitle;
	private EditText mEditTextDescription;

	private Button mButtonKeywords;
	private Spinner mSpinnerEventType;
	
	private ImageButton mImageButtonSave;
	private ImageButton mImageButtonCancel;
	
	private EventItem mEventItem;
	
	private Context mContext;
	
	public AddEventBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
		mContext = context;
	}

	@Override
	protected void setupView(Context context, final ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.addevent_balloon, parent);
		
		bindGUIElements(v);
		setGUIListeners();
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, 
				R.array.event_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		mSpinnerEventType.setAdapter(adapter);
	}
	
	private void bindGUIElements(View v) {
		mEditTextTitle = (EditText)v.findViewById(R.id.edittext_title_addevent_balloon);
		mEditTextDescription = (EditText)v.findViewById(R.id.edittext_descr_addevent_balloon);
		
		mButtonKeywords = (Button)v.findViewById(R.id.button_addkeyword_addevent_balloon);
		mSpinnerEventType = (Spinner)v.findViewById(R.id.spinner_type_addevent_balloon);
		
		mImageButtonSave = (ImageButton)v.findViewById(R.id.button_save_addevent_balloon);
		mImageButtonCancel = (ImageButton)v.findViewById(R.id.button_cancel_addevent_balloon);
	}
	
	private void setGUIListeners() {
		mButtonKeywords.setOnClickListener(this);
		mImageButtonSave.setOnClickListener(this);
		mImageButtonCancel.setOnClickListener(this);
	}
	
	@Override
	protected void setBalloonData(EventItem item, ViewGroup parent) {
		mEventItem = item;
	}

	@Override
	public void onClick(View v) {
		if(v == mImageButtonCancel) {
			Handler yesnohandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if(msg.what == 0) {
						App.instance().getEventOverlayManager().
							removeEventItem(mEventItem);
					} else {
						//no
					}
				}
			};
			DialogHelper.yesNoDialog(mContext, 
					mContext.getResources().getString(R.string.alert), 
					mContext.getResources().getString(R.string.cancel_addevent),
					yesnohandler);
		}
	}
}
