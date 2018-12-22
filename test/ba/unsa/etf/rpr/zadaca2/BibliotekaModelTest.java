package ba.unsa.etf.rpr.zadaca2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BibliotekaModelTest {

    @Test
    void getTrenutnaKnjiga() {
        BibliotekaModel m = new BibliotekaModel();
        m.napuni();
        assertNull(m.getTrenutnaKnjiga());
    }

    @Test
    void dajKnjige() {
        BibliotekaModel m = new BibliotekaModel();
        m.napuni();
        assertTrue(m.dajKnjige().contains("Meša Selimović, Tvrđava"));
        assertTrue(m.dajKnjige().contains("Ivo Andrić, Travnička hronika"));
        assertTrue(m.dajKnjige().contains("J. K. Rowling, Harry Potter"));
    }

    @Test
    void setTrenutnaKnjiga() {
        BibliotekaModel m = new BibliotekaModel();
        m.napuni();
        Knjiga k = m.getKnjige().get(0);
        m.setTrenutnaKnjiga(k);
        assertEquals(k, m.getTrenutnaKnjiga());
    }

    @Test
    void deleteKnjiga() {
        BibliotekaModel m = new BibliotekaModel();
        m.napuni();
        Knjiga k = m.getKnjige().get(0);
        m.setTrenutnaKnjiga(k);
        m.deleteKnjiga();
        String expected = "Meša Selimović";
        assertFalse(m.dajKnjige().contains(expected));
    }

    @Test
    void addKnjiga1() {
        BibliotekaModel m = new BibliotekaModel();
        m.addKnjiga(new Knjiga("a", "a", "a", 1));
        m.addKnjiga(new Knjiga("b", "b", "b", 1));
        m.addKnjiga(new Knjiga("c", "c", "c", 1));
        assertTrue(m.dajKnjige().contains("a, a"));
        assertTrue(m.dajKnjige().contains("b, b"));
        assertTrue(m.dajKnjige().contains("c, c"));
    }

    @Test
    void testSetKnjige() {
        BibliotekaModel m = new BibliotekaModel();
        m.addKnjiga(new Knjiga("Autor 1", "Naslov 1", "abc", 10));
        m.addKnjiga(new Knjiga("Autor 2", "Naslov 2", "cba", 20));
        m.addKnjiga(new Knjiga("Autor 3", "Naslov 3", "acb", 30));
        ObservableList<Knjiga> o = FXCollections.observableArrayList();
        o.add(new Knjiga("a", "n", "i", 100, LocalDate.of(2001, 9, 9)));
        o.add(new Knjiga("a1", "n1", "i1", 200, LocalDate.of(2002, 5, 6)));
        m.setKnjige(o);
        assertAll("test setKnjige",
                () -> assertSame(o, m.getKnjige()),
                () -> assertEquals(2, m.getKnjige().size())
        );
    }

    @Test
    void testTrenutnaKnjigaProperty() {
        BibliotekaModel m = new BibliotekaModel();
        Knjiga k1 = new Knjiga("Autor 1", "Naslov 1", "abc", 10);
        Knjiga k2 = new Knjiga("Autor 2", "Naslov 2", "cba", 20);
        Knjiga k3 = new Knjiga("Autor 3", "Naslov 3", "acb", 30);
        m.addKnjiga(k1);
        m.addKnjiga(k2);
        m.addKnjiga(k3);
        m.setTrenutnaKnjiga(k2);
        assertSame(k2, m.trenutnaKnjigaProperty().get());
    }

    @Test
    void testIspisiKnjige() {
        BibliotekaModel m = new BibliotekaModel();
        Knjiga k1 = new Knjiga("Autor 1", "Naslov 1", "abc", 10,
                LocalDate.of(2015, 9, 8));
        Knjiga k2 = new Knjiga("Autor 2", "Naslov 2", "cba", 20,
                LocalDate.of(2016, 5, 6));
        Knjiga k3 = new Knjiga("Autor 3", "Naslov 3", "acb", 30,
                LocalDate.of(2014, 7, 6));
        m.addKnjiga(k1);
        m.addKnjiga(k2);
        m.addKnjiga(k3);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        m.ispisiKnjige();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        String ispisano = byteArrayOutputStream.toString(); // Pročitamo sa standardnog izlaza
        assertAll("test ispisa knjiga",
                () -> assertTrue(ispisano.contains("Knjige su:")),
                () -> assertTrue(ispisano.contains("Autor 1, Naslov 1, abc, 10, 08. 09. 2015")),
                () -> assertTrue(ispisano.contains("Autor 2, Naslov 2, cba, 20, 06. 05. 2016")),
                () -> assertTrue(ispisano.contains("Autor 3, Naslov 3, acb, 30, 06. 07. 2014"))
        );
        byteArrayOutputStream.reset();
    }
}