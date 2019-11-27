package ar.com.sifir.laburapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import ar.com.sifir.laburapp.entities.User;
import ar.com.sifir.laburapp.widget.FormEditText;

/**
 * Created by Sifir on 27/11/2017.
 */

public class SignupActivity extends BaseActivity {

    FormEditText nombre;
    FormEditText apellido;
    FormEditText email;
    FormEditText pass;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Alta de Cuenta");

        nombre = (FormEditText) findViewById(R.id.nameText);
        apellido = (FormEditText) findViewById(R.id.apellidoText);
        email = (FormEditText) findViewById(R.id.emailText);
        pass = (FormEditText) findViewById(R.id.passText);

        progressBar = (ProgressBar) findViewById(R.id.progress_signup);
    }

    public void alta(View v) {
        boolean errores[] = new boolean[4];
        errores[0] = !nombre.isValid();
        errores[1] = !apellido.isValid();
        errores[2] = !email.isMailValid();
        errores[3] = !pass.isPassValid();

        if (!errores[0]&&!errores[1]&&!errores[2]&&!errores[3]) {
            progressBar.setVisibility(View.VISIBLE);
            getHttpService().createUser(
                    new User(
                            nombre.getText().toString(),
                            apellido.getText().toString(),
                            email.getText().toString(),
                            pass.getText().toString()
                    ),
                    //1er callback - respuesta
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            User user = getGson().fromJson(response.toString(), User.class);
                            user.setPassword(pass.getText().toString());
                            getDatabaseService().save(user, false);
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
        }
    }

    private void moveToMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
