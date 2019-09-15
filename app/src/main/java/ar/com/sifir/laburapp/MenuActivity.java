package ar.com.sifir.laburapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Sifir on 27/11/2017.
 */

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    //user
    public void fichar (View v){
        Intent intent = new Intent(this, FingerActivity.class);
        startActivity(intent);

        /*va tdo aca, start activity for result 2*/
    }

    //admin
    public void admin (View v){
        Intent intent = new Intent(this, AdminMenuActivity.class);
        startActivity(intent);
    }
}
