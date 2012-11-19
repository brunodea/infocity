package br.ufsm.brunodea.tcc.map;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.model.EventItem;

import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class InfoEventBalloonOverlayView <Item extends OverlayItem>
	extends BalloonOverlayView<EventItem> {

	private TextView mTextViewPubdate;
	private TextView mTextViewTitle;
	private TextView mTextViewDescription;
	private TextView mTextViewKeywords;
	
	public InfoEventBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
	}
	

	@Override
	protected void setupView(Context context, final ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.infoevent_balloon, parent);
		
		bindGUIElements(v);
	}

	@Override
	protected void setBalloonData(EventItem item, ViewGroup parent) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		mTextViewPubdate.setText(sdf.format(item.getPubDate()));
		mTextViewTitle.setText(item.getTitle());
		mTextViewDescription.setText(item.getSnippet());
		
		String keywords = "";
		ArrayList<String> itemkeywords = item.getKeywords();
		for(int i = 0; i < itemkeywords.size(); i++) {
			keywords += itemkeywords.get(i);
			if(i + 1 < itemkeywords.size()) {
				keywords += ", ";
			}
		}
		
		mTextViewKeywords.setText(keywords);
	}
	
	private void bindGUIElements(View v) {
		mTextViewPubdate = (TextView)v.findViewById(R.id.textview_pubdate_infoballoon);
		mTextViewTitle = (TextView)v.findViewById(R.id.balloon_close);
		mTextViewDescription = (TextView)v.findViewById(R.id.textview_description_infoballoon);
		mTextViewKeywords = (TextView)v.findViewById(R.id.textview_keywords_infoballoon);
	}
}
