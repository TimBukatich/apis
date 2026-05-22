package by.bsu.computerfirm.validator;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.Computer;
import by.bsu.computerfirm.entity.component.ComputerComponent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ComputerValidator {

    private static final int MIN_YEAR = 1990;
    private static final int MAX_YEAR = 2100;
    private static final int MIN_MODEL_LENGTH = 2;
    private static final int MAX_MODEL_LENGTH = 60;
    private static final double EPSILON = 0.001d;

    private ComputerValidator() {
    }

    public static boolean isValidYear(int year) {
        return year >= MIN_YEAR && year <= MAX_YEAR;
    }

    public static boolean isValidModel(String model) {
        if (model == null) {
            return false;
        }
        String trimmed = model.trim();
        return trimmed.length() >= MIN_MODEL_LENGTH && trimmed.length() <= MAX_MODEL_LENGTH;
    }

    public static boolean isValidComputer(Computer computer) {
        if (computer == null) {
            return false;
        }
        if (!isValidModel(computer.getModel())) {
            return false;
        }
        if (!isValidYear(computer.getProductionYear())) {
            return false;
        }
        if (computer.getRootComponent() == null) {
            return false;
        }
        return hasMandatoryComponents(computer);
    }

    public static boolean hasMandatoryComponents(Computer computer) {
        if (computer == null || computer.getRootComponent() == null) {
            return false;
        }
        Set<ComponentType> presentTypes = new HashSet<>();
        collectTypes(computer.getRootComponent(), presentTypes);
        return presentTypes.contains(ComponentType.CPU)
                && presentTypes.contains(ComponentType.RAM)
                && presentTypes.contains(ComponentType.MOTHERBOARD)
                && presentTypes.contains(ComponentType.PSU);
    }

    public static boolean isPriceConsistent(Computer computer, double calculatedPrice) {
        if (computer == null || computer.getRootComponent() == null) {
            return false;
        }
        if (Double.isNaN(calculatedPrice) || Double.isInfinite(calculatedPrice)) {
            return false;
        }
        return calculatedPrice >= 0 && calculatedPrice < Double.MAX_VALUE - EPSILON;
    }

    private static void collectTypes(ComputerComponent component, Set<ComponentType> acc) {
        if (component == null) {
            return;
        }
        acc.add(component.getType());
        List<ComputerComponent> children = component.getChildren();
        for (ComputerComponent child : children) {
            collectTypes(child, acc);
        }
    }
}
