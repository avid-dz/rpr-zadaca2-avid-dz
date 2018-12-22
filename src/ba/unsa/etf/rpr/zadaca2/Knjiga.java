package ba.unsa.etf.rpr.zadaca2;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Knjiga {
    private SimpleStringProperty autor;
    private SimpleStringProperty naslov;
    private SimpleStringProperty isbn;
    private SimpleIntegerProperty brojStranica;
    private ObjectProperty<LocalDate> datumIzdanja;

    public Knjiga() {
        autor = new SimpleStringProperty();
        naslov = new SimpleStringProperty();
        isbn = new SimpleStringProperty();
        brojStranica = new SimpleIntegerProperty();
        datumIzdanja = new SimpleObjectProperty<>();
    }

    public Knjiga(String a, String n, String i, int b) {
        autor = new SimpleStringProperty(a);
        naslov = new SimpleStringProperty(n);
        isbn = new SimpleStringProperty(i);
        brojStranica = new SimpleIntegerProperty(b);
        datumIzdanja = new SimpleObjectProperty<>(LocalDate.now());
    }

    public Knjiga(String a, String n, String i, int b, LocalDate d) {
        autor = new SimpleStringProperty(a);
        naslov = new SimpleStringProperty(n);
        isbn = new SimpleStringProperty(i);
        brojStranica = new SimpleIntegerProperty(b);
        datumIzdanja = new SimpleObjectProperty<>(d);
    }

    public String getIsbn() {
        return isbn.get();
    }

    public SimpleStringProperty isbnProperty() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }


    public String getNaslov() {
        return naslov.get();
    }

    public SimpleStringProperty naslovProperty() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov.set(naslov);
    }


    public String getAutor() {
        return autor.get();
    }

    public SimpleStringProperty autorProperty() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor.set(autor);
    }


    public int getBrojStranica() {
        return brojStranica.get();
    }

    public SimpleIntegerProperty brojStranicaProperty() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica.set(brojStranica);
    }


    public LocalDate getDatumIzdanja() {
        return datumIzdanja.get();
    }

    public ObjectProperty<LocalDate> datumIzdanjaProperty() {
        return datumIzdanja;
    }

    public void setDatumIzdanja(LocalDate datumIzdanja) {
        this.datumIzdanja.set(datumIzdanja);
    }


    @Override
    public String toString() {  // Podrska za ispisivanje karakteristika knjige
        return autor.get() + ", " + naslov.get() + ", " + isbn.get() + ", " + (brojStranica.get()) + ", "
                + DateTimeFormatter.ofPattern("dd. MM. yyyy").format(datumIzdanja.get());
    }
}
