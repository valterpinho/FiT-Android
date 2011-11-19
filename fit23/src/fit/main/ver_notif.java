package fit.main;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ver_notif extends Activity {
	
	String titulo;
	String conteudo;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		setContentView(R.layout.ver_notificacao);
		titulo = getIntent().getExtras().getString("titulo");
		conteudo = getIntent().getExtras().getString("texto");
		Log.e("erro1", getIntent().getExtras().getString("titulo"));		
		getInfo();

	}

	
		public void getInfo() {
			try {
				
				TextView tv_titulo =(TextView)findViewById(R.id.tv_titulo);
	        	TextView tv_conteudo =(TextView)findViewById(R.id.tv_conteudo);
	        	
	        	//Log.e("erro", getIntent().getExtras().getString("titulo"));

	       		tv_titulo.setText(titulo);
	    		tv_conteudo.setText(conteudo);

	        	
				
				} catch (Exception e){
					Toast t = Toast.makeText(getApplicationContext(),
							"Erro inesperado!",
							Toast.LENGTH_SHORT);
					t.show();
					Log.e("erro2", getIntent().getExtras().getString("titulo"));
			}
		}
}
