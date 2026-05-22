package by.bsu.computerfirm.validator;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.component.SimpleComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComponentValidatorTest {

    @Test
    @DisplayName("Valid names are accepted")
    void validNamesAccepted() {
        assertTrue(ComponentValidator.isValidName("Core i7-13700K"));
        assertTrue(ComponentValidator.isValidName("Fury 16GB DDR5"));
    }

    @Test
    @DisplayName("Invalid names are rejected")
    void invalidNamesRejected() {
        assertFalse(ComponentValidator.isValidName(null));
        assertFalse(ComponentValidator.isValidName(""));
        assertFalse(ComponentValidator.isValidName("a"));
        assertFalse(ComponentValidator.isValidName("@@@!"));
    }

    @Test
    @DisplayName("Valid manufacturers are accepted")
    void validManufacturersAccepted() {
        assertTrue(ComponentValidator.isValidManufacturer("Intel"));
        assertTrue(ComponentValidator.isValidManufacturer("AMD"));
        assertTrue(ComponentValidator.isValidManufacturer("Western Digital"));
    }

    @Test
    @DisplayName("Invalid manufacturers are rejected")
    void invalidManufacturersRejected() {
        assertFalse(ComponentValidator.isValidManufacturer(null));
        assertFalse(ComponentValidator.isValidManufacturer(""));
        assertFalse(ComponentValidator.isValidManufacturer("X"));
        assertFalse(ComponentValidator.isValidManufacturer("@INVALID@"));
    }

    @Test
    @DisplayName("Valid prices are accepted")
    void validPricesAccepted() {
        assertTrue(ComponentValidator.isValidPrice(0.01));
        assertTrue(ComponentValidator.isValidPrice(420.50));
        assertTrue(ComponentValidator.isValidPrice(99_999.99));
    }

    @Test
    @DisplayName("Invalid prices are rejected")
    void invalidPricesRejected() {
        assertFalse(ComponentValidator.isValidPrice(0));
        assertFalse(ComponentValidator.isValidPrice(-1));
        assertFalse(ComponentValidator.isValidPrice(200_000));
        assertFalse(ComponentValidator.isValidPrice(Double.NaN));
        assertFalse(ComponentValidator.isValidPrice(Double.POSITIVE_INFINITY));
    }

    @Test
    @DisplayName("Valid types are accepted")
    void validTypesAccepted() {
        assertTrue(ComponentValidator.isValidType("CPU"));
        assertTrue(ComponentValidator.isValidType("gpu"));
        assertTrue(ComponentValidator.isValidType("Motherboard"));
    }

    @Test
    @DisplayName("Invalid types are rejected")
    void invalidTypesRejected() {
        assertFalse(ComponentValidator.isValidType(null));
        assertFalse(ComponentValidator.isValidType(""));
        assertFalse(ComponentValidator.isValidType("WRONG"));
    }

    @Test
    @DisplayName("isValidComponent checks entire entity")
    void isValidComponentChecksWholeEntity() {
        SimpleComponent good =
                new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel");
        SimpleComponent badName =
                new SimpleComponent("", ComponentType.CPU, 320d, "Intel");
        SimpleComponent badPrice =
                new SimpleComponent("Core i7", ComponentType.CPU, -10d, "Intel");

        assertTrue(ComponentValidator.isValidComponent(good));
        assertFalse(ComponentValidator.isValidComponent(badName));
        assertFalse(ComponentValidator.isValidComponent(badPrice));
        assertFalse(ComponentValidator.isValidComponent(null));
    }
}
