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
import android.view.Window;
import android.widget.ListView;


public class pagamentos extends Activity {

	String userID;
	ArrayList<String> res = null;
	ProgressDialog d;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		Bundle bu = getIntent().getExtras();
		userID = bu.getString("user-id");
		
		d = ProgressDialog.show(this, Utils.header, Utils.text);
		new getPagamentos().execute();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pagamentos);
		
		//ActionBar
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("FiT :: Pagamentos");
        actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
        actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));
		
	}

	private class getPagamentos extends AsyncTask<String, Integer, Intent> {

		Intent i = null;

		protected Intent doInBackground(String... urls) {
			String respFields[] = {"preco", "pago", "mes", "ano"};
			String fields[] = {"token"};
			String values[] = {""+userID}; //token

			try {
				
				res = Utils.GET("pagamentos.xml" , "aula", respFields, fields, values); //rootNode é aula.. alterar

				if(res.size() == 0){

					Bundle bu = new Bundle();
					bu.putString("user-id", userID);

					i = new Intent(pagamentos.this, menu.class);
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
				
				pagamentos.this.finish();

				startActivity(i);
			}
		};

		protected void onPostExecute(Intent result) {
			if(result != null){
				d.dismiss();

				AlertDialog.Builder infoResultado = new AlertDialog.Builder(pagamentos.this);
				infoResultado.setTitle("Aviso");
				infoResultado.setMessage("Não existem dados de pagamento disponiveis!");
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
	
	public String getEstadoPagamento(String in){
		if(in.equals("true"))
			return "Pago";
		else
			return "Em dívida";
	}
	
	public String getMesPagamento(String in){
		String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", 
						  "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
		
		int indice = Integer.parseInt(in) - 1;
		
		return meses[indice];
	}


	public void getInfo() throws ParserConfigurationException, SAXException{
		try {

			final ListView lv_pagamentos=(ListView)findViewById(R.id.lv_pagamentos);

			//o array passado contem em cada posicao os dois conteudos: item e subitem
			ArrayList<ListMenuItem> lmi = new ArrayList<ListMenuItem>();
			for(int i = 0; i < res.size(); i+=4){
				ListMenuItem temp = new ListMenuItem("Mensalidade de " + getMesPagamento(res.get(i+2)) + " de " + res.get(i+3),
						"Valor: " + res.get(i) + " euros",
						"Estado: " + getEstadoPagamento(res.get(i+1))
						);
				lmi.add(temp);
			}

			// By using setAdpater method in listview we an add string array in list.			
			lv_pagamentos.setAdapter(new list_exercs(this, android.R.layout.simple_list_item_1 , lmi));

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