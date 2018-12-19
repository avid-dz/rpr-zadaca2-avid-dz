package ba.unsa.etf.rpr.zadaca2;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDate;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {

    private BibliotekaModel bibliotekaModel;
    public TableView tabelaKnjiga;
    public Label statusMsg;
    private SimpleStringProperty tekstStatusa;
    private BibliotekaController bibliotekaController;

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
        bibliotekaController = null;
    }

    @FXML
    public void initialize() {
        statusMsg.textProperty().bind(tekstStatusa);
        setTekstStatusa("Program pokrenut.");
        tabelaKnjiga.setEditable(true);
        tabelaKnjiga.setItems(bibliotekaModel.getKnjige());
        tabelaKnjiga.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Knjiga>() {
            @Override
            public void changed(ObservableValue<? extends Knjiga> observableValue, Knjiga o, Knjiga n) {
                bibliotekaModel.setTrenutnaKnjiga(n);
            }
        });
        tabelaKnjiga.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                changeEvent();
            }
        });
    }

    public void doSave(File file) {

    }

    public void doOpen(File file) {

    }

    public void openEvent() {

    }

    public void saveEvent() {

    }

    public void printEvent() {
        setTekstStatusa("Štampam knjige na standardni izlaz.");
        bibliotekaModel.ispisiKnjige();
    }

    public void exitEvent() {
        Platform.exit();
    }

    public void addEvent() {
        TableView.TableViewSelectionModel tableViewSelectionModel = tabelaKnjiga.getSelectionModel();
        Knjiga nova = new Knjiga("", "", "", 0);
        nova.setDatumIzdanja(null);
        bibliotekaModel.addKnjiga(nova);
        bibliotekaModel.setTrenutnaKnjiga(nova);
        Stage noviStage = null;
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource("biblioteka.fxml"));
            bibliotekaController = new BibliotekaController(bibliotekaModel);
            loader.setController(bibliotekaController);
            Parent root = loader.load();
            noviStage = new Stage();
            noviStage.setResizable(false);
            noviStage.setTitle("Dodavanje nove knjige");
            noviStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            noviStage.show();
            setTekstStatusa("Dodajem novu knjigu.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (noviStage == null) return;
        noviStage.setOnCloseRequest(event -> {
            if (bibliotekaController.validnaForma()) {
                tabelaKnjiga.getSelectionModel().select(bibliotekaModel.getTrenutnaKnjiga());
                setTekstStatusa("Knjiga dodana.");
            }
            else {
                bibliotekaModel.deleteKnjiga();
                setTekstStatusa("Knjiga nije dodana.");
                tabelaKnjiga.refresh();
                tabelaKnjiga.setSelectionModel(tableViewSelectionModel);
            }
        });
    }

    public void changeEvent() {
        if (bibliotekaModel.getTrenutnaKnjiga() == null) return;
        Knjiga trenutna = bibliotekaModel.getTrenutnaKnjiga();
        Knjiga knjigaSaStarimKarakteristikama =
                new Knjiga(trenutna.getAutor(), trenutna.getNaslov(), trenutna.getIsbn(), trenutna.getBrojStranica());
        knjigaSaStarimKarakteristikama.setDatumIzdanja(trenutna.getDatumIzdanja());
        Stage noviStage = null;
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource("biblioteka.fxml"));
            bibliotekaController = new BibliotekaController(bibliotekaModel);
            loader.setController(bibliotekaController);
            Parent root = loader.load();
            noviStage = new Stage();
            noviStage.setResizable(false);
            noviStage.setTitle("Izmjena knjige");
            noviStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            noviStage.show();
            setTekstStatusa("Mijenjam knjigu.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (noviStage == null) return;
        noviStage.setOnCloseRequest(event -> {
            if (bibliotekaController.validnaForma()) {
                setTekstStatusa("Knjiga izmijenjena.");
            }
            else {
                trenutna.setAutor(knjigaSaStarimKarakteristikama.getAutor());
                trenutna.setNaslov(knjigaSaStarimKarakteristikama.getNaslov());
                trenutna.setIsbn(knjigaSaStarimKarakteristikama.getIsbn());
                trenutna.setBrojStranica(knjigaSaStarimKarakteristikama.getBrojStranica());
                trenutna.setDatumIzdanja(knjigaSaStarimKarakteristikama.getDatumIzdanja());
                setTekstStatusa("Knjiga nije izmijenjena.");
            }
        });
    }

    public void deleteEvent() {
        if (bibliotekaModel.getTrenutnaKnjiga() == null) return;
        setTekstStatusa("Brišem knjigu.");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Brisanje knjige");
        alert.setHeaderText("Da li ste sigurni da želite obrisati trenutnu knjigu?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                bibliotekaModel.deleteKnjiga();
                setTekstStatusa("Knjiga obrisana.");
                tabelaKnjiga.refresh();
                tabelaKnjiga.getSelectionModel().clearSelection();
            }
            else if (response == ButtonType.CANCEL) {
                alert.close();
                setTekstStatusa("Knjiga nije obrisana.");
            }
        });
    }

    public void aboutEvent() {
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
