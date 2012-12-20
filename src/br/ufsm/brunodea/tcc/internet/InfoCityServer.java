package br.ufsm.brunodea.tcc.internet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.context.ContextData;
import br.ufsm.brunodea.tcc.internet.facebook.InfoCityFacebook;
import br.ufsm.brunodea.tcc.model.EventComment;
import br.ufsm.brunodea.tcc.model.EventItem;
import br.ufsm.brunodea.tcc.util.InfoCityPreferences;

/**
 * Classe que serve de ponte entre o servidor e a aplicação.
 * 
 * @author bruno
 *
 */
public class InfoCityServer {
	/**
	 * Método que checa se a resposta do servidor é um JSON válido.
	 * Apresenta uma mensagem Toast de erro se não deu certo.
	 * 
	 * @param c Contexto para pegar as strings de resources.
	 * @param response String de resposta do servidor.
	 * @return Retorna um JSON com a resposta do servidor ou com uma mensagem de erro.
	 */
	private static JSONObject checkResponse(Context c, String response) {
		JSONObject ret = null;
		try {
			if(response != null && !response.equals("")) {
				if(!response.startsWith("<!DOCTYPE")) {
					ret = new JSONObject(response);
				} else {
					Log.d("InfoCityServer.checkResponse", response);
				}
			} else {
				ret = new JSONObject();
				ret.put("error", c.getResources().getString(R.string.server_error));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			ret = new JSONObject();
			try {
				ret.put("error", c.getResources().getString(R.string.server_error));
			} catch (NotFoundException e1) {
			} catch (JSONException e1) {
			}
		} catch (Exception e) {
			ret = new JSONObject();
			try {
				ret.put("error",  c.getResources().getString(R.string.conn_error));
			} catch (NotFoundException e1) {
			} catch (JSONException e1) {
			}
		}

		return ret;
	}

	/**
	 * Envia uma requisição de POST no servidor para salvar no banco de dados de lá
	 * um novo Evento.
	 * 
	 * @param c Contexto para ser passado a outros métodos.
	 * @param event Evento a ser adicionado no servidor.
	 * @return JSON com a resposta do servidor.
	 */
	public static JSONObject saveEvent(Context c, EventItem event) {
		return checkResponse(c, Internet.postRequest(InfoCityPreferences.getServerBaseURI(c)
				+"add/?", event.getListNameValuePair()));
	}

	/**
	 * Evia uma requisição do tipo GET ao servidor para retornar todos os eventos
	 * que estão em um determinado raio a partir da localização passada como parâmetro.
	 * 
	 * @param c Contexto a ser passado a outros métodos.
	 * @param location Localização de onde se busca os Eventos ao redor.
	 * @param radius Distância máxima em que o evento deve estar para ser retornado.
	 * @param context_data Dados relacionados ao contexto.
	 * @return JSON com a resposta do servidor.
	 */
	public static JSONObject getEvents(Context c, float radius, int max_events,
			ContextData context_data) {
		try {
			return checkResponse(c, Internet.getRequest(InfoCityPreferences.getServerBaseURI(c)
					+"getWithin/"+context_data.getLatitude()+"/"+context_data.getLongitude()+"/"+radius+"/"+
					max_events+"/"+URLEncoder.encode(context_data.toString(),"UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static JSONObject getEventTypes(Context c) {
		return checkResponse(c, Internet.getRequest(InfoCityPreferences.getServerBaseURI(c)+
				"eventTypes/"));
	}
	
	public static JSONObject likeEvent(Context c, EventItem event) {
		return checkResponse(c, Internet.getRequest(InfoCityPreferences.getServerBaseURI(c)
				+"likeEvent/"+event.getPrimaryKey()+"/"+InfoCityFacebook.getUser().getId()+"/"
				+event.getLikeAction()));
	}
	
	public static JSONObject getLikeAction(Context c, EventItem event) {
		return checkResponse(c, Internet.getRequest(InfoCityPreferences.getServerBaseURI(c)
				+"getLikeAction/"+event.getPrimaryKey()+"/"+InfoCityFacebook.getUser().getId()));
	}
	
	public static JSONObject countLikesDislikes(Context c, EventItem event)  {
		return checkResponse(c, Internet.getRequest(InfoCityPreferences.getServerBaseURI(c)
				+"countLikesDislikes/"+event.getPrimaryKey()));
	}
	
	public static JSONObject getEventComments(Context c, int event_id) {
		return checkResponse(c, Internet.getRequest(InfoCityPreferences.getServerBaseURI(c)
				+"getComments/"+event_id));
	}
	
	public static JSONObject saveEventComment(Context c, EventComment comment) {
		return checkResponse(c, Internet.postRequest(InfoCityPreferences.getServerBaseURI(c)+"saveComment/?",
				comment.getListNameValuePair()));
	}
}
