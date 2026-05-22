package by.bsu.computerfirm.entity.component;

import by.bsu.computerfirm.entity.ComponentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CompositeComponentTest {

    @Test
    @DisplayName("Composite reports isComposite true")
    void compositeReportsItself() {
        CompositeComponent composite =
                new CompositeComponent("Motherboard", ComponentType.MOTHERBOARD, 200d);
        assertTrue(composite.isComposite());
    }

    @Test
    @DisplayName("Composite stores and lists children")
    void compositeStoresChildren() {
        CompositeComponent composite =
                new CompositeComponent("Motherboard", ComponentType.MOTHERBOARD, 200d);
        SimpleComponent cpu =
                new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel");
        SimpleComponent ram =
                new SimpleComponent("Fury", ComponentType.RAM, 80d, "Kingston");

        composite.add(cpu);
        composite.add(ram);

        assertEquals(2, composite.getChildren().size());
        assertSame(cpu, composite.getChildren().get(0));
        assertSame(ram, composite.getChildren().get(1));
    }

    @Test
    @DisplayName("Composite remove deletes the requested child")
    void compositeRemoveDeletesChild() {
        CompositeComponent composite =
                new CompositeComponent("PC", ComponentType.COMPUTER, 0d);
        SimpleComponent cpu =
                new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel");
        SimpleComponent ram =
                new SimpleComponent("Fury", ComponentType.RAM, 80d, "Kingston");
        composite.add(cpu);
        composite.add(ram);

        composite.remove(cpu);

        assertEquals(1, composite.getChildren().size());
        assertSame(ram, composite.getChildren().get(0));
    }

    @Test
    @DisplayName("Composite rejects null children")
    void compositeRejectsNullChild() {
        CompositeComponent composite =
                new CompositeComponent("PC", ComponentType.COMPUTER, 0d);
        assertThrows(IllegalArgumentException.class, () -> composite.add(null));
    }

    @Test
    @DisplayName("getChildren returns an immutable view")
    void getChildrenIsImmutable() {
        CompositeComponent composite =
                new CompositeComponent("PC", ComponentType.COMPUTER, 0d);
        composite.add(new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel"));

        assertThrows(UnsupportedOperationException.class,
                () -> composite.getChildren().add(
                        new SimpleComponent("X", ComponentType.RAM, 5d, "X")));
    }

    @Test
    @DisplayName("copy() produces deep clone with independent children")
    void copyProducesDeepClone() {
        CompositeComponent original =
                new CompositeComponent("PC", ComponentType.COMPUTER, 100d);
        SimpleComponent cpu =
                new SimpleComponent("Core i7", ComponentType.CPU, 320d, "Intel");
        original.add(cpu);

        CompositeComponent clone = original.copy();

        assertEquals(original, clone);
        assertNotSame(original, clone);
        assertEquals(1, clone.getChildren().size());
        assertNotSame(cpu, clone.getChildren().get(0));

        clone.getChildren().get(0).setPrice(999d);
        assertEquals(320d, cpu.getPrice(), 0.0001d);
    }

    @Test
    @DisplayName("Composites with same data are equal")
    void compositesWithSameDataAreEqual() {
        CompositeComponent a = new CompositeComponent("PC", ComponentType.COMPUTER, 0d);
        CompositeComponent b = new CompositeComponent("PC", ComponentType.COMPUTER, 0d);
        a.add(new SimpleComponent("CPU", ComponentType.CPU, 100d, "Intel"));
        b.add(new SimpleComponent("CPU", ComponentType.CPU, 100d, "Intel"));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("Composite different children breaks equality")
    void compositeDifferentChildrenBreaksEquality() {
        CompositeComponent a = new CompositeComponent("PC", ComponentType.COMPUTER, 0d);
        CompositeComponent b = new CompositeComponent("PC", ComponentType.COMPUTER, 0d);
        a.add(new SimpleComponent("CPU A", ComponentType.CPU, 100d, "Intel"));
        b.add(new SimpleComponent("CPU B", ComponentType.CPU, 100d, "Intel"));

        assertFalse(a.equals(b));
    }
}
