package fit.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class perfil extends Activity {

	String userID;
	ArrayList<String> res = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		userID = getIntent().getExtras().getString("user-id");		

		setContentView(R.layout.perfil);
		getInfo();
	}

	@Override
	//inflating our own menu
	public boolean onCreateOptionsMenu(Menu menu) {
		//super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_perfil, menu);
		return true;
	}

	@Override
	//implement a reaction of our menu
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.editar) {

			Bundle b = new Bundle();
			b.putStringArrayList("perfil", res);

			b.putString("user-id", userID);

			Intent i = new Intent(perfil.this, editarPerfil.class);
			i.putExtras(b);

			startActivity(i);

			return true;
		}
		return false;
	}

	public void getInfo() {
		try {

			String s[] = {"datanascimento", "email", "morada", "nome", "telefone", "foto"};
			String fields[] = {"token"};
			String values[] = {""+userID};
			res = Utils.GET("users.xml", "user", s, fields, values);

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

			//coloca a foto do socio na imageview atraves do url recebido no get
			try {
				ImageView i = (ImageView)findViewById(R.id.foto);
				Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(res.get(5)).getContent());
				i.setImageBitmap(bitmap); 
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


		} catch (Exception e){
			Toast t = Toast.makeText(getApplicationContext(),
					"Erro inesperado!",
					Toast.LENGTH_SHORT);
			t.show();
		}
	}
}
