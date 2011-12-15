package fit.main;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
		//"hora", "estudio", "staff", "modalidade"
		ArrayList<ListMenuItem> lmi = new ArrayList<ListMenuItem>();
		for(int i = 0; i < dia.size(); i+=4){				
			lmi.add(new ListMenuItem(dia.get(i+3),
					"Início: " + dia.get(i) + "h   |   Sala: " + dia.get(i+1),
					"Staff: " + dia.get(i+2)));
		}

		// By using setAdpater method in listview we an add string array in list.
		listView.setAdapter(new list_exercs(context, android.R.layout.simple_list_item_1 , lmi));
		//

		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);

		adapter.add("ListView 1 Item 1");
		adapter.add("ListView 1 Item 2");
		adapter.add("ListView 1 Item 3");
		adapter.add("ListView 1 Item 4");
		adapter.add("ListView 1 Item 5");
		adapter.add("ListView 1 Item 6");

		listView.setAdapter(adapter);*/

		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		addView(listView, params);
	}

}
