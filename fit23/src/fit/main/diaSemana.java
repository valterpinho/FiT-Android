package fit.main;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;


//dia da semana na apresentaçao do horario de aulas
public class diaSemana extends LinearLayout {


	public diaSemana(Context context, AttributeSet attrs, ArrayList<String> dia) {
		super(context, attrs);
		init(dia, context);
	}

	public diaSemana(Context context, ArrayList<String> dia) {
		super(context);
		init(dia, context);
	}

	private void init(ArrayList<String> dia, Context context) {

		ListView listView = new ListView(getContext());
		//o array passado contem em cada posicao os dois conteudos: item e subitem
		//{"hora", "duracao", "estudio", "staff", "modalidade"};
		ArrayList<ListMenuItem> lmi = new ArrayList<ListMenuItem>();
		if(dia.size() != 0)
			for(int i = 0; i < dia.size(); i+=5){				
				lmi.add(new ListMenuItem(dia.get(i+3),
						"Início: " + dia.get(i) + "h   |   Duração: " + dia.get(i+1) + "min",
						"Staff: " + dia.get(i+3) + "   |   Sala: " + dia.get(i+2)));
			}
		else //mensagem quando não há aulas no dia
			lmi.add(new ListMenuItem("Não há aulas neste dia!", null,null));
			

		// By using setAdpater method in listview we an add string array in list.
		listView.setAdapter(new list_exercs(context, android.R.layout.simple_list_item_1 , lmi));

		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		addView(listView, params);
	}

}
