package ba.unsa.etf.rpr.zadaca2;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
    private FormularController formularController;

    private void setTekstStatusa(String tekstStatusa) {
        this.tekstStatusa.set(tekstStatusa);
    }

    public GlavnaController(BibliotekaModel bibliotekaModel) {
        this.bibliotekaModel = bibliotekaModel;
        tekstStatusa = new SimpleStringProperty("");
        formularController = null;
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
        if (file == null) return;
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
        if (file == null) return;
        ObservableList<Knjiga> novaListaKnjiga = FXCollections.observableArrayList();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            Element korijenskiElement = document.getDocumentElement();
            NodeList listaKnjiga = korijenskiElement.getChildNodes();
            int brojKnjiga = listaKnjiga.getLength();
            for (int i = 0; i < brojKnjiga; i++) {
                Node dijeteKnjiga = listaKnjiga.item(i);
                if (dijeteKnjiga instanceof Element) {
                    Element knjigaElement = (Element) dijeteKnjiga;
                    if (!knjigaElement.getTagName().equals("knjiga")) {
                        prikazProzoraZaGresku();
                        return;
                    }
                    if (!knjigaElement.hasAttribute("brojStranica")) {
                        prikazProzoraZaGresku();
                        return;
                    }
                    if (knjigaElement.getAttributes().getLength() != 1) {
                        prikazProzoraZaGresku();
                        return;
                    }
                    Knjiga knjiga = new Knjiga();
                    String brojStanicaKnjige = knjigaElement.getAttribute("brojStranica");
                    knjiga.setBrojStranica(Integer.parseInt(brojStanicaKnjige));
                    NodeList listaDjeceOdKnjige = knjigaElement.getChildNodes();
                    int brojDjeceOdKnjige = listaDjeceOdKnjige.getLength();
                    if (brojDjeceOdKnjige != 4) {
                        prikazProzoraZaGresku();
                        return;
                    }
                    boolean autorPronadjen = false;
                    boolean naslovPronadjen = false;
                    boolean isbnPronadjen = false;
                    boolean datumPronadjen = false;
                    for (int j = 0; j < brojDjeceOdKnjige; j++) {
                        Node dijeteOdKnjige = listaDjeceOdKnjige.item(j);
                        if (dijeteOdKnjige instanceof Element) {
                            Element dijeteOdKnjigeElement = (Element) dijeteOdKnjige;
                            if (dijeteOdKnjigeElement.getAttributes().getLength() != 0) {
                                prikazProzoraZaGresku();
                                return;
                            }
                            if (dijeteOdKnjigeElement.getTagName().equals("autor")) {
                                knjiga.setAutor(dijeteOdKnjigeElement.getTextContent());
                                autorPronadjen = true;
                            }
                            else if (dijeteOdKnjigeElement.getTagName().equals("naslov")) {
                                knjiga.setNaslov(dijeteOdKnjigeElement.getTextContent());
                                naslovPronadjen = true;
                            }
                            else if (dijeteOdKnjigeElement.getTagName().equals("isbn")) {
                                knjiga.setIsbn(dijeteOdKnjigeElement.getTextContent());
                                isbnPronadjen = true;
                            }
                            else if (dijeteOdKnjigeElement.getTagName().equals("datum")) {
                                knjiga.setDatumIzdanja(LocalDate.parse(dijeteOdKnjigeElement.getTextContent(),
                                        DateTimeFormatter.ofPattern("dd. MM. yyyy")));
                                datumPronadjen = true;
                            }
                        }
                    }
                    if (!autorPronadjen || !naslovPronadjen || !isbnPronadjen || !datumPronadjen) {
                        prikazProzoraZaGresku();
                        return;
                    }
                    novaListaKnjiga.add(knjiga);
                }
            }
        } catch (Exception e) {
            prikazProzoraZaGresku();
            return;
        }
        bibliotekaModel.setKnjige(novaListaKnjiga);
        tabelaKnjiga.setItems(novaListaKnjiga);
        tabelaKnjiga.getSelectionModel().clearSelection();
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
        Knjiga prethodnoSelektovanaKnjiga = bibliotekaModel.getTrenutnaKnjiga();
        Knjiga nova = new Knjiga("", "", "", 0);
        bibliotekaModel.addKnjiga(nova);
        bibliotekaModel.setTrenutnaKnjiga(nova);
        Stage noviStage = null;
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource("formular.fxml"));
            formularController = new FormularController(bibliotekaModel);
            loader.setController(formularController);
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
            if (formularController.validnaForma()) {
                tabelaKnjiga.getSelectionModel().select(bibliotekaModel.getTrenutnaKnjiga());
                setTekstStatusa("Knjiga dodana.");
            }
            else {
                bibliotekaModel.deleteKnjiga();
                setTekstStatusa("Knjiga nije dodana.");
                tabelaKnjiga.refresh();
                bibliotekaModel.setTrenutnaKnjiga(prethodnoSelektovanaKnjiga);
                tabelaKnjiga.getSelectionModel().select(prethodnoSelektovanaKnjiga);
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
                new Knjiga(trenutna.getAutor(), trenutna.getNaslov(), trenutna.getIsbn(),
                        trenutna.getBrojStranica(), trenutna.getDatumIzdanja());
        Stage noviStage = null;
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource("formular.fxml"));
            formularController = new FormularController(bibliotekaModel);
            loader.setController(formularController);
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
            if (formularController.validnaForma()) {
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

    private void prikazProzoraZaGresku() {
        Alert greska = new Alert(Alert.AlertType.ERROR);
        greska.setTitle("Greška");
        greska.setHeaderText("Neispravan format datoteke");
        greska.setContentText("Provjerite format datoteke ili probajte sa drugom datotekom.");
        greska.show();
    }
}
