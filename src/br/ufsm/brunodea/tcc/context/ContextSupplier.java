package br.ufsm.brunodea.tcc.context;

import android.os.Handler;

public interface ContextSupplier {
	public enum ContextAction {
		NONE(-1), ADD_EVENT(0), FETCH_EVENTS(1);
		
		private int value;
		private ContextAction(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
	}
	
	/**
	 * Valores possíveis para o handler.what;
	 */
	public static final int ALOHAR_USERSTAY_CHANGED = 0;
	public static final int ALOHAR_ON_ARRIVAL = 1;
	public static final int ALOHAR_ON_DEPARTURE = 2;
	public static final int ALOHAR_ON_MOVEMENT_CHANGED = 3;
	public static final int ALOHAR_USER_REGISTERED = 4;
	public static final int ALOHAR_USER_AUTHENTICATED = 5;
	
	public ContextData getContextData();
	public void setHandler(Handler handler);
}
