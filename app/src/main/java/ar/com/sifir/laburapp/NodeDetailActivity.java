package ar.com.sifir.laburapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import ar.com.sifir.laburapp.entities.Node;
import ar.com.sifir.laburapp.entities.User;

import static ar.com.sifir.laburapp.MainActivity.SERVER_URL;

public class NodeDetailActivity extends Activity {
    String nodeDetailUrl = "/nodes?id=";
    String nodeID;
    TextView name;
    EditText geo;
    EditText horario;

    //https://laburapp.herokuapp.com/nodes?id=5d7eb9c42a12237a0c1335da

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_detail);
        name =  findViewById(R.id.nodeTitleTxt);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        nodeID = bundle.getString("ID");

        //QUERY CON VOLLEY
        final Gson gson = new Gson();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, SERVER_URL + nodeDetailUrl + nodeID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Node node = gson.fromJson(response, Node.class);
                        Log.i("exito:", response);

                        String nombre = "";
                        nombre = node.getName();
                        String geo = "";
                        geo = node.getGeo();
                        int horarios[] = node.getHorarios();
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
                });
        queue.add(request);
    }
}
