package br.ufsm.brunodea.tcc.internet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import br.ufsm.brunodea.tcc.util.DialogHelper;

/**
 * Classe que faz operações com a internet.
 * 
 * @author bruno
 *
 */
public class Internet {
	/**
	 * Checa se a conexão com a Internet está ligada.
	 * 
	 * @param c Contexto.
	 * @return true se está conectado à internet.
	 */
	private static boolean hasConnection(Context c) {
		ConnectivityManager connMgr = (ConnectivityManager) 
	           c.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	}
	
	/**
	 * Checa se há conexão com a Internet.
	 * 
	 * @param c Contexto
	 * @param show_dialog true se é para mostrar diálogo que leva usuário às
	 * 		  configurações para habilitar o WIFI.
	 * @return true se tem conexão com a internet.
	 */
	public static boolean hasConnection(Context c, boolean show_dialog) {
		boolean has = Internet.hasConnection(c);
		if(show_dialog) {
			if(!has) {
				DialogHelper.showNoConnectionDialog(c);
			}
		}
		return has;
	}
	
	/**
	 * Faz uma requisição do tipo POST ao servidor.
	 * 
	 * @param url URL para onde se quer fazer o POST.
	 * @param nameValuePairs parâmetros passados para essa requisição de POST.
	 * @return String com a resposta do servidor. (se tudo ok, a string estará em JSON).
	 */
	public static String postRequest(String url, List<NameValuePair> nameValuePairs) {
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	    HttpParams params = httppost.getParams();

	    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	    HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
	    
	    httppost.setParams(params);
	    HttpResponse response = null;
        try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

		    httppost.setHeader("Accept", "application/json");
		    httppost.setHeader("Content-type", "application/json; charset=UTF-8");
		    response = httpclient.execute(httppost);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return responseToString(response);
	}
	
	/**
	 * Método para fazer uma requisição GET ao servidor.
	 * 
	 * @param url URL para onde se quer fazer a requisição GET.
	 * @return String com o retorno do servidor (JSON se tudo ok).
	 */
	public static String getRequest(String url) {
		HttpResponse response = null;    
        HttpClient client = new DefaultHttpClient();
        
        HttpGet request = new HttpGet();
        HttpParams params = request.getParams();
        
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
        
        request.setParams(params);
        try {
			request.setURI(new URI(url));
			response = client.execute(request);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return responseToString(response);
	}

	/**
	 * Método que pega a resposta do servidor no tipo HttpResponse e transforma
	 * em string,
	 * 
	 * @param response Resposta em HttpResponse do servidor.
	 * @return Resposta transformada em String.
	 */
	private static String responseToString(HttpResponse response) {
		if(response == null) {
			return null;
		}
		String res = null;
		try {
			res = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			res = new String(res.getBytes("UTF-8"), "UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return res;
	}
}
