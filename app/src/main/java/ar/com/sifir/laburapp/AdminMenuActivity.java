package ar.com.sifir.laburapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import ar.com.sifir.laburapp.adapters.NodeAdapter;
import ar.com.sifir.laburapp.entities.Node;
import ar.com.sifir.laburapp.helper.DBhelper;
import ar.com.sifir.laburapp.service.HttpService;

/**
 * Created by Sifir on 27/11/2017.
 */

public class AdminMenuActivity extends Activity implements Response.Listener<String>, Response.ErrorListener {

    private HttpService httpService;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        httpService = ((MyApplication) getApplication()).httpService;

        //cargo userId
        DBhelper helper = new DBhelper(this, "Login", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from Login", null);

        if (c.moveToFirst()) {
            userId = c.getString(c.getColumnIndex("id"));
        }

        httpService.nodesByAdmin(userId, this, this);

        db.close();
    }

    //nuevo nodo
    public void newNode(View v) {
        Intent intent = new Intent(this, NewNodeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //si falla
        Log.i("Error:", error.toString());
    }

    @Override
    public void onResponse(String response) {
        final Gson gson = new Gson();
        Node[] nodes = gson.fromJson(response, Node[].class);
        Log.i("exito:", response);

        //armo lista de nodos
        String[] nombres = new String[nodes.length];
        String[] ids = new String[nodes.length];
        int[] userCounts = new int[nodes.length];

        int i = 0;
        for (Node node : nodes) {
            nombres[i] = node.getName();

            if (userCounts[i] != 0) {
                userCounts[i] = node.getUserCount();
            } else {
                userCounts[i] = 0;
            }
            ids[i] = node.getId();
            i++;
        }

        //cargo los datos en la lista
        NodeAdapter adapter = new NodeAdapter(AdminMenuActivity.this, R.layout.item_listado_nodes, nombres, userCounts, ids);
        ListView nodelist = findViewById(R.id.nodeList);
        Log.i("Adapter: ", adapter.toString());
        nodelist.setAdapter(adapter);
    }
}
