package fit.main;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;


//dia da semana na apresentaçao do horario de aulas
public class diaSemana extends LinearLayout {

	String token="";
	ArrayList<String> ids = new ArrayList<String>();
	ListView lv_aulas = null;

	public diaSemana(Context context, AttributeSet attrs, ArrayList<String> dia, String token) {
		super(context, attrs);
		init(dia, context);
		this.token = token;
	}

	public diaSemana(Context context, ArrayList<String> dia) {
		super(context);
		init(dia, context);
	}
	
	public ListView getListView(){
			return lv_aulas;
	}
	
	public ArrayList<String> getIds(){
		return ids;
	}

	private void init(ArrayList<String> dia, Context context) {

		lv_aulas = new ListView(getContext());
		//o array passado contem em cada posicao os dois conteudos: item e subitem
		//{"id", "hora", "duracao", "estudio", "staff", "modalidade"};
		ArrayList<ListMenuItem> lmi = new ArrayList<ListMenuItem>();
		if(dia.size() != 0){
			for(int i = 0; i < dia.size(); i+=6){

				ids.add(dia.get(i));

				lmi.add(new ListMenuItem(dia.get(i+5),
						"Início: " + dia.get(i+1) + "h   |   Duração: " + dia.get(i+2) + "min",
						"Staff: " + dia.get(i+4) + "   |   Sala: " + dia.get(i+3)));
			}
		}
		else //mensagem quando não há aulas no dia
			lmi.add(new ListMenuItem("Não há aulas neste dia!", null,null));


		// By using setAdpater method in listview we an add string array in list.
		lv_aulas.setAdapter(new list_exercs(context, android.R.layout.simple_list_item_1 , lmi));

		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		addView(lv_aulas, params);
	}
}
