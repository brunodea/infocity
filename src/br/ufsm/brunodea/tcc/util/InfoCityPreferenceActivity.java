package br.ufsm.brunodea.tcc.util;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.Window;
import android.widget.Toast;
import br.ufsm.brunodea.tcc.R;

public class InfoCityPreferenceActivity extends PreferenceActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        addPreferencesFromResource(R.xml.preferences);
        EditTextPreference event_radius = (EditTextPreference) getPreferenceScreen().findPreference("event_radius");
        event_radius.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean ok = true;
				if(newValue.toString().equals("")) {
					ok = false;
					Toast.makeText(InfoCityPreferenceActivity.this, 
							getResources().getString(R.string.not_empty), Toast.LENGTH_SHORT).show();
				}
				return ok;
			}
		});
    }
    
    
}
