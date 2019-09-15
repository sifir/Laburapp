package ar.com.sifir.laburapp;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import ar.com.sifir.laburapp.adapters.NodeAdapter;
import ar.com.sifir.laburapp.entities.Node;
import ar.com.sifir.laburapp.entities.User;

/**
 * Created by Sifir on 27/11/2017.
 */

public class AdminNodeListActivity extends Activity {

    String nodeUrl = "https://laburapp.herokuapp.com/nodes?administrator=";
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_list);

        //cargo userId
        DBhelper helper = new DBhelper(this, "Login", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from Login", null);

        if (c.moveToFirst() ){
                userId = c.getString(c.getColumnIndex("id"));
        }
        db.close();

        //QUERY CON VOLLEY
        final Gson gson = new Gson();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request =new StringRequest(Request.Method.GET, nodeUrl+userId,
                //1er callback - respuesta
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Node[] nodes = gson.fromJson(response, Node[].class);
                        Log.i("exito:", response);

                        //armo lista de nodos
                        String[] nombres = new String[nodes.length];
                        String[] ids = new String[nodes.length];
                        int[] userCounts = new int[nodes.length];

                        int i = 0;
                        for ( Node node : nodes ){
                            nombres[i] = node.getNombre();

                            if (userCounts[i] != 0){
                                userCounts[i] = node.getUserCount();
                            } else {
                                userCounts[i] = 0;
                            }
                            ids[i] = node.getId();
                            i++;
                        }

                        //cargo los datos en la lista
                        NodeAdapter adapter = new NodeAdapter(AdminNodeListActivity.this, R.layout.item_listado_nodes, nombres, userCounts);
                        ListView nodelist = findViewById(R.id.listadoList);
                        Log.i("Adapter: ", adapter.toString());
                        nodelist.setAdapter(adapter);

                    }
                },
                //2do callback - error
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
