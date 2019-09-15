package ar.com.sifir.laburapp.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.CheckBox;
import android.widget.EditText;

import ar.com.sifir.laburapp.DBhelper;
import ar.com.sifir.laburapp.R;

/**
 * Created by Sifir on 27/11/2017.
 */

public class User {

    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Node[] nodes;

    public User(){}

    public User(String id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public void save(Context ctx) {
        //guardo el login offline
            DBhelper helper = new DBhelper(ctx, "Login", null, 1);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.rawQuery("DELETE FROM Login", null);
            ContentValues registro = new ContentValues();
            registro.put("email", this.getEmail());
            registro.put("password", this.getPassword());
            registro.put("id", String.valueOf(this.getId()));
            db.insert("Login", null, registro);
            db.close();
    }

    public void load (Context ctx){
        DBhelper helper = new DBhelper(ctx, "Login", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from Login", null);
        //si hay algo guardado, lo traigo
        if (c.moveToFirst() != false){
            this.email = c.getString(c.getColumnIndex("email"));
            this.password = c.getString(c.getColumnIndex("password"));
            this.id = c.getString(c.getColumnIndex("id"));
        }
        db.close();
    }

/*    public void saveSession(Context ctx) {
        DBhelper helper = new DBhelper(ctx, "Session", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.rawQuery("DELETE FROM Session", null);
        ContentValues registro = new ContentValues();
        registro.put("email", this.getEmail());
        registro.put("password", this.getPassword());
        registro.put("id", String.valueOf(this.getId()));
        db.insert("Session", null, registro);
        db.close();
    }*/

    public void loadSession (Context ctx){
        DBhelper helper = new DBhelper(ctx, "Session", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from Session", null);
        //si hay algo guardado, lo traigo
        if (c.moveToFirst() != false){
            this.email = c.getString(c.getColumnIndex("email"));
            this.password = c.getString(c.getColumnIndex("password"));
            this.id = c.getString(c.getColumnIndex("id"));
        }
        db.close();
    }

    public void getOnlineNodes (){

    }

    public void saveNodesSession (Context ctx){

    }

    public void loadNodesSession (Context ctx){

    }

    public Node getNode(int i){
        return nodes[i];
    }

    public void setNode(Node node){
        int lenght = this.nodes.length;
        this.nodes[lenght+1]= node;
    }

    public String getId(){return  id;}

    public String getEmail(){return email;}

    public String getPassword(){return password;}
    public void setPassword(String password){
        this.password = password;
    }

    public String getFirstName(){return firstName;}
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){return lastName;}
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }
}
