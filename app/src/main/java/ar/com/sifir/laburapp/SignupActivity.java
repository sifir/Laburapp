package ar.com.sifir.laburapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sifir on 27/11/2017.
 */

public class SignupActivity extends Activity {

    EditText nombre;
    EditText apellido;
    EditText email;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void alta (View v){
        nombre = (EditText) findViewById(R.id.nameText);
        apellido = (EditText) findViewById(R.id.apellidoText);
        email = (EditText) findViewById(R.id.emailText);
        pass = (EditText) findViewById(R.id.passText);

        JSONObject obj = new JSONObject();
        try{
            obj.put("firstName", nombre.getText().toString());
            obj.put("lastName", apellido.getText().toString());
            obj.put("email", email.getText().toString());
            obj.put("password", pass.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
