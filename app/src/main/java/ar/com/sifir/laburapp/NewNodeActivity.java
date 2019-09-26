package ar.com.sifir.laburapp;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import ar.com.sifir.laburapp.entities.Node;
import ar.com.sifir.laburapp.entities.User;
import ar.com.sifir.laburapp.service.HttpService;

/**
 * Created by Sifir on 27/11/2017.
 */

public class NewNodeActivity extends Activity implements View.OnClickListener {

    private HttpService httpService;

    TextView mTimeTextView1;
    TextView mTimeTextView2;
    Button mPickTimeButton1;
    Button mPickTimeButton2;
    Context mContext = this;

    EditText nombre;
    ProgressBar progressBar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_node);
        httpService = ((MyApplication) getApplication()).httpService;
        //progressBar = (ProgressBar) findViewById(R.id.progress_newnode);

        mTimeTextView1 = findViewById(R.id.timeTextView1);
        mTimeTextView2 = findViewById(R.id.timeTextView2);

        mPickTimeButton1 = findViewById(R.id.pickTimeButton1);
        mPickTimeButton2 = findViewById(R.id.pickTimeButton2);

        mPickTimeButton1.setOnClickListener(this);
        mPickTimeButton2.setOnClickListener(this);

        user = new User();
        user.load(this);
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
                nombre = findViewById(R.id.nodeName);

                httpService.createNode(
                        new Node(
                                nombre.getText().toString(),
                                data.getStringExtra("result"),
                                null,
                                mTimeTextView1.getText().toString().replace(":", ""),
                                mTimeTextView2.getText().toString().replace(":", ""),
                                user.getId(),
                                null
                        ),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), "Nodo creado correctamente", Toast.LENGTH_LONG).show();
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
}
