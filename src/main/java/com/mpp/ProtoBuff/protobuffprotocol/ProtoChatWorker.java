package com.mpp.ProtoBuff.protobuffprotocol;

import com.mpp.domain.Arbitru;
import com.mpp.domain.Rezultat;
import com.mpp.services.ChatException;
import com.mpp.services.IChatObserver;
import com.mpp.services.IChatServices;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class ProtoChatWorker implements Runnable, IChatObserver {
    private final IChatServices server;
     private final Socket connection;

     private InputStream input;
     private OutputStream output;
     private volatile boolean connected;
     public ProtoChatWorker(IChatServices server, Socket connection) {
         this.server = server;
         this.connection = connection;
         try{
             output=connection.getOutputStream() ;//new ObjectOutputStream(connection.getOutputStream());
             input=connection.getInputStream(); //new ObjectInputStream(connection.getInputStream());
             connected=true;
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public void run() {

         while(connected){
             try {
                // Object request=input.readObject();
                 System.out.println("Waiting requests ...");
                 ChatProtobufs.ChatRequest request=ChatProtobufs.ChatRequest.parseDelimitedFrom(input);
                 System.out.println("Request received: "+request);
                 ChatProtobufs.ChatResponse response=handleRequest(request);
                 if (response!=null){
                    sendResponse(response);
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }
             try {
                 Thread.sleep(1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
         try {
             input.close();
             output.close();
             connection.close();
         } catch (IOException e) {
             System.out.println("Error "+e);
         }
     }


     private ChatProtobufs.ChatResponse handleRequest(ChatProtobufs.ChatRequest request){
         ChatProtobufs.ChatResponse response=null;
         switch (request.getType()){
             case Login:{
                 System.out.println("Login request ...");
                 System.out.println(ProtoUtils.getUser(request));
                 Arbitru user=ProtoUtils.getUser(request);
                 try {
                     server.login(user, this);
                     return ProtoUtils.createOkResponse();
                 } catch (ChatException e) {
                     connected=false;
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 }
             }
             case Logout:{
                 System.out.println("Logout request");
                 Arbitru user=ProtoUtils.getUser(request);
                 try {
                     server.logout(user, this);
                     connected=false;
                     return ProtoUtils.createOkResponse();

                 } catch (ChatException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 }
             }
             case SendMessage:{
                 System.out.println("SendMessageRequest ...");
                 Rezultat message=ProtoUtils.getMessage(request);
                 try {
                     server.sendPunctaj(message);
                     return ProtoUtils.createOkResponse();
                 } catch (ChatException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 }
             }
             case GetLoggedFriends:{
                 System.out.println("GetLoggedFriends Request ...");
                 Arbitru user=ProtoUtils.getUser(request);
                 try {
                     Arbitru[] friends=server.getLoggedFriends();
                     return ProtoUtils.createLoggedFriendsResponse(friends);
                 } catch (ChatException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 }
             }
         }
         return response;
     }

     private void sendResponse(ChatProtobufs.ChatResponse response) throws IOException{
         System.out.println("sending response "+response);
         response.writeDelimitedTo(output);
         //output.writeObject(response);
         output.flush();
     }

    @Override
    public void messageReceived(Rezultat rezultat) throws ChatException {
        System.out.println("Message received  "+rezultat);
        try {
            sendResponse(ProtoUtils.createNewMessageResponse(rezultat));
        } catch (IOException e) {
            throw new ChatException("Sending error: "+e);
        }
    }

    @Override
    public void friendLoggedIn(Arbitru friend) throws ChatException {
        System.out.println("s-a logat " +friend.getUsername());
        try {
            sendResponse(ProtoUtils.createFriendLoggedInResponse(friend));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
