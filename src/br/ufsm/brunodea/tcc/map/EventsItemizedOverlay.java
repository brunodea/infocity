package br.ufsm.brunodea.tcc.map;

import java.util.ArrayList;
import java.util.Iterator;

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

/**
 * Classe que gerencia/determina o comportamento dos markers dos eventos no mapa.
 * Por exemplo, se mDraggable for true, os markers deste EventsItemizedOverlay
 * serão arrastáveis.
 * 
 * @author bruno
 *
 */
public class EventsItemizedOverlay extends BalloonItemizedOverlay<EventItem> {
	private ArrayList<EventItem> mEventOverlays;
	
	/**
	 * Enum  que determina os tipos de balões.
	 */
	public enum BalloonType {
		INFO, ADD
	}
	
	private InfoCityMap mInfoCityMap;

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
			BalloonType balloon_type, EventType event_type, InfoCityMap infocitymap) {
		super(boundCenterBottom(marker), mapView);
		mInfoCityMap = infocitymap;
		
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
		
		populate();
	}
	
	/**
	 * Adiciona um Evento à este EventsItemizedOverlay.
	 * 
	 * @param eventitem evento a ser adicionado.
	 */
	public void addEventItem(EventItem eventitem) {
		mEventOverlays.add(eventitem);
		populate();
	}

	/**
	 * Remove um Evento deste EventsItemizedOverlay a partir de sua
	 * chave primária.
	 * 
	 * @param pk
	 */
	public void removeEventItemByPk(int pk) {
		boolean removed = false;
		Iterator<EventItem> it = mEventOverlays.iterator();
		while(it.hasNext()) {
			EventItem e = it.next();
			if(e.getPrimaryKey() == pk) {
				it.remove();
				removed = true;
				break;
			}
		}
		if(removed) {
			populate();
		}
	}
	
	/**
	 * Remove todos os Eventos deste EventsItemizedOverlay cuja chave primária
	 * seja < 0.
	 */
	public void removeInvalidEventItemsPk() {
		boolean removed = false;
		Iterator<EventItem> it = mEventOverlays.iterator();
		while(it.hasNext()) {
			EventItem e = it.next();
			if(e.getPrimaryKey() == -1) {
				it.remove();
				removed = true;
			}
		}
		if(removed) {
			populate();
		}
	}
	
	public void removeEventItemsNotIn(ArrayList<Integer> pk_list) {
		boolean removed = false;
		Iterator<EventItem> it = mEventOverlays.iterator();
		while(it.hasNext()) {
			EventItem e = it.next();
			if(!pk_list.contains(e.getPrimaryKey())) {
				it.remove();
				removed = true;
			}
		}
		if(removed) {
			populate();
		}
	}
	
	/**
	 * Verifica se este EventsItemizedOverlay contém um evento a partir de sua chave
	 * primária.
	 * 
	 * @param eventitem Evento que contém ou não neste EventsItemizedOverlay.
	 * @return true se este EventsItemizedOverlay contém o evento eventitem.
	 */
	public boolean containsEventItem(EventItem eventitem) {
		boolean contains = false;
		for(EventItem e : mEventOverlays) {
			if(e.getPrimaryKey() == eventitem.getPrimaryKey()) {
				contains = true;
				break;
			}
		}
		
		return contains;
	}
	
	/**
	 * Remove todos os eventos deste EventsItemizedOverlay.
	 */
	public void clearOverlays() {
		mEventOverlays.clear();
		populate();
	}
	
	/**
	 * Pega a lista com todas as chaves primárias dos eventos deste 
	 * EventsItemizedOverlay exceto as que são < 0.
	 * 
	 * @return Lista com todas primary keys maiores que -1.
	 */
	public ArrayList<Integer> getAllPks() {
		ArrayList<Integer> pks = new ArrayList<Integer>();
		for(EventItem e : mEventOverlays) {
			int pk = e.getPrimaryKey();
			if(pk >= 0) {
				pks.add(pk);
			}
		}
		return pks;
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
	
	/**
	 * Cria um balão para os Eventos deste EventsItemizedOverlay de acordo com
	 * o BalloonType setado.
	 */
	@Override
	protected BalloonOverlayView<EventItem> createBalloonOverlayView() {
		BalloonOverlayView<EventItem> res = null;
		switch(mBalloonType) {
		case INFO:
			res = new InfoEventBalloonOverlayView<EventItem>(
					getMapView().getContext(),  mMarker.getIntrinsicHeight());
			break;
		case ADD:
			res = new AddEventBalloonOverlayView<EventItem>(
					getMapView().getContext(), mMarker.getIntrinsicHeight(), mInfoCityMap);
			break;
		default:
			res = super.createBalloonOverlayView();
		}
		
		return res;
	}

	/**
	 * Determina se os markers deste EventsItemizedOverlay são arrastáveis ou não.
	 * @param draggable true para ser possível arrastar os markers deste EventsItemizedOverlay.
	 */
	public void setDraggable(boolean draggable) {
		mDraggable = draggable;
	}

	/**
	 * Faz as ações necessárias para a arrastabilidade dos markers funcionar
	 * corretamente.
	 */
    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
    	if(mDraggable == false) {
    		return super.onTouchEvent(event, mapView);
    	}
    	
    	final int action = event.getAction();
        final int x = (int)event.getX();
        final int y = (int)event.getY();
        
        boolean result = false;
    	
        //Se o usuário só clicou no marker, abre o balão.
        if(action == MotionEvent.ACTION_DOWN) {
        	for(final EventItem item : mEventOverlays) {
                Point p = mapView.getProjection().toPixels(item.getPoint(), null);

                if(hitTest(item, mMarker, x-p.x, y-p.y)) {
                	result = true;
                	
                	mDragItem = item;
                	setFocus(item); //ação para abrir o balão.
                	
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
        	setFocus(null); //ao arrastar o marker, seu balão é fechado.
        	
        	setDragImagePosition(x, y);
            result = true;
        }
        
        if(mDragItem == null) {
        	return super.onTouchEvent(event, mapView);
        }
        
        return result || super.onTouchEvent(event, mapView);
    }

    /**
     * Método que determina a posição da imagem de arrasto na tela.
     * 
     * @param x
     * @param y
     */
    private void setDragImagePosition(int x, int y) {
    	RelativeLayout.LayoutParams lp=
    			(RelativeLayout.LayoutParams)mDragImage.getLayoutParams();

      	lp.setMargins(x-mXDragImageOffset-mXDragTouchOffset,
                      y-mYDragImageOffset-mYDragTouchOffset, 0, 0);
      	mDragImage.setLayoutParams(lp);
    }
}
