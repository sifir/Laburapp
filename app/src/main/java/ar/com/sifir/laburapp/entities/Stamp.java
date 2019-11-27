package ar.com.sifir.laburapp.entities;

import java.util.Date;

public class Stamp {

    private Shift shift;
    private Date createdAt;
    private String error;

    public Stamp() {
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Stamp{" +
                "shift=" + shift +
                ", createdAt=" + createdAt +
                ", error='" + error + '\'' +
                '}';
    }
}
