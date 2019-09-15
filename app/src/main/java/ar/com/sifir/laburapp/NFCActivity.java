package ar.com.sifir.laburapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ar.com.sifir.laburapp.entities.User;
import be.appfoundry.nfclibrary.activities.NfcActivity;

/**
 * Created by Sifir on 14/09/2017.
 */

public class NFCActivity extends NfcActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

    }

    //validar
    private boolean isValidId(byte[] arr) {
        for (int i = 0; i < 4; i++) {
            if (arr[i] < -127 || arr[i] > 128) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle b = intent.getExtras();
        byte[] arr = b.getByteArray("android.nfc.extra.ID");

        if (arr != null) {
            if(isValidId(arr)){
                stamp(Utils.formatPassValue(arr));
            } else {
                Toast.makeText(this,"Error, chip NFC erroneo",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Error de lectura de tarjeta", Toast.LENGTH_LONG).show();
        }
    }

    private void stamp (String key) {
        final Gson gson = new Gson();
        //cargo el usuario
        User user = new User();
        user.load(getApplicationContext());
        JSONObject obj = new JSONObject();
        try{
            obj.put("user", user.getId());
            obj.put("tag", key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest (Request.Method.POST,
                //url de login
                "https://laburapp.herokuapp.com/stamps",
                obj,
                //1er callback - respuesta
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Toast.makeText(getApplicationContext(),"Lectura correcta de NFC",Toast.LENGTH_LONG).show();
                        Intent miIntent = new Intent(getApplicationContext(), MenuActivity.class);
                        miIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(miIntent);
                    }
                },
                //2do callback - error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error.networkResponse.statusCode
//                        /agarrar mensaje del error y mostrarlo
                    }
                });
        queue.add(request);
    }
}
