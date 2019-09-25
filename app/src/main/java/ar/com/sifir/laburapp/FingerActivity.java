package ar.com.sifir.laburapp;

import android.app.Activity;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import ar.com.sifir.laburapp.entities.User;

import static ar.com.sifir.laburapp.MainActivity.SERVER_URL;

/**
 * Created by Sifir on 27/11/2017.
 */

public class FingerActivity extends Activity implements FingerPrintAuthCallback {
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
