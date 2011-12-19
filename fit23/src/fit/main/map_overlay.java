package fit.main;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.sax.StartElementListener;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class map_overlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();

	private Context context;
	String url;

	public map_overlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public map_overlay(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		this.context = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {

		OverlayItem item = mapOverlays.get(index);
		url = item.getSnippet().toString();
		String[] dados = url.split(" ");
		String dados_last = dados[dados.length-1];
		final String telef = "tel: "+dados_last;
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		if(!dados[0].equals("Latitude:")){
			dialog.setPositiveButton("Chamar", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int id) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(telef));				
					context.startActivity(intent);

				}});
		}
		dialog.setNeutralButton("Ok",null);
		dialog.show();
		return true;
	}

	public void addOverlay(OverlayItem overlay) {
		mapOverlays.add(overlay);
		this.populate();
	}

}
