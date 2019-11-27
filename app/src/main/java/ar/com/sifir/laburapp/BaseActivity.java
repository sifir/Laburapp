package ar.com.sifir.laburapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import ar.com.sifir.laburapp.entities.User;
import ar.com.sifir.laburapp.service.DatabaseService;
import ar.com.sifir.laburapp.service.HttpService;

public abstract class BaseActivity extends AppCompatActivity {

    private HttpService httpService;
    private DatabaseService databaseService;
    private final Gson gson = new Gson();

    protected User mUser = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpService = ((MyApplication) getApplication()).httpService;
        databaseService = ((MyApplication) getApplication()).databaseService;
        mUser = databaseService.load();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public HttpService getHttpService() {
        return httpService;
    }

    public DatabaseService getDatabaseService() {return databaseService;}

    public Gson getGson() {
        return gson;
    }

    public User getUser() {
        return mUser;
    }

    public void refresh(){}

}
