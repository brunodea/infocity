package br.ufsm.brunodea.tcc.util;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.Window;
import android.widget.Toast;
import br.ufsm.brunodea.tcc.R;

public class InfoCityPreferenceActivity extends PreferenceActivity 
										implements OnPreferenceChangeListener{

	public static final int REQUEST_CODE = 0;
	public static final int RESULT_CODE_EVENT_RADIUS = 1;
	public static final int RESULT_CODE_MYLOCATION = 2;
	
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
    }
    
    private void setListeners() {
        mEventRadius.setOnPreferenceChangeListener(this);
        mServerIP.setOnPreferenceChangeListener(this);
        mServerPort.setOnPreferenceChangeListener(this);
        
        mEnableCompass.setOnPreferenceChangeListener(this);
        mEnableMyLocation.setOnPreferenceChangeListener(this);
    }
    
    private void setPreValues() {        
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
			}
		}
		
		setResult(result);
		
		return ok;
	}
}
