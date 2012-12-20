package br.ufsm.brunodea.tcc.comment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.internet.InfoCityServer;
import br.ufsm.brunodea.tcc.internet.facebook.InfoCityFacebook;
import br.ufsm.brunodea.tcc.model.EventComment;

public class EventCommentActivity extends Activity implements OnClickListener {

	private TextView mTextViewInfo;
	private TextView mTextViewLoadingComments;
	private ProgressBar mProgressBarLoadingComments;
	private ProgressBar mProgressBarSavingComment;
	
	private ListView mListViewComments;
	
	private EditText mEditTextAddComment;
	private ImageButton mButtonAddComment;
	
	private int mEventID;
	
	@Override
	public void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.event_comments);
		
		mEventID = -1;
		if(getIntent() != null) {
			if(getIntent().hasExtra("event_id")) {
				mEventID = getIntent().getIntExtra("event_id", -1);
			}
		}

		initGUI();
		loadComments();
	}
	
	private void initGUI() {
		mTextViewLoadingComments = (TextView) findViewById(R.id.textview_loading_comments);
		mProgressBarLoadingComments = (ProgressBar) findViewById(R.id.progressbar_loading_comments);
		mListViewComments = (ListView) findViewById(R.id.listview_comments);
		mEditTextAddComment = (EditText) findViewById(R.id.edittext_add_comment);
		mButtonAddComment = (ImageButton) findViewById(R.id.button_add_comment);
		mProgressBarSavingComment = (ProgressBar) findViewById(R.id.progressbar_saving_comment);
		mTextViewInfo = (TextView) findViewById(R.id.textview_comments_info);
		
		mButtonAddComment.setOnClickListener(this);
	}
	
	private void adjustVisibilities(boolean is_loading_comments) {
		int visibility = is_loading_comments ? View.VISIBLE : View.GONE;
		int inverse_visibility = is_loading_comments ? View.GONE : View.VISIBLE;
		
		mTextViewLoadingComments.setVisibility(visibility);
		mProgressBarLoadingComments.setVisibility(visibility);

		mListViewComments.setVisibility(inverse_visibility);
		mButtonAddComment.setVisibility(inverse_visibility);
		mEditTextAddComment.setVisibility(inverse_visibility);
		
		boolean logged = InfoCityFacebook.isLogged();
		mButtonAddComment.setEnabled(logged);
		mEditTextAddComment.setEnabled(logged);
	}
	
	private void loadComments() {
		adjustVisibilities(true);
		final Handler done = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				adjustVisibilities(false);
				if(msg.what == 0) {
					mListViewComments.setAdapter(new CommentArrayAdapter(EventCommentActivity.this,
							(ArrayList<EventComment>) msg.obj));
				} else if(msg.what == 1) {
					mTextViewInfo.setVisibility(View.VISIBLE);
					mTextViewInfo.setText(getResources().getString(R.string.no_comments));
				} else if(msg.what == -1) {
					Toast.makeText(EventCommentActivity.this, getResources().getString(R.string.server_error), 
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		Thread t = new Thread() {
			@Override
			public void run() {
				Message msg = done.obtainMessage();
				JSONObject res = InfoCityServer.getEventComments(EventCommentActivity.this, mEventID);
				if(res != null && res.has("size")) {
					try {
						int size = res.getInt("size");
						ArrayList<EventComment> comments = new ArrayList<EventComment>();
						for(int i = 0; i < size; i++) {
							JSONObject c = res.getJSONObject("comment_"+i);
							String user_id = c.getString("user_id");
							String user_name = c.getString("user_name");
							String comment = c.getString("comment");
							
							comments.add(new EventComment(mEventID, user_id, user_name, comment));
						}
						
						if(size > 0) {
							msg.obj = comments;
							msg.what = 0;
						} else {
							msg.what = 1;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						msg.what = -1;
					}
				} else {
					msg.what = -1;
				}
				done.sendMessage(msg);
			}
		};
		
		t.start();
	}

	private EventComment createEventComment(String comment) {
		return new EventComment(mEventID, InfoCityFacebook.getUser().getId(),
				InfoCityFacebook.getUser().getName(), comment);
	}
	
	@Override
	public void onClick(View v) {
		if(v == mButtonAddComment) {
			if(!InfoCityFacebook.isLogged()) {
				Toast.makeText(this, getResources().getString(R.string.cannot_comment), 
						Toast.LENGTH_SHORT).show();
				return;
			}

			final String comment = mEditTextAddComment.getText().toString().trim();
			if(comment.equals("")) {
				Toast.makeText(this, getResources().getString(R.string.comment_not_empty), 
						Toast.LENGTH_SHORT).show();
			} else {
				mButtonAddComment.setEnabled(false);
				mProgressBarSavingComment.setVisibility(View.VISIBLE);
				final Handler done = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						mProgressBarSavingComment.setVisibility(View.GONE);
						mButtonAddComment.setEnabled(true);
						if(msg.what == 1) {
							mEditTextAddComment.setText("");
							EventComment event_comment = createEventComment(comment); 
							CommentArrayAdapter caa = (CommentArrayAdapter) mListViewComments.getAdapter();
							caa.add(event_comment);
							mListViewComments.setAdapter(caa);
							mTextViewInfo.setVisibility(View.GONE);
						}
					}
				};
				
				Thread t = new Thread() {
					@Override
					public void run() {
						EventComment event_comment = createEventComment(comment); 
						JSONObject res = InfoCityServer.saveEventComment(EventCommentActivity.this, 
								event_comment);
						int what = 0;
						if(res != null) {
							what = 1;
						}

						done.sendEmptyMessage(what);
					}
				};
				t.start();
			}
		}
	}
}
