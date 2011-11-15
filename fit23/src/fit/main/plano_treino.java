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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class plano_treino extends Activity {
	/** Called when the activity is first created. */
	
	int userID;
	Intent i = null;
	ArrayList<String> res = null;
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		Bundle bu = getIntent().getExtras();
		
		userID = bu.getInt("user-id");
		
		res = bu.getStringArrayList("planos");

		setContentView(R.layout.plano_treino);

		try { //altera o texto da textView do Plano de Treino
			getInfo(bu);
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
				i = new Intent(plano_treino.this, listar_planos.class);//listar_planos de treino
				
				Bundle b_plan = new Bundle();
				b_plan.putStringArrayList("planos", res);
				b_plan.putInt("user-id", userID);

				i.putExtras(b_plan);

				startActivity(i);
			}
			else{
				
				Toast t = Toast.makeText(getApplicationContext(),
						"N�o existem planos anteriores!",
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
	
	public void getInfo(Bundle b) throws ParserConfigurationException, SAXException{
        try {
        	TextView tv_data=(TextView)findViewById(R.id.tv_dataplano);
        	TextView tv_altura=(TextView)findViewById(R.id.tv_alturaplano);
        	TextView tv_peso=(TextView)findViewById(R.id.tv_pesoplano);
        	ListView lv_exerc=(ListView)findViewById(R.id.lv_exercicios);
        	
        	int id_plano=0;
        	
        	if(b.get("planoID") != null){//mostrar o plano cujo id � planoID passado no bundle

        		id_plano = Integer.parseInt(b.getString("planoID"));
        		
        		tv_data.setText("Data: " + b.getString("data"));
        		tv_altura.setText("Altura: " + b.getString("altura") + " cm");
        		tv_peso.setText("Peso: " + b.getString("peso") + " Kg");
        		        		
        	}
        	else{ //mostrar o plano mais actualizado

        		Log.e("entra",""+res.get(1));
	        		tv_data.setText("Data: " + res.get(1));
	        		tv_altura.setText("Altura: " + res.get(2) + " cm");
	        		tv_peso.setText("Peso: " + res.get(3) + " Kg");
	        		id_plano = Integer.parseInt(res.get(0)); //assume planos ordenados 
        		
        	}
        		String exercicios[] = {"nome", "maquina", "tipo"};
        		
        		ArrayList<String> exercs = Utils.GET("users/" + userID + "/planos/" + id_plano + ".xml", "exercicio", exercicios);
        		
        		//o array passado contem em cada posicao os dois conteudos: item e subitem
        		ArrayList<ListMenuItem> lmi = new ArrayList<ListMenuItem>();
        		for(int i = 0; i < exercs.size(); i+=3){
        			ListMenuItem temp = new ListMenuItem(exercs.get(i), "M�quina: " + exercs.get(i+1) + "   |   Tipo: " + exercs.get(i+2));
        			lmi.add(temp);
        		}
        	
	        	// By using setAdpater method in listview we an add string array in list.
	        	lv_exerc.setAdapter(new list_exercs(this, android.R.layout.simple_list_item_1 , lmi));
        	
        } catch (IOException ex) {
            Logger.getLogger(plano_treino.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}