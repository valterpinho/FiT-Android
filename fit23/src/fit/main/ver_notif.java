package fit.main;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
		
		titulo = getIntent().getExtras().getString("titulo");
		conteudo = getIntent().getExtras().getString("texto");	
		
		//ActionBar
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("FiT :: Notificações");
        actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));
		
		
        
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
	
	//metodos actionBar
    public Intent createLogoutIntent(Context context) {
        Intent i = new Intent(context, login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}
