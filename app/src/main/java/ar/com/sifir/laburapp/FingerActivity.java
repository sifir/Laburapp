package ar.com.sifir.laburapp;

import android.app.Activity;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.widget.Toast;

import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

/**
 * Created by Sifir on 27/11/2017.
 */

public class FingerActivity extends Activity implements FingerPrintAuthCallback {

    FingerPrintAuthHelper mFingerPrintAuthHelper;
    Boolean isIngressValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);

        //INTENTS
        Intent intent = getIntent();
        Bundle miBundle = intent.getExtras();
        isIngressValid = miBundle.getBoolean("isIngressValid");

        //USAR FINGERPRINT
        mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFingerPrintAuthHelper.startAuth();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFingerPrintAuthHelper.stopAuth();
    }

    @Override
    public void onNoFingerPrintHardwareFound() {
        Toast.makeText(this, "No se detecto ningun sensor de huella digital", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoFingerPrintRegistered() {

    }

    @Override
    public void onBelowMarshmallow() {
        Toast.makeText(this, "Necesitas una version de Android superior a Marshmallow", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
        if (isIngressValid == true) {
            Toast.makeText(this, "Exito!", Toast.LENGTH_SHORT).show();
            Intent miIntent = new Intent(this, NFCActivity.class);
            miIntent.putExtra("isIngressValid", true);
            startActivity(miIntent);
        } else {
            Toast.makeText(this, "Exito!", Toast.LENGTH_SHORT).show();
            Intent miIntent = new Intent(this, NFCActivity.class);
            miIntent.putExtra("isIngressValid", false);
            startActivity(miIntent);
        }

    }

    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {
        switch (errorCode) {    //Parse the error code for recoverable/non recoverable error.
            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
                //Cannot recognize the fingerprint scanned.
                Toast.makeText(this, "No se puede reconocer la huella escaneada", Toast.LENGTH_SHORT).show();
                break;
            case AuthErrorCodes.NON_RECOVERABLE_ERROR:
                //This is not recoverable error. Try other options for user authentication. like pin, password.
                break;
            case AuthErrorCodes.RECOVERABLE_ERROR:
                //Any recoverable error. Display message to the user.
                break;
        }
    }
}
