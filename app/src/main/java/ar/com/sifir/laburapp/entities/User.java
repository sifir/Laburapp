package ar.com.sifir.laburapp.entities;

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

    public User() {
    }

    public User(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void getOnlineNodes() {

    }

    public Node getNode(int i) {
        return nodes[i];
    }

    public void setNode(Node node) {
        int lenght = this.nodes.length;
        this.nodes[lenght + 1] = node;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

}
