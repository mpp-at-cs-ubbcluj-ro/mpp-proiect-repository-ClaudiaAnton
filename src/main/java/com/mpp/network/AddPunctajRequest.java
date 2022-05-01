package com.mpp.network;

import com.mpp.domain.NumPct;
import com.mpp.domain.Rezultat;

public class AddPunctajRequest implements Request{
    private final Rezultat rezultat;

    public AddPunctajRequest(Rezultat rezultat) {
        this.rezultat=rezultat;
    }

    public Rezultat getRezultat() {
        return rezultat;
    }
}
