package com.mpp.network;


import com.mpp.domain.Arbitru;
import com.mpp.domain.Rezultat;
import com.mpp.services.ChatException;
import com.mpp.services.IChatObserver;
import com.mpp.services.IChatServices;
import org.apache.logging.log4j.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServicesObjectProxy implements IChatServices {
    private final String host;
    private final int port;

    private IChatObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    //private List<Response> responses;
    private final BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        //responses=new ArrayList<Response>();
        qresponses = new LinkedBlockingQueue<Response>();
    }


    public void login(Arbitru user, IChatObserver client) throws ChatException {
        initializeConnection();
        sendRequest(new LoginRequest(user));
        Response response = readResponse();
        if (response instanceof OkResponse) {
            this.client = client;
            return;
        }
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            closeConnection();
            throw new ChatException(err.getMessage());
        }
    }

    public void sendPunctaj(Rezultat rezultat) throws ChatException {
        System.out.println("am ajuns in proxy");
        sendRequest(new AddPunctajRequest(rezultat));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new ChatException(err.getMessage());
        }
    }

    public void logout(Arbitru user, IChatObserver client) throws ChatException {
        sendRequest(new LogoutRequest(user));
        Response response = readResponse();
        closeConnection();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new ChatException(err.getMessage());
        }
    }

    @Override
    public Arbitru[] getLoggedFriends() throws ChatException {
        return new Arbitru[0];
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws ChatException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ChatException("Error sending object " + e);
        }

    }

    private Response readResponse() throws ChatException {
        Response response = null;
        try {

            response = qresponses.take();
            System.out.println("raspuns: "+response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws ChatException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(UpdateResponse update) {

        if (update instanceof NewPunctajResponse) {
            NewPunctajResponse msgRes = (NewPunctajResponse) update;
            Rezultat rezultat = msgRes.getRezultat();
            try {
                client.messageReceived(rezultat);
            } catch (ChatException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReaderThread implements Runnable {
        //private class ReaderThread extends Thread{
        public void run() {
            System.out.println("okok");
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse) response);
                    } else {
                        /*responses.add((Response)response);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (responses){
                            responses.notify();
                        }*/
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
