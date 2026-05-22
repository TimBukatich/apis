package by.bsu.computerfirm.validator;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.Computer;
import by.bsu.computerfirm.entity.component.CompositeComponent;
import by.bsu.computerfirm.entity.component.SimpleComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComputerValidatorTest {

    private CompositeComponent buildCompleteRoot() {
        CompositeComponent root = new CompositeComponent("PC", ComponentType.COMPUTER, 0d);
        root.add(new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel"));
        root.add(new SimpleComponent("Fury 16GB", ComponentType.RAM, 80d, "Kingston"));
        root.add(new SimpleComponent("ROG Strix", ComponentType.MOTHERBOARD, 300d, "ASUS"));
        root.add(new SimpleComponent("RM850x", ComponentType.PSU, 140d, "Corsair"));
        return root;
    }

    @Test
    @DisplayName("Year inside the allowed range is valid")
    void yearInsideRangeIsValid() {
        assertTrue(ComputerValidator.isValidYear(2025));
        assertTrue(ComputerValidator.isValidYear(1990));
        assertTrue(ComputerValidator.isValidYear(2100));
    }

    @Test
    @DisplayName("Year outside range is invalid")
    void yearOutsideRangeIsInvalid() {
        assertFalse(ComputerValidator.isValidYear(1989));
        assertFalse(ComputerValidator.isValidYear(2101));
    }

    @Test
    @DisplayName("Valid models are accepted")
    void validModelsAccepted() {
        assertTrue(ComputerValidator.isValidModel("BSU"));
        assertTrue(ComputerValidator.isValidModel("BSU Pro Workstation"));
    }

    @Test
    @DisplayName("Invalid models are rejected")
    void invalidModelsRejected() {
        assertFalse(ComputerValidator.isValidModel(null));
        assertFalse(ComputerValidator.isValidModel(""));
        assertFalse(ComputerValidator.isValidModel("a"));
    }

    @Test
    @DisplayName("Valid Computer passes validation")
    void validComputerPasses() {
        Computer computer = new Computer("BSU Pro", 2025, buildCompleteRoot());
        assertTrue(ComputerValidator.isValidComputer(computer));
    }

    @Test
    @DisplayName("Computer without root component is invalid")
    void computerWithoutRootInvalid() {
        Computer computer = new Computer("BSU Pro", 2025, null);
        assertFalse(ComputerValidator.isValidComputer(computer));
    }

    @Test
    @DisplayName("Computer without mandatory components fails")
    void computerWithoutMandatoryComponentsInvalid() {
        CompositeComponent root = new CompositeComponent("PC", ComponentType.COMPUTER, 0d);
        root.add(new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel"));
        Computer computer = new Computer("BSU Pro", 2025, root);
        assertFalse(ComputerValidator.isValidComputer(computer));
    }

    @Test
    @DisplayName("Null Computer is invalid")
    void nullComputerInvalid() {
        assertFalse(ComputerValidator.isValidComputer(null));
    }

    @Test
    @DisplayName("Price consistency check rejects NaN and Infinity")
    void priceConsistencyRejectsInvalidNumbers() {
        Computer computer = new Computer("BSU Pro", 2025, buildCompleteRoot());
        assertFalse(ComputerValidator.isPriceConsistent(computer, Double.NaN));
        assertFalse(ComputerValidator.isPriceConsistent(computer, Double.POSITIVE_INFINITY));
        assertTrue(ComputerValidator.isPriceConsistent(computer, 840d));
    }
}
