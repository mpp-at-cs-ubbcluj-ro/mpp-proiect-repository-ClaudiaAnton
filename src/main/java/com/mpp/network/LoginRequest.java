package com.mpp.network;


import com.mpp.domain.Arbitru;

public class LoginRequest implements Request {
    private final Arbitru user;

    public LoginRequest(Arbitru user) {
        this.user = user;
    }

    public Arbitru getArbitru() {
        return user;
    }
}
