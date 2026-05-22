package by.bsu.computerfirm.builder;

import by.bsu.computerfirm.entity.ComponentType;
import by.bsu.computerfirm.entity.Computer;
import by.bsu.computerfirm.entity.component.SimpleComponent;
import by.bsu.computerfirm.exception.ComponentValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComputerBuilderTest {

    private static final double DELTA = 0.0001d;

    @Test
    @DisplayName("Builder produces a fully populated Computer")
    void builderProducesPopulatedComputer() throws ComponentValidationException {
        Computer computer = new ComputerBuilder()
                .withModel("BSU Pro")
                .withProductionYear(2025)
                .withRootName("BSU Pro Tower")
                .withRootType(ComponentType.COMPUTER)
                .addComponent(new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel"))
                .addComponent(new SimpleComponent("Fury 16GB", ComponentType.RAM, 80d, "Kingston"))
                .build();

        assertEquals("BSU Pro", computer.getModel());
        assertEquals(2025, computer.getProductionYear());
        assertNotNull(computer.getRootComponent());
        assertEquals(2, computer.getRootComponent().getChildren().size());
        assertEquals(400d, computer.getRootComponent().getPrice(), DELTA);
    }

    @Test
    @DisplayName("Builder rejects invalid model")
    void builderRejectsInvalidModel() {
        ComputerBuilder builder = new ComputerBuilder();
        assertThrows(ComponentValidationException.class, () -> builder.withModel(""));
        assertThrows(ComponentValidationException.class, () -> builder.withModel(null));
    }

    @Test
    @DisplayName("Builder rejects invalid year")
    void builderRejectsInvalidYear() {
        ComputerBuilder builder = new ComputerBuilder();
        assertThrows(ComponentValidationException.class, () -> builder.withProductionYear(1500));
        assertThrows(ComponentValidationException.class, () -> builder.withProductionYear(3000));
    }

    @Test
    @DisplayName("Builder rejects invalid component")
    void builderRejectsInvalidComponent() {
        ComputerBuilder builder = new ComputerBuilder();
        SimpleComponent bad =
                new SimpleComponent("", ComponentType.CPU, -10d, "Intel");
        assertThrows(ComponentValidationException.class, () -> builder.addComponent(bad));
    }

    @Test
    @DisplayName("Builder requires model before build")
    void builderRequiresModelBeforeBuild() {
        ComputerBuilder builder = new ComputerBuilder();
        assertThrows(ComponentValidationException.class, builder::build);
    }

    @Test
    @DisplayName("Builder requires production year before build")
    void builderRequiresYearBeforeBuild() throws ComponentValidationException {
        ComputerBuilder builder = new ComputerBuilder().withModel("BSU");
        assertThrows(ComponentValidationException.class, builder::build);
    }

    @Test
    @DisplayName("Builder supports method chaining and reset")
    void builderResetWorks() throws ComponentValidationException {
        ComputerBuilder builder = new ComputerBuilder()
                .withModel("BSU")
                .withProductionYear(2025)
                .addComponent(new SimpleComponent("CPU", ComponentType.CPU, 100d, "Intel"));

        builder.reset();
        assertThrows(ComponentValidationException.class, builder::build);
    }

    @Test
    @DisplayName("Builder accepts a batch of components via addComponents")
    void builderAcceptsBatch() throws ComponentValidationException {
        Computer computer = new ComputerBuilder()
                .withModel("BSU")
                .withProductionYear(2025)
                .addComponents(java.util.Arrays.asList(
                        new SimpleComponent("CPU", ComponentType.CPU, 100d, "Intel"),
                        new SimpleComponent("RAM", ComponentType.RAM, 50d, "Kingston")))
                .build();

        assertEquals(2, computer.getRootComponent().getChildren().size());
        assertTrue(computer.getRootComponent().getPrice() > 0d);
    }
}
