package by.bsu.computerfirm.builder;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.Computer;
import by.bsu.computerfirm.entity.component.CompositeComponent;
import by.bsu.computerfirm.entity.component.ComputerComponent;
import by.bsu.computerfirm.exception.ComponentValidationException;
import by.bsu.computerfirm.validator.ComponentValidator;
import by.bsu.computerfirm.validator.ComputerValidator;

import java.util.ArrayList;
import java.util.List;

public class ComputerBuilder {

    private static final String DEFAULT_ROOT_NAME = "Computer Assembly";
    private static final ComponentType DEFAULT_ROOT_TYPE = ComponentType.COMPUTER;

    private String model;
    private int productionYear;
    private String rootName;
    private ComponentType rootType;
    private final List<ComputerComponent> components;

    public ComputerBuilder() {
        this.rootName = DEFAULT_ROOT_NAME;
        this.rootType = DEFAULT_ROOT_TYPE;
        this.components = new ArrayList<>();
    }

    public ComputerBuilder withModel(String model) throws ComponentValidationException {
        if (!ComputerValidator.isValidModel(model)) {
            throw new ComponentValidationException("Invalid computer model: " + model);
        }
        this.model = model.trim();
        return this;
    }

    public ComputerBuilder withProductionYear(int year) throws ComponentValidationException {
        if (!ComputerValidator.isValidYear(year)) {
            throw new ComponentValidationException("Invalid production year: " + year);
        }
        this.productionYear = year;
        return this;
    }

    public ComputerBuilder withRootName(String name) throws ComponentValidationException {
        if (!ComponentValidator.isValidName(name)) {
            throw new ComponentValidationException("Invalid root component name: " + name);
        }
        this.rootName = name.trim();
        return this;
    }

    public ComputerBuilder withRootType(ComponentType type) throws ComponentValidationException {
        if (type == null) {
            throw new ComponentValidationException("Root type must not be null");
        }
        this.rootType = type;
        return this;
    }

    public ComputerBuilder addComponent(ComputerComponent component)
            throws ComponentValidationException {
        if (!ComponentValidator.isValidComponent(component)) {
            throw new ComponentValidationException(
                    "Invalid component: " + (component == null ? "null" : component.getName()));
        }
        this.components.add(component);
        return this;
    }

    public ComputerBuilder addComponents(List<? extends ComputerComponent> componentsToAdd)
            throws ComponentValidationException {
        if (componentsToAdd == null) {
            throw new ComponentValidationException("Component list must not be null");
        }
        for (ComputerComponent component : componentsToAdd) {
            addComponent(component);
        }
        return this;
    }

    public Computer build() throws ComponentValidationException {
        if (model == null) {
            throw new ComponentValidationException("Model is required to build a Computer");
        }
        if (productionYear == 0) {
            throw new ComponentValidationException(
                    "Production year is required to build a Computer");
        }
        double rootPrice = sumOfPrices(components);
        CompositeComponent root = new CompositeComponent(rootName, rootType, rootPrice);
        for (ComputerComponent component : components) {
            root.add(component);
        }
        return new Computer(model, productionYear, root);
    }

    public void reset() {
        this.model = null;
        this.productionYear = 0;
        this.rootName = DEFAULT_ROOT_NAME;
        this.rootType = DEFAULT_ROOT_TYPE;
        this.components.clear();
    }

    private static double sumOfPrices(List<ComputerComponent> components) {
        double total = 0.0d;
        for (ComputerComponent component : components) {
            total += component.getPrice();
        }
        return total;
    }
}
