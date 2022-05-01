package com.mpp.ProtoBuff.protobuffprotocol;


import com.mpp.domain.Arbitru;
import com.mpp.domain.Rezultat;
import com.mpp.services.ChatException;
import com.mpp.services.IChatObserver;
import com.mpp.services.IChatServices;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoChatProxy implements IChatServices {
    private String host;
    private int port;

    private IChatObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<ChatProtobufs.ChatResponse> qresponses;
    private volatile boolean finished;
    public ProtoChatProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<ChatProtobufs.ChatResponse>();
    }


//    public Arbitru[] getLoggedFriends(Arbitru user) throws ChatException {
//        sendRequest(ProtoUtils.createLoggedFriendsRequest(user));
//        ChatProtobufs.ChatResponse response=readResponse();
//        if (response.getType()==ChatProtobufs.ChatResponse.Type.Error){
//            String errorText=ProtoUtils.getError(response);
//            throw new ChatException(errorText);
//        }
//        User[] friends=ProtoUtils.getFriends(response);
//        return friends;
//    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(ChatProtobufs.ChatRequest request)throws ChatException{
        try {
            System.out.println("Sending request ..."+request);
            //request.writeTo(output);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new ChatException("Error sending object "+e);
        }

    }

    private ChatProtobufs.ChatResponse readResponse() throws ChatException{
        ChatProtobufs.ChatResponse response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws ChatException{
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(ChatProtobufs.ChatResponse updateResponse){
        switch (updateResponse.getType()){
            case NewMessage:{
                Rezultat message=ProtoUtils.getMessage(updateResponse);
                try {
                    client.messageReceived(message);
                } catch (ChatException e) {
                    e.printStackTrace();
                }
                break;
            }
            case FriendLoggedIn:
            {
                Arbitru friend=ProtoUtils.getUser(updateResponse);
                System.out.println("Friend logged in "+friend);
                try {
                    client.friendLoggedIn(friend);
                } catch (ChatException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

    @Override
    public void sendPunctaj(Rezultat rezultat) throws ChatException {
        sendRequest(ProtoUtils.createSendMesssageRequest(rezultat));
        ChatProtobufs.ChatResponse response=readResponse();
        if (response.getType()==ChatProtobufs.ChatResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new ChatException(errorText);
        }
    }

    @Override
    public void login(Arbitru user, IChatObserver client) throws ChatException {
        initializeConnection();
        System.out.println("Login request ...");
        sendRequest(ProtoUtils.createLoginRequest(user));
        ChatProtobufs.ChatResponse response=readResponse();
        if (response.getType()==ChatProtobufs.ChatResponse.Type.Ok){
            this.client=client;
            return;
        }
        if (response.getType()==ChatProtobufs.ChatResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new ChatException(errorText);
        }
    }

    @Override
    public void logout(Arbitru user, IChatObserver client) throws ChatException {
        sendRequest(ProtoUtils.createLogoutRequest(user));
        ChatProtobufs.ChatResponse response=readResponse();
        closeConnection();
        if (response.getType()==ChatProtobufs.ChatResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new ChatException(errorText);
        }
    }

    @Override
    public Arbitru[] getLoggedFriends() throws ChatException {
        sendRequest(ProtoUtils.createLoggedFriendsRequest());
        ChatProtobufs.ChatResponse response=readResponse();
        if (response.getType()==ChatProtobufs.ChatResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new ChatException(errorText);
        }
        Arbitru[] friends=ProtoUtils.getFriends(response);
        return friends;
    }


    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    ChatProtobufs.ChatResponse response=ChatProtobufs.ChatResponse.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private boolean isUpdateResponse(ChatProtobufs.ChatResponse.Type type){
        switch (type){
            case FriendLoggedIn:  return true;
            case FriendLoggedOut: return true;
            case NewMessage:return true;
        }
        return false;
    }
}
