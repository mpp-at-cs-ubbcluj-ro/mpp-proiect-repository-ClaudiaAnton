package com.mpp.service;

import com.mpp.domain.Proba;
import com.mpp.repository.ProbaRepo;

public class ProbaService {
    private final ProbaRepo probaRepo;

    public ProbaService(ProbaRepo probaRepo) {
        this.probaRepo = probaRepo;
    }

    public Proba getProba(Long id) {
        return probaRepo.findOne(id);
    }
}
