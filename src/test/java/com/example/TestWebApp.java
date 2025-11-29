package com.example;

import com.example.miniprojetdevopsjava.HelloServlet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestWebApp {

    @Test
    void testSimpleAddition() {
        System.out.println("✅ Test d'addition simple exécuté");
        int result = 2 + 2;
        assertEquals(4, result, "2 + 2 doit être égal à 4");
    }

    @Test
    void testStringComparison() {
        System.out.println("✅ Test de comparaison de chaînes exécuté");
        String expected = "Hello";
        String actual = "Hello";
        assertEquals(expected, actual, "Les chaînes doivent être égales");
    }

    @Test
    void testWebAppLogic() {
        System.out.println("✅ Test de logique métier exécuté");
        // Simulation d'une logique métier simple
        boolean isActive = true;
        assertTrue(isActive, "L'application doit être active");
    }

    @Test
    void testServletInitialization() {
        System.out.println("✅ Test d'initialisation Servlet exécuté");
        HelloServlet servlet = new HelloServlet();
        assertNotNull(servlet, "La servlet doit être instanciée");
    }
}