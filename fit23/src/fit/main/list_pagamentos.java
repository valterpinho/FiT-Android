package fit.main;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//classe auxiliar para apresentaçao de listview personalizada nos pagamentos
public class list_pagamentos extends ArrayAdapter<ListMenuItem> {
	private ArrayList<ListMenuItem> exercicios;

	public list_pagamentos(Context context, int textViewResourceId, ArrayList<ListMenuItem> exercicios) {
		super(context, textViewResourceId, exercicios);
		this.exercicios = exercicios;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_pagamentos, null);
		}

		ListMenuItem ex = exercicios.get(position);
		
		//imagem
		ImageView img = (ImageView) v.findViewById(R.id.imageView);
		if(Boolean.parseBoolean(ex.bool_pago))
			img.setImageResource(R.drawable.money);
		else
			img.setImageResource(R.drawable.nomoney);

		if (ex != null ) {
			TextView item = (TextView) v.findViewById(R.id.nome);
			TextView subitem = (TextView) v.findViewById(R.id.maquina_tipo);
			TextView subitem2 = (TextView) v.findViewById(R.id.subitem2);

			if (item != null) {
				item.setText(ex.item);
			}

			if(subitem != null) {
				subitem.setText(ex.subitem);
			}

			if(subitem2 != null) {
				subitem2.setText(ex.subitem2);
			}
		}
		return v;
	}
}