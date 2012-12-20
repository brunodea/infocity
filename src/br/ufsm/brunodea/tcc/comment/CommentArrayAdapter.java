package br.ufsm.brunodea.tcc.comment;

import java.util.ArrayList;

import com.facebook.widget.ProfilePictureView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.model.EventComment;

public class CommentArrayAdapter extends ArrayAdapter<EventComment> {

	private Context mContext;
	private ArrayList<EventComment> mComments;
	
	public CommentArrayAdapter(Context c, ArrayList<EventComment> comments) {
		super(c, R.layout.event_comment_row, comments);
		
		mContext = c;
		mComments = comments;
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.event_comment_row, parent, false);

		TextView name = (TextView)rowView.findViewById(R.id.textview_username);
		TextView comment = (TextView)rowView.findViewById(R.id.textview_comment);
		ProfilePictureView ppv = (ProfilePictureView)rowView.findViewById(R.id.profilepic);
		
		EventComment eventcomment = mComments.get(position);
		name.setText(eventcomment.getUserName());
		comment.setText(eventcomment.getComment());
		ppv.setProfileId(eventcomment.getUserID());
		
		return rowView;
	}
}
