package br.ufsm.brunodea.tcc.util;

import java.util.ArrayList;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.Window;
import android.widget.Toast;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.model.EventType;
import br.ufsm.brunodea.tcc.model.EventTypeManager;
import br.ufsm.brunodea.tcc.model.EventTypeManager.TypeName;

public class InfoCityPreferenceActivity extends PreferenceActivity 
										implements OnPreferenceChangeListener{

	public static final int REQUEST_CODE = 0;
	public static final int RESULT_CODE_EVENT_RADIUS = 1;
	public static final int RESULT_CODE_MYLOCATION = 2;
	
	private ListPreference mFiltersEventType;
	
	private EditTextPreference mMaxEvents;
	private EditTextPreference mEventRadius;
	private EditTextPreference mServerIP;
	private EditTextPreference mServerPort;
	
	private CheckBoxPreference mEnableMyLocation;
	private CheckBoxPreference mEnableCompass;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        setResult(0);
        addPreferencesFromResource(R.xml.preferences);
        
        initGUI();
        setListeners();
        setPreValues();
    }
    
    private void initGUI() {
    	mFiltersEventType = (ListPreference)
    			getPreferenceScreen().findPreference("filters_eventtype");
        mMaxEvents = (EditTextPreference)
        		getPreferenceScreen().findPreference("max_events");
        mEventRadius = (EditTextPreference)
        		getPreferenceScreen().findPreference("event_radius");
        mServerIP = (EditTextPreference)
        		getPreferenceScreen().findPreference("server_ip");
        mServerPort = (EditTextPreference)
        		getPreferenceScreen().findPreference("server_port");
        mEnableMyLocation = (CheckBoxPreference)
        		getPreferenceScreen().findPreference("enable_mylocation");
        mEnableCompass = (CheckBoxPreference)
        		getPreferenceScreen().findPreference("enable_compass");

        ArrayList<EventType> types = EventTypeManager.instance().types();
        types.add(0, EventTypeManager.instance().type_all());
        
        String []entries = new String[types.size()];
        String []values = new String[types.size()];
        for(int i = 0; i < types.size(); i++) {
        	EventType type = types.get(i);
        	String key = type.toString();
        	String value = type.getName().toString();
        	entries[i] = key;
        	values[i] = value;
        }
        mFiltersEventType.setEntries(entries);
        mFiltersEventType.setEntryValues(values);
    }
    
    private void setListeners() {
    	mFiltersEventType.setOnPreferenceChangeListener(this);
    	
    	mMaxEvents.setOnPreferenceChangeListener(this);
        mEventRadius.setOnPreferenceChangeListener(this);
        mServerIP.setOnPreferenceChangeListener(this);
        mServerPort.setOnPreferenceChangeListener(this);
        
        mEnableCompass.setOnPreferenceChangeListener(this);
        mEnableMyLocation.setOnPreferenceChangeListener(this);
    }
    
    private void setPreValues() {
    	mFiltersEventType.setSummary(InfoCityPreferences.eventTypeFilter(this).toString());
    	
    	mMaxEvents.setSummary(InfoCityPreferences.getMaxEvents(this)+"");
        mEventRadius.setSummary(InfoCityPreferences.getEventMaxRadius(this) + "m");
        mServerIP.setSummary(InfoCityPreferences.getServerIP(this));
        mServerPort.setSummary(InfoCityPreferences.getServerPort(this)+"");
        
        mEnableMyLocation.setChecked(InfoCityPreferences.shouldEnableMyLocation(this));
        mEnableCompass.setChecked(InfoCityPreferences.shouldEnableCompass(this));
    }
    
    private void showToastNotEmpty() {
		Toast.makeText(InfoCityPreferenceActivity.this, 
				getResources().getString(R.string.not_empty), Toast.LENGTH_SHORT).show();
    }
    
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		boolean ok = false;
		int result = 0;
		if(newValue.toString().equals("")) {
			showToastNotEmpty();
		} else {
			if(preference == mEventRadius) {
				ok = true;
				result = RESULT_CODE_EVENT_RADIUS;
				preference.setSummary(newValue.toString()+"m");
			} else if(preference == mMaxEvents) {
				ok = true;
				preference.setSummary(newValue.toString());
			} else if(preference == mServerIP) {
				String ip = newValue.toString();
				if(ip.matches("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|" +
						"2[0-4]\\d|[0-1]?\\d?\\d)){3}$")) {
					ok = true;
					preference.setSummary(newValue.toString());
				} else {
					Toast.makeText(InfoCityPreferenceActivity.this, 
							getResources().getString(R.string.invalid_ip), 
							Toast.LENGTH_SHORT).show();
				}
			} else if(preference == mServerPort) {
				String port = newValue.toString();
				if(port.matches("\\d\\d\\d\\d(\\d)?")) {
					ok = true;
					preference.setSummary(newValue.toString());
				} else {
					Toast.makeText(InfoCityPreferenceActivity.this, 
							getResources().getString(R.string.invalid_port), 
							Toast.LENGTH_SHORT).show();
				}
			} else if(preference == mEnableCompass || preference == mEnableMyLocation) {
				ok = true;
				result = RESULT_CODE_MYLOCATION;
			} else if(preference == mFiltersEventType) {
				ok = true;
				preference.setSummary(EventTypeManager.instance().eventTypeFromTypeName(
						TypeName.fromValue(newValue.toString())).toString());
			}
		}
		
		setResult(result);
		
		return ok;
	}
}
