package com.mpp.service;

import com.mpp.domain.NumPct;
import com.mpp.events.EventType;
import com.mpp.observer.Observable;
import com.mpp.observer.Observer;
import com.mpp.repository.ParticipantRepo;
import com.mpp.domain.Participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantService implements Observable<EventType> {
    private final ParticipantRepo participantRepo;
    private final List<Observer<EventType>> observers=new ArrayList<>();


    public ParticipantService(ParticipantRepo participantRepo) {
        this.participantRepo = participantRepo;
    }
    public Iterable<Participant> getAll(){
        return participantRepo.findAll();
    }

    public Participant getOne(Long id){return participantRepo.findOne(id);}

    public List<NumPct> getListProba()
    {
        List<NumPct> list=new ArrayList<>();
        for(Participant i:getAll())
        {
            Long sum=participantRepo.get_participant_punctajTotal_proba(i.getId());
            NumPct x=new NumPct(i.getId(),i.getNume(),sum);
            list.add(x);
        }
        return list;
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
