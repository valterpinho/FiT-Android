package fit.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class perfil extends Activity {

	String userID;
	ArrayList<String> res = null;
	ProgressDialog d;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		userID = getIntent().getExtras().getString("user-id");		

		d = ProgressDialog.show(this, Utils.header, Utils.text);
		new getPerfil().execute();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.perfil);

	}

	private class getPerfil extends AsyncTask<Bundle, Integer, Intent> {

		Intent i = null;

		protected Intent doInBackground(Bundle... bundles) {

			getInfo();
			return null;
		}	

		private DialogInterface.OnClickListener empty_listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				perfil.this.finish();
				startActivity(i);
			}
		};

		protected void onPostExecute(Intent result) {

			if(res == null){
				d.dismiss();

				AlertDialog.Builder infoResultado = new AlertDialog.Builder(perfil.this);
				infoResultado.setTitle("Erro");
				infoResultado.setMessage("Erro de conexão!");
				infoResultado.setNegativeButton("OK", empty_listener);
				infoResultado.show();

			} else
				setText();

			d.dismiss();
		}
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

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		int width = bm.getWidth();

		int height = bm.getHeight();

		float scaleWidth = ((float) newWidth) / width;

		float scaleHeight = ((float) newHeight) / height;

		// create a matrix for the manipulation

		Matrix matrix = new Matrix();

		// resize the bit map

		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

		return resizedBitmap;

	}

	public void setText(){
		
		TextView txt_top = (TextView) findViewById(R.id.txt_top);
		txt_top.setText(".:: Pefil ::.");
		
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
			i.setImageBitmap(getResizedBitmap(bitmap, 100, 100)); 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getInfo() {
		try {

			String s[] = {"datanascimento", "email", "morada", "nome", "telefone", "foto"};
			String fields[] = {"token"};
			String values[] = {""+userID};
			res = Utils.GET("users.xml", "user", s, fields, values);			

		} catch (Exception e){
			Toast t = Toast.makeText(getApplicationContext(),
					"Erro inesperado!",
					Toast.LENGTH_SHORT);
			t.show();
		}
	}
}
