package br.ufsm.brunodea.tcc.internet.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import br.ufsm.brunodea.tcc.R;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.UserSettingsFragment;

public class FacebookLoginLogoutActivity extends FragmentActivity {
    private UserSettingsFragment userSettingsFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.facebook);

        setResult(0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.fragment_facebook_usersettings);
        userSettingsFragment.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
            	if(session.isOpened()) {
					Request.executeMeRequestAsync(session, new GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user, Response response) {
							InfoCityFacebook.setUser(user);
						}
					});
            	} else {
            		InfoCityFacebook.setUser(null);
            	}
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        userSettingsFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}