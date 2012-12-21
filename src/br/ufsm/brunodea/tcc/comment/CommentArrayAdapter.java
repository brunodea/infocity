package br.ufsm.brunodea.tcc.comment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

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
		TextView pubdate = (TextView)rowView.findViewById(R.id.textview_comment_date);
		
		EventComment eventcomment = mComments.get(position);
		ppv.setProfileId(eventcomment.getUserID());
		name.setText(eventcomment.getUserName());
		comment.setText(eventcomment.getComment());
		pubdate.setText(eventcomment.dateToReprString());
		
		return rowView;
	}
	
	public void sort() {
		super.sort(new Comparator<EventComment>() {
			@Override
			public int compare(EventComment lhs, EventComment rhs) {
				return lhs.getDate().after(rhs.getDate()) ? 0 : 1;
			}
		});
	}
}
