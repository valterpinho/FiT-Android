package fit.main;


import java.util.ArrayList;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class plano_alimentar extends Activity {

	String userID;
	ArrayList<String> res = null;
	ProgressDialog d;
	ArrayList<String> fromList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		Bundle bu = getIntent().getExtras();
		userID = bu.getString("user-id");		
		fromList = bu.getStringArrayList("plano");
		
		new getPlano().execute(b);
		d = ProgressDialog.show(this, Utils.header, Utils.text);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.plano_alimentar);
		
		//ActionBar
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("FiT :: Plano Alimentar");
        actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
        actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));
    

	}
	
	private class getPlano extends AsyncTask<Bundle, Integer, Intent> {

		Intent i = null;

		protected Intent doInBackground(Bundle... bundles) {

			getInfo();
			return null;
		}	

		private DialogInterface.OnClickListener empty_listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				plano_alimentar.this.finish();
				startActivity(i);
			}
		};

		protected void onPostExecute(Intent result) {

			if(res == null){
				d.dismiss();

				AlertDialog.Builder infoResultado = new AlertDialog.Builder(plano_alimentar.this);
				infoResultado.setTitle("Erro");
				infoResultado.setMessage("Erro de conexão!");
				infoResultado.setNegativeButton("OK", empty_listener);
				infoResultado.show();

			} else
				setText();

			d.dismiss();
		}
	}	

	@Override
	//inflating our own menu
	public boolean onCreateOptionsMenu(Menu menu) {
		//super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_palimentar, menu);
		return true;
	}

	@Override
	//implement a reaction of our menu
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.planos_anteriores) {

			Bundle b = new Bundle();
			b.putStringArrayList("planos", res);

			b.putString("user-id", userID);

			Intent i = new Intent(plano_alimentar.this, listar_planosalimentares.class);
			i.putExtras(b);

			startActivity(i);

			return true;
		}
		return false;
	}


	public void setText(){
		
		//data
		TextView tv_datacriacaot = (TextView) findViewById(R.id.tv_datacriacaot);
		tv_datacriacaot.setTextColor(Color.rgb(173,32,33));
		tv_datacriacaot.setText("Criado em: ");
		
		TextView tv_datacriacaov = (TextView) findViewById(R.id.tv_datacriacaov);
		if(fromList == null)
			tv_datacriacaov.setText(res.get(0));
		else
			tv_datacriacaov.setText(fromList.get(0));
		
		//pequenoalmoco
		TextView tv_pequenoalmocot = (TextView) findViewById(R.id.tv_pequenoalmocot);
		tv_pequenoalmocot.setTextColor(Color.rgb(173,32,33));
		tv_pequenoalmocot.setText("Pequeno-Almoço: ");
		
		TextView tv_pequenoalmocov = (TextView) findViewById(R.id.tv_pequenoalmocov);
		if(fromList == null)
			tv_pequenoalmocov.setText(res.get(1));
		else
			tv_pequenoalmocov.setText(fromList.get(1));
		//manha
		TextView tv_manhat = (TextView) findViewById(R.id.tv_manhat);
		tv_manhat.setTextColor(Color.rgb(173,32,33));
		tv_manhat.setText("Manhã: ");
		
		TextView tv_manhav = (TextView) findViewById(R.id.tv_manhav);
		if(fromList == null)
			tv_manhav.setText(res.get(2));
		else
			tv_manhav.setText(fromList.get(2));
		
		//almoco
		TextView tv_almocot = (TextView) findViewById(R.id.tv_almocot);
		tv_almocot.setTextColor(Color.rgb(173,32,33));
		tv_almocot.setText("Almoço: ");
		
		TextView tv_almocov = (TextView) findViewById(R.id.tv_almocov);
		if(fromList == null)
			tv_almocov.setText(res.get(3));
		else
			tv_almocov.setText(fromList.get(3));
		
		//tarde
		TextView tv_tardet = (TextView) findViewById(R.id.tv_tardet);
		tv_tardet.setTextColor(Color.rgb(173,32,33));
		tv_tardet.setText("Lanche: ");
		
		TextView tv_tardev = (TextView) findViewById(R.id.tv_tardev);
		if(fromList == null)
			tv_tardev.setText(res.get(4));
		else
			tv_tardev.setText(fromList.get(4));
		
		//jantar
		TextView tv_jantart = (TextView) findViewById(R.id.tv_jantart);
		tv_jantart.setTextColor(Color.rgb(173,32,33));
		tv_jantart.setText("Jantar: ");
		
		TextView tv_jantarv = (TextView) findViewById(R.id.tv_jantarv);
		if(fromList == null)
			tv_jantarv.setText(res.get(5));
		else
			tv_jantarv.setText(fromList.get(5));
		
		//ceia
		TextView tv_ceiat = (TextView) findViewById(R.id.tv_ceiat);
		tv_ceiat.setTextColor(Color.rgb(173,32,33));
		tv_ceiat.setText("Ceia: ");
		
		TextView tv_ceiav = (TextView) findViewById(R.id.tv_ceiav);
		if(fromList == null)
			tv_ceiav.setText(res.get(6));
		else
			tv_ceiav.setText(fromList.get(6));
		
		
	}

	public void getInfo() {
		try {

			String s[] = {"data", "pequenoalmoco", "manha", "almoco", "tarde", "jantar", "ceia"};
			String fields[] = {"token"};
			String values[] = {""+userID};
			res = Utils.GET("alimentar_planos.xml", "alimentar-plano", s, fields, values);			

		} catch (Exception e){
			Toast t = Toast.makeText(getApplicationContext(),
					"Erro inesperado!",
					Toast.LENGTH_SHORT);
			t.show();
		}
	}
	
	//metodos actionBar
    public Intent createLogoutIntent(Context context) {
        Intent i = new Intent(context, login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}
