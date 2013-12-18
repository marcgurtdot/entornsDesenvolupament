package com.exemple.locationmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class AndroidGPSTrackingActivity extends Activity {

	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	public void onEstic(View view) {
		gps = new GPSTracker(AndroidGPSTrackingActivity.this);

		// Comprovar que el GPS estigui activat
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			// Mostrar la localització actual
			Toast.makeText(
					getApplicationContext(),
					"La teva posició és \n Longitud: " + longitude
							+ ", \nLatitud: " + latitude, Toast.LENGTH_LONG)
					.show();
		} else{
			// Si no es pot aconseguir la localització
			// GPS o Xarxa no disponibles
			// Preguntar per encendre GPS/Xarxa a eines del sistema
			gps.showSettingsAlert();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
