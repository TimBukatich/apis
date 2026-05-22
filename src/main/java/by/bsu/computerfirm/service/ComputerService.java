package by.bsu.computerfirm.service;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.Computer;
import by.bsu.computerfirm.entity.component.ComputerComponent;

import java.util.ArrayList;
import java.util.List;

public final class ComputerService {

    private ComputerService() {
    }

    public static double calculateTotalPrice(Computer computer) {
        if (computer == null || computer.getRootComponent() == null) {
            return 0.0d;
        }
        return calculateComponentPrice(computer.getRootComponent());
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
            return matches;
        }
        collectByType(computer.getRootComponent(), type, matches);
        return matches;
    }

    public static int countLeafComponents(Computer computer) {
        if (computer == null || computer.getRootComponent() == null) {
            return 0;
        }
        return countLeaves(computer.getRootComponent());
    }

    public static ComputerComponent findMostExpensive(Computer computer) {
        if (computer == null || computer.getRootComponent() == null) {
            return null;
        }
        return findMaxPriceLeaf(computer.getRootComponent(), null);
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
