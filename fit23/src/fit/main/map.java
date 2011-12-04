package fit.main;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class map extends MapActivity {

	private MapView mapView;
	private GeoPoint myLocation = null;
	Location current = null;

	//private static final int gym_lat_dragao = 41161427;
	//private static final int gym_long_dragao = -8582106;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		verificaLigacoes();

	}

	void criaMapa(){
		mapView = (MapView) findViewById(R.id.map_view);       
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true); //new

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.plano_treino);
		map_overlay itemizedOverlay = new map_overlay(drawable, this);

		getLocation();

		if(myLocation != null){
			OverlayItem overlayitem = new OverlayItem(myLocation, "Solinca Dragão", "Telef:  22 110 11 01");

			itemizedOverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedOverlay);

			MapController mapController = mapView.getController();

			mapController.animateTo(myLocation);
			mapController.setZoom(16);
		}
	}

	//vai buscar a posicao atual do telemovel pela net ou por gps (dependendo do servico que estiver diponivel)
	void getLocation(){
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> prov = locationManager.getAllProviders();

		if(prov.size()>0){
			for(String s : prov)
			{
				if(locationManager.isProviderEnabled(s))
					current = locationManager.getLastKnownLocation(s);
			}
		}
		if(current!=null){
			int lat = (int) (current.getLatitude() * 1E6);
			int lng = (int) (current.getLongitude() * 1E6);
			myLocation = new GeoPoint(lat, lng);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	void verificaLigacoes(){
		ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
		boolean mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
		LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean gps = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean net = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("É necessário uma ligação à Internet. Verifique as suas definições.")
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		});

		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
		builder2.setMessage("É necessário encontrar a sua posição actual através do GPS ou redes sem fios. Verifique as suas definições.")
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		});

		if(wifi || mobile)
			if(gps || net)
				criaMapa();
			else{
				AlertDialog alert = builder2.create();
				alert.show();
			}
		else{
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

}
