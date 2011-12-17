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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class plano_treino extends Activity{
	/** Called when the activity is first created. */

	//userID corresponde ao token
	String userID;
	ArrayList<String> res = null;
	ProgressDialog d;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bundle bu = getIntent().getExtras();

		userID = bu.getString("user-id");

		new getPlanos().execute(bu);
		d = ProgressDialog.show(this, Utils.header, Utils.text);

		setContentView(R.layout.plano_treino);

		//ActionBar
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("FiT :: Plano de Treino");
		actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
		//actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));

	}	

	private class getPlanos extends AsyncTask<Bundle, Integer, Intent> {

		Intent i = null;

		protected Intent doInBackground(Bundle... bundles) {
			String s[] = {"id", "data", "altura", "peso"};

			try {

				//bundle que vem de outra atividade
				Bundle bu = bundles[0];

				res = bu.getStringArrayList("planos");

				//caso em que não vem do listar planos
				//tem de obter o array de exercicios por pedido ao site
				if(res == null){

					String fields[] = {"token"};
					String values[] = {""+userID};
					res = Utils.GET("planos.xml", "plano", s, fields, values);	
				}


				//se não tiver exercicios envia um bundle só com o user-id
				if(res.size() == 0){
					bu.putString("user-id", userID);

					i = new Intent(plano_treino.this, menu.class);
					i.putExtras(bu);

					return i;
				}
				//caso existem exercicios guarda no bundle o array e "encapsula-o" num intent
				else{
					bu.putStringArrayList("planos", res);
					i = new Intent();
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
				// TODO Auto-generated method stub
				plano_treino.this.finish();
				startActivity(i);
			}
		};

		protected void onPostExecute(Intent result) {
			Bundle b = result.getExtras();
			res = b.getStringArrayList("planos");
			//não existem planos de treino
			if(res == null){
				d.dismiss();

				AlertDialog.Builder infoResultado = new AlertDialog.Builder(plano_treino.this);
				infoResultado.setTitle("Aviso");
				infoResultado.setMessage("Não existem planos disponiveis!");
				infoResultado.setNegativeButton("OK", empty_listener);
				infoResultado.show();

			} else //se existirem planos de treino vai obter 
				try {
					getInfo(b);
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			d.dismiss();
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

		Intent i = null;
		switch (item.getItemId()) {
		case R.id.planos_anteriores:

			if(res != null && res.size() != 4){
				i = new Intent(plano_treino.this, listar_planos.class);//listar_planos de treino

				Bundle b_plan = new Bundle();
				b_plan.putStringArrayList("planos", res);
				b_plan.putString("user-id", userID);

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
		case R.id.novo_plano: //solocitar novo plano
			//fazer POST
			String extension = "pedido_planos.xml";
			String rNode = "pedido";
			String[] fields = {"token"};
			String[] values = {userID};
			String[] responseFields = {"message"};
			ArrayList<String> response = null;
			try {
				response = Utils.POST(extension, rNode, responseFields, fields, values);

				if(response.get(0).equals("success")){
					AlertDialog.Builder infoResultado = new AlertDialog.Builder(plano_treino.this);
					infoResultado.setTitle("Informação");
					infoResultado.setMessage("Novo plano de treino requisitado com sucesso!");
					infoResultado.setNeutralButton("Ok",null);
					infoResultado.show();
					response.clear();
				}
				else{
					AlertDialog.Builder infoResultado = new AlertDialog.Builder(plano_treino.this);
					infoResultado.setTitle("Informação");
					infoResultado.setMessage("Já tem um pedido de novo plano de treino! Por favor aguarde.");
					infoResultado.setNeutralButton("Ok",null);
					infoResultado.show();
					response.clear();
				}
				return true;
			}
			catch (Exception e) {
				AlertDialog.Builder infoResultado = new AlertDialog.Builder(plano_treino.this);
				infoResultado.setTitle("Erro");
				infoResultado.setMessage("Nao foi possível concluir a operação. Tente novamente mais tarde.");
				infoResultado.setNeutralButton("Ok",null);
				infoResultado.show();
				response.clear();
			} 
		}
		return false;
	}

	public void getInfo(Bundle b) throws ParserConfigurationException, SAXException{
		try {
			TextView tv_data=(TextView)findViewById(R.id.tv_dataplano);
			TextView tv_altura=(TextView)findViewById(R.id.tv_alturaplano);
			TextView tv_peso=(TextView)findViewById(R.id.tv_pesoplano);
			TextView ex = (TextView)findViewById(R.id.exercicios);
			ListView lv_exerc=(ListView)findViewById(R.id.lv_exercicios);

			int id_plano=0;
			ex.setText("Exercícios");
			if(b.get("planoID") != null){//mostrar o plano cujo id é planoID passado no bundle


				id_plano = Integer.parseInt(b.getString("planoID"));

				tv_data.setText("Data: " + b.getString("data"));
				tv_altura.setText("Altura: " + b.getString("altura") + " cm");
				tv_peso.setText("Peso: " + b.getString("peso") + " Kg");

			}
			else{ //mostrar o plano mais actualizado

				tv_data.setText("Data: " + res.get(1));
				tv_altura.setText("Altura: " + res.get(2) + " cm");
				tv_peso.setText("Peso: " + res.get(3) + " Kg");
				id_plano = Integer.parseInt(res.get(0)); //assume planos ordenados 

			}
			String exercicios[] = {"nome", "maquina", "tipo", "peso", "series", "repeticoes"};
			String fields[] = {};
			String values[] = {};
			ArrayList<String> exercs = Utils.GET("planos/" + id_plano + ".xml", "exercicio", exercicios, fields, values);

			//o array passado contem em cada posicao os dois conteudos: item e subitem
			ArrayList<ListMenuItem> lmi = new ArrayList<ListMenuItem>();
			for(int i = 0; i < exercs.size(); i+=6){
				ListMenuItem temp = null;

				if(exercs.get(i+2).equals("Musculação"))
					temp = new ListMenuItem(exercs.get(i), "Máquina: " + exercs.get(i+1) + "   |   Tipo: " + exercs.get(i+2),
							"Peso: " + exercs.get(i+3) + "   |   Séries: "+ exercs.get(i+4) + "   |   Repetições: " + exercs.get(i+5));
				else
					temp = new ListMenuItem(exercs.get(i), "Máquina: " + exercs.get(i+1) + "   |   Tipo: " + exercs.get(i+2),
							"Velocidade/Nível: " + exercs.get(i+3) + "   |   Duração: "+ exercs.get(i+4));
				lmi.add(temp);
			}

			// By using setAdpater method in listview we an add string array in list.
			lv_exerc.setAdapter(new list_exercs(getApplicationContext(), android.R.layout.simple_list_item_1 , lmi));

		} catch (IOException ex) {
			Logger.getLogger(plano_treino.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	//metodos actionBar
	public Intent createLogoutIntent(Context context) {
		Intent i = new Intent(context, login.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}
}