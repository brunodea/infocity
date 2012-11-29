package br.ufsm.brunodea.tcc.util;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import br.ufsm.brunodea.tcc.R;
import br.ufsm.brunodea.tcc.context.ContextSupplier;

/**
 * Classe auxiliar na criação e apresentação de certas caixas de diálogo.
 * 
 * @author bruno
 *
 */
public class DialogHelper {
	/**
     * Display a dialog that user has no internet connection
     * @param ctx1
     *
     * Code from: http://osdir.com/ml/Android-Developers/2009-11/msg05044.html
     */
    public static void showNoConnectionDialog(Context ctx1) {
        final Context ctx = ctx1;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setMessage(R.string.go_to_internet_settings);
        builder.setTitle(R.string.no_internet_dialog_title);
        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ctx.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });

        builder.show();
    }
    
    /**
     * Diálogo do tipo sim/não.
     * 
     * @param c Contexto em que esse diálogo será apresentado.
     * @param title Título para o diálogo.
     * @param description Mensagem do diálogo.
     * @param handler Handler que será chamado quando o usuário clicar em sim ou não
     * 				  o valor da variável Message.what será 0 se for sim e 1 se for não.
     */
    public static void yesNoDialog(Context c, String title, String description, 
    		final Handler handler) {
    	 AlertDialog.Builder builder = new AlertDialog.Builder(c);
         builder.setCancelable(true);
         builder.setTitle(title);
         builder.setMessage(description);
         
         builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
            	 handler.sendEmptyMessage(0);
             }
         });
         builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
                 handler.sendEmptyMessage(1);
             }
         });
         
         builder.show();
    }
    
    private static void inflateAddKeywordRow(final LinearLayout rows,
    		final Context c, int id, String text) {
    	LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View v = inflater.inflate(R.layout.addkeyword_row, null);
		v.setId(id);

    	EditText et = (EditText)v.findViewById(R.id.edittext_addkeywords_dialog);
    	ImageButton ib = (ImageButton)v.findViewById(R.id.button_removekeyword_dialog);
    	
    	et.setId(id);
    	ib.setId(id);
    	
		et.setText(text);
    	
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout l = (LinearLayout) rows.findViewById(v.getId());
				EditText et = (EditText) l.getChildAt(0);
				et.setText("");
				adjustKeywordsDialog(c,rows,getKeywords(rows));
			}
		});
		
		rows.addView(v);
		v.requestFocus();
    }
    
    private static void adjustKeywordsDialog(final Context context,
    		final LinearLayout rows, final ArrayList<String> keywords) {
    	rows.removeAllViews();
    	for(int i = 0; i < keywords.size(); i++) {
    		inflateAddKeywordRow(rows, context, i, keywords.get(i));
    	}
    	if(keywords.size() == 0) {
    		inflateAddKeywordRow(rows, context, 0, "");
    	}
    }
    
    private static ArrayList<String> getKeywords(final LinearLayout rows) {
    	ArrayList<String> res = new ArrayList<String>();
    	for(int i = 0; i < rows.getChildCount(); i++) {
    		LinearLayout v = (LinearLayout) rows.getChildAt(i);
    		EditText et = (EditText)v.getChildAt(0);
    		if(et != null) {
	    		String text = et.getText().toString();
	    		if(!text.equals("")) {
	    			res.add(et.getText().toString());
	    		}
    		}
    	}
    	
    	return res;
    }
    
    /**
     * Diálogo utilizado na criação de eventos.
     * Permite ao usuário adicionar e remover palavras-chaves do evento sendo criado,
     * sendo limitado à max_keywords.
     * 
     * @param context Contexto em que será apresentado o diálogo.
     * @param keywords Palavras-chave registradas anteriormente.
     * @param max_keywords Número máximo de palavras-chave permitidas.
     * @param handler Handler que será chamado quando o usuário terminar de editar.
     * 				  Em Message.obj é passado o ArrayList<String> com as palavras-chave setadas.
     */
    public static void addKeywordDialog(final Context context, final ArrayList<String> keywords, 
    		final int max_keywords, final Handler handler) {
    	final Dialog dialog = new Dialog(context);
    	dialog.setContentView(R.layout.addkeyword_dialog);
    	dialog.setTitle(context.getResources().getString(R.string.add)+" "+
    			context.getResources().getString(R.string.keywords));
    	
    	final LinearLayout rows = (LinearLayout)dialog.findViewById(R.id.linearlayout_keywordrow_dialog);
    	adjustKeywordsDialog(context, rows, keywords);

    	final ImageButton ib = (ImageButton)dialog.findViewById(R.id.button_addkeyword_dialog);
    	ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(rows.getChildCount() < max_keywords) {
					inflateAddKeywordRow(rows, context, keywords.size(),"");
				}
			}
		});
    	
    	Button done = (Button)dialog.findViewById(R.id.button_done_addkeyord_dialog);
    	done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				keywords.clear();
				keywords.addAll(getKeywords(rows));
				
				Message msg = handler.obtainMessage();
				msg.obj = keywords;
				handler.sendMessage(msg);
			}
		});
    	
    	dialog.show();
    }
    
    public static void selectContextProviderDialog(final Context context, String action_name, 
    		ContextSupplier[] suppliers, final Handler handler) {
    	final Dialog dialog = new Dialog(context);
    	dialog.setTitle(action_name);
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(
    			Context.LAYOUT_INFLATER_SERVICE);
    	View v = inflater.inflate(R.layout.selectcontextprovider_dialog, null, false);
    	
    	final ListView listview = (ListView)v.findViewById(R.id.listview_select_contextprovider);

    	ContextSupplierAdapter adapter = new ContextSupplierAdapter(context, 
    			android.R.layout.simple_list_item_1, android.R.id.text1, suppliers);
    	
    	listview.setAdapter(adapter);
    	listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ContextSupplier supplier = (ContextSupplier)listview.getItemAtPosition(arg2);
				String name = supplier.toString();
				if(name.equals("Alohar")) {
					handler.sendEmptyMessage(0);
				} else if(name.equals("Qr-Code")) {
					handler.sendEmptyMessage(1);
				}
				dialog.dismiss();				
			}
		});

    	dialog.setContentView(v);
    	dialog.show();
    }
}
