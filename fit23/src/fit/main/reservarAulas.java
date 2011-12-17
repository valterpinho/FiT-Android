package fit.main;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class reservarAulas extends Activity {

	String userID;
	ArrayList<String> res = null, aula = null;
	ProgressDialog d;
	Button marcar = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		Bundle reserva = getIntent().getExtras();
		userID = reserva.getString("user-id");
		//"id", "hora", "duracao", "estudio", "staff", "modalidade"
		aula = reserva.getStringArrayList("aula");

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reservaraulas);

		//ActionBar
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("FiT :: Reservar Aula");
		actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
		actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));

	}

	@Override
	public void onResume() {
		super.onResume();
		d = ProgressDialog.show(this, Utils.header, Utils.text);
		new getReservas().execute();
	}

	private class getReservas extends AsyncTask<Bundle, Integer, Intent> {


		protected Intent doInBackground(Bundle... bundles) {

			getInfo();
			return null;
		}

		protected void onPostExecute(Intent i) {

			setInfo();

			d.dismiss();
		}

		private void setInfo() {

			//nome
			TextView nomeaula_v = (TextView) findViewById(R.id.tv_nomeaula_v);
			nomeaula_v.setText(aula.get(5));
			nomeaula_v.setTextSize(20);

			//hora
			TextView hora = (TextView) findViewById(R.id.tv_hora);
			hora.setTextColor(Color.rgb(173,32,33));
			hora.setText("Início: ");

			TextView hora_v = (TextView) findViewById(R.id.tv_hora_v);
			hora_v.setText(aula.get(1) + "h");

			//staff
			TextView staff = (TextView) findViewById(R.id.tv_staff);
			staff.setTextColor(Color.rgb(173,32,33));
			staff.setText("Staff: ");

			TextView staff_v = (TextView) findViewById(R.id.tv_staff_v);
			staff_v.setText(aula.get(4));

			//duracao
			TextView duracao = (TextView) findViewById(R.id.tv_duracao);
			duracao.setTextColor(Color.rgb(173,32,33));
			duracao.setText("Duração: ");

			TextView duracao_v = (TextView) findViewById(R.id.tv_duracao_v);
			duracao_v.setText(aula.get(2) + "min");

			//lotacao
			TextView lotacao = (TextView) findViewById(R.id.tv_lotacao);
			lotacao.setTextColor(Color.rgb(173,32,33));
			lotacao.setText("Lotação: ");

			TextView lotacao_v = (TextView) findViewById(R.id.tv_lotacao_v);
			lotacao_v.setText(res.get(1) + " lugares");

			//disponiveis
			TextView disponiveis = (TextView) findViewById(R.id.tv_diponiveis);
			disponiveis.setTextColor(Color.rgb(173,32,33));
			disponiveis.setText("Disponíveis: ");

			TextView disponiveis_v = (TextView) findViewById(R.id.tv_disponiveis_v);
			disponiveis_v.setText(res.get(0) + " lugares");

			//botao marcar/desmarcar
			marcar = (Button) findViewById(R.id.btn_marcar);

			if(res.get(0).equals("0"))
				marcar.setVisibility(View.GONE);
			else{
				if(res.get(2).equals("sim"))
					marcar.setText("Desmarcar");
				else
					marcar.setText("Marcar");
	
				Log.e("MSG", "entrou");
				
				marcar.setOnClickListener(btn_marcar_listner);
			}
		}
	}	

	private OnClickListener btn_marcar_listner = new OnClickListener() {
		public void onClick(View v) {
			String rNode = "reserva";
			String[] fields = {"token","aula_id"};
			Log.e("IDAULA", ""+aula.get(0));
			Log.e("TOKEN", userID);
			String[] values = {userID, aula.get(0)};
			String[] responseFields = {"message"};
			ArrayList<String> response = null;

			try {
				if(marcar.getText().toString().equals("Marcar")){
					response = Utils.POST("reserva_aulas.xml", rNode, responseFields, fields, values);
					if(response.get(0).equals("success")){

						Toast t = Toast.makeText(getApplicationContext(),
								"Reservar efectuada com sucesso!",
								Toast.LENGTH_LONG);
						t.show();
						marcar.setText("Desmarcar");

					}
					else{
						Toast t = Toast.makeText(getApplicationContext(),
								"Pedimos desculpa, mas não foi possivel efectuar a sua reserva.",
								Toast.LENGTH_LONG);
						t.show();
					}
				}
				else{
					response = Utils.POST("desmarcar.xml", rNode, responseFields, fields, values);
					if(response.get(0).equals("success")){

						Toast t = Toast.makeText(getApplicationContext(),
								"Desmarcação efectuada!",
								Toast.LENGTH_LONG);
						t.show();
						marcar.setText("Marcar");

					}
					else{
						Toast t = Toast.makeText(getApplicationContext(),
								"Pedimos desculpa, mas não foi possivel efectuar a desmarcação.",
								Toast.LENGTH_LONG);
						t.show();
					}
				}
			} catch (Exception e){

			}
		}
	};

	public void getInfo() {
		try {

			String s[] = {"lugares", "lotacao", "tem_reserva"};
			String fields[] = {"token", "aula_id"};
			String values[] = {userID, aula.get(0)};
			res = Utils.GET("getinfo.xml", "reserva", s, fields, values);			

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
