package ba.unsa.etf.rpr.zadaca2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class BibliotekaController {

    private BibliotekaModel model;

    public TextField knjigaAutor;
    public TextField knjigaNaslov;
    public TextField knjigaIsbn;
    public DatePicker knjigaDatum;

    public BibliotekaController(BibliotekaModel m) {
        model = m;
    }

    @FXML
    public void initialize() {
        /*model.trenutnaKnjigaProperty().addListener((obs, oldKnjiga, newKnjiga) -> {
            System.out.print("Mijenjam data binding");
            if (oldKnjiga != null) {
                System.out.print(" sa "+oldKnjiga);
                knjigaAutor.textProperty().unbindBidirectional(oldKnjiga.autorProperty());
                knjigaNaslov.textProperty().unbindBidirectional(oldKnjiga.naslovProperty());
                knjigaIsbn.textProperty().unbindBidirectional(oldKnjiga.isbnProperty());
                knjigaDatum.valueProperty().unbindBidirectional(oldKnjiga.datumIzdanjaProperty());
            }
            if (newKnjiga == null) {
                System.out.println(" na ništa");
                knjigaAutor.setText("");
                knjigaNaslov.setText("");
                knjigaIsbn.setText("");
            }
            else {
                System.out.println(" na " + newKnjiga);
                knjigaAutor.textProperty().bindBidirectional(newKnjiga.autorProperty());
                knjigaNaslov.textProperty().bindBidirectional(newKnjiga.naslovProperty());
                knjigaIsbn.textProperty().bindBidirectional(newKnjiga.isbnProperty());
                knjigaDatum.valueProperty().bindBidirectional(newKnjiga.datumIzdanjaProperty());
            }
        });*/
        knjigaAutor.textProperty().bindBidirectional(model.getTrenutnaKnjiga().autorProperty());
        knjigaNaslov.textProperty().bindBidirectional(model.getTrenutnaKnjiga().naslovProperty());
        knjigaIsbn.textProperty().bindBidirectional(model.getTrenutnaKnjiga().isbnProperty());
        knjigaDatum.valueProperty().bindBidirectional(model.getTrenutnaKnjiga().datumIzdanjaProperty());
    }

    public void ispisiKnjige(ActionEvent actionEvent) {
        model.ispisiKnjige();
    }
}
