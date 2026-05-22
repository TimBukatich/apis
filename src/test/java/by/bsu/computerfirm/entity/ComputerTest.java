package by.bsu.computerfirm.entity;

import by.bsu.computerfirm.entity.component.CompositeComponent;
import by.bsu.computerfirm.entity.component.SimpleComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComputerTest {

    private CompositeComponent buildSampleRoot() {
        CompositeComponent root = new CompositeComponent("Tower", ComponentType.COMPUTER, 0d);
        root.add(new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel"));
        root.add(new SimpleComponent("Fury 16GB", ComponentType.RAM, 80d, "Kingston"));
        return root;
    }

    @Test
    @DisplayName("Computer stores all data passed to the constructor")
    void computerStoresAllData() {
        CompositeComponent root = buildSampleRoot();
        Computer computer = new Computer("BSU Pro", 2025, root);

        assertEquals("BSU Pro", computer.getModel());
        assertEquals(2025, computer.getProductionYear());
        assertEquals(root, computer.getRootComponent());
    }

    @Test
    @DisplayName("Equal computers have equal hashCodes")
    void equalComputersHaveEqualHashCodes() {
        Computer a = new Computer("BSU Pro", 2025, buildSampleRoot());
        Computer b = new Computer("BSU Pro", 2025, buildSampleRoot());

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("Different model breaks equality")
    void differentModelBreaksEquality() {
        Computer a = new Computer("BSU Pro", 2025, buildSampleRoot());
        Computer b = new Computer("BSU Lite", 2025, buildSampleRoot());

        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("copy() returns deep independent clone")
    void copyReturnsDeepClone() {
        Computer original = new Computer("BSU Pro", 2025, buildSampleRoot());
        Computer clone = original.copy();

        assertEquals(original, clone);
        assertNotSame(original, clone);
        assertNotSame(original.getRootComponent(), clone.getRootComponent());

        clone.setModel("Cloned");
        assertNotEquals(original.getModel(), clone.getModel());
    }

    @Test
    @DisplayName("toString contains model and year")
    void toStringContainsModelAndYear() {
        Computer computer = new Computer("BSU Pro", 2025, buildSampleRoot());
        String representation = computer.toString();

        assertTrue(representation.contains("BSU Pro"));
        assertTrue(representation.contains("2025"));
    }

    @Test
    @DisplayName("copy() handles null root component")
    void copyHandlesNullRoot() {
        Computer original = new Computer("BSU Pro", 2025, null);
        Computer clone = original.copy();

        assertEquals(original, clone);
        assertEquals("BSU Pro", clone.getModel());
    }
}
