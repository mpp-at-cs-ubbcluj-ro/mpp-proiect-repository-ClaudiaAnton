package com.mpp.ProtoBuff.protobuffprotocol.utils;



import com.mpp.network.ClientObjectWorker;
import com.mpp.services.IChatServices;

import java.net.Socket;


public class ChatObjectConcurrentServer extends AbsConcurrentServer {
    private IChatServices chatServer;
    public ChatObjectConcurrentServer(int port, IChatServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker=new ClientObjectWorker(chatServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }


}
