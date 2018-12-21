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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {

    private BibliotekaModel bibliotekaModel;
    public TableView tabelaKnjiga;
    public TableColumn kolonaDatum;
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

        tabelaKnjiga.setRowFactory(tv -> {
            TableRow<Knjiga> redTabele = new TableRow<>();
            redTabele.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!redTabele.isEmpty())) {
                    promjenaKnjige();
                }
            });
            return redTabele;
        });

        kolonaDatum.setCellFactory(new Callback<TableColumn<Knjiga, LocalDate>, TableCell<Knjiga, LocalDate>>() {
            @Override
            public TableCell<Knjiga, LocalDate> call(TableColumn<Knjiga, LocalDate> param) {
                return new TableCell<Knjiga, LocalDate>() {
                    @Override
                    protected void updateItem(LocalDate datum, boolean prazan) {
                        super.updateItem(datum, prazan);
                        if (datum == null || prazan) {
                            setText(null);
                        }
                        else {
                            setText(DateTimeFormatter.ofPattern("dd. MM. yyyy").format(datum));
                        }
                    }
                };
            }
        });
    }

    public void doSave(File file) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element korijenskiElement = document.createElement("biblioteka");
            document.appendChild(korijenskiElement);
            for (Knjiga knjiga : bibliotekaModel.getKnjige()) {
                Element knjigaElement = document.createElement("knjiga");
                korijenskiElement.appendChild(knjigaElement);
                knjigaElement.setAttribute("brojStranica", Integer.toString(knjiga.getBrojStranica()));
                Element autorElement = document.createElement("autor");
                autorElement.appendChild(document.createTextNode(knjiga.getAutor()));
                knjigaElement.appendChild(autorElement);
                Element naslovElement = document.createElement("naslov");
                naslovElement.appendChild(document.createTextNode(knjiga.getNaslov()));
                knjigaElement.appendChild(naslovElement);
                Element isbnElement = document.createElement("isbn");
                isbnElement.appendChild(document.createTextNode(knjiga.getIsbn()));
                knjigaElement.appendChild(isbnElement);
                Element datumElement = document.createElement("datum");
                datumElement.appendChild(document.createTextNode(DateTimeFormatter.ofPattern("dd. MM. yyyy")
                        .format(knjiga.getDatumIzdanja())));
                knjigaElement.appendChild(datumElement);
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException pcException) {
            pcException.printStackTrace();
        } catch (TransformerException tException) {
            tException.printStackTrace();
        }
    }

    public void doOpen(File file) {

    }

    public void openEvent(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll
                (new FileChooser.ExtensionFilter("XML File", "*.xml"));
        File izabraniFajl = fileChooser.showOpenDialog(new Stage());
        doOpen(izabraniFajl);
    }

    public void saveEvent(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll
                (new FileChooser.ExtensionFilter("XML File", "*.xml"));
        File izabraniFajl = fileChooser.showSaveDialog(new Stage());
        doSave(izabraniFajl);
    }

    public void printEvent(ActionEvent actionEvent) {
        setTekstStatusa("Štampam knjige na standardni izlaz.");
        bibliotekaModel.ispisiKnjige();
    }

    public void exitEvent(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void addEvent(ActionEvent actionEvent) {
        TableView.TableViewSelectionModel tableViewSelectionModel = tabelaKnjiga.getSelectionModel();
        Knjiga nova = new Knjiga("", "", "", 0);
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

    public void changeEvent(ActionEvent actionEvent) { promjenaKnjige(); }

    public void deleteEvent(ActionEvent actionEvent) {
        if (bibliotekaModel.getTrenutnaKnjiga() == null) return;
        setTekstStatusa("Brišem knjigu.");
        Alert potvrda = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        potvrda.setTitle("Brisanje knjige");
        potvrda.setHeaderText("Da li ste sigurni da želite obrisati trenutnu knjigu?");
        potvrda.showAndWait().ifPresent(izborKorisnika -> {
            if (izborKorisnika == ButtonType.OK) {
                bibliotekaModel.deleteKnjiga();
                setTekstStatusa("Knjiga obrisana.");
                tabelaKnjiga.refresh();
                tabelaKnjiga.getSelectionModel().clearSelection();
            }
            else if (izborKorisnika == ButtonType.CANCEL) {
                potvrda.close();
                setTekstStatusa("Knjiga nije obrisana.");
            }
        });
    }

    public void aboutEvent(ActionEvent actionEvent) {
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

    public void promjenaKnjige() {
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
}
