package fit.main;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class menu extends Activity {

	static String userID = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu); 
		
		userID = getIntent().getExtras().getString("user-id");
		
		//ActionBar
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("FiT :: Menu Principal");
        //actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));
		
		LinearLayout plano_treino = (LinearLayout) findViewById(R.id.layout_plano_treino);
		plano_treino.setOnClickListener(lstn_ptreino);

		LinearLayout perfil = (LinearLayout) findViewById(R.id.layout_conta_pessoal);
		perfil.setOnClickListener(lstn_perfil);

		LinearLayout notificacoes = (LinearLayout) findViewById(R.id.layout_notificacoes);
		notificacoes.setOnClickListener(lstn_notific);
		
		LinearLayout gin_prox = (LinearLayout) findViewById(R.id.layout_ginasio_mais_proximo);
		gin_prox.setOnClickListener(lstn_ginprox);
		
		LinearLayout contactos = (LinearLayout) findViewById(R.id.layout_contactos);
		contactos.setOnClickListener(lstn_contactos);
		
		LinearLayout aulas = (LinearLayout) findViewById(R.id.layout_aulas);
		aulas.setOnClickListener(lstn_aulas);
		
		LinearLayout pagamentos = (LinearLayout) findViewById(R.id.layout_pagamentos);
		pagamentos.setOnClickListener(lstn_pagamentos);

		//falta adicionar listeners aos outros layouts/botoes
	}
	
	//menu about
	@Override
	//inflating our own menu
	public boolean onCreateOptionsMenu(Menu menu) {
		//super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_login, menu);
		return true;
	}

	@Override
	//implement a reaction of our menu
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.about) {
			AlertDialog.Builder infoResultado = new AlertDialog.Builder(menu.this);
			infoResultado.setTitle("About");
			infoResultado.setMessage("FiT Android App\nLDSO 2011 @ MIEIC-FEUP");
			infoResultado.setNeutralButton("Ok",null);
			infoResultado.show();

			return true;
		}
		return false;
	}

	private OnClickListener lstn_perfil = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(menu.this, perfil.class);

			Bundle b = new Bundle();
			b.putString("user-id", userID);
			i.putExtras(b);

			startActivity(i);
		}
	};

	private OnClickListener lstn_notific = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(menu.this, list_notif.class);

			Bundle b = new Bundle();
			b.putString("user-id", userID);
			i.putExtras(b);

			startActivity(i);
		}
	};

	private OnClickListener lstn_ptreino = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(menu.this, plano_treino.class);

			Bundle b = new Bundle();

			b.putString("user-id", userID);

			i.putExtras(b);

			startActivity(i);   		
		}
	};
	
	private OnClickListener lstn_ginprox = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(menu.this, map.class);
			
			Bundle b = new Bundle();

			String userID = (getIntent().getExtras()).getString("user-id");

			b.putString("user-id", userID);

			i.putExtras(b);
			
			startActivity(i);
		}
	};
	
	private OnClickListener lstn_contactos = new OnClickListener() {
		public void onClick(View v) {
			Bundle b = new Bundle();
			
			Intent i = new Intent(menu.this, list_ginasios.class);

			b.putString("user-id", userID);

			i.putExtras(b);
			
			startActivity(i);
		}
	};
	
	private OnClickListener lstn_aulas = new OnClickListener() {
		public void onClick(View v) {
			Bundle b = new Bundle();
			
			Intent i = new Intent(menu.this, horarios.class);

			b.putString("user-id", userID);

			i.putExtras(b);
			
			startActivity(i);
		}
	};
	
	private OnClickListener lstn_pagamentos = new OnClickListener() {
		public void onClick(View v) {
			Bundle b = new Bundle();
			
			Intent i = new Intent(menu.this, pagamentos.class);

			b.putString("user-id", userID);

			i.putExtras(b);
			
			startActivity(i);
		}
	};
	
	//metodos utilizados pela ActionBar
    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, menu.class);
        Bundle b = new Bundle();
        b.putString("user-id", userID);
        i.putExtras(b);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
    
    public Intent createLogoutIntent(Context context) {
        Intent i = new Intent(context, login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

}