package ar.com.sifir.laburapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import ar.com.sifir.laburapp.adapters.NodeAdapter;
import ar.com.sifir.laburapp.entities.Node;

/**
 * Created by Sifir on 27/11/2017.
 */

public class AdminMenuActivity extends BaseActivity implements Response.Listener<String>, Response.ErrorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        setTitle("Administrador");
        //getActionBar().setIcon(R.drawable.laburappthumb);

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
    public void refresh() {
        super.refresh();
        fetchData();
    }

    private void fetchData() {
        getHttpService().nodesByAdmin(mUser.getId(), this, this);
    }

    @Override
    public void onResponse(String response) {
        final Gson gson = new Gson();
        Node[] nodes = gson.fromJson(response, Node[].class);
        Log.i("exito:", response);

        //armo lista de nodos
        String[] nombres = new String[nodes.length];
        String[] ids = new String[nodes.length];

        int i = 0;
        for (Node node : nodes) {
            nombres[i] = node.getName();
            ids[i] = node.getId();
            i++;
        }
        NodeAdapter adapter = new NodeAdapter(AdminMenuActivity.this, R.layout.item_listado_nodes, nombres, ids);
        ListView nodelist = (ListView) findViewById(R.id.nodeList);
        Log.i("Adapter: ", adapter.toString());
        nodelist.setAdapter(adapter);
    }
}
