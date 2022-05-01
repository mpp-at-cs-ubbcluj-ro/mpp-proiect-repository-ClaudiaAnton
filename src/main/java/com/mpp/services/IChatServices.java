package com.mpp.services;


import com.mpp.domain.Arbitru;
import com.mpp.domain.Rezultat;

public interface IChatServices {
    //, IChatObserver client
    void sendPunctaj(Rezultat rezultat) throws ChatException;
     //de adugat
     void login(Arbitru user, IChatObserver client) throws ChatException;
//     void sendMessage(Message message) throws ChatException;
     void logout(Arbitru user, IChatObserver client) throws ChatException;


     Arbitru[] getLoggedFriends() throws ChatException;

}
