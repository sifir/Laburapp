package ar.com.sifir.laburapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ar.com.sifir.laburapp.entities.User;
import ar.com.sifir.laburapp.helper.DBhelper;
import ar.com.sifir.laburapp.service.HttpService;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private HttpService httpService;

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
        httpService = ((MyApplication) getApplication()).httpService;

        checkbox = (CheckBox) findViewById(R.id.remember);
        progressBar = (ProgressBar) findViewById(R.id.progress_main);
        //cargo datos si hay guardados
        DBhelper helper = new DBhelper(this, "Login", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from Login", null);
        //si hay algo guardado, lo traigo
        if (c.moveToFirst()) {
            name = (EditText) findViewById(R.id.user);
            passw = (EditText) findViewById(R.id.pass);
            checkbox.setChecked(true);

            name.setText(c.getString(c.getColumnIndex("email")));
            passw.setText(c.getString(c.getColumnIndex("password")));
        }
        db.close();
    }

    //login
    public void login(View v) {
        progressBar.setVisibility(View.VISIBLE);
        //agarro los datos
        name = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);

        //autenticacion usuario
        JSONObject obj = new JSONObject();
        try {
            obj.put("identifier", name.getText().toString());
            obj.put("password", pass.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpService.authenticate(
                obj,
                this,
                this
        );
    }

    private void moveToMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    //signup
    public void signup(View v) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(JSONObject response) {
        User user = gson.fromJson(response.toString(), User.class);
        user.setPassword(pass.getText().toString());
        if (checkbox.isChecked())
            user.save(getApplicationContext());
        moveToMenu();
        progressBar.setVisibility(View.GONE);
    }
}
