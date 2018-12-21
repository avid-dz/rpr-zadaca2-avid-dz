package ba.unsa.etf.rpr.zadaca2;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class BibliotekaModel {
    private ObservableList<Knjiga> knjige = FXCollections.observableArrayList();
    private ObjectProperty<Knjiga> trenutnaKnjiga = new SimpleObjectProperty<>();

    public ObjectProperty<Knjiga> trenutnaKnjigaProperty() {
        return trenutnaKnjiga;
    }

    public Knjiga getTrenutnaKnjiga() {
        return trenutnaKnjiga.get();
    }

    public void setTrenutnaKnjiga(Knjiga k) {
        trenutnaKnjiga.set(k);
    }

    public ObservableList<Knjiga> getKnjige() {
        return knjige;
    }

    public void setKnjige(ObservableList<Knjiga> knjige) {
        this.knjige = knjige;
    }

    public void ispisiKnjige() {
        System.out.println("Knjige su:");
        for (Knjiga k : knjige)
            System.out.println(k);
    }

    void napuni() {
        knjige.add(new Knjiga("Meša Selimović", "Tvrđava", "abcd", 500));
        knjige.add(new Knjiga("Ivo Andrić", "Travnička hronika", "abcd", 500));
        knjige.add(new Knjiga("J. K. Rowling", "Harry Potter", "abcd", 500));
    }

    public String dajKnjige() {
        String ispis = "";
        for (Knjiga knjiga : knjige) {
            ispis += knjiga;
            ispis += "\n";
        }
        return ispis;
    }

    public void deleteKnjiga() {
        knjige.remove(getTrenutnaKnjiga());
        setTrenutnaKnjiga(null);
    }

    public void addKnjiga(Knjiga knjiga) {
        if (knjiga.getDatumIzdanja() == null) knjiga.setDatumIzdanja(LocalDate.now());
        knjige.add(knjiga);
    }
}
