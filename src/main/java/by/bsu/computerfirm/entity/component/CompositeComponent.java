package by.bsu.computerfirm.entity.component;

import by.bsu.computerfirm.entity.ComponentType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeComponent extends ComputerComponent {

    private final List<ComputerComponent> children;

    public CompositeComponent(String name, ComponentType type, double price) {
        super(name, type, price);
        this.children = new ArrayList<>();
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public List<ComputerComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public void add(ComputerComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("Component must not be null");
        }
        children.add(component);
    }

    @Override
    public void remove(ComputerComponent component) {
        children.remove(component);
    }

    @Override
    public CompositeComponent copy() {
        CompositeComponent clone = new CompositeComponent(getName(), getType(), getPrice());
        for (ComputerComponent child : children) {
            clone.add(child.copy());
        }
        return clone;
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        CompositeComponent that = (CompositeComponent) other;
        return children.equals(that.children);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + children.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CompositeComponent{name='").append(getName()).append('\'');
        builder.append(", type=").append(getType());
        builder.append(", price=").append(getPrice());
        builder.append(", children=").append(children);
        builder.append('}');
        return builder.toString();
    }
}
