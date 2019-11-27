package ar.com.sifir.laburapp.entities;

import java.util.Objects;

public class Location {

    private Double lat;
    private Double lng;

    public Location() {
    }

    public Location(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String[] splitLocation(String locationString){
        String[] splitted = new String[2];
        if (locationString.contains(":")){
            splitted = locationString.split(":");
        }
        return splitted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(lat, location.lat) &&
                Objects.equals(lng, location.lng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }

    @Override
    public String toString() {
        return lat + ":" + lng;
    }
}
