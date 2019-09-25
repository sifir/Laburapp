package ar.com.sifir.laburapp.entities;

import java.util.Date;

public class Shift {

    private String id;
    private Date started;
    private Date ended;

    public Shift() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getEnded() {
        return ended;
    }

    public void setEnded(Date ended) {
        this.ended = ended;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "id='" + id + '\'' +
                ", started=" + started +
                ", ended=" + ended +
                '}';
    }
}
