package fit.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

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

		LinearLayout notificacoes = (LinearLayout) findViewById(R.id.layout_notificacoes);
		notificacoes.setOnClickListener(lstn_notific);

		//falta adicionar listeners aos outros layouts/botoes
	}

	private OnClickListener lstn_perfil = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(menu.this, perfil.class);

			Bundle b = new Bundle();
			b.putString("user-id", (getIntent().getExtras()).getString("user-id"));
			i.putExtras(b);

			startActivity(i);
		}
	};

	private OnClickListener lstn_notific = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(menu.this, list_notif.class);

			Bundle b = new Bundle();
			b.putString("user-id", (getIntent().getExtras()).getString("user-id"));
			i.putExtras(b);

			startActivity(i);
		}
	};

	private OnClickListener lstn_ptreino = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(menu.this, plano_treino.class);

			Bundle b = new Bundle();

			String userID = (getIntent().getExtras()).getString("user-id");

			b.putString("user-id", userID);

			i.putExtras(b);

			startActivity(i);   		
		}
	};

}