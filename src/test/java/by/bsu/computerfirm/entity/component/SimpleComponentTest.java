package by.bsu.computerfirm.entity.component;

import by.bsu.computerfirm.entity.ComponentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleComponentTest {

    private static final double DELTA = 0.0001d;

    @Test
    @DisplayName("Simple component stores all constructor arguments")
    void shouldStoreConstructorArguments() {
        SimpleComponent component =
                new SimpleComponent("Core i7", ComponentType.CPU, 320.50d, "Intel");

        assertEquals("Core i7", component.getName());
        assertEquals(ComponentType.CPU, component.getType());
        assertEquals(320.50d, component.getPrice(), DELTA);
        assertEquals("Intel", component.getManufacturer());
        assertFalse(component.isComposite());
    }

    @Test
    @DisplayName("Equal simple components produce equal hash codes")
    void equalComponentsHaveEqualHashCodes() {
        SimpleComponent a = new SimpleComponent("Core i7", ComponentType.CPU, 320.50d, "Intel");
        SimpleComponent b = new SimpleComponent("Core i7", ComponentType.CPU, 320.50d, "Intel");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("Different manufacturer breaks equality")
    void differentManufacturerBreaksEquality() {
        SimpleComponent a = new SimpleComponent("Core i7", ComponentType.CPU, 320.50d, "Intel");
        SimpleComponent b = new SimpleComponent("Core i7", ComponentType.CPU, 320.50d, "AMD");

        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("copy() returns an independent equal instance")
    void copyReturnsIndependentEqualInstance() {
        SimpleComponent original =
                new SimpleComponent("RTX 4070", ComponentType.GPU, 800.00d, "Nvidia");
        SimpleComponent clone = original.copy();

        assertEquals(original, clone);
        assertNotSame(original, clone);

        clone.setPrice(950.00d);
        assertNotEquals(original.getPrice(), clone.getPrice(), DELTA);
    }

    @Test
    @DisplayName("Leaf component rejects add() and remove()")
    void leafRejectsCompositeOperations() {
        SimpleComponent leaf =
                new SimpleComponent("RAM", ComponentType.RAM, 80.00d, "Kingston");
        SimpleComponent other =
                new SimpleComponent("CPU", ComponentType.CPU, 200.00d, "Intel");

        assertThrows(UnsupportedOperationException.class, () -> leaf.add(other));
        assertThrows(UnsupportedOperationException.class, () -> leaf.remove(other));
        assertTrue(leaf.getChildren().isEmpty());
    }

    @Test
    @DisplayName("toString contains class name and key fields")
    void toStringContainsKeyFields() {
        SimpleComponent component =
                new SimpleComponent("Core i7", ComponentType.CPU, 320.50d, "Intel");
        String representation = component.toString();

        assertTrue(representation.contains("SimpleComponent"));
        assertTrue(representation.contains("Core i7"));
        assertTrue(representation.contains("Intel"));
        assertTrue(representation.contains("CPU"));
    }

    @Test
    @DisplayName("equals returns false for null and unrelated types")
    void equalsRejectsForeignObjects() {
        SimpleComponent component =
                new SimpleComponent("Core i7", ComponentType.CPU, 320.50d, "Intel");

        assertNotEquals(null, component);
        assertNotEquals("Core i7", component);
    }
}
