package br.ufsm.brunodea.tcc.context.alohar;

import br.ufsm.brunodea.tcc.App;
import br.ufsm.brunodea.tcc.util.InfoCityPreferences;

import com.alohar.core.Alohar;
import com.alohar.user.callback.ALEventListener;
import com.alohar.user.content.data.ALEvents;

public class InfoCityAlohar {
	private final int APP_ID = 239;
	private final String API_KEY = "5a470a7e524a5959dea5620cd6a593c10b60eb56";
	
	private Alohar mAlohar;
	
	public InfoCityAlohar() {
		Alohar.init(App.instance());
		mAlohar = Alohar.getInstance();
		
		String uid = InfoCityPreferences.getAloharUID();
		if(uid != null && !uid.equals("")) {
			authenticate(uid);
		} else {
			register();
		}
	}
	
	private void register() {
		mAlohar.register(APP_ID, API_KEY, new ALEventListener() {
			@Override
			public void handleEvent(ALEvents event, Object data) {
				if(event == ALEvents.REGISTRATION_CALLBACK) {
					if(data instanceof String) {
						InfoCityPreferences.setAloharUID((String)data);
					}
				} else if(event == ALEvents.GENERAL_ERROR_CALLBACK ||
						  event == ALEvents.SERVER_ERROR_CALLBACK) {
				}
			}
		});
	}
	
	private void authenticate(String uid) {
		mAlohar.authenticate(uid, APP_ID, API_KEY, new ALEventListener(){
		    @Override
		    public void handleEvent(ALEvents event, Object data) {
		        if (event == ALEvents.AUTHENTICATE_CALLBACK) {
		            if (data instanceof String) {
						InfoCityPreferences.setAloharUID((String)data);
		            }
		        } else if (event == ALEvents.GENERAL_ERROR_CALLBACK 
		        		|| event == ALEvents.SERVER_ERROR_CALLBACK) {
		                // Fail to register or authenticate
		        }
		    }
		});
	}
	
	public void start() {
		stop();
		mAlohar.startServices();
	}
	public void stop() {
		if(mAlohar.isServiceRunning()) {
			mAlohar.stopServices();
		}
	}
}
