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
}
