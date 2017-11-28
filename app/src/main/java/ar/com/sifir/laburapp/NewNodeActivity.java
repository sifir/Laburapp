package ar.com.sifir.laburapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import static ar.com.sifir.laburapp.R.id.pass;
import static ar.com.sifir.laburapp.R.id.user;

/**
 * Created by Sifir on 27/11/2017.
 */

public class NewNodeActivity extends Activity {
    EditText nombre;
    EditText entrada;
    EditText salida;
    ProgressBar progressBar;
    User user;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_node);
        progressBar = (ProgressBar) findViewById(R.id.progress_newnode);
        user = new User();
        user.load(this);
    }

    public void aceptarBtn (View v) {
        Intent i = new Intent(this, NFCNewNodeActivity.class);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //si devolvio ok
            if(resultCode == Activity.RESULT_OK){
                progressBar.setVisibility(View.VISIBLE);
                nombre = (EditText) findViewById(R.id.nodeName);
                entrada = (EditText) findViewById(R.id.entrada);
                salida = (EditText) findViewById(R.id.salida);
                final Gson gson = new Gson();

                JSONObject obj = new JSONObject();
                try{
                    obj.put("name", nombre.getText().toString());
                    obj.put("entrada", entrada.getText().toString());
                    obj.put("salida", salida.getText().toString());
                    obj.put("tag",data.getStringExtra("result"));
                    obj.put("administrator", user.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(this);
                JsonObjectRequest request = new JsonObjectRequest (Request.Method.POST,
                        //url de login
                        "https://laburapp.herokuapp.com/nodes",
                        obj,
                        //1er callback - respuesta
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response){
                                Toast.makeText(getApplicationContext(), "Nodo creado correctamente", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        },
                        //2do callback - error
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                queue.add(request);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //no devolvio nada
            }
        }
    }

}
