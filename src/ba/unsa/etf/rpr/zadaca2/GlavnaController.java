package ba.unsa.etf.rpr.zadaca2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {

    private BibliotekaModel bibliotekaModel;

    public GlavnaController(BibliotekaModel bibliotekaModel) {
        this.bibliotekaModel = bibliotekaModel;
    }

    public void doSave(File test) {

    }

    public void doOpen(File test) {

    }

    public void prikazFormulara(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("biblioteka.fxml"));
            loader.setController(new BibliotekaController(bibliotekaModel));
            Parent root = loader.load();
            Stage noviStage = new Stage();
            noviStage.setResizable(false);
            noviStage.setTitle("Biblioteka");
            noviStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            noviStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
