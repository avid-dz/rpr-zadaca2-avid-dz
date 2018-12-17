package ba.unsa.etf.rpr.zadaca2;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.File;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {

    private BibliotekaModel bibliotekaModel;
    public Label statusMsg;
    private SimpleStringProperty tekstStatusa;

    public String getTekstStatusa() {
        return tekstStatusa.get();
    }

    public SimpleStringProperty tekstStatusaProperty() {
        return tekstStatusa;
    }

    public void setTekstStatusa(String tekstStatusa) {
        this.tekstStatusa.set(tekstStatusa);
    }

    public GlavnaController(BibliotekaModel bibliotekaModel) {
        this.bibliotekaModel = bibliotekaModel;
        tekstStatusa = new SimpleStringProperty("");
    }

    @FXML
    public void initialize() {
        statusMsg.textProperty().bind(tekstStatusa);
        setTekstStatusa("Program pokrenut.");
    }

    public void doSave(File file) {

    }

    public void doOpen(File file) {

    }

    public void openEvent(ActionEvent actionEvent) {

    }

    public void saveEvent(ActionEvent actionEvent) {

    }

    public void printEvent(ActionEvent actionEvent) {
        bibliotekaModel.ispisiKnjige();
    }

    public void exitEvent(ActionEvent actionEvent) {

    }

    public void addEvent(ActionEvent actionEvent) {
        prikazFormulara();
    }

    public void changeEvent(ActionEvent actionEvent) {
        prikazFormulara();
    }

    public void deleteEvent(ActionEvent actionEvent) {
        ButtonType okButton = new ButtonType("Ok");
        ButtonType cancelButton = new ButtonType("Cancel");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", okButton, cancelButton);
        alert.setTitle("Brisanje knjige");
        alert.setHeaderText("Da li ste sigurni da želite obrisati trenutnu knjigu?");
        alert.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                bibliotekaModel.deleteKnjiga();
            }
            else if (response == cancelButton) {
                alert.close();
            }
        });
    }

    public void aboutEvent(ActionEvent actionEvent) {

    }

    public void prikazFormulara() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("biblioteka.fxml"));
            loader.setController(new BibliotekaController(bibliotekaModel));
            Parent root = loader.load();
            Stage noviStage = new Stage();
            noviStage.setResizable(false);
            noviStage.setTitle("Izmjena knjige");
            noviStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            noviStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
