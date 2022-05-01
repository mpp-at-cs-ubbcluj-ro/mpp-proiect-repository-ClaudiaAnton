package com.mpp.networkUtils;



import com.mpp.network.ClientObjectWorker;
import com.mpp.services.IChatServices;

import java.net.Socket;


public class ObjectConcurrentServer extends AbsConcurrentServer {
    private final IChatServices chatServer;
    public ObjectConcurrentServer(int port, IChatServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker=new ClientObjectWorker(chatServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }


}
