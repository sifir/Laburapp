package ar.com.sifir.laburapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Sifir on 27/11/2017.
 */

public class AdminMenuActivity extends Activity {

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
    }

    //nuevo nodo
    public void newNode (View v){
        Intent intent = new Intent(this, NewNodeActivity.class);
        startActivity(intent);
    }

    //admin nodos
    public void adminNode (View v){
        Intent intent = new Intent(this, AdminNodeListActivity.class);
        startActivity(intent);
    }
}
