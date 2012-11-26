package br.ufsm.brunodea.tcc.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.model.EventTypeManager.TypeName;


public class EventType {
	private TypeName mName;
	private String mReprString;
	private Drawable mDrawable;
	
	public EventType(TypeName name, String repr_str) {
		mName = name;
		mReprString = repr_str;
		mDrawable = null;
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
		if(mDrawable == null) {
			mDrawable = createDrawable(c);
		}
		return mDrawable;
	}
	
	public Drawable createDrawable(Context c) {
		Drawable marker = null;
		
		switch(getName()) {
		case ADD:
			marker = c.getResources().getDrawable(R.drawable.ic_addevent);
			break;
		case UNKNOWN:
			marker = c.getResources().getDrawable(R.drawable.ic_unknownevent);
			break;
		default:
			marker = c.getResources().getDrawable(R.drawable.ic_unknownevent);
			break;
		}
		int w = marker.getIntrinsicWidth();
		int h = marker.getIntrinsicHeight();
		
		marker.setBounds(0, 0, w-(w/4), h-(h/4));
		
		return marker;
	}
}
