package by.bsu.computerfirm.observer;

import by.bsu.computerfirm.entity.component.ComputerComponent;

public interface ComponentObserver {

    void onNewComponent(ComputerComponent component);

    String getObserverName();
}
