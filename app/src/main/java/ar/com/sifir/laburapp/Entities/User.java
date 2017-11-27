package ar.com.sifir.laburapp.Entities;

/**
 * Created by Sifir on 27/11/2017.
 */

public class User {

    private Number id;
    private String email;
    private String password;

    public User(Number id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Number getId(){return  id;}

    public String getEmail(){return email;}

    public String getPassword(){return password;}
}
