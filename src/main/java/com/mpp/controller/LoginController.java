package com.mpp.controller;

import com.mpp.service.ParticipantService;
import com.mpp.service.ProbaService;
import com.mpp.service.RezultatService;
import com.mpp.services.ChatException;
import com.mpp.services.IChatObserver;
import com.mpp.services.IChatServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.mpp.service.ArbitruService;
import javafx.scene.control.TextField;


public class LoginController {
    private ChatClientCtrl ctrl;
//    private ArbitruService arbitruService;
//    private ParticipantService participantService;
//    private ProbaService probaService;
//    private RezultatService rezultatService;

    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Button login;
    private Stage stage;


    public LoginController() {
    }

    public void init(ChatClientCtrl ctrl){
        this.ctrl=ctrl;

    }

    public void init(ArbitruService arbitruService, ParticipantService participantService, ProbaService probaService, RezultatService rezultatService) {
//        this.arbitruService = arbitruService;
//        this.participantService = participantService;
//        this.probaService = probaService;
//        this.rezultatService = rezultatService;

    }


    public Stage loginMouseClick() {
//        var arb = arbitruService.getArbitruByUsername(username.getText());
//        var arb = server.getArbitru(username.getText());
//        if (arb == null) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setContentText("The account doesn't exist");
//            alert.showAndWait();
//        } else if (password.getText() == arb.getParola()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setContentText("Wrong password!");
//            alert.showAndWait();
//        } else {
            try {

                ctrl.login(username.getText(),password.getText());

                FXMLLoader cloader = new FXMLLoader(getClass().getClassLoader().getResource("mainPage.fxml"));
                Parent croot = cloader.load();
                MainController chatCtrl = cloader.getController();

                Stage stage = new Stage();
                stage.setScene(new Scene(croot));
                chatCtrl.init(ctrl);
                stage.show();
                chatCtrl.setTable();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        return null;
    }
}
