package com.mpp;

import com.mpp.ProtoBuff.protobuffprotocol.ProtoChatProxy;
import com.mpp.controller.LoginController;
import com.mpp.network.ServicesObjectProxy;
import com.mpp.repository.ArbitruRepo;
import com.mpp.repository.ParticipantRepo;
import com.mpp.repository.ProbaRepo;
import com.mpp.repository.RezultatRepo;
import com.mpp.service.ArbitruService;
import com.mpp.service.ParticipantService;
import com.mpp.service.ProbaService;
import com.mpp.service.RezultatService;
import com.mpp.services.IChatServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Client extends Application {
    private static final int defaultChatPort=55555;
    private static final String defaultServer="localhost";
    public void start(Stage stage) throws IOException {
        Properties clientProps=new Properties();
        try {
            clientProps.load(Client.class.getResourceAsStream("/chatclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties "+e);
            return;
        }
        String serverIP=clientProps.getProperty("chat.server.host",defaultServer);
        int serverPort=defaultChatPort;
        try{
            serverPort=Integer.parseInt(clientProps.getProperty("chat.server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number "+ex.getMessage());
            System.out.println("Using default port: "+defaultChatPort);
        }
        System.out.println("Using server IP "+serverIP);
        System.out.println("Using server port "+serverPort);

        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (
                IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        ArbitruRepo arbitruRepo = new ArbitruRepo(props);
        ArbitruService arbitruService = new ArbitruService(arbitruRepo);

        ParticipantRepo participantRepo=new ParticipantRepo(props);
        ParticipantService participantService=new ParticipantService(participantRepo);

        ProbaRepo probaRepo=new ProbaRepo(props);
        ProbaService probaService=new ProbaService(probaRepo);

        RezultatRepo rezultatRepo=new RezultatRepo(props);
        RezultatService rezultatService=new RezultatService(rezultatRepo);

        IChatServices server=new ProtoChatProxy(serverIP, serverPort);


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        Parent root=loader.load();

        LoginController ctrl = loader.getController();
        //ctrl.init(arbitruService,participantService,probaService,rezultatService,server);

        stage.setTitle("MPP chat");
        stage.setScene(new Scene(root));
        stage.show();


    }
}
