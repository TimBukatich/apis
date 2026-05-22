package by.bsu.computerfirm.builder;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.Computer;
import by.bsu.computerfirm.entity.component.CompositeComponent;
import by.bsu.computerfirm.entity.component.ComputerComponent;
import by.bsu.computerfirm.exception.ComponentValidationException;
import by.bsu.computerfirm.validator.ComponentValidator;
import by.bsu.computerfirm.validator.ComputerValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ComputerBuilder {

    private static final Logger LOGGER = LogManager.getLogger(ComputerBuilder.class);

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
            LOGGER.error("Invalid computer model: {}", model);
            throw new ComponentValidationException("Invalid computer model: " + model);
        }
        this.model = model.trim();
        LOGGER.debug("Model set to {}", this.model);
        return this;
    }

    public ComputerBuilder withProductionYear(int year) throws ComponentValidationException {
        if (!ComputerValidator.isValidYear(year)) {
            LOGGER.error("Invalid production year: {}", year);
            throw new ComponentValidationException("Invalid production year: " + year);
        }
        this.productionYear = year;
        LOGGER.debug("Production year set to {}", year);
        return this;
    }

    public ComputerBuilder withRootName(String name) throws ComponentValidationException {
        if (!ComponentValidator.isValidName(name)) {
            LOGGER.error("Invalid root component name: {}", name);
            throw new ComponentValidationException("Invalid root component name: " + name);
        }
        this.rootName = name.trim();
        return this;
    }

    public ComputerBuilder withRootType(ComponentType type) throws ComponentValidationException {
        if (type == null) {
            LOGGER.error("Root type must not be null");
            throw new ComponentValidationException("Root type must not be null");
        }
        this.rootType = type;
        return this;
    }

    public ComputerBuilder addComponent(ComputerComponent component)
            throws ComponentValidationException {
        if (!ComponentValidator.isValidComponent(component)) {
            String name = component == null ? "null" : component.getName();
            LOGGER.error("Invalid component rejected by builder: {}", name);
            throw new ComponentValidationException("Invalid component: " + name);
        }
        this.components.add(component);
        LOGGER.debug("Component added to builder: {}", component);
        return this;
    }

    public ComputerBuilder addComponents(List<? extends ComputerComponent> componentsToAdd)
            throws ComponentValidationException {
        if (componentsToAdd == null) {
            LOGGER.error("Component list must not be null");
            throw new ComponentValidationException("Component list must not be null");
        }
        for (ComputerComponent component : componentsToAdd) {
            addComponent(component);
        }
        return this;
    }

    public Computer build() throws ComponentValidationException {
        if (model == null) {
            LOGGER.error("Build attempted without model");
            throw new ComponentValidationException("Model is required to build a Computer");
        }
        if (productionYear == 0) {
            LOGGER.error("Build attempted without production year");
            throw new ComponentValidationException(
                    "Production year is required to build a Computer");
        }
        double rootPrice = sumOfPrices(components);
        CompositeComponent root = new CompositeComponent(rootName, rootType, rootPrice);
        for (ComputerComponent component : components) {
            root.add(component);
        }
        Computer computer = new Computer(model, productionYear, root);
        LOGGER.info("Computer built: model={}, year={}, components={}",
                model, productionYear, components.size());
        return computer;
    }

    public void reset() {
        this.model = null;
        this.productionYear = 0;
        this.rootName = DEFAULT_ROOT_NAME;
        this.rootType = DEFAULT_ROOT_TYPE;
        this.components.clear();
        LOGGER.debug("Builder reset");
    }

    private static double sumOfPrices(List<ComputerComponent> components) {
        double total = 0.0d;
        for (ComputerComponent component : components) {
            total += component.getPrice();
        }
        return total;
    }
}
