package logic.model;

import javax.persistence.*;

@Embeddable
public class Place {
    private String name;
    private String address;
}