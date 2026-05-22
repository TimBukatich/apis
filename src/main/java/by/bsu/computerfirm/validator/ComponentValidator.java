package by.bsu.computerfirm.validator;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.component.ComputerComponent;

public final class ComponentValidator {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 60;
    private static final int MIN_MANUFACTURER_LENGTH = 2;
    private static final int MAX_MANUFACTURER_LENGTH = 40;
    private static final double MIN_PRICE = 0.01d;
    private static final double MAX_PRICE = 100_000.00d;
    private static final String NAME_REGEX = "[A-Za-z0-9][A-Za-z0-9\\s\\-/.,()+]{1,59}";
    private static final String MANUFACTURER_REGEX = "[A-Za-z][A-Za-z0-9\\s\\-&.]{1,39}";

    private ComponentValidator() {
    }

    public static boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        String trimmed = name.trim();
        if (trimmed.length() < MIN_NAME_LENGTH || trimmed.length() > MAX_NAME_LENGTH) {
            return false;
        }
        return trimmed.matches(NAME_REGEX);
    }

    public static boolean isValidManufacturer(String manufacturer) {
        if (manufacturer == null) {
            return false;
        }
        String trimmed = manufacturer.trim();
        if (trimmed.length() < MIN_MANUFACTURER_LENGTH
                || trimmed.length() > MAX_MANUFACTURER_LENGTH) {
            return false;
        }
        return trimmed.matches(MANUFACTURER_REGEX);
    }

    public static boolean isValidPrice(double price) {
        if (Double.isNaN(price) || Double.isInfinite(price)) {
            return false;
        }
        return price >= MIN_PRICE && price <= MAX_PRICE;
    }

    public static boolean isValidType(String typeName) {
        if (typeName == null) {
            return false;
        }
        for (ComponentType type : ComponentType.values()) {
            if (type.name().equalsIgnoreCase(typeName.trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidComponent(ComputerComponent component) {
        if (component == null) {
            return false;
        }
        if (!isValidName(component.getName())) {
            return false;
        }
        if (component.getType() == null) {
            return false;
        }
        return isValidPrice(component.getPrice());
    }
}
