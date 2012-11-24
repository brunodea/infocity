package br.ufsm.brunodea.tcc.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import br.ufsm.brunodea.tcc.App;
import br.ufsm.brunodea.tcc.R;


public class EventTypeManager {
	public enum TypeName {
		NONE("none"), ADD("add"), UNKNOWN("unknown");
		
		private final String value;
		private TypeName(String val) {
			value = val;
		}
		
		public static TypeName fromValue(String value) {
			TypeName res = TypeName.NONE;
			
			for(TypeName t : TypeName.values()) {
				if(t.value.equals(value)) {
					res = t;
					break;
				}
			}
			
			return res;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	private static EventTypeManager sInstance = null;
	
	private HashMap<TypeName, EventType> mTypeNameEventTypeMap;
	private Context mContext;
	
	private EventTypeManager(Context c) {
		mTypeNameEventTypeMap = new HashMap<TypeName, EventType>();
		mContext = c;
		init();
	}
	
	public static EventTypeManager instance() {
		if(sInstance == null) {
			sInstance = new EventTypeManager(App.instance());
		}
		
		return sInstance;
	}

	private void init() {
		type_add();
		type_none();
		type_unknown();
	}
	
	public EventType type_add() {
		return get_type(TypeName.ADD, "add");
	}
	public EventType type_none() {
		return get_type(TypeName.NONE, "none");
	}
	public EventType type_unknown() {
		return get_type(TypeName.ADD, mContext.getResources().getString(R.string.unknown));
	}
	
	private EventType get_type(TypeName type_name, String repr) {
		EventType type = null;
		if(mTypeNameEventTypeMap.containsKey(type_name)) {
			type = mTypeNameEventTypeMap.get(type_name);
		} else {
			type = new EventType(type_name, repr);
			mTypeNameEventTypeMap.put(type_name, type);
		}
		
		return type;
	}
	
	public EventType eventTypeFromTypeName(TypeName name) {
		return mTypeNameEventTypeMap.get(name);
	}
	
	public ArrayList<EventType> types() {
		ArrayList<EventType> t = new ArrayList<EventType>();
		
		t.add(type_unknown());
		
		return t;
	}
}