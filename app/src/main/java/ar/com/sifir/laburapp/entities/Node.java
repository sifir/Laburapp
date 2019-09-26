package ar.com.sifir.laburapp.entities;

/**
 * Created by Sifir on 27/11/2017.
 */

public class Node {
    private String id;
    private String name;
    private String tag;
    private String geo;
    private String shiftStarts;
    private String shiftEnds;
    private String administrador;
    private User[] users;

    public Node(String name, String tag, String geo, String shiftStarts, String shiftEnds, String administrador, User[] users) {
        this.name = name;
        this.tag = tag;
        this.geo = geo;
        this.shiftStarts = shiftStarts;
        this.shiftEnds = shiftEnds;
        this.administrador = administrador;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public String getGeo() {
        return geo;
    }

    public String getAdministrador() {
        return administrador;
    }

    public User[] getUsers() {
        return users;
    }

    public int getUserCount() {
        return users.length;
    }

    public String getShiftStarts() {
        return shiftStarts;
    }

    public String getShiftEnds() {
        return shiftEnds;
    }
}
