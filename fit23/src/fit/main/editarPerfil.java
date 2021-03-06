package fit.main;

import java.util.ArrayList;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.editar_perfil);
		
		//ActionBar
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("FiT :: Editar Pefil");
        actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));
		

		getInfo();

		Button save = (Button) findViewById(R.id.btn_save_changes);
		save.setOnClickListener(btn_save_listener);

	}

	private OnClickListener btn_save_listener = new OnClickListener() {
		public void onClick(View v) {

			String datanasc = et_datanasc.getText().toString(), email = et_email.getText().toString();
			String em[] = email.split("@");
			String dn[] = datanasc.split("-");
			
			//email tem de ter @ e . na string a seguir ao @
			//primeira parte tem de ter no minimo 3 caracteres
			if(em[0].length() < 3 || em.length != 2){
				Toast t = Toast.makeText(getApplicationContext(),
						"Email inv�lido!",
						Toast.LENGTH_SHORT);
				t.show();
			}
			//data de nascimento tem de ter 3 partes (dia,ano,mes)
			//e cada uma das partes tem de ter no minimo 2 numeros e no maximo 4
			else if(dn.length != 3 
					|| (dn[0].length() < 2 || dn[0].length() > 4)
					|| (dn[1].length() < 2 || dn[1].length() > 4)
					|| (dn[2].length() < 2 || dn[2].length() > 4)){
				Toast t = Toast.makeText(getApplicationContext(),
						"Data de nascimento inv�lida!",
						Toast.LENGTH_SHORT);
				t.show();
			}else{

				String extension = "users/edit.xml";
				String rNode = "edit";
				String[] fields = {"token", "datanascimento", "email", "morada", "nome", "telefone"};
				String[] values = {userID, datanasc, email, et_morada.getText().toString(), et_nome.getText().toString(), et_telefone.getText().toString()};
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

						//startActivity(i);	
						editarPerfil.this.finish();
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
	
	//metodos actionBar
    public Intent createLogoutIntent(Context context) {
        Intent i = new Intent(context, login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}
