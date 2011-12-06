package fit.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.widget.ListView;


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

		setContentView(R.layout.listar_ginasios);
	}

	private class getGinasios extends AsyncTask<String, Integer, Intent> {

		Intent i = null;

		protected Intent doInBackground(String... urls) {
			String respFields[] = {"nome", "morada", "telefone"};
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
			for(int i = 0; i < res.size(); i+=3){
				ListMenuItem temp = new ListMenuItem(res.get(i), "Telefone: " + res.get(i+1), "Morada: " + res.get(i+2));
				lmi.add(temp);
			}

			// By using setAdpater method in listview we an add string array in list.			
			lv_gyms.setAdapter(new list_exercs(this, android.R.layout.simple_list_item_1 , lmi));

		} catch (Exception ex) {
			Logger.getLogger(listar_planos.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}