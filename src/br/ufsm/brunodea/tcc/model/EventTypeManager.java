package br.ufsm.brunodea.tcc.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import br.ufsm.brunodea.tcc.App;
import br.ufsm.brunodea.tcc.R;


public class EventTypeManager {
	public enum TypeName {
		NONE("none"), ALL("all"), ADD("add"), UNKNOWN("unknown"),
		BUS("bus"), HEALTH("health"), REPORT("report"), SHOPPING("shopping"),
		SUGGESTION("suggestion");
		
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
	public static EventTypeManager instance(Context c) {
		if(sInstance == null) {
			sInstance = new EventTypeManager(c);
		}
		
		return sInstance;
	}

	private void init() {
		type_add();
		type_none();
		type_all();
		type_unknown();
		type_bus();
		type_health();
		type_report();
		type_shopping();
		type_suggestion();
	}
	
	public EventType type_add() {
		return get_type(TypeName.ADD, "add");
	}
	public EventType type_none() {
		return get_type(TypeName.NONE, mContext.getResources().getString(R.string.type));
	}
	public EventType type_all() {
		return get_type(TypeName.ALL, mContext.getResources().getString(R.string.all));
	}
	public EventType type_unknown() {
		return get_type(TypeName.UNKNOWN, mContext.getResources().getString(R.string.other));
	}
	public EventType type_bus() {
		return get_type(TypeName.BUS, mContext.getResources().getString(R.string.bus));
	}
	public EventType type_health() {
		return get_type(TypeName.HEALTH, mContext.getResources().getString(R.string.health));
	}
	public EventType type_report() {
		return get_type(TypeName.REPORT, mContext.getResources().getString(R.string.report));
	}
	public EventType type_shopping() {
		return get_type(TypeName.SHOPPING, mContext.getResources().getString(R.string.shopping));
	}
	public EventType type_suggestion() {
		return get_type(TypeName.SUGGESTION, mContext.getResources().getString(R.string.suggestion));
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

		t.add(type_report());
		t.add(type_health());
		t.add(type_shopping());
		t.add(type_bus());
		t.add(type_suggestion());
		t.add(type_unknown());
		
		return t;
	}
}
