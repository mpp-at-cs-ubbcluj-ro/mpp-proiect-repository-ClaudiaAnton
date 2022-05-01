package com.mpp.domain;

import java.io.Serializable;

public class NumPct implements Serializable {
    private Long id;
    private String nume;
    private Long punctaj;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NumPct(Long id, String nume, Long punctaj) {
        this.id = id;
        this.nume = nume;
        this.punctaj = punctaj;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Long getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(Long punctaj) {
        this.punctaj = punctaj;
    }
}
