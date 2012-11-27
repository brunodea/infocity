package br.ufsm.brunodea.tcc.util;

import android.content.Context;
import android.widget.ArrayAdapter;
import br.ufsm.brunodea.tcc.context.ContextSupplier;

class ContextSupplierAdapter extends ArrayAdapter<ContextSupplier> {	
	public ContextSupplierAdapter(Context context, int resource,
			int textViewResourceId, ContextSupplier[] objects) {
		super(context, resource, textViewResourceId, objects);
	}
	
	@Override
	public boolean areAllItemsEnabled() {
        return false;
    }
	
	@Override
	public boolean isEnabled(int position) {
		return getItem(position).isReady();
	}
}