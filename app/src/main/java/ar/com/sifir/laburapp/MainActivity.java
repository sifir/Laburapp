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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ar.com.sifir.laburapp.Entities.User;

public class MainActivity extends AppCompatActivity {

    CheckBox checkbox;
    EditText name;
    EditText pass;
    EditText passw;
    Number id;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //cargo datos si hay guardados
        DBhelper helper = new DBhelper(this, "Login", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from Login", null);
        String[] nombre = new String[c.getCount()];
        String[] pass = new String[c.getCount()];
        //si hay algo guardado, lo traigo
        if (c.moveToFirst() != false){
            name = (EditText) findViewById(R.id.user);
            passw = (EditText) findViewById(R.id.pass);

            name.setText(c.getString(0));
            passw.setText(c.getString(1));
        }
    }

    //login
    public void login (View v) {

        //agarro los datos
        name = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);

        //query online
        JSONObject obj = new JSONObject();
        try {
            obj.put("email", name.toString());
            obj.put("password", pass.toString());
        } catch (JSONException e){
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest (Request.Method.GET,
                "https://laburapp.herokuapp.com/users",
                //1er callback - respuesta
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        User user = gson.fromJson(response, User.class);
                        id = user.getId();
                    }
                },
                //2do callback - error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error de volley: ", error.getLocalizedMessage());
                    }
                });

        //guardo el login offline
        checkbox = (CheckBox) findViewById(R.id.remember);
        if (checkbox.isChecked()){
            DBhelper helper = new DBhelper(this, "Login", null, 1);
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("nombre", name.toString() );
            registro.put("pass", pass.toString());
            registro.put("id", id.toString());
            db.insert("Login", null, registro);
            db.close();
        }

        //abro menu
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    //signup
    public void signup (View v){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
