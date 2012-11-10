package br.ufsm.brunodea.tcc.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import br.ufsm.brunodea.tcc.R;

public class DialogHelper {
	/**
     * Display a dialog that user has no internet connection
     * @param ctx1
     *
     * Code from: http://osdir.com/ml/Android-Developers/2009-11/msg05044.html
     */
    public static void showNoConnectionDialog(Context ctx1) {
        final Context ctx = ctx1;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setMessage(R.string.go_to_internet_settings);
        builder.setTitle(R.string.no_internet_dialog_title);
        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ctx.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });

        builder.show();
    }
    
    public static void yesNoDialog(Context c, String title, String description, 
    		final Handler handler) {
    	 AlertDialog.Builder builder = new AlertDialog.Builder(c);
         builder.setCancelable(true);
         builder.setTitle(title);
         builder.setMessage(description);
         
         builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
            	 handler.sendEmptyMessage(0);
             }
         });
         builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
                 handler.sendEmptyMessage(1);
             }
         });
    }
}
