package domain;

public class Arbitru extends Entity<Long>{
    String nume;
    String username;
    String parola;
    String numeProba;

    public Arbitru(String nume, String username, String parola, String numeProba) {
        this.nume = nume;
        this.username = username;
        this.parola = parola;
        this.numeProba = numeProba;
    }

    public String getNumeProba() {
        return numeProba;
    }

    public String getUsername() {
        return username;
    }

    public String getParola() {
        return parola;
    }

    public String getNume() {
        return nume;
    }
}
