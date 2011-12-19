package fit.main;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class listar_planosalimentares extends Activity {
	/** Called when the activity is first created. */

	String userID;
	Intent i = null;
	ArrayList<String> res = null;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		Bundle bu = getIntent().getExtras();

		res = bu.getStringArrayList("planos");

		userID = bu.getString("user-id");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listar_planos);
		
		//ActionBar
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("FiT :: Planos Alimentares");
        actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));
		

		try {
			getInfo(res);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public void getInfo(ArrayList<String> res) throws ParserConfigurationException, SAXException{
		try {

			final ListView lv_planos=(ListView)findViewById(R.id.lv_planos);

			//o array passado contem em cada posicao os dois conteudos: item e subitem
			//String data;
			ArrayList<String> lmi = new ArrayList<String>();
			for(int i = 0; i < res.size(); i+=7){
				lmi.add(res.get(i));
			}

			// By using setAdpater method in listview we an add string array in list.
			lv_planos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , lmi));
			lv_planos.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

					Bundle b = find(pos);

					Intent i = new Intent(listar_planosalimentares.this, plano_alimentar.class);

					i.putExtras(b);

					startActivity(i);

				}

			}
					);

		} catch (Exception ex) {
			Logger.getLogger(listar_planosalimentares.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Bundle find(int pos){

		Bundle b = new Bundle();
		ArrayList<String> dados = new ArrayList<String>();
		for(int i=pos*7; i < pos*7+7; i++){
			dados.add(res.get(i));				
		}
		b.putString("user-id", userID);
		b.putStringArrayList("plano", dados);

		return b;
	}
	
	//metodos actionBar
    public Intent createLogoutIntent(Context context) {
        Intent i = new Intent(context, login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}