package by.bsu.computerfirm;

import by.bsu.computerfirm.builder.ComputerBuilder;
import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.Computer;
import by.bsu.computerfirm.entity.component.ComputerComponent;
import by.bsu.computerfirm.entity.component.SimpleComponent;
import by.bsu.computerfirm.exception.ComponentReaderException;
import by.bsu.computerfirm.exception.ComponentValidationException;
import by.bsu.computerfirm.observer.Client;
import by.bsu.computerfirm.observer.ComponentStore;
import by.bsu.computerfirm.parser.ComponentParser;
import by.bsu.computerfirm.reader.ComponentReader;
import by.bsu.computerfirm.service.ComputerService;

import java.util.EnumSet;
import java.util.List;

public final class Main {

    private static final String DATA_FILE_PATH = "src/main/resources/data/components.txt";

    private Main() {
    }

    public static void main(String[] args) {
        try {
            List<String> rawLines = ComponentReader.readNonEmptyLines(DATA_FILE_PATH);
            System.out.println("Lines read from file: " + rawLines.size());

            List<ComputerComponent> components = ComponentParser.parseLines(rawLines);
            System.out.println("Components parsed successfully: " + components.size());
            System.out.println("Invalid lines skipped: " + (rawLines.size() - components.size()));

            ComponentStore store = new ComponentStore("BSU Computers");
            Client retailClient = new Client("Retail Client");
            Client gamingClient = new Client(
                    "Gaming Client",
                    EnumSet.of(ComponentType.GPU, ComponentType.CPU, ComponentType.RAM));
            store.attach(retailClient);
            store.attach(gamingClient);

            for (ComputerComponent component : components) {
                store.addNewComponent(component);
            }

            System.out.println();
            System.out.println("Retail Client received notifications: "
                    + retailClient.getReceivedNotifications().size());
            System.out.println("Gaming Client received notifications: "
                    + gamingClient.getReceivedNotifications().size());

            Computer baseConfig = buildBaseConfiguration(components);
            System.out.println();
            System.out.println("Base configuration:");
            System.out.println(baseConfig);
            System.out.println("Total price: " + ComputerService.calculateTotalPrice(baseConfig));
            System.out.println("Total leaf components: "
                    + ComputerService.countLeafComponents(baseConfig));

            Computer cloned = baseConfig.copy();
            cloned.setModel("BSU Pro Clone");
            cloned.setProductionYear(2026);
            System.out.println();
            System.out.println("Cloned configuration:");
            System.out.println(cloned);

            System.out.println();
            System.out.println("Most expensive component in base config: "
                    + ComputerService.findMostExpensive(baseConfig));

            List<ComputerComponent> cpus = ComputerService.findByType(baseConfig, ComponentType.CPU);
            System.out.println("CPUs in base config: " + cpus.size());
        } catch (ComponentReaderException e) {
            System.err.println("Failed to read components: " + e.getMessage());
        } catch (ComponentValidationException e) {
            System.err.println("Failed to build configuration: " + e.getMessage());
        }
    }

    private static Computer buildBaseConfiguration(List<ComputerComponent> available)
            throws ComponentValidationException {
        ComputerBuilder builder = new ComputerBuilder()
                .withModel("LEHA")
                .withProductionYear(2026)
                .withRootName("LEHA PRO")
                .withRootType(ComponentType.COMPUTER);

        ComputerComponent cpu = firstOfType(available, ComponentType.CPU);
        ComputerComponent ram = firstOfType(available, ComponentType.RAM);
        ComputerComponent motherboard = firstOfType(available, ComponentType.MOTHERBOARD);
        ComputerComponent psu = firstOfType(available, ComponentType.PSU);
        ComputerComponent gpu = firstOfType(available, ComponentType.GPU);

        if (cpu != null) {
            builder.addComponent(cpu);
        }
        if (ram != null) {
            builder.addComponent(ram);
        }
        if (motherboard != null) {
            builder.addComponent(motherboard);
        }
        if (psu != null) {
            builder.addComponent(psu);
        }
        if (gpu != null) {
            builder.addComponent(gpu);
        }
        if (cpu == null) {
            builder.addComponent(new SimpleComponent(
                    "Fallback CPU", ComponentType.CPU, 199.99, "Generic"));
        }
        if (ram == null) {
            builder.addComponent(new SimpleComponent(
                    "Fallback RAM", ComponentType.RAM, 79.99, "Generic"));
        }
        if (motherboard == null) {
            builder.addComponent(new SimpleComponent(
                    "Fallback Motherboard", ComponentType.MOTHERBOARD, 149.99, "Generic"));
        }
        if (psu == null) {
            builder.addComponent(new SimpleComponent(
                    "Fallback PSU", ComponentType.PSU, 89.99, "Generic"));
        }
        return builder.build();
    }

    private static ComputerComponent firstOfType(List<ComputerComponent> components,
                                                 ComponentType type) {
        for (ComputerComponent component : components) {
            if (component.getType() == type) {
                return component;
            }
        }
        return null;
    }
}
