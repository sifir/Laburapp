package ar.com.sifir.laburapp.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ar.com.sifir.laburapp.entities.User;
import ar.com.sifir.laburapp.helper.DBhelper;

public class DatabaseService {
    private Context context;

    public DatabaseService(Context context) {
        this.context = context;
    }

    public void deleteAll() {
        SQLiteDatabase db = connection();
        db.delete("Login", null, null);
        db.close();
    }

    public void save(User user, Boolean remember) {
        //guardo el login offline
        SQLiteDatabase db = connection();
        ContentValues registro = new ContentValues();
        registro.put("id", String.valueOf(user.getId()));
        registro.put("email", user.getEmail());
        if (remember)
            registro.put("password", user.getPassword());
        db.insert("Login", null, registro);
        db.close();
    }

    public User load() {
        SQLiteDatabase db = connection();
        Cursor c = db.rawQuery("Select * from Login", null);
        User user = null;
        if (c.moveToFirst()) {
            user = new User();
            user.setEmail(c.getString(c.getColumnIndex("email")));
            user.setPassword(c.getString(c.getColumnIndex("password")));
            user.setId(c.getString(c.getColumnIndex("id")));
        }
        c.close();
        db.close();
        return user;
    }


    private SQLiteDatabase connection() {
        DBhelper helper = new DBhelper(context, "Application", null, 1);
        return helper.getWritableDatabase();
    }

}
