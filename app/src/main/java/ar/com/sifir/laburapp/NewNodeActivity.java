package ar.com.sifir.laburapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Sifir on 27/11/2017.
 */

public class NewNodeActivity extends Activity {
    EditText nombre;
    EditText entrada;
    EditText salida;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_node);
    }

    public void aceptarBtn (View v) {
        nombre = (EditText) findViewById(R.id.nodeName);
        entrada = (EditText) findViewById(R.id.entrada);
        salida = (EditText) findViewById(R.id.salida);


    }
}
