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
	
	public ContextData getContextData();
	public void setHandler(Handler handler);
	public String toString();
	//true se está pronto para pegar o contexto.
	public boolean isReady();
}
