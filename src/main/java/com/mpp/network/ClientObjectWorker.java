package com.mpp.network;


import com.mpp.domain.Arbitru;
import com.mpp.domain.Rezultat;
import com.mpp.services.ChatException;
import com.mpp.services.IChatObserver;
import com.mpp.services.IChatServices;
import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientObjectWorker implements Runnable, IChatObserver {
    private final IChatServices server;
    private final Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientObjectWorker(IChatServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                System.out.println("sunt aici");
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                   sendResponse((Response) response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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


    public void messageReceived(Rezultat rezultat) throws ChatException {
        Rezultat mdto= rezultat;
        System.out.println("Punctaj received  "+rezultat);
        try {
            sendResponse(new NewPunctajResponse(mdto));
        } catch (IOException e) {
            throw new ChatException("Sending error: "+e);
        }
    }

    @Override
    public void friendLoggedIn(Arbitru friend) throws ChatException {

    }


    private Response handleRequest(Request request){
        Response response=null;

        if (request instanceof AddPunctajRequest){
            System.out.println("AddPunctajRequest ...");
            AddPunctajRequest senReq=(AddPunctajRequest) request;
            Rezultat mdto=senReq.getRezultat();
            try {
                server.sendPunctaj(mdto);
                System.out.println("s-a trimis punctaj din clientObject");
                 return new OkResponse();
            } catch (ChatException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof LoginRequest){
            System.out.println("Login request ...");
            LoginRequest logReq=(LoginRequest)request;
            Arbitru udto=logReq.getArbitru();

            try {
                server.login(udto, this);
                return new OkResponse();
            } catch (ChatException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof LogoutRequest){
            System.out.println("Logout request");
            LogoutRequest logReq=(LogoutRequest)request;
            Arbitru udto=logReq.getArbitru();
            try {
                server.logout(udto, this);
                connected=false;
                return new OkResponse();

            } catch (ChatException e) {
                return new ErrorResponse(e.getMessage());
            }
        }


        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}
