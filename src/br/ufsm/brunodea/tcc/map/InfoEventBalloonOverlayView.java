package br.ufsm.brunodea.tcc.map;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.internet.InfoCityServer;
import br.ufsm.brunodea.tcc.internet.facebook.InfoCityFacebook;
import br.ufsm.brunodea.tcc.model.EventItem;

import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

/**
 * Classe que monta a forma como os dados dos eventos serão apresentados no balão
 * do tipo INFO.
 * Isso é feito simplesmente inflando o layout infoevent_balloon e setando os
 * valores dos TextViews.
 * 
 * @author bruno
 *
 * @param <Item>
 */
public class InfoEventBalloonOverlayView <Item extends OverlayItem>
	extends BalloonOverlayView<EventItem> implements OnClickListener {

	private TextView mTextViewPubdate;
	private TextView mTextViewTitle;
	private TextView mTextViewDescription;
	private TextView mTextViewKeywords;
	
	private TextView mTextViewLike;
	private TextView mTextViewDislike;
	
	private ImageButton mImageButtonLike;
	private ImageButton mImageButtonDislike;
	
	private ProgressBar mProgressBarLikeDislike;
	
	private EventItem mEventItem;
	private Context mContext;
	
	public InfoEventBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
		mContext = context;
	}
	

	@Override
	protected void setupView(Context context, final ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.infoevent_balloon, parent);
		
		mEventItem = null;
		bindGUIElements(v);
	}

	@Override
	protected void setBalloonData(EventItem item, ViewGroup parent) {
		mEventItem = item;

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
		
		Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				adjustLikeDislike();				
			}
		};
		
		adjustLikeAction(h);
	}
	
	private void bindGUIElements(View v) {
		mTextViewPubdate = (TextView)v.findViewById(R.id.textview_pubdate_infoballoon);
		mTextViewTitle = (TextView)v.findViewById(R.id.balloon_close);
		mTextViewDescription = (TextView)v.findViewById(R.id.textview_description_infoballoon);
		mTextViewKeywords = (TextView)v.findViewById(R.id.textview_keywords_infoballoon);
		
		mImageButtonLike = (ImageButton)v.findViewById(R.id.imagebutton_like);
		mImageButtonLike.setOnClickListener(this);
		
		mImageButtonDislike = (ImageButton)v.findViewById(R.id.imagebutton_dislike);
		mImageButtonDislike.setOnClickListener(this);
		
		mTextViewLike = (TextView)v.findViewById(R.id.textview_likes);
		mTextViewDislike = (TextView)v.findViewById(R.id.textview_dislikes);
		
		mProgressBarLikeDislike = (ProgressBar)v.findViewById(R.id.progressbar_like_dislike);
	}

	private void adjustLikeDislike() {
		mTextViewLike.setText(mEventItem.getLikes()+"");
		mTextViewDislike.setText(mEventItem.getDislikes()+"");
		if(!InfoCityFacebook.isLogged()) {
			mImageButtonLike.setEnabled(false);
			mImageButtonDislike.setEnabled(false);
			
			mImageButtonLike.setImageResource(R.drawable.ic_thumbs_up_off);
			mImageButtonDislike.setImageResource(R.drawable.ic_thumbs_down_off);
		} else {
			mImageButtonLike.setEnabled(true);
			mImageButtonDislike.setEnabled(true);

			int like_action = mEventItem.getLikeAction();
			boolean thumbsup = like_action == -1 || like_action == 1;
			boolean thumbsdown = like_action == -1 || like_action == 0;

			if(thumbsup) {
				mImageButtonLike.setImageResource(R.drawable.ic_thumbs_up);
			} else {
				mImageButtonLike.setImageResource(R.drawable.ic_thumbs_up_off);
			}

			if(thumbsdown) {
				mImageButtonDislike.setImageResource(R.drawable.ic_thumbs_down);
			} else {
				mImageButtonDislike.setImageResource(R.drawable.ic_thumbs_down_off);
			}
		}
	}

	private void adjustLikeAction(final Handler done_handler) {
		mProgressBarLikeDislike.setVisibility(View.VISIBLE);
		mImageButtonLike.setEnabled(false);
		mImageButtonDislike.setEnabled(false);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				mProgressBarLikeDislike.setVisibility(View.GONE);
				mImageButtonLike.setEnabled(true);
				mImageButtonDislike.setEnabled(true);
			}
		};
		Thread t_like_action = new Thread() {
			@Override
			public void run() {
				if(InfoCityFacebook.isLogged()) {
					JSONObject res = InfoCityServer.getLikeAction(mContext, mEventItem);
					if(res.has("like_action")) {
						try {
							mEventItem.setLikeAction(res.getInt("like_action"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				
				JSONObject res = InfoCityServer.countLikesDislikes(mContext, mEventItem);
				try {
					mEventItem.setLikes(res.getInt("likes"));
					mEventItem.setDislikes(res.getInt("dislikes"));
				} catch(JSONException e) {
					e.printStackTrace();
				}

				handler.sendEmptyMessage(0);
				if(done_handler != null) {
					done_handler.sendEmptyMessage(0);
				}
			}
		};
		t_like_action.start();
	}
	
	@Override
	public void onClick(View v) {
		if(mEventItem != null) {
			mProgressBarLikeDislike.setVisibility(View.VISIBLE);
			mImageButtonLike.setEnabled(false);
			mImageButtonDislike.setEnabled(false);
			
			final int like_action = mEventItem.getLikeAction();
			final int likes = mEventItem.getLikes();
			final int dislikes = mEventItem.getDislikes();
			if(v == mImageButtonLike) {
				mEventItem.doLike();
			} else if(v == mImageButtonDislike) {
				mEventItem.doDislike();
			}
			
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if(msg.what == 0) {
						adjustLikeDislike();
					} else {
						mEventItem.setLikeAction(like_action);
						mEventItem.setLikes(likes);
						mEventItem.setDislikes(dislikes);
						
						Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), 
								Toast.LENGTH_SHORT).show();
					}
					mImageButtonLike.setEnabled(true);
					mImageButtonDislike.setEnabled(true);
					mProgressBarLikeDislike.setVisibility(View.GONE);
				}
			};
			
			Thread t = new Thread() {
				@Override
				public void run() {
					JSONObject res = InfoCityServer.likeEvent(mContext, mEventItem);
					handler.sendEmptyMessage(res.has("ok") ? 0 : 1);
				}
			};
			t.start();
			
		}
	}
}
