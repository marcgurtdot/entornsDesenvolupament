package com.exemple.locationmanager;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {

	private final Context mContext;

	// flag per l'estat del GPS
	boolean isGPSEnabled = false;

	// flag per l'estat de la xarxa
	boolean isNetworkEnabled = false;

	boolean canGetLocation = false;

	Location location; // localització
	double latitude; // Longitud
	double longitude; // Latitut

	// La mínima distància per realitzar actualitzacions en metres
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

	// El temps mínim entre les actualitzacions en milisegons
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

	// Declarar un LocationManager
	protected LocationManager locationManager;

	/**
	 * Constructor
	 * 
	 * @param Context
	 *            d'ús
	 */
	public GPSTracker(Context context) {
		this.mContext = context;
		getLocation();
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// Comprovar l'estat del GPS
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// No hi ha proveidor de xarxa disponible
			} else {
				this.canGetLocation = true;
				// El primer és agafar la localització del proveidor de xarxa
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("NETWORK", "NET WORK'S!");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// Si el GPS està engegat agafar lat/long usant el servei GPS
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS ENGEGAT", "GPS ENGEGAT");
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if(location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}
	
	/**
	 * Funció per obtenir la latitud
	 */
	public double getLatitude(){
		if(location != null) {
			latitude = location.getLatitude();
		}
		return latitude;
	}
	
	/**
	 * Mètode per obtenir la longitud
	 */
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		} 
		
		return longitude;
	}
	
	/**
	 * Mètode per comprovar el proveidor de xarxa
	 */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}
	
	/**
	 * Mètode per mostrar un dialeg per engegar la configuració del GPS
	 */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		
		// Titol del dialog
		alertDialog.setTitle("Eina GPS");
		
		// Posar el missatge del dialog
		alertDialog.setMessage("El GPS no està activat. Vols engegar-lo?");
	
		// Posar l'icona al Dialeg
		alertDialog.setIcon(R.drawable.ic_launcher);
		
		// Al apretar el botó per activar
		alertDialog.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
		
		// Al apretar el botó per cancelar
		alertDialog.setNegativeButton("Cancel·lar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
		
		alertDialog.show();
	}
	
	/**
	 * Para l'ús de l'escolta GPS
	 * La crida d'aquest mètode para l'ús del GPS a la APP
	 */
	public void stopUsingGPS() {
		if(locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}
	
	
	/**
	 * Funcions a implementar de la interface LocationListener
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
