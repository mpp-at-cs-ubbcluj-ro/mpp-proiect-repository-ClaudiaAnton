package domain;

public class Proba extends Entity<Long> {
    String nume;

    public String getNume() {
        return nume;
    }

    public Proba(String nume) {
        this.nume = nume;
    }
}
