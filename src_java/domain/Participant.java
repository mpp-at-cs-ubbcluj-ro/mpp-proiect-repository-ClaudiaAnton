package domain;

import java.util.List;

public class Participant extends Entity<Long>{
    String nume;
    List<Rezultat> probe;

    public Participant(String nume) {
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }

    public List<Rezultat> getProbe() {
        return probe;
    }
}
