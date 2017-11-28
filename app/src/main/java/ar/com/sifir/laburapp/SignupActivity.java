package ar.com.sifir.laburapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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

/**
 * Created by Sifir on 27/11/2017.
 */

public class SignupActivity extends Activity {

    EditText nombre;
    EditText apellido;
    EditText email;
    EditText pass;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        progressBar = (ProgressBar) findViewById(R.id.progress_signup);
    }

    public void alta (View v){
        progressBar.setVisibility(View.VISIBLE);
        nombre = (EditText) findViewById(R.id.nameText);
        apellido = (EditText) findViewById(R.id.apellidoText);
        email = (EditText) findViewById(R.id.emailText);
        pass = (EditText) findViewById(R.id.passText);
        final Gson gson = new Gson();

        JSONObject obj = new JSONObject();
        try{
            obj.put("firstName", nombre.getText().toString());
            obj.put("lastName", apellido.getText().toString());
            obj.put("email", email.getText().toString());
            obj.put("password", pass.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest (Request.Method.POST,
                //url de login
                "https://laburapp.herokuapp.com/users",
                obj,
                //1er callback - respuesta
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        User user = gson.fromJson(response.toString(), User.class);
                        user.setPassword(pass.getText().toString());
                        user.save(getApplicationContext());
                        moveToMenu();
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

    private void moveToMenu () {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
