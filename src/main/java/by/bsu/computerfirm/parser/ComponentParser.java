package by.bsu.computerfirm.parser;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.component.ComputerComponent;
import by.bsu.computerfirm.entity.component.SimpleComponent;
import by.bsu.computerfirm.exception.InvalidComponentDataException;
import by.bsu.computerfirm.validator.ComponentValidator;

import java.util.ArrayList;
import java.util.List;

public final class ComponentParser {

    private static final String FIELD_SEPARATOR = ";";
    private static final int EXPECTED_FIELDS = 4;
    private static final int TYPE_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int MANUFACTURER_INDEX = 2;
    private static final int PRICE_INDEX = 3;

    private ComponentParser() {
    }

    public static SimpleComponent parseLine(String line) throws InvalidComponentDataException {
        if (line == null) {
            throw new InvalidComponentDataException("Line must not be null");
        }
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidComponentDataException("Line must not be empty");
        }
        String[] parts = trimmed.split(FIELD_SEPARATOR, -1);
        if (parts.length != EXPECTED_FIELDS) {
            throw new InvalidComponentDataException(
                    "Expected " + EXPECTED_FIELDS + " fields but got " + parts.length
                            + " in line: " + line);
        }

        String typeRaw = parts[TYPE_INDEX].trim();
        String nameRaw = parts[NAME_INDEX].trim();
        String manufacturerRaw = parts[MANUFACTURER_INDEX].trim();
        String priceRaw = parts[PRICE_INDEX].trim();

        if (!ComponentValidator.isValidType(typeRaw)) {
            throw new InvalidComponentDataException("Invalid component type: " + typeRaw);
        }
        ComponentType type = ComponentType.valueOf(typeRaw.toUpperCase());

        if (!ComponentValidator.isValidName(nameRaw)) {
            throw new InvalidComponentDataException("Invalid component name: " + nameRaw);
        }

        if (!ComponentValidator.isValidManufacturer(manufacturerRaw)) {
            throw new InvalidComponentDataException(
                    "Invalid manufacturer: " + manufacturerRaw);
        }

        double price;
        try {
            price = Double.parseDouble(priceRaw);
        } catch (NumberFormatException e) {
            throw new InvalidComponentDataException(
                    "Price is not a valid number: " + priceRaw, e);
        }
        if (!ComponentValidator.isValidPrice(price)) {
            throw new InvalidComponentDataException("Price is out of range: " + price);
        }

        return new SimpleComponent(nameRaw, type, price, manufacturerRaw);
    }

    public static List<ComputerComponent> parseLines(List<String> lines) {
        List<ComputerComponent> result = new ArrayList<>();
        if (lines == null) {
            return result;
        }
        for (String line : lines) {
            try {
                result.add(parseLine(line));
            } catch (InvalidComponentDataException e) {

            }
        }
        return result;
    }
}
