package com.mpp.controller;

import com.mpp.domain.Arbitru;
import com.mpp.domain.NumPct;
import com.mpp.repository.ArbitruRepo;
import com.mpp.repository.ParticipantRepo;
import com.mpp.service.ArbitruService;
import com.mpp.service.ParticipantService;
import com.mpp.service.ProbaService;
import com.mpp.service.RezultatService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class RaportController {
    private ArbitruService arbitruService;
    private ParticipantService participantService;
    private ProbaService probaService;
    private RezultatService rezultatService;
    private Arbitru arbitruObject;
    private ChatClientCtrl chatClientCtrl;

    ObservableList<NumPct> modelFriendship = FXCollections.observableArrayList();
    @FXML private TableView<NumPct> table;
    @FXML private TableColumn<?,?> numeTabel;
    @FXML private TableColumn<?,?> punctajTabel;
    @FXML private Button back;
    private Stage stage;


    public RaportController() {
    }

    public void init(Long id,ArbitruService arbitruService, ParticipantService participantService, ProbaService probaService, RezultatService rezultatService){
        this.arbitruService=arbitruService;
        this.participantService=participantService;
        this.probaService=probaService;
        this.rezultatService=rezultatService;
        this.arbitruObject=arbitruService.getArbitru(id);

        numeTabel.setCellValueFactory(new PropertyValueFactory<>("nume"));
        punctajTabel.setCellValueFactory(new PropertyValueFactory<>("punctaj"));
        modelFriendship.setAll(get());
        table.setItems(modelFriendship);
    }

    private List<NumPct> sort()
    {
        List<NumPct> list=participantService.getListProba();
        list.sort(new Comparator<NumPct>() {
            @Override
            public int compare(NumPct o1, NumPct o2) {
                return o2.getPunctaj().compareTo(o1.getPunctaj());
            }
        });
        return list;
    }

    private List<NumPct> get() {
        return participantService.getListProba();
    }

    public void backButton() {
        URL fxmlLoc = getClass().getClassLoader().getResource("mainPage.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLoc);
        stage = (Stage) back.getScene().getWindow();
        Scene scene=null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        MainController controller=fxmlLoader.getController();
        controller.init(chatClientCtrl);
        stage.show();
        controller.setTable();
    }
}
