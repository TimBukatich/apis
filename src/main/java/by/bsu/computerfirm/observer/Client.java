package by.bsu.computerfirm.observer;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.component.ComputerComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class Client implements ComponentObserver {

    private final String name;
    private final Set<ComponentType> interests;
    private final List<ComputerComponent> receivedNotifications;

    public Client(String name) {
        this.name = name;
        this.interests = EnumSet.allOf(ComponentType.class);
        this.receivedNotifications = new ArrayList<>();
    }

    public Client(String name, Set<ComponentType> interests) {
        this.name = name;
        this.interests = interests == null
                ? EnumSet.noneOf(ComponentType.class)
                : EnumSet.copyOf(interests);
        this.receivedNotifications = new ArrayList<>();
    }

    @Override
    public void onNewComponent(ComputerComponent component) {
        if (component == null) {
            return;
        }
        if (interests.contains(component.getType())) {
            receivedNotifications.add(component);
        }
    }

    @Override
    public String getObserverName() {
        return name;
    }

    public List<ComputerComponent> getReceivedNotifications() {
        return Collections.unmodifiableList(receivedNotifications);
    }

    public Set<ComponentType> getInterests() {
        return Collections.unmodifiableSet(interests);
    }

    public void clearNotifications() {
        receivedNotifications.clear();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Client that = (Client) other;
        if (name == null) {
            return that.name == null;
        }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Client{name='").append(name).append('\'');
        builder.append(", interests=").append(interests);
        builder.append(", receivedNotifications=").append(receivedNotifications.size());
        builder.append('}');
        return builder.toString();
    }
}
