package ar.com.sifir.laburapp;

import android.app.Application;

import ar.com.sifir.laburapp.service.HttpService;

public class MyApplication extends Application {

    public HttpService httpService;

    @Override
    public void onCreate() {
        super.onCreate();

        httpService = new HttpService(this);
    }
}
