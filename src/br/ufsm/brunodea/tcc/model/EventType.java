package br.ufsm.brunodea.tcc.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.model.EventTypeManager.TypeName;


public class EventType {
	private TypeName mName;
	private String mReprString;
	
	public EventType(TypeName name, String repr_str) {
		mName = name;
		mReprString = repr_str;
	}
	
	public TypeName getName() {
		return mName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof EventType)) return false;
		
		EventType eventtype = (EventType)obj;
		return mName.equals(eventtype.getName());
	}
	
	@Override
	public String toString() {
		return mReprString;
	}
	
	
	public Drawable getDrawable(Context c) {
		Drawable marker = null;

		switch(getName()) {
		case ADD:
			marker = c.getResources().getDrawable(R.drawable.ic_addevent);
			break;
		case UNKNOWN:
			marker = c.getResources().getDrawable(R.drawable.ic_unknownevent);
			break;
		default:
			marker = c.getResources().getDrawable(R.drawable.ic_launcher);
			break;
		}
		
		return marker;
	}
}
