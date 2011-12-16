package fit.main;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;


public class horarios extends Activity {
	
	ActionBar actionBar;
	ProgressDialog d;
	ArrayList<String> res = null;
	String userID = null;
	ArrayList<String> segunda = new ArrayList<String>();
	ArrayList<String> terca = new ArrayList<String>();
	ArrayList<String> quarta = new ArrayList<String>();
	ArrayList<String> quinta = new ArrayList<String>();
	ArrayList<String> sexta = new ArrayList<String>();
	ArrayList<String> sabado = new ArrayList<String>();
	ArrayList<String> domingo = new ArrayList<String>();	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bu = getIntent().getExtras();

		userID = bu.getString("user-id");

		setContentView(R.layout.horarios);

		//ActionBar
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("FiT :: Aulas :: Segunda");
		actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
		//actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));

		new getHorario().execute(bu);
		d = ProgressDialog.show(this, Utils.header, Utils.text);
	}

	private class getHorario extends AsyncTask<Bundle, Integer, Intent> {

		Intent i = null;


		protected Intent doInBackground(Bundle... bundles) {
			String s[] = {"dia", "hora", "duracao", "estudio", "staff", "modalidade"};

			try {

				Bundle bu = new Bundle();

				//obter todas as aulas
				if(res == null){

					String fields[] = {"token"};
					String values[] = {""+userID};
					res = Utils.GET("aulas.xml", "aula", s, fields, values);	
				}


				//se não existirem aulas envia um bundle só com o user-id
				if(res.size() == 0){
					bu.putString("user-id", userID);

					i = new Intent(horarios.this, menu.class);
					i.putExtras(bu);

					return i;
				}
				//caso existam aulas guarda no bundle o array e "encapsula-o" num intent
				else{
					bu.putStringArrayList("aulas", res);
					i = new Intent();
					i.putExtras(bu);
					return i;
				}

			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (SAXException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			return null;
		}	

		private DialogInterface.OnClickListener empty_listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				horarios.this.finish();
				startActivity(i);
			}
		};

		protected void onPostExecute(Intent result) {
			Bundle b = result.getExtras();
			res = b.getStringArrayList("aulas");
			//não existem aulas
			if(res == null){
				d.dismiss();

				AlertDialog.Builder infoResultado = new AlertDialog.Builder(horarios.this);
				infoResultado.setTitle("Aviso");
				infoResultado.setMessage("Não existem aulas disponiveis!");
				infoResultado.setNegativeButton("OK", empty_listener);
				infoResultado.show();

			} else //se existirem aulas vai obter 
				try {
					getInfo(b);
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			d.dismiss();
		}
	}
	
	public void getInfo(Bundle b) throws ParserConfigurationException, SAXException{				

		for(int i=0; i < res.size(); i+=6){
			int dia = Integer.parseInt(res.get(i));
			switch (dia){
			case 0:
				for(int j = i+1; j <= i + 5; j++)
					segunda.add(res.get(j));
				break;
			case 1:
				for(int j = i+1; j <= i + 5; j++)
					terca.add(res.get(j));
				break;
			case 2:
				for(int j = i+1; j <= i + 5; j++)
					quarta.add(res.get(j));
				break;
			case 3:
				for(int j = i+1; j <= i + 5; j++)
					quinta.add(res.get(j));
				break;
			case 4:
				for(int j = i+1; j <= i + 5; j++)
					sexta.add(res.get(j));
				break;
			case 5:
				for(int j = i+1; j <= i + 5; j++)
					sabado.add(res.get(j));
				break;
			case 6:
				for(int j = i+1; j <= i + 5; j++)
					domingo.add(res.get(j));
				break;
			default:
				break;
			}		
		}
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		MyPagerAdapter adapter = new MyPagerAdapter(this);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int page) {
				//tvHeader.setText("Page " + (page + 1));
				switch (page){
				case 0:
					actionBar.setTitle("FiT :: Aulas :: Segunda");
					break;
				case 1:
					actionBar.setTitle("FiT :: Aulas :: Terça");
					break;
				case 2:
					actionBar.setTitle("FiT :: Aulas :: Quarta");
					break;
				case 3:
					actionBar.setTitle("FiT :: Aulas :: Quinta");
					break;
				case 4:
					actionBar.setTitle("FiT :: Aulas :: Sexta");
					break;
				case 5:
					actionBar.setTitle("FiT :: Aulas :: Sábado");
					break;
				case 6:
					actionBar.setTitle("FiT :: Aulas :: Domingo");
					break;
				default:
					break;
				}				
			}
			
			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				// TODO Auto-generated method stub
					
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}	

	private class MyPagerAdapter extends PagerAdapter {

		private ArrayList<LinearLayout> views;

		public MyPagerAdapter(Context context) {
			views = new ArrayList<LinearLayout>();
			
			views.add(new diaSemana(context, segunda));
			views.add(new diaSemana(context, terca));
			views.add(new diaSemana(context, quarta));
			views.add(new diaSemana(context, quinta));
			views.add(new diaSemana(context, sexta));
			views.add(new diaSemana(context, sabado));
			views.add(new diaSemana(context, domingo));
		}

		@Override
		public void destroyItem(View view, int arg1, Object object) {
			((ViewPager) view).removeView((LinearLayout) object);
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View view, int position) {
			View myView = views.get(position);
			((ViewPager) view).addView(myView);
			return myView;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

	}

	//metodos actionBar
	public Intent createLogoutIntent(Context context) {
		Intent i = new Intent(context, login.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}
}