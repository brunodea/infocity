package br.ufsm.brunodea.tcc.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.model.EventItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class EventsItemizedOverlay extends BalloonItemizedOverlay<EventItem> {
	private ArrayList<EventItem> mEventOverlays;
	
	public enum BalloonType {
		INFO, ADD
	}

	private Drawable mMarker;
	private BalloonType mBalloonType;
	
	private EventItem mDragItem;
	private ImageView mDragImage;

    private int xDragImageOffset = 0;
    private int yDragImageOffset = 0;
    private int xDragTouchOffset = 0;
    private int yDragTouchOffset = 0;
	
	public EventsItemizedOverlay(Context c, Drawable marker, MapView mapView, 
			BalloonType balloon_type) {
		super(boundCenterBottom(marker), mapView);
		mEventOverlays = new ArrayList<EventItem>();
		
		mDragImage = (ImageView) ((View)mapView.getParent()).findViewById(R.id.imageview_map_drag);
		mDragImage.setImageDrawable(marker);
		mDragItem = null;
		
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), 
				marker.getIntrinsicHeight());
		mMarker = marker;
		mBalloonType = balloon_type;
	}
	
	public void addEventItem(EventItem eventitem) {
		mEventOverlays.add(eventitem);
		populate();
	}
	
	public void removeEventItem(EventItem eventitem) {
		mEventOverlays.remove(eventitem);
		populate();
	}
	
	@Override
	protected EventItem createItem(int i) {
		return mEventOverlays.get(i);
	}

	@Override
	public int size() {
		return mEventOverlays.size();
	}
	
	@Override
	protected boolean onBalloonTap(int index, EventItem item) {
		return true;
	}
	
	@Override
	protected BalloonOverlayView<EventItem> createBalloonOverlayView() {
		BalloonOverlayView<EventItem> res = null;
		switch(mBalloonType) {
		case INFO:
			res = super.createBalloonOverlayView();
			break;
		case ADD:
			res = new AddEventBalloonOverlayView<EventItem>(
					getMapView().getContext(), 0);
			break;
		default:
			res = super.createBalloonOverlayView();
		}
		
		return res;
	}

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
    	final int action=event.getAction();
        final int x=(int)event.getX();
        final int y=(int)event.getY();
        
        boolean result = false;
    	
        if(action == MotionEvent.ACTION_DOWN) {
        	setFocus(null);
        	for(final EventItem item : mEventOverlays) {
                Point p = new Point(0,0);
                mapView.getProjection().toPixels(item.getPoint(), p);

                if(hitTest(item, mMarker, x-p.x, y-p.y)) {
                	result = true;
                	
                	mDragItem = item;
                	setFocus(item);
                	mEventOverlays.remove(item);
                	populate();
                	
                	xDragTouchOffset = 0;
                	yDragTouchOffset = 0;

                	setDragImagePosition(p.x, p.y);
                	mDragImage.setVisibility(View.VISIBLE);

                	xDragTouchOffset = x-p.x;
                  	yDragTouchOffset = y-p.y;

                  	break;
                }
        	}
        } else if (action==MotionEvent.ACTION_UP && mDragItem != null) {
        	mDragImage.setVisibility(View.GONE);
            
            GeoPoint pt = mapView.getProjection().fromPixels(x-xDragTouchOffset,
                                                         	 y-yDragTouchOffset);

            EventItem toDrop = new EventItem(pt, mDragItem.getTitle(), 
            		mDragItem.getSnippet(), mDragItem.getType());
            toDrop.setKeywords(mDragItem.getKeywords());
            toDrop.setPubDate(mDragItem.getPubDate());
                        
            mEventOverlays.add(toDrop);
            populate();

        	setFocus(toDrop);
            mDragItem = null;
            result = false;
        } else if(action == MotionEvent.ACTION_MOVE && mDragImage != null) {
        	setFocus(null);
            setDragImagePosition(x, y);
            result = true;
        }
        
        return result || super.onTouchEvent(event, mapView);
    }

    private void setDragImagePosition(int x, int y) {
    	RelativeLayout.LayoutParams lp=
    			(RelativeLayout.LayoutParams)mDragImage.getLayoutParams();
            
      	lp.setMargins(x-xDragImageOffset-xDragTouchOffset,
                      y-yDragImageOffset-yDragTouchOffset, 0, 0);
      	mDragImage.setLayoutParams(lp);
    }
}
