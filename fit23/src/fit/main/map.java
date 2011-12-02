package fit.main;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class map extends MapActivity {

	private MapView mapView;

	private static final int gym_lat_dragao = 41161427;
	private static final int gym_long_dragao = -8582106;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		mapView = (MapView) findViewById(R.id.map_view);       
		mapView.setBuiltInZoomControls(true);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.plano_treino);
		map_overlay itemizedOverlay = new map_overlay(drawable, this);

		GeoPoint point = new GeoPoint(gym_lat_dragao, gym_long_dragao);
		OverlayItem overlayitem = new OverlayItem(point, "Solinca Dragão", "Telef:  22 110 11 01");

		itemizedOverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedOverlay);

		MapController mapController = mapView.getController();

		mapController.animateTo(point);
		mapController.setZoom(16);

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
