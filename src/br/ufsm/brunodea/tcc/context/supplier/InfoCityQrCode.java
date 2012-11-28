package br.ufsm.brunodea.tcc.context.supplier;

import android.app.Activity;
import android.os.Handler;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.context.ContextData;
import br.ufsm.brunodea.tcc.context.ContextSupplier;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class InfoCityQrCode implements ContextSupplier {
	private IntentIntegrator mZxingIntentIntegrator;
	private ContextData mContextData;
	private Handler mHandler;
	
	private ContextAction mContextAction;
	
	public InfoCityQrCode(Activity parent) {
		mZxingIntentIntegrator = new IntentIntegrator(parent);
		mZxingIntentIntegrator.setTitleByID(R.string.install_barcodescanner);
		mZxingIntentIntegrator.setMessageByID(R.string.requires_barcodescanner);

		mHandler = null;
		mContextData = new ContextData();
	}
	
	public void finishedScan(IntentResult intentresult) {
		String qrcode_json = intentresult.getContents();
		if(qrcode_json != null && !qrcode_json.equals("")) {
			try {
				Gson gson = new Gson();
				mContextData = gson.fromJson(qrcode_json, ContextData.class);
				mContextData.setFrom(toString());

				if(mHandler != null) {
					mHandler.sendEmptyMessage(mContextAction.getValue());
				}
			} catch(JsonSyntaxException e) {
				e.printStackTrace();
				if(mHandler != null) {
					mHandler.sendEmptyMessage(ContextAction.NONE.getValue());
				}
			}
		}
	}
	
	public void beginScan(ContextAction context_action) {
		mContextAction = context_action;
		mZxingIntentIntegrator.initiateScan();
	}

	@Override
	public ContextData getContextData() {
		return mContextData;
	}
	@Override
	public void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	@Override
	public String toString() {
		return "Qr-Code";
	}

	@Override
	public boolean isReady() {
		return true;
	}

}
