package com.mpp.service;

import com.mpp.domain.Arbitru;
import com.mpp.repository.ArbitruRepo;
import com.mpp.repository.ArbitruRepoInterface;

import java.util.List;

public class ArbitruService {
    private final ArbitruRepoInterface arbitruRepo;

    public ArbitruService(ArbitruRepo arbitruRepo) {
        this.arbitruRepo = arbitruRepo;
    }

    public Arbitru getArbitruByUsername(String st)
    {
        return arbitruRepo.getArbitruUsername(st);
    }

    public Arbitru saveArbitru(Arbitru a)
    {
        return null;
    }

    public Arbitru getArbitru(Long id){return arbitruRepo.findOne(id);}

    public Iterable<Arbitru> getAll(){return arbitruRepo.findAll();}
}
