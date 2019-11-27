package ar.com.sifir.laburapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ar.com.sifir.laburapp.service.GPStracker;

/**
 * Created by Sifir on 27/11/2017.
 */

public class LocationActivity extends AppCompatActivity {
    public static final String TAG = "LocationActivity";

    public static final int REQUEST_CODE = 8;

    public static final int RESULT_OK = -1;
    public static final int RESULT_ERROR = 1;

    private GPStracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Huella Dactilar");

        //USAR FINGERPRINT
        gps = new GPStracker(this);

        double lat = getLatitude();
        double lon = getLongitude();
        Log.d("posicion", lat + " | " + lon);
        if (lat == 0 || lon == 0) {
            setResult(RESULT_ERROR);
        } else {
            Intent i = new Intent();
            i.putExtra("lat", lat);
            i.putExtra("lon", lon);
            setResult(RESULT_OK, i);
        }
        finish();
    }


    private double getLatitude() {
        Location l = gps.getLocation();
        if (l != null) {
            return l.getLatitude();
        }
        return 0;
    }

    private double getLongitude() {
        Location l = gps.getLocation();
        if (l != null) {
            return l.getLongitude();
        }
        return 0;
    }

}
