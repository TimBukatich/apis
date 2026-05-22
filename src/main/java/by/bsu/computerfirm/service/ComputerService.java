package by.bsu.computerfirm.service;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.Computer;
import by.bsu.computerfirm.entity.component.ComputerComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public final class ComputerService {

    private static final Logger LOGGER = LogManager.getLogger(ComputerService.class);

    private ComputerService() {
    }

    public static double calculateTotalPrice(Computer computer) {
        if (computer == null || computer.getRootComponent() == null) {
            LOGGER.warn("calculateTotalPrice called with empty computer");
            return 0.0d;
        }
        double total = calculateComponentPrice(computer.getRootComponent());
        LOGGER.debug("Total price for {}: {}", computer.getModel(), total);
        return total;
    }

    public static double calculateComponentPrice(ComputerComponent component) {
        if (component == null) {
            return 0.0d;
        }
        if (!component.isComposite()) {
            return component.getPrice();
        }
        double sum = 0.0d;
        for (ComputerComponent child : component.getChildren()) {
            sum += calculateComponentPrice(child);
        }
        return sum;
    }

    public static List<ComputerComponent> findByType(Computer computer, ComponentType type) {
        List<ComputerComponent> matches = new ArrayList<>();
        if (computer == null || computer.getRootComponent() == null || type == null) {
            LOGGER.warn("findByType called with insufficient input");
            return matches;
        }
        collectByType(computer.getRootComponent(), type, matches);
        LOGGER.debug("Found {} components of type {} in {}",
                matches.size(), type, computer.getModel());
        return matches;
    }

    public static int countLeafComponents(Computer computer) {
        if (computer == null || computer.getRootComponent() == null) {
            return 0;
        }
        int count = countLeaves(computer.getRootComponent());
        LOGGER.debug("Leaf count for {}: {}", computer.getModel(), count);
        return count;
    }

    public static ComputerComponent findMostExpensive(Computer computer) {
        if (computer == null || computer.getRootComponent() == null) {
            LOGGER.warn("findMostExpensive called with empty computer");
            return null;
        }
        ComputerComponent top = findMaxPriceLeaf(computer.getRootComponent(), null);
        LOGGER.debug("Most expensive component in {}: {}",
                computer.getModel(), top == null ? "none" : top.getName());
        return top;
    }

    private static void collectByType(ComputerComponent component,
                                      ComponentType type,
                                      List<ComputerComponent> acc) {
        if (component.getType() == type) {
            acc.add(component);
        }
        for (ComputerComponent child : component.getChildren()) {
            collectByType(child, type, acc);
        }
    }

    private static int countLeaves(ComputerComponent component) {
        if (!component.isComposite()) {
            return 1;
        }
        int count = 0;
        for (ComputerComponent child : component.getChildren()) {
            count += countLeaves(child);
        }
        return count;
    }

    private static ComputerComponent findMaxPriceLeaf(ComputerComponent component,
                                                      ComputerComponent current) {
        ComputerComponent best = current;
        if (!component.isComposite()) {
            if (best == null || component.getPrice() > best.getPrice()) {
                best = component;
            }
            return best;
        }
        for (ComputerComponent child : component.getChildren()) {
            best = findMaxPriceLeaf(child, best);
        }
        return best;
    }
}
