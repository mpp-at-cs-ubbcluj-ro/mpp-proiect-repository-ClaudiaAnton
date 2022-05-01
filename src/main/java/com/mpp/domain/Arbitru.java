package com.mpp.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table( name = "Arbitru" )
public class Arbitru  implements Serializable,Comparable<Arbitru> {
    Long id;


    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    String nume;
    String username;
    String parola;
    Long proba;

    public Arbitru(String nume, String username, String parola, Long proba) {
        this.nume = nume;
        this.username = username;
        this.parola = parola;
        this.proba = proba;
    }

    public Arbitru() {
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public void setProba(Long proba) {
        this.proba = proba;
    }

    public Long getProba() {
        return proba;
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

    @Override
    public int compareTo(Arbitru o) {
        return (int) (this.getId()- o.getId());
    }
}
