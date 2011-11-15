package fit.main;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class menu extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		
		setContentView(R.layout.menu);    

		LinearLayout plano_treino = (LinearLayout) findViewById(R.id.layout_plano_treino);
		plano_treino.setOnClickListener(lstn_ptreino);
		
		LinearLayout perfil = (LinearLayout) findViewById(R.id.layout_conta_pessoal);
		perfil.setOnClickListener(lstn_perfil);

		//falta adicionar listeners aos outros layouts/botoes
	}

	private OnClickListener lstn_perfil = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(menu.this, perfil.class);
			
			Bundle b = new Bundle();
			b.putInt("user-id", (getIntent().getExtras()).getInt("user-id"));
			i.putExtras(b);

			
			startActivity(i);
		}
	};
	
	private OnClickListener lstn_ptreino = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(menu.this, plano_treino.class);
			
			Bundle b = new Bundle();
			
			int userID = (getIntent().getExtras()).getInt("user-id");

    		String s[] = {"id", "data", "altura", "peso"};
    		try {
    			ArrayList<String> res = Utils.GET("api/users/" + userID + "/planos.xml", "plano", s);

    			if(res.size() > 0){
    				
    				b.putInt("user-id", userID);
    				b.putStringArrayList("planos", res);
    				i.putExtras(b);
    				
    				startActivity(i);
    			} else{
    				Log.e("cenas", "null");
    				Toast t = Toast.makeText(getApplicationContext(),
    						"Não existem planos disponíveis!",
    						Toast.LENGTH_SHORT);
    				t.show();
    			}
    		}catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

}