package fr.baptistemasoud.vanillaframework;

import fr.baptistemasoud.vanillaframework.exception.DependenciesCycleException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    void shouldInstantiateTestClass() throws DependenciesCycleException {
        var output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        Main.main(new String[]{"fr.baptistemasoud.vanillaframework"});

        String allWrittenLines = output.toString();
        System.out.println(allWrittenLines);
        assertTrue(allWrittenLines.contains("LightBulb: Bulb turned on..."));
        assertTrue(allWrittenLines.contains("LightBulb: Bulb turned off..."));
    }
}