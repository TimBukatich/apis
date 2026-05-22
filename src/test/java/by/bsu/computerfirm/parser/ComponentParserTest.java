package by.bsu.computerfirm.parser;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.component.ComputerComponent;
import by.bsu.computerfirm.entity.component.SimpleComponent;
import by.bsu.computerfirm.exception.InvalidComponentDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComponentParserTest {

    private static final double DELTA = 0.0001d;

    @Test
    @DisplayName("Parses a well-formed line")
    void parsesWellFormedLine() throws InvalidComponentDataException {
        SimpleComponent component =
                ComponentParser.parseLine("CPU;Core i7-13700K;Intel;420.50");

        assertNotNull(component);
        assertEquals("Core i7-13700K", component.getName());
        assertEquals(ComponentType.CPU, component.getType());
        assertEquals(420.50d, component.getPrice(), DELTA);
        assertEquals("Intel", component.getManufacturer());
    }

    @Test
    @DisplayName("Trims whitespace around fields")
    void trimsWhitespace() throws InvalidComponentDataException {
        SimpleComponent component =
                ComponentParser.parseLine("  CPU ; Core i7 ; Intel ; 200.00 ");

        assertEquals("Core i7", component.getName());
        assertEquals("Intel", component.getManufacturer());
    }

    @Test
    @DisplayName("Rejects null line")
    void rejectsNullLine() {
        assertThrows(InvalidComponentDataException.class, () -> ComponentParser.parseLine(null));
    }

    @Test
    @DisplayName("Rejects empty line")
    void rejectsEmptyLine() {
        assertThrows(InvalidComponentDataException.class, () -> ComponentParser.parseLine("   "));
    }

    @Test
    @DisplayName("Rejects wrong field count")
    void rejectsWrongFieldCount() {
        assertThrows(InvalidComponentDataException.class,
                () -> ComponentParser.parseLine("CPU;Core;Intel"));
        assertThrows(InvalidComponentDataException.class,
                () -> ComponentParser.parseLine("CPU;Core;Intel;100;EXTRA"));
    }

    @Test
    @DisplayName("Rejects unknown component type")
    void rejectsUnknownType() {
        assertThrows(InvalidComponentDataException.class,
                () -> ComponentParser.parseLine("WRONG;Name;Vendor;100"));
    }

    @Test
    @DisplayName("Rejects empty name")
    void rejectsEmptyName() {
        assertThrows(InvalidComponentDataException.class,
                () -> ComponentParser.parseLine("CPU;;Intel;100"));
    }

    @Test
    @DisplayName("Rejects empty manufacturer")
    void rejectsEmptyManufacturer() {
        assertThrows(InvalidComponentDataException.class,
                () -> ComponentParser.parseLine("CPU;Core i7;;100"));
    }

    @Test
    @DisplayName("Rejects negative price")
    void rejectsNegativePrice() {
        assertThrows(InvalidComponentDataException.class,
                () -> ComponentParser.parseLine("CPU;Core i7;Intel;-1"));
    }

    @Test
    @DisplayName("Rejects price that is not a number")
    void rejectsNonNumericPrice() {
        assertThrows(InvalidComponentDataException.class,
                () -> ComponentParser.parseLine("CPU;Core i7;Intel;abc"));
    }

    @Test
    @DisplayName("parseLines skips invalid lines and keeps valid ones")
    void parseLinesSkipsInvalid() {
        List<String> input = Arrays.asList(
                "CPU;Core i7;Intel;100",
                "BAD;Line;",
                "RAM;Fury 16GB;Kingston;50",
                "",
                "CPU;Bad Price;Intel;-1",
                "GPU;RTX 4070;Nvidia;800");

        List<ComputerComponent> result = ComponentParser.parseLines(input);

        assertEquals(3, result.size());
        assertEquals(ComponentType.CPU, result.get(0).getType());
        assertEquals(ComponentType.RAM, result.get(1).getType());
        assertEquals(ComponentType.GPU, result.get(2).getType());
    }

    @Test
    @DisplayName("parseLines returns empty list for null input")
    void parseLinesHandlesNull() {
        assertTrue(ComponentParser.parseLines(null).isEmpty());
    }
}
