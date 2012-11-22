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
        
        mEventRadius.setSummary(InfoCityPreferences.getEventMaxRadius(this) + "m");
        mServerIP.setSummary(InfoCityPreferences.getServerIP(this));
        mServerPort.setSummary(InfoCityPreferences.getServerPort(this)+"");
        
        mEventRadius.setOnPreferenceChangeListener(this);
        mServerIP.setOnPreferenceChangeListener(this);
        mServerPort.setOnPreferenceChangeListener(this);
        
        mEnableCompass.setOnPreferenceChangeListener(this);
        mEnableMyLocation.setOnPreferenceChangeListener(this);
    }
    
    private void showToastNotEmpty() {
		Toast.makeText(InfoCityPreferenceActivity.this, 
				getResources().getString(R.string.not_empty), Toast.LENGTH_SHORT).show();
    }

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		boolean ok = false;
		if(newValue.toString().equals("")) {
			showToastNotEmpty();
		} else {
			if(preference == mEventRadius) {
				ok = true;
				setResult(1);
			} else if(preference == mServerIP) {
				String ip = newValue.toString();
				if(ip.matches("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|" +
						"2[0-4]\\d|[0-1]?\\d?\\d)){3}$")) {
					ok = true;
				} else {
					Toast.makeText(InfoCityPreferenceActivity.this, 
							getResources().getString(R.string.invalid_ip), 
							Toast.LENGTH_SHORT).show();
				}
			} else if(preference == mServerPort) {
				String port = newValue.toString();
				if(port.matches("\\d\\d\\d\\d(\\d)?")) {
					ok = true;
				} else {
					Toast.makeText(InfoCityPreferenceActivity.this, 
							getResources().getString(R.string.invalid_port), 
							Toast.LENGTH_SHORT).show();
				}
			} else if(preference == mEnableCompass || preference == mEnableMyLocation) {
				ok = true;
				setResult(2);
			}
		}
		
		return ok;
	}
}
