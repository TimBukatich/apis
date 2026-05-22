package by.bsu.computerfirm.observer;

import by.bsu.computerfirm.entity.component.ComputerComponent;
import by.bsu.computerfirm.exception.ComponentValidationException;
import by.bsu.computerfirm.validator.ComponentValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentStore implements ComponentSubject {

    private final String storeName;
    private final List<ComponentObserver> observers;
    private final List<ComputerComponent> availableComponents;

    public ComponentStore(String storeName) {
        this.storeName = storeName;
        this.observers = new ArrayList<>();
        this.availableComponents = new ArrayList<>();
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
            throw new IllegalArgumentException("Observer must not be null");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(ComponentObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ComputerComponent component) {
        if (component == null) {
            return;
        }
        for (ComponentObserver observer : observers) {
            observer.onNewComponent(component);
        }
    }

    public void addNewComponent(ComputerComponent component) throws ComponentValidationException {
        if (!ComponentValidator.isValidComponent(component)) {
            throw new ComponentValidationException(
                    "Cannot add invalid component to store: "
                            + (component == null ? "null" : component.getName()));
        }
        availableComponents.add(component);
        notifyObservers(component);
    }
}
