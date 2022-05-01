package com.mpp.ProtoBuff.protobuffprotocol.utils;

import com.mpp.ProtoBuff.protobuffprotocol.ProtoChatWorker;
import com.mpp.services.IChatServices;

import java.net.Socket;


public class ChatProtobuffConcurrentServer extends AbsConcurrentServer {
    private IChatServices chatServer;
    public ChatProtobuffConcurrentServer(int port, IChatServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatProtobuffConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProtoChatWorker worker=new ProtoChatWorker(chatServer,client);
        Thread tw=new Thread(worker);
        return tw;
    }
}