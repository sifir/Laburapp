package ar.com.sifir.laburapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import ar.com.sifir.laburapp.entities.Stamp;
import ar.com.sifir.laburapp.service.StampService;

/**
 * Created by Sifir on 27/11/2017.
 */

public class MenuActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    public static final String TAG = "MenuActivity";

    private StampService stampService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        stampService = new StampService(this, this, this);
        // TODO: cargar lista de shifts
    }

    //user
    public void fichar(View v) {
        stampService.stamp();
    }

    //admin
    public void admin(View v) {
        Intent intent = new Intent(this, AdminMenuActivity.class);
        startActivity(intent);
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
            }
        }
    }

    private void fingerResult(int resultCode) {
        switch (resultCode) {
            case FingerActivity.RESULT_OK:
                stampService.stamp();
                break;
            case FingerActivity.RESULT_INVALID_FINGERPRINT:
                Toast.makeText(this, "No se puede reconocer la huella escaneada", Toast.LENGTH_SHORT).show();
                stampService.abort();
                break;
            default:
                Toast.makeText(this, "Ocurrió un error inesperado accediendo a tu huella.", Toast.LENGTH_SHORT).show();
                stampService.abort();
        }
    }

    private void nfcResult(int resultCode, Intent data) {
        if (resultCode == ReadNFCActivity.RESULT_OK) {
            stampService.stamp(data.getStringExtra("result"));
        } else {
            stampService.abort();
            Toast.makeText(this, "Error leyendo nfc", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        final Gson gson = new Gson();
        Stamp stamp = gson.fromJson(response.toString(), Stamp.class);

        if (stamp.getError() == null) {
            if (stamp.getShift().getEnded() != null) {
                //TODO: mostrar solo horas (ej: Cerraste a las: 16:00hs)
                Toast.makeText(getApplicationContext(), "Cerraste a las: " + stamp.getShift().getEnded(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Abriste a las: " + stamp.getShift().getStarted(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), stamp.getError(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("FingerActivity", String.valueOf(error.networkResponse.statusCode));
        try {
            Toast.makeText(getApplicationContext(), new String(error.networkResponse.data, "utf-8"), Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
