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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

    }

    //validar
    private boolean isValidId(byte[] arr) {
        for (int i = 0; i < 4; i++) {
            if (arr[i] < -127 || arr[i] > 128) {
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
        String resultHexa = Utils.formatPassValue(arr);

        if (arr != null) {
            if(isValidId(arr)){
                Toast.makeText(this,"Lectura correcta de NFC",Toast.LENGTH_LONG).show();
                Intent miIntent = new Intent(this, MainActivity.class);
                miIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(miIntent);
            } else {
                Toast.makeText(this,"Error, chip NFC erroneo",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Error de lectura de tarjeta", Toast.LENGTH_LONG).show();
        }
    }


}
