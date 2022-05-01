package com.mpp.domain;

import java.io.Serializable;

public class Proba extends EntityObj<Long> implements Serializable {
    String nume;

    public String getNume() {
        return nume;
    }

    public Proba(String nume) {
        this.nume = nume;
    }
}
