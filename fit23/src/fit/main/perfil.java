package fit.main;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class perfil extends Activity {

	int userID;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		
		userID = getIntent().getExtras().getInt("user-id");
		
		setContentView(R.layout.perfil);
		getInfo();
	}

	
		public void getInfo() {
			try {
				
				String s[] = {"datanascimento", "email", "morada", "nome", "telefone"};
				ArrayList<String> res = Utils.GET("api/users/" + userID + ".xml", "user", s);
				
				TextView tv_datanasc =(TextView)findViewById(R.id.tv_data_nasc);
	        	TextView tv_email =(TextView)findViewById(R.id.tv_email);
	        	TextView tv_morada =(TextView)findViewById(R.id.tv_morada);
	        	TextView tv_nome =(TextView)findViewById(R.id.tv_nome);
	        	TextView tv_telefone =(TextView)findViewById(R.id.tv_telefone);
	        	
	        	tv_datanasc.setText("Data de Nascimento: " + res.get(0));
	        	tv_email.setText("E-mail: " + res.get(1));
	        	tv_morada.setText("Morada: " + res.get(2));
	        	tv_nome.setText("Nome: " + res.get(3));
	        	tv_telefone.setText("Telefone: " + res.get(4));
	        	
				
				} catch (Exception e){
					Toast t = Toast.makeText(getApplicationContext(),
							"Erro inesperado!",
							Toast.LENGTH_SHORT);
					t.show();
			}
		}
}
