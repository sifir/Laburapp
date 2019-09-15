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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import ar.com.sifir.laburapp.entities.User;

/**
 * Created by Sifir on 27/11/2017.
 */

public class NewNodeActivity extends Activity {
    TextView mTimeTextView1;
    TextView mTimeTextView2;
    Button mPickTimeButton1;
    Button mPickTimeButton2;
    Context mContext = this;

    EditText nombre;
    ProgressBar progressBar;
    User user;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_node);
        //progressBar = (ProgressBar) findViewById(R.id.progress_newnode);

        mTimeTextView1 = findViewById(R.id.timeTextView1);
        mTimeTextView2 = findViewById(R.id.timeTextView2);

        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        mPickTimeButton1 = findViewById(R.id.pickTimeButton1);
        mPickTimeButton2 = findViewById(R.id.pickTimeButton2);

        mPickTimeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        mTimeTextView1.setText(i + ":" + i1);
                    }
                },hour, minute, android.text.format.DateFormat.is24HourFormat(mContext));
                timePickerDialog.show();
            }
        });

        mPickTimeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        mTimeTextView2.setText(i + ":" + i1);
                    }
                },hour, minute, android.text.format.DateFormat.is24HourFormat(mContext));
                timePickerDialog.show();
            }
        });

        user = new User();
        user.load(this);
    }

    public void aceptarBtn (View v) {
        Intent i = new Intent(this, NFCNewNodeActivity.class);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //si devolvio ok
            if(resultCode == Activity.RESULT_OK){
                //progressBar.setVisibility(View.VISIBLE);
                nombre = findViewById(R.id.nodeName);

                final Gson gson = new Gson();

                JSONObject obj = new JSONObject();
                try{
                    obj.put("administrator", user.getId());
                    obj.put("name", nombre.getText().toString());
                    obj.put("tag",data.getStringExtra("result"));
                    obj.put("shift_starts", mTimeTextView1.getText());
                    obj.put("shift_ends", mTimeTextView2.getText());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(this);
                JsonObjectRequest request = new JsonObjectRequest (Request.Method.POST,
                        //url de nodos
                        "https://laburapp.herokuapp.com/nodes",
                        obj,
                        //1er callback - respuesta
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response){
                                Toast.makeText(getApplicationContext(), "Nodo creado correctamente", Toast.LENGTH_LONG).show();
                                Log.i("nodo creado bien ", response.toString());
  //                              progressBar.setVisibility(View.GONE);
                            }
                        },
                        //2do callback - error
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                progressBar.setVisibility(View.GONE);
                            }
                        });
                queue.add(request);
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

}
