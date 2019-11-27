package ar.com.sifir.laburapp.entities;

import java.util.ArrayList;

/**
 * Created by Sifir on 27/11/2017.
 */

public class Node {
    private String id;
    private String name;
    private String tag;
    private String location;
    private String shiftStarts;
    private String shiftEnds;
    private String administrador;
    private User[] users;
    private boolean isGPSenabled;
    private boolean isFingerEnabled;
    private ArrayList<Boolean> days;

    public Node(String name, String tag, String location, String shiftStarts, String shiftEnds, String administrador, User[] users, ArrayList<Boolean> days, boolean isGPSenabled, boolean isFingerEnabled) {
        this.name = name;
        this.tag = tag;
        this.location = location;
        this.shiftStarts = shiftStarts;
        this.shiftEnds = shiftEnds;
        this.administrador = administrador;
        this.users = users;
        this.days = days;
        this.isGPSenabled = isGPSenabled;
        this.isFingerEnabled = isFingerEnabled;
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

    public String getLocation() {
        return location;
    }

    public boolean isGPSenabled() {
        return isGPSenabled;
    }

    public boolean isFingerEnabled() {
        return isFingerEnabled;
    }

    public String getAdministrador() {
        return administrador;
    }

    public User[] getUsers() {
        return users;
    }

    public ArrayList<Boolean> getDays() { return days; }

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
