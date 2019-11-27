package ar.com.sifir.laburapp;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ar.com.sifir.laburapp.entities.User;
import ar.com.sifir.laburapp.helper.DBhelper;
import ar.com.sifir.laburapp.service.DatabaseService;
import ar.com.sifir.laburapp.service.HttpService;

public class MyApplication extends Application {

    public HttpService httpService;
    public DatabaseService databaseService;

    @Override
    public void onCreate() {
        super.onCreate();

        httpService = new HttpService(this);
        databaseService = new DatabaseService(this);
    }
}
