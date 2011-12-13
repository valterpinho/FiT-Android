package fit.main;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class listar_planos extends Activity {
	/** Called when the activity is first created. */

	int userID;
	Intent i = null;
	ArrayList<String> res = null;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		Bundle bu = getIntent().getExtras();

		res = bu.getStringArrayList("planos");

		userID = bu.getInt("user-id");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listar_planos);
		
		TextView txt_top = (TextView) findViewById(R.id.txt_top);
		txt_top.setText(".:: Planos de Treino ::.");

		try {
			getInfo(res);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	//inflating our own menu
	public boolean onCreateOptionsMenu(Menu menu) {
		//super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_ptreino, menu);
		return true;
	}

	@Override
	//implement a reaction of our menu
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.planos_anteriores:

			if(res != null && res.size() != 4){
				i = new Intent(listar_planos.this, listar_planos.class);//listar_planos de treino
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
	}

	public void getInfo(ArrayList<String> res) throws ParserConfigurationException, SAXException{
		try {

			final ListView lv_planos=(ListView)findViewById(R.id.lv_planos);

			//o array passado contem em cada posicao os dois conteudos: item e subitem
			//String data;
			ArrayList<String> lmi = new ArrayList<String>();
			for(int i = 1; i < res.size(); i+=4){

				lmi.add(res.get(i));
			}

			// By using setAdpater method in listview we an add string array in list.
			lv_planos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , lmi));
			lv_planos.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

					Bundle b = findByDate(""+lv_planos.getItemAtPosition(pos));

					Intent i = new Intent(listar_planos.this, plano_treino.class);

					i.putExtras(b);

					startActivity(i);

				}

			}
					);

		} catch (Exception ex) {
			Logger.getLogger(listar_planos.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Bundle findByDate(String date){

		Bundle b = new Bundle();

		for(int i=1; i < res.size(); i+=4){
			if(res.get(i).equals(date)){

				b.putString("data", date);
				b.putString("planoID", res.get(i-1));
				b.putString("altura", res.get(i+1));
				b.putString("peso", res.get(i+2));

				b.putInt("user-id", userID);

			}	
		}

		b.putStringArrayList("planos", res);

		return b;
	}
}