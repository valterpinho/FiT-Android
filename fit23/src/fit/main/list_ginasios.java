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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class list_ginasios extends Activity {
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
		new getGinasios().execute();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listar_ginasios);
		
		//ActionBar
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("FiT :: Contactos");
        actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));
		
	}

	private class getGinasios extends AsyncTask<String, Integer, Intent> {

		Intent i = null;

		protected Intent doInBackground(String... urls) {
			String respFields[] = {"nome", "morada", "telefone", "latitude", "longitude"};
			String fields[] = {"token"};
			String values[] = {""+userID}; //token

			try {
				
				res = Utils.GET("ginasios.xml" , "ginasio", respFields, fields, values);

				if(res.size() == 0){

					Bundle bu = new Bundle();
					bu.putString("user-id", userID);

					i = new Intent(list_ginasios.this, menu.class);
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
				
				list_ginasios.this.finish();

				startActivity(i);
			}
		};

		protected void onPostExecute(Intent result) {
			if(result != null){
				d.dismiss();

				AlertDialog.Builder infoResultado = new AlertDialog.Builder(list_ginasios.this);
				infoResultado.setTitle("Aviso");
				infoResultado.setMessage("Não existem contactos disponiveis!");
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

			final ListView lv_gyms=(ListView)findViewById(R.id.lv_gyms);

			//o array passado contem em cada posicao os dois conteudos: item e subitem
			ArrayList<ListMenuItem> lmi = new ArrayList<ListMenuItem>();
			for(int i = 0; i < res.size(); i+=5){
				ListMenuItem temp = new ListMenuItem(res.get(i), "Telefone: " + res.get(i+1), "Morada: " + res.get(i+2));
				lmi.add(temp);
			}

			// By using setAdpater method in listview we an add string array in list.			
			lv_gyms.setAdapter(new list_exercs(this, android.R.layout.simple_list_item_1 , lmi));
			
			lv_gyms.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
					ArrayList<String> temp = new ArrayList<String>();
					
					for(int i = pos*5; i < pos*5+5; i++){
						temp.add(res.get(i));
					}
					
					Bundle b = new Bundle();
					b.putStringArrayList("fromContacts", temp);
					b.putString("user-id", userID);
					
					Intent i = new Intent(list_ginasios.this, map.class);

					i.putExtras(b);

					startActivity(i);

				}
			});

		} catch (Exception ex) {
			Logger.getLogger(listar_planos.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	//metodos actionBar
    public Intent createLogoutIntent(Context context) {
        Intent i = new Intent(context, login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}