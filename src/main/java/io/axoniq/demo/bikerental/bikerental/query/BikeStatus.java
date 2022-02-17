package io.axoniq.demo.bikerental.bikerental.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BikeStatus {

    @Id
    private String bikeId;
    private String location;
    private String renter;

    public BikeStatus() {
    }

    @JsonCreator
    public BikeStatus(@JsonProperty("bikeId") String bikeId, @JsonProperty("location") String location) {
        this.bikeId = bikeId;
        this.location = location;
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRenter() {
        return renter;
    }

    public void setRenter(String renter) {
        this.renter = renter;
    }

}
