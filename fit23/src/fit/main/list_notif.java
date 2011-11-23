package fit.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class list_notif extends Activity {
	/** Called when the activity is first created. */

	String userID;
	ArrayList<String> res = null;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		Bundle bu = getIntent().getExtras();

		String respFields[] = {"id", "data", "titulo", "texto"};
		String fields[] = {"token"};
		String values[] = {""+userID}; //token

		try {
			res = Utils.GET("notificacaos.xml" , "notificacao", respFields, fields, values);

			if(res.size() == 0){
				Bundle b1 = new Bundle();

				b1.putString("message", "Não existem notificações disponiveis!");

				Intent i = new Intent(list_notif.this, menu.class);

				i.putExtras(b1);

				this.finish();

				startActivity(i);
			}

		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		userID = bu.getString("user-id");

		setContentView(R.layout.listar_notif);

		try {
			getInfo();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void getInfo() throws ParserConfigurationException, SAXException{
		try {

			final ListView lv_notifs=(ListView)findViewById(R.id.lv_notifs);

			//o array passado contem em cada posicao os dois conteudos: item e subitem
			//String data;
			Log.e("RES SIZE", ""+res.size());
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
}