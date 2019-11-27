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

import org.json.JSONObject;

import ar.com.sifir.laburapp.entities.User;
import ar.com.sifir.laburapp.helper.DBhelper;
import ar.com.sifir.laburapp.service.HttpService;
import ar.com.sifir.laburapp.widget.FormEditText;

public class MainActivity extends BaseActivity implements Response.Listener<User>, Response.ErrorListener {

    CheckBox checkbox;
    FormEditText name;
    FormEditText pass;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Laburapp");

        checkbox = (CheckBox) findViewById(R.id.remember);
        progressBar = (ProgressBar) findViewById(R.id.progress_main);

        if (mUser != null) {
            name = (FormEditText) findViewById(R.id.user);
            pass = (FormEditText) findViewById(R.id.pass);
            name.setText(mUser.getEmail());
            if (mUser.getPassword() != null) {
                checkbox.setChecked(true);
                pass.setText(mUser.getPassword());
                login(null);
            }
        }
    }

    //login
    public void login(View v) {
        boolean errores[] = new boolean[2];
        errores[0] = !name.isMailValid();
        errores[1] = !pass.isPassValid();

        if (!errores[0]&&!errores[1]) {
            progressBar.setVisibility(View.VISIBLE);
            name = (FormEditText) findViewById(R.id.user);
            pass = (FormEditText) findViewById(R.id.pass);

            getHttpService().authenticate(
                    name.getText().toString(),
                    pass.getText().toString(),
                    this,
                    this
            );
        }
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
    public void onResponse(User user) {
        getDatabaseService().deleteAll();
        user.setPassword(pass.getText().toString());
        getDatabaseService().save(user, checkbox.isChecked());
        moveToMenu();
        progressBar.setVisibility(View.GONE);
    }
}
