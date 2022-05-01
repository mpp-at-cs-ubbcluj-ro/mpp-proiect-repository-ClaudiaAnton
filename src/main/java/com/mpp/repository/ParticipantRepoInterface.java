package com.mpp.repository;

import com.mpp.domain.NumPct;
import com.mpp.domain.Participant;

import java.util.List;

public interface ParticipantRepoInterface extends Repository<Long, Participant> {
    Long get_participant_punctajTotal_proba(Long participant);
}
