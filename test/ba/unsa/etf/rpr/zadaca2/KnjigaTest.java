package ba.unsa.etf.rpr.zadaca2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class KnjigaTest {
    @Test
    void getIsbn() {
        Knjiga k = new Knjiga("a","b","c",1);
        assertEquals("c", k.getIsbn());
    }

    @Test
    void getDatumIzdanja() {
        Knjiga k = new Knjiga("a","b","c",1);
        assertEquals(LocalDate.now(), k.getDatumIzdanja());
    }

    @Test
    void setDatumIzdanja() {
        Knjiga k = new Knjiga("a","b","c",1);
        k.setDatumIzdanja(LocalDate.of(2018, 11, 17));
        assertEquals(LocalDate.of(2018, 11, 17), k.getDatumIzdanja());
    }

    @Test
    void toStringTest() {
        Knjiga k = new Knjiga("a","b","c",1);
        k.setDatumIzdanja(LocalDate.of(2018, 11, 17));
        String result = "" + k;
        String expected = "a, b, c, 1, 17. 11. 2018";
        assertEquals(expected, result);
    }

    @Test
    void toStringTest1() {
        Knjiga k = new Knjiga("a","b","c",1);
        k.setDatumIzdanja(LocalDate.of(2018, 11, 17));
        String result = "" + k;
        String content = "a, b, c";
        assertTrue(result.contains(content));
    }

    @Test
    void testKonstruktoraBezParametara() {
        Knjiga k = new Knjiga();
        assertAll("konstruktor bez parametara",
                () -> assertNull(k.getAutor()),
                () -> assertNull(k.getNaslov()),
                () -> assertNull(k.getIsbn()),
                () -> assertEquals(0, k.getBrojStranica()),
                () -> assertNull(k.getDatumIzdanja()));
    }

    @Test
    void testKonstruktoraSaPetParametara() {
        Knjiga k = new Knjiga("Branko Ćopić", "Ježeva kućica", "abcde", 100,
                LocalDate.of(2008, 10, 10));
        assertAll("konstruktor sa pet parametara",
                () -> assertEquals("Branko Ćopić", k.getAutor()),
                () -> assertEquals("Ježeva kućica", k.getNaslov()),
                () -> assertEquals("abcde", k.getIsbn()),
                () -> assertEquals(100, k.getBrojStranica()),
                () -> assertEquals(LocalDate.of(2008, 10, 10), k.getDatumIzdanja())
        );
    }

    @Test
    void testIsbn() {
        Knjiga k = new Knjiga();
        k.setIsbn("abcgf");
        assertAll("test isbn",
                () -> assertEquals("abcgf", k.getIsbn()),
                () -> assertEquals("abcgf", k.isbnProperty().get())
        );
    }

    @Test
    void testNaslov() {
        Knjiga k = new Knjiga();
        k.setNaslov("Habetova koliba");
        assertAll("test naslova",
                () -> assertEquals("Habetova koliba", k.getNaslov()),
                () -> assertEquals("Habetova koliba", k.naslovProperty().get())
        );
    }

    @Test
    void testBrojStranica() {
        Knjiga k = new Knjiga();
        k.setBrojStranica(89);
        assertAll("test broja stranica",
                () -> assertEquals(89, k.getBrojStranica()),
                () -> assertEquals(89, k.brojStranicaProperty().get())
        );
    }

    @Test
    void testAutor() {
        Knjiga k = new Knjiga();
        k.setAutor("Bajruzin Hajro Planjac");
        assertAll("test autora",
                () -> assertEquals("Bajruzin Hajro Planjac", k.getAutor()),
                () -> assertEquals("Bajruzin Hajro Planjac", k.autorProperty().get())
        );
    }

    @Test
    void testDatumIzdanja() {
        Knjiga k = new Knjiga();
        k.setDatumIzdanja(LocalDate.of(2005, 5, 30));
        assertAll("test datuma izdanja",
                () -> assertEquals(LocalDate.of(2005, 5, 30), k.getDatumIzdanja()),
                () -> assertEquals(LocalDate.of(2005, 5, 30), k.datumIzdanjaProperty().get())
        );
    }
}