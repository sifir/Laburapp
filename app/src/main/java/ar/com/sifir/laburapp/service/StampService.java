package ar.com.sifir.laburapp.service;

import android.app.Activity;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import ar.com.sifir.laburapp.FingerActivity;
import ar.com.sifir.laburapp.ReadNFCActivity;
import ar.com.sifir.laburapp.entities.User;

import static ar.com.sifir.laburapp.MainActivity.SERVER_URL;

public class StampService {

    private final Activity context;
    private final Response.Listener<JSONObject> stampSuccess;
    private final Response.ErrorListener stampError;

    private final int STEP_READ_NFC = 0;
    private final int STEP_READ_FINGER = 1;
    private final int STEP_STAMP = 2;

    private int step = STEP_READ_NFC;
    private String tag;

    public StampService(Activity context, Response.Listener<JSONObject> stampSuccess, Response.ErrorListener stampError) {
        this.context = context;
        this.stampSuccess = stampSuccess;
        this.stampError = stampError;
    }

    public void stamp() {
        next(null);
    }

    public void stamp(String tag) {
        next(tag);
    }

    public void abort() {
        this.tag = null;
        this.step = STEP_READ_NFC;
    }

    private void next(@Nullable String tag) {
        if (tag != null)
            this.tag = tag;

        switch (step) {
            case STEP_READ_NFC:
                readNFC();
                step = STEP_READ_FINGER;
                break;
            case STEP_READ_FINGER:
                readFingerPrint();
                step = STEP_STAMP;
                break;
            case STEP_STAMP:
                makeStamp();
                step = STEP_READ_NFC;
        }
    }


    private void readNFC() {
        Intent intent = new Intent(context, ReadNFCActivity.class);
        context.startActivityForResult(intent, ReadNFCActivity.REQUEST_CODE);
    }

    private void readFingerPrint() {
        Intent intent = new Intent(context, FingerActivity.class);
        context.startActivityForResult(intent, FingerActivity.REQUEST_CODE);
    }

    private void makeStamp() {
        final Gson gson = new Gson();
        //cargo el usuario
        User user = new User();
        user.load(context);
        JSONObject obj = new JSONObject();
        try {
            obj.put("user", user.getId());
            obj.put("tag", this.tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                //url de login
                SERVER_URL + "/stamps",
                obj,
                this.stampSuccess,
                this.stampError
        );
        queue.add(request);
        this.tag = null;
    }
}
