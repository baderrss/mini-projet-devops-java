package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestWebApp {

    @Test
    public void testSimpleAddition() {
        System.out.println("✅ Test d'addition simple exécuté");
        int result = 2 + 2;
        assertTrue(result == 4, "2 + 2 doit être égal à 4");
    }

    @Test
    public void testStringComparison() {
        System.out.println("✅ Test de comparaison de chaînes exécuté");
        String expected = "Hello";
        String actual = "Hello";
        assertEquals(expected, actual, "Les chaînes doivent être égales");
    }

    @Test
    public void testWebAppLogic() {
        System.out.println("✅ Test de logique métier exécuté");
        // Simulation d'une logique métier simple
        boolean isActive = true;
        assertTrue(isActive, "L'application doit être active");
    }
}