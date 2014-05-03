package com.example.raven.objects;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Language extends Activity implements LocationListener {
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
	

	
	// locale cell languate
//	String text = this.getResources().getConfiguration().locale.getCountry();

	
	//offline gps
//	// gps language -> not working because of 
//	LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//    final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//    Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//	String text = "";
//	Geocoder gcd = new Geocoder(this, Locale.getDefault());
//    List<Address> addresses;
//    try {
//        addresses = gcd.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
//         if (addresses.size() > 0) 
//        	text = addresses.get(0).getLocality();
//    } catch (IOException e1) {
//        // TODO Auto-generated catch block
//        e1.printStackTrace();
//    }


//    if not working use google 
//    but need api key...
    
    
    
//	 /*----Method to Check GPS is enable or disable ----- */
//	 private Boolean displayGpsStatus() {
//	  ContentResolver contentResolver = getBaseContext()
//	  .getContentResolver();
//	  boolean gpsStatus = Settings.Secure
//	  .isLocationProviderEnabled(contentResolver, 
//	  LocationManager.GPS_PROVIDER);
//	  if (gpsStatus) {
//	   return true;
//
//	  } else {
//	   return false;
//	  }
//	 }
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//        latituteField = (TextView) findViewById(R.id.TextView02);
//        longitudeField = (TextView) findViewById(R.id.TextView04);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            int lat = (int) (location.getLatitude());
            int lng = (int) (location.getLongitude());
            latituteField.setText(String.valueOf(lat));
            longitudeField.setText(String.valueOf(lng));
        } else {
            latituteField.setText("Provider not available");
            longitudeField.setText("Provider not available");
        }
    }

	
    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    
    
	@Override
	public void onLocationChanged(Location loc) {
		
	     int lat = (int) (loc.getLatitude());
	        int lng = (int) (loc.getLongitude());
	        latituteField.setText(String.valueOf(lat));
	        longitudeField.setText(String.valueOf(lng));
		
		
		
//
//	        Toast.makeText(getBaseContext(),
//	                "Location changed : Lat: " + loc.getLatitude() + " Lng: "
//	                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
//	        String longitude = "Longitude: " + loc.getLongitude();
//	        String latitude = "Latitude: " + loc.getLatitude();
//
//	        /*----------to get City-Name from coordinates ------------- */
//	        String cityName = null;
//	        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//	        List<Address> addresses;
//	        try {
//	            addresses = gcd.getFromLocation(loc.getLatitude(),
//	                    loc.getLongitude(), 1);
//	            if (addresses.size() > 0)
//	                System.out.println(addresses.get(0).getLocality());
//	            cityName = addresses.get(0).getLocality();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//
//	        String s = longitude + "\n" + latitude + "\n\nMy Currrent City is: "
//	                + cityName;
//		
	}
	@Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
	@Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
