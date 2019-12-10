package logic.model;

import javax.persistence.*;

@Embeddable
public class Place {
    private String name;
    private String address;

    public Place() {
    }

    public Place(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
