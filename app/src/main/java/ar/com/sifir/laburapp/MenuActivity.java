package ar.com.sifir.laburapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ar.com.sifir.laburapp.entities.Node;
import ar.com.sifir.laburapp.entities.Shift;
import ar.com.sifir.laburapp.entities.Stamp;
import ar.com.sifir.laburapp.service.StampService;

/**
 * Created by Sifir on 27/11/2017.
 */

public class MenuActivity extends BaseActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    public static final String TAG = "MenuActivity";

    private StampService stampService;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setTitle("Laburapp");
        ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        stampService = new StampService(this, this, this);
    }

    //user
    public void stamp(View v) {
        stampService.stamp();
    }

    //admin
    public void admin(View v) {
        Intent intent = new Intent(this, AdminMenuActivity.class);
        startActivity(intent);
    }

    public void inviteMenu(View v) {
        Intent intent = new Intent(this, InvitesMenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void refresh() {
        getHttpService().lastShiftbyId(mUser.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Shift[] shifts = getGson().fromJson(response, Shift[].class);
                        if (shifts.length > 0) {
                            TextView lastFichajeTxt = (TextView) findViewById(R.id.ultimoFichajeTxt);
                            TextView fichajeTxt = (TextView) findViewById(R.id.fichajeTxt);
                            if (shifts[0].getEnded() != null) {
                                fichajeTxt.setText(getApplicationContext().getString(R.string.closeShift));
                                lastFichajeTxt.setText(sdf.format(shifts[0].getEnded()));
                            } else {
                                lastFichajeTxt.setText(sdf.format(shifts[0].getStarted()));
                                fichajeTxt.setText(getApplicationContext().getString(R.string.openShift));
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error:", error.toString());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            stampService.abort();
        } else {
            if (requestCode == FingerActivity.REQUEST_CODE) {
                fingerResult(resultCode);
            } else if (requestCode == ReadNFCActivity.REQUEST_CODE) {
                nfcResult(resultCode, data);
            } else if (requestCode == LocationActivity.REQUEST_CODE) {
                locationResult(resultCode, data);
            }
        }
    }

    private void fingerResult(int resultCode) {
        switch (resultCode) {
            case FingerActivity.RESULT_OK:
                stampService.stamp();
                break;
            case FingerActivity.RESULT_INVALID_FINGERPRINT:
                Toast.makeText(this, this.getString(R.string.wrongFinger), Toast.LENGTH_SHORT).show();
                stampService.abort();
                break;
            default:
                Toast.makeText(this, this.getString(R.string.fingerError), Toast.LENGTH_SHORT).show();
                stampService.abort();
        }
    }

    private void nfcResult(int resultCode, Intent data) {
        final Context self = this;
        if (resultCode == ReadNFCActivity.RESULT_OK) {
            final String tag = data.getStringExtra("result");
            getHttpService().userNodesByTag(tag, mUser,
                    new Response.Listener<Node>() {
                        @Override
                        public void onResponse(Node node) {
                            stampService.stamp(node);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            stampService.abort();
                            Toast.makeText(self, "No estas reigstrado en este nodo", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            stampService.abort();
            Toast.makeText(this, this.getString(R.string.NFCfingerError), Toast.LENGTH_SHORT).show();
        }
    }

    private void locationResult(int resultCode, Intent data) {
        if (resultCode == LocationActivity.RESULT_OK)
            // distance
            stampService.stamp();
        else {
            stampService.abort();
            Toast.makeText(this, this.getString(R.string.locationError), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        Stamp stamp = getGson().fromJson(response.toString(), Stamp.class);
        if (stamp.getError() == null) {
            if (stamp.getShift().getEnded() != null) {
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.closeShift) + sdf.format(stamp.getShift().getEnded()), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.openShift) + sdf.format(stamp.getShift().getStarted()), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), stamp.getError(), Toast.LENGTH_LONG).show();
        }
        refresh();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("FingerActivity", String.valueOf(error.networkResponse.statusCode));
        try {
            Toast.makeText(getApplicationContext(), new String(error.networkResponse.data, "utf-8"), Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        refresh();
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLong = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLong / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c; // en millas
    }

    private boolean lessThanBlock(double dist) {
        return dist <= 0.062;
    }

}
