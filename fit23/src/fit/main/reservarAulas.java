package fit.main;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

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
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class reservarAulas extends Activity {

	String userID;
	ArrayList<String> res = null, aula = null;
	ProgressDialog d;
	Button marcar = null;
	TextView disponiveis_v;
	int diaSemana;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		Bundle reserva = getIntent().getExtras();
		userID = reserva.getString("user-id");
		//"id", "hora", "duracao", "estudio", "staff", "modalidade"
		aula = reserva.getStringArrayList("aula");
		diaSemana = reserva.getInt("diaSemana");

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reservaraulas);

		marcar = (Button) findViewById(R.id.btn_marcar);
		marcar.setVisibility(View.GONE);

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

			disponiveis_v = (TextView) findViewById(R.id.tv_disponiveis_v);
			disponiveis_v.setText(res.get(0) + " lugares");

			//RatingBar
			RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingBar);
			final TextView tv_rating = (TextView) findViewById(R.id.tv_rating);
			
			ratingbar.setRating(Float.parseFloat(res.get(5)));
			
			tv_rating.setText("Dê o seu Feedback sobre esta aula!");
			
			//desactiva classificação caso o user ja tenha submetido
			if(Float.parseFloat(res.get(5)) > 0){
				ratingbar.setEnabled(false);
				ratingbar.setClickable(false);
				tv_rating.setText("Feedback Submetido: " + res.get(5) + "/5");
			}
			
			
			
			ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

				public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
					String rNode = "feedback";
					String[] fields = {"token","aula_id", "valor"};
					String[] values = {userID, aula.get(0), Float.toString(rating)};
					String[] responseFields = {"message"};
					ArrayList<String> response = null;

					try {
						response = Utils.POST("feedbacks.xml", rNode, responseFields, fields, values);

						if(response.get(0).equals("success")){
							
							tv_rating.setText("Feedback Submetido: " + (int)rating + "/5");

							Toast t = Toast.makeText(getApplicationContext(),
									"Feedback submetido com sucesso!",
									Toast.LENGTH_LONG);
							t.show();
						}
						else{

							Toast t = Toast.makeText(getApplicationContext(),
									"Já submeteu feedback para esta aula!",
									Toast.LENGTH_LONG);
							t.show();

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}

				}
			});

			//end RatingBar

			int dia_servidor = Integer.parseInt(res.get(3));  //Dia
			String[] hora_servidor = res.get(4).split(":"); //Hora HH:MM
			String tempHora = hora_v.getText().toString();
			tempHora = tempHora.substring(0, tempHora.length()-1);
			String hora_aula[] = tempHora.split(":");

			if(res.get(0).equals("0"))
				marcar.setVisibility(View.GONE);
			else if(diaSemana != dia_servidor)
				marcar.setVisibility(View.GONE);
			else if(diaSemana == dia_servidor){
				if(Integer.parseInt(hora_servidor[0]) == Integer.parseInt(hora_aula[0])){
					if(Integer.parseInt(hora_servidor[1]) >= Integer.parseInt(hora_aula[1]))
						marcar.setVisibility(View.GONE);
					else{
						marcar.setVisibility(1);
						if(res.get(2).equals("sim"))
							marcar.setText("Desmarcar");
						else
							marcar.setText("Marcar");

						Log.e("MSG", "entrou");

						marcar.setOnClickListener(btn_marcar_listner);
					}
				}
				else if(Integer.parseInt(hora_servidor[0]) > Integer.parseInt(hora_aula[0]))
					marcar.setVisibility(View.GONE);
				else{
					marcar.setVisibility(1);
					if(res.get(2).equals("sim"))
						marcar.setText("Desmarcar");
					else
						marcar.setText("Marcar");

					Log.e("MSG", "entrou");

					marcar.setOnClickListener(btn_marcar_listner);
				}

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

						String[] nlugares = disponiveis_v.getText().toString().split(" ");
						disponiveis_v.setText(Integer.parseInt(nlugares[0]) - 1 + " lugares");

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

						String[] nlugares = disponiveis_v.getText().toString().split(" ");
						disponiveis_v.setText(Integer.parseInt(nlugares[0]) + 1 + " lugares");

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

			String s[] = {"lugares", "lotacao", "tem_reserva", "dia", "hora", "feedback"};
			String fields[] = {"token", "aula_id"};
			String values[] = {userID, aula.get(0)};
			Log.e("ERRO", "aqui_1");
			res = Utils.GET("getinfo.xml", "reserva", s, fields, values);
			Log.e("ERRO", "aqui_2");
			Log.e("ERRO", ""+res);

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
