package fit.main;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class editarPerfil extends Activity {

	String userID;
	ArrayList<String> res = null;
	TextView et_datanasc = null;
	TextView et_email = null;
	TextView et_morada = null;
	TextView et_nome = null;
	TextView et_telefone = null;	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		
		userID = getIntent().getExtras().getString("user-id");
		res = getIntent().getExtras().getStringArrayList("perfil");		
		
		setContentView(R.layout.editar_perfil);
		
		getInfo();
		
		Button save = (Button) findViewById(R.id.btn_save_changes);
		save.setOnClickListener(btn_save_listener);

	}
	
	private OnClickListener btn_save_listener = new OnClickListener() {
		public void onClick(View v) {
			
			String extension = "users/edit.xml";
			String rNode = "edit";
			String[] fields = {"token", "datanascimento", "email", "morada", "nome", "telefone"};
			String[] values = {userID, et_datanasc.getText().toString(), et_email.getText().toString(), et_morada.getText().toString(), et_nome.getText().toString(), et_telefone.getText().toString()};
			String[] responseFields = {"message"};
			ArrayList<String> response = null;
			try {
				response = Utils.POST(extension, rNode, responseFields, fields, values);
				
				if(response.get(0).equals("success")){
					
					Intent i = new Intent(editarPerfil.this, perfil.class);
					
					if(response != null){
						Bundle b = new Bundle();
						b.putString("user-id", userID);
						i.putExtras(b);
					}
					
					startActivity(i);					
				}
				else{

					AlertDialog.Builder infoResultado = new AlertDialog.Builder(editarPerfil.this);
					infoResultado.setTitle("Erro");
					infoResultado.setMessage(response.get(0));
					infoResultado.setNeutralButton("Ok",null);
					infoResultado.show();
					response.clear();
				}					
			} catch (Exception e){
				AlertDialog.Builder infoResultado = new AlertDialog.Builder(editarPerfil.this);
				infoResultado.setTitle("Erro");
				infoResultado.setMessage("Erro inesperado");
				infoResultado.setNeutralButton("Ok",null);
				infoResultado.show();
				response.clear();
			}
		}
	};	
	
	public void getInfo() {
		try {

			et_datanasc =(TextView)findViewById(R.id.et_data_nasc);
			et_email =(TextView)findViewById(R.id.et_email);
			et_morada =(TextView)findViewById(R.id.et_morada);
			et_nome =(TextView)findViewById(R.id.et_nome);
			et_telefone =(TextView)findViewById(R.id.et_telefone);	
			
			et_datanasc.setText(res.get(0));
			et_email.setText(res.get(1));
			et_morada.setText(res.get(2));
			et_nome.setText(res.get(3));
			et_telefone.setText(res.get(4));
			
		} catch (Exception e){
			Toast t = Toast.makeText(getApplicationContext(),
					"Erro inesperado!",
					Toast.LENGTH_SHORT);
			t.show();
		}
	}
}
