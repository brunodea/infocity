package br.ufsm.brunodea.tcc.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import br.ufsm.brunodea.tcc.App;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.model.EventItem;
import br.ufsm.brunodea.tcc.model.EventItem.EventType;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class EventsItemizedOverlay extends BalloonItemizedOverlay<EventItem> {
	private ArrayList<EventItem> mEventOverlays;
	
	public enum BalloonType {
		INFO, ADD
	}

	private boolean mDraggable;
	
	private Drawable mMarker;
	private BalloonType mBalloonType;
	
	private EventItem mDragItem;
	private ImageView mDragImage;

    private int mXDragImageOffset = 0;
    private int mYDragImageOffset = 0;
    private int mXDragTouchOffset = 0;
    private int mYDragTouchOffset = 0;
	
	public EventsItemizedOverlay(Context c, Drawable marker, MapView mapView, 
			BalloonType balloon_type, EventType event_type) {
		super(boundCenterBottom(marker), mapView);
		mEventOverlays = new ArrayList<EventItem>();
		
		mDragImage = (ImageView) ((View)mapView.getParent()).findViewById(R.id.imageview_map_drag);
		Drawable dragimg_drawable = App.instance().getEventOverlayManager().
				getEventTypeMarker(event_type);
		mDragImage.setImageDrawable(dragimg_drawable);

		mDragItem = null;
		mDraggable = false;

		mXDragImageOffset = marker.getIntrinsicWidth()/2;
		mYDragImageOffset = marker.getIntrinsicHeight();

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
					getMapView().getContext(), mMarker.getIntrinsicHeight());
			break;
		default:
			res = super.createBalloonOverlayView();
		}
		
		return res;
	}

	public void setDraggable(boolean draggable) {
		mDraggable = draggable;
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
    	if(mDraggable == false) {
    		return super.onTouchEvent(event, mapView);
    	}
    	
    	final int action = event.getAction();
        final int x = (int)event.getX();
        final int y = (int)event.getY();
        
        boolean result = false;
    	
        if(action == MotionEvent.ACTION_DOWN) {
        	setFocus(null);
        	for(final EventItem item : mEventOverlays) {
                Point p = mapView.getProjection().toPixels(item.getPoint(), null);

                if(hitTest(item, mMarker, x-p.x, y-p.y)) {
                	result = true;
                	
                	mDragItem = item;
                	setFocus(item);
                	
                	mEventOverlays.remove(item);
                	populate();
                	
                	mXDragTouchOffset = 0;
                	mYDragTouchOffset = 0;
            		
                	setDragImagePosition(x, y);
                	mDragImage.setVisibility(View.VISIBLE);

                	mXDragTouchOffset = x-p.x;
                  	mYDragTouchOffset = y-p.y;

                  	break;
                }
        	}
        } else if (action==MotionEvent.ACTION_UP && mDragItem != null) {
            GeoPoint pt = mapView.getProjection().fromPixels(x-mXDragTouchOffset,
            		                                         y-mYDragTouchOffset);

        	mDragImage.setVisibility(View.GONE);
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
        
        if(mDragItem == null) {
        	return super.onTouchEvent(event, mapView);
        }
        
        return result || super.onTouchEvent(event, mapView);
    }

    private void setDragImagePosition(int x, int y) {
    	RelativeLayout.LayoutParams lp=
    			(RelativeLayout.LayoutParams)mDragImage.getLayoutParams();

      	lp.setMargins(x-mXDragImageOffset-mXDragTouchOffset,
                      y-mYDragImageOffset-mYDragTouchOffset, 0, 0);
      	mDragImage.setLayoutParams(lp);
    }
}
