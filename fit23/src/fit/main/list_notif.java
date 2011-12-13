package fit.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

//TODO Quando o titulo ocupa + do k 1 linha dá porcaria na formatacao

public class list_notif extends Activity {
	/** Called when the activity is first created. */

	String userID;
	ArrayList<String> res = null;
	ProgressDialog d;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		Bundle bu = getIntent().getExtras();
		userID = bu.getString("user-id");

		d = ProgressDialog.show(this, Utils.header, Utils.text);
		new getNotif().execute();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listar_notif);
		
		//ActionBar
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("FiT :: Notificações");
        actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));
	}

	private class getNotif extends AsyncTask<String, Integer, Intent> {

		Intent i = null;

		protected Intent doInBackground(String... urls) {
			String respFields[] = {"id", "data", "titulo", "texto"};
			String fields[] = {"token"};
			String values[] = {""+userID}; //token

			try {

				res = Utils.GET("notificacaos.xml" , "notificacao", respFields, fields, values);

				if(res.size() == 0){

					Bundle bu = new Bundle();
					bu.putString("user-id", userID);

					i = new Intent(list_notif.this, menu.class);
					i.putExtras(bu);

					return i;
				}

			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (SAXException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			return null;
		}

		private DialogInterface.OnClickListener empty_listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				list_notif.this.finish();

				startActivity(i);
			}
		};

		protected void onPostExecute(Intent result) {
			if(result != null){
				d.dismiss();

				AlertDialog.Builder infoResultado = new AlertDialog.Builder(list_notif.this);
				infoResultado.setTitle("Aviso");
				infoResultado.setMessage("Não existem noticias disponiveis!");
				infoResultado.setNegativeButton("OK", empty_listener);
				infoResultado.show();

			}
			else{
				try {
					getInfo();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			d.dismiss();
		}
	}


	public void getInfo() throws ParserConfigurationException, SAXException{
		try {

			final ListView lv_notifs=(ListView)findViewById(R.id.lv_notifs);

			//o array passado contem em cada posicao os dois conteudos: item e subitem
			//String data;
			ArrayList<ListMenuItem> lmi = new ArrayList<ListMenuItem>();
			for(int i = 1; i < res.size(); i+=4){
				ListMenuItem temp = new ListMenuItem(res.get(i+1), res.get(i), "");
				lmi.add(temp);
			}

			// By using setAdpater method in listview we an add string array in list.
			//lv_notifs.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , lmi));
			lv_notifs.setAdapter(new list_exercs(this, android.R.layout.simple_list_item_1 , lmi));

			lv_notifs.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

					Bundle b = findById(res.get(pos*4));

					Intent i = new Intent(list_notif.this, ver_notif.class);

					i.putExtras(b);

					startActivity(i);

				}
			}
					);

		} catch (Exception ex) {
			Logger.getLogger(listar_planos.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Bundle findById(String id){

		Bundle b = new Bundle();

		for(int i=0; i < res.size(); i+=4){
			if(res.get(i).equals(id)){

				b.putString("titulo", res.get(i+2));
				b.putString("texto", res.get(i+3));
				b.putString("user-id", userID);

			}	
		}

		return b;
	}
	
	//metodos actionBar
    public Intent createLogoutIntent(Context context) {
        Intent i = new Intent(context, login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}