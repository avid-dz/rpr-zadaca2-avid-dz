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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Brisanje knjige");
        alert.setHeaderText("Da li ste sigurni da želite obrisati trenutnu knjigu?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                bibliotekaModel.deleteKnjiga();
            }
            else if (response == ButtonType.CANCEL) {
                alert.close();
            }
        });
    }

    public void aboutEvent(ActionEvent actionEvent) {
        prikazAboutProzora();
    }

    private void prikazFormulara() {
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

    private void prikazAboutProzora() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("about.fxml"));
            Parent root = loader.load();
            Stage noviStage = new Stage();
            noviStage.setResizable(false);
            noviStage.setTitle("About");
            noviStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            noviStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
