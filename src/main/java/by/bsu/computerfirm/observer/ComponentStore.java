package by.bsu.computerfirm.observer;

import by.bsu.computerfirm.entity.component.ComputerComponent;
import by.bsu.computerfirm.exception.ComponentValidationException;
import by.bsu.computerfirm.validator.ComponentValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentStore implements ComponentSubject {

    private static final Logger LOGGER = LogManager.getLogger(ComponentStore.class);

    private final String storeName;
    private final List<ComponentObserver> observers;
    private final List<ComputerComponent> availableComponents;

    public ComponentStore(String storeName) {
        this.storeName = storeName;
        this.observers = new ArrayList<>();
        this.availableComponents = new ArrayList<>();
        LOGGER.debug("Store created: {}", storeName);
    }

    public String getStoreName() {
        return storeName;
    }

    public List<ComputerComponent> getAvailableComponents() {
        return Collections.unmodifiableList(availableComponents);
    }

    public List<ComponentObserver> getObservers() {
        return Collections.unmodifiableList(observers);
    }

    @Override
    public void attach(ComponentObserver observer) {
        if (observer == null) {
            LOGGER.error("Cannot attach a null observer to store {}", storeName);
            throw new IllegalArgumentException("Observer must not be null");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
            LOGGER.info("Observer {} attached to store {}", observer.getObserverName(), storeName);
        }
    }

    @Override
    public void detach(ComponentObserver observer) {
        if (observers.remove(observer) && observer != null) {
            LOGGER.info("Observer {} detached from store {}",
                    observer.getObserverName(), storeName);
        }
    }

    @Override
    public void notifyObservers(ComputerComponent component) {
        if (component == null) {
            return;
        }
        LOGGER.debug("Notifying {} observers about component {}", observers.size(),
                component.getName());
        for (ComponentObserver observer : observers) {
            observer.onNewComponent(component);
        }
    }

    public void addNewComponent(ComputerComponent component) throws ComponentValidationException {
        if (!ComponentValidator.isValidComponent(component)) {
            String name = component == null ? "null" : component.getName();
            LOGGER.error("Rejected invalid component for store {}: {}", storeName, name);
            throw new ComponentValidationException(
                    "Cannot add invalid component to store: " + name);
        }
        availableComponents.add(component);
        LOGGER.info("Component {} added to store {}", component.getName(), storeName);
        notifyObservers(component);
    }
}
