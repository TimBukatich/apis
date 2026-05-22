package by.bsu.computerfirm.observer;

import by.bsu.computerfirm.entity.component.ComputerComponent;

public interface ComponentSubject {

    void attach(ComponentObserver observer);

    void detach(ComponentObserver observer);

    void notifyObservers(ComputerComponent component);
}
