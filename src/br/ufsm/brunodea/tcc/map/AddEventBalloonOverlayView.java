package br.ufsm.brunodea.tcc.map;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.ufsm.brunodea.tcc.App;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.internet.InfoCityServer;
import br.ufsm.brunodea.tcc.model.EventItem;
import br.ufsm.brunodea.tcc.model.EventItem.EventType;
import br.ufsm.brunodea.tcc.util.DialogHelper;

import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class AddEventBalloonOverlayView <Item extends OverlayItem>
	extends BalloonOverlayView<EventItem> implements OnClickListener {
	
	private EditText mEditTextTitle;
	private EditText mEditTextDescription;

	private TextView mTextViewKeywords;
	
	private Button mButtonKeywords;
	private Spinner mSpinnerEventType;
	
	private ImageButton mImageButtonSave;
	private ImageButton mImageButtonCancel;
	
	private EventItem mEventItem;
	
	private Context mContext;
	private InfoCityMap mInfoCityMap;
	
	public AddEventBalloonOverlayView(Context context, int balloonBottomOffset,
			InfoCityMap infocitymap) {
		super(context, balloonBottomOffset);
		mInfoCityMap = infocitymap;
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
		
		mTextViewKeywords = (TextView)v.findViewById(R.id.textivew_keywords_addevent_balloon);
		
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
							getEventOverlay(mEventItem.getType(), mInfoCityMap).setFocus(null);
						App.instance().getEventOverlayManager().
							removeEventItem(mEventItem);
						mInfoCityMap.toggleWindowTitleAddEventCenterOn();
					} else {
						//no
					}
				}
			};
			DialogHelper.yesNoDialog(mContext, 
					mContext.getResources().getString(R.string.alert), 
					mContext.getResources().getString(R.string.cancel_addevent),
					yesnohandler);
		} else if(v == mButtonKeywords) {
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					ArrayList<String> keywords = (ArrayList<String>) msg.obj;
					if(keywords.size() == 0) {
						mTextViewKeywords.setText(mContext.getResources()
								.getString(R.string.no_keywords));
					} else {
						mEventItem.setKeywords(keywords);
						mTextViewKeywords.setText(mEventItem.keywordsToString());
					}
				}
			};
			DialogHelper.addKeywordDialog(mContext, mEventItem.getKeywords(), 5, handler);
		} else if(v == mImageButtonSave) {
			if(validate()) {
				toggleProgressbar();
				final Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						JSONObject response = (JSONObject) msg.obj;
						String message = "";
						if(response != null && response.has("response")) {
							try {
								if(response.has("error")) {
									message = response.getString("error");
								} else {
									String r = response.getString("response");
									if(!r.equals("ok")) {
										message = mContext.getResources()
												.getString(R.string.server_error);
									} else {
										message = mContext.getResources()
												.getString(R.string.save_success);
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {
							message = mContext.getResources()
									.getString(R.string.server_error);
						}
						Toast.makeText(mContext, message,
								Toast.LENGTH_SHORT).show();
						toggleProgressbar();
					}
				};
				Thread t = new Thread() {
					@Override
					public void run() {
						Message msg = handler.obtainMessage();
						msg.obj = InfoCityServer.saveEvent(mContext, createEvent());
						handler.sendMessage(msg);
					}
				};
				t.start();
			}
		}
	}
	
	private void toggleProgressbar() {
		View v = findViewById(R.id.linearlayout_progressbar_addevent_balloon);
		int visibility = v.getVisibility() == View.VISIBLE ? 
				View.GONE : View.VISIBLE;
		v.setVisibility(visibility);
		findViewById(R.id.linearlayout_balloon_form).setVisibility(
				visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
	}
	
	private boolean validate() {
		boolean ok = false;
		String title = mEditTextTitle.getText().toString();
		String descr = mEditTextDescription.getText().toString();
		
		int type_pos = mSpinnerEventType.getSelectedItemPosition();
		ok = type_pos != 0 && !title.equals("") && !descr.equals("");
		
		Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
		if(type_pos == 0) {
			mSpinnerEventType.startAnimation(shake);
		}
		if(title.equals("")) {
			mEditTextTitle.startAnimation(shake);
		}
		if(descr.equals("")) {
			mEditTextDescription.startAnimation(shake);
		}
		
		return ok;
	}
	
	private EventItem createEvent() {
		String title = mEditTextTitle.getText().toString();
		String descr = mEditTextDescription.getText().toString();
		
		EventItem event = new EventItem(mEventItem.getPoint(), title, descr, 
				selectedEventType());
		event.setKeywords(mEventItem.getKeywords());
		
		return event;
	}
	
	private EventType selectedEventType() {
		EventType type = null;
		String str_type = mSpinnerEventType.getSelectedItem().toString();
		if(str_type.equalsIgnoreCase("desconhecido")) {
			type = EventType.UNKNOWN;
		}
		return type;
	}
}
