package fit.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class map extends MapActivity {

	private MapView mapView;
	private GeoPoint myLocation = null;
	private GeoPoint destiny = null;
	Location current = null;
	String userID = null;	
	private ArrayList<String> near_gym = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map);
		
		//ActionBar
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("FiT :: Localização");
        actionBar.setHomeAction(new IntentAction(this, menu.createIntent(this), R.drawable.ic_title_home_default));
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.addAction(new IntentAction(this, createLogoutIntent(this), R.drawable.ic_title_share_default));
		

		Bundle bu = getIntent().getExtras();
		userID = bu.getString("user-id");
		near_gym = bu.getStringArrayList("fromContacts");
		
		verificaLigacoes();

	}
	
	//menu para ver percurso
	@Override
	//inflating our own menu
	public boolean onCreateOptionsMenu(Menu menu) {
		//super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_map, menu);
		return true;
	}

	@Override
	//implement a reaction of our menu
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.ver_percurso) {

			//abrir google maps com o percurso
			double lat_or = myLocation.getLatitudeE6()/1E6;
			double lon_or = myLocation.getLongitudeE6()/1E6;
			String origem = lat_or + "," + lon_or;
			double lat_ds = destiny.getLatitudeE6()/1E6;
			double lon_ds = destiny.getLongitudeE6()/1E6;
			String destino = lat_ds + "," + lon_ds;
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + origem + "&daddr=" + destino));
			startActivity(intent);

			startActivity(intent);

			return true;
		}
		return false;
	}
	//fim menu ver percurso

	void getAllGyms() throws ParserConfigurationException, SAXException, IOException{
		String respFields[] = {"nome", "morada", "telefone", "latitude", "longitude"};
		String fields[] = {"token"};
		String values[] = {""+userID}; //token

		ArrayList<String> res = Utils.GET("ginasios.xml" , "ginasio", respFields, fields, values);
		
		getNearestGym(res);
	}

	void getNearestGym(ArrayList<String> gyms){
		ArrayList<String> nearest = new ArrayList<String>();
		float[] results = new float[1];
		float nearest_actual = 0;
		int nearest_index = 0;

		for(int i = 0; i < gyms.size(); i+=5)
			if (myLocation != null){				
				Location.distanceBetween(myLocation.getLatitudeE6()/1E6, myLocation.getLongitudeE6()/1E6,
						Double.parseDouble(gyms.get(i+3)), Double.parseDouble(gyms.get(i+4)), results);
				if(i == 0){
					nearest_actual = results[0];
					nearest_index = i;
				}
				else
					if(results[0] < nearest_actual){
						nearest_actual = results[0];
						nearest_index = i;
					}
						
				//Log.e("Distancia: ", ""+ results[0]);
			}
		
		for(int i=nearest_index; i < nearest_index+5; i++){
			nearest.add(gyms.get(i));
		}
		
		Toast t = Toast.makeText(getApplicationContext(),
				"Ginásio mais próximo: " + nearest.get(0),
				Toast.LENGTH_LONG);
		t.show();
		
		near_gym = nearest;
	}

	void criaMapa(){
		mapView = (MapView) findViewById(R.id.map_view);       
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.plano_treino);
		map_overlay itemizedOverlay = new map_overlay(drawable, this);

		getLocation();

		if(myLocation != null){

			try {
				if(near_gym == null){
					getAllGyms();
				}
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//myPosition
			OverlayItem overlayitem = new OverlayItem(myLocation, "Posição Atual", "Latitude: " + myLocation.getLatitudeE6() + "  " + "Longitude: " + myLocation.getLongitudeE6());
			itemizedOverlay.addOverlay(overlayitem);
			
			//nearestGym
			int lat = (int) (Double.parseDouble(near_gym.get(3)) * 1E6);
			int lng = (int) (Double.parseDouble(near_gym.get(4)) * 1E6);
			GeoPoint gp = new GeoPoint(lat, lng);
			destiny = gp;
			OverlayItem overlayitem2 = new OverlayItem(gp, "Ginásio " + near_gym.get(0), near_gym.get(1) + " Tel: " + near_gym.get(2));
			itemizedOverlay.addOverlay(overlayitem2);
			
			
			mapOverlays.add(itemizedOverlay);

			MapController mapController = mapView.getController();
			
			GeoPoint gp_center = new GeoPoint((lat+myLocation.getLatitudeE6())/2, (lng+myLocation.getLongitudeE6())/2);
			mapController.animateTo(gp_center); //calcular ponto medio?
			mapController.zoomToSpan(Math.abs(lat-myLocation.getLatitudeE6()), Math.abs(lng-myLocation.getLongitudeE6()));
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
	
	//metodos actionBar
    public Intent createLogoutIntent(Context context) {
        Intent i = new Intent(context, login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

}
