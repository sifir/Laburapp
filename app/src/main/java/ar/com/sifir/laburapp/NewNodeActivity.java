package ar.com.sifir.laburapp;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ar.com.sifir.laburapp.entities.Location;
import ar.com.sifir.laburapp.entities.Node;
import ar.com.sifir.laburapp.entities.User;
import ar.com.sifir.laburapp.service.GPStracker;
import ar.com.sifir.laburapp.service.HttpService;

/**
 * Created by Sifir on 27/11/2017.
 */

public class NewNodeActivity extends BaseActivity implements View.OnClickListener {

    TextView mTimeTextView1;
    TextView mTimeTextView2;
    Button mPickTimeButton1;
    Button mPickTimeButton2;
    CheckBox checkBox0;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;
    CheckBox checkBox5;
    CheckBox checkBox6;
    Context mContext = this;
    Switch isGPSenabled;
    Switch isFingerEnabled;

    EditText nombre;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_node);
        setTitle("Nuevo Nodo");
        //progressBar = (ProgressBar) findViewById(R.id.progress_newnode);

        checkBox0 = (CheckBox) findViewById(R.id.check0);
        checkBox1 = (CheckBox) findViewById(R.id.check1);
        checkBox2 = (CheckBox) findViewById(R.id.check2);
        checkBox3 = (CheckBox) findViewById(R.id.check3);
        checkBox4 = (CheckBox) findViewById(R.id.check4);
        checkBox5 = (CheckBox) findViewById(R.id.check5);
        checkBox6 = (CheckBox) findViewById(R.id.check6);
        isGPSenabled = (Switch) findViewById(R.id.GPSenabled);
        isFingerEnabled = (Switch) findViewById(R.id.fingerEnabled);

        mTimeTextView1 = (TextView) findViewById(R.id.timeTextView1);
        mTimeTextView2 = (TextView) findViewById(R.id.timeTextView2);

        mPickTimeButton1 = (Button) findViewById(R.id.pickTimeButton1);
        mPickTimeButton2 = (Button) findViewById(R.id.pickTimeButton2);

        mPickTimeButton1.setOnClickListener(this);
        mPickTimeButton2.setOnClickListener(this);

    }

    private String formatZeroes(int hours, int minutes) {
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    public void aceptarBtn(View v) {
        Intent i = new Intent(this, ReadNFCActivity.class);
        startActivityForResult(i, ReadNFCActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ReadNFCActivity.REQUEST_CODE) {
            //si devolvio ok
            if (resultCode == ReadNFCActivity.RESULT_OK) {
                //progressBar.setVisibility(View.VISIBLE);
                nombre = (EditText) findViewById(R.id.nodeName);
                getHttpService().createNode(
                        new Node(
                                nombre.getText().toString(),
                                data.getStringExtra("result"),
                                getCurrentLocation().toString(),
                                mTimeTextView1.getText().toString().replace(":", ""),
                                mTimeTextView2.getText().toString().replace(":", ""),
                                mUser.getId(),
                                null,
                                parseDays(),
                                isGPSenabled.isChecked(),
                                isFingerEnabled.isChecked()
                        ),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.nodeCreated), Toast.LENGTH_LONG).show();
                                finish();
                                Log.i("nodo creado bien ", response.toString());
                                //                              progressBar.setVisibility(View.GONE);
                            }
                        },
                        //2do callback - error
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("nodo crear", String.valueOf(error.networkResponse.statusCode));
//                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        }
    }

    @Override
    public void onClick(final View v) {
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                if (v.getId() == mPickTimeButton1.getId())
                    mTimeTextView1.setText(formatZeroes(hours, minutes));
                else if (v.getId() == mPickTimeButton2.getId())
                    mTimeTextView2.setText(formatZeroes(hours, minutes));
            }
        }, hour, minute, android.text.format.DateFormat.is24HourFormat(mContext));
        timePickerDialog.show();
    }

    private ArrayList<Boolean> parseDays() {
        ArrayList<Boolean> days = new ArrayList<>();
        days.add(checkBox0.isChecked());
        days.add(checkBox1.isChecked());
        days.add(checkBox2.isChecked());
        days.add(checkBox3.isChecked());
        days.add(checkBox4.isChecked());
        days.add(checkBox5.isChecked());
        days.add(checkBox6.isChecked());
        return days;
    }

    private Location getCurrentLocation() {
        GPStracker gps = new GPStracker(this);
        return new Location(
                getLatitude(gps),
                getLongitude(gps)
        );
    }

    private double getLatitude(GPStracker gps) {
        android.location.Location l = gps.getLocation();
        if (l != null) {
            return l.getLatitude();
        }
        return 0;
    }

    private double getLongitude(GPStracker gps) {
        android.location.Location l = gps.getLocation();
        if (l != null) {
            return l.getLongitude();
        }
        return 0;
    }
}
