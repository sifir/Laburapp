package ar.com.sifir.laburapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ar.com.sifir.laburapp.entities.User;

public class MainActivity extends AppCompatActivity {

    CheckBox checkbox;
    EditText name;
    EditText pass;
    EditText passw;
    ProgressBar progressBar;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkbox = (CheckBox) findViewById(R.id.remember);
        progressBar = (ProgressBar) findViewById(R.id.progress_main);
        //cargo datos si hay guardados
        DBhelper helper = new DBhelper(this, "Login", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from Login", null);
        //si hay algo guardado, lo traigo
        if (c.moveToFirst() != false){
            name = (EditText) findViewById(R.id.user);
            passw = (EditText) findViewById(R.id.pass);
            checkbox.setChecked(true);

            name.setText(c.getString(c.getColumnIndex("email")));
            passw.setText(c.getString(c.getColumnIndex("password")));
        }
        db.close();
    }

    //login
    public void login (View v) {

        progressBar.setVisibility(View.VISIBLE);
        //agarro los datos
        name = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);

        //query online
        JSONObject obj = new JSONObject();
        try {
            obj.put("identifier", name.getText().toString());
            obj.put("password", pass.getText().toString());
        } catch (JSONException e){
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest (Request.Method.POST,
                //url de login
                "https://laburapp.herokuapp.com/auth/local",
                obj,
                //1er callback - respuesta
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        User user = gson.fromJson(response.toString(), User.class);
                        user.setPassword(pass.getText().toString());
                        if (checkbox.isChecked())
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
        //abro menu
    }

    private void moveToMenu () {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    //signup
    public void signup (View v){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
