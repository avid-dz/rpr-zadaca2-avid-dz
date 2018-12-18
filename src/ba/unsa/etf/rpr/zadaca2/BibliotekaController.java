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
    public TextField knjigaAutor;
    private boolean validanAutorKnjige;
    public TextField knjigaNaslov;
    private boolean validanNaslovKnjige;
    public TextField knjigaIsbn;
    private boolean validanIsbnKnjige;
    public DatePicker knjigaDatum;
    private boolean validanDatumIzdanjaKnjige;

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
                    validanNaslovKnjige = true;
                } else {
                    knjigaNaslov.getStyleClass().removeAll("validField");
                    knjigaNaslov.getStyleClass().add("invalidField");
                    validanNaslovKnjige = false;
                }
            }
        });

        knjigaAutor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                if (validanAutor(n)) {
                    knjigaAutor.getStyleClass().removeAll("invalidField");
                    knjigaAutor.getStyleClass().add("validField");
                    validanAutorKnjige = true;
                } else {
                    knjigaAutor.getStyleClass().removeAll("validField");
                    knjigaAutor.getStyleClass().add("invalidField");
                    validanAutorKnjige = false;
                }
            }
        });

        knjigaIsbn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                if (validanIsbn(n)) {
                    knjigaIsbn.getStyleClass().removeAll("invalidField");
                    knjigaIsbn.getStyleClass().add("validField");
                    validanIsbnKnjige = true;
                } else {
                    knjigaIsbn.getStyleClass().removeAll("validField");
                    knjigaIsbn.getStyleClass().add("invalidField");
                    validanIsbnKnjige = false;
                }
            }
        });

        knjigaDatum.valueProperty().addListener((old, o, n) -> {
            if (validanDatumIzdanja(n)) {
                knjigaDatum.getStyleClass().removeAll("invalidField");
                knjigaDatum.getStyleClass().add("validField");
                validanDatumIzdanjaKnjige = true;
            } else {
                knjigaDatum.getStyleClass().removeAll("validField");
                knjigaDatum.getStyleClass().add("invalidField");
                validanDatumIzdanjaKnjige = false;
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

    private boolean validnaForma() {
        return validanAutorKnjige && validanNaslovKnjige && validanIsbnKnjige && validanDatumIzdanjaKnjige;
    }
}
