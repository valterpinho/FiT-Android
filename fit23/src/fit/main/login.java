package fit.main;


import java.util.ArrayList;

import com.markupartist.android.widget.ActionBar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class login extends Activity {

	EditText nsocio, passw;
	CheckBox cb_remember; //checkbox remember me

	public static final String PREFS_NAME = "MyPrefsFile";
	private static final String PREF_USERNAME = "username";
	private static final String PREF_PASSWORD = "password";
	SharedPreferences pref;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 

		//ActionBar
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("FiT :: Bem-vindo");
        //actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));

        
		//adiciona listener ao botão login
		Button login = (Button) findViewById(R.id.btn_login);
		login.setOnClickListener(btn_login_listener);
		
		Button clear = (Button) findViewById(R.id.btn_clear);
		clear.setOnClickListener(btn_clear_listener);
		
		
		//caixas de texto
		nsocio = (EditText) findViewById(R.id.txt_nsocio);
		passw = (EditText) findViewById(R.id.txt_passw);

		//se existirem, apresenta dados guardados anteriormente
		pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
		nsocio.setText(pref.getString(PREF_USERNAME, null));
		passw.setText(pref.getString(PREF_PASSWORD, null));

		//checkbox remember
		cb_remember = (CheckBox) findViewById(R.id.cb_remember);
		//cb_remember.setChecked(false);

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
			AlertDialog.Builder infoResultado = new AlertDialog.Builder(login.this);
			infoResultado.setTitle("About");
			infoResultado.setMessage("FiT Android App\nLDSO 2011 @ MIEIC-FEUP");
			infoResultado.setNeutralButton("Ok",null);
			infoResultado.show();

			return true;
		}
		return false;
	}

	//funcao que verifica se existe ligacao a internet diponivel
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	private OnClickListener btn_login_listener = new OnClickListener() {
		public void onClick(View v) {
			//obtem e verifica validade dos dados de login
			String str_nsocio = nsocio.getText().toString();
			String str_passw = passw.getText().toString();

			//verifica se a checkbox remember esta activa e guarda os dados de login
			if(cb_remember.isChecked()){
				getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
				.edit()
				.putString(PREF_USERNAME, str_nsocio) //guarda dados
				.putString(PREF_PASSWORD, str_passw)
				.commit();
			}

			//apaga dados se unchecked com dados do socio anteriormente guardados
			if(!cb_remember.isChecked() && str_nsocio.equals(pref.getString(PREF_USERNAME, null))){
				getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
				.edit()
				.putString(PREF_USERNAME, "") //apaga dados
				.putString(PREF_PASSWORD, "")
				.commit();
			}

			String rNode = "hash";
			String[] fields = {"email", "password"};
			String[] values = {str_nsocio, str_passw};
			String[] responseFields = {"token"};
			ArrayList<String> response = null;

			//verifica se conexao Internet disponivel
			if(isNetworkAvailable()){

				try {
					response = Utils.POST("sessions.xml", rNode, responseFields, fields, values);

					if(response.get(0).equals("149")){
						AlertDialog.Builder infoResultado = new AlertDialog.Builder(login.this);
						infoResultado.setTitle("Erro");
						infoResultado.setMessage("Dados de login inválidos.");
						infoResultado.setNeutralButton("Ok",null);
						infoResultado.show();
						response.clear();

					}
					else if(Integer.parseInt(response.get(0)) > 0){

						AlertDialog.Builder infoResultado = new AlertDialog.Builder(login.this);
						infoResultado.setTitle("Erro");
						infoResultado.setMessage("A aplicação falhou!");
						infoResultado.setNeutralButton("Ok",null);
						infoResultado.show();
						response.clear();
					}					
				} catch (NumberFormatException e){

					Intent i = new Intent(login.this, menu.class);

					if(response != null){
						Bundle b = new Bundle();
						b.putString("user-id", response.get(0));
						i.putExtras(b);
					}

					startActivity(i);

				} catch (Exception e){

				}
			}
			else{
				//mostra informacao de ligacao internet nao disponivel
				AlertDialog.Builder infoResultado = new AlertDialog.Builder(login.this);
				infoResultado.setTitle("Erro");
				infoResultado.setMessage("Ligação à Internet não disponível!");
				infoResultado.setNeutralButton("Ok",null);
				infoResultado.show();
			}
		}
	};

	private OnClickListener btn_clear_listener = new OnClickListener() {
		public void onClick(View v) {
			nsocio.setText("");
			passw.setText("");
		}
	};
	
	//metodos actionBar
    public Intent createLogoutIntent(Context context) {
        Intent i = new Intent(context, login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

}