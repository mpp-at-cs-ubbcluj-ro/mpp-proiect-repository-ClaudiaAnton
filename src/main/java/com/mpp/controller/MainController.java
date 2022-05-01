package com.mpp.controller;

import com.mpp.domain.Arbitru;
import com.mpp.domain.NumPct;
import com.mpp.domain.Participant;
import com.mpp.domain.Rezultat;
import com.mpp.observer.Observable;
import com.mpp.observer.Observer;
import com.mpp.repository.ParticipantRepo;
import com.mpp.repository.ProbaRepo;
import com.mpp.repository.RezultatRepo;
import com.mpp.service.ParticipantService;
import com.mpp.service.ProbaService;
import com.mpp.service.RezultatService;
import com.mpp.services.ChatException;
import com.mpp.services.IChatObserver;
import com.mpp.services.IChatServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;
import com.mpp.events.EventType;
import com.mpp.observer.Observer;

import java.io.FileReader;
import java.io.IOException;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import com.mpp.repository.ArbitruRepo;
import com.mpp.service.ArbitruService;
import javafx.stage.Stage;

import javax.swing.plaf.basic.BasicButtonUI;

//public class MainController implements Observer<EventType>, Initializable, IChatObserver {
public class MainController implements Initializable {

    private Arbitru arbitruObject;
    private IChatServices server;
    private ChatClientCtrl ctrl;
    private ParticipantService participantService;

    ObservableList<NumPct> modelFriendship = FXCollections.observableArrayList();
    @FXML
    private TableView<NumPct> table;
    @FXML
    private TableColumn<?, ?> numeTabel;
    @FXML
    private TableColumn<?, ?> punctajTabel;
    @FXML
    private Label arbitru;
    @FXML
    private Label proba;
    @FXML
    private TextField punctaj;
    @FXML
    private Button raportButtom;
    private Stage stage;


    public MainController() {
    }

    public void setServer(IChatServices s) {
        this.server = s;
    }

    //public void init(Long id, ArbitruService arbitruService, ParticipantService participantService, ProbaService probaService, RezultatService rezultatService) {
    public void init( ChatClientCtrl ctr) {
        this.ctrl=ctr;
//        this.arbitruService = arbitruService;
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (
                IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        ParticipantRepo participantRepo = new ParticipantRepo(props);
        this.participantService = new ParticipantService(participantRepo);
        var index=ctrl.getArbitruListModel().getSize();
        System.out.println(index);
        arbitruObject=(Arbitru) ctrl.getArbitruListModel().getElementAt(index-1);
        arbitru.setText(arbitruObject.getNume() + ": ");
        Long idProba = arbitruObject.getProba();
        //proba.setText(probaService.getProba(idProba).getNume());
        numeTabel.setCellValueFactory(new PropertyValueFactory<>("nume"));
        punctajTabel.setCellValueFactory(new PropertyValueFactory<>("punctaj"));
    }

    public void setTable() {
        table.getItems().clear();
        for (NumPct i : get())
            table.getItems().add(i);
    }

    private List<NumPct> sort() {
        List<NumPct> list = participantService.getListProba();
//        List<NumPct> list = (List<NumPct>) ctrl.getRezultatListModel();
        list.sort(new Comparator<NumPct>() {
            @Override
            public int compare(NumPct o1, NumPct o2) {
                return o1.getNume().compareTo(o2.getNume());
            }
        });
        return list;
    }

    private List<NumPct> get() {
        return sort();
    }

    public void addPoints() {
        NumPct nm = table.getSelectionModel().getSelectedItem();
        Rezultat rezultat = new Rezultat(arbitruObject.getProba(), nm.getId(), Long.parseLong(punctaj.getText()));
        //rezultatService.addRezultat(rezultat);
        try {
            server.sendPunctaj(rezultat);
            System.out.println("s-a trimis punctaj din main");
        } catch (ChatException e) {
            e.printStackTrace();
        }
        punctaj.clear();
    }

//    void logout() {
//        try {
//            server.logout(arbitruObject, this);
//        } catch (ChatException e) {
//            System.out.println("Logout error " + e);
//        }
//
//    }

    public void raport() {
        URL fxmlLoc = getClass().getClassLoader().getResource("raport.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLoc);
//                FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("mainPage.fxml"));
        stage = (Stage) raportButtom.getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        RaportController controller = fxmlLoader.getController();
        //controller.init(arbitruObject.getId(), arbitruService, participantService, probaService, rezultatService);
        stage.show();

    }

    public void logOutClick(MouseEvent event) {
        URL fxmlLoc = getClass().getClassLoader().getResource("login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLoc);
//                FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("mainPage.fxml"));
        stage = (Stage) raportButtom.getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        LoginController controller = fxmlLoader.getController();
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


//    public void messageReceived(Rezultat rezultat) throws ChatException {
//        Platform.runLater(() -> setTable());
//    }
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//    }


}
