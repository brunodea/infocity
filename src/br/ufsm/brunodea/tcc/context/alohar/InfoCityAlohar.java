package br.ufsm.brunodea.tcc.context.alohar;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import br.ufsm.brunodea.tcc.context.ContextData;
import br.ufsm.brunodea.tcc.context.ContextSupplier;
import br.ufsm.brunodea.tcc.util.InfoCityPreferences;

import com.alohar.core.Alohar;
import com.alohar.user.callback.ALEventListener;
import com.alohar.user.callback.ALMovementListener;
import com.alohar.user.callback.ALPlaceEventListener;
import com.alohar.user.content.ALMotionManager;
import com.alohar.user.content.ALPlaceManager;
import com.alohar.user.content.data.ALEvents;
import com.alohar.user.content.data.MovementState;
import com.alohar.user.content.data.Place;
import com.alohar.user.content.data.UserStay;

public class InfoCityAlohar implements ContextSupplier {
	private final int APP_ID = 239;
	private final String API_KEY = "5a470a7e524a5959dea5620cd6a593c10b60eb56";
	
	private Alohar mAlohar;
	private ALPlaceManager mPlaceManager;
	private ALMotionManager mMotionManager;
	
	private ContextData mCurrContextData;
	private Handler mHandler;
	
	private boolean mIsAuthenticated;
	
	private Context mContext;
	
	public InfoCityAlohar(Application app) {
		Alohar.init(app);
		mContext = app.getApplicationContext();
		mAlohar = Alohar.getInstance();
		start();

		mIsAuthenticated = false;
		String uid = InfoCityPreferences.getAloharUID(mContext);
		if(uid != null && !uid.equals("")) {
			authenticateUser();
		} else {
			registerUser();
			authenticateUser();
		}
		
		mPlaceManager = mAlohar.getPlaceManager();
		mMotionManager = mAlohar.getMotionManager();
		mCurrContextData = new ContextData();
		mHandler = new Handler() {
			//default: handler que não faz nada.
		};
		
		
		if(mIsAuthenticated) {
			initListeners();
		}
	}
	
	private void initListeners() {
		mPlaceManager.registerPlaceEventListener(new ALPlaceEventListener() {
			@Override
			public void onUserStayChanged(UserStay newUserStay) {
				if(userStayToContextData(newUserStay)) {
					mHandler.sendEmptyMessage(ContextSupplier.ALOHAR_USERSTAY_CHANGED);
				}
			}
			
			@Override
			public void onDeparture(double latitude, double longitude) {
				mHandler.sendEmptyMessage(ContextSupplier.ALOHAR_ON_DEPARTURE);
			}
			
			@Override
			public void onArrival(double latitude, double longitude) {
				mHandler.sendEmptyMessage(ContextSupplier.ALOHAR_ON_ARRIVAL);
			}
		});

		mMotionManager.registerMovementListener(new ALMovementListener() {
			@Override
			public void onMovementChanged(MovementState oldState, MovementState newState) {
				mHandler.sendEmptyMessage(ContextSupplier.ALOHAR_ON_MOVEMENT_CHANGED);
			}
		});
	}

	private void registerUser() {
		mAlohar.register(APP_ID, API_KEY, new ALEventListener() {
			@Override
			public void handleEvent(ALEvents event, Object data) {
				if(event == ALEvents.REGISTRATION_CALLBACK) {
					if(data instanceof String && !data.toString().equals("")) {
						InfoCityPreferences.setAloharUID(mContext, (String)data);
					}
				} else if(event == ALEvents.GENERAL_ERROR_CALLBACK ||
						  event == ALEvents.SERVER_ERROR_CALLBACK) {
				}
			}
		});
	}
	
	private void authenticateUser() {
		mAlohar.authenticate(InfoCityPreferences.getAloharUID(mContext),
				APP_ID, API_KEY, new ALEventListener(){
		    @Override
		    public void handleEvent(ALEvents event, Object data) {
		        if (event == ALEvents.AUTHENTICATE_CALLBACK) {
		            if (data instanceof String) {
						mIsAuthenticated = true;
		            }
		        } else if (event == ALEvents.GENERAL_ERROR_CALLBACK 
		        		|| event == ALEvents.SERVER_ERROR_CALLBACK) {
		                // Fail to register or authenticate
		        }
		    }
		});
	}
	
	@Override
	public void start() {
		if(!mAlohar.isServiceRunning()) {
			mAlohar.startServices();
		}
	}
	@Override
	public void stop() {
		if(mAlohar.isServiceRunning()) {
			mAlohar.stopServices();
		}
	}
	
	private boolean userStayToContextData(UserStay userstay) {
		boolean ret = false;
		if(userstay != null && userstay.hasSelectedPlace()) {
			Place place = userstay.getSelectedPlace();
			if(place != null) {
				mCurrContextData.setAddress(place.getAddress());
				mCurrContextData.setPlaceName(place.getName());
				mCurrContextData.setPlaceType(place.getCategory()+","+place.getSubCategory());
				
				ret = true;
			}
		}
		
		return ret;
	}

	@Override
	public ContextData getContextData() {
		UserStay lastKnownStay = mPlaceManager.getLastKnownStay();
		userStayToContextData(lastKnownStay);
		mCurrContextData.setMovementState(mMotionManager.getCurrentMovementState().name());
		mCurrContextData.setOnCommute(mMotionManager.isOnCommute());
		
		return mCurrContextData;
	}

	@Override
	public void setHandler(Handler handler) {
		mHandler = handler;
	}
}
