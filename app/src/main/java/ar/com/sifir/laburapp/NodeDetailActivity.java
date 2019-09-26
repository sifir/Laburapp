package ar.com.sifir.laburapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import ar.com.sifir.laburapp.entities.Node;
import ar.com.sifir.laburapp.entities.User;
import ar.com.sifir.laburapp.service.HttpService;


public class NodeDetailActivity extends Activity {

    private HttpService httpService;

    String nodeID;
    TextView name;
    EditText geo;
    EditText horario;

    //https://laburapp.herokuapp.com/nodes?id=5d7eb9c42a12237a0c1335da

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_detail);
        httpService = ((MyApplication) getApplication()).httpService;
        name = findViewById(R.id.nodeTitleTxt);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        nodeID = bundle.getString("ID");

        //QUERY CON VOLLEY
        final Gson gson = new Gson();

        httpService.nodeById(
                nodeID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Node node = gson.fromJson(response, Node.class);
                        Log.i("exito:", response);

                        String nombre = "";
                        nombre = node.getName();
                        String geo = "";
                        geo = node.getGeo();
                        User[] users = node.getUsers();

                        //cargo datos
                        //NUEVO USER ADAPTER ACA
                        name.setText(nombre);
                        //ETC
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //si falla
                        Log.i("Error:", error.toString());
                    }
                }
        );
    }
}
