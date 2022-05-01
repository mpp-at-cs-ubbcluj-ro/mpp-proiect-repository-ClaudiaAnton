package com.mpp.domain;

import java.io.Serializable;

public class Participant extends EntityObj<Long> implements Serializable {
    String nume;

    public Participant(String nume) {
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }
}
