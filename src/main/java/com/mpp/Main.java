package com.mpp;

import com.mpp.domain.Arbitru;
import com.mpp.domain.Participant;
import com.mpp.domain.Proba;
import com.mpp.domain.Rezultat;
import com.mpp.repository.ArbitruRepo;
import com.mpp.repository.ParticipantRepo;
import com.mpp.repository.ProbaRepo;
import com.mpp.repository.RezultatRepo;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        ArbitruRepo arbrepo=new ArbitruRepo(props);
        Arbitru arb=arbrepo.findOne(1L);
        System.out.println(arb.getNume());
        //arbrepo.save(new Arbitru("ion","ion@","a","1"));

        ParticipantRepo partrepo=new ParticipantRepo(props);
        //partrepo.save(new Participant("ana"));
//        System.out.println(partrepo.get_participant_punctajTotal_proba(1L,1L));

        ProbaRepo probarepo=new ProbaRepo(props);
        //probarepo.save(new Proba("canotaj"));

        RezultatRepo rezrepo=new RezultatRepo(props);
//        rezrepo.save(new Rezultat(1L,1L,10L));
    }
}
