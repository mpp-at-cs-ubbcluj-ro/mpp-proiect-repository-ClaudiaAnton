package domain;

public class Rezultat {
    Long probaID;
    Long participantID;
    Long puncte;

    public Rezultat(Long probaID, Long participantID) {
        this.probaID = probaID;
        this.participantID = participantID;
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
