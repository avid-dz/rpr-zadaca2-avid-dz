package ba.unsa.etf.rpr.zadaca2;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    public Spinner<Integer> knjigaBrojStranica;
    private ObjectProperty<Integer> knjigaBrojStranicaObject;

    public BibliotekaController(BibliotekaModel m) {
        model = m;
        knjigaBrojStranicaObject = model.getTrenutnaKnjiga().brojStranicaProperty().asObject();
    }

    @FXML
    public void initialize() {
        knjigaAutor.textProperty().bindBidirectional(model.getTrenutnaKnjiga().autorProperty());
        knjigaNaslov.textProperty().bindBidirectional(model.getTrenutnaKnjiga().naslovProperty());
        knjigaIsbn.textProperty().bindBidirectional(model.getTrenutnaKnjiga().isbnProperty());
        knjigaDatum.valueProperty().bindBidirectional(model.getTrenutnaKnjiga().datumIzdanjaProperty());
        knjigaBrojStranica.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory
                (0, 10000, model.getTrenutnaKnjiga().getBrojStranica()));
        knjigaBrojStranica.getValueFactory().valueProperty().bindBidirectional(knjigaBrojStranicaObject);

        validacijaPoljaAutor(knjigaAutor.getText());
        validacijaPoljaNaslov(knjigaNaslov.getText());
        validacijaPoljaIsbn(knjigaIsbn.getText());
        validacijaDatumaIzdanja(knjigaDatum.getValue());

        knjigaNaslov.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                validacijaPoljaNaslov(n);
            }
        });

        knjigaAutor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                validacijaPoljaAutor(n);
            }
        });

        knjigaIsbn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                validacijaPoljaIsbn(n);
            }
        });

        knjigaDatum.valueProperty().addListener((old, o, n) -> {
            validacijaDatumaIzdanja(n);
        });

        knjigaDatum.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate datumZaPretvaranje) {
                if (datumZaPretvaranje != null) {
                    try {
                        return DateTimeFormatter.ofPattern("dd. MM. yyyy").format(datumZaPretvaranje);
                    } catch (DateTimeException dte) {
                        dte.printStackTrace();
                    }
                }
                knjigaDatum.getStyleClass().removeAll("validField");
                knjigaDatum.getStyleClass().add("invalidField");
                validanDatumIzdanjaKnjige = false;
                return "";
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, DateTimeFormatter.ofPattern("dd. MM. yyyy"));
                    } catch (DateTimeParseException dtpe) {
                        dtpe.printStackTrace();
                    }
                }
                knjigaDatum.getStyleClass().removeAll("validField");
                knjigaDatum.getStyleClass().add("invalidField");
                validanDatumIzdanjaKnjige = false;
                return null;
            }
        });
    }

    private boolean validanNaslov(String n) {
        return !n.trim().equals("");
    }

    private boolean validanAutor(String n) {
        return !n.trim().equals("");
    }

    private boolean validanIsbn(String n) {
        return !n.trim().equals("");
    }

    private boolean validanDatumIzdanja(LocalDate n) { return !n.isAfter(LocalDate.now()); }

    private void validacijaPoljaNaslov(String n) {
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

    private void validacijaPoljaAutor(String n) {
        if (validanAutor(n)) {
            knjigaAutor.getStyleClass().removeAll("invalidField");
            knjigaAutor.getStyleClass().add("validField");
            validanAutorKnjige = true;
        }
        else {
            knjigaAutor.getStyleClass().removeAll("validField");
            knjigaAutor.getStyleClass().add("invalidField");
            validanAutorKnjige = false;
        }
    }

    private void validacijaPoljaIsbn(String n) {
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

    private void validacijaDatumaIzdanja(LocalDate n) {
        if (validanDatumIzdanja(n)) {
            knjigaDatum.getStyleClass().removeAll("invalidField");
            knjigaDatum.getStyleClass().add("validField");
            validanDatumIzdanjaKnjige = true;
        } else {
            knjigaDatum.getStyleClass().removeAll("validField");
            knjigaDatum.getStyleClass().add("invalidField");
            validanDatumIzdanjaKnjige = false;
        }
    }

    public boolean validnaForma() {
        return validanAutorKnjige && validanNaslovKnjige && validanIsbnKnjige && validanDatumIzdanjaKnjige;
    }
}
