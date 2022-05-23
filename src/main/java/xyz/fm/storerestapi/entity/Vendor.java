package xyz.fm.storerestapi.entity;

import javax.persistence.*;

@Entity
public class Vendor extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "vendor_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String regNumber;

    private String ceo;

    @Embedded
    private Address location;

    protected Vendor() {/* empty */}

    private Vendor(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.regNumber = builder.regNumber;
        this.ceo = builder.ceo;
        this.location = builder.location;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getCeo() {
        return ceo;
    }

    public Address getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Vendor(" + "id: " + id +
                ", name: " + name +
                ", regNumber: " + regNumber +
                ", ceo: " + ceo +
                ", " + location +
                ")";
    }

    //== builder ==//
    public static class Builder {
        private Long id;
        private final String name;
        private final String regNumber;
        private final String ceo;
        private final Address location;

        public Builder(String name, String regNumber, String ceo, Address location) {
            this.name = name;
            this.regNumber = regNumber;
            this.ceo = ceo;
            this.location = location;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Vendor build() {
            return new Vendor(this);
        }
    }
}
