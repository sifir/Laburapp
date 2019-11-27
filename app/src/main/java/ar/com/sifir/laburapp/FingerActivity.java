package ar.com.sifir.laburapp;

import android.app.Activity;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

/**
 * Created by Sifir on 27/11/2017.
 */

public class FingerActivity extends AppCompatActivity implements FingerPrintAuthCallback {
    public static final String TAG = "FingerActivity";

    public static final int REQUEST_CODE = 6;

    public static final int RESULT_OK = -1;
    public static final int RESULT_ERROR = 1;
    public static final int RESULT_INVALID_FINGERPRINT = 2;

    FingerPrintAuthHelper mFingerPrintAuthHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        setTitle("Huella Dactilar");

        //USAR FINGERPRINT
        mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);

        findViewById(R.id.btn_fich).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAuthSuccess(null);
            }
        });
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
        Toast.makeText(this, this.getString(R.string.noSensorFinger), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoFingerPrintRegistered() {

    }

    @Override
    public void onBelowMarshmallow() {
        Toast.makeText(this, this.getString(R.string.marshVersion), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
        Log.d(TAG, "Lectura de huella: Exito!");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {
        //Parse the error code for recoverable/non recoverable error.
        if (errorCode == AuthErrorCodes.CANNOT_RECOGNIZE_ERROR) {
            setResult(RESULT_INVALID_FINGERPRINT);
        } else {
            setResult(RESULT_ERROR);
        }
        finish();
    }

}
