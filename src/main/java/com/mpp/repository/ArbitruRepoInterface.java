package com.mpp.repository;

import com.mpp.domain.Arbitru;

public interface ArbitruRepoInterface extends Repository<Long, Arbitru> {
    Arbitru getArbitruUsername(String st);
}
