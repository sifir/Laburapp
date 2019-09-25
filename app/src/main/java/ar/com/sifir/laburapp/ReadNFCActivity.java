package ar.com.sifir.laburapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import be.appfoundry.nfclibrary.activities.NfcActivity;

/**
 * Created by Sifir on 14/09/2017.
 */

public class ReadNFCActivity extends NfcActivity {

    public static final String TAG = "ReadNFCActivity";

    public static final int REQUEST_CODE = 9;

    public static final int RESULT_OK = -1;
    public static final int RESULT_READ_ERROR = 1;
    public static final int RESULT_WRONG_CHIP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

    }


    private boolean isValidId(byte[] arr) {
        for (int i = 0; i < 4; i++) {
            if (arr[i] < -127) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle b = intent.getExtras();
        byte[] arr = null;

        if (b != null) {
            arr = b.getByteArray("android.nfc.extra.ID");
        }

        if (arr != null) {
            if (isValidId(arr)) {
                Log.d(TAG, "Lectura correcta de NFC");
                Intent i = new Intent();
                i.putExtra("result", Utils.formatPassValue(arr));
                setResult(RESULT_OK, i);
            } else {
                setResult(RESULT_WRONG_CHIP);
                Log.e(TAG, "Error, chip NFC erroneo");
            }
        } else {
            setResult(RESULT_READ_ERROR);
            Log.e(TAG, "Error de lectura de tarjeta");
        }
        finish();
    }


}
