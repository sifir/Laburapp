package ar.com.sifir.laburapp.entities;

/**
 * Created by Sifir on 27/11/2017.
 */

public class Node {
    private String nombre;
    private String tag;
    private String geo;
    int[] horarios;
    int administrador;

    public Node(String nombre, String tag, String geo, int[] horarios, int administrador){
        this.nombre = nombre;
        this.tag = tag;
        this.geo = geo;
        this.horarios = horarios;
        this.administrador = administrador;
    }

    public String getNombre() {return nombre;}

    public String getTag() {return tag;}

    public String getGeo() {return geo;}

    public int[] getHorarios() {return horarios;}

    public int getAdministrador() {return administrador;}
}
