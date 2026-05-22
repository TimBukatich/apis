package by.bsu.computerfirm.observer;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.component.SimpleComponent;
import by.bsu.computerfirm.exception.ComponentValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComponentStoreTest {

    @Test
    @DisplayName("Store notifies all attached observers")
    void storeNotifiesAllObservers() throws ComponentValidationException {
        ComponentStore store = new ComponentStore("BSU Store");
        Client first = new Client("First");
        Client second = new Client("Second");
        store.attach(first);
        store.attach(second);

        SimpleComponent newComponent =
                new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel");
        store.addNewComponent(newComponent);

        assertEquals(1, first.getReceivedNotifications().size());
        assertEquals(1, second.getReceivedNotifications().size());
        assertEquals(newComponent, first.getReceivedNotifications().get(0));
    }

    @Test
    @DisplayName("Detached observer no longer receives notifications")
    void detachedObserverStopsReceiving() throws ComponentValidationException {
        ComponentStore store = new ComponentStore("BSU Store");
        Client client = new Client("First");
        store.attach(client);
        store.detach(client);

        store.addNewComponent(
                new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel"));

        assertTrue(client.getReceivedNotifications().isEmpty());
    }

    @Test
    @DisplayName("Client only receives notifications for interested types")
    void clientFiltersByInterest() throws ComponentValidationException {
        ComponentStore store = new ComponentStore("BSU Store");
        Client gpuFan = new Client("GPU Fan", EnumSet.of(ComponentType.GPU));
        store.attach(gpuFan);

        store.addNewComponent(
                new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel"));
        store.addNewComponent(
                new SimpleComponent("RTX 4070", ComponentType.GPU, 800d, "Nvidia"));

        assertEquals(1, gpuFan.getReceivedNotifications().size());
        assertEquals(ComponentType.GPU,
                gpuFan.getReceivedNotifications().get(0).getType());
    }

    @Test
    @DisplayName("Store rejects null observer")
    void storeRejectsNullObserver() {
        ComponentStore store = new ComponentStore("BSU Store");
        assertThrows(IllegalArgumentException.class, () -> store.attach(null));
    }

    @Test
    @DisplayName("Store rejects invalid component data")
    void storeRejectsInvalidComponent() {
        ComponentStore store = new ComponentStore("BSU Store");
        SimpleComponent invalid =
                new SimpleComponent("", ComponentType.CPU, -10d, "Intel");

        assertThrows(ComponentValidationException.class, () -> store.addNewComponent(invalid));
    }

    @Test
    @DisplayName("Available components list is updated after addNewComponent")
    void availableListUpdated() throws ComponentValidationException {
        ComponentStore store = new ComponentStore("BSU Store");
        SimpleComponent component =
                new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel");

        store.addNewComponent(component);

        assertEquals(1, store.getAvailableComponents().size());
        assertEquals(component, store.getAvailableComponents().get(0));
    }

    @Test
    @DisplayName("Attaching same observer twice does not duplicate notifications")
    void attachingTwiceDoesNotDuplicate() throws ComponentValidationException {
        ComponentStore store = new ComponentStore("BSU Store");
        Client client = new Client("Once");
        store.attach(client);
        store.attach(client);

        store.addNewComponent(
                new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel"));

        assertEquals(1, client.getReceivedNotifications().size());
    }

    @Test
    @DisplayName("clearNotifications empties received list")
    void clearNotificationsEmptiesList() throws ComponentValidationException {
        ComponentStore store = new ComponentStore("BSU Store");
        Client client = new Client("Once");
        store.attach(client);
        store.addNewComponent(
                new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel"));

        client.clearNotifications();

        assertTrue(client.getReceivedNotifications().isEmpty());
    }
}
