package ar.com.sifir.laburapp.entities;

/**
 * Created by Sifir on 27/11/2017.
 */

public class Node {
    private String id;
    private String nombre;
    private String tag;
    private String geo;
    private int[] horarios;
    private int administrador;
    private User[] users;

    public Node(String nombre, String tag, String geo, int[] horarios, int administrador, User[] users){
        this.nombre = nombre;
        this.tag = tag;
        this.geo = geo;
        this.horarios = horarios;
        this.administrador = administrador;
        this.users = users;
    }

    public String getNombre() {return nombre;}

    public String getId() {return id;}

    public String getTag() {return tag;}

    public String getGeo() {return geo;}

    public int[] getHorarios() {return horarios;}

    public int getAdministrador() {return administrador;}

    public User[] getUsers(){return  users;}

    public int getUserCount() {return  users.length;}
}
