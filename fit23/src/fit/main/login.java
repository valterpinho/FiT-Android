package fit.main;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class login extends Activity {

	EditText nsocio, passw;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login);
		
		//adiciona listener ao botão login
		Button login = (Button) findViewById(R.id.btn_login);
		login.setOnClickListener(btn_login_listener);
		
		//caixas de texto
		nsocio = (EditText) findViewById(R.id.txt_nsocio);
		passw = (EditText) findViewById(R.id.txt_passw);

	}

	private OnClickListener btn_login_listener = new OnClickListener() {
		public void onClick(View v) {
			//obtem e verifica validade dos dados de login
			String str_nsocio = nsocio.getText().toString();
			String str_passw = passw.getText().toString();
			
			String extension = "sessions.xml";
			String rNode = "hash";
			String[] fields = {"email", "password"};
			//String[] values = {str_nsocio, str_passw};
			String[] values = {"jpenedos@gmail.com", "123456"};
			String[] responseFields = {"token"};
			ArrayList<String> response = null;
			try {
				response = Utils.request("POST",extension, rNode, responseFields, fields, values);
				
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
	};
}