package com.mpp.network;

import com.mpp.domain.Rezultat;

public class NewPunctajResponse implements UpdateResponse{
    private final Rezultat rezultat;

    public NewPunctajResponse(Rezultat rezultat) {
        this.rezultat = rezultat;
    }

    public Rezultat getRezultat() {
        return rezultat;
    }
}
