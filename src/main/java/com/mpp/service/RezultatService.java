package com.mpp.service;

import com.mpp.domain.Rezultat;
import com.mpp.events.EventType;
import com.mpp.observer.Observable;
import com.mpp.observer.Observer;
import com.mpp.repository.RezultatRepo;

import java.util.ArrayList;
import java.util.List;

public class RezultatService implements Observable<EventType> {
    private final RezultatRepo rezultatRepo;
    private final List<Observer<EventType>> observers=new ArrayList<>();

    public RezultatService(RezultatRepo rezultatRepo) {
        this.rezultatRepo = rezultatRepo;
    }

    public void addRezultat(Rezultat rez) {
        rezultatRepo.save(rez);
        notifyObservers(new EventType());
    }

    @Override
    public void addObserver(Observer<EventType> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EventType> e) {

    }
    @Override
    public void notifyObservers(EventType t) {
        observers.stream().forEach(x->x.update(t));
    }

}
