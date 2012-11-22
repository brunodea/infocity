package br.ufsm.brunodea.tcc.context;

import android.os.Handler;

public interface ContextSupplier {
	/**
	 * Valores possíveis para o handler.what;
	 */
	public static final int ALOHAR_USERSTAY_CHANGED = 0;
	public static final int ALOHAR_ON_ARRIVAL = 1;
	public static final int ALOHAR_ON_DEPARTURE = 2;
	public static final int ALOHAR_ON_MOVEMENT_CHANGED = 3;
	
	public void start();
	public void stop();
	public ContextData getContextData();
	public void setHandler(Handler handler);
}
