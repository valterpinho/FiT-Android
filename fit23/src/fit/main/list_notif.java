package fit.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class list_notif extends Activity {
	/** Called when the activity is first created. */

	int userID;
	Intent i = null;
	ArrayList<String> res = null;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		Bundle bu = getIntent().getExtras();
		
		String respFields[] = {"id", "data", "titulo", "texto"};
		String fields[] = {"token"};
		String values[] = {""+userID}; //token
		
		try {
			res = Utils.GET("notificacaos.xml" , "notificacaos", respFields, fields, values);
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		//res = bu.getStringArrayList("planos");
		
		userID = bu.getInt("user-id");

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

	/*@Override
	//inflating our own menu
	public boolean onCreateOptionsMenu(Menu menu) {
		//super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_ptreino, menu);
		return true;
	}*/

	/*@Override
	//implement a reaction of our menu
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.planos_anteriores:

			if(res != null && res.size() != 4){
				i = new Intent(list_notif.this, listar_planos.class);//listar_planos de treino
				Bundle b_plan = new Bundle();
				b_plan.putStringArrayList("planos", res);
				i.putExtras(b_plan);
				startActivity(i);
			}
			else{

				Toast t = Toast.makeText(getApplicationContext(),
						"Não existem planos anteriores!",
						Toast.LENGTH_SHORT);
				t.show();
			}
			return true;
		case R.id.novo_plano:
			//TO-DO
			return true;
		}
		return false;
	}*/

	public void getInfo() throws ParserConfigurationException, SAXException{
		try {

			final ListView lv_notifs=(ListView)findViewById(R.id.lv_notifs);

			//o array passado contem em cada posicao os dois conteudos: item e subitem
			//String data;
			ArrayList<ListMenuItem> lmi = new ArrayList<ListMenuItem>();
			for(int i = 1; i < res.size(); i+=4){
				ListMenuItem temp = new ListMenuItem(res.get(i+1), res.get(i));
    			lmi.add(temp);
			}

			// By using setAdpater method in listview we an add string array in list.
			//lv_notifs.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , lmi));
        	lv_notifs.setAdapter(new list_exercs(this, android.R.layout.simple_list_item_1 , lmi));
			
			lv_notifs.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
					
					Bundle b = findById(res.get(0));
					
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
				
				b.putInt("user-id", userID);

			}	
		}
		
		return b;
	}
}