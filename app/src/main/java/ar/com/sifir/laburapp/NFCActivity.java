package ar.com.sifir.laburapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import be.appfoundry.nfclibrary.activities.NfcActivity;

/**
 * Created by Sifir on 14/09/2017.
 */

public class NFCActivity extends NfcActivity {

    Boolean isIngressValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        //INTENTS
        Intent intent = getIntent();
        Bundle miBundle = intent.getExtras();
        isIngressValid = miBundle.getBoolean("isIngressValid");
    }

    private boolean isValidId(byte[] arr) {
        byte[] pass = {111, 41, 99, 76};
        for (int i = 0; i < 4; i++) {
            if (arr[i] != pass[i]) {
                Log.d("Error de clave ", "en posicion " + i + " No es igual");
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
                if (isIngressValid == true) {
                    Toast.makeText(this,"Buen trabajo, hasta la proxima!",Toast.LENGTH_LONG).show();
                    Intent miIntent = new Intent(this, MainActivity.class);
                    miIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(miIntent);
                } else {
                    Toast.makeText(this,"Lectura correcta, a laburar!",Toast.LENGTH_LONG).show();
                    Intent miIntent = new Intent(this, MainActivity.class);
                    miIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(miIntent);
                }
            } else {
                Toast.makeText(this,"Error, chip NFC erroneo",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Error de lectura de tarjeta", Toast.LENGTH_LONG).show();
        }
    }
}
