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
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.internet.InfoCityServer;
import br.ufsm.brunodea.tcc.model.EventItem;
import br.ufsm.brunodea.tcc.model.EventType;
import br.ufsm.brunodea.tcc.model.EventTypeManager;
import br.ufsm.brunodea.tcc.util.DialogHelper;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

/**
 * Classe que monta e manipula os dados do balão com o formulário para a adição
 * de um novo evento.
 * 
 * @author bruno
 *
 * @param <Item>
 */
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
		
		ArrayList<EventType> types_list = EventTypeManager.instance(context).types();
		EventType []types = new EventType[types_list.size()];
		types = types_list.toArray(types);
		ArrayAdapter<EventType> adapter = new ArrayAdapter<EventType>(context, 
				android.R.layout.simple_spinner_item, types);
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
						removeAddEventItem();
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
			/**
			 * Faz a requisição ao servidor para salvar no seu banco de dados
			 * o evento recém criado.
			 */
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
									if(r.equals("ok")) {
										int pk = response.getInt("pk");
										EventItem e = createEvent(false);
										e.setPrimaryKey(pk);
										
										removeAddEventItem(); //remove o evento com balão de adicionar evento.
										mInfoCityMap.addEventItem(e);
										
										message = mContext.getResources()
												.getString(R.string.save_success);
									} else {
										message = mContext.getResources()
												.getString(R.string.server_error);
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
						msg.obj = InfoCityServer.saveEvent(mContext, createEvent(true));
						handler.sendMessage(msg);
					}
				};
				t.start();
			}
		}
	}
	
	/**
	 * "liga"/"desliga" a barra de progresso no windowtitle. 
	 */
	private void toggleProgressbar() {
		View v = findViewById(R.id.linearlayout_progressbar_addevent_balloon);
		int visibility = v.getVisibility() == View.VISIBLE ? 
				View.GONE : View.VISIBLE;
		v.setVisibility(visibility);
		findViewById(R.id.linearlayout_balloon_form).setVisibility(
				visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
	}
	
	/**
	 * Faz a validação do formulário.
	 * 
	 * @return true se o formulário foi preenchido corretamente.
	 */
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
	
	/**
	 * Cria um evento com os dados passados ao formulário.
	 * Recebe um booleano dizendo se vai ser enviado ao servidor, pois se este for o caso,
	 * deve-se inverter a ordem da latitude e longitude já que no servidor é assim
	 * ao contrário.
	 * 
	 * @param to_server true se esse evento vai ser enviado ao servidor.
	 * @return Evento criado a partir dos dados do formulário.
	 */
	private EventItem createEvent(boolean to_server) {
		String title = mEditTextTitle.getText().toString();
		String descr = mEditTextDescription.getText().toString();

		int lat = mEventItem.getPoint().getLatitudeE6();
		int lon = mEventItem.getPoint().getLongitudeE6();
		GeoPoint p = null;
		if(to_server) {
			p = new GeoPoint(lon, lat);
		} else {
			p = mEventItem.getPoint();
		}
		//pois no server é ao contrário: POINT(<lon> <lat>).
		EventItem event = new EventItem(p, title, descr, 
				selectedEventType());
		event.setKeywords(mEventItem.getKeywords());
		event.setPubDate(mEventItem.getPubDate());
		
		return event;
	}
	
	private EventType selectedEventType() {
		return (EventType) mSpinnerEventType.getSelectedItem();
	}
	
	/**
	 * Função que remove do mapa o evento do tipo ADD. (e.g. quando cancela a adição)
	 */
	private void removeAddEventItem() {
		mInfoCityMap.removeAddEventItem();
		mEventItem = null;
	}
	
	
}
