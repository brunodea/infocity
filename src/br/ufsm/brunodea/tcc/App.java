package br.ufsm.brunodea.tcc;

import android.app.Application;

public class App extends Application {
	private static App sInstance = null;
	public App() {
		sInstance = this;
	}
	
	public static App instance() {
		return sInstance;
	}
}
