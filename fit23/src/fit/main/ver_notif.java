package fit.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class ver_notif extends Activity {

	String titulo;
	String conteudo;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ver_notificacao);
		
		TextView txt_top = (TextView) findViewById(R.id.txt_top);
		txt_top.setText(".:: Notificações ::.");
		
		titulo = getIntent().getExtras().getString("titulo");
		conteudo = getIntent().getExtras().getString("texto");	
		getInfo();

	}


	public void getInfo() {
		try {

			TextView tv_titulo =(TextView)findViewById(R.id.tv_titulo);
			TextView tv_conteudo =(TextView)findViewById(R.id.tv_conteudo);

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
