package com.mpp.services;


import com.mpp.domain.Arbitru;
import com.mpp.domain.Rezultat;
import org.apache.logging.log4j.message.Message;

public interface IChatObserver {
     //de adugat
     void messageReceived(Rezultat rezultat) throws ChatException;
     void friendLoggedIn(Arbitru friend) throws ChatException;
//     void friendLoggedOut(User friend) throws ChatException;
}
