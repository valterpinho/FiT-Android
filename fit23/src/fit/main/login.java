package fit.main;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

		setContentView(R.layout.login);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 

		//adiciona listener ao bot�o login
		Button login = (Button) findViewById(R.id.btn_login);
		login.setOnClickListener(btn_login_listener);

		//caixas de texto
		nsocio = (EditText) findViewById(R.id.txt_nsocio);
		passw = (EditText) findViewById(R.id.txt_passw);

		//se existirem, apresenta dados guardados anteriormente
		pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
		nsocio.setText(pref.getString(PREF_USERNAME, null));
		passw.setText(pref.getString(PREF_PASSWORD, null));

		//checkbox remember
		cb_remember = (CheckBox) findViewById(R.id.cb_remember);

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

			String extension = "sessions.xml";
			String rNode = "hash";
			String[] fields = {"email", "password"};
			String[] values = {str_nsocio, str_passw};
			//String[] values = {"jpenedos@gmail.com", "123456"};
			String[] responseFields = {"token"};
			ArrayList<String> response = null;

			//verifica se conexao Internet disponivel
			if(isNetworkAvailable()){

				try {
					response = Utils.POST(extension, rNode, responseFields, fields, values);

					if(response.get(0).equals("149")){
						AlertDialog.Builder infoResultado = new AlertDialog.Builder(login.this);
						infoResultado.setTitle("Erro");
						infoResultado.setMessage("Dados de login inv�lidos.");
						infoResultado.setNeutralButton("Ok",null);
						infoResultado.show();
						response.clear();

					}
					else if(Integer.parseInt(response.get(0)) > 0){

						AlertDialog.Builder infoResultado = new AlertDialog.Builder(login.this);
						infoResultado.setTitle("Erro");
						infoResultado.setMessage("A aplica��o falhou!");
						infoResultado.setNeutralButton("Okay",null);
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
				infoResultado.setMessage("Liga��o � Internet n�o dispon�vel!");
				infoResultado.setNeutralButton("Ok",null);
				infoResultado.show();
			}
		}
	};
}