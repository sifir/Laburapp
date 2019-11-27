package ar.com.sifir.laburapp.entities;

public class Invite {

    private String id;
    private Node node;
    private User requester;
    private User requested;

    public Invite(Node node, User requester, User requested) {
        this.node = node;
        this.requester = requester;
        this.requested = requested;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Node getNode() {
        return node;
    }

    public User getRequester() {
        return requester;
    }

    public User getRequested() {
        return requested;
    }

    @Override
    public String toString() {
        return "Invite{" +
                "id='" + id + '\'' +
                ", node=" + node +
                ", requester=" + requester +
                ", requested=" + requested +
                '}';
    }
}
