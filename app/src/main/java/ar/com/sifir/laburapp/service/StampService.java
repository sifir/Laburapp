package ar.com.sifir.laburapp.service;

import android.content.Intent;

import com.android.volley.Response;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import ar.com.sifir.laburapp.BaseActivity;
import ar.com.sifir.laburapp.FingerActivity;
import ar.com.sifir.laburapp.LocationActivity;
import ar.com.sifir.laburapp.ReadNFCActivity;
import ar.com.sifir.laburapp.entities.Node;

public class StampService {

    private final BaseActivity context;
    private final Response.Listener<JSONObject> stampSuccess;
    private final Response.ErrorListener stampError;

    private final int STEP_READ_NFC = 0;
    private final int STEP_READ_FINGER = 1;
    private final int STEP_STAMP = 2;
    private final int STEP_GPS = 3;

    private int step = STEP_READ_NFC;
    private Node node;

    public StampService(BaseActivity context, Response.Listener<JSONObject> stampSuccess, Response.ErrorListener stampError) {
        this.context = context;
        this.stampSuccess = stampSuccess;
        this.stampError = stampError;
    }

    public void stamp() {
        next(null);
    }

    public void stamp(Node node) {
        next(node);
    }

    public void abort() {
        this.node = null;
        this.step = STEP_READ_NFC;
    }

    private void next() {
        this.next(null);
    }

    private void next(@Nullable Node node) {
        if (node != null)
            this.node = node;

        switch (step) {
            case STEP_READ_NFC:
                readNFC();
                step = STEP_READ_FINGER;
                break;
            case STEP_READ_FINGER:
                readFingerPrint();
                step = STEP_GPS;
                break;
            case STEP_GPS:
                readGps();
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
        if (node == null || !node.isFingerEnabled())
            next();

        Intent intent = new Intent(context, FingerActivity.class);
        context.startActivityForResult(intent, FingerActivity.REQUEST_CODE);
    }

    private void readGps() {
        if (node == null || !node.isGPSenabled())
            next();

        Intent intent = new Intent(context, LocationActivity.class);
        context.startActivityForResult(intent, LocationActivity.REQUEST_CODE);
    }

    private void makeStamp() {
        //cargo el usuario
        context.getHttpService().stamp(context.getUser(), this.node, this.stampSuccess, this.stampError);

        this.node = null;
    }
}
