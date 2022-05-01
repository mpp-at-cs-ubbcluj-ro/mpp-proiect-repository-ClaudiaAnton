package com.mpp.network;


import com.mpp.domain.Arbitru;


public class LogoutRequest implements Request {
    private final Arbitru user;

    public LogoutRequest(Arbitru user) {
        this.user = user;
    }

    public Arbitru getArbitru() {
        return user;
    }
}
