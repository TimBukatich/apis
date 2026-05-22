package by.bsu.computerfirm.service;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.Computer;
import by.bsu.computerfirm.entity.component.CompositeComponent;
import by.bsu.computerfirm.entity.component.ComputerComponent;
import by.bsu.computerfirm.entity.component.SimpleComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ComputerServiceTest {

    private static final double DELTA = 0.0001d;

    private Computer buildSampleComputer() {
        CompositeComponent root = new CompositeComponent("PC", ComponentType.COMPUTER, 0d);
        root.add(new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel"));
        root.add(new SimpleComponent("Fury 16GB", ComponentType.RAM, 80d, "Kingston"));

        CompositeComponent storage = new CompositeComponent(
                "Storage Bay", ComponentType.STORAGE_GROUP, 0d);
        storage.add(new SimpleComponent("980 Pro 1TB", ComponentType.SSD, 130d, "Samsung"));
        storage.add(new SimpleComponent("Barracuda 2TB", ComponentType.HDD, 55d, "Seagate"));
        root.add(storage);

        return new Computer("BSU Pro", 2025, root);
    }

    @Test
    @DisplayName("calculateTotalPrice sums leaves recursively")
    void calculateTotalPriceSumsLeaves() {
        Computer computer = buildSampleComputer();
        double total = ComputerService.calculateTotalPrice(computer);

        assertEquals(320d + 80d + 130d + 55d, total, DELTA);
    }

    @Test
    @DisplayName("calculateTotalPrice returns 0 for null inputs")
    void calculateTotalPriceHandlesNull() {
        assertEquals(0d, ComputerService.calculateTotalPrice(null), DELTA);
    }

    @Test
    @DisplayName("findByType returns all matching leaves")
    void findByTypeReturnsMatches() {
        Computer computer = buildSampleComputer();
        List<ComputerComponent> ssds = ComputerService.findByType(computer, ComponentType.SSD);

        assertEquals(1, ssds.size());
        assertEquals("980 Pro 1TB", ssds.get(0).getName());
    }

    @Test
    @DisplayName("findByType returns empty when nothing matches")
    void findByTypeReturnsEmpty() {
        Computer computer = buildSampleComputer();
        List<ComputerComponent> gpus = ComputerService.findByType(computer, ComponentType.GPU);

        assertEquals(0, gpus.size());
    }

    @Test
    @DisplayName("countLeafComponents counts only leaves")
    void countLeafComponentsCountsLeaves() {
        Computer computer = buildSampleComputer();
        assertEquals(4, ComputerService.countLeafComponents(computer));
    }

    @Test
    @DisplayName("findMostExpensive returns the leaf with maximum price")
    void findMostExpensiveReturnsTopLeaf() {
        Computer computer = buildSampleComputer();
        ComputerComponent top = ComputerService.findMostExpensive(computer);

        assertNotNull(top);
        assertEquals("Core i7", top.getName());
    }

    @Test
    @DisplayName("findMostExpensive returns null for empty computer")
    void findMostExpensiveOnEmptyReturnsNull() {
        assertNull(ComputerService.findMostExpensive(null));
    }
}
