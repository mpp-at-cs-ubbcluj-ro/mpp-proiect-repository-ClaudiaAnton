package com.mpp.domain;

import java.io.Serializable;

public class Rezultat extends EntityObj<Long> implements Serializable {
    Long probaID;
    Long participantID;
    Long puncte;

    public Rezultat(Long probaID, Long participantID,Long puncte) {
        this.probaID = probaID;
        this.participantID = participantID;
        this.puncte=puncte;
    }

    public void setProbaID(Long probaID) {
        this.probaID = probaID;
    }

    public void setParticipantID(Long participantID) {
        this.participantID = participantID;
    }

    public Rezultat() {
    }

    public Long getProbaID() {
        return probaID;
    }

    public Long getParticipantID() {
        return participantID;
    }

    public Long getPuncte() {
        return puncte;
    }

    public void setPuncte(Long puncte) {
        this.puncte = puncte;
    }
}
