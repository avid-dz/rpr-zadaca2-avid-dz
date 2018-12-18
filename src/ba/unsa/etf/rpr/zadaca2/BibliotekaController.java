package ba.unsa.etf.rpr.zadaca2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class BibliotekaController {

    private BibliotekaModel model;
    private boolean validnaForma;
    public TextField knjigaAutor;
    public TextField knjigaNaslov;
    public TextField knjigaIsbn;
    public DatePicker knjigaDatum;

    public BibliotekaController(BibliotekaModel m) {
        model = m;
    }

    @FXML
    public void initialize() {
        knjigaAutor.textProperty().bindBidirectional(model.getTrenutnaKnjiga().autorProperty());
        knjigaNaslov.textProperty().bindBidirectional(model.getTrenutnaKnjiga().naslovProperty());
        knjigaIsbn.textProperty().bindBidirectional(model.getTrenutnaKnjiga().isbnProperty());
        knjigaDatum.valueProperty().bindBidirectional(model.getTrenutnaKnjiga().datumIzdanjaProperty());

        knjigaNaslov.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                if (validanNaslov(n)) {
                    knjigaNaslov.getStyleClass().removeAll("invalidField");
                    knjigaNaslov.getStyleClass().add("validField");
                    validnaForma = true;
                } else {
                    knjigaNaslov.getStyleClass().removeAll("validField");
                    knjigaNaslov.getStyleClass().add("invalidField");
                    validnaForma = false;
                }
            }
        });

        knjigaAutor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                if (validanAutor(n)) {
                    knjigaAutor.getStyleClass().removeAll("invalidField");
                    knjigaAutor.getStyleClass().add("validField");
                    validnaForma = true;
                } else {
                    knjigaAutor.getStyleClass().removeAll("validField");
                    knjigaAutor.getStyleClass().add("invalidField");
                    validnaForma = false;
                }
            }
        });

        knjigaIsbn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                if (validanIsbn(n)) {
                    knjigaIsbn.getStyleClass().removeAll("invalidField");
                    knjigaIsbn.getStyleClass().add("validField");
                    validnaForma = true;
                } else {
                    knjigaIsbn.getStyleClass().removeAll("validField");
                    knjigaIsbn.getStyleClass().add("invalidField");
                    validnaForma = false;
                }
            }
        });

        knjigaDatum.valueProperty().addListener((old, o, n) -> {
            if (validanDatumIzdanja(n)) {
                knjigaDatum.getStyleClass().removeAll("invalidField");
                knjigaDatum.getStyleClass().add("validField");
                validnaForma = true;
            } else {
                knjigaDatum.getStyleClass().removeAll("validField");
                knjigaDatum.getStyleClass().add("invalidField");
                validnaForma = false;
            }
        });
    }

    public void ispisiKnjige(ActionEvent actionEvent) {
        model.ispisiKnjige();
    }

    private boolean validanNaslov(String n) {
        if (n.trim().equals("")) return false;
        return true;
    }

    private boolean validanAutor(String n) {
        if (n.trim().equals("")) return false;
        return true;
    }

    private boolean validanIsbn(String n) {
        if (n.trim().equals("")) return false;
        return true;
    }

    private boolean validanDatumIzdanja(LocalDate n) {
        if (n.isAfter(LocalDate.now())) return false;
        return true;
    }
}
