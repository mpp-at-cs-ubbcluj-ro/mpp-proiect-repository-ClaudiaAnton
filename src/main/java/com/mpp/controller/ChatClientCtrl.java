package com.mpp.controller;

import com.mpp.domain.Arbitru;
import com.mpp.domain.Rezultat;
import com.mpp.services.ChatException;
import com.mpp.services.IChatObserver;
import com.mpp.services.IChatServices;

import javax.swing.*;


public class ChatClientCtrl implements IChatObserver {
    private IChatServices server;
    private RezultatModelList rezultatModelList;
    private ArbitruModelList arbitruModelList;
    private Arbitru user;

    public ChatClientCtrl(IChatServices server) {
        this.server = server;
        this.rezultatModelList=new RezultatModelList();
        this.arbitruModelList=new ArbitruModelList();
    }

    public ListModel getArbitruListModel(){return arbitruModelList;}
    public ListModel getRezultatListModel(){return rezultatModelList;}

    public void messageReceived(Rezultat message) throws ChatException {
        rezultatModelList.newRezultat(message);
    }

    @Override
    public void friendLoggedIn(Arbitru friend) throws ChatException {
        arbitruModelList.newArbitru(friend);
    }


    public void logout() {
        try {
            server.logout(user, this);
        } catch (ChatException e) {
            System.out.println("Logout error "+e);
        }
    }

    public void login(String username, String parola) throws ChatException {
        Arbitru arbitru=new Arbitru("", username, parola, 0L);
        server.login(arbitru,this);
        user=arbitru;

        //var a=server.get
        //arbitruModelList.reset();
        Arbitru[] loggedInFriends=server.getLoggedFriends();
        for(Arbitru ar:loggedInFriends)
        {
            arbitruModelList.newArbitru(ar);
        }
    }

    public void sendMessage(Rezultat rezultat) throws ChatException{
        rezultatModelList.newRezultat(rezultat);
//        User sender=new User(user.getId());
//        User receiver=new User((String)friendsModel.getElementAt(indexFriend));
//        Message message=new Message(sender,txtMsg,receiver);
        server.sendPunctaj(rezultat);
    }

    public void sendMessageToAll(String txtMsg) throws ChatException{

    }


}
